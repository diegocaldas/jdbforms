<?xml version='1.0'?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:db="http://www.wap-force.com/dbforms">

<!--
**
**   STYLESHEET FOR GENERATION OF JSP VIEWS FOR DBFORMS
** 
**   This stylesheet will make JSP views that list table content. The
**   data in the list can not be updated ("read only")
**  
-->

<xsl:output indent="yes"/>

<!--
definition of variables
choose appropriate values that fit your needs
-->

<xsl:variable name="maxRows">*</xsl:variable>

<xsl:template match="table">
<!-- origTableName is the original table name -->
<xsl:variable name="origTableName"><xsl:value-of select="@name"/></xsl:variable>
<!-- tableName is the name with schema name corrected -->
<xsl:variable name="tableName">
	<xsl:choose>
		<!-- contains '.' means we have tables from other schema (owner) but only one '.' or the stylesheet will not work -->
		<xsl:when test="contains(@name, '.')">
			<xsl:value-of select="substring-before(@name, '.')"/>
			<xsl:value-of select="'_'"/>
			<xsl:value-of select="substring-after(@name, '.')"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="@name"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<xsl:variable name="fileName"><xsl:value-of select="$tableName"/><xsl:text disable-output-escaping="yes">_list.jsp</xsl:text></xsl:variable>
<xsl:variable name="fileName_New"><xsl:value-of select="$tableName"/><xsl:text disable-output-escaping="yes">_single.jsp</xsl:text></xsl:variable>
//--file "<xsl:value-of select="$fileName"/>" ------------------------------------------------
<xsl:text disable-output-escaping="yes">&lt;%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %&gt;
&lt;% int i=0; %&gt;
</xsl:text>
<html>
	<head>
		<db:base/>
		<title>List --- file: <xsl:value-of select="$fileName"/></title>
		<link rel="stylesheet" href="dbforms.css" />
    </head>
<body class="clsPageBody" >
<table class="clsMainMenuTable" cellpadding="1" cellspacing="0" width="100%" border="0" align="center">
	<tr>
		<td>
			<table class="clsMainMenuTable" cellpadding="3" cellspacing="0" width="100%" border="0">
				<tr class="clsMainMenuTableRow">
					<td><span class="clsMainMenu"><xsl:value-of select="$origTableName" /></span></td>
					<td align="right" class="clsMainMenu">
						<a href="{$fileName}" class="clsMainMenu">[List]</a>
						<a href="menu.jsp" class="clsMainMenu">[Menu]</a>
						<a href="logout.jsp" class="clsMainMenu">[Log out]</a>		
		</td>
	     </tr>
	   </table>
	   </td></tr>	 		   
	</table>

<!-- check for multipart fields like blob, image and diskblob -->
<xsl:variable name="allblobs" select="field[@fieldType='blob' or @fieldType='image' or @fieldType='diskblob']"></xsl:variable>
<xsl:variable name="countMultipart">
	<xsl:value-of select="count($allblobs)"></xsl:value-of>
</xsl:variable>
<xsl:variable name="MultiPart">
	<xsl:choose>
		<xsl:when test="$countMultipart > 0">
			<xsl:text disable-output-escaping="yes">true</xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:text disable-output-escaping="yes">false</xsl:text>
		</xsl:otherwise>
	</xsl:choose>
</xsl:variable>
<db:dbform tableName="{$origTableName}" maxRows="{$maxRows}" followUp="/{$fileName}" autoUpdate="false" multipart="{$MultiPart}">
<!-- so now multiparts are ok -->

<db:header>
	<db:errors />
	<xsl:text disable-output-escaping="yes">  &lt;table align="center" &gt;
		</xsl:text>	
		<tr class="clsHeaderDataTableRow">
			<xsl:for-each select="field">
				<xsl:text disable-output-escaping="yes">&lt;td class="clsHeaderDataTableCell"&gt;</xsl:text>
				<db:message><xsl:attribute name="key"><xsl:value-of select='@name'/></xsl:attribute></db:message>
				<xsl:text disable-output-escaping="yes">&lt;/td&gt;</xsl:text>
			</xsl:for-each>
		</tr>
</db:header>

<db:body allowNew="false">
	<xsl:text disable-output-escaping="yes">&lt;tr class="&lt;%= (i++%2==0) ? "clsOddDataTableRow" : "clsEvenDataTableRow" %&gt;"&gt;</xsl:text>
    <xsl:for-each select="field">
       <xsl:text disable-output-escaping="yes">&lt;td&gt;</xsl:text>
       <xsl:choose>
         <xsl:when test="position()=1">
		<xsl:text disable-output-escaping="yes">&lt;a href="&lt;db:linkURL href="/</xsl:text>
		<xsl:value-of select="$fileName_New"/>
		<xsl:text disable-output-escaping="yes">" tableName="</xsl:text>
		<xsl:value-of select="$origTableName"/>
		<xsl:text disable-output-escaping="yes">" position="&lt;%= position_</xsl:text>
		<xsl:value-of select="$tableName"/>
		<xsl:text disable-output-escaping="yes"> %&gt;"/&gt;" &gt;&lt;%= currentRow_</xsl:text>
		<xsl:value-of select="$tableName"/>
		<xsl:text disable-output-escaping="yes">.get("</xsl:text>
		<xsl:value-of select="@name"/>
		<xsl:text disable-output-escaping="yes">") %&gt;&lt;/a&gt;</xsl:text>
         </xsl:when>
         <xsl:otherwise>
	       <xsl:choose>
	             <xsl:when test="@fieldType='date'">
                          <db:dateLabel fieldName="{@name}" />
                     </xsl:when>
	            <xsl:otherwise>
		        <xsl:text disable-output-escaping="yes">&lt;%= currentRow_</xsl:text>
		        <xsl:value-of select="$tableName"/>
		       <xsl:text disable-output-escaping="yes">.get("</xsl:text>
		       <xsl:value-of select="@name"/>
		       <xsl:text disable-output-escaping="yes">") %&gt;</xsl:text>
	          </xsl:otherwise>
	      </xsl:choose>
         </xsl:otherwise>
       </xsl:choose>
        <xsl:text disable-output-escaping="yes">&amp;nbsp;&lt;/td&gt;&#xD;&#xA;</xsl:text>
     </xsl:for-each>

    <xsl:text disable-output-escaping="yes">&lt;/tr&gt;&#xD;&#xA;</xsl:text>
	</db:body>
        <db:footer>
             <xsl:text disable-output-escaping="yes">&lt;/table&gt;     </xsl:text>
          <center><hr width="400"/><db:navNewButton caption="Insert new ..." followUp="/{$fileName_New}" styleClass="clsButtonStyle"/></center>			
        </db:footer>
       </db:dbform>
      </body>
     </html>

</xsl:template>
</xsl:stylesheet>
