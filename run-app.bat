@echo off
setlocal
set "JAVA_HOME=C:\Program Files\Java\jdk-25.0.4.1"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d "%~dp0"

echo ==============================================
echo Khoi dong Ung dung Shop Web (Spring Boot) - Java 25 LTS
echo ==============================================

for /f "tokens=5" %%a in ('netstat -aon 2^>nul ^| findstr :8080 ^| findstr LISTENING') do (
    echo [INFO] Phat hien cong 8080 dang duoc su dung boi PID %%a. Dang giai phong cong...
    taskkill /F /PID %%a >nul 2>&1
    timeout /t 1 /nobreak >nul
)

if not exist "target\shop-asm-1.0.0.jar" (
    echo [INFO] Dang dong goi ung dung shop-asm-1.0.0.jar...
    call .\mvnw.cmd package -DskipTests
)

echo [INFO] Dang khoi dong Web tai http://localhost:8080 ...
echo [INFO] Nhan Ctrl+C de dung ung dung bat ky luc nao.
echo ----------------------------------------------
java "-Dnet.bytebuddy.experimental=true" -jar "target\shop-asm-1.0.0.jar"
pause
