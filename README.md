<div align="center">
  <img src="boomerang/src/main/resources/static/esset/footer.png" alt="Footer" width="80%">
  <br><br>
  <img src="boomerang/src/main/resources/static/esset/logo.png" alt="Logo" width="30%">
</div>

## 🎯 목차
- [프로젝트 소개](#프로젝트-소개)
- [기술 스택](#기술-스택)
- [주요 기능별 설명](#주요-기능별-설명)
- [API 명세서](#api-명세서)
- [프로젝트 구조](#프로젝트-구조)
- [배포된 인스턴스 주소](#배포된-인스턴스-주소)
- [문서화 자료](#문서화-자료)
- [기간](#기간)
- [라이센스](#라이센스)
- [팀원 소개](#팀원-소개)

## 📢 프로젝트 소개
**부메랑**은 전세사기 피해자들을 위한 **전세사기 대처 웹 플랫폼**입니다.

집주인에게 간 보증금이 돌아오는 그날까지 함께하는 메이트가 되어드리겠습니다.

### 📝 기획 배경
2024년 8월 기준 전세사기 피해자는 2만명을 돌파했으며, 보증보험이 가입되어 있는 경우에는 국교교통부 전세사기 피해자로 인정이 안되어, 실제 피해자는 이보다 훨씬 많을 것으로 추정됩니다.

피해자들이 겪는 주요 어려움은 다음과 같습니다.  
- 복잡한 법률 용어와 절차에 대한 이해 부족
- 각종 서류 준비와 작성에 대한 심리적 부담
- 필요한 정보가 여러 사이트에 분산되어 있음
- 피해자들간의 정보 공유 커뮤니티 부재

### 💡 프로젝트 가치
 부메랑은 전세사기 피해자들이 환급 과정에서 겪는 복잡한 절차와 정보 부족의 어려움을 덜기 위해 개발되었습니다.     
 피해자들은 환급을 위해 전세사기 유형을 파악하고, 법적 용어를 이해하는 등 추가적인 피로를 겪습니다. 따라서 피해자들이 환급 과정에만 집중할 수 있도록, 유형 파악부터 환급까지 필요한 기능을 제공하고자 노력했습니다.     
 또한, 채팅과 상담, 서류 작성 등 피해자들에게 실질적인 도움이 되는 기능들을 지속적으로 고민하며 발전시켰습니다.

### 🛠 문제 해결
**부메랑**은 피해자들이 복잡한 법적 절차를 쉽게 이해하고 진행할 수 있도록 도와드립니다:

- 전세사기 유형 검사와 맞춤형 가이드라인 제공
- 서류 자동 완성 기능
- 쉬운 법률 용어 설명 
- 전세사기 예방 안전도 검사
- 지식인 채팅 상담 서비스
- 피해자 커뮤니티 플랫폼

사용자들은 별도의 검색이나 사이트 방문 없이 부메랑 내에서 모든 과정을 일관되게 진행할 수 있습니다.

## ⚙️ 기술 스택
| 분류 | 기술 |
|------|------|
| **Backend** | Java 21, Spring Boot 3.3.1, Spring Security, Spring Data JPA, Spring Batch, Spring WebSocket, Spring Mail |
| **Database & Cache** | MySQL, Redis |
| **Cloud & Infrastructure** | AWS S3, Azure Database for MySQL, Azure Cache for Redis |
| **Document Processing** | Apache PDFBox 3.0.0, Jsoup 1.18.1 |
| **Security** | JWT, Spring Security |
| **Tools & Libraries** | Lombok, Log4j2, Guava, Thymeleaf |

## 🔧 주요 기능별 설명

1. 전세사기 유형 검사와 맞춤형 가이드라인 제공
- 제공 가치
  - 유형검사, 해결방법, 서류 작성까지 원스톱 서비스
  - 필수 외부 사이트 직접 링크 제공
  - 유형별 맞춤 단계별 상세 안내

- 기능 설명
  - 계약 종류/보험 가입여부 기반 유형 검사
  - 유형별 맞춤형 환급 과정 안내
  - 단계별 진행도 체크 관리
  - 상세 설명 및 필요 서류 안내

2. 서류 자동 완성 기능
- 제공 가치
  - 법률 서류 작성 부담 해소
  - 크로스 플랫폼 서류 작성/다운로드
  - 오류 없는 정확한 서류 작성

- 기능 설명
  - 맞춤형 입력 필드와 템플릿 제공
  - 입력 정보 기반 서류 자동 완성
  - PDF 형식 제공

3. 전세사기 예방 안전도 검사
- 제공 가치
  - 사전 위험 파악
  - 객관적 안전성 검증
  - 추가 확인사항 정보 제공

- 기능 설명
  - 위험도 점수화 평가    
  `(집값*0.8 - 채권액 - 전세보증금 > 0)`

4. 지식인 채팅 상담
- 제공 가치
  - 실시간 전문가 상담
  - 맞춤형 개인 상황 조언
  - 전문적 법률 절차 안내

- 기능 설명
  - Socket 기반 실시간 채팅
  - 유연한 상담 시간 관리
  - 상담 내역 저장/조회

5. 피해자 커뮤니티 플랫폼
- 제공 가치
  - 피해자간 정보 공유 및 심리적 지지
  - 실제 경험 기반 해결방안 공유
  - 안전한 소통 공간 제공
  - 맞춤형 정보 접근성

- 기능 설명
  - 목적별 게시판 (자유/지역별/시크릿/단계별)
  - 베스트 게시글 시스템
      - 4시간 주기 선정
      - 7일 유효기간
      - 점수 = 좋아요(×10) + 댓글(×7)
  - 전화번호 필터링
  - 게시글/댓글/좋아요
  - 카테고리 분류 및 검색

## 📜 API 명세서
- API의 상세한 사용법과 엔드포인트에 대한 정보는 아래 링크에서 확인할 수 있습니다
- [Boomerang API 명세서](https://documenter.getpostman.com/view/29615301/2sAXxY4oTG)

## 🏗 프로젝트 구조
### 시스템 아키텍쳐
![시스템 아키텍처](boomerang/src/main/resources/static/esset/architecture.png)
+ ERD

## 🌐 배포된 인스턴스 주소
- BE: http://52.79.80.3:8080
- FE: http://54.252.224.76  


## 📚 문서화 자료
- 회의 자료
    - [CustomRepository 사용](docs/discussion_notes/CustomRepository%20사용.md)
    - [DB 운영 방식 결정](docs/discussion_notes/DB%20운영%20방식%20결정.md)
    - [Embedded 사용 방식](docs/discussion_notes/Embedded%20사용%20방식.md)
    - [개발이 되지 않은 Entity를 사용하여 개발하여아 하는 경우 개발 방식 결정](docs/discussion_notes/개발이%20되지%20않은%20Entity를%20사용하여%20개발하여아%20하는%20경우%20개발%20방식%20결정.md)
    - [게시판 URL에 슬러그 사용](docs/discussion_notes/게시판%20URL에%20슬러그%20사용.md)
    - [도메인 변환 메서드 통일](docs/discussion_notes/도메인%20변환%20메서드%20통일.md)
- 멘토링 자료
    - [3주차_멘토링](docs/mentoring/3주차_멘토링.md)
    - [8주차_멘토링](docs/mentoring/8주차_멘토링.md)
    - [11주차_멘토링](docs/mentoring/11주차_멘토링.md)

## 📅 기간
25 Aug 2024 ~ 15 Nov 2024

## 📄 라이센스
MIT License - Copyright (c) 2024 kakao-tech-campus-2nd-step3

## 🧑‍🤝‍🧑 팀원 소개
<div align="center">
 <table>
   <tr>
     <td align="center" width="25%">
       <img src="https://avatars.githubusercontent.com/u/121755257?s=64&v=4" width="100" height="100" alt="진서현 프로필"><br>
       <a href="https://github.com/jinseohyun1228" target="_blank">진서현</a><br>
     </td>
     <td align="center" width="25%">
       <img src="https://avatars.githubusercontent.com/u/150018566?s=64&v=4" width="100" height="100" alt="문성민 프로필"><br>
       <a href="https://github.com/Dalsungmin" target="_blank">문성민</a><br>
     </td>
     <td align="center" width="25%">
       <img src="https://avatars.githubusercontent.com/u/65036351?s=64&v=4" width="100" height="100" alt="정재빈 프로필"><br>
       <a href="https://github.com/JaeBin2019" target="_blank">정재빈</a><br>
     </td>
     <td align="center" width="25%">
       <img src="https://avatars.githubusercontent.com/u/55781137?v=4" width="100" height="100" alt="이상준 프로필"><br>
       <a href="https://github.com/J-1ac" target="_blank">이상준</a><br>
     </td>
   </tr>
 </table>
</div>
