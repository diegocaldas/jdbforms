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

<db:dbform tableName="bugs" maxRows="1" followUp="/bugs_single.jsp" autoUpdate="false">
<db:header>
<h1>Add a new Bug!</h1>
</db:header>
<db:errors/>
<db:body>
<table border="0" cellspacing="1" align="center" width="400">

<tr bgcolor="#fee9ce">
<td>ID</td>
<td>#<db:label fieldName="id"/></td>
</tr>


<tr bgcolor="#fee9aa">
<td>category</td>
<td>
          <db:select fieldName="category" size="0">
            <db:tableData 
               name="data_category" 
               foreignTable="category"
               visibleFields="title"
               storeField="id"
            />
          </db:select>   
</td>
</tr>
<tr bgcolor="#fee9ce">
<td>priority</td>
<td>

          <db:select fieldName="priority" size="0">
            <db:tableData 
               name="data_priority" 
               foreignTable="priority"
               visibleFields="title"
               storeField="id"
            />
          </db:select>   


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

<tr bgcolor="#fee9aa">
<td>reporter</td>
<td>
<db:textField fieldName="reporter" size="40"/>
</td>
</tr>

<!--
<tr bgcolor="#fee9aa">
<td>title</td>
<td>
<db:textField fieldName="indate" size="40"/>
</td>
</tr>

<tr bgcolor="#fee9aa">
<td>title</td>
<td>
<db:textField fieldName="outdate" size="40"/>
</td>
</tr>
-->

<tr bgcolor="#fee9aa">
<td>title</td>
<td>

          <db:select fieldName="bugstate" size="3">
            <db:staticData name="data_bugstate">
              <db:staticDataItem key="0" value="open"/>
              <db:staticDataItem key="1" value="progress"/>            
              <db:staticDataItem key="2" value="closed"/>            
            </db:staticData>
          </db:select>     

</td>
</tr>

</table>
<br/>
<center>
<db:updateButton caption="UPDATE this bug report!" followUp="/bugs_list.jsp" />
<db:deleteButton caption="REMOVE this bug report!" followUp="/bugs_list.jsp" />
</center>
</db:body>
<db:footer>
<table align="center" border="0">


</table>

</db:footer>
</db:dbform>
</body>
</html>

	

