<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html xmlns:db="http://www.wap-force.com/dbforms">
	<head>
		<db:base/>
	</head>
	<body>
		<db:dbform 
			multipart="false" 
			autoUpdate="false" 
			followUp="/tests/testBOOKSSingle.jsp" 
			maxRows="1" tableName="BOOK">
			<db:header>
				<table>
			</db:header>
			<db:body>
				<tr>
					<td><%=currentRow_BOOK.get("BOOK_ID") %></a>&nbsp;</td>
					<td><%=currentRow_BOOK.get("ISBN") %>&nbsp;</td>
					<td><%=currentRow_BOOK.get("AUTHOR_ID") %>&nbsp;</td>
					<td><%=currentRow_BOOK.get("TITLE") %>&nbsp;</td>
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
	</body>
</html>    

