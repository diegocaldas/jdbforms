<?xml version="1.0" encoding="UTF-8"?>
<!--

generate_validation.xsl

Author: Matthew Stein (mstein@ergito.com)
Date: 2/4/03

Used by DevGui to generate the validation.xml file that dbforms uses for validation. (The source XML file is dbforms-config.xml.)

Note: we added the attribute generate-jsp (true/false) to table tags because there were a few tables we wanted in the dbforms-config.xml file for foreign-key reasons
but we didn't want tables to be generated. The line test="not(@generate-jsp='false')" returns true when (a) generate-jsp is set to some value other than false or
(b) when it doesn't exist. (generate-jsp!='false' by comparison returns true only when generate-jsp is set to something other than false.) This way, its functionality is
retained while making it an optional attribute as far as DevGui is concerned.

Limitations: (a) Assumes that all foreign-keys are required and that the server will do its own testing for valid entry. (b) Asserts that isKey'd columns that aren't also
autoInc'd are (i) required and (ii) in need of maxlength validating; the resulting validation.xml asserts that (iii) the database server will insure that there are no key column
conflicts.

-->
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xalan="http://xml.apache.org/xslt"
	>
	<!-- foreignkeys is a hash for use later to determine requirements (all foreign keys ought to be required) -->
	<xsl:key name="foreignkeys" match="/dbforms-config/table//foreign-key" use="reference/@local"/>
	<!-- stuffing date-format into a global variable is for use in the table subroutines -->
	<xsl:variable name="dateFmt" select="/dbforms-config/date-format"/>
 	<xsl:strip-space elements="*"/>
 	<xsl:output method="xml" 
              encoding="UTF-8"
              indent="yes" 
              xalan:indent-amount="3"/>
	<xsl:template match="dbforms-config">
//--file "WEB-INF/validation.xml" -------------------------------------------
		<form-validation>
   			<formset>
      				<xsl:apply-templates select="table"/>
   			</formset>
		</form-validation>
	</xsl:template>
	<!-- table -->
	<xsl:template match="table">
		<xsl:if test="not(@generate-jsp='false')">
			<form name="{@name}">
				<!-- This only filters out fields with the autoInc attribute set to true and ignores the isKey attribute, because even if a field's a key, there's still necessary validation -->
				<xsl:for-each select="field[not(@autoInc='true')]">
					<xsl:variable name="foreignKey" select="key('foreignkeys',@name)"/>
					<xsl:choose>
						<xsl:when test="$foreignKey">
						<!-- Foreign keys are generated with drop-down boxes in the other XSLs, so it's assumed that they're filled in - and besides, 
							the required values would have to be checked against a foreign table. So it's easier just to require them and let dbforms
							return a "normal" error for misassignment of a foreign-key. -->
							<xsl:call-template name="foreignKeyField">
								<xsl:with-param name="thisField" select="."/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="notForeignKeyField">
								<xsl:with-param name="thisField" select="."/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</form>
		</xsl:if>
	</xsl:template>
	<!-- "Subroutine" for foreignKeys -->
	<xsl:template name="foreignKeyField">
		<xsl:param name="thisField"/>
		<field property="{$thisField/@name}" depends="required">
			<msg name="required" resource="false" key="{$thisField/@name} is a required field"/>
		</field>
	</xsl:template>
	<!-- The other "Subroutine" -->
	<xsl:template name="notForeignKeyField">
		<xsl:param name="thisField"/>
		<xsl:choose>
			<xsl:when test="$thisField/@fieldType='int' or $thisField/@fieldType='integer'">
				<field property="{@name}" depends="integer">
					<msg name="integer" resource="false" key="{$thisField/@name} should be set to an integer value"/>
				</field>
			</xsl:when>
			<xsl:when test="$thisField/@fieldType='smallint'">
				<field property="{@name}" depends="short">
					<msg name="short" resource="false" key="{$thisField/@name} should be set to an short value (e.g. 0 to 65535 unsigned)"/>
				</field>
			</xsl:when>
			<xsl:when test="$thisField/@fieldType='tinyint'">
				<field property="{@name}" depends="byte">
					<msg name="byte" resource="false" key="{$thisField/@name} should be set to a byte value (e.g. 0 or 1)"/>
				</field>
			</xsl:when>
			<xsl:when test="$thisField/@fieldType='char' or $thisField/@fieldType='varchar' or $thisField/@fieldType='varchar2'  or $thisField/@fieldType='nvarchar'  or $thisField/@fieldType='longvarchar' or $thisField/@fieldType='character'">
				<field property="{@name}" depends="maxlength">
					<msg name="maxlength" resource="false" key="{@name} has a maximum length of ${{var:maxchars}}"/>
					<arg0 name="maxlength" resource="false" key="${{var:maxchars}}"/>
					<var>
						<var-name>maxchars</var-name>
						<var-value>
							<xsl:value-of select="@size"/>
						</var-value>
					</var>
				</field>
			</xsl:when>
			<xsl:when test="$thisField/@fieldType='double'">
				<field property="{@name}" depends="double">
					<msg name="double" resource="false" key="{@name} requires a double value (approximately 1.7E–308 to 1.7E+308)"/>
				</field>
			</xsl:when>
			<xsl:when test="$thisField/@fieldType='float' or $thisField/@fieldType='decimal'">
				<field property="{@name}" depends="float">
					<msg name="float" resource="false" key="{@name} expects a float value (approximately 3.4E–38 to 3.4E+38)"/>
				</field>
			</xsl:when>
			<xsl:when test="$thisField/@fieldType='date' or $thisField/@fieldType='datetime' or $thisField/@fieldType='timestamp'">
				<field property="{@name}" depends="date">
					<msg name="date" resource="false" key="{@name} should be formatted as ${{var:datePattern}}"/>
					<arg0 name="date" resource="false" key="${{var:datePattern}}"/>
					<var>
						<var-name>datePattern</var-name>
						<var-value>
							<xsl:value-of select="$dateFmt"/>
						</var-value>
					</var>
				</field>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
