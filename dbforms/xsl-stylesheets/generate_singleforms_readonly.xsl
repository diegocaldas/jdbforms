<?xml version='1.0'?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:db="http://www.wap-force.com/dbforms">


<xsl:output indent="yes"/>

<xsl:template match="table">

//--file "<xsl:value-of select="@name"/>_single_ro.jsp" ------------------------------------------------

<xsl:text disable-output-escaping="yes">
&lt;%@ taglib uri="/WEB-INF/taglib.tld" prefix="db" %&gt;
</xsl:text>

  <html>
    <head>
      <db:base/>
      <link rel="stylesheet" href="dbforms.css"/>
    </head>
    <body>

	<table bgcolor="#0000FF" cellpadding="1" cellspacing="0" width="100%" border="0" align="center">
	  <tr><td>
	  <table bgcolor="#DDCCDD" cellpadding="3" cellspacing="0" width="100%" border="0">
	    <tr>
		<td bgcolor="#00CCAA" align="right">
		<a href="{@name}_list.jsp">[List]</a>
		<a href="menu.jsp">[Menu]</a>
		<a href="logout.jsp">[Log out]</a>		
		</td>
	      </tr>
	   </table>
	   </td></tr>
	</table>

     <db:dbform tableName="{@name}" maxRows="1" followUp="/{@name}_single_ro.jsp" autoUpdate="false">
       <db:header>
	<h1><xsl:value-of select="@name"/></h1>
       </db:header>				
       <db:errors/>		
       <db:body allowNew="false">
         <table border="0" align="center" width="400">

           <xsl:for-each select="field">

           <!-- we create input fields for NON-AUTOMATIC values only -->

           <xsl:if test="string-length(@autoInc) = 0 or @autoInc = 'false' ">

           <xsl:variable name="bgcolor">
             <xsl:choose>
               <xsl:when test="position() mod 2 = 0">fee9aa</xsl:when>
               <xsl:otherwise>fee9ce</xsl:otherwise>
             </xsl:choose>
           </xsl:variable>


           <tr bgcolor="#{$bgcolor}">
             <td><xsl:value-of select="@name"/></td>
             <td>


               <xsl:choose>
                 <xsl:when test="@fieldType='int' or @fieldType='smallint'  or @fieldType='tinyint'">
                   <db:label fieldName="{@name}" />
                 </xsl:when>
                 <xsl:when test="@fieldType='char' or @fieldType='varchar'  or @fieldType='nvarchar'  or @fieldType='longvarchar'">
                   <xsl:choose>
                     <xsl:when test="@size>80">
                        <db:textArea fieldName="{@name}" cols="40" rows="3" wrap="virtual" />
                     </xsl:when>
                     <xsl:otherwise>
                        <db:label fieldName="{@name}" />
                     </xsl:otherwise>
                   </xsl:choose>
                 </xsl:when>
                 <xsl:when test="@fieldType='date'">
                   <db:label fieldName="{@name}" />
                 </xsl:when>
                 <xsl:when test="@fieldType='timestamp'">
                   <db:label fieldName="{@name}" />
                 </xsl:when>
                 <xsl:when test="@fieldType='double'">
                   <db:label fieldName="{@name}" />
                 </xsl:when>
                 <xsl:when test="@fieldType='float' or @fieldType='real'">
                   <db:label fieldName="{@name}" />
                 </xsl:when>
                 <xsl:when test="@fieldType='numeric'">
                   <db:label fieldName="{@name}" />
                 </xsl:when>
                 <xsl:when test="@fieldType='blob' or @fieldType='image'">
                   <a href="&lt;db:blobURL fieldName=&quot;{@name}&quot; /&gt;">[view]</a>
                 </xsl:when>
                 <xsl:when test="@fieldType='diskblob'">
                   <a href="&lt;db:blobURL fieldName=&quot;{@name}&quot; /&gt;">[view]</a>
                 </xsl:when>
                 <xsl:otherwise>
                 *** error - fieldtype <xsl:value-of select="@fieldType"/> is currently not supported. please send an e-mail to joepeer@wap-force.net ! ***
                 </xsl:otherwise>
               </xsl:choose>


             </td>
           </tr>

           </xsl:if>

           </xsl:for-each>
         </table>			
         <br/>
	 
       </db:body>

       <db:footer>
 	 <table align="center" border="0">	
           <tr>
 	     <td><db:navFirstButton caption="&lt;&lt; First" style="width:90"/></td>
	     <td><db:navPrevButton caption="&lt; Previous" style="width:90"/></td>
	     <td><db:navNextButton caption="Next &gt;" style="width:90"/></td>
	     <td><db:navLastButton caption="Last &gt;&gt;" style="width:90"/></td>	
           </tr>
         </table>
       </db:footer>

     </db:dbform>	
    </body>
   </html>	
  </xsl:template>

</xsl:stylesheet>
