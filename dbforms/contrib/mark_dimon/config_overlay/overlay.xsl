<?xml version='1.0'?>

<!--

 * $Header: 
 * $Revision: 
 * $Date$
 *
 * overlay.xml stylesheet for dbforms 
 * Copyright (C) 2002 Mark Dimon 
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" >

<xsl:output indent="yes"/>

<xsl:param name="overlay-path" select="''" />

<xsl:variable name="overlay" select="document($overlay-path)/dbforms-config"/>

<xsl:template match="/">
    <xsl:apply-templates />
</xsl:template>

<xsl:template match="*">
    <xsl:copy>
        <xsl:call-template name="copy-attributes"/>
        <xsl:apply-templates />
    </xsl:copy>
</xsl:template>

<xsl:template match="xml">
        <xsl:apply-templates />
</xsl:template>

<xsl:template match="dbforms-config">
    <dbforms-config>
      	<xsl:apply-templates />
		<xsl:call-template name="merge-other-dbconnections"/>
    </dbforms-config>
</xsl:template>

<xsl:template match="table">
    <xsl:copy>
        <xsl:call-template name="copy-attributes"/>
        <xsl:apply-templates mode="copy"/>
        <xsl:call-template name="merge-nodes"/>
    </xsl:copy>
</xsl:template>

<xsl:template match="dbconnection[1]">
    <xsl:copy>
        <xsl:call-template name="copy-attributes"/>
        <xsl:call-template name="merge-dbconnection-attributes"/>
    </xsl:copy>
</xsl:template>



<xsl:template match="field" mode="copy">
    <xsl:copy>
        <xsl:call-template name="copy-attributes"/>
        <xsl:call-template name="merge-attributes"/>
        <xsl:call-template name="merge-child-nodes"/>
    </xsl:copy>
</xsl:template>


<xsl:template match="*" mode="copy">
    <xsl:copy>
        <xsl:call-template name="copy-attributes"/>
        <xsl:apply-templates />
    </xsl:copy>
</xsl:template>

<xsl:template name="copy-attributes">
    <xsl:for-each select="@*">
        <xsl:copy>
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:for-each>
</xsl:template>

<!--
merge the attributes of any field nodes with the same name as the context node
-->

<xsl:template name="merge-attributes">
    <xsl:variable name="name" select="../@name"/>
    <xsl:variable name="fname" select="@name"/>
    <xsl:variable name="thisNode" select="."/>
    <xsl:variable name="matchField" select="$overlay/table[@name=$name]/field[@name=$fname]"/>
    <xsl:for-each select="$matchField/@*">
                <xsl:copy />
    </xsl:for-each>
</xsl:template>

<xsl:template name="merge-other-dbconnections">
    <xsl:for-each select="$overlay/dbconnection">
		<xsl:if test="position()!=1">
			<xsl:message>found dbconnection <xsl:value-of select="@name"/><xsl:value-of select="position()"/></xsl:message>    
			<xsl:copy-of select="." />
		</xsl:if>
    </xsl:for-each>
</xsl:template>

<xsl:template name="merge-dbconnection-attributes">
    <xsl:variable name="matchField" select="$overlay/dbconnection[1]"/>
	<xsl:message>merge-dbconnection</xsl:message>
    <xsl:for-each select="$matchField/@*">
            <xsl:copy />
    </xsl:for-each>
</xsl:template>


<!--
merge any nodes that are *not* field nodes into the table
-->

<xsl:template name="merge-nodes">
    <xsl:variable name="name" select="@name"/>
    <xsl:for-each select="$overlay/table[@name=$name]">
        <xsl:for-each select="*">
        <xsl:choose>
            <xsl:when test="local-name()='field'">
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="."/>
            </xsl:otherwise>
        </xsl:choose>
        </xsl:for-each>
    </xsl:for-each>
</xsl:template>


<xsl:template name="merge-child-nodes">
    <xsl:variable name="name" select="../@name"/>
    <xsl:variable name="fname" select="@name"/>
    <xsl:variable name="thisNode" select="."/>
    <xsl:variable name="matchNode" select="$overlay/table[@name=$name]/field[@name=$fname]/*"/>
    <xsl:for-each select="$matchNode">
                <xsl:copy-of select="." />
    </xsl:for-each>
</xsl:template>


</xsl:stylesheet>
