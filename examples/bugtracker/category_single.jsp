<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<%@ taglib uri="/WEB-INF/request.tld" prefix="request" %>

<html>
<head>
<db:base/>
<link rel="stylesheet" href="dbforms.css"/>
<META name="GENERATOR" content="IBM WebSphere Studio">
</head>
<body>

<table width="100%" bgcolor="#ccffff" cellspacing="0" cellpadding="0">
    <tr>
        <td><img src="templates/sourceforge/images/bt_logo.jpg" border="0"></td>
        <td align="right">
   	    		<request:isuserinrole role="bug_admin" value="true"> 
 	  	     		<img src="templates/sourceforge/images/pfeilchen.jpg"/> 
  		      		<a href="category_list.jsp">[<db:message key="menu.list.displayname"/>]</a> 
        		</request:isuserinrole> 
   
        		<request:isuserinrole role="bug_admin" value="true"> 
        			<img src="templates/sourceforge/images/pfeilchen.jpg"/>
        			<a href="admin_menu.jsp">[<db:message key="menu.admin.displayname"/>]</a>&nbsp; 
        		</request:isuserinrole> 
        		
        		<request:isuserinrole role="bug_admin" value="true"> 
        			<img src="templates/sourceforge/images/pfeilchen.jpg"/> 
        			<a href="logout.jsp">[<db:message key="menu.logout.displayname"/>]</a> 	
        		</request:isuserinrole> 
        		
      		
        		<request:isuserinrole role="bug_admin" value="false"> 
        			<img src="templates/sourceforge/images/pfeilchen.jpg"/> 
        			<a href="admin_menu.jsp">[<db:message key="menu.login.displayname"/>]</a> 
        		</request:isuserinrole></td>
    </tr>
</table>


<db:dbform 	tableName="category" 
			maxRows="1" 
			followUp="/category_single.jsp" 
			autoUpdate="false"
			captionResource="true"
			formValidatorName="Category"> 

<db:header>
	<h1><db:message key="categoryForm.title"/></h1>
</db:header> 

<db:xmlErrors/>

<db:body>
<table border="0" align="center" width="400">
    <tr bgcolor="#fee9ce">
        <td>id</td>
        <td><db:textField fieldName="id" size="11"/></td>
    </tr>
    <tr bgcolor="#fee9aa">
        <td><db:message key="categoryForm.title.displayname"/></td>
        <td><db:textField fieldName="title" size="40"/></td>
    </tr>
    <tr bgcolor="#fee9ce">
        <td><db:message key="categoryForm.description.displayname"/></td>
        <td><db:textArea fieldName="description" cols="40" rows="3" wrap="virtual"/></td>
    </tr>
</table>
<br>
<center>
	<db:insertButton caption="categoryForm.button.addthisone"/>
</center>
</db:body> 

<db:footer>
<table align="center" border="0">
    <tr>
        <td><db:navFirstButton caption="button.nav.first" style="width:90"/></td>
        <td><db:navPrevButton caption="button.nav.previous" style="width:90"/></td>
        <td><db:navNextButton caption="button.nav.next" style="width:90"/></td>
        <td><db:navLastButton caption="button.nav.last" style="width:90"/></td>
    </tr>
</table>
<table align="center" border="0">
    <tr>
        <td><db:updateButton caption="button.update" style="width:110"/></td>
        <td><db:deleteButton caption="button.delete" style="width:90"/></td>
        <td><db:navNewButton caption="button.new" style="width:90"/></td>
    </tr>
</table>
</db:footer> </db:dbform>

<h5 style="color:#ff3333">using I18N for error messages </h5>

</body>
</html>



