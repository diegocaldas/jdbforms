@echo off
rem -------------------------------------------------------------------------
rem *** DbForms Developer's swing based GUI ***
rem *** start script for win32 systems ***
rem 
rem Environment Variable Prerequisites:
rem
rem   DBFORMS_HOME 
rem
rem                Note: This batch file does not function properly
rem                if DBFORMS_HOME contains spaces.
rem
rem
rem   CLASSPATH    keep in mind the XALAN, XERCES, and your JDBC driver should
rem 		   be in classpath
rem
rem   JAVA_HOME    Must point at your Java Development Kit installation.
rem

rem -------------------------------------------------------------------------


rem ----- Save Environment Variables That May Change ------------------------

set _CP=%CP%
set _TOMCAT_HOME=%TOMCAT_HOME%
set _CLASSPATH=%CLASSPATH%;%DBFORMS_HOME%\lib\dbforms_v09d.jar;
set _RUNJAVA="%JAVA_HOME%\bin\java"

rem ----- Verify and Set Required Environment Variables ---------------------

if not "%JAVA_HOME%" == "" goto gotJavaHome
echo You must set JAVA_HOME to point at your Java Development Kit installation
goto cleanup
:gotJavaHome

if not "%DBFORMS_HOME%" == "" goto gotDbFormsHome
echo You must set JAVA_HOME to point at your Java Development Kit installation
goto cleanup

:gotDbFormsHome

%_RUNJAVA% -classpath %_CLASSPATH% -DDBFORMS_HOME=%DBFORMS_HOME% com.itp.dbforms.devgui.DevGui

:cleanup
set CLASSPATH=%_CLASSPATH%
set _CLASSPATH=