<?xml version='1.0'?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:db="http://www.wap-force.com/dbforms">
<xsl:output indent="yes"/>

<xsl:template match="/">
//--file "menu.jsp" -------------------------------------------
<xsl:text disable-output-escaping="yes">&lt;%@ taglib uri="/WEB-INF/taglib.tld" prefix="db" %&gt;</xsl:text>
<html>
<head>
  <db:base/>
  <title>Menu</title>
  <link rel="stylesheet" href="dbforms.css"/>
</head>
<body>
  <db:errors/>
	

<table width="100%" height="100%">
<tr><td></td></tr><tr><td>
<center>
  <db:dbform followUp="/menu.jsp">
  <center>  	
     <h1>M A I N M E N U</h1> <hr/>	  		  	  	
       <table cellspacing="20">
         <xsl:apply-templates/>
       </table>
	
      <hr/>
			
      <p><db:gotoButton caption="Log out" destination="/logout.jsp" /></p>			
    </center>
  </db:dbform>
  </center>
</td></tr>
<tr><td></td>
</tr></table>  
  
  
</body>
</html>
</xsl:template>

<xsl:template match="table">
  <tr valign="TOP">
    <td valign="TOP">
     <p>
       <db:gotoButton caption="{@name} (list)" destination="/{@name}_list.jsp" style="width:200"/>
       <db:gotoButton caption="{@name} (single)" destination="/{@name}_single.jsp" style="width:200"/>
     </p>	  	
    </td>				
  </tr>   		 			
</xsl:template>

</xsl:stylesheet>
