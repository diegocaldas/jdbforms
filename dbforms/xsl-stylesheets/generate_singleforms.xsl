<?xml version='1.0'?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:db="http://www.wap-force.com/dbforms">

<xsl:output indent="yes"/>

<!--
**
**   STYLESHEET FOR GENERATION OF JSP VIEWS FOR DBFORMS
** 
**   This stylesheet will make JSP views that list content of 1 table row.
**   (== a dataset). Data can be updated. 
**
**  
-->

<!--
definition of variables
choose appropriate values that fit your needs
-->

<xsl:variable name="pageBgColor">99CCFF</xsl:variable>


<xsl:template match="table">

//--file "<xsl:value-of select="@name"/>_single.jsp" ------------------------------------------------

<xsl:text disable-output-escaping="yes">
&lt;%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %&gt;
</xsl:text>

  <html>
    <head>
      <db:base/>
      <link rel="stylesheet" href="dbforms.css"/>
    </head>
    <body bgcolor="#{$pageBgColor}">

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




     <db:dbform tableName="{@name}" maxRows="1" followUp="/{@name}_single.jsp" autoUpdate="false">
       <db:header>	
       </db:header>				
       <db:errors/>		
       <db:body>
         <table border="0" align="center" width="400">

           <xsl:for-each select="field">

           <!-- we create input fields for NON-AUTOMATIC values only -->

           <xsl:if test="string-length(@autoInc) = 0 or @autoInc = 'false' ">

           <xsl:variable name="bgcolor">
             <xsl:choose>
               <xsl:when test="position() mod 2 = 0">FFFFCC</xsl:when>
               <xsl:otherwise>FFFF99</xsl:otherwise>
             </xsl:choose>
           </xsl:variable>


           <tr bgcolor="#{$bgcolor}">
             <td><xsl:value-of select="@name"/></td>
             <td>


               <xsl:choose>
                 <xsl:when test="@fieldType='int' or @fieldType='smallint'  or @fieldType='tinyint'">
                   <db:textField fieldName="{@name}" size="{@size}" />
                 </xsl:when>
                 <xsl:when test="@fieldType='char' or @fieldType='varchar' or @fieldType='varchar2'  or @fieldType='nvarchar'  or @fieldType='longvarchar'">
                   <xsl:choose>
                     <xsl:when test="@size>80">
                        <db:textArea fieldName="{@name}" cols="40" rows="3" wrap="virtual" />
                     </xsl:when>
                     <xsl:otherwise>
                        <db:textField fieldName="{@name}" size="{@size}" />
                     </xsl:otherwise>
                   </xsl:choose>
                 </xsl:when>
                 <xsl:when test="@fieldType='date'">
                   <db:dateField fieldName="{@name}" size="{@size}" />
                 </xsl:when>
                 <xsl:when test="@fieldType='timestamp'">
                   <db:textField fieldName="{@name}" size="{@size}" />
                 </xsl:when>
                 <xsl:when test="@fieldType='double'">
                   <db:textField fieldName="{@name}" size="{@size}" />
                 </xsl:when>
                 <xsl:when test="@fieldType='float' or @fieldType='real'">
                   <db:textField fieldName="{@name}" size="{@size}" />
                 </xsl:when>
                 <xsl:when test="@fieldType='numeric'  or @fieldType='number'">
                   <db:textField fieldName="{@name}" size="{@size}" />
                 </xsl:when>
                 <xsl:when test="@fieldType='blob' or @fieldType='image'">                   
                   <db:file fieldName="{@name}" />
                 </xsl:when>
                 <xsl:when test="@fieldType='diskblob'">                  
                   <db:file fieldName="{@name}" />
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
	 <center><db:insertButton caption="Create new {@name}"/></center>				 		
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
 	 <table align="center" border="0">	
           <tr>
	     <td><db:updateButton caption="Update" style="width:90"/></td>
	     <td><db:deleteButton caption="Delete" style="width:90"/></td>
	     <td><db:navNewButton caption="New" style="width:40"/></td>
           </tr>
         </table>
       </db:footer>

     </db:dbform>	
    </body>
   </html>	
  </xsl:template>

</xsl:stylesheet>
