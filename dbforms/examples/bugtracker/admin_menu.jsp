<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<%@ taglib uri="/WEB-INF/request.tld" prefix="request" %>

<html>
<head>
<db:base/>
<title></title>
<link rel="stylesheet" href="dbforms.css">
<META name="GENERATOR" content="IBM WebSphere Studio">
</head>

<body bgcolor="#ccffff">

<table width="100%" bgcolor="#ccffff" cellspacing="0" cellpadding="0">
    <tr>
        <td><img src="templates/sourceforge/images/bt_logo.jpg" border="0"></td>
        <td align="right">
        	 
        <request:isuserinrole role="bug_admin" value="true"> 
	        <img src="templates/sourceforge/images/pfeilchen.jpg"/>
        	<a href="logout.jsp">[<db:message key="menu.logout.displayname"/>]</a> </request:isuserinrole> 
        <request:isuserinrole role="bug_admin" value="false"> 
			<img src="templates/sourceforge/images/pfeilchen.jpg"/>
        	<a href="admin_menu.jsp">[<db:message key="menu.login.displayname"/>]</a> 
        </request:isuserinrole></td>
    </tr>
</table>


<db:dbform 	followUp="/postlogin.jsp"
			captionResource="true">
<center>
<h1><db:message key="adminForm.title.displayname"/> </h1>


<hr/>

<table cellspacing="20">


    <tr valign="TOP">

        <td valign="TOP">

        <p><db:message key="adminForm.bugs.displayname"/></p>

        <p>	<db:gotoButton caption="adminForm.button.bugslist.displayname" destination="/bugs_list.jsp" style="width:200"/> 
        	<db:gotoButton caption="adminForm.button.bugssingle.displayname" destination="/bugs_single_readonly.jsp" style="width:200"/></p>
        <hr>
        </td>
    </tr>

    <tr valign="TOP">

        <td valign="TOP">

        <p><db:message key="adminForm.category.displayname"/></p>

        <p>	<db:gotoButton caption="adminForm.button.categorylist.displayname" destination="/category_list.jsp" style="width:200"/> 
        	<db:gotoButton caption="adminForm.button.categorysingle.displayname" destination="/category_single.jsp" style="width:200"/></p>
        <hr>
        </td>
    </tr>


    <tr valign="TOP">

        <td valign="TOP">

        <p><db:message key="adminForm.priority.displayname"/> </p>

        <p>	<db:gotoButton caption="adminForm.button.prioritylist.displayname" destination="/priority_list.jsp" style="width:200"/> 
        	<db:gotoButton caption="adminForm.button.prioritysingle.displayname" destination="/priority_single.jsp" style="width:200"/></p>
        <hr>
        </td>
    </tr>
</table>
</db:dbform>
</body>
</html>



