<html>
	<%@ taglib uri="/WEB-INF/dbForms.tld" prefix="db" %>
	<%int i=0; %>
	<head>
		<db:base/>
	</head>
	<body>
		<db:errors/>
		<db:dbform tableName="BOOK" 
			maxRows="*" 
			followUp="/tests/testSEARCHRESULT.jsp" 
			autoUpdate="false"
		>
			<db:header>
				<h1>Search Result Page</h1>
				<table borader="5" width="60%" align="center">
			</db:header>
			<db:body>
				<tr bgcolor="#<%= (i++%2==0) ? "FFFFCC" : "FFFF99" %>">
					<td><db:label fieldName="TITLE" /></td>
					<td><db:label fieldName="ISBN" /></td>
				</tr>
			</db:body>
			<db:footer>
				</table>
				<p><center>
					<db:navFirstButton caption="First" />
					<db:navPrevButton caption="Previous" />
					<db:navNextButton caption="Next" />
					<db:navLastButton caption="Last" />
				<!-- db:navNewButton caption="nbsp;nbsp;*nbsp;nbsp;" --> 
				</center></p>
			</db:footer>
		</db:dbform>
	</body>
</html>
