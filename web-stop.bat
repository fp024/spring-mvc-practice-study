@echo off
echo Stopping Docker web service "web-tomcat"...
cd /d "%~dp0"
docker compose stop web-tomcat
echo.
echo Done. Web service is stopped.
pause
