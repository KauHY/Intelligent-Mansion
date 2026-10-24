@echo off
setlocal
chcp 65001 >nul
cd /d "%~dp0"
echo [START] Local face check-in device control
set PYTHON=%TEMP%\uv-python\cpython-3.12.13-windows-x86_64-none\python.exe
if exist "%PYTHON%" goto run
set PYTHON=%LOCALAPPDATA%\Programs\Python\Python313\python.exe
if exist "%PYTHON%" goto run
set PYTHON=python
:run
"%PYTHON%" -X utf8 -u pc_bridge\device_control.py --port 9010 --board-port COM5 --board-baud 115200
echo [EXIT] Device control stopped.
pause
