  
    <%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>

    <html>
      <head>
	<db:base/>
	<title></title>
	<link rel="stylesheet" href="dbforms.css">
      </head>

      <body bgcolor="#99CCFF" >
<table align="center" border="0" width="100%" cellspacing="0" cellpadding="1" bgcolor="#999900" xmlns:db="http://www.wap-force.com/dbforms"><tr><td><table border="0" width="100%" cellspacing="0" cellpadding="3" bgcolor="#999900"><tr bgcolor="#CCCC00"><td><h1>complaint</h1></td><td align="right"><a href="complaint_list.jsp">[List]</a><a href="menu.jsp">[Menu]</a><a href="logout.jsp">[Log out]</a></td></tr></table></td></tr></table>
      <% int i=0; %>			

      <db:dbform tableName="complaint" maxRows="*" followUp="/complaint_list.jsp" autoUpdate="false">

        <db:header>
          <db:errors/>			

          <table align="center" cellspacing=0 cellpadding=2 bgcolor="#ffffff" width="100%">
          <tr bgcolor="#CCCC00">



   <td>complaint_id</td>
     <td>customer</td>
     <td>usermessage</td>
     <td>incomingdate</td>
     <td>priority</td>
     	
          </tr>
        </db:header>		

        <db:body allowNew="false">
          <% String bgcolor = (i % 2 == 0) ? "#FFFFCC" : "#FFFF99"; i++; %>		
          <tr bgcolor="<%= bgcolor %>">
    <td><a href="<db:linkURL href="/complaint_single.jsp" tableName="complaint" position="<%= position_complaint %>"/>" ><%= currentRow_complaint.get("complaint_id") %></a>&nbsp;</td>
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
        <td><%= currentRow_complaint.get("usermessage") %>&nbsp;</td>
        <td><db:dateLabel fieldName="incomingdate" />&nbsp;</td>
        <td><%= currentRow_complaint.get("priority") %>&nbsp;</td>
        
          </tr>			              		 				
        </db:body>
        <db:footer>
          </table>
          <center><db:navNewButton caption="new complaint..." followUp="/complaint_single.jsp" /></center>			
        </db:footer>
       </db:dbform>
      </body>
     </html>
   

	
