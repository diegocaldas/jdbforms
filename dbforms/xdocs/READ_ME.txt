__________________________________________________________________________________________________

MAVEN SITE GENERATION
---------------------
a) get and install the latest Turbine-Maven distribuition
    http://maven.apache.org

b.0) put $MAVEN_HOME/bin directory into your system PATH;
b.1) put your cvs.exe client program into your system PATH;

c) > cd $DBFORMS_HOME
    $DBFORMS_HOME > maven site:generate

d) wait a bit... ;^)

A successfull Maven build process creates the $DBFORMS_HOME/target/docs 
directory; to view the new site load the 
$DBFORMS_HOME/target/docs/index.html file into your web browser.

WIN32: MAVEN CVS operations on SourceForge and SSH
--------------------------------------------------
To generate some "Project Reports" tasks (i.e.: ChangeLog, Developer 
Activity, etc), your CVS client must get a connection to the 
SourceForge's CVS server. If you are a developer with CVS access, 
probably you have got a SSH cvs access - and you want to use it ! ;^). To let Maven access to SourceForge's CVS using an SSH connection on 
win32 platforms, you could:

- use the Putty package
   http://www.chiark.greenend.org.uk/~sgtatham/putty/

- set the CVS_RSH env var to:
   <my_path_to_putty_directory>\plink.exe

   Example:
   CVS_RSH = D:\progs\putty\plink.exe

- set your default settings to your sourceforge accout!

That's all !


MAVEN changes report
--------------------
Maven can generate a nice changes report for DbForms.
Here's the plugin's doc: http://maven.apache.org/reference/index.html


MAVEN dbforms goals
--------------------
We have integrated 3 special goals for dbforms:
  - dbforms:doc
    generate dbforms documentation: taglib docu and manual
  - dbforms:dist
    generates the distribution via build.xml
  - dbforms:deploy
    do the whole deployment: generates site, generates doc, generates distribution

