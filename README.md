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
pnpm install
pnpm init-project
```

* 해당 공통 스크립트 프로젝트에 Maven은 적용하지않아서, mvn wrapper 실행 부분만 프로젝트의 scripts/이하에 NodeJs스크립트로 추가했다.
  * Maven 관련 기능도 통합을 할 예정이다. 😅



## 웹 서버 실행

maven-cargo-plugin를 붙인 상태여서, 

tomcat-run.bat 또는 tomcat-run.sh를 실행하면 바로 웹 프로젝트가 시작되도록 설정했다.

