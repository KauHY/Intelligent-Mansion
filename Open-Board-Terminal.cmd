@echo off
setlocal
title XiUOS Board Terminal - COM3
cd /d "%~dp0"

echo XiUOS serial terminal: COM3 / 115200 / 8N1
echo Close K-Flash, the photo bridge, and other COM3 terminals first.
echo This script does NOT flash firmware.
echo.

if exist "%~dp0.venv\Scripts\python.exe" goto check_dependency

python --version >nul 2>&1
if not errorlevel 1 goto create_with_python
py -3 --version >nul 2>&1
if not errorlevel 1 goto create_with_py
echo ERROR: Python 3 was not found. Install Python 3 and try again.
goto failed

:create_with_python
echo Preparing the local Python environment...
python -m venv "%~dp0.venv"
if errorlevel 1 goto failed
goto check_dependency

:create_with_py
echo Preparing the local Python environment...
py -3 -m venv "%~dp0.venv"
if errorlevel 1 goto failed

:check_dependency
"%~dp0.venv\Scripts\python.exe" -c "import serial.tools.miniterm" >nul 2>&1
if not errorlevel 1 goto connect
echo Installing pySerial into the project environment. Internet is required once.
"%~dp0.venv\Scripts\python.exe" -m pip install -r "%~dp0pc_bridge\requirements.txt"
if errorlevel 1 goto failed

:connect
echo Press Ctrl+] to exit. Press ENTER if the shell prompt is not visible.
echo To see boot messages, press the board RESET button after connecting.
echo.
"%~dp0.venv\Scripts\python.exe" -m serial.tools.miniterm COM3 115200 --eol CR --dtr 0 --rts 0
if errorlevel 1 goto connection_failed
echo.
echo Terminal closed.
pause
exit /b 0

:connection_failed
echo.
echo Unable to use COM3. Check the USB cable and Device Manager port number.
echo Close K-Flash, bridge.py, and any other program using COM3, then retry.
goto failed

:failed
echo.
echo Startup failed. See the error above. Double-click this script to retry.
pause
exit /b 1
