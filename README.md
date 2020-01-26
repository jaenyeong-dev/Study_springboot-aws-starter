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

#### Google Cloud Platform 구글 서비스 등록
* 새 프로젝트 생성
* API 및 서비스 > 사용자 인증 정보 만들기 (OAuth 클라이언트 ID로 생성)
* 동의화면 구성 (앱 이름, 지원 이메일, Google API 범위 등 확인)
* 승인된 리디렉션 URI 설정 (승인 후 이동할 URL) 
  - 스프링 시큐리티는 기본적으로 리다이렉트 URL이 {도메인}/login/oauth2/code/{소셜서비스코드}로 되어 있음
  
#### spring security oauth2 client 라이브러리 사용
* spring-security-oauth2-autoconfigure 는 사용함에 문제는 없으나 신규 기능 추가가 되지 않음
* spring-security-oauth2-client의 스프링 부트용 라이브러리(starter) 출시

#### application-oauth.yaml(properties)
* spring.security.oauth2.client.registration.google.scope: profile,email
* 강제로 profile, email 등록 (기본은 openid, profile, email)
* OpenId Provider 서비스(구글 등)와 아닌 서비스(네이버, 카카오 등)를 나눠 각각 OAuth2Service를 생성해야 함
* 따라서 하나의 OAuth2Service로 사용하기 위해 openid scope 제거
* 클라이언트 ID, Secret 등 때문에 gitignore에 추가
* registration 설정
  - spring.security.oauth2.client.registration.google.client-id = ***
  - spring.security.oauth2.client.registration.google.client-secret = ***
  - spring.security.oauth2.client.registration.google.scope = profile, email

#### application.yaml(properties)
* 스프링 부트에서는 application-xxx.yaml(properties) 파일을 xxx라는 이름의 profile으로 인식
* spring.profiles.include = oauth 추가
* yaml 파일에서 {} 사용시 ''으로 감싸야함(문법)

#### 스프링 시큐리티 ROLE
* 스프링 시큐리티에서 권한 코드에는 항상 ROLE이 앞에 있어야 함

#### spring-boot-starter-oauth2-client
* spring-security-oauth2-client와 spring-security-oauth2-jose를 기본 관리해줌

#### IndexController
* SessionUser user = (User) httpSession.getAttribute("user"); (오타로 예상)
* 타입 캐스팅을 User > SessionUser로 수정

#### spring-session-jdbc
* gradle 의존성 추가시 h2에 테이블 자동생성(JPA에 의해)
* SPRING_SESSION, SPRING_SESSION_ATTRIBUTES

#### Naver ID login
* 네이버는 스프링 시큐리티를 공식 지원하지 않아 CommonOAuth2Provider에서 해주는 값도 전부 수동 입력 필요
* registration 설정
  - spring.security.oauth2.client.registration.naver.client-id = ***
  - spring.security.oauth2.client.registration.naver.client-secret = ***
  - spring.security.oauth2.client.registration.naver.scope = name, email, profile_image
  - spring.security.oauth2.client.registration.naver.redirect_uri_template = {baseUrl}/{action}/oauth2/code/{registrationId}
  - spring.security.oauth2.client.registration.naver.authorization_grant_type = authorization_code
  - spring.security.oauth2.client.registration.naver.client-name = Naver
* provider 설정
  - spring.security.oauth2.client.provider.naver.authorization_uri = https://nid.naver.com/oauth2.0/authorize
  - spring.security.oauth2.client.provider.naver.token_uri = https://nid.naver.com/oauth2.0/token
  - spring.security.oauth2.client.provider.naver.user-info-uri = https://openapi.naver.com/v1/nid/me
  - spring.security.oauth2.client.provider.naver.user_name_attribute = response
    네이버 회원 조회시 반환되는 JSON 형태 때문에 기준이 되는 user_name의 이름을 response로 해야함

#### Spring Security Test
* application.properties(yaml) 파일은 test 경로에 없을시 main 경로에 파일을 자동으로 참조
* 그러나 application-oauth.properties(yaml) 파일은 자동으로 참조하지 않아 기존 테스트 에러 발생
* spring-security-test 의존성 추가 (testImplementation)
  - @WithMockUser 어노테이션은 MockMvc에서만 작동
* @WebMvcTest에 secure 옵션은 2.1부터 Deprecated 되었음
