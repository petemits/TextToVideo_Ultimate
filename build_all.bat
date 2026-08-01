@echo off
echo ====================================
echo Building Ultimate Text-to-Video System
echo ====================================

echo [1/8] Creating directory structure...
mkdir C++\bin 2>nul
mkdir output\frames 2>nul
mkdir output\videos 2>nul
mkdir logs 2>nul
mkdir temp 2>nul
mkdir cache 2>nul

echo [2/8] Installing Python dependencies...
cd Python
pip install -r requirements.txt
if errorlevel 1 (
    echo Python dependencies failed to install!
    pause
    exit /b 1
)
cd ..

echo [3/8] Installing Node.js dependencies...
cd Web
npm install
if errorlevel 1 (
    echo Node.js dependencies failed to install!
    pause
    exit /b 1
)
cd ..

echo [4/8] Compiling Java components...
cd Java
javac -cp ".\lib\commons-math3-3.6.1.jar" *.java
cd ..

echo [5/8] Building C++ components...
cd C++
make clean
make
cd ..

echo [6/8] Setting up configuration files...
copy Config\settings.json.example Config\settings.json
copy Config\prompts.json.example Config\prompts.json

echo [7/8] Creating necessary directories...
mkdir -p output\frames
mkdir -p output\videos
mkdir -p logs

echo [8/8] System build complete!
echo.
echo Run 'start_system.bat' to launch the system.
pause