@echo off
setlocal
cd /d "%~dp0"
if exist ".venv\Scripts\python.exe" (
  ".venv\Scripts\python.exe" -u pc_bridge\tcp_bridge.py %*
) else (
  where py >nul 2>nul
  if errorlevel 1 (
    python -u pc_bridge\tcp_bridge.py %*
  ) else (
    py -3 -u pc_bridge\tcp_bridge.py %*
  )
)
pause
