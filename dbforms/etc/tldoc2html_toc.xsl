<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- Create table of contents for HTML Tag Library Documentation -->
<!-- First version 2002-11-1 (dikr@users.sourceforge.net)        -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0">

  <!-- Output method and formatting -->
  <xsl:output
             method="html"
             indent="yes"
  />
  <xsl:param name="sortTagsAlphabetically"/>

  <xsl:strip-space elements="taglib tag attribute"/>

  <!-- =========================================================== -->
  <!-- template for document					   -->
  <!-- =========================================================== -->
  <xsl:template match="document">
     <html>
     <head>
     <link rel="stylesheet" href="taglib-stylesheet.css" type="text/css"/>
     </head> 
     <body vlink="#023264" alink="#023264" link="#023264"
           text="#000000" bgcolor="#ffffff">
       <xsl:apply-templates  select="body/taglib"/>
     </body>
     </html>
  </xsl:template>

  <!-- =========================================================== -->
  <!-- template for body/taglib                                    -->
  <!-- =========================================================== -->
  <xsl:template match="body/taglib">

   <a href="http://jdbforms.sourceforge.net" 
       alt="DbForms Homepage" 
       target="_top">
    <img border="0" src="dbforms_logo.png" width="116" heigth="25"/>
   </a>
   <br/>
    <xsl:if test="tlibversion">
      <em>Tag Library Version:</em>
      <xsl:value-of select="tlibversion"/>
      <br/>
    </xsl:if>
      
  <!-- table of contents  -->

      <xsl:choose>

       <xsl:when test="$sortTagsAlphabetically = 'true'">
         <p/>
          <a href="DbFormsTags_TocSem.html"><xsl:text>Use semantic order</xsl:text></a>
         <p/>

         <xsl:for-each select="tag">
           <xsl:sort select="name"/>
           <a target="referencedoc" style="text-decoration:none;">
           <xsl:attribute name="href">
            <xsl:text>DbFormsTags.html#</xsl:text>
            <xsl:value-of select="name"/>
           </xsl:attribute>
           <xsl:value-of select="name"/>
           </a><br/>
        </xsl:for-each>
      </xsl:when>

      <xsl:otherwise>
         <p/>
          <a href="DbFormsTags_TocAlph.html"><xsl:text>Use alphabetical order</xsl:text></a>
         <p/>

        <xsl:for-each select="tag">
           <a target="referencedoc" style="text-decoration:none;">
           <xsl:attribute name="href">
            <xsl:text>DbFormsTags.html#</xsl:text>
            <xsl:value-of select="name"/>
           </xsl:attribute>
           <xsl:value-of select="name"/>
           </a><br/>
       </xsl:for-each>
      </xsl:otherwise>

     </xsl:choose>


   </xsl:template>

</xsl:stylesheet>
