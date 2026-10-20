@echo off
setlocal
chcp 65001 >nul
cd /d "%~dp0"
echo [START] XiUOS Wi-Fi photo receiver
echo [PROJECT] %CD%
echo [INFO] Waiting for Python to print the TCP listening address...
if exist ".venv\Scripts\python.exe" (
  ".venv\Scripts\python.exe" -X utf8 -u pc_bridge\tcp_bridge.py %*
) else (
  where py >nul 2>nul
  if errorlevel 1 (
    python -X utf8 -u pc_bridge\tcp_bridge.py %*
  ) else (
    py -3 -X utf8 -u pc_bridge\tcp_bridge.py %*
  )
)
echo [EXIT] Receiver stopped. Check any error message above.
pause
