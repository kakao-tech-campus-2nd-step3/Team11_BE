<div align="center">
  <img src="boomerang/src/main/resources/static/esset/footer.png" alt="Footer" width="80%">
  <br><br>
  <img src="boomerang/src/main/resources/static/esset/logo.png" alt="Logo" width="30%">
</div>

## 🎯 목차
- [프로젝트 소개](#프로젝트-소개)
- [기술 스택](#기술-스택)
- [문서화 자료](#문서화-자료)
- [주요 기능별 설명](#주요-기능별-설명)
- [API 명세서](#api-명세서)
- [프로젝트 구조](#프로젝트-구조)
- [배포된 인스턴스 주소](#배포된-인스턴스-주소)
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
 또한, 채팅과 상담, 서류 자동 완성 등 피해자들에게 실질적인 도움이 되는 기능들을 지속적으로 고민하며 발전시켰습니다.

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

## 📚 문서화 자료
- 회의 자료 
  - [CustomRepository 사용](docs/discussion_notes/CustomRepository%20사용.md)   
    <br>
    **💡 회의 배경**  
    >프로젝트의 데이터 접근 계층 설계에 있어 명시적이고 경량화된 Repository 구조를 도입할지 결정 필요
    
    **📋 논의 안건**  
    >JpaRepository vs CustomRepository 도입 검토
    
    **📝 주요 내용**    
    >CustomRepository의 장점: 명시적 메서드 선언, 코드 경량화, 데이터 접근 패턴 통일성    
    CustomRepository의 단점: 반복적인 메서드 선언, 개발 생산성 저하, 동적 프록시 제한
    
    **✅ 결론**  
    >개발 생산성과 유지보수성을 고려하여 JpaRepository 사용 결정

  - [DB 운영 방식 결정](docs/discussion_notes/DB%20운영%20방식%20결정.md)   
    <br>
    **💡 회의 배경**      
    >개발 및 운영 환경에서의 효율적인 데이터베이스 운영 방식 결정 필요
    
    **📋 논의 안건**      
    >개발 단계와 운영 단계에서의 데이터베이스 운영 전략 수립    
    >1. H2 + 배포 DB 방식    
    >2. 개발용/운영용 배포 DB 분리 방식    
    >3. 단일 배포 DB 사용 방식    
    
    **📝 주요 내용**    
    >1. H2 사용: 빠른 개발 속도와 스키마 유연성 장점, 환경 일관성 부재 단점    
    >2. 개발/운영 DB 분리: 안전한 개발 환경 보장, 관리 부담 증가    
    >3. 단일 DB 사용: 간편한 관리, 운영 데이터 위험 노출 가능성    
    
    **✅ 결론**  
    >초기 개발은 H2를 사용하고, 추후 dev MySQL로의 전환을 고려하는 단계적 접근 방식 채택

  - [Embedded 사용 방식](docs/discussion_notes/Embedded%20사용%20방식.md)
    <br>
    **💡 회의 배경**  
    >엔티티의 필드 설계에서 @Embedded 어노테이션의 적절한 사용 범위와 방식 결정 필요    
    
    **📋 논의 안건**
    >1. 모든 필드에 @Embedded 적용 vs 선택적 적용     
    >2. 객체 생성 시점의 유효성 검증 전략    
    >3. 도메인 모델의 복잡도와 유지보수성 간의 균형점 모색     
    
    **📝 주요 내용**
    >1. 전체 필드 @Embedded 적용: 일관된 유효성 검사 가능, 과도한 복잡도 유발    
    >2. 선택적 적용: 복잡한 객체(주소 등)에만 적용, 적절한 복잡도 유지    
    >3. 유효성 검사는 API 요청 레벨에서 처리하여 도메인 로직 단순화    
    
    **✅ 결론**  
    >복잡한 구조를 가진 필드에만 선택적으로 @Embedded를 적용하여 유지보수성과 도메인 모델의 명확성 확보    

  - [개발이 되지 않은 Entity를 사용하여 개발하여아 하는 경우 개발 방식 결정](docs/discussion_notes/개발이%20되지%20않은%20Entity를%20사용하여%20개발하여아%20하는%20경우%20개발%20방식%20결정.md)
    <br>
    **💡 회의 배경**      
    >연관된 엔티티가 아직 개발되지 않은 상황에서의 효율적인 개발 방식 결정 필요
    
    **📋 논의 안건**      
    >미개발 엔티티 의존성이 있는 기능 개발 시 접근 방식    
    >1. 임시 엔티티 생성 후 개발
    >2. 단순 구조로 우선 개발
    >3. 예상 도메인 기반 선개발
    
    **📝 주요 내용**
    >1. 임시 엔티티 방식: 빠른 개발 가능, 코드 충돌 위험    
    >2. 단순 구조 방식: 안정적 개발, 개발 속도 저하    
    >3. 예상 도메인 방식: 빠른 개발, 리팩토링 부담    
    >4. 멘토링: 핵심 도메인 스키마 팀 정의 후 개발 권장    
    
    **✅ 결론**  
    >현재 진행 중인 개발은 유지하되, 향후 새로운 개발 시 핵심 도메인 스키마를 팀 단위로 정의 후 개발하는 방식 채택

  - [게시판 URL에 슬러그 사용](docs/discussion_notes/게시판%20URL에%20슬러그%20사용.md)
    <br>
    **💡 회의 배경**  
    >게시판 URL의 가독성 향상과 SEO 최적화를 위한 슬러그(Slug) 도입 검토
    
    **📋 논의 안건**
    >게시판 URL에 슬러그 적용 여부
    슬러그 생성 및 관리 정책
    RESTful 규칙과의 호환성
    
    **📝 주요 내용**
    >슬러그 유니크성: 중복 제목에 대한 넘버링 처리 (/제목-(1))    
    제목 변경 정책: 슬러그 동기화 vs 독립성 유지    
    구현 방식: `@GetMapping("/{idOrSlug}")` 활용 가능    
    RESTful 규칙 준수 가능성 확인    
    
    **✅ 결론**  
    >프론트엔드 구현 방식 검토 후 슬러그 도입 여부 최종 결정

  - [도메인 변환 메서드 통일](docs/discussion_notes/도메인%20변환%20메서드%20통일.md)
    <br>
    **💡 회의 배경**  
    >도메인 객체 변환 로직의 일관성 있는 구현 방식 정립 필요
    
    **📋 논의 안건**  
    >도메인 변환 메서드 구현 방식 선택
    >1. 서비스 레이어 직접 변환
    >2. DTO 내부 변환 메서드
    >3. 도메인 생성자 활용
    >4. 팩토리 패턴 적용
    
    **📝 주요 내용**
    >서비스 레이어 변환: 단순하나 코드 중복 발생   
    DTO 변환: 응집도 높으나 중복 로직 발생   
    도메인 생성자: 깔끔한 구현, 도메인 복잡도 증가   
    팩토리 패턴: 중복 제거, 추가 클래스 필요   
    ➡️멘토링: Mapper 클래스 활용 권장   
    
    **✅ 결론**  
    >팀 투표를 통해 도메인 내부 생성자를 활용한 변환 방식 채택

- 멘토링 자료
  - [3주차_멘토링](docs/mentoring/3주차_멘토링.md)   
    **🤔 질문 사항**
      1. MyCrudRepository 커스텀 구현의 실무 적합성
      2. 로깅 프레임워크 선택 및 활용 방안
      3. API 응답 구조의 단순화 방법
      4. 일급 컬렉션 도입의 적절성 검토
      5. 실무 환경에서의 개발 협업 프로세스
  - [8주차_멘토링](docs/mentoring/8주차_멘토링.md)    
    **🤔 질문 사항**
      1. 베스트 게시글 선정을 위한 최적의 조건식 구성 방안
      2. 진행도 타입별 처리를 위한 설계 패턴 선택 (인터페이스 vs 전략패턴)
      3. 사용자 인증 정보의 서비스 계층 전달 방식 (PrincipalDetails vs Email)
      4. 컨트롤러 응답 시 도메인 객체 반환 범위
      5. 페이지네이션 응답 객체의 구조화 방안     
  - [11주차_멘토링](docs/mentoring/11주차_멘토링.md)    
    **🤔 질문 사항**
       1. 상담 상태 변경 로직의 최적화 방안 (조회 시 업데이트 vs 스케줄러)
       2. 상담 Entity에서의 멘토 참조 구조 설계 (Member vs Mentor)
       3. 현재 프로젝트 코드의 개선점 분석
       4. 프로젝트 성능 개선 포인트 도출
       5. 코딩 테스트 준비 전략
       6. 개발자 지원 시 자기소개서 작성 방법
       7. CS 학습 리소스 추천
  
## 🔧 주요 기능별 설명

### 1. 전세사기 유형 검사와 맞춤형 가이드라인 제공
**🏆 유형검사, 해결방법, 서류 작성까지 한 번에 하는 원스톱 서비스 제공**

- 📝 주요 기능
  - 계약 종류/보험 가입여부 기반 유형 검사
  - 유형별 맞춤형 환급 과정 안내
  - 단계별 진행도 체크 관리
  - 상세 설명 및 필요 서류 안내

- 🔍 트러블 슈팅  
  **진행도 타입을 구분 할 때 인터페이스와 전략클래스 비교:**
    4가지 타입별로 메인단계가 중복 될 수 있고, 메인단계는 여러개의 서브단계로 저장되는데
    각 메인 단계의 서브 단계의 완료 여부를 저장해야함
  - 해결 방안:
    - 인터페이스로 구현시 타입 안정성은 좋지만 나중에 기능 추가, 수정 등의 리팩토링이 어려움
    - 도메인에 여러 타입이 존재하는 경우 공통 로직이 있는경우 전략패턴을 많이 사용
    - 공통 로직을 별도의 전략 클래스로 하여 조합 (A전략 클래스가 Z전략 클래스를 조합)
    - `전략 패턴` + `조합` 방식으로 해결

### 2. 서류 자동 완성 기능

**🏆 법률 서류 작성 부담을 줄이고, 크로스 플랫폼에서 정확하고 표준화된 PDF 문서를 제공**

- 📝 주요 기능
  - AWS S3를 활용한 PDF 템플릿 저장 및 관리
  - PDFBox AcroForm을 활용한 동적 폼 필드 처리
  - 입력된 데이터 기반 실시간 PDF 문서 생성, 폼 필드 자동 채우기 및 플래튼(flatten) 처리
  - api 를 통한 문서 다운로드 지원


- 🔍 트러블슈팅  
  **PDF 렌더링 호환성 문제 :**
    PDF 뷰어(Chrome, Postman, Adobe Acrobat)별로 폰트 렌더링이 다르게 되어 글자가 깨지는 현상 발생

  - 해결 방안:
    - PDF to Image to PDF 변환 방식 적용
    - 생성된 PDF 문서를 이미지로 변환하여 폰트 렌더링 문제 해결
    - 이미지를 다시 PDF로 변환하여 최종 문서 생성
      

### 3. 전세사기 예방 안전도 검사
**🏆 전세 계약 전 사전 위험을 파악하고 객관적인 안전성을 검증하며 추가 확인사항을 안내**

- 📝 주요 기능
  - 위험도 점수화 평가 식 적용 :`(집값*0.8 - 채권액 - 전세보증금 > 0)`

### 4. 지식인 채팅 상담
**🏆 실시간 전문가 상담으로 개인 맞춤 조언과 전문적인 법률 절차 안내를 제공**

- 📝 주요 기능
  - Web Socket 기반 실시간 채팅
  - Scheduler 를 이용한 상담 관리
  - 멘토 이메일 인증 시스템
    - Redis를 활용한 인증 코드 관리
    - Thymeleaf 템플릿 기반 인증 메일 발송
    - 커스텀 어노테이션을 통한 도메인 검증    

- 🔍 트러블 슈팅  
  **1. 시간에 따른 상담 상태 자동 변경 문제**  
  - 문제: 시간에 따른 STATUS값 변경 자동화를 위해 Spring Batch를 사용하려 했으나 서버가 부하 문제 발생 가능성 높음
  - 해결:    
      - 추가적인 의존성이 필요 없고 사용이 쉬운 Spring Scheduler를 사용
      - 매시간 정각마다 STATUS를 업데이트    
  
  **2. 이메일 전송 API 응답 지연 문제**
  - 문제: 이메일 발송 로직으로 인한 API 응답 시간 4.36초 지연
  - 해결:
    - @Async 어노테이션을 활용한 비동기 처리
    - Custom ThreadPool 설정으로 이메일 발송 최적화
    - API 응답 시간을 10ms까지 단축 (99.7% 개선)

### 5. 피해자 커뮤니티 플랫폼
**🏆 피해자 간 정보 공유와 심리적 지지를 통해 안전한 소통 공간과 맞춤형 정보 접근성을 제공**

- 📝 주요 기능
  - 게시판 별 페이지네이션 검색 기능 (자유/지역별/시크릿/단계별)
  - 베스트 게시글 식을 적용 : `1주일 이내, 댓글수 * 10 + 좋아요 수 * 7`


## 📜 API 명세서
- API의 상세한 사용법과 엔드포인트에 대한 정보는 아래 링크에서 확인할 수 있습니다
- [Boomerang API 명세서](https://documenter.getpostman.com/view/29615301/2sAXxY4oTG)

## 🏗 프로젝트 구조
### 시스템 아키텍쳐
![시스템 아키텍처](boomerang/src/main/resources/static/esset/architecture.png)
![ERD](https://github.com/user-attachments/assets/e1cac376-f090-4240-8f2a-c66d6dcd70fe)


## 🌐 배포된 인스턴스 주소
- BE: http://52.79.80.3:8080
- FE: http://54.252.224.76  


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
<div align="center">
 <table>
   <tr>
     <td align="center" width="25%">
       <img src="https://avatars.githubusercontent.com/u/65104605?s=64&v=4" width="100" height="100" alt="진서현 프로필"><br>
       <a href="https://github.com/JaeanHan" target="_blank">한재안</a><br>
     </td>
     <td align="center" width="25%">
       <img src="https://avatars.githubusercontent.com/u/114674380?s=64&v=4" width="100" height="100" alt="문성민 프로필"><br>
       <a href="https://github.com/yunseong0404" target="_blank">최윤성</a><br>
     </td>
     <td align="center" width="25%">
       <img src="https://avatars.githubusercontent.com/u/108441979?s=64&v=4" width="100" height="100" alt="정재빈 프로필"><br>
       <a href="https://github.com/seongikx" target="_blank">한성익</a><br>
     </td>
   </tr>
 </table>
</div>