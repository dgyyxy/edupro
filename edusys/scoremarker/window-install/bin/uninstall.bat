@echo off
cd..
set BASEDIR=%CD%
set SERVICE_NAME=scoremark
set "SRV=%BASEDIR%\bin\prunsrv.exe"

echo %SERVICE_NAME%

%SRV% //DS//%SERVICE_NAME%

:end