  
    <%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>

    <html>
      <head>
	<db:base/>
	<title></title>
	<link rel="stylesheet" href="dbforms.css">
      </head>

      <body bgcolor="#99CCFF" >
<table align="center" border="0" width="100%" cellspacing="0" cellpadding="1" bgcolor="#999900" xmlns:db="http://www.wap-force.com/dbforms"><tr><td><table border="0" width="100%" cellspacing="0" cellpadding="3" bgcolor="#999900"><tr bgcolor="#CCCC00"><td><h1>service</h1></td><td align="right"><a href="service_list.jsp">[List]</a><a href="menu.jsp">[Menu]</a><a href="logout.jsp">[Log out]</a></td></tr></table></td></tr></table>
      <% int i=0; %>			

      <db:dbform tableName="service" maxRows="*" followUp="/service_list.jsp" autoUpdate="false">

        <db:header>
          <db:errors/>			

          <table align="center" cellspacing=0 cellpadding=2 bgcolor="#ffffff" width="100%">
          <tr bgcolor="#CCCC00">



   <td>id</td>
     <td>name</td>
     <td>description</td>
     	
          </tr>
        </db:header>		

        <db:body allowNew="false">
          <% String bgcolor = (i % 2 == 0) ? "#FFFFCC" : "#FFFF99"; i++; %>		
          <tr bgcolor="<%= bgcolor %>">
    <td><a href="<db:linkURL href="/service_single.jsp" tableName="service" position="<%= position_service %>"/>" ><%= currentRow_service.get("id") %></a>&nbsp;</td>
        <td><%= currentRow_service.get("name") %>&nbsp;</td>
        <td><%= currentRow_service.get("description") %>&nbsp;</td>
        
          </tr>			              		 				
        </db:body>
        <db:footer>
          </table>
          <center><db:navNewButton caption="new service..." followUp="/service_single.jsp" /></center>			
        </db:footer>
       </db:dbform>
      </body>
     </html>
   

	
	

	
