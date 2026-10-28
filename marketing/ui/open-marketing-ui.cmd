@echo off
setlocal
title App Toolkit Marketing UI
pushd "%~dp0" || exit /b 1
where node >nul 2>&1
if errorlevel 1 goto :missing_node
where npm.cmd >nul 2>&1
if errorlevel 1 goto :missing_node

echo Updating marketing UI dependencies...
call npm.cmd install --no-fund
if errorlevel 1 goto :failed
call npm.cmd run dev -- --open
if errorlevel 1 goto :failed
popd
exit /b 0

:missing_node
echo Node.js and npm must be installed and available on PATH.
:failed
echo Marketing UI could not start. See the error above.
pause
popd
exit /b 1
