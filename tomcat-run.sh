#!/bin/sh
# 속성 파일 읽기 (주석(#)과 빈 줄 제외)
export $(awk -F= '!/^#/ && NF==2 {print $1 "=" $2}' cargo.properties)

echo "### Tomcat ${cargo_tomcat_version} Run ... ###"

# Maven 명령어 실행
# [INFO] Lunix 환경에서는 toolchain.xml 파일을 .m2에 수동으로 구성할 필요 있음.
#        아직 toolchain.xml을 자동으로 만들어주는 기능은 PowerShell기반으로 윈도우에서만 지원하고 있음 😅
mvn clean verify org.codehaus.cargo:cargo-maven3-plugin:run \
  -DskipTests \
  -DcargoContextPath=${cargo_context_path} \
  -Dcargo.jvmargs="${cargo_jvmargs}" \
  -Dcargo.maven.containerId=${cargo_maven_containerId} \
  -Dcargo.maven.containerUrl=https://repo.maven.apache.org/maven2/org/apache/tomcat/tomcat/${cargo_tomcat_version}/tomcat-${cargo_tomcat_version}.zip \
  -Dcargo.servlet.port=${cargo_servlet_port} 
