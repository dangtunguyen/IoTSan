@echo off
REM
REM  Set BANDERAHOME to Bandera root installation directory
REM    -the directory which contaings /build/Bandera.class
REM
IF NOT DEFINED BANDERAHOME set BANDERAHOME=.

REM
REM  Set JAVA_HOME to location of JDK 1.3 or higher
REM
IF NOT DEFINED JAVA_HOME set JAVA_HOME=C:\PROGRA~1\JDK1.31_02

REM
REM  The following variables should not have to change.
REM
set BANDERA_BUILD=%BANDERAHOME%\build
set JPFHOME=%BANDERAHOME%\SRC
set LOG4JHOME=%BANDERAHOME%\LIB\LOG4J
set OPENJGRAPHHOME=%BANDERAHOME%\LIB\OPENJGRAPH
set XERCESHOME=%BANDERAHOME%\LIB\XERCES
set GEFHOME=%BANDERAHOME%\LIB\GEF
set GNUHOME=%BANDERAHOME%\LIB\GNU

set LOG4JLIB=%LOG4JHOME%\log4j.jar
set OPENJGRAPHLIB=%OPENJGRAPHHOME%\openjgraph.jar
set XERCESLIB=%XERCESHOME%\xerces.jar
set GEFLIB=%GEFHOME%\gef.jar
set GNULIB=%GNUHOME%\gnu.jar

set JAVALIB=%JAVA_HOME%\lib\tools.jar;%JAVA_HOME%\jre\lib\rt.jar

set CLASSPATH=.;%BANDERA_BUILD%;%JPFHOME%;%LOG4JLIB%;%OPENJGRAPHLIB%;%XERCESLIB%;%JAVALIB%;%GEFLIB%;%GNULIB%

REM This lets us pass the arguments through without the "=" sign being stripped
IF NOT %2"==" %JAVA_HOME%\BIN\java -classpath %CLASSPATH% Bandera %1=%2 %3 %4 %5 %6 %9
IF %2"==" %JAVA_HOME%\BIN\java -classpath %CLASSPATH% Bandera %1 %2 %3 %4 %5 %6 %7 %8
