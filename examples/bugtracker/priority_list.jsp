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

<body>
<table width="100%" bgcolor="#ccffff" cellspacing="0" cellpadding="0">
   <tr>
        <td><img src="templates/sourceforge/images/bt_logo.jpg" border="0"></td>
        <td align="right">
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

<% int i=0; %>

<db:dbform 		tableName="priority" 
				maxRows="*" 
				followUp="/priority_list.jsp" 
				autoUpdate="false"
				captionResource="true"> 

<db:header> <db:errors/>

<table align="center" cellspacing=0 cellpadding=2 bgcolor="#ffffff" width="100%">
    <tr bgcolor="#F7A629">



        <td>id</td>
        <td><db:message key="priorityForm.title.displayname"/></td>
        <td><db:message key="priorityForm.description.displayname"/></td>

    </tr>
    </db:header>

    <db:body allowNew="false"> <% String bgcolor = (i % 2 == 0) ? "#FEE9CE" : "#FEE9AA"; i++; %>
    <tr bgcolor="<%= bgcolor %>">
        <td><a href="<db:linkURL href="/priority_single.jsp" tableName="priority" position="<%= position_priority %>"/>"><%= currentRow_priority.get("id") %></a>&nbsp;</td>
        <td><%= currentRow_priority.get("title") %>&nbsp;</td>
        <td><%= currentRow_priority.get("description") %>&nbsp;</td>

    </tr>
    </db:body>
    <db:footer>
</table>
<center><db:navNewButton caption="priorityForm.button.addnew.displayname" followUp="/priority_single.jsp"/></center>
</db:footer></db:dbform>
</body>
</html>






