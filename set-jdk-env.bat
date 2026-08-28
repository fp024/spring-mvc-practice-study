@ECHO OFF

SET "SCRIPT_DIR=%~dp0"
SET "CUSTOM_ENV_FILE=%SCRIPT_DIR%setenv-custom.properties"
SET "DEFAULT_ENV_FILE=%SCRIPT_DIR%setenv.properties"

IF EXIST "%CUSTOM_ENV_FILE%" (
	SET "ENV_FILE=%CUSTOM_ENV_FILE%"
) ELSE (
	SET "ENV_FILE=%DEFAULT_ENV_FILE%"
)

IF NOT EXIST "%ENV_FILE%" (
	ECHO [ERROR] setenv-custom.properties or setenv.properties not found.
	EXIT /B 1
)

FOR /F "usebackq tokens=1,* delims==" %%A IN ("%ENV_FILE%") DO (
	IF /I "%%~A"=="JAVA_HOME" SET "JAVA_HOME=%%~B"
)

IF "%JAVA_HOME%"=="" (
	ECHO [ERROR] JAVA_HOME not found in %ENV_FILE%.
	EXIT /B 1
)

SET "JAVA_HOME=%JAVA_HOME:/=\%"
@ECHO JAVA_HOME=%JAVA_HOME%

powershell -NoProfile -ExecutionPolicy Bypass -File "%SCRIPT_DIR%build-scripts\generate-toolchains.ps1" -PropertiesPath "%ENV_FILE%"
IF ERRORLEVEL 1 (
	ECHO [ERROR] Failed to generate toolchains.xml.
	EXIT /B 1
)