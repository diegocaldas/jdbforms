<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<%@ taglib uri="/WEB-INF/request.tld" prefix="request" %>

    <html>
      <head>
	<db:base/>
	<title></title>
	<link rel="stylesheet" href="dbforms.css">
      </head>

      <body>
<table width="100%" bgcolor="#ccffff" cellspacing="0" cellpadding="0">
<tr>
<td>
<img src="img/bt_logo.gif" border="0">
</td>
<td align="right">

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


      <% int i=0; %>			

      <db:dbform tableName="category" maxRows="*" followUp="/category_list.jsp" autoUpdate="false">

        <db:header>
          <db:errors/>			

          <table align="center" cellspacing=0 cellpadding=2 bgcolor="#ffffff" width="100%">
          <tr bgcolor="#F7A629">



           <td>id</td>
             <td>title</td>
             <td>description</td>
     	
          </tr>
        </db:header>		

        <db:body allowNew="false">
          <% String bgcolor = (i % 2 == 0) ? "#FEE9CE" : "#FEE9AA"; i++; %>		
          <tr bgcolor="<%= bgcolor %>">
    <td><a href="<db:linkURL href="/category_single.jsp" tableName="category" position="<%= position_category %>"/>" ><%= currentRow_category.get("id") %></a>&nbsp;</td>
        <td><%= currentRow_category.get("title") %>&nbsp;</td>
        <td><%= currentRow_category.get("description") %>&nbsp;</td>
        
          </tr>			              		 				
        </db:body>
        <db:footer>
          </table>
          <center><db:navNewButton caption="new category..." followUp="/category_single.jsp" /></center>			
        </db:footer>
       </db:dbform>
      </body>
     </html>
   

	
