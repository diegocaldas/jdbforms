<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- Convert Tag Library Documentation into HTML Tag Library Documentation -->
<!-- First version 2002-10-15 (dikr@users.sourceforge.net)                 -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0">

  <!-- Output method and formatting -->
  <xsl:output
             method="html"
             indent="yes"
  />

  <xsl:strip-space elements="taglib tag attribute"/>

  <!-- =========================================================== -->
  <!-- template for document					   -->
  <!-- =========================================================== -->
  <xsl:template match="document">
     <html>
     <head>
       <xsl:apply-templates  select="properties" mode="htmlheader"/>
     </head> 
     <body vlink="#023264" alink="#023264" link="#023264"
           text="#000000" bgcolor="#ffffff">
       <xsl:apply-templates  select="properties" mode="header"/>
       <xsl:apply-templates  select="body/taglib"/>
     </body>
     </html>
  </xsl:template>

  <!-- =========================================================== -->
  <!-- template for properties, mode "htmlheader"                  -->
  <!-- =========================================================== -->
  <xsl:template match="properties" mode="htmlheader"> 
   <meta name="author">
   <xsl:attribute name="content"><xsl:value-of select="author"/></xsl:attribute> 
   </meta>
   <title><xsl:value-of select="title"/></title>
  </xsl:template>

  <!-- =========================================================== -->
  <!-- template for properties, mode "header"                      -->
  <!-- =========================================================== -->
  <xsl:template match="properties" mode="header">
   <font size="+1" color="#023264"><strong>
     <xsl:value-of select="title"/>
   </strong></font><br/>
  </xsl:template>

  <!-- =========================================================== -->
  <!-- template for body/taglib                                    -->
  <!-- =========================================================== -->
  <xsl:template match="body/taglib">

    <xsl:if test="tlibversion">
      <em>Tag library version:</em>
      <xsl:value-of select="tlibversion"/>
      <br/>
    </xsl:if>
      
  <!-- Create table containing table of contents  -->

   <blockquote>
     <table cellpadding="2" cellspacing="2" border="1">
       <tr><th>Tag name</th><th>Description</th></tr>
       <xsl:for-each select="tag">
        <tr> <td align="center">
           <a>
           <xsl:attribute name="href">
            <xsl:text>#</xsl:text>
            <xsl:value-of select="name"/>
           </xsl:attribute>
           <xsl:value-of select="name"/>
           </a>
          </td>
          <td>
           <xsl:if test="summary"><xsl:value-of select="summary"/> </xsl:if>
          </td>
       </tr>
      </xsl:for-each>
     </table>
   </blockquote>

   <xsl:apply-templates select="tag"/>
   </xsl:template>

  <!-- =========================================================== -->
  <!-- template for tag                                            -->
  <!-- =========================================================== -->
  <xsl:template match="tag">
    <xsl:if test="name">
       <a>
        <xsl:attribute name="name">
          <xsl:value-of select="name"/>
        </xsl:attribute>
        <strong>
          <xsl:value-of select="name"/>
        </strong>
       </a>
   </xsl:if> 

    <xsl:if test="summary">
        <xsl:text> - </xsl:text>
        <em><xsl:value-of select="summary"/></em> 
    </xsl:if>

    <blockquote>
    <xsl:if test="info"><p><xsl:apply-templates select="info"/></p> </xsl:if>

     <xsl:choose>
      <xsl:when test="count(attribute)>0">
         <table width="95%" align="center" cellpadding="2" cellspacing="2" border="1">
            <tr><th width="15%">Attribute Name</th><th>Description</th></tr>
            <xsl:apply-templates select="attribute"/>
        </table>
      </xsl:when>
      <xsl:otherwise><xsl:text>This tag has no attributes.</xsl:text><p/></xsl:otherwise>
    </xsl:choose>

    </blockquote>
  </xsl:template>

  <!-- =========================================================== -->
  <!-- template for attribute                                      -->
  <!-- =========================================================== -->
  <xsl:template match="attribute">
    <tr><td valign="top" align="center">
          <xsl:if test="name"><xsl:value-of select="name"/></xsl:if>
        </td>
        <td>
            <xsl:choose>
             <xsl:when test="info"> 
                 <p><xsl:apply-templates select="info"/></p>
             </xsl:when>
             <xsl:otherwise>
                 <xsl:text disable-output-escaping="yes">&amp;nbsp; </xsl:text>
             </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="required = 'true'">
               <xsl:text disable-output-escaping="yes">[Required]</xsl:text>
            </xsl:if>
            <xsl:if test="rtexprvalue = 'true'">
               <xsl:text disable-output-escaping="yes">[RTExpValue]</xsl:text>
            </xsl:if>
        </td>
    </tr>
  </xsl:template>

  <!-- =========================================================== -->
  <!-- templates for several html tags                             -->
  <!-- =========================================================== -->
  <xsl:template match="p"><p><xsl:apply-templates/></p></xsl:template>
  <xsl:template match="pre"><pre><xsl:apply-templates/></pre></xsl:template>
  <xsl:template match="ul"><ul><xsl:apply-templates/></ul></xsl:template>
  <xsl:template match="li"><li><xsl:apply-templates/></li></xsl:template>
  <xsl:template match="br"><br><xsl:apply-templates/></br></xsl:template>
  <xsl:template match="b"><b><xsl:apply-templates/></b></xsl:template>
</xsl:stylesheet>
