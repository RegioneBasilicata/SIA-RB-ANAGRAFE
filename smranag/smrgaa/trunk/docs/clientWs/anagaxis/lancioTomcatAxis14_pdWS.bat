rem #########################################################################################################################
rem ##           Pochettino  -  4 Settembre 2014                                                                           ##
rem ##     File per generare un client Web Services secondo l'articolo :                                                   ##
rem ##     http://kbt.csi.it/csi/modalita-di-fruizione/descrizione-componenti/item/989-passaggio-da-pa/pd-a-ws-con-pd      ##
rem #########################################################################################################################

rem ------ CONFIGURAZIONI ------
set JAVA_HOME=C:\Vecchio_pc\Installazioni\Java\jdk1.8.0_65
set AXIS_LIB=C:\workspace\smrcomm\lib
set AXISCLASSPATH=%AXIS_LIB%\axis-1.4.csi-1.0.0.jar;%AXIS_LIB%\commons-discovery-0.2.jar;%AXIS_LIB%\commons-logging-1.1.1.jar;%AXIS_LIB%\saaj-impl-1.3.2.jar;%AXIS_LIB%\log4j-1.2.15.jar;%AXIS_LIB%\wsdl4j-1.6.2.jar;%AXIS_LIB%\axis-jaxrpc-1.4.jar
set CLASSPATH=%CLASSPATH%;C:\csi\Java\jdk1.8.0_73_64bit\bin

set ENDPOINT=http://localhost:8080/anagWsfad/services/anagaxis?wsdl
set WORKDIR=C:\workspace\smrgaa\docs\clientWs\anagaxis
set JARFILENAME=smrgaaweb-anagaxis-axisClient-1.0.0.jar
set DESTCOMPILEDFILE=C:\workspace\smrgaa\docs\clientWs\anagaxis\compiled

rem ------ CREAZIONE CLIENT WS ------
java -cp %AXISCLASSPATH% org.apache.axis.wsdl.WSDL2Java -p it.csi.solmr.dto.anagaxis.axisgen %ENDPOINT% -v
rem create classi client Web Services

rem ------ COMPILAZIONE CLASSI ------
rem compilo tutti i file dichiarati nel file fileTocompile.txt
copy fileTocompile.txt "%JAVA_HOME%\bin"
cd %JAVA_HOME%\bin\
javac -d %DESTCOMPILEDFILE% -g -verbose -cp %AXISCLASSPATH% @fileTocompile.txt

rem ------ CREAZIONE JAR ------
jar cvfm %WORKDIR%\%JARFILENAME% %WORKDIR%\manifest-addition.txt -C %WORKDIR%\compiled\ it\
rem JAR creato

cd %WORKDIR%
