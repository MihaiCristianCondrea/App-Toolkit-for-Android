@echo off
setlocal
title App Toolkit Motion Studio

rem Double-click to open Remotion Studio for this UI motion starter.
rem It prepares the UI kit first, because the videos render its live screens.

where npm >nul 2>nul
if errorlevel 1 (
    echo Node.js was not found. Install it from https://nodejs.org and try again.
    pause
    exit /b 1
)

cd /d "%~dp0..\..\..\ui" || (
    echo Could not find marketing\ui.
    pause
    exit /b 1
)
if not exist "node_modules" (
    echo Installing UI kit dependencies, first run only...
    call npm install
    if errorlevel 1 goto :failed
)
call npm run assets
if errorlevel 1 goto :failed

cd /d "%~dp0" || (
    echo Could not open the folder of this file.
    pause
    exit /b 1
)
if not exist "node_modules" (
    echo Installing Remotion dependencies, first run only...
    call npm install
    if errorlevel 1 goto :failed
)

echo Starting Remotion Studio, it opens in your browser when ready...
call npm run dev
if errorlevel 1 goto :failed
exit /b 0

:failed
echo.
echo Motion Studio failed to start. See the messages above.
pause
exit /b 1
