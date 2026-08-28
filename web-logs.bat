@echo off
echo Showing web-tomcat logs. Press Ctrl+C to stop watching logs.
cd /d "%~dp0"
docker compose logs -f web-tomcat
