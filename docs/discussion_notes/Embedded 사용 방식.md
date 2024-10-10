### 💡 Embedded 사용 방식

```java

public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Subtitle subtitle;

    @Embedded
    private Content content;

    ...
}

```

- **장점** 🌟
  - 모든 곳에 @Embedded를 사용하면 통일성을 지킬 수 있다
  - Controller, Service Repository 모든 곳에서 객체 생성 시 유효성 검사를 할 수 있다
- **단점** ⚠️
  - 공통된 환경을 사용하지 않아서, 매번 테이블과 데이터를 수동으로 넣어야 한다


---

#### 🗣 **멘토님의 조언**
- 전체 필드를 Embedded로 사용하는 것은 복잡도를 지나치게 높일 수 있다
- 불필요한 **@Embedded** 사용은 유지보수와 가독성을 저하시킬 수 있다
- 복잡도가 높은 클래스나 필드가 여러 개인 경우에만 @Embedded를 적용하는 것을 추천
  -  주소나 복잡한 객체 같은 경우에만 사용을 권장.
- 유효성 검사는 API 요청 레벨에서 처리하는 것으로 충분함
  - 객체 생성 시 유효성 검사를 넣으면 도메인 로직이 과도하게 복잡해질 수 있음.
- 해당 처리 방식은 팀원들끼리 협의해서 통일할 필요가 있다


#### 💡 **팀 결론**
복잡도가 높은 경우에만 **@Embedded** 사용

