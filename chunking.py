# chunking.py
# chunking.py
from langchain_text_splitters import (
    MarkdownHeaderTextSplitter,
    RecursiveCharacterTextSplitter
)

import os
import json
from pathlib import Path

global i


class MarkdownChunker:
    def __init__(self, chunk_size=800, chunk_overlap=80):
        """
        Args:
            chunk_size: 한 청크의 최대 크기 (글자 수)
            chunk_overlap: 청크 간 오버랩 크기 (10% = 80자)
        """
        self.chunk_size = chunk_size
        self.chunk_overlap = chunk_overlap

        # 헤더 기반 분리기
        self.headers_to_split_on = [
            ("#", "제목"),
            ("##", "대분류"),
        ]

        self.markdown_splitter = MarkdownHeaderTextSplitter(
            headers_to_split_on=self.headers_to_split_on
        )

        # 크기 조정용 분리기
        self.text_splitter = RecursiveCharacterTextSplitter(
            chunk_size=self.chunk_size,
            chunk_overlap=self.chunk_overlap,
            separators=["\n\n", "\n", ".", "!", "?", " ", ""],
            length_function=len,
        )

    # 제목과 부제목은 제외
    def chunk_file_1(self, file_path):
        """단일 마크다운 파일을 청킹"""
        print(f"처리 중: {file_path}")

        file_name = os.path.basename(file_path)

        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        # 1단계: 헤더 단위로 먼저 분리
        try:
            md_chunks = self.markdown_splitter.split_text(content)
        except Exception as e:
            print(f"헤더 분리 실패, 텍스트 분리로 전환: {e}")
            md_chunks = [{"page_content": content, "metadata": {}}]

        # 2단계: 크기 조정
        final_chunks = []
        for i, chunk in enumerate(md_chunks):
            chunk_content = chunk.page_content if hasattr(chunk, 'page_content') else chunk
            chunk_metadata = chunk.metadata if hasattr(chunk, 'metadata') else {}

            base_id = f"{file_name}_{i}"

            # 청크가 너무 크면 재분할
            if len(chunk_content) > self.chunk_size:
                sub_chunks = self.text_splitter.split_text(chunk_content)
                for j, sub in enumerate(sub_chunks):
                    final_chunks.append({
                        'content': sub,
                        'metadata': {
                            'source': file_name,
                            'chunk_id': f"{base_id}_{j}",
                            **chunk_metadata
                        }
                    })
            else:
                final_chunks.append({
                    'content': chunk_content,
                    'metadata': {
                        'source': file_name,
                        'chunk_id': base_id,
                        **chunk_metadata
                    }
                })

        return final_chunks

    def chunk_file(self, file_path):
        """단일 마크다운 파일을 청킹하며 고유한 ID 부여"""
        print(f"처리 중: {file_path}")

        # 파일명을 ID의 일부로 사용하기 위해 추출
        file_name = os.path.basename(file_path)

        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()

        try:
            md_chunks = self.markdown_splitter.split_text(content)
        except Exception as e:
            print(f"헤더 분리 실패: {e}")
            md_chunks = [{"page_content": content, "metadata": {}}]

        final_chunks = []
        for i, chunk in enumerate(md_chunks):
            chunk_content = chunk.page_content if hasattr(chunk, 'page_content') else chunk
            chunk_metadata = chunk.metadata if hasattr(chunk, 'metadata') else {}

            header_prefix = ""
            title = chunk_metadata.get('제목', '')
            category = chunk_metadata.get('대분류', '')

            if title or category:
                header_prefix = f"{title} {category} ".strip() + " "

            processed_content = header_prefix + chunk_content

            # 고유 ID 생성을 위한 베이스 ID (파일명 + 헤더순서)
            base_id = f"{file_name}_{i}"
            # base_id = f"{i}"

            if len(processed_content) > self.chunk_size:
                sub_chunks = self.text_splitter.split_text(processed_content)
                for j, sub in enumerate(sub_chunks):
                    final_chunks.append({
                        'content': sub,
                        'metadata': {
                            'source': file_name,
                            'chunk_id': f"{base_id}_{j}",  # 예: document.md_0_1
                            **chunk_metadata
                        }
                    })
            else:
                final_chunks.append({
                    'content': processed_content,
                    'metadata': {
                        'source': file_name,
                        'chunk_id': base_id,  # 예: document.md_0
                        **chunk_metadata
                    }
                })

        return final_chunks




    def chunk_directory(self, directory_path="문서"):
        """디렉토리 내 모든 .md 파일 청킹"""
        all_chunks = []

        # 모든 .md 파일 찾기
        base_path = Path(__file__).parent / directory_path
        print(base_path)

        if not base_path.exists():
            print(f"오류: {base_path} 경로를 찾을 수 없습니다.")
            return []

        md_files = list(base_path.glob("*.md"))

        print(f"\n총 {len(md_files)}개의 마크다운 파일을 찾았습니다.")

        for file_path in md_files:
            chunks = self.chunk_file(file_path)
            all_chunks.extend(chunks)
            print(f"  → {len(chunks)}개 청크 생성")

        print(f"\n총 {len(all_chunks)}개의 청크가 생성되었습니다.")
        return all_chunks

    def save_chunks(self, chunks, output_file="json/chunks.json"):
        """청크를 JSON 파일로 저장"""
        with open(output_file, 'w', encoding='utf-8') as f:
            json.dump(chunks, f, ensure_ascii=False, indent=2)
        print(f"\n청크가 {output_file}에 저장되었습니다.")

    def print_chunk_stats(self, chunks):
        """청크 통계 출력"""
        if not chunks:
            return

        chunk_lengths = [len(chunk['content']) for chunk in chunks]
        print("\n=== 청킹 통계 ===")
        print(f"총 청크 수: {len(chunks)}")
        print(f"평균 길이: {sum(chunk_lengths) / len(chunk_lengths):.0f}자")
        print(f"최소 길이: {min(chunk_lengths)}자")
        print(f"최대 길이: {max(chunk_lengths)}자")

        # 파일별 통계
        from collections import Counter
        sources = Counter([chunk['metadata']['source'] for chunk in chunks])
        print("\n파일별 청크 수:")
        for source, count in sources.items():
            print(f"  - {source}: {count}개")


def main():
    # 청커 초기화
    chunker = MarkdownChunker(
        chunk_size=1000,  # 최대 800자
        chunk_overlap=80  # 10% 오버랩
    )

    # 현재 디렉토리의 모든 .md 파일 청킹
    chunks = chunker.chunk_directory()

    # 통계 출력
    chunker.print_chunk_stats(chunks)

    # JSON 파일로 저장
    chunker.save_chunks(chunks, "json/chunks.json")

    # 첫 3개 청크 미리보기
    print("\n=== 첫 3개 청크 미리보기 ===")
    for i, chunk in enumerate(chunks[:3]):
        print(f"\n[청크 {i + 1}]")
        print(f"파일: {chunk['metadata']['source']}")
        print(f"메타데이터: {chunk['metadata']}")
        print(f"내용 (처음 200자):\n{chunk['content'][:200]}...")


if __name__ == "__main__":
    main()
