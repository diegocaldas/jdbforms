
<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<%@ taglib uri="/WEB-INF/request.tld" prefix="request" %>

<html>
<head>
<db:base/>
<link rel="stylesheet" href="dbforms.css"/>
</head>
<body>

<table width="100%" bgcolor="#ccffff" cellspacing="0" cellpadding="0">
<tr>
<td>
<img src="img/bt_logo.gif" border="0">
</td>
<td align="right">
<img src="img/pfeilchen.gif"/>
<a href="priority_list.jsp">[List]</a>

<request:isuserinrole role="bug_admin"value="true">
<img src="img/pfeilchen.gif"/><a href="admin_menu.jsp">[Menu]</a>&nbsp;
</request:isuserinrole>


<img src="img/pfeilchen.gif"/>
<request:isuserinrole role="bug_admin"value="true">
<a href="logout.jsp">[Log out]</a>
</request:isuserinrole>
<request:isuserinrole role="bug_admin" value="false">
<a href="admin_menu.jsp">[Log in]</a>
</request:isuserinrole>

</td></tr></table>

<db:dbform tableName="priority" maxRows="1" followUp="/priority_single.jsp" autoUpdate="false">
<db:header>
<h1>priority</h1>
</db:header>
<db:errors/>
<db:body>
<table border="0" align="center" width="400">
<tr bgcolor="#fee9ce">
<td>id</td>
<td>
<db:textField fieldName="id" size="11"/>
</td>
</tr>
<tr bgcolor="#fee9aa">
<td>title</td>
<td>
<db:textField fieldName="title" size="40"/>
</td>
</tr>
<tr bgcolor="#fee9ce">
<td>description</td>
<td>
<db:textArea fieldName="description" cols="40" rows="3" wrap="virtual"/>
</td>
</tr>
</table>
<br/>
<center>
<db:insertButton caption="Create new priority"/>
</center>
</db:body>
<db:footer>
<table align="center" border="0">
<tr>
<td>
<db:navFirstButton caption="&#60;&#60; First" style="width:90"/>
</td>
<td>
<db:navPrevButton caption="&#60; Previous" style="width:90"/>
</td>
<td>
<db:navNextButton caption="Next &#62;" style="width:90"/>
</td>
<td>
<db:navLastButton caption="Last &#62;&#62;" style="width:90"/>
</td>
</tr>
</table>
<table align="center" border="0">
<tr>
<td>
<db:updateButton caption="Update" style="width:90"/>
</td>
<td>
<db:deleteButton caption="Delete" style="width:90"/>
</td>
<td>
<db:navNewButton caption="New" style="width:40"/>
</td>
</tr>
</table>
</db:footer>
</db:dbform>
</body>
</html>

	
	

	
