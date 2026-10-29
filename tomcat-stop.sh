#!/bin/sh
# 어디서 실행하더라도 프로젝트 루트를 기준으로 동작시킨다.
SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
cd "$SCRIPT_DIR" || exit 1

echo "### Tomcat Stop ... ###"

# JAVA_HOME 설정 + toolchains.xml 생성 (tomcat-run.bat의 set-jdk-env.bat 호출에 대응)
. ./set-jdk-env.sh || exit 1

# Maven 명령어 실행
./mvnw -t ./toolchains.xml org.codehaus.cargo:cargo-maven3-plugin:stop