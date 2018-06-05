/*
This is a security aware application. This file provides information about the options you have
to facilitate or switch off security using your concrete application server or JSP/servlet engine.
[This document is not finished yet.]
*/


There are some alternatives you may choose from:


I. If you DO NOT want to use security then you have to modify dbforms-config and web.xml:

======================
DBFORMS-CONFIG.XML
======================

* remove all "granted-privileges" - elements

======================
WEB.XML
======================

* remove all <security-role>, <login-config>, <security-constraint> - elements and their subelements.

* update welcome-file-list so that it looks like this:

  <welcome-file-list>
    <welcome-file>menu.jsp</welcome-file>
  </welcome-file-list>  
  
  


II. If you DO want to use security but you are running into troubles with your application
server

in future distributions practical examples for several application servers will be added.
Installation reports from users will be added here as well.
Please drop me a line to <j.peer@gmx.net>

[for now, here a few hints - if you 've got more detailed info, please contribute it]


TOMCAT: additional to the static definition of user roles you may try the JDBC driven UserManger 
done by Apache Group. This enables dynamic userbases without any additional programming.
Info about Tomcat and Security can be found at:
http://jakarta.apache.org/tomcat/jakarta-tomcat/src/doc/JDBCRealm.howto
http://jakarta.apache.org/tomcat/jakarta-tomcat/src/doc/uguide/tomcat-security.html

ORIONSERVER: this should be straightforward - just alter Principals.xml to add the user roles.

more info about OrionServer + Security:
http://www.orionserver.com 
http://www.jollem.com/~ernst/orion-security-primer


