@echo off
set "SERVICE=%~1"
if "%SERVICE%"=="" set "SERVICE=oracle-free"

echo [WARNING] This will permanently delete "%SERVICE%" container and related volumes!
echo.
set /p confirm=Are you sure you want to proceed? (y/N):
if /i not "%confirm%"=="y" (
    echo Cancelled.
    pause
    exit /b 0
)

echo.
echo Stopping and removing "%SERVICE%" container and volumes...
cd /d "%~dp0"
docker compose down -v %SERVICE%
echo.
echo Done. The init-scripts will be re-executed on the next startup.
pause

