<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db"%>
<html>
<head>
<db:base />
</head>
<body>
<db:presetFormValues
	className="interceptors.HowtoUsePresetFiltersInterceptor">
	<db:property name="AUTHOR_ID" value="1" />
</db:presetFormValues>
<db:dbform autoUpdate="false"
	followUp="/howto/howtoUsePresetFilters.jsp" tableName="BOOKSPERAUTHOR">
	<db:header>
		<h1 align="center">Book with preset filter</h1>
		<table class="fixed" align="center">
			</db:header>
			<db:body>
				<tr>
					<td><db:label fieldName="BOOK_ID" /></td>
					<td><db:label fieldName="ISBN" /></td>
					<td><db:label fieldName="TITLE" /></td>
					<td><db:label fieldName="AUTHOR_ID" /></td>
					<td><db:label fieldName="NAME" /></td>
				</tr>
			</db:body>
			<db:footer>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td><db:search fieldName="AUTHOR_ID" /></td>
					<td>&nbsp;</td>
				</tr>
		</table>
		</db:footer>
</db:dbform>


</body>
</html>





