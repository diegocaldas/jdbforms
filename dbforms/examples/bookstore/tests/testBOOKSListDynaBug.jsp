<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>

<%@ page import="org.dbforms.taglib.DbFormContext" %>
<%@ page import="org.dbforms.config.ResultSetVector" %>
<%
  String tableName="BOOK";
  DbFormContext form = null;
  ResultSetVector rsv = null;
%>
<html>
	<head>
		<db:base/>
	</head>
	<body>
		<table>
			<tr><td colspan="4">--------------------------------------------------------------------------------</td></tr>
			<tr><td>#  </td><td colspan="3">: &lt;db:label/&gt; tag</td></tr>
			<tr><td>--></td><td colspan="3">: DbFormContext.getCurrentRow()</td></tr>
			<tr><td>==></td><td colspan="3">: DbFormContext.getRsv().getCurrentRow()</td></tr>
			<tr><td>>>></td><td colspan="3">: currentRow_BOOK</td></tr>
			<tr><td>**></td><td colspan="3">: rsv_BOOK</td></tr>
			<tr><td colspan="4">--------------------------------------------------------------------------------</td></tr>
			<db:dbform
			name = "form"
			tableName="BOOK"
			multipart="false"
			autoUpdate="false"
			followUp="/tests/testBOOKSList.jsp"
			maxRows="*">
				<db:header>
					<db:errors/>
					<% form = ((DbFormContext)dbforms.get("form")); %>
				</db:header>
				<db:body allowNew="false">
					<tr>
						<td> #<db:pos/>&nbsp;</td>
						<td> <db:label fieldName="BOOK_ID"/>&nbsp;</td>
						<td> <db:label fieldName="ISBN"/>&nbsp;</td>
						<td> <db:label fieldName="AUTHOR_ID" />&nbsp;</td>
						<td> <db:label fieldName="TITLE"/>&nbsp;</td>
					</tr>
					<tr>
						<td>--><%=form.getPosition() %>&nbsp;</td>
						<td> <%= form.getCurrentRow().get("BOOK_ID") %>&nbsp;</td>
						<td> <%= form.getCurrentRow().get("ISBN") %>&nbsp;</td>
						<td> <%= form.getCurrentRow().get("AUTHOR_ID") %>&nbsp;</td>
						<td> <%= form.getCurrentRow().get("TITLE") %>&nbsp;</td>
					</tr>
					<tr>
						<td>==>&nbsp;</td>
						<td> <%= form.getRsv().getCurrentRow()[0]%>&nbsp;</td>
						<td> <%= form.getRsv().getCurrentRow()[1]%>&nbsp;</td>
						<td> <%= form.getRsv().getCurrentRow()[2]%>&nbsp;</td>
						<td> <%= form.getRsv().getCurrentRow()[3]%>&nbsp;</td>
					</tr>
					<tr>
						<td>>>><%=position_BOOK %>&nbsp;</td>
						<td> <%= currentRow_BOOK.get("BOOK_ID") %>&nbsp;</td>
						<td> <%= currentRow_BOOK.get("ISBN") %>&nbsp;</td>
						<td> <%= currentRow_BOOK.get("AUTHOR_ID") %>&nbsp;</td>
						<td> <%= currentRow_BOOK.get("TITLE") %>&nbsp;</td>
					</tr>
					<tr>
						<td>**>&nbsp;</td>
						<td> <%= rsv_BOOK.getCurrentRow()[0]%>&nbsp;</td>
						<td> <%= rsv_BOOK.getCurrentRow()[1]%>&nbsp;</td>
						<td> <%= rsv_BOOK.getCurrentRow()[2]%>&nbsp;</td>
						<td> <%= rsv_BOOK.getCurrentRow()[3]%>&nbsp;</td>
					</tr>
					<tr><td colspan="4">--------------------------------------------------------------------------------</td></tr>
				</db:body>
				<db:footer>
				</db:footer>
			</db:dbform>
		</table>
		<%@ include file="httpSnooper.jsp" %>
	</body>
</html>























