#! /bin/sh
#
# $Id$
#
# -------------------------------------------------------------------------
# *** DbForms Developer's swing based GUI ***
# *** start script for unix shells        ***
# 
# Environment Variable Prerequisites:
#
#   DBFORMS_HOME 
#
#                Note: This batch file does not function properly
#                if DBFORMS_HOME contains spaces.
#
#
#   CLASSPATH    keep in mind the XALAN, XERCES, and your JDBC driver should
# 		   be in classpath
#
#   JAVA_HOME    Must point at your Java Development Kit installation.
#
# -------------------------------------------------------------------------
#
# A T T E N T I O N
#
#
# this is a first port of devgui.bat (dos/windows batch file) to unix shell
# as i have no unix box in my developing environment, i did not test it 
#
# So, if it does not work, here a brief description of _WHAT_ it should do:
#
# 1. check if environment variable DBFORMS_HOME is set
# 2. append ${DBFORMS_HOME}/lib/dbforms_v09d.jar; to CLASSPATH
# 3. start DbForms- GUI using java interpreter
# 4. unset CLASSPATH after execution of DbForms GUI
#
#
# (many statements of this script where taken from tomcat-startup script)
#
#
# PLEASE REPORT COMMENTS, BUGs ENHANCEMENTS TO <jdbforms-developers@lists.sourceforge.net> !



if [ "$DBFORMS_HOME" = "" ] ; then
    echo DBFORMS_HOME not set, you need to set it or install in a standard location
    exit 1
fi

if [ -z "$JAVA_HOME" ] ;  then
  JAVA=`which java`
  if [ -z "$JAVA" ] ; then
    echo "Cannot find JAVA. Please set your PATH."
    exit 1
  fi
  JAVA_BINDIR=`dirname $JAVA`
  JAVA_HOME=$JAVA_BINDIR/..
fi

if [ "$JAVACMD" = "" ] ; then 
   # it may be defined in env - including flags!!
   JAVACMD=$JAVA_HOME/bin/java
fi

oldCP=$CLASSPATH
CLASSPATH=${CLASSPATH}:${DBFORMS_HOME}/lib/dbforms_v09d.jar;

export CLASSPATH

# joepeer:
# i do not know if classpath is needed as argument on unix shells

$JAVACMD -DDBFORMS_HOME=${DBFORMS_HOME} org.dbforms.devgui.DevGui

if [ "$oldCP" != "" ]; then
    CLASSPATH=${oldCP}
    export CLASSPATH
else
    unset CLASSPATH
fi
