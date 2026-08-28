@echo off
set "SERVICE=%~1"
if "%SERVICE%"=="" set "SERVICE=oracle-free"

echo Stopping Docker service "%SERVICE%"...
cd /d "%~dp0"
docker compose stop %SERVICE%
echo.
echo Done. Data is preserved. Run db-start.bat %SERVICE% to restart.
pause

