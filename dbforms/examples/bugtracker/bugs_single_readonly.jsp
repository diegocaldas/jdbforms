<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<%@ taglib uri="/WEB-INF/request.tld" prefix="request" %>

<html>
<head>
<db:base/>
<link rel="stylesheet" href="dbforms.css"/>
</head>
<body bgcolor="#ccffff">

<table width="100%" bgcolor="#ccffff" cellspacing="0" cellpadding="0">
<tr>
<td>
<img src="img/bt_logo.gif" border="0">
</td>
<td align="right">
<img src="img/pfeilchen.gif"/>
<a href="bugs_list.jsp">[List]</a>&nbsp;
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
</td>
</tr>
</table>


<db:dbform tableName="bugs" maxRows="1" followUp="/bugs_single_readonly.jsp" autoUpdate="false" gotoPrefix="fv_">
<db:header>
<h1>bugs</h1>
</db:header>
<db:errors/>
<db:body>
<table border="0" align="center" cellspacing="1" width="400">

<tr bgcolor="#fee9ce">
<td>ID #</td>
<td><db:label fieldName="id" /></td>
</tr>

<tr bgcolor="#fee9aa">
<td>category</td>
<td>

          <db:dataLabel fieldName="category">
            <db:tableData 
               name="data_category" 
               foreignTable="category"
               visibleFields="title"
               storeField="id"
            />
          </db:dataLabel>   

</td>
</tr>
<tr bgcolor="#fee9ce">
<td>priority</td>
<td>

          <db:dataLabel fieldName="priority">
            <db:tableData 
               name="data_priority" 
               foreignTable="priority"
               visibleFields="title"
               storeField="id"
            />
          </db:dataLabel>           

</td>
</tr>
<tr bgcolor="#fee9aa">
<td>title</td>
<td>
<db:label fieldName="title" />
</td>
</tr>
<tr bgcolor="#fee9ce">
<td>description</td>
<td>
<db:label fieldName="description" />&nbsp;
</td>
</tr>
<tr bgcolor="#fee9aa">
<td>indate</td>
<td>
<db:label fieldName="indate" />
                 </td>
</tr>
<tr bgcolor="#fee9ce">
<td>outdate</td>
<td>
<db:label fieldName="outdate" />&nbsp;                 
</td>
</tr>
<tr bgcolor="#fee9aa">
<td>reporter</td>
<td>
<db:label fieldName="reporter" />
</td>
</tr>
<tr bgcolor="#fee9ce">
<td>bugstate</td>
<td>
<db:label fieldName="bugstate" />
</td>
</tr>
</table>
<br/>


</db:body>
<db:footer>


<request:isuserinrole role="bug_admin"value="true">
<center><db:gotoButton caption="Edit this bug report!" destination="/bugs_single.jsp"/></center>
</request:isuserinrole>

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
<td colspan="3">
<db:navNewButton caption="New" followUp="/bugs_single_addnew.jsp" />
</td>
</tr>
</table>
</db:footer>
</db:dbform>
</body>
</html>

	

