<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:db="http://www.wap-force.com/dbforms">
<xsl:output indent="yes"/>
<!--
definition of variables
choose appropriate values that fit your needs
-->
<xsl:template match="/">
//--file "menu.jsp" -------------------------------------------
<xsl:text disable-output-escaping="yes">&lt;%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %&gt;</xsl:text>
		<html>
			<head>
				<db:base/>
				<title>Main Menu Form</title>
				<link rel="stylesheet" href="dbforms.css"/>
			</head>
			<body class="clsPageBody">
				<db:errors/>
				<table width="100%" height="100%" cellpadding="0" cellspacing="5">
					<tr>
						<td/>
					</tr>
					<tr>
						<td>
							<center>
								<db:dbform followUp="/menu.jsp">
									<center>
										<h1>-= Main  Menu =-</h1>
										<hr/>
										<table cellspacing="0" cellpadding="0" >
											<xsl:apply-templates/>
										</table>
										<hr/>
										<p>
											<db:gotoButton caption="Log out" destination="/logout.jsp" style="width:100" styleClass="clsButtonStyle"/>
										</p>
									</center>
								</db:dbform>
							</center>
						</td>
					</tr>
					<tr>
						<td/>
					</tr>
				</table>
			</body>
		</html>
</xsl:template>

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
		<tr valign="middle">
			<td valign="middle">
				<b>
					<xsl:value-of select="$origTableName"/>
					<xsl:text disable-output-escaping="yes"> : &#x0D;&#x0A;</xsl:text>
				</b>
			</td>
			<td>
				<xsl:text disable-output-escaping="yes">&amp;nbsp;&amp;nbsp;&amp;nbsp;</xsl:text>
			</td>
			<td align="center">
				<db:gotoButton caption=" View advanced " destination="/{$tableName}_viewTable.jsp"  styleClass="clsButtonStyle"/>
			</td>
			<td align="center">
				<db:gotoButton caption=" Edit all " destination="/{$tableName}_list_editable.jsp" styleClass="clsButtonStyle"/>
			</td>
  		      <td align="center">
				<db:gotoButton caption=" Single " destination="/{$tableName}_single.jsp"  styleClass="clsButtonStyle"/>
			</td>
			<td align="center">
				<db:gotoButton caption=" View record " destination="/{$tableName}_single_ro.jsp"  styleClass="clsButtonStyle"/>
			</td>
	        </tr>
		<tr valign="middle">
		       <td> <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td> 
		       <td> <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
			<td align="center">
				<db:gotoButton caption=" View normal " destination="/{$tableName}_list.jsp"  styleClass="clsButtonStyle"/>
			</td>
			<td align="center">
				<db:gotoButton caption=" Edit range " destination="/{$tableName}_partial_list_editable.jsp"  styleClass="clsButtonStyle"/>
			</td>
			<td align="center">
				<db:gotoButton caption=" Edit all with selection " destination="/{$tableName}_list_editable_rb.jsp"  styleClass="clsButtonStyle"/>
			</td>
			<td align="center">
				<db:gotoButton caption=" View and Insert " destination="/{$tableName}_list_and_single.jsp"  styleClass="clsButtonStyle"/>
			</td>
		</tr>
</xsl:template>

<xsl:template match="date-format"/>

</xsl:stylesheet>
