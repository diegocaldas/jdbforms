The DocBook_UsersGuide is still under construction.

To build it, you will need to do three things:

1)  in dbforms/docs.xml set  <property name="UsersGuide.dir" value="your-path-to/dbforms/usersguide_src"/> 

2)  in your environment set DOCBOOK_HOME

    -or-
    
    in dbforms/docs.xml set <property name="docbookHome.dir" value="your-path-to-your-DOCBOOK_HOME"/>

3)  in your environment set FOP_HOME

    -or-

    in dbforms/docs.xml set 

        a)set <fileset dir="your_path_to_your_FOP_HOME/lib/"> 

        and

        b)set <fileset dir="your_path_to_your_FOP_HOME/build/"> 

_____________________________________________________________________________________________

A DOCBOOK_HOME can be obtained at http://sourceforge.net/project/showfiles.php?group_id=21935

A FOP_HOME can be obtained at http://xml.apache.org/fop/download.html 

______________________________________________________________________________________________

IF YOU DO NOT WANT A DOCBOOK_HOME, YOU CAN SET THE FOLLOWING IN dbforms/docs.xml (SLOWER)

<!-- Main Docbook stylesheet  /> -->
<property name="main.stylesheet" value="${sSheetHome.dir}\html\chunk.xsl"/> 1        
 
<!-- Stylesheet to use for fo processing  -->
<property name="fo.stylesheet" value="${sSheetHome.dir}\fo\docbook.xsl"/> 2

***  REPLACE THE ABOVE VALUES WITH THE ONES BELOW 

1  -----   value="http://docbook.sourceforge.net/release/xsl/current/html/chunk.xsl" is for non-local resolution


2  -----   value="http://docbook.sourceforge.net/release/xsl/current/fo/docbook.xsl"/> is for non-local resolution 

__________________________________________________________________________________________________

NO FOP_HOME MEANS NO PDF CAN BE CREATED