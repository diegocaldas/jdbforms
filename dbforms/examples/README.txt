Instructions for Setting Up the Example Web Applications
++++++++++++++++++++++++++++++++++++++++++++++++++++++++

I. About the Example Web Applications
=====================================

BUGTRACKER
------------
This web application uses real business logic. It can also be
accessed online at http://www.wap-force.net/bugtracker.

TESTSUITE
-------------
This application contains no business logic; instead, it demonstrates
pure table inserts, updates, and deletes via JSP pages that use
DbForms JSP tags. The numerous JSPs for this Web application were automatically generated; for
more information on how, see Appendix III of the DbForms User Guide.
TESTSUITE is useful for testing code and demonstrating DbForms.

BOOKSTORE
------------
Some hand made examples to show and test some special features of dbforms, 
e.g. aliases, querys, filters, xml data source. This examples are for educational purpose only.
Not a real application, no use of devgui. Just a mixture of small examples.



II. Database installation
=============================
Each example web application requires its own separate database.

- Create a database on your database server for each application.
- For each database, create a user and password with read and write access to that database.
- Create and populate the tables need by the application.
-- The /WEB-INF/ directory of each application contains SQL scripts that create and populate the tables.
   Each script is named after the database program it is written for. For example,
   dbformstest_MSSQLServer.sql is for Microsoft SQL Server; simply run it in MS SQL Query
   Analyzer.
-- If there is no script for your database program, and you succeed in writing one for it
   yourself, we will be glad to make it available to all DbForms users. Simply send
   an e-mail to j.peer@gmx.net.

Note for Microsoft SQL Server: make sure that the user you create has a default
database. This is because, as you will see below, you cannot tell DbForms the
name of the database to connect to. DbForms will use whatever database the user
has access to by default.


III. Installing required classes on your servlet server
--------------------------------------------------------
In order to keep the DbForms ZIP file small, no JAR files where placed in the /WEB-INF/lib/
directories of the example web applications. Instead, you must download some of them separately,
and copy them in their correct directories.

Assume the directory you expanded the DbForms directory into is /dbformsDir/.

- Copy /dbformsDir/dist/dbforms*.jar into the /WEB-INF/lib directories of the example web applications.
- Download the "must have" JAR files from http://www.wap-force.net/dbforms/dependend/.
- Copy the "must have" JARs into the WEB-INF/lib directories of the example web applications.

(Alternatively, you can of course copy these JARs into the /lib directory of you application server or
you can add them to your CLASSPATH manually.)


IV. Configuring the Example Web Application
-------------------------------------------

- In the /WEB-INF/web.xml of each example web application, make sure that
  <init-param> tag for "log4j.configuration" points to a valid log4j
  properties file. If you do not want to use log4j, leave this tag commented out.

The DbForms User's Guide contains additional information on your /WEB-INF/web.xml file.


V. Configuring DbForms
----------------------
You must edit the <dbconnection> tag of /WEB-INF/dbforms-config.xml. Since there
are at least four different ways to connect to a database, each requiring
a fair amount of explanation, they are not listed here.
Please refer to the DbForms User's Guide, under "Defining physical access to the model".


VI. BUGTRACKER Specific Information
-------------------------------
For BUGTRACKER, you must configure your servlet or Web server to accommodate the security settings
in /WEB-INF/web.xml. Please see the file "readme_bugtracker_security.txt" for more
information; it should be in the same directory as the file you are currently reading.

Note the DbForms does not provide any security functions. The security features
in BUGTRACKER are simply standard servlet/JSP features. If you are not familiar with
servlet/JSP security, see the latest Servlet specifications, or your favorite servlet or
JSP book, and/or the documentation that comes with your servlet/application/web server.


VI. TESTSUITE_V09 Specific Information
--------------------------------------
Version 9 of TESTSUITE contains the following change:

- table "pets" now has a DISKBLOB field and a BLOB field.

(To use the DISKBLOB field in table "pets" you must edit dbforms-config.xml so that the
attribute "directory" in field-element "portrait_pic" points to an existing directory
on your system. The DbForms User's Guide contains more information on this.)

Finally, feel free to re-generate all the TESTSUITE JSP pages using the XSL transformation tool included in
the DevGui Application! For more information on how to do this, see Appendix III of the
DbForms User's Guide.

VII. PROBLEMS
--------------------------------------
If you have any problems with the example web applications, don't hesitate to
send me e-mail at j.peer@gmx.net. You can also post to the DbForms mailing list;
for more information, visit www.dbforms.org.


Ok, that's it! Enjoy!

