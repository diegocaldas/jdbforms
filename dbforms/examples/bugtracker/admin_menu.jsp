
<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<%@ taglib uri="/WEB-INF/request.tld" prefix="request" %>

    <html>
      <head>
	<db:base/>
	<title></title>
	<link rel="stylesheet" href="dbforms.css">
      </head>

      <body bgcolor="#ccffff">

<table width="100%" bgcolor="#ccffff" cellspacing="0" cellpadding="0">
<tr>
<td>
<img src="img/bt_logo.gif" border="0">
</td>
<td align="right">

<img src="img/pfeilchen.gif"/>

<request:isuserinrole role="bug_admin"value="true">
<a href="logout.jsp">[Log out]</a>
</request:isuserinrole>
<request:isuserinrole role="bug_admin" value="false">
<a href="admin_menu.jsp">[Log in]</a>
</request:isuserinrole>

</td>
</tr>
</table>


<db:dbform followUp="/postlogin.jsp">
<center>
<h1>Welcome!</h1>
<h1>A D M I N - M E N U</h1>
<hr/>

<table cellspacing="20">


	<tr valign="TOP">

<td valign="TOP">

<p>
<b>BUGS</b> 
<br>
edit bug reports: you can change their state, remove them etc.
</p>

<p>
<db:gotoButton caption="bugs (list)" destination="/bugs_list.jsp" style="width:200"/>
<db:gotoButton caption="bugs (single)" destination="/bugs_single.jsp" style="width:200"/>
</p>
<hr>
</td>
</tr>

	<tr valign="TOP">

<td valign="TOP">

<p>
<b>CATEGORY DEFINITIONS</b> &nbsp;<font color="red">[admin only]</font>
<br>
define and change categories for a better organization of bugs.
</p>

<p>
<db:gotoButton caption="category (list)" destination="/category_list.jsp" style="width:200"/>
<db:gotoButton caption="category (single)" destination="/category_single.jsp" style="width:200"/>
</p>
<hr>
</td>
</tr>


	<tr valign="TOP">

<td valign="TOP">

<p>
<b>PRIORITY DEFINNITIONS</b> &nbsp;<font color="red">[admin only]</font>
<br>
define and change priority definitions. we need priority definitions to get an idea of the importance of a certain bug.
</p>

<p>
<db:gotoButton caption="priority (list)" destination="/priority_list.jsp" style="width:200"/>
<db:gotoButton caption="priority (single)" destination="/priority_single.jsp" style="width:200"/>
</p>
<hr>
</td>
</tr>




</table>



</db:dbform>
      </body>
     </html>
   

	
