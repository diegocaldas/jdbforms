Note: The DocBook_UsersGuide has relative links to the Tag Library that will be broken if either resource is moved.  Specifically, there are 3-4 places (in sorting, taglib apendix etc) where ../../taglib/DbFormsTags_Frame.html is used within the UsersGuide to link to the tag library.

To build it, you will need to do three things:

 
1)  in your environment set DOCBOOK_HOME

    -or-
    
    in dbforms/docs.xml set <property name="docbookHome.dir" value="your-path-to-your/DOCBOOK_HOME"/>

    USE A RECENT VERSION SUCH AS docbook-xsl-1.60.1
    __________________________________________________________________________________________________


2)  in your environment set FOP_HOME

    -or-

    in dbforms/docs.xml set <property name="fopHome.dir" value="your-path-to-your/FOP-HOME"/>                

    USE A RECENT VERSION SUCH AS fop-0.20.5rc2
    __________________________________________________________________________________________________


3)  Add image support for FOP. The Jimi image library, which is by default used for processing images in PNG and other formats, was removed from the distribution for licensing reasons. So...:

    Either

        a) obtain Jimi from http://java.sun.com/products/jimi/ and Extract the file "JimiProClasses.zip" from the archive you've downloaded, rename it to "jimi-1.0.jar" and move it to FOP's lib directory.
            
            (I wasn't able to get Jimi working but your luck may be different)

    OR

        b) obtain JAI from http://developer.java.sun.com/developer/earlyAccess/jai/index.html and follow the installation instructions for a JRE install there.
            
            (much faster, but not available for all platforms)

_____________________________________________________________________________________________

A DOCBOOK_HOME can be obtained at http://sourceforge.net/project/showfiles.php?group_id=21935

A FOP_HOME can be obtained at http://xml.apache.org/fop/download.html 

______________________________________________________________________________________________

IF YOU DO NOT WANT A DOCBOOK_HOME, YOU CAN RESET THE FOLLOWING IN dbforms/docs.xml (SLOWER)

<!-- Main Docbook stylesheet  /> -->
<property name="main.stylesheet" value="${sSheetHome.dir}\html\chunk.xsl"/> 1        
 
<!-- Stylesheet to use for fo processing  -->
<property name="fo.stylesheet" value="${sSheetHome.dir}\fo\docbook.xsl"/> 2

***  REPLACE THE ABOVE VALUES WITH THE ONES BELOW 

1  -----   value="http://docbook.sourceforge.net/release/xsl/current/html/chunk.xsl" is for non-local resolution


2  -----   value="http://docbook.sourceforge.net/release/xsl/current/fo/docbook.xsl"/> is for non-local resolution 

__________________________________________________________________________________________________

NO FOP_HOME MEANS NO PDF CAN BE CREATED