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
        <td align="right"><img src="templates/sourceforge/images/pfeilchen.jpg"/> 
        	<a href="bugs_list.jsp">[<db:message key="menu.list.displayname"/>]</a>&nbsp; 

        	<request:isuserinrole role="bug_admin" value="true"> 
        		<img src="templates/sourceforge/images/pfeilchen.jpg"/>
        		<a href="admin_menu.jsp">[<db:message key="menu.admin.displayname"/>]</a>&nbsp; 
        	</request:isuserinrole> <img src="templates/sourceforge/images/pfeilchen.jpg"/> 

        	<request:isuserinrole role="bug_admin" value="true"> 
        		<a href="logout.jsp">[<db:message key="menu.logout.displayname"/>]</a> 
        	</request:isuserinrole> 

        	<request:isuserinrole role="bug_admin" value="false"> 
        		<a href="admin_menu.jsp">[<db:message key="menu.login.displayname"/>]</a> 
        	</request:isuserinrole>
         </td>
    </tr>
</table>


<db:dbform 		tableName="bugs" 
				maxRows="1" 
				followUp="/bugs_single_readonly.jsp" 
				autoUpdate="false" gotoPrefix="fv_"
				captionResource="true"
				> <db:header>
				
<h1><db:message key="menu.list.readonly.displayname"/></h1>

</db:header> <db:errors/> <db:body>
<table border="0" align="center" cellspacing="1" width="450">

    <tr bgcolor="#fee9ce">
        <td>ID #</td>
        <td><db:label fieldName="id"/></td>
    </tr>

    <tr bgcolor="#fee9aa">
        <td><db:message key="bugsForm.category.displayname"/></td>
        <td><db:dataLabel fieldName="category"> <db:tableData name="data_category" foreignTable="category" visibleFields="title" storeField="id"/> </db:dataLabel></td>
    </tr>
    <tr bgcolor="#fee9ce">
        <td><db:message key="bugsForm.priority.displayname"/></td>
        <td><db:dataLabel fieldName="priority"> <db:tableData name="data_priority" foreignTable="priority" visibleFields="title" storeField="id"/> </db:dataLabel></td>
    </tr>
    <tr bgcolor="#fee9aa">
        <td><db:message key="bugsForm.title.displayname"/></td>
        <td><db:label fieldName="title"/></td>
    </tr>
    <tr bgcolor="#fee9ce">
        <td><db:message key="bugsForm.description.displayname"/></td>
        <td><db:label fieldName="description"/>&nbsp;</td>
    </tr>
    <tr bgcolor="#fee9aa">
        <td><db:message key="bugsForm.indate.displayname"/></td>
        <td><db:label fieldName="indate"/></td>
    </tr>
    <tr bgcolor="#fee9ce">
        <td><db:message key="bugsForm.outdate.displayname"/></td>
        <td><db:label fieldName="outdate"  nullFieldValue="msg.null"/>&nbsp;</td>
    </tr>
    <tr bgcolor="#fee9aa">
        <td><db:message key="bugsForm.reporter.displayname"/></td>
        <td><db:label fieldName="reporter" nullFieldValue="msg.null"/></td>
    </tr>
     <tr bgcolor="#fee9aa">
        <td><db:message key="bugsForm.phone.displayname"/></td>
        <td><db:label fieldName="phone"/></td>
    </tr>
 
    <tr bgcolor="#fee9ce">
        <td><db:message key="bugsForm.bugstate.displayname"/></td>
		<td>
			<% String state = "bugsForm.status."+rsv_bugs.getField("bugstate").trim(); %>
			<db:message key="<%=state%>"/>
		</td>
    </tr>
    <tr bgcolor="#fee9aa">
        <td><db:message key="bugsForm.contactFirst.displayname"/></td>
		<td><% if(rsv_bugs.getField("contactFirst").equals("1")){ %>
				<db:message key="msg.yes"/>
			<% } else { %>
				<db:message key="msg.no"/>
			<% } %>
		</td>
    </tr>
</table>
<br/>


</db:body> <db:footer> <!-- request:isuserinrole role="bug_admin"value="true" -->

<center><db:gotoButton caption="button.edit" destination="/bugs_single.jsp"/></center>

<!-- /request:isuserinrole -->

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
        <td colspan="3"><db:navNewButton caption="button.new" followUp="/bugs_single_addnew.jsp"/></td>
    </tr>
</table>
</db:footer> </db:dbform>
</body>
</html>



