  
    <%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>

    <html>
      <head>
	<db:base/>
	<title></title>
	<link rel="stylesheet" href="dbforms.css">
      </head>

      <body bgcolor="#99CCFF" >
<table align="center" border="0" width="100%" cellspacing="0" cellpadding="1" bgcolor="#999900" xmlns:db="http://www.wap-force.com/dbforms"><tr><td><table border="0" width="100%" cellspacing="0" cellpadding="3" bgcolor="#999900"><tr bgcolor="#CCCC00"><td><h1>priority</h1></td><td align="right"><a href="priority_list.jsp">[List]</a><a href="menu.jsp">[Menu]</a><a href="logout.jsp">[Log out]</a></td></tr></table></td></tr></table>
      <% int i=0; %>			

      <db:dbform tableName="priority" maxRows="*" followUp="/priority_list.jsp" autoUpdate="false">

        <db:header>
          <db:errors/>			

          <table align="center" cellspacing=0 cellpadding=2 bgcolor="#ffffff" width="100%">
          <tr bgcolor="#CCCC00">



   <td>id</td>
     <td>shortname</td>
     <td>description</td>
     	
          </tr>
        </db:header>		

        <db:body allowNew="false">
          <% String bgcolor = (i % 2 == 0) ? "#FFFFCC" : "#FFFF99"; i++; %>		
          <tr bgcolor="<%= bgcolor %>">
    <td><a href="<db:linkURL href="/priority_single.jsp" tableName="priority" position="<%= position_priority %>"/>" ><%= currentRow_priority.get("id") %></a>&nbsp;</td>
        <td><%= currentRow_priority.get("shortname") %>&nbsp;</td>
        <td><%= currentRow_priority.get("description") %>&nbsp;</td>
        
          </tr>			              		 				
        </db:body>
        <db:footer>
          </table>
          <center><db:navNewButton caption="new priority..." followUp="/priority_single.jsp" /></center>			
        </db:footer>
       </db:dbform>
      </body>
     </html>
   

	
