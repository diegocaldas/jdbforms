<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %><html xmlns:db="http://www.wap-force.com/dbforms">
<head>
<db:base/>
<title>Main Menu Form</title>
<link href="dbforms.css" rel="stylesheet"/>
</head>
<body class="clsPageBody">
<db:errors/>
<table cellspacing="5" cellpadding="0" height="100%" width="100%">
<tr>
<td/>
</tr>
<tr>
<td>
<center>
<db:dbform followUp="/menu.jsp">
<center>
<h1>-= Main  Menu =-</h1>
<hr/>
<table cellpadding="0" cellspacing="0">
	    <tr valign="middle">

<td valign="middle">
<b>Nav New Test : 
</b>
</td>
<td>&nbsp;&nbsp;&nbsp;</td>
<td align="center">
<db:navNewButton followUp="/test_pages/AUTHOR_single.jsp" destTable="AUTHOR" styleClass="clsButtonStyle" style="width:90" caption="Insert Author"/>
</td>
<td align="center">
<db:navNewButton  followUp="/test_pages/AUTHOR_single_1.jsp" destTable="AUTHOR" styleClass="clsButtonStyle" style="width:90" caption="Insert Author_1"/>
</td>

</tr>
 
</table>
</body>
</html>
