<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<% int i=0; %>
<html xmlns:db="http://www.wap-force.com/dbforms">
<head>
<db:base/>
<title>List --- file: AUTHOR_list.jsp</title>
<link href="dbforms.css" rel="stylesheet"/>
</head>
<body class="clsPageBody">
<table align="center" border="0" width="100%" cellspacing="0" cellpadding="1" class="clsMainMenuTable">
<tr>
<td>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="clsMainMenuTable">
<tr class="clsMainMenuTableRow">
<td>
<span class="clsMainMenu">AUTHOR</span>
</td>
<td class="clsMainMenu" align="right">
<a class="clsMainMenu" href="AUTHOR_list.jsp">[List]</a>
<a class="clsMainMenu" href="menu.jsp">[Menu]</a>
<a class="clsMainMenu" href="logout.jsp">[Log out]</a>
</td>
</tr>
</table>
</td>
</tr>
</table>
<db:dbform multipart="false" autoUpdate="false" followUp="/tests/testAUTHOR_list.jsp" maxRows="*" tableName="AUTHOR">
<db:header>
<db:errors/>  <table align="center" >
		
<tr class="clsHeaderDataTableRow"><td class="clsHeaderDataTableCell">AUTHOR_ID</td><td class="clsHeaderDataTableCell">NAME</td><td class="clsHeaderDataTableCell">ORGANISATION</td></tr>
</db:header>
<db:body allowNew="false"><tr class="<%= (i++%2==0) ? "clsOddDataTableRow" : "clsEvenDataTableRow" %>"><td><a href="<db:linkURL href="/tests/testAUTHOR_single.jsp" tableName="AUTHOR" position="<%= position_AUTHOR %>"/>" ><%= currentRow_AUTHOR.get("AUTHOR_ID") %></a>&nbsp;</td>
<td><%= currentRow_AUTHOR.get("NAME") %>&nbsp;</td>
<td><%= currentRow_AUTHOR.get("ORGANISATION") %>&nbsp;</td>
</tr>
</db:body>
<db:footer></table>     
<center>
<hr width="400"/>
<db:navNewButton styleClass="clsButtonStyle" followUp="/tests/new/AUTHOR_single.jsp" caption="Insert new ..."/>
</center>
</db:footer>
</db:dbform>
</body>
</html>    
	    
	    
