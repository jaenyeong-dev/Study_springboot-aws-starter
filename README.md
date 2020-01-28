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

#### AWS EC2 구축
* EC2 생성 (Default VPC, Subnet & 새 보안그룹 생성)
* EIP 생성 (Amazon ) 및 연결
* EC2 Java 세팅
  - sudo yum install -y java-1.8.0.openjdk-devel.x86_64
  - sudo /usr/sbin/alternatives --config java
  - sudo yum remove java-1.7.0-openjdk
  - java -version
* EC2 UTC > KST 세팅
  - sudo rm /etc/localtime
  - sudo ln -s /usr/share/zoneinfo/Asia/Seoul /etc/localtime
* EC2 hostname 세팅
  - sudo vim /etc/sysconfig/network
    - HOSTNAME springboot-aws-starter (속성 값 변경)
  - sudo reboot
  - sudo vim /etc/hosts
    - 127.0.0.1 springboot-aws-starter (추가)
    - curl springboot-aws-starter (확인)
* mysql 설치
  - sudo yum install mysql

#### AWS RDS(Maria DB) 구축
* 사용자 이름 : admin
* 암호 : admin1234
* 퍼블릭 엑세스 가능 옵션 true (보안그룹에서 차단 설정)
* 파라미터 그룹 생성, 파라미터 값 설정 후 미리 생성해 둔 RDS(Maria DB)에 연결, 즉시 적용 및 재부팅
  - character-set-client : utf8mb4
  - character-set-connection : utf8mb4
  - character-set-database : utf8mb4
  - character-set-filesystem : utf8mb4
  - character-set-results : utf8mb4
  - character_set_server : utf8mb4
  - collation_connection : utf8mb4_unicode_ci
  - collation_server : utf8mb4_unicode_ci
  - max_connections : 150
* 보안그룹 생성 및 연결
  - 기존 EC2 보안그룹 및 내 IP 설정
  - EC2 인스턴스는 대수가 늘어날 가능성이 있어 보편적으로 위와 같이 보안 그룹간 연동을 진행함
* DB 접속
  - Database 생성
  - show variables like 'c%';
  - ALTER DATABASE springboot_aws_starter 
    CHARACTER SET = 'utf8mb4' 
    COLLATE = 'utf8mb4_general_ci';
  - SET collation_server = 'utf8mb4_general_ci';
  - SELECT @@time_zone, now();
  - 테이블 생성은 인코딩 설정 변경후 하는 것이 좋음(자동 변경이 되지 않아 강제로 적용해야 하기 때문에)
  - CREATE TABLE test (
        id bigint(20) NOT NULL AUTO_INCREMENT,
        content varchar(255) DEFAULT NULL,
        PRIMARY KEY (id)
    ) ENGINE = InnoDB;

#### EC2에서 RDS(MariaDB) 접속
* mysql -u 계정 -p -h Host주소
  - mysql -u admin -p -h springboot-aws-starter.chgk0xrg8hpj.ap-northeast-2.rds.amazonaws.com
  - SHOW DATABASES;
  - SELECT * FROM test;

#### EC2에 프로젝트 배포
* sudo yum install git
* git --version (2.14.5)
* mkdir ~/app && mkdir ~/app/step1 (프로젝트를 저장할 디렉토리 생성)
* cd ~/app/step1/
* git clone https://github.com/jaenyeong-dev/Study_springboot-aws-starter.git springboot-aws-starter
* ./gradlew test
  - git pull (소스 수정시 다시 내려받음)
  - chmod +x ./gradlew (Permission denied 일 때 파일 권한 추가)
  - ./gradlew test 명령 실행시
    Error
    Could not open JPA EntityManager for transaction;
    nested exception is java.lang.IllegalStateException: EntityManagerFactory is closed
    로컬에서만 에러 발생 (EC2 인스턴스에서는 성공) 확인필요
    
#### 배포 스크립트 생성
* vim ~/app/git/deploy.sh (아래는 셸 스크립트 파일 내용)

  #!/bin/bash
  
  REPOSITORY=/home/ec2-user/app/step1
  PROJECT_NAME=springboot-aws-starter
  
  cd $REPOSITORY/$PROJECT_NAME/
  
  echo "> Git Pull"
  
  git pull
  
  echo "> Project Build start"
  
  ./gradlew build
  
  echo "> Move step1 directory"
  
  cd $REPOSITORY
  
  echo "> Build Copy file"
  
  cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/
  
  echo "> Check Processing PID NOW"
  
  CURRENT_PID=$(pgrep -f ${PROJECT_NAME}*.jar)
  
  echo " Current Processing PID : $CURRENT_PID"
  
  if [ -z "$CURRENT_PID" ]; then
      echo "> It will not be terminated to isn't running application"
  else
      echo "> kill -15 $CURRENT_PID"
      kill -15 $CURRENT_PID
      sleep 5
  fi
  
  echo "> Deploy New application"
  
  JAR_NAME=$(ls -tr $REPOSITORY/ | grep *.jar | tail -n 1)
  
  echo "> JAR Name : $JAR_NAME"
  
  nohup java -jar $REPOSITORY/$JAR_NAME 2>&1 &
  
* chmod +x ./deploy.sh (권한 변경)
* ./deploy.sh (셸 스크립트 파일 실행)
* vim nohup.out (실행 상태 확인 > 인증, 인가 정보 파일로 인하여 에러)
* vim /home/ec2-user/app/application-oauth.yaml(properties) (인증, 인가 정보 파일 생성, 내용 복사 후 저장)
* 셸 스크립트 실행 파일 수정
  - #nohup java -jar $REPOSITORY/$JAR_NAME 2>&1 &
  - nohup java -jar \
        -Dspring.config.location=classpath:/application.yaml,/home/ec2-user/app/application-oauth.yaml \
        $REPOSITORY/$JAR_NAME 2>&1 &
  - DSpring.config.location : 스프링 설정 파일 위치 지정
    classpath가 붙으면 jar 안에 있는 resources 디렉토리를 기준으로 경로 생성
    application-oauth.yaml(properties) 파일은 외부에 파일이 있기 때문에 절대 경로 사용
* 실행파일 종료 상태 확인 후 deploy.sh 파일 다시 실행

#### RDS 접근
* 테이블 생성 (H2 > MariaDB)
* 프로젝트 설정 (Maria DB 드라이버 추가)
* EC2 설정 (EC2 서버 인스턴스 내부에 접속 정보를 관리하도록 설정)
* 아래 쿼리(테스트 코드 실행시 로그로 생성되는 쿼리)를 통하여 RDS에 테이블 생성 (posts, user)
  - create table posts (
        id bigint not null auto_increment,
        create_date datetime, 
        modified_date datetime, 
        author varchar(255), 
        content TEXT not null, 
        title varchar(500) not null, 
        primary key (id)
    ) engine=InnoDB;
  - create table user (
        id bigint not null auto_increment, 
        create_date datetime, 
        modified_date datetime, 
        email varchar(255) not null, 
        name varchar(255) not null, 
        picture varchar(255), 
        role varchar(255) not null, 
        primary key (id)
    ) engine=InnoDB;
* Spring Session 테이블 생성
  - CREATE TABLE SPRING_SESSION (
    	PRIMARY_ID CHAR(36) NOT NULL,
    	SESSION_ID CHAR(36) NOT NULL,
    	CREATION_TIME BIGINT NOT NULL,
    	LAST_ACCESS_TIME BIGINT NOT NULL,
    	MAX_INACTIVE_INTERVAL INT NOT NULL,
    	EXPIRY_TIME BIGINT NOT NULL,
    	PRINCIPAL_NAME VARCHAR(100),
    	CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
    ) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;
    
    CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
    CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
    CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);
    
    CREATE TABLE SPRING_SESSION_ATTRIBUTES (
    	SESSION_PRIMARY_ID CHAR(36) NOT NULL,
    	ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
    	ATTRIBUTE_BYTES BLOB NOT NULL,
    	CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    	CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
    ) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;
* MariaDB 드라이버 의존성 추가
  - implementation('org.mariadb.jdbc:mariadb-java-client')
* application-real.yaml(properties) 파일 생성
  - 서버에서 구동될 환경 설정 파일
* application-real-db.yaml(properties) 파일을 EC2 인스턴스에 생성
  - RDS 접속 정보 설정 파일
    spring:
      jpa:
        hibernate:
          ddl-auto: none
      datasource:
        url: jdbc:mariadb://rds엔드포인트:포트명/스키마명
        username: ID
        password: Password
        driver-class-name: org.mariadb.jdbc.Driver
  - spring.jpa.hibernate.ddl-auto=none
    JPA로 테이블이 자동 생성되는 옵션을 None(하지 않음)으로 지정
    실제 운영되는 서비스는 JPA로 테이블을 생성하게 하지 말것
* deploy.sh 파일이 real profile을 사용할 수 있도록 수정
  - nohup java -jar \
        -Dspring.config.location=classpath:/application.yaml,/home/ec2-user/app/application-oauth.yaml \
        -Dspring.profiles.active=real \
        $REPOSITORY/$JAR_NAME 2>&1 &
  - -Dspring.profiles.active=real
    application-real.yaml(properties) 파일 활성화
    application-real.yaml(properties) 파일 안에 spring.profiles.include=oauth,real-db 옵션으로 인해 real-db 또한 활성화됨
* curl localhost:8080
  - 스프링부트 실행 후 해당 명령어로 확인

#### 소셜로그인 EC2 설정
* EC2 인스턴스에 보안그룹에 해당 포트 인바운드 확인
* [Google] console.cloud.google.com/home/dashboard (구글 웹 콘솔) 접속하여 해당 프로젝트 설정
  - jaenyeong.dev@gmail.com 계정 사용
  - API 및 서비스 > 사용자 인증정보 > OAuth 동의화면
    승인된 도메인에 해당 인스턴스 퍼블릭 DNS 정보 입력 엔터 후 저장 버튼 클릭 (http, 포트 및 하위 경로 제외)
  - API 및 서비스 > 사용자 인증정보
    승인된 리디렉션 URI에 해당 인스턴스 '퍼블릭 DNS:8080' + '/login/oauth2/code/google' 추가
* [Naver] developers.naver.com/apps/#/myapps (네이버 개발자 센터) 접속하여 해당 프로젝트 설정
  - jaenyeongdev@naver.com 계정 사용
  - 내 어플리케이션 > API 설정 > PC 웹 항목
    서비스 URL에 해당 인스턴스 퍼블릭 DNS 정보 입력 (http 포함, 포트 및 하위 경로 제외)
    콜백 URL에 해당 인스턴스 '퍼블릭 DNS:8080' + '/login/oauth2/code/naver' 추가
    
#### Travis CI 설정
* Github에서 제공하는 무료 CI 서비스 (https://travis-ci.org)
* 계정명 > Settings 해당 프로젝트 (Study_springboot-aws-starter) 활성화
* build.gradle과 같은 위치에 .travis.yml 파일 생성 (.travis.yaml 파일은 인식 못함)

#### AWS CodeDeploy 연동
* CodeDeploy에는 저장 기능이 없음 > Travis CI가 빌드한 결과물을 S3 등을 이용하여 CodeDeploy가 가져갈 수 있도록 처리함
* 빌드, 배포를 분리하는 것을 추천함
  - CodeDeploy로 빌드, 배포 가능하나 (Github 코드를 가져오는 기능 지원) 빌드 없이 배포만 필요할 때 대응이 어려움
  - 빌드, 배포가 분리되어 있으면 기존에 빌드된 Jar 파일을 재사용하면 되지만 CodeDeploy 사용시 항상 빌드를 하게 되어 확장성이 떨어짐
* AWS IAM
  - 사용자 > 사용자추가
  - 액세스 유형 : 프로그래밍 방식 엑세스만 허용
  - 기존 정책 연결 : s3full, CodeDeployFull 연결
  - Name 속성 삽입
  - 엑세스 ID, 엑세스 시크릿 획득
* Travis CI에 키 등록
  - Settings > Environment Variables
  - AWS_ACCESS_KEY, AWS_SECRET_KEY 변수에 위 엑세스 키, 시크릿 데이터 바인딩, 추가
  - 이 변수에 값들은 .travis.yml 파일내에서 각각 $AWS_ACCESS_KEY, $AWS_SECRET_KEY 변수명으로 사용 가능
* S3 버킷 생성
  - 버킷명 등 기입, 버킷 버전 정보 등은 크게 상관 없음, 모든 퍼블릭 엑세스 차단 체크 되어 있는 상태로 버킷 생성
* .travis.yml 파일에 추가
  - 배포 전 실행
    * before_deploy:
  - CodeDeploy는 Jar 파일을 인식 못함, Jar+기타 설정 파일을 모아 압축
  - 명령어의 마지 위치는 본인의 프로젝트 이름일 것
  - 배포 등 외부 서비스와 연동 행위 선언
    * deploy:
    
#### AWS CodeDeploy 연동 2
* EC2 IAM 역할 추가
  - AWS 서비스 > EC2 > AmazonEC2RoleforAWSCodeDeploy 정책 연결
  - 원하는 태그 추가
  - 역할 이름 입력 후 생성
* 해당 EC2 인스턴스 마우스 우클릭 > 인스턴스 설정 > IAM 역할연결/바꾸기
  - 위에서 생성한 역할 선택, 저장
* .travis.yml 파일에 ./gradle test에러로 인해 before_script(pgrep -f gradle | xargs kill) 임시 추가
  - ./gradle test가 간헐적으로 성공, 실패됨 (기존 종료되지 않은 gradle 데몬이 충돌되는 것으로 예상)
  - Error
    - Could not open JPA EntityManager for transaction;
    - JPA EntityManagerFactory is closed
* CodeDeploy 에이전트 설치
  - EC2 인스턴스에서 CLI 실행
    - aws s3 cp s3://aws-codedeploy-ap-northeast-2/latest/install . --region ap-northeast-2
    - 성공시 download: s3://aws-codedeploy-ap-northeast-2/latest/install to ./install 출력
    - sudo chmod +x ./install
    - sudo ./install auto
    - sudo service codedeploy-agent status
    - 정상 상태일 경우 The AWS CodeDeploy agent is running as PID *** 출력
* CodeDeploy 권한 생성 (EC2 접근용)
  - AWS IAM 역할 추가
    - AWS 서비스 > CodeDeploy > AWSCodeDeployRole 정책 연결
    - 원하는 태그 추가
    - 역할 이름 입력 후 생성
* AWS 배포 설명
  - Code Commit
    - Github 같은 코드 저장소
  - Code Build
    - Travis CI와 마찬가지로 빌드용 서비스 (젠킨스/팀시티 등을 대신 이용함)
  - CodeDeploy
    - AWS 배포 서비스
    - 위 서비스들과 다르게 대체제가 없음
    - 오토 스케일링 그룹 배포, 블루 그린 배포, 롤링 배포, EC2 단독 배포 등 많은 기능을 지원
* CodeDeploy 어플리케이션 생성
  - AWS 서비스에서 CodeDeploy 선택 > 어플리케이션 > 생성 버튼 클릭
  - 어플리케이션 이름 입력 후 컴퓨팅 플랫폼 설정 (여기선 EC2/온프레미스)
  - 해당 어플리케이션 정보에서 배포 그룹 생성 버튼 클릭
  - 배포 그룹이름 입력 후 서비스 역할 선택 (앞에서 생성한 CodeDeploy용 IAM 역할 선택)
  - 배포 유형은 현재 위치 선택 (2대 이상일 경우에 블루/그린 선택)
  - 환경 구성에서는 Amazon EC2 인스턴스 선택
  - 원하는 태그 추가
  - 배포 설정 > 배포 구성
    - CodeDeployDefaultAllAtOnce (한대씩, 절반씩 등 선택 옵션 확인할 것)
  - 로드 밸런서 활성화 체크 박스 풀고 배포 그룹 생성 버튼 클릭

#### Travis CI, S3, CodeDeploy 연동
* S3에서 넘겨줄 zip 파일을 저장할 디렉토리 생성
  - mkdir ~/app/step2 && mkdir ~/app/step2/zip
* 순서
  - Travis CI에 build가 끝나면 S3에 zip 파일이 전송, 이 zip 파일은 /home/ec2-user/app/step2/zip 으로 복사, 압축을 풀 예정
* AWS CodeDeploy 설정은 appspec.yml 파일로 진행
  - 내용은 위 파일 참조
* .travis.yml 파일에도 CodeDeploy 내용 추가

#### 배포 자동화 구성 (Jar 파일 배포, 실행)
* deploy.sh 파일 추가
  - /home/ec2-user/app/step2 경로에 deploy.sh 생성
    - 해당 프로젝트에서 script 디렉토리 생성 후 스크립트 생성 (아래는 셸 스크립트 파일 내용)
    
      #!/bin/bash
      
      REPOSITORY=/home/ec2-user/app/step2
      PROJECT_NAME=springboot-aws-starter
      
      echo "> Build copy file"
      
      cp $REPOSITORY/zip/*.jar $REPOSITORY/
      
      echo "> Check Processing PID NOW"
      
      CURRENT_PID=$(pgrep -fl $PROJECT_NAME | grep jar | awk '{print $1}')
      
      echo " Current Processing PID : $CURRENT_PID"
      
      if [ -z "$CURRENT_PID" ]; then
          echo "> It will not be terminated to isn't running application"
      else
          echo "> kill -15 $CURRENT_PID"
          kill -15 $CURRENT_PID
          sleep 5
      fi
      
      echo "> Deploy New application"
      
      JAR_NAME=$(ls -tr $REPOSITORY/ | grep *.jar | tail -n 1)
      
      echo "> JAR Name : $JAR_NAME"
      
      echo "> Add execution permission on $JAR_NAME"
      
      sudo chmod +x $JAR_NAME
      
      echo "> Run $JAR_NAME"
      
      nohup java -jar \
              -Dspring.config.location=/application.yaml,classpath:/application-real.yaml,/home/ec2-user/app/application-oauth.yaml,/home/ec2-user/app/application-real-db.yaml \
              -Dspring.profiles.active=real \
              $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
* .travis.yml 파일 수정
  - 모든 파일을 zip 파일로 만드는 것 수정 (Jar, appspec.yml, script 등 파일이 실제로 필요함) > before_deploy 수정
  - Travis CI는 S3로 특정 파일만 업로드가 안되서 (디렉토리 단위로만 가능) deploy 디렉토리는 항상 생성
  - before-deploy에는 zip 파일에 포함시킬 파일들을 저장
  - zip -r 은 before-deploy 디렉토리 전체 파일을 압축
* appspec.yml 파일 수정
  - permissions, hooks 추가
* 이슈 발생시 CodeDeploy 로그 확인
  - /opt/codedeploy-agent/deployment-root 경로에 로그 등을 확인하여 처리
* jar version 변경
  - build.gradle에서 변경
  - 확인용으로 index.mustache 파일에 h1 태그를 ver3으로 변경

