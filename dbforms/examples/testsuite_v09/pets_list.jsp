  
    <%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>

    <html>
      <head>
	<db:base/>
	<title></title>
	<link rel="stylesheet" href="dbforms.css">
      </head>

      <body bgcolor="#99CCFF" >
<table align="center" border="0" width="100%" cellspacing="0" cellpadding="1" bgcolor="#999900" xmlns:db="http://www.wap-force.com/dbforms"><tr><td><table border="0" width="100%" cellspacing="0" cellpadding="3" bgcolor="#999900"><tr bgcolor="#CCCC00"><td><h1>pets</h1></td><td align="right"><a href="pets_list.jsp">[List]</a><a href="menu.jsp">[Menu]</a><a href="logout.jsp">[Log out]</a></td></tr></table></td></tr></table>
      <% int i=0; %>			

      <db:dbform tableName="pets" maxRows="*" followUp="/pets_list.jsp" autoUpdate="false" multipart="true">

        <db:header>
          <db:errors/>			

          <table align="center" cellspacing=0 cellpadding=2 bgcolor="#ffffff" width="100%">
          <tr bgcolor="#CCCC00">



   <td>pet_id</td>
     <td>name</td>
     <td>customer</td>
     <td>portrait_pic</td>
     <td>story</td>
     	
          </tr>
        </db:header>		

        <db:body allowNew="false">
          <% String bgcolor = (i % 2 == 0) ? "#FFFFCC" : "#FFFF99"; i++; %>		
          <tr bgcolor="<%= bgcolor %>">
    <td><a href="<db:linkURL href="/pets_single.jsp" tableName="pets" position="<%= position_pets %>"/>" ><%= currentRow_pets.get("pet_id") %></a>&nbsp;</td>
        <td><%= currentRow_pets.get("name") %>&nbsp;</td>
        <td>
        <db:dataLabel fieldName="customer">
					<db:tableData 
						name = "fkCust"
						foreignTable = "customer"
						visibleFields = "id,firstname,lastname"
						storeField = "id"
					/>        
        </db:dataLabel>                      
        </td>
        
        <td><%= currentRow_pets.get("portrait_pic") %>&nbsp;</td>
        <td><%= currentRow_pets.get("story") %>&nbsp;</td>
        
          </tr>			              		 				
        </db:body>
        <db:footer>
          </table>
          <center><db:navNewButton caption="new pets..." followUp="/pets_single.jsp" /></center>			
        </db:footer>
       </db:dbform>
      </body>
     </html>
   

	
