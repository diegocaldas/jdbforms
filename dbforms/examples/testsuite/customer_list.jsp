  
    <%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>

    <html>
      <head>
	<db:base/>
	<title></title>
	<link rel="stylesheet" href="dbforms.css">
      </head>

      <body bgcolor="#99CCFF" >
<table align="center" border="0" width="100%" cellspacing="0" cellpadding="1" bgcolor="#999900" xmlns:db="http://www.wap-force.com/dbforms"><tr><td><table border="0" width="100%" cellspacing="0" cellpadding="3" bgcolor="#999900"><tr bgcolor="#CCCC00"><td><h1>customer</h1></td><td align="right"><a href="customer_list.jsp">[List]</a><a href="menu.jsp">[Menu]</a><a href="logout.jsp">[Log out]</a></td></tr></table></td></tr></table>
      <% int i=0; %>			

      <db:dbform tableName="customer" maxRows="*" followUp="/customer_list.jsp" autoUpdate="false">

        <db:header>
          <db:errors/>			

          <table align="center" cellspacing=0 cellpadding=2 bgcolor="#ffffff" width="100%">
          <tr bgcolor="#CCCC00">



   <td>id</td>
     <td>firstname</td>
     <td>lastname</td>
     <td>address</td>
     <td>pcode</td>
     <td>city</td>
     	
          </tr>
        </db:header>		

        <db:body allowNew="false">
          <% String bgcolor = (i % 2 == 0) ? "#FFFFCC" : "#FFFF99"; i++; %>		
          <tr bgcolor="<%= bgcolor %>">
    <td><a href="<db:linkURL href="/customer_single.jsp" tableName="customer" position="<%= position_customer %>"/>" ><%= currentRow_customer.get("id") %></a>&nbsp;</td>
        <td><%= currentRow_customer.get("firstname") %>&nbsp;</td>
        <td><%= currentRow_customer.get("lastname") %>&nbsp;</td>
        <td><%= currentRow_customer.get("address") %>&nbsp;</td>
        <td><%= currentRow_customer.get("pcode") %>&nbsp;</td>
        <td><%= currentRow_customer.get("city") %>&nbsp;</td>
        
          </tr>			              		 				
        </db:body>
        <db:footer>
          </table>
          <center><db:navNewButton caption="new customer..." followUp="/customer_single.jsp" /></center>			
        </db:footer>
       </db:dbform>
      </body>
     </html>
   

	
