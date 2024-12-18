# LLM AI활용 영어 학습 도우미 - AStartrip

## 소개

A-Startrip은 Large Language Model(LLM)을 활용한 혁신적인 영어 학습 웹서비스입니다. Spring Boot Web Framework를 기반으로 구축되었으며, Perplexity 社의 Sonar 모델 API를 활용하여 개인화된 영어 학습 경험을 제공합니다. 이 서비스는 사용자의 요구에 맞춘 6가지 유형의 학습 콘텐츠를 제공하여 효과적이고 깊이 있는 영어 학습을 지원합니다.

## 개발 배경
- 글로벌화된 세계에서 영어의 중요성이 증대되고 있어, 효과적이고 혁신적인 영어 교육 방법에 대한 수요가 증가하고 있습니다.
- LLM의 급속한 발전으로 인해 교육 분야에 새로운 가능성이 열리고 있습니다.
- 학습자의 다양한 요구와 학습 스타일을 고려한 개인화된 학습 경험 제공이 필요합니다.
- 시간과 공간의 제약 없이 효과적인 학습이 이루어질 수 있는 환경 조성이 필요합니다.

## 설치 및 구성

### 1. requirements
- 시스템에 OpenJDK 21 이상이 설치되어 있어야 함
- Gradle 패키지 관리자가 정상적으로 동작하는 환경
- MariaDB에 접속할 수 있을 것

### 2. 환경 변수 수정하기
- MariaDB 서버를 구축하고 [application.properties](src/main/resources/application.properties)에서 DB 주소와 DB의 ID, Password를 설정합니다.
<br>최소한 MariaDB가 구성되어야 서버가 정상적으로 실행됩니다.
- LLM API 응답 ,OAuth, 회원가입 시에 email 발송 기능을 활용하려면 [application.properties](src/main/resources/application.properties) / [application-oauth.properties](src/main/resources/application-oauth.properties) / [.env](src/main/.env) 를 적절히 수정합니다.
### 3. 서버 실행하기
아래 명령어를 사용하여 프로젝트를 설치하고 실행합니다:
```
git clone https://github.com/imdhson/aStartrip.git ;
cd aStartrip ;
./gradlew bootRun ;
```
### 4. 접속하기
웹브라우저를 열고 [http://localhost:8080](http://localhost:8080) 에 접속하세요.

LLM AI활용 영어 학습 도우미 - AStartrip의 주요 기능을 다음과 같이 확장하였습니다:

## 주요 기능

- **Reading 학습**: 'R01' 타입 카드로 영어 지문 생성, 'R02' 타입 카드로 선/후 독서 질문 및 배경지식 생성.
- **Writing 학습**: 'W01' 타입 카드로 영어 지문 첨삭, 'W02' 타입 카드로 문장 성분 분석.
- **Vocabulary 학습**: 'V01' 타입 카드로 단어 기반 지문 생성, 'V02' 타입 카드로 동의어, 반의어, word-family 찾기.
- **실시간 협업**: WebSocket 기술을 활용한 비동기 실시간 소통 기능.
- **파일 공유**: 이미지, 동영상, 기타 파일 업로드 및 공유 기능.
- **OAuth 2.0 인증**: Google 계정을 통한 간편 로그인 및 회원가입 지원.
- **Spring Security 적용**: 사용자 인증 및 권한 관리를 통한 보안 강화.
- **게시글 통합 검색**: 제목, 내용, 작성자 등 다양한 필드에 대한 전문 검색 기능 구현.
- **유동적 카드 추가**: 하나의 게시글에 다양한 유형의 학습 카드를 동적으로 추가할 수 있는 기능.

## 라이선스

이 프로젝트는 [MIT LICENSE](LICENSE.md) 하에 배포됩니다.

## 연락처
```
개발자: 손동휘
이메일: mail@imdhson.com
GitHub: [https://github.com/imdhson](https://github.com/imdhson)
```