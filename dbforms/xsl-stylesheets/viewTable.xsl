<?xml version='1.0'?>

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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:db="http://www.wap-force.net/dbforms">

<xsl:output indent="yes"/>

<!--
definition of variables
choose appropriate values that fit your needs
-->

<xsl:variable name="maxRows">5</xsl:variable>
<xsl:variable name="pageBgColor">99CCFF</xsl:variable>
<!-- Set this variable to true if you want that buttons for deleting and updating table appears  -->
<xsl:variable name="updateDeleteButtons">true</xsl:variable>


<xsl:template match="table">

//--file "<xsl:value-of select="@name"/>_viewTable.jsp" ------------------------------------------------


<xsl:text disable-output-escaping="yes">
&lt;%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %&gt;
&lt;%int i=0; %&gt;
&lt;%
   boolean showSearchRow;
   String sr=request.getParameter("showSearchRow");
   if (sr==null)
      showSearchRow=false;
   else
      showSearchRow=(new Boolean(sr)).booleanValue();

%&gt;
</xsl:text>


  <html>
    <head>
      <db:base/>
      <link rel="stylesheet" href="dbforms.css"/>
      <xsl:text disable-output-escaping="yes">
         &lt;script language="javascript1.1"&gt;
         &lt;!--
            function switchSearch(newValue) {
               document.dbform.showSearchRow.value=newValue;
               document.dbform.submit();
            }
         --&gt;
         &lt;/script&gt;
      </xsl:text>
    </head>
    <body bgcolor="#{$pageBgColor}">

	<table bgcolor="#999900" cellpadding="1" cellspacing="0" width="100%" border="0" align="center">
	  <tr><td>
	  <table bgcolor="#999900" cellpadding="3" cellspacing="0" width="100%" border="0">
	    <tr bgcolor="#CCCC00">
	        <td><h1><xsl:value-of select="@name"/></h1></td>
		<td align="right">
		<!--<a href="{@name}_viewTable.jsp">[List]</a>-->
                  <xsl:text disable-output-escaping="yes">
                     &lt;a href="javascript:switchSearch('&lt;%=!showSearchRow%&gt;')"&gt; [Filtri &lt;%=(showSearchRow)?"OFF":"ON" %&gt;]&lt;/a&gt;
                  </xsl:text>
		<a href="menu.jsp">[Menu]</a>
		<!--<a href="logout.jsp">[Log out]</a> -->
		</td>
	      </tr>
	   </table>
	   </td></tr>
	</table>


     <db:dbform tableName="{@name}" maxRows="{$maxRows}" followUp="/{@name}_viewTable.jsp" autoUpdate="false">
       <db:header>
       <xsl:text disable-output-escaping="yes">
          &lt;table align="center" &gt;
          <!-- serve per far viaggiare avanti e indietro il valore di showSearchRow -->
          &lt;INPUT type="hidden" name="showSearchRow" value="&lt;%=showSearchRow%&gt;"&gt;
        </xsl:text>

           <tr bgcolor="#ACACAC">
           <xsl:for-each select="field[not(@autoInc) or @autoInc='false']">
             <td>
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
           <!-- header per tenere posto per i tastini di modifica/cancellazione della riga, nelle righe sottostanti -->
           <td colspan="2">Ordina per</td>
          </tr>

          <!-- se showSearchRow e true, creo un campo di ricerca per ogni field sortable -->
          <xsl:text disable-output-escaping="yes">
             &lt;% if (showSearchRow) { %&gt;
           </xsl:text>
                       <tr bgcolor="#CECECE">
                       <xsl:for-each select="field[not(@autoInc) or @autoInc='false']">
                         <td>
                           <xsl:choose>
                              <xsl:when test="@isKey or @sortable='true'">
                                 <table>
                                 <tr>
                                    <td>
                                       <xsl:text disable-output-escaping="yes">
                                          &lt;INPUT type="text" name="&lt;%= searchFieldNames_</xsl:text><xsl:value-of select="../@name"/>
                                       <xsl:text disable-output-escaping="yes">.get("</xsl:text><xsl:value-of select="@name"/>
                                       <xsl:text disable-output-escaping="yes">") %&gt;" size="10" &gt;</xsl:text>
                                    </td>
                                    <td>
                                       <xsl:text disable-output-escaping="yes">
                                          &lt;INPUT type="radio" name="&lt;%= searchFieldModeNames_</xsl:text><xsl:value-of select="../@name"/>
                                       <xsl:text disable-output-escaping="yes">.get("</xsl:text><xsl:value-of select="@name"/>
                                       <xsl:text disable-output-escaping="yes">") %&gt;" checked value="and" &gt;AND</xsl:text>
                                    </td>
                                 </tr>
                                 <tr>
                                    <td>
                                       <xsl:text disable-output-escaping="yes">
                                          &lt;INPUT type="checkbox" name="&lt;%= searchFieldAlgorithmNames_</xsl:text><xsl:value-of select="../@name"/>
                                       <xsl:text disable-output-escaping="yes">.get("</xsl:text><xsl:value-of select="@name"/>
                                       <xsl:text disable-output-escaping="yes">") %&gt;" value="weak" size="10" &gt; Weak</xsl:text>
                                    </td>
                                    <td>
                                       <xsl:text disable-output-escaping="yes">
                                          &lt;INPUT type="radio" name="&lt;%= searchFieldModeNames_</xsl:text><xsl:value-of select="../@name"/>
                                       <xsl:text disable-output-escaping="yes">.get("</xsl:text><xsl:value-of select="@name"/>
                                       <xsl:text disable-output-escaping="yes">") %&gt;" value="or" &gt;OR</xsl:text>
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
                         <td colspan="2">
                           <xsl:text disable-output-escaping="yes">
                                          &lt;input type="button" value="Applica!" onClick="javascript:document.dbform.submit()"&gt;
                           </xsl:text>
                         </td>
                      </tr>
          <xsl:text disable-output-escaping="yes">
                  &lt;% } %&gt;
          </xsl:text>

          <tr bgcolor="#CCCC00">
              <xsl:for-each select="field[not(@autoInc) or @autoInc='false']">
                <td>
                  <xsl:value-of select="@name"/>
                </td>
              </xsl:for-each>
                <td colspan="2"></td> <!-- header per tenere posto per i tastini di modifica/cancellazione della riga, nelle righe sottostanti -->
          </tr>
       </db:header>

       <db:errors/>

       <db:body>

          <xsl:text disable-output-escaping="yes">
            &lt;tr bgcolor="#&lt;%= (i++%2==0) ? "FFFFCC" : "FFFF99" %&gt;"&gt;
          </xsl:text>
          <xsl:for-each select="field">
             <xsl:if test="not(@autoInc) or @autoInc = 'false' ">
             <xsl:text disable-output-escaping="yes">&lt;td&gt;</xsl:text>
             <xsl:choose>
               <xsl:when test="position()=1">
                   <xsl:text disable-output-escaping="yes">&lt;a href="&lt;db:linkURL href="/</xsl:text>
                   <xsl:value-of select="../@name"/>
                   <xsl:text disable-output-escaping="yes">_single_ro.jsp" tableName="</xsl:text>
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
             <xsl:text disable-output-escaping="yes">&amp;nbsp;&lt;/td&gt;</xsl:text>
             </xsl:if>
          </xsl:for-each>
          <xsl:choose>
          <xsl:when test="$updateDeleteButtons = 'true'">
	   <td>
                   <xsl:text disable-output-escaping="yes">&lt;a href="&lt;db:linkURL href="/</xsl:text>
                   <xsl:value-of select="@name"/>
                   <xsl:text disable-output-escaping="yes">_single.jsp" tableName="</xsl:text>
                   <xsl:value-of select="@name"/>
                   <xsl:text disable-output-escaping="yes">" position="&lt;%= position_</xsl:text>
                   <xsl:value-of select="@name"/>
                   <xsl:text disable-output-escaping="yes"> %&gt;"/&gt;" &gt;&lt;IMG src="edit.gif" border="0" &gt; &lt;/a&gt;</xsl:text>
           </td>
	   <td><db:deleteButton caption="delete" flavor="image" alt="cancella" src="delete.gif" style="border:0"/></td>
           </xsl:when>
           <xsl:otherwise>
               <td colspan="2"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
           </xsl:otherwise>
           </xsl:choose>

	   <xsl:text disable-output-escaping="yes">
              &lt;/tr&gt;
           </xsl:text>


	 <center><db:insertButton caption="Nuovo {@name}"/></center>
       </db:body>

       <db:footer>


       <xsl:text disable-output-escaping="yes">
          &lt;/table&gt;
        </xsl:text>

         <xsl:if test="not($maxRows='*')">

 	 <table align="center" border="0">
           <tr>
 	     <td><db:navFirstButton caption="&lt;&lt; First" style="width:90"/></td>
	     <td><db:navPrevButton caption="&lt; Previous" style="width:90"/></td>
	     <td><db:navNextButton caption="Next &gt;" style="width:90"/></td>
	     <td><db:navLastButton caption="Last &gt;&gt;" style="width:90"/></td>
           </tr>
         </table>

         </xsl:if>

 	 <table align="center" border="0">
           <tr>
	     <td><db:navNewButton caption="New" followUp="/{@name}_single.jsp" style="width:40"/></td>
           </tr>
         </table>
       </db:footer>

     </db:dbform>
    </body>
   </html>


  </xsl:template>

</xsl:stylesheet>
