@echo off
set "SERVICE=%~1"
if "%SERVICE%"=="" set "SERVICE=oracle-free"

echo Starting Docker service "%SERVICE%"...
cd /d "%~dp0"
docker compose up -d %SERVICE%
echo.
if /i "%SERVICE%"=="oracle-free" (
	echo Oracle is starting up. It may take 1-2 minutes to be fully ready.
	echo   - JDBC URL : jdbc:oracle:thin:@//localhost:1521/FREEPDB1
) else (
	echo Service "%SERVICE%" is starting up.
)
echo.
echo Showing "%SERVICE%" logs now. Press Ctrl+C to stop watching logs.
docker compose logs -f %SERVICE%