# main.py
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from dotenv import load_dotenv
from langchain_chroma import Chroma
from langchain_huggingface import HuggingFaceEmbeddings
from langchain_openai import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import StrOutputParser
from langchain_core.runnables import RunnablePassthrough
import time

load_dotenv()

app = FastAPI()


class ChatRequest(BaseModel):
    question: str


class ChatResponse(BaseModel):
    answer: str


class EndToEndRAG:
    def __init__(self, embedding_model, db_path, k=7, llm_model="gpt-3.5-turbo"):
        self.embeddings = HuggingFaceEmbeddings(model_name=embedding_model)
        self.vectordb = Chroma(persist_directory=db_path, embedding_function=self.embeddings)
        self.k = k
        self.llm = ChatOpenAI(model=llm_model, temperature=0, max_tokens=500)

        self.prompt = ChatPromptTemplate.from_messages([
            ("system", """당신은 전월세 사기 피해 지원 서비스 '부메랑'의 AI 어시스턴트입니다.
주어진 문서를 바탕으로 정확하고 간결하게 답변을 제공하세요.

문서:
{context}"""),
            ("human", "{question}")
        ])

        self.chain = (
                {"context": self.retrieve_context, "question": RunnablePassthrough()}
                | self.prompt
                | self.llm
                | StrOutputParser()
        )

    def retrieve_context(self, question):
        docs = self.vectordb.similarity_search(question, k=self.k)
        context = "\n\n".join([d.page_content for d in docs])
        print(f"\n{'=' * 60}")
        print(f"검색된 문서:\n{context}")
        print(f"{'=' * 60}\n")
        return context

    def generate_answer(self, question):
        return self.chain.invoke(question)


rag_system = None


@app.on_event("startup")
async def startup_event():
    global rag_system
    print("RAG 시스템 초기화 중...")
    rag_system = EndToEndRAG(
        embedding_model="nlpai-lab/KURE-v1",
        db_path="./kure_chroma_db",
        k=7,
        llm_model="gpt-3.5-turbo"
    )
    print("RAG 시스템 초기화 완료!")


@app.post("/chat", response_model=ChatResponse)
async def chat(request: ChatRequest):
    try:
        start = time.time()
        answer = rag_system.generate_answer(request.question)
        elapsed = time.time() - start

        print(f"처리 시간: {elapsed:.2f}초")

        return ChatResponse(answer=answer)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@app.get("/health")
async def health():
    return {"status": "ok"}


@app.get("/")
async def root():
    return {"message": "부메랑 LLM 서버가 실행중입니다"}