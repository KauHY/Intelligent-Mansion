@echo off
setlocal
chcp 65001 >nul
cd /d "%~dp0"
set MEETING_ID=%1
if "%MEETING_ID%"=="" (
  echo Usage: Start-Ethernet-Checkin.cmd ^<meetingReserveId^>
  echo Example: Start-Ethernet-Checkin.cmd 2112867938806198274
  pause
  exit /b 1
)
echo [START] XiUOS Ethernet face check-in bridge
echo [PROJECT] %CD%
echo [MEETING] %MEETING_ID%
if exist ".venv\Scripts\python.exe" (
  ".venv\Scripts\python.exe" -X utf8 -u pc_bridge\api_bridge.py --listen-port 9000 --api-base http://localhost:8099 --device-id k210-001 --device-token board-token --meeting-reserve-id %MEETING_ID% %2 %3 %4 %5
) else (
  py -3 -X utf8 -u pc_bridge\api_bridge.py --listen-port 9000 --api-base http://localhost:8099 --device-id k210-001 --device-token board-token --meeting-reserve-id %MEETING_ID% %2 %3 %4 %5
)
echo [EXIT] Check-in bridge stopped.
pause
