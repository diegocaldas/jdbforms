<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html xmlns:db="http://www.wap-force.com/dbforms">
	<head>
		<db:base/>
	</head>
	<body>
		<db:dbform 
			autoUpdate="false" 
			followUp="/tests/testBOOKSSingle.jsp" 
			maxRows="1" tableName="BOOK">
			<db:header>
				<table>
			</db:header>
			<db:body>
				<tr>
					<td><db:label fieldName="BOOK_ID"/></a>&nbsp;</td>
					<td><db:label fieldName="ISBN"/>&nbsp;</td>
					<td><db:label fieldName="AUTHOR_ID"/>&nbsp;</td>
					<td><db:label fieldName="TITLE"/>&nbsp;</td>
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
						<td><db:navReloadButton  caption="reload"/></td>
					</tr>
				</table>
			</db:footer>
		</db:dbform>
	</body>
</html>    

