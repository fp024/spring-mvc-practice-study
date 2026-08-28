@echo off
echo [WARNING] This will stop and remove Docker web container "web-tomcat".
echo.
set /p confirm=Are you sure you want to proceed? (y/N):
if /i not "%confirm%"=="y" (
    echo Cancelled.
    pause
    exit /b 0
)

echo.
echo Stopping and removing "web-tomcat"...
cd /d "%~dp0"
docker compose stop web-tomcat
docker compose rm -f web-tomcat
echo [Environment Variables...]
CALL .\set-jdk-env.bat
CALL .\mvnw.cmd -t .\toolchains.xml clean
echo.
echo Done. Run web-start.bat to create/start web-tomcat again.
pause