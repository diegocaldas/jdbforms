<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html xmlns:db="http://www.wap-force.com/dbforms">
	<head>
		<db:base/>
	</head>
	<body>
		<db:dbform 
			multipart="false" 
			autoUpdate="false" 
			followUp="/tests/testBOOKSSingleXML.jsp" 
			maxRows="1" tableName="XMLBOOKS">
			<db:header>
				<table>
			</db:header>
			<db:body>
				<tr>
					<td><%=currentRow_XMLBOOKS.get("ISBN") %>&nbsp;</td>
					<td><%=currentRow_XMLBOOKS.get("AUTHOR_ID") %>&nbsp;</td>
					<td><%=currentRow_XMLBOOKS.get("TITLE") %>&nbsp;</td>
				</tr>
			</db:body>
			<db:footer>
				</table>
				<table>
					<tr>
						<td><db:navFirstButton caption="first"/></td>
						<td><db:navPrevButton  caption="previous"/></td>
						<td><db:navNextButton  caption="next"/></td>
						<td><db:navLastButton  caption="last"/></td>
					</tr>
				</table>
			</db:footer>
		</db:dbform>

<%@ include file="httpSnooper.jsp" %>

	</body>
</html>    

