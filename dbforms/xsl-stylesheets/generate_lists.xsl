<?xml version='1.0'?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:db="http://www.wap-force.com/dbforms">

<!--
definition of variables
choose appropriate values that fit your needs
-->

<xsl:variable name="maxRows">*</xsl:variable>
<xsl:variable name="pageBgColor">99CCFF</xsl:variable>

  <xsl:template match="table">
//--file "<xsl:value-of select="@name"/>_list.jsp" -------------------------------------------
  <xsl:text disable-output-escaping="yes">
    &lt;%@ taglib uri="/WEB-INF/taglib.tld" prefix="db" %&gt;

    &lt;html&gt;
      &lt;head&gt;
	&lt;db:base/&gt;
	&lt;title&gt;&lt;/title&gt;
	&lt;link rel="stylesheet" href="dbforms.css"&gt;
      &lt;/head&gt;

      &lt;body bgcolor="#99CCFF" &gt;
</xsl:text>
   
	<table bgcolor="#999900" cellpadding="1" cellspacing="0" width="100%" border="0" align="center">
	  <tr><td>
	  <table bgcolor="#999900" cellpadding="3" cellspacing="0" width="100%" border="0">
	    <tr bgcolor="#CCCC00">
	        <td><h1><xsl:value-of select="@name"/></h1></td>	    
		<td align="right">
		<a href="{@name}_list.jsp">[List]</a>
		<a href="menu.jsp">[Menu]</a>
		<a href="logout.jsp">[Log out]</a>		
		</td>
	      </tr>
	   </table>
	   </td></tr>	 		   
	</table>

<xsl:text disable-output-escaping="yes">
      &lt;% int i=0; %&gt;			

      &lt;db:dbform tableName="</xsl:text><xsl:value-of select="@name"/><xsl:text disable-output-escaping="yes">" maxRows="*" followUp="/</xsl:text><xsl:value-of select="@name"/><xsl:text disable-output-escaping="yes">_list.jsp" autoUpdate="false"&gt;

        &lt;db:header&gt;
          &lt;db:errors/&gt;			

          &lt;table align="center" cellspacing=0 cellpadding=2 bgcolor="#ffffff" width="100%"&gt;
          &lt;tr bgcolor="#CCCC00"&gt;



   </xsl:text>

   <xsl:for-each select="field">
     <xsl:text disable-output-escaping="yes">        &lt;td&gt;</xsl:text>
     <xsl:value-of select="@name"/>
     <xsl:text disable-output-escaping="yes">&lt;/td&gt;
     </xsl:text>
   </xsl:for-each>

   <xsl:text disable-output-escaping="yes">	
          &lt;/tr&gt;
        &lt;/db:header&gt;		

        &lt;db:body allowNew="false"&gt;
          &lt;% String bgcolor = (i % 2 == 0) ? "#FFFFCC" : "#FFFF99"; i++; %&gt;		
          &lt;tr bgcolor="&lt;%= bgcolor %&gt;"&gt;
    </xsl:text>

    <xsl:for-each select="field">
        <xsl:text disable-output-escaping="yes">&lt;td&gt;</xsl:text>
       <xsl:choose>
         <xsl:when test="position()=1">
            <xsl:text disable-output-escaping="yes">&lt;a href="&lt;db:linkURL href="/</xsl:text>
            <xsl:value-of select="../@name"/>
            <xsl:text disable-output-escaping="yes">_single.jsp" tableName="</xsl:text>
            <xsl:value-of select="../@name"/>
            <xsl:text disable-output-escaping="yes">" position="&lt;%= position_</xsl:text>
            <xsl:value-of select="../@name"/>
            <xsl:text disable-output-escaping="yes"> %&gt;"/&gt;" &gt;&lt;%= currentRow_</xsl:text><xsl:value-of select="../@name"/><xsl:text disable-output-escaping="yes">.get("</xsl:text><xsl:value-of select="@name"/><xsl:text disable-output-escaping="yes">") %&gt;</xsl:text>
            <xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>
         </xsl:when>
         <xsl:otherwise>
            <xsl:text disable-output-escaping="yes">&lt;%= currentRow_</xsl:text><xsl:value-of select="../@name"/><xsl:text disable-output-escaping="yes">.get("</xsl:text><xsl:value-of select="@name"/><xsl:text disable-output-escaping="yes">") %&gt;</xsl:text>
         </xsl:otherwise>
       </xsl:choose>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;&lt;/td&gt;
        </xsl:text>

     </xsl:for-each>

    <xsl:text disable-output-escaping="yes">
          &lt;/tr&gt;			              		 				
        &lt;/db:body&gt;
        &lt;db:footer&gt;
          &lt;/table&gt;
          &lt;center&gt;&lt;db:navNewButton caption="new </xsl:text><xsl:value-of select="@name"/><xsl:text disable-output-escaping="yes">..." followUp="/</xsl:text><xsl:value-of select="@name"/><xsl:text disable-output-escaping="yes">_single.jsp" /&gt;&lt;/center&gt;			
        &lt;/db:footer&gt;
       &lt;/db:dbform&gt;
      &lt;/body&gt;
     &lt;/html&gt;
   </xsl:text>
  </xsl:template>

</xsl:stylesheet>
