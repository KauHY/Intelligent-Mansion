@echo off
setlocal
chcp 65001 >nul
cd /d "%~dp0"
echo [START] Face check-in bridge live log
echo [TIP] This window tails pc_bridge\device_control.log. Press Ctrl+C to exit.
if not exist "pc_bridge\device_control.log" (
  echo [WAIT] No log file yet. Start recognition from the meeting detail page.
  pause
  exit /b 1
)
powershell -NoProfile -ExecutionPolicy Bypass -Command "Get-Content -Path 'pc_bridge\device_control.log' -Tail 200 -Wait"
echo [EXIT] Live log stopped.
pause