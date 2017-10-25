@echo off
cd..
set BASEDIR=%CD%
set SERVICE_NAME=scoremark
set MONITOR_PATH=%BASEDIR%\bin\scoremark.exe

echo stop %SERVICE_NAME% 

%MONITOR_PATH% //MR//%SERVICE_NAME%

:end