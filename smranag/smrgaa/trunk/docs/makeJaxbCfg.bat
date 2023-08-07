@echo off
set PATH=%JAVA_HOME%\bin\;%PATH%

rem call "C:\Program Files\ant\ant-1.6.2_Bea9\bin\ant5.bat" -f buildfiles\buildJaxbCfg.xml -Dfascicolo=true -Daltrastampa=true
call "C:\Program Files\ant\ant-1.6.2_Bea9\bin\ant5.bat" -f buildfiles\buildJaxbCfg.xml -Dfascicolo=true

@rem if %errorlevel% == 1 pause
pause