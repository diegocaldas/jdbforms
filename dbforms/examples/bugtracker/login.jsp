<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<%@ taglib uri="/WEB-INF/tags/request.tld" prefix="request" %>

<html>

<head>	
  <db:base/>
	<link rel="stylesheet" href="dbforms.css">
<META name="GENERATOR" content="IBM WebSphere Studio">
</head>

<body>

<IMG src="templates/sourceforge/images/bt_logo.jpg" border=0 >
<left>
<table width="100%" bgcolor="#ccffff" cellspacing="0" cellpadding="0">
    <tr>
        <td align="right"><img src="templates/sourceforge/images/pfeilchen.jpg"/> <a href="bugs_list.jsp">[<db:message key="menu.list.displayname"/>]</a>&nbsp; 
        	<request:isuserinrole role="bug_admin" value="true"> 
        		<img src="templates/sourceforge/images/pfeilchen.jpg"/>
        		<a href="admin_menu.jsp">[<db:message key="menu.admin.displayname"/>]</a>&nbsp; 
        	</request:isuserinrole> 
        	<img src="templates/sourceforge/images/pfeilchen.jpg"/> 
        	<request:isuserinrole role="bug_admin" value="true"> 
        		<a href="logout.jsp">[<db:message key="menu.logout.displayname"/>]</a> 
        	</request:isuserinrole> 
        	<request:isuserinrole role="bug_admin" value="false"> 
        		<a href="admin_menu.jsp">[<db:message key="menu.admin.displayname"/>]</a> 
        	</request:isuserinrole>
        </td>
    </tr>
</table>
</left>

<BR><BR><BR><BR>
<H1><db:message key="loginForm.message.welcome"/></H1>
<HR>
<BR><BR>
<center>
<H2><db:message key="loginForm.message.valid"/></H2>
</center>
<BR><BR>

	<db:style template="sourceforge" part="begin" paramList="width='300'"/>
	  <db:style template="center" part="begin" />
	  
		
	  <!-- code for orion app-server -->
		<form action="<%= response.encodeURL("j_security_check") %>">
		
			<table align="center">
				<tr>
					<td>
						<b><db:message key="loginForm.username.displayname"/> : </b><br><input name="j_username" type="text"><br>
					</td>
				</tr>
				<tr>
					<td>
						<b><db:message key="loginForm.password.displayname"/> : </b><br><input name="j_password" type="password"><br>
					</td>
				</tr>
				<tr>
					<td align="center">
						<br><input type="submit" value="<db:message key="menu.login.displayname"/>" >						
					</td>
				</tr>
			</table>
		</form>
		
	  <db:style template="center" part="end" />
	<db:style template="sourceforge" part="end"/>
	  	  
	  
</body>
</html>