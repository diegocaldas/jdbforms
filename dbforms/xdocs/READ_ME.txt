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

- use the Putty package (only if you use WinCVS 1.2 not with WinCVS 1.3!):
   http://www.chiark.greenend.org.uk/~sgtatham/putty/

- set the CVS_RSH env var to (only if you use WinCVS 1.2 not with WinCVS 1.3!):
   <my_path_to_putty_directory>\plink.exe

   Example:
   CVS_RSH = D:\progs\putty\plink.exe

- set your default settings to your sourceforge accout!

- set your path so that maven can find the cvs command! For WinCVS 1.2 this is the WinCVS dir, for WinCVS 1.3 it's
  the WinCVS/CVSNT dir!

                   
That's all !


MAVEN changes report
--------------------
Maven can generate a nice changes report for DbForms.
Here's the plugin's doc: http://maven.apache.org/reference/index.html


MAVEN dbforms goals
--------------------
We have integrated some special goals for dbforms:
  - dbforms:doc
    generate dbforms documentation: taglib docu and manual
  - dbforms:dist
    generates the distribution via build.xml
	-dbforms:release
	  generates a new release on SF
  -dbforms:update-project
    gets a fresh copy from the CVS

And the masters:

  - dbforms:site:generate
    This goals gets a fresh copy of all sources from the CVS, builds a new 
    version locally
	
  - dbforms:site:deploy
    This goals gets a fresh copy of all sources from the CVS, builds a new 
    version and sends it to the SF web server.      

  - dbforms:site:release
    Same as dbforms:site:deploy. After renewing dbforms website a new version
    of dbforms is released to SF.


needed MAVEN plugins
-----------------------
You need to install newest cactus plugin:

maven plugin:download -DartifactId=cactus-maven -DgroupId=cactus -Dversion=1.7

see http://jakarta.apache.org/cactus/integration/maven/ for newes version!

jdk 1.5
--------------------------
To run maven with jdk 1.5 you must copy the xalan-2.5.1.jar to ${MAVEN_HOME}/lib/endorsed!