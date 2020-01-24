# Study_springboot-aws-starter
### 스프링 부트와 AWS로 혼자 구현하는 웹 서비스 (이동욱) 소스
- https://github.com/jojoldu/freelec-springboot2-webservice 참조

---
oracle JDK 1.8(1.8.0_192) or zulu JDK 1.8(1.8.0_232) 
- java 9 이상은 지원 API 등이 다름

Gradle (4.10.2) 
- gradle/wrapper/gradle-wrapper.properties 파일 안에 4.10.2 설정
- (CLI) 해당 프로젝트 경로에서 ./gradlew wrapper --gradle-version 4.10.2
- https://github.com/jojoldu/freelec-springboot2-webservice/issues/2 참조
                     
SpringBoot 2.1.7 RELEASE
- 2.2.X 이상 안됨

Jnuit5

---

### [Contents]
#### DTO, Entity
* DTO와 Entity는 구분해서 사용할 것(역할 분리)
* DTO > View layer (사소한 기능 변경)
* Entity > DB layer (DB 연동 큰 변경 > request, response 클래스로 사용하지 말 것)
 
#### PostsApiControllerTest
* @WebMvcTest : JPA 기능이 작동하지 않음, 외부 연동과 관련된 부분만 활성화(Controller, ControllerAdvice 등)
* @SpringBootTest + TestRestTemplate 사용

#### PostsApiController
* public Long save(@RequestBody PostsSaveRequestDto requestDto) 메소드는 @PutMapping이 아닌 @PostMapping(오타로 예상)

#### h2-console
* URL : http://localhost:8080/h2-console
* JDBC URL : jdbc:h2:mem:testdb

#### index.js
* IndexController에서 public String index() 메소드 GetMapping 어노테이션에 path ("/") 꼭 붙여줄 것
