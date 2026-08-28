# 스프링 MVC 프로젝트 연습

> 스프링 MVC 프로젝트 연습용으로 만들어보았다. 



## 사용된 베이스 프로젝트 템플릿

이전에 Maven 아키타입으로 만든 프로젝트를 사용해서 만들었다.

* https://github.com/fp024/spring-project-template/tree/master/archetypes/spring0-mvc-project-archetype



## 프로젝트 초기화

VSCode 환경에서 좀 더 편하게 사용할 수 있도록 NodeJS 스크립트 / 프로젝트를 포함했다.

* VSCode Java 프로젝트의 개발 환경 세팅을 자동화하는 CLI 스크립트 모음
  * https://github.com/fp024/vscode-java-setup-scripts

```bash
# Node 25의 경우는 npm install -g corepack을 먼저 실행해준다.
corepack enable
pnpm install
pnpm init-project
```


## 로컬 환경 웹 서버 실행 (cargo-maven3-plugin 사용)

[cargo-maven3-plugin](https://codehaus-cargo.github.io/cargo/Maven+3+Plugin.html)를 붙인 상태여서, 

[tomcat-run.bat](tomcat-run.bat) 또는 [tomcat-run.sh](tomcat-run.sh)를 실행하면 바로 웹 프로젝트가 시작되도록 설정했다.


## Docker Compose 실행 배치 파일 목록

### 데이터베이스 (Oracle Free 23c) 관련

| 파일 | 설명 |
|------|------|
| `db-start.bat` | Oracle DB 컨테이너 생성 및 실행 (이미 있다면 재시작만 함). DB 준비 완료 시까지 대기 |
| `db-stop.bat` | Oracle DB 컨테이너 중지. 데이터 유지됨 |
| `db-clean.bat` | Oracle DB 컨테이너 및 볼륨 삭제. 데이터 완전 초기화 |

**DB 접속 정보:**
- JDBC URL: `jdbc:oracle:thin:@//localhost:1521/FREEPDB1`


### 웹 서버 (Tomcat 11) 관련

| 파일 | 설명 |
|------|------|
| `web-start.bat` | WAR 빌드 (필요시) → DB 준비 대기 → 웹 서비스 시작. 한 번에 모든 과정 완료 |
| `web-stop.bat` | 웹 컨테이너 중지 |
| `web-clean.bat` | 웹 컨테이너 중지 후 삭제 (DB/볼륨 영향 없음) |
| `web-restart.bat` | 웹 컨테이너 재시작 후 로그 표시 |
| `web-logs.bat` | 웹 로그 실시간 표시 (재시작 없음) |

**웹 접속 정보:**
- App URL: `http://localhost:8080/`