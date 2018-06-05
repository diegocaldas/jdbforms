<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
	<head>
		<db:base/>
	</head>
	<body>
		<db:dbform 
			multipart="false" 
			autoUpdate="false" 
			followUp="/tests/testBOOKSSingleXML2.jsp" 
			maxRows="2" 
			tableName="XMLBOOKS">
			<db:header>
				<db:errors/>
				<table>
			</db:header>
			<db:body>
				<tr>
					<td><db:label fieldName="BOOK_ID"/>&nbsp;</td>
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
					</tr>
				</table>
			</db:footer>
		</db:dbform>

<%@ include file="httpSnooper.jsp" %>

	</body>
</html>    

