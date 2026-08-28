@echo off
echo Restarting web service "web-tomcat"...
cd /d "%~dp0"
docker compose restart web-tomcat
echo.
echo Web service restarted.
echo Showing web-tomcat logs now. Press Ctrl+C to stop watching logs.
docker compose logs -f web-tomcat
