<?xml version='1.0'?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:db="http://www.wap-force.net/dbforms">

<!--
**
**   STYLESHEET FOR GENERATION OF JSP VIEWS FOR DBFORMS
**
**   This stylesheet will make JSP views that list table content. The
**   data in the list can be updated.
**
**   (note: you can switch "autoupdate" to "true" in this stylesheet
**   and the users of the resulting JSPs will be able to update multiple
**   rows at once !)
-->

<xsl:output indent="yes"/>

<!--
definition of variables
choose appropriate values that fit your needs
-->

<xsl:variable name="maxRows">15</xsl:variable>
<!-- Set this variable to true if you want that buttons for deleting and updating table appears  -->
<xsl:variable name="updateDeleteButtons">true</xsl:variable>

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
<xsl:variable name="fileName"><xsl:value-of select="$tableName"/><xsl:text disable-output-escaping="yes">_viewTable.jsp</xsl:text></xsl:variable>
<xsl:variable name="fileName_List"><xsl:value-of select="$tableName"/><xsl:text disable-output-escaping="yes">_list.jsp</xsl:text></xsl:variable>
<xsl:variable name="fileName_New"><xsl:value-of select="$tableName"/><xsl:text disable-output-escaping="yes">_single.jsp</xsl:text></xsl:variable>
<xsl:variable name="fileName_CrtRec"><xsl:value-of select="$tableName"/><xsl:text disable-output-escaping="yes">_single_ro.jsp</xsl:text></xsl:variable>
//--file "<xsl:value-of select="$fileName"/>" ------------------------------------------------
<xsl:text disable-output-escaping="yes">&lt;%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %&gt;
&lt;%int i=0;
   boolean showSearchRow;
   String sr = request.getParameter("showSearchRow");
   if (sr == null)
      showSearchRow = false;
   else
      showSearchRow = (new Boolean(sr)).booleanValue();
%&gt;
</xsl:text>
<html>
	<head>
		<db:base/>
		<title>View table --- file: <xsl:value-of select="$fileName"/></title>
		<link rel="stylesheet" href="dbforms.css"/>
		<xsl:text disable-output-escaping="yes">&lt;script language="javascript1.1"&gt;
		&lt;!--
		function switchSearch(newValue) {
			document.dbform.showSearchRow.value = newValue;
			document.dbform.submit();
		}
		--&gt;
		&lt;/script&gt;
      	</xsl:text>
	</head>
<body class="clsPageBody">
<table class="clsMainMenuTable" cellpadding="1" cellspacing="0" width="100%" border="0" align="center">
	<tr>
		<td>
	  		<table class="clsMainMenuTable" cellpadding="3" cellspacing="0" width="100%" border="0">
				<tr class="clsMainMenuTableRow">
					<td><span class="clsMainMenu"><xsl:value-of select="$origTableName"/></span></td>
					<td align="right">
						<xsl:text disable-output-escaping="yes">&lt;a class="clsMainMenu" href="javascript:switchSearch(&lt;%= !showSearchRow %&gt;)"&gt;[Filter &lt;%= (showSearchRow) ? "off" : "on" %&gt;]&lt;/a&gt;
						</xsl:text>
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
&lt;INPUT type="hidden" name="showSearchRow" value="&lt;%= showSearchRow %&gt;"&gt;
	</xsl:text>
	<tr class="clsHeaderDataTableRow">
           <xsl:for-each select="field[not(@autoInc) or @autoInc='false']">
             <td align="left">
               <xsl:choose>
                  <xsl:when test="@isKey or @sortable='true'">
                     <db:sort fieldName="{@name}"/>
                  </xsl:when>
                  <xsl:otherwise>
                     <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                  </xsl:otherwise>
               </xsl:choose>
             </td>
           </xsl:for-each>
           <td colspan="2"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
          </tr>

          <xsl:text disable-output-escaping="yes">
             &lt;% if (showSearchRow) { %&gt;
           </xsl:text>
                       <tr class="clsSearchDataTableRow">
                       <xsl:for-each select="field[not(@autoInc) or @autoInc='false']">
                         <td>
                           <xsl:choose>
                              <xsl:when test="@isKey or @sortable='true'">
                                 <table>
                                 <tr class="clsSearchDataTableRow">
                                    <td>
                                       <xsl:text disable-output-escaping="yes">
                                          &lt;INPUT type="text" name="&lt;%= searchFieldNames_</xsl:text><xsl:value-of select="$tableName"/>
                                       <xsl:text disable-output-escaping="yes">.get("</xsl:text><xsl:value-of select="@name"/>
                                       <xsl:text disable-output-escaping="yes">") %&gt;" size="10" class="clsInputStyle" &gt;</xsl:text>
                                    </td>
                                    <td>
                                       <xsl:text disable-output-escaping="yes">
                                          &lt;INPUT type="radio" name="&lt;%= searchFieldModeNames_</xsl:text><xsl:value-of select="$tableName"/>
                                       <xsl:text disable-output-escaping="yes">.get("</xsl:text><xsl:value-of select="@name"/>
                                       <xsl:text disable-output-escaping="yes">") %&gt;" checked value="and" class="clsInputStyle" &gt;and</xsl:text>
                                    </td>
                                 </tr>
                                 <tr class="clsSearchDataTableRow">
                                    <td>
                                       <xsl:text disable-output-escaping="yes">
                                          &lt;INPUT type="checkbox" name="&lt;%= searchFieldAlgorithmNames_</xsl:text><xsl:value-of select="$tableName"/>
                                       <xsl:text disable-output-escaping="yes">.get("</xsl:text><xsl:value-of select="@name"/>
                                       <xsl:text disable-output-escaping="yes">") %&gt;" value="weak" size="10" class="clsInputStyle" &gt; weak</xsl:text>
                                    </td>
                                    <td>
                                       <xsl:text disable-output-escaping="yes">
                                          &lt;INPUT type="radio" name="&lt;%= searchFieldModeNames_</xsl:text><xsl:value-of select="$tableName"/>
                                       <xsl:text disable-output-escaping="yes">.get("</xsl:text><xsl:value-of select="@name"/>
                                       <xsl:text disable-output-escaping="yes">") %&gt;" value="or" class="clsInputStyle" &gt;or</xsl:text>
                                    </td>
                                 </tr>
                                 </table>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                              </xsl:otherwise>
                           </xsl:choose>
                         </td>
                       </xsl:for-each>
                         <td colspan="2"><input type="button" value="Apply" onClick="javascript:document.dbform.submit()" class="clsButtonStyle"/></td>
                      </tr>
          <xsl:text disable-output-escaping="yes">
                  &lt;% } %&gt;
          </xsl:text>

          <tr class="clsHeaderDataTableRow">
              <xsl:for-each select="field[not(@autoInc) or @autoInc='false']">
                <td class="clsHeaderDataTableCell">
                  <xsl:value-of select="@name"/>
                </td>
              </xsl:for-each>
                <td colspan="2"></td> 
          </tr>
       </db:header>

       <db:errors/>

       <db:body>

          <xsl:text disable-output-escaping="yes">
	&lt;tr class="&lt;%= (i++%2==0) ? "clsOddDataTableRow" : "clsEvenDataTableRow" %&gt;"&gt;
          </xsl:text>
          <xsl:for-each select="field">
			<!-- render fields, but not identity fields. They are handled in database -->
             <xsl:if test="not(@autoInc) or @autoInc = 'false' ">
             <xsl:text disable-output-escaping="yes">		&lt;td align="left"&gt;
             </xsl:text>
             <xsl:choose>
               <xsl:when test="position()=1">
                   <xsl:text disable-output-escaping="yes">			&lt;a href="&lt;db:linkURL href="/</xsl:text><xsl:value-of select="$fileName_CrtRec"/>
                   <xsl:text disable-output-escaping="yes">" tableName="</xsl:text><xsl:value-of select="$origTableName"/>
                   <xsl:text disable-output-escaping="yes">" position="&lt;%= position_</xsl:text><xsl:value-of select="$tableName"/>
                   <xsl:text disable-output-escaping="yes"> %&gt;"/&gt;" &gt;&lt;%= currentRow_</xsl:text><xsl:value-of select="$tableName"/>
                   <xsl:text disable-output-escaping="yes">.get("</xsl:text><xsl:value-of select="@name"/>
                   <xsl:text disable-output-escaping="yes">") %&gt;&lt;/a&gt;</xsl:text>
                </xsl:when>
                <xsl:otherwise>
	        <xsl:choose>
	             <xsl:when test="@fieldType='date'">
                          <db:dateLabel fieldName="{@name}" />
                     </xsl:when>
	            <xsl:otherwise>		          
                        <xsl:text disable-output-escaping="yes"> &lt;%= currentRow_</xsl:text><xsl:value-of select="$tableName"/>
                        <xsl:text disable-output-escaping="yes">.get("</xsl:text><xsl:value-of select="@name"/>
                        <xsl:text disable-output-escaping="yes">") %&gt;</xsl:text>
	            </xsl:otherwise>
		   </xsl:choose>
                </xsl:otherwise>
             </xsl:choose>
             <xsl:text disable-output-escaping="yes">&amp;nbsp;&lt;/td&gt;
             </xsl:text>
             </xsl:if>
          </xsl:for-each>
          <xsl:choose>
          <xsl:when test="$updateDeleteButtons = 'true'">
	   	<td>
                   <xsl:text disable-output-escaping="yes">&lt;a href="&lt;db:linkURL href="/</xsl:text><xsl:value-of select="$fileName_New"/>
                   <xsl:text disable-output-escaping="yes">" tableName="</xsl:text><xsl:value-of select="$origTableName"/>
                   <xsl:text disable-output-escaping="yes">" position="&lt;%= position_</xsl:text><xsl:value-of select="$tableName"/>
                   <xsl:text disable-output-escaping="yes"> %&gt;"/&gt;" &gt;&lt;IMG src="dbformslib/icons/edit.gif" border="0" &gt; &lt;/a&gt;</xsl:text>
           </td>
	   	 <td><db:deleteButton caption="delete" flavor="image" alt="cancella" src="dbformslib/icons/delete.gif" style="border:0"/></td>
           </xsl:when>
           <xsl:otherwise>
               <td colspan="2"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
           </xsl:otherwise>
           </xsl:choose>

		<xsl:text disable-output-escaping="yes">
              &lt;/tr&gt;
		</xsl:text>
	</db:body>
	<db:footer>
	<xsl:text disable-output-escaping="yes">
          &lt;/table&gt;	  	
       
	</xsl:text>

	<table align="center" border="0">
	<tr><td>
<xsl:if test="not($maxRows='*')">
	 	<table align="center" border="0">
			<tr valign="middle"><td colspan="4" valign="bottom"><hr width="400"/></td></tr>
			<tr align="center">
				<td align="right"><db:navFirstButton caption="&lt;&lt; First" style="width:90" styleClass="clsButtonStyle"/></td>
				<td align="center"><db:navPrevButton caption="&lt; Previous" style="width:90" styleClass="clsButtonStyle"/></td>
				<td align="center"><db:navNextButton caption="Next &gt;" style="width:90" styleClass="clsButtonStyle"/></td>
				<td align="left"><db:navLastButton caption="Last &gt;&gt;" style="width:90" styleClass="clsButtonStyle"/></td>
	 		</tr>
	     </table>
</xsl:if>
		<table align="center" border="0">
			<tr valign="middle"><hr width="400"/></tr>
			<tr align="center">
				<td><db:navNewButton caption="Insert new ..." followUp="/{$fileName_New}" styleClass="clsButtonStyle"/>	</td>
			</tr>
		</table>
     </td></tr>
	</table>
        
        
        
        
        
        
	</db:footer>
</db:dbform>
</body>
</html>
</xsl:template>

</xsl:stylesheet>
