@echo off
title Text-to-Video Ultimate System

echo Starting Ultimate Text-to-Video System...
echo.

REM Start Python AI Server
start "Python AI Server" cmd /k "cd Python && python server.py"

REM Start Java Effects Server
start "Java Effects Server" cmd /k "cd Java && java -cp ".;lib\commons-math3-3.6.1.jar" EffectsServer"

REM Start Web Server
start "Web Interface" cmd /k "cd Web && node server.js"

REM Start Main Launcher
timeout /t 5
echo Launching main system interface...
python launcher.py

pause