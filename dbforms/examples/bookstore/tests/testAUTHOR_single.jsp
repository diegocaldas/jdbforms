<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html xmlns:db="http://www.wap-force.com/dbforms">
<head>
<db:base/>
<title>Single --- file: AUTHOR_single.jsp</title>
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
<db:dbform multipart="false" autoUpdate="false" followUp="/tests/testAUTHOR_single.jsp" maxRows="1" tableName="AUTHOR">
<db:header/>
<db:errors/>
<db:body>
<table width="400" align="center" border="0">
<tr class="clsOddDataTableRow">
<td style="font-weight: bold" align="left">NAME</td>
<td align="left">
<db:textField styleClass="clsInputStyle" size="" fieldName="NAME"/>
</td>
</tr>
<tr class="clsEvenDataTableRow">
<td style="font-weight: bold" align="left">ORGANISATION</td>
<td align="left">
<db:textField styleClass="clsInputStyle" size="" fieldName="ORGANISATION"/>
</td>
</tr>
</table>
<br/>
<center>
<db:insertButton showAlways="false" styleClass="clsButtonStyle" caption="Commit data into AUTHOR"/>
</center>
</db:body>
<db:footer>
<table border="0" align="center">
<tr>
<td align="right">
<db:navFirstButton styleClass="clsButtonStyle" style="width:90" caption="&lt;&lt; First"/>
</td>
<td align="center">
<db:navPrevButton styleClass="clsButtonStyle" style="width:90" caption="&lt; Previous"/>
</td>
<td align="center">
<db:navNextButton styleClass="clsButtonStyle" style="width:90" caption="Next &gt;"/>
</td>
<td align="left">
<db:navLastButton styleClass="clsButtonStyle" style="width:90" caption="Last &gt;&gt;"/>
</td>
</tr>
</table>
<table border="0" align="center">
<tr valign="middle">
<td colspan="3">
<hr/>
</td>
</tr>
<tr align="center">
<td align="center">
<db:updateButton styleClass="clsButtonStyle" style="width:90" caption="Update"/>
</td>
<td align="center">
<db:deleteButton styleClass="clsButtonStyle" style="width:90" caption="Delete"/>
</td>
<td align="center">
<db:navNewButton styleClass="clsButtonStyle" style="width:90" caption="Insert new"/>
</td>
</tr>
</table>
</db:footer>
</db:dbform>
</body>
</html>    
	    
	    
