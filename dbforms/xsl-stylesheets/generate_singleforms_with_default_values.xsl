<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:db="http://www.wap-force.com/dbforms">
<!--
**
**   STYLESHEET FOR GENERATION OF JSP VIEWS FOR DBFORMS
** 
**   This stylesheet will make JSP views that list content of 1 table row.
**   (== a dataset). Data can be updated. 
**
**  
-->
<xsl:output indent="yes"/>
<xsl:param name="useCalendar"/> 
<!--
definition of variables
choose appropriate values that fit your needs
-->

<!-- in single mode  we can only see 1 row -->
<xsl:variable name="maxRows">1</xsl:variable>


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
<xsl:variable name="fileName"><xsl:value-of select="$tableName"/><xsl:text disable-output-escaping="yes">_single.jsp</xsl:text></xsl:variable>
<xsl:variable name="fileName_List"><xsl:value-of select="$tableName"/><xsl:text disable-output-escaping="yes">_list.jsp</xsl:text></xsl:variable>
//--file "<xsl:value-of select="$fileName"/>" ------------------------------------------------
<xsl:text disable-output-escaping="yes">&lt;%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %&gt;
</xsl:text>
<html>
	<head>
		<db:base/>
		<title>Single --- file: <xsl:value-of select="$fileName"/></title>
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

<db:header/>
	<db:errors/>
<db:body>
	<table border="0" align="center" width="400">
		<xsl:for-each select="field">
		<!-- we create input fields for NON-AUTOMATIC values only -->
			<xsl:if test="string-length(@autoInc) = 0 or @autoInc = 'false' ">
				<xsl:variable name="customClass">
					<xsl:choose>
						<xsl:when test="position() mod 2 = 0">clsOddDataTableRow</xsl:when>
						<xsl:otherwise>clsEvenDataTableRow</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
		<tr class="{$customClass}">
			<td align="left" style="font-weight: bold"><db:message><xsl:attribute name="key"><xsl:value-of select='@name'/></xsl:attribute></db:message></td>
			<td align="left">
			<xsl:choose>
				<xsl:when test="@fieldType='int' or @fieldType='smallint'  or @fieldType='tinyint' or @fieldType='integer'">
					<xsl:choose>
						<xsl:when test="@defaultValue">
							<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle" overrideValue="{@defaultValue}"/>
						</xsl:when>
						<xsl:otherwise>
							<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="@fieldType='char' or @fieldType='varchar' or @fieldType='varchar2'  or @fieldType='nvarchar'  or @fieldType='longvarchar' or @fieldType='character'">
					<xsl:choose>
						<xsl:when test="@size>80">
					<db:textArea fieldName="{@name}" cols="40" rows="3" wrap="virtual" styleClass="clsInputStyle"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="@defaultValue">
									<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle" overrideValue="{@defaultValue}"/>
								</xsl:when>
								<xsl:otherwise>
									<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle"/>
								</xsl:otherwise>
							</xsl:choose>
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
				<xsl:when test="@fieldType='time'">
					<xsl:choose>
						<xsl:when test="@defaultValue">
							<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle" overrideValue="{@defaultValue}"/>
						</xsl:when>
						<xsl:otherwise>
							<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="@fieldType='timestamp'">
					<xsl:choose>
						<xsl:when test="@defaultValue">
							<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle" overrideValue="{@defaultValue}"/>
						</xsl:when>
						<xsl:otherwise>
							<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="@fieldType='double'">
					<xsl:choose>
						<xsl:when test="@defaultValue">
							<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle" overrideValue="{@defaultValue}"/>
						</xsl:when>
						<xsl:otherwise>
							<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="@fieldType='float' or @fieldType='real' or @fieldType='decimal'">
					<xsl:choose>
						<xsl:when test="@defaultValue">
							<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle" overrideValue="{@defaultValue}"/>
						</xsl:when>
						<xsl:otherwise>
							<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="@fieldType='numeric'  or @fieldType='number'">
					<xsl:choose>
						<xsl:when test="@defaultValue">
							<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle" overrideValue="{@defaultValue}"/>
						</xsl:when>
						<xsl:otherwise>
							<db:textField fieldName="{@name}" size="{@size}" styleClass="clsInputStyle"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="@fieldType='blob' or @fieldType='image'">
					<db:file fieldName="{@name}" styleClass="clsInputStyle"/>
				</xsl:when>
				<xsl:when test="@fieldType='diskblob'">
					<db:file fieldName="{@name}" styleClass="clsInputStyle"/>
				</xsl:when>
				<xsl:otherwise>
				*** error - fieldtype <xsl:value-of select="@fieldType"/> is currently not supported. please send an e-mail to jdbforms-interest@lists.sourceforge.net! ***
                 		</xsl:otherwise>
			</xsl:choose>
			</td>
		</tr>
			</xsl:if>
		</xsl:for-each>
	</table>
	<br/>
		<center><db:insertButton caption="Commit data into {@name}" styleClass="clsButtonStyle" showAlways="false" /></center>
	</db:body>
	<db:footer>
		<table align="center" border="0">
			<tr>
				<td align="right"><db:navFirstButton caption="&lt;&lt; First" style="width:90" styleClass="clsButtonStyle"/></td>
				<td align="center"><db:navPrevButton caption="&lt; Previous" style="width:90" styleClass="clsButtonStyle"/></td>
				<td align="center"><db:navNextButton caption="Next &gt;" style="width:90" styleClass="clsButtonStyle"/></td>
				<td align="left"><db:navLastButton caption="Last &gt;&gt;" style="width:90" styleClass="clsButtonStyle"/></td>
			</tr>
		</table>
		<table align="center" border="0">
			<tr valign="middle"><td colspan="3"><hr/></td></tr>
			<tr align="center">
				<td align="center"><db:updateButton caption="Update" style="width:90" styleClass="clsButtonStyle"/></td>
				<td align="center"><db:deleteButton caption="Delete" style="width:90" styleClass="clsButtonStyle"/></td>
				<td align="center"><db:navNewButton caption="Insert new" style="width:90" styleClass="clsButtonStyle"/></td>
			</tr>
		</table>
	</db:footer>
</db:dbform>
</body>
</html>
</xsl:template>
</xsl:stylesheet>
