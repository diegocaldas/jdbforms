<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>

<%
session.invalidate();
%>

<html>
<head>
<db:base/>
<link rel="stylesheet" href="dbforms.css">
</head>
<body>

	
	  <db:style template="center" part="begin"/>
     	 <db:style template="sourceforge" part="begin" paramList="width=270"/>
     	
     		<center>
     	 		<br><b>You are logged off now.</b><br><br>
     	 		<a href="admin_menu.jsp">LOGIN!</a><br>
     	 		<a href="bugs_list.jsp">HUNT THE BUGS!</a><br>
     	 	</center>
     	 	
	  	 <db:style template="sourceforge" part="end" />
	  <db:style template="center" part="end"/>
  

</body>
</html>
