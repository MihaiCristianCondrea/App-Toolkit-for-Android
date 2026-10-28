@echo off
setlocal
title App Toolkit Marketing UI
pushd "%~dp0" || exit /b 1
where node >nul 2>&1
if errorlevel 1 goto :missing_node
where npm.cmd >nul 2>&1
if errorlevel 1 goto :missing_node
rem Material Next is installed from its Git source until the next npm release.
where git >nul 2>&1
if errorlevel 1 goto :missing_git

echo Updating marketing UI dependencies...
call npm.cmd install --no-fund
if errorlevel 1 goto :failed
call npm.cmd run dev -- --open
if errorlevel 1 goto :failed
popd
exit /b 0

:missing_git
echo Git must be installed and available on PATH: Material Next is installed from its source until its next release.
goto :failed
:missing_node
echo Node.js and npm must be installed and available on PATH.
:failed
echo Marketing UI could not start. See the error above.
pause
popd
exit /b 1
