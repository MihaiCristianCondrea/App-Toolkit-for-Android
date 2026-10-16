@echo off
title Marketing UI
cd /d "%~dp0"
if not exist node_modules (
    echo Installing dependencies...
    call npm install
)
call npm run dev -- --open
