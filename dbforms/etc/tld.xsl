<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- Convert Tag Library Documentation into Tag Library Descriptor -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!-- Output method and formatting -->
  <xsl:output
             method="xml"
             indent="yes"
     doctype-public="-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN"
     doctype-system="http://java.sun.com/dtd/web-jsptaglibrary_1_2.dtd"
   />
   <xsl:strip-space elements="taglib tag attribute"/>

  <!-- Process an entire tag library -->
  <xsl:template match="taglib">
    <taglib>
      <xsl:if test="tlibversion">
        <tlib-version><xsl:value-of select="tlibversion"/></tlib-version>
      </xsl:if>
      <xsl:if test="jspversion">
        <jsp-version><xsl:value-of select="jspversion"/></jsp-version>
      </xsl:if>
      <xsl:if test="shortname">
        <short-name><xsl:value-of select="shortname"/></short-name>
      </xsl:if>
      <xsl:if test="uri">
        <uri><xsl:value-of select="uri"/></uri>
      </xsl:if>
      <xsl:if test="display-name">
        <display-name><xsl:value-of select="display-name"/></display-name>
      </xsl:if>
      <xsl:if test="info">
        <description><xsl:value-of select="info"/></description>
      </xsl:if>
      <xsl:apply-templates select="tag"/>
    </taglib>
  </xsl:template>

  <!-- Process an individual tag -->
  <xsl:template match="tag">
  
<xsl:text disable-output-escaping="yes">
&lt;!--*******************************************************************--&gt;
</xsl:text>
	
    <tag>
      <xsl:if test="name">
        <name><xsl:value-of select="name"/></name>
      </xsl:if>
      <xsl:if test="tagclass">
        <tag-class><xsl:value-of select="tagclass"/></tag-class>
      </xsl:if>
      <xsl:if test="teiclass">
        <tei-class><xsl:value-of select="teiclass"/></tei-class>
      </xsl:if>
      <xsl:if test="bodycontent">
        <body-content><xsl:value-of select="bodycontent"/></body-content>
      </xsl:if>
      <xsl:if test="summary">
        <description><xsl:value-of select="summary"/></description>
      </xsl:if>
      <xsl:apply-templates select="attribute"/>
      <xsl:if test="info">
        <example><xsl:value-of select="info"/></example>
      </xsl:if>
    </tag> 
  </xsl:template>

  <!-- Process an individual tag attribute -->
  <xsl:template match="attribute">
    <attribute>
      <xsl:if test="name">
        <name><xsl:value-of select="name"/></name>
      </xsl:if>
      <xsl:if test="required">
        <required><xsl:value-of select="required"/></required>
      </xsl:if>
      <xsl:if test="rtexprvalue">
        <rtexprvalue><xsl:value-of select="rtexprvalue"/></rtexprvalue>
      </xsl:if>
      <xsl:if test="type">
        <type><xsl:value-of select="type"/></type>
      </xsl:if>
      <xsl:if test="info">
        <description><xsl:value-of select="info"/></description>
      </xsl:if>
    </attribute>
  </xsl:template>

  <!-- Skip irrelevant details -->
  <xsl:template match="properties"/>

</xsl:stylesheet>