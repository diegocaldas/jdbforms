I. About the example applications
====================================

BUGTRACKER 
------------
- a little sample application with real business logic
- this application is visible online at www.wap-force.net/bugtracker

TESTSUITE_V09
---------------
- an automatically generated application (using XSL transformation)
- has got no real "business logic", all that it does is providing a database interface on the web
- it is used primarily for testing code and demonstrating DbForms


II. Database installation
=============================


BOTH applications use their own database

you'll find SQL files for creating the databases in the  /WEB-INF/ directory of the respective
database application.

as this document was written, these SQL files existed for MYSQL + Oracle + MS SQL Server + InstantDB (dbformstest)
respective for MYSQL + Oracle (bugtracker)

the experienced
developer may translate the SQL statements into the dialect of his/her database system, if missing. 
... and it would be useful to contribute the resulting SQL to the community - 
send an email to: j.peer@gmx.net



III. Install the APPs on the server
-----------------------------------------


important: to keep the ZIP file small, no JAR files where placed into the /WEB-INF/lib/ of the 
examples. 

This means you have to copy the JARs manually to enable access to the libraries the apps will need!

You need to do the following:

1. copy dbforms_v09d.jar into the WEB-INF/lib directories of the web applications
2. download the "must have" - JAR files from http://www.wap-force.net/dbforms/dependend/
3. copy the downloaded JARs into the WEB-INF/lib directories of the web applications

(of course you can copy the JARs into the /lib directory of you application server or 
you can add them to your CLASSPATH manually.)



IV. Confiuring Application
---------------------------------

- make sure that init-parameter "log4j.configuration" points to a valid log4j -
  properties file (also check out DbForms user's manual for more information 
  about your WEB.XML file)


BUGTRACKER 
------------

- the bugtracker is a security-aware web-application. (this is necessary to demonstrate
  DbForms' security-related features) you must edit security data in you App/Web - Server 
  to fulfil the requirements defined in web.xml 
 
  check "readme_bugtracker_security.txt" for more info.
  NOTE: security is _Servlet/JSP_ relevant, not necessarily _DbForms_ relevant, please
  check Servlet 2.2 specificaton and the Doc/FAQ of your Application/Web-Server if you 
  are not familiar with these concepts!


TESTSUITE_V09
---------------

- table "pets" has got a DISKBLOB field and a BLOB field

- to use the DISKBLOB field in table "pets" you must edit dbforms-config.xml: 
  
  attribute "directory" in field-element "portrait_pic" must point to an existing directory
  on your system.

  check dbforms User's manual for more info

- feel free to re-generate the JSP views using the XSL transformation tool included in
  the DevGui Application!



Ok. that's it, enjoy it!

  
===============================================================================
if you experience troubles, do not hesitate to send an eMail: j.peer@gmx.net
===============================================================================