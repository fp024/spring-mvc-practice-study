@ECHO OFF
cd /d "%~dp0"
@ECHO ### Tomcat Stop 10.1.x ... ###

echo [Environment Variables...]
CALL .\set-jdk-env.bat

:: Maven 명령어 실행
CALL .\mvnw.cmd -t .\toolchains.xml org.codehaus.cargo:cargo-maven3-plugin:stop