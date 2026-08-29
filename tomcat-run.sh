#!/bin/sh
# 어디서 실행하더라도 프로젝트 루트를 기준으로 동작시킨다.
SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
cd "$SCRIPT_DIR" || exit 1

# JAVA_HOME 설정 + toolchains.xml 생성 (tomcat-run.bat의 set-jdk-env.bat 호출에 대응)
. ./set-jdk-env.sh || exit 1

# 속성 파일 읽기 (주석(#)과 빈 줄 제외)
export $(awk -F= '!/^#/ && NF==2 {print $1 "=" $2}' cargo.properties)

echo "### Tomcat ${cargo_tomcat_version} Run ... ###"

# Maven 명령어 실행
./mvnw -t ./toolchains.xml clean verify org.codehaus.cargo:cargo-maven3-plugin:run \
  -DskipTests \
  -DcargoContextPath=${cargo_context_path} \
  -Dcargo.jvmargs="${cargo_jvmargs}" \
  -Dcargo.maven.containerId=${cargo_maven_containerId} \
  -Dcargo.maven.containerUrl=https://repo.maven.apache.org/maven2/org/apache/tomcat/tomcat/${cargo_tomcat_version}/tomcat-${cargo_tomcat_version}.zip \
  -Dcargo.servlet.port=${cargo_servlet_port}
