<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<%@ taglib uri="/WEB-INF/tags/request.tld" prefix="request" %>

<html>
<head>
<db:base/>
<link rel="stylesheet" href="dbforms.css"/>
<META name="GENERATOR" content="IBM WebSphere Studio">
</head>

<body bgcolor="#ccffff">

<table width="100%" bgcolor="#ccffff" cellspacing="0" cellpadding="0">
    <tr>
        <td><img src="templates/sourceforge/images/bt_logo.jpg" border="0"></td>
        <td align="right">
        	<img src="templates/sourceforge/images/pfeilchen.jpg"/> 
        	<a href="bugs_list.jsp">[<db:message key="menu.list.displayname"/>]</a>&nbsp; 
        	
        	<request:isuserinrole role="bug_admin" value="true"> 
        		<img src="templates/sourceforge/images/pfeilchen.jpg"/> 
        	 	<a href="bugs_single_readonly.jsp">[<db:message key="menu.list.readonly.displayname"/>]</a> 
        	</request:isuserinrole> 
 
        	<request:isuserinrole role="bug_admin" value="true"> 
        		<img src="templates/sourceforge/images/pfeilchen.jpg"/> 
        	 	<a href="logout.jsp">[<db:message key="menu.logout.displayname"/>]</a> 
        	</request:isuserinrole> 

        	<request:isuserinrole role="bug_admin" value="true"> 
	        	<img src="templates/sourceforge/images/pfeilchen.jpg"/>
        		<a href="admin_menu.jsp">[<db:message key="menu.admin.displayname"/>]</a> 
        	</request:isuserinrole>
 
         	<request:isuserinrole role="bug_admin" value="false"> 
	        	<img src="templates/sourceforge/images/pfeilchen.jpg"/>
        		<a href="admin_menu.jsp">[<db:message key="menu.login.displayname"/>]</a> 
        	</request:isuserinrole>
 
        </td>
    </tr>
</table>

<db:dbform 	tableName="bugs" 
			maxRows="1" 
			followUp="/bugs_single_addnew.jsp" 
			formValidatorName="Bugs" 
			redisplayFieldsOnError="true" 
			autoUpdate="false"
			captionResource="true"> 
			
<db:header>
	<h1><db:message key="bugsForm.title.add"/></h1>
</db:header> <db:xmlErrors/> <db:body>

<table border="0" cellspacing="1" align="center" width="500">



    <tr bgcolor="#fee9aa">
        <td><db:message key="bugsForm.category.displayname"/></td>
        <td><db:select fieldName="category" size="0"> 
        		<db:tableData 	name="data_category" 
        						foreignTable="category" 
        						visibleFields="title" 
        						storeField="id" />
        	</db:select>
        </td>
    </tr>
    <tr bgcolor="#fee9ce">
        <td><db:message key="bugsForm.priority.displayname"/></td>
        <td><db:select fieldName="priority" size="0"> 
        		<db:tableData 	name="data_priority" 
        						foreignTable="priority" 
        						visibleFields="title" 
        						storeField="id"/> 
        		</db:select>
        </td>
    </tr>
    <tr bgcolor="#fee9aa">
        <td><db:message key="bugsForm.title.displayname"/></td>
        <td><db:textField fieldName="title" size="40"/></td>
    </tr>
    <tr bgcolor="#fee9ce">
        <td><db:message key="bugsForm.description.displayname"/></td>
        <td><db:textArea fieldName="description" cols="40" rows="3" wrap="virtual"/></td>
    </tr>

    <tr bgcolor="#fee9ce">
        <td><db:message key="bugsForm.reporter.displayname"/></td>
        <td><db:textField fieldName="reporter" size="40"/></td>
    </tr>
    <tr bgcolor="#fee9ce">
        <td><db:message key="bugsForm.phone.displayname"/></td>
        <td><db:textField fieldName="phone" size="15"/></td>
    </tr>
</table>
<br/>
<center>
	<db:insertButton caption="button.submit" followUp="/bugs_list.jsp" followUpOnError="/bugs_single_addnew.jsp"/>
</center>

</db:body> 

<db:footer>
	<table align="center" border="0">
	</table>
</db:footer> 

</db:dbform>

<h5 style="color:#ff3333">using I18N for error messages </h5>

</body>
</html>



