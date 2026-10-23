@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-25.0.4.1"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd /d "%~dp0"
echo ==============================================
echo Chay Selenium UI Tests (TestNG POM) - Java 25 LTS
echo ==============================================
call .\mvnw.cmd test -Pui
pause
