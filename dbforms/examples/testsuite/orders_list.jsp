  
    <%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>

    <html>
      <head>
	<db:base/>
	<title></title>
	<link rel="stylesheet" href="dbforms.css">
      </head>

      <body bgcolor="#99CCFF" >
<table align="center" border="0" width="100%" cellspacing="0" cellpadding="1" bgcolor="#999900" xmlns:db="http://www.wap-force.com/dbforms"><tr><td><table border="0" width="100%" cellspacing="0" cellpadding="3" bgcolor="#999900"><tr bgcolor="#CCCC00"><td><h1>orders</h1></td><td align="right"><a href="orders_list.jsp">[List]</a><a href="menu.jsp">[Menu]</a><a href="logout.jsp">[Log out]</a></td></tr></table></td></tr></table>
      <% int i=0; %>			

      <db:dbform tableName="orders" maxRows="*" followUp="/orders_list.jsp" autoUpdate="false">

        <db:header>
          <db:errors/>			

          <table align="center" cellspacing=0 cellpadding=2 bgcolor="#ffffff" width="100%">
          <tr bgcolor="#CCCC00">



   <td>order_id</td>
     <td>customer</td>
     <td>service_id</td>
     <td>orderdate</td>
     	
          </tr>
        </db:header>		

        <db:body allowNew="false">
          <% String bgcolor = (i % 2 == 0) ? "#FFFFCC" : "#FFFF99"; i++; %>		
          <tr bgcolor="<%= bgcolor %>">
    <td><a href="<db:linkURL href="/orders_single.jsp" tableName="orders" position="<%= position_orders %>"/>" ><%= currentRow_orders.get("order_id") %></a>&nbsp;</td>
        <td>
        
                <db:dataLabel fieldName="customer_id">
				                      <db:tableData 
				                        name = "fkCust"
				                        foreignTable = "customer"
				                        visibleFields = "id,firstname,lastname"
				                        storeField = "id"
				                      />
        				</db:dataLabel>
        
        &nbsp;</td>
        <td><%= currentRow_orders.get("service_id") %>&nbsp;</td>
        <td><%= currentRow_orders.get("orderdate") %>&nbsp;</td>
        
          </tr>			              		 				
        </db:body>
        <db:footer>
          </table>
          <center><db:navNewButton caption="new orders..." followUp="/orders_single.jsp" /></center>			
        </db:footer>
       </db:dbform>
      </body>
     </html>
   

	
