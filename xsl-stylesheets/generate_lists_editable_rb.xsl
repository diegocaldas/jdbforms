<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:db="http://www.wap-force.net/dbforms">
<!--
**
**   STYLESHEET FOR GENERATION OF JSP VIEWS FOR DBFORMS
** 
**   This stylesheet will make JSP views that list table content. The
**   data in the list can be updated. The list will show a radio button
**   for each row. The user needs to select the radio button before 
**   he/she can update or delete a row. This way the amount of buttons
**   to be rendered by the browser is minimized.
**  
**   (note: you can switch "autoupdate" to "true" in this stylesheet 
**   and the users of the resulting JSPs will be able to update multiple
**   rows at once !)
**  
-->
<xsl:output method="html" indent="yes"/>
<xsl:param name="useCalendar"/> 
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
<xsl:variable name="fileName"><xsl:value-of select="$tableName"/><xsl:text disable-output-escaping="yes">_list_editable_rb.jsp</xsl:text></xsl:variable>
<xsl:variable name="fileName_List"><xsl:value-of select="$tableName"/><xsl:text disable-output-escaping="yes">_list.jsp</xsl:text></xsl:variable>
<xsl:variable name="fileName_New"><xsl:value-of select="$tableName"/><xsl:text disable-output-escaping="yes">_single.jsp</xsl:text></xsl:variable>
//--file "<xsl:value-of select="$fileName"/>" ------------------------------------------------
<xsl:text disable-output-escaping="yes">&lt;%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %&gt;
&lt;%int i=0; %&gt;
</xsl:text>
<html>
	<head>
		<db:base/>
		<title>List editable and Radio --- file: <xsl:value-of select="$fileName"/></title>
		<link rel="stylesheet" href="dbforms.css"/>
	</head>
	<xsl:if test="$useCalendar = 'true'">
		<xsl:text disable-output-escaping="yes">
		&lt;script language="javascript"   
                           src=&lt;%= "\"" +request.getContextPath() + "/dbformslib/jscal/calendar.js\""%&gt;&gt;
                &lt;/script&gt;
	       </xsl:text>
	</xsl:if>
<body class="clsPageBody">
<table class="clsMainMenuTable" cellpadding="1" cellspacing="0" width="100%" border="0" align="center">
	<tr>
		<td>
			<table class="clsMainMenuTable" cellpadding="3" cellspacing="0" width="100%" border="0">
				<tr class="clsMainMenuTableRow">
					<td><span class="clsMainMenu"><xsl:value-of select="$origTableName"/></span></td>
					<td align="right" class="clsMainMenu">
						<a href="{$fileName_List}" class="clsMainMenu">[List]</a>
						<a href="menu.jsp" class="clsMainMenu">[Menu]</a>
						<a href="logout.jsp" class="clsMainMenu">[Log out]</a>
					</td>
				</tr>
			</table>
		</td>
	</tr>
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
	<xsl:text disable-output-escaping="yes">
	&lt;table align="center" &gt;
       </xsl:text>
	<tr valign="middle" class="clsHeaderDataTableRow">
		<td><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
		<xsl:for-each select="field[not(@autoInc) or @autoInc='false']">
			<td class="clsHeaderDataTableCell">
			<db:message><xsl:attribute name="key"><xsl:value-of select='@name'/></xsl:attribute></db:message>
			</td>
		</xsl:for-each>
	</tr>
	<tr valign="middle" class="clsHeaderDataTableRow">
		<td><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
		<xsl:for-each select="field[not(@autoInc) or @autoInc='false']">
			<td>
			<xsl:if test="@isKey or @sortable='true'">
				<db:sort fieldName="{@name}"/>
			</xsl:if>
			</td>
		</xsl:for-each>
	</tr>
	<db:errors/>
</db:header>
<db:body allowNew="false">
	<xsl:text disable-output-escaping="yes">&lt;tr class="&lt;%= (i++%2==0) ? "clsOddDataTableRow" : "clsEvenDataTableRow" %&gt;"&gt;
	</xsl:text>
	<td><db:associatedRadio name="radio_{@name}"/></td>
	<xsl:for-each select="field">
	<!-- we create input fields for NON-AUTOMATIC values only -->
		<xsl:if test="not(@autoInc) or @autoInc = 'false' ">
			<td>
			<xsl:choose>
				<xsl:when test="@fieldType='int' or @fieldType='smallint' or @fieldType='tinyint' or @fieldType='integer'">
					<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle"/>
				</xsl:when>
				<xsl:when test="@fieldType='char' or @fieldType='varchar' or @fieldType='varchar2'  or @fieldType='nvarchar'  or @fieldType='longvarchar' or @fieldType='character'">
					<xsl:choose>
						<xsl:when test="@size>80">
							<db:textArea fieldName="{@name}" cols="40" rows="3" wrap="virtual" styleClass="clsInputStyle"/>
						</xsl:when>
						<xsl:otherwise>
							<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="@fieldType='date'">
					<xsl:text disable-output-escaping="yes">&#xD;&#xA;&lt;db:dateField</xsl:text>
					<xsl:text disable-output-escaping="yes"> styleClass="clsInputStyle"</xsl:text>
					<xsl:text disable-output-escaping="yes"> size="</xsl:text>	<xsl:value-of select="@size+3"/>
					<xsl:text disable-output-escaping="yes">" fieldName="</xsl:text><xsl:value-of select="@name"/> 
                                        <xsl:text disable-output-escaping="yes">"</xsl:text>                                       					
					<xsl:if test="$useCalendar = 'true'">
					       <xsl:text disable-output-escaping="yes">  useJsCalendar="true"</xsl:text>
					</xsl:if>
					<xsl:text disable-output-escaping="yes">/&gt;&#xD;&#xA;</xsl:text>
				</xsl:when>
				<xsl:when test="@fieldType='timestamp'">
					<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle"/>
				</xsl:when>
				<xsl:when test="@fieldType='double'">
					<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle"/>
				</xsl:when>
				<xsl:when test="@fieldType='float' or @fieldType='real' or @fieldType='decimal'">
					<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle"/>
				</xsl:when>
				<xsl:when test="@fieldType='numeric'  or @fieldType='number'">
					<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle"/>
				</xsl:when>
				<xsl:when test="@fieldType='blob' or @fieldType='image'">
					<xsl:text disable-output-escaping="yes">&lt;a href="&lt;db:blobURL fieldName=&quot;</xsl:text>
					<xsl:value-of select="@name"/>
					<xsl:text disable-output-escaping="yes">&quot; /&gt;" target="_blank" &gt;[view]&lt;/a&gt;</xsl:text><br/>
						<db:file fieldName="{@name}" styleClass="clsInputStyle"/>
				</xsl:when>
				<xsl:when test="@fieldType='diskblob'">
					<xsl:text disable-output-escaping="yes">&lt;a href="&lt;db:blobURL fieldName=&quot;</xsl:text>
					<xsl:value-of select="@name"/>
					<xsl:text disable-output-escaping="yes">&quot; /&gt;" target="_blank" &gt;[view]&lt;/a&gt;</xsl:text><br/>
						<db:file fieldName="{@name}" styleClass="clsInputStyle"/>
				</xsl:when>
				<xsl:otherwise>*** error - fieldtype <xsl:value-of select="@fieldType"/> is currently not supported. please send an e-mail to jdbforms-interest@lists.sourceforge.net! ***
                 		</xsl:otherwise>
			</xsl:choose>
			</td>
		</xsl:if>
	</xsl:for-each>

<xsl:text disable-output-escaping="yes">
           &lt;/tr&gt;
</xsl:text>
</db:body>
<db:footer>
<xsl:text disable-output-escaping="yes">
	&lt;/table&gt;
</xsl:text>
<xsl:if test="not($maxRows='*')">
	<table align="center" border="0">
		<tr valign="middle"><hr width="400"/></tr>
		<tr>
			<td align="right"><db:navFirstButton caption="&lt;&lt; First" style="width:90" styleClass="clsButtonStyle"/></td>
			<td align="center"><db:navPrevButton caption="&lt; Previous" style="width:90" styleClass="clsButtonStyle"/></td>
			<td align="center"><db:navNextButton caption="Next &gt;" style="width:90" styleClass="clsButtonStyle"/></td>
			<td align="left"><db:navLastButton caption="Last &gt;&gt;" style="width:90" styleClass="clsButtonStyle"/></td>
		</tr>
	</table>
</xsl:if>
	<table align="center" border="0">
		<tr valign="middle"><td colspan="3"><hr/></td></tr>
		<tr>
			<td align="center"><db:updateButton caption="Update" associatedRadio="radio_{@name}" style="width:90" styleClass="clsButtonStyle"/></td>
			<td align="center"><db:deleteButton caption="Delete" associatedRadio="radio_{@name}" style="width:90" styleClass="clsButtonStyle"/></td>
			<td align="center"><db:navNewButton caption="Insert new" followUp="/{$fileName_New}" style="width:90" styleClass="clsButtonStyle"/></td>
		</tr>
	</table>
</db:footer>
</db:dbform>
</body>
</html>
</xsl:template>
</xsl:stylesheet>
