<%@ taglib
	uri="/WEB-INF/dbforms.tld"
	prefix="db"%>
<html>
	<head>
		<db:base />
	</head>
	<body>
		<db:dbform
			multipart="false"
			autoUpdate="true"
			followUp="/tests/testBOOKSSingleAutoUpdate.jsp"
			maxRows="1"
			tableName="BOOK">
			<db:header>
				<db:errors />
				<table>
					</db:header>
					<db:body allowNew="true">
						<tr>
							<td>
								<db:dataLabel fieldName="BOOK_ID" />
								&nbsp;
							</td>
							<td>
								<db:textField fieldName="ISBN" />
								&nbsp;
							</td>
							<td>
								<db:textField fieldName="AUTHOR_ID" />
								&nbsp;
							</td>
							<td>
								<db:textField fieldName="TITLE" />
								&nbsp;
							</td>
						</tr>
					</db:body>
					<db:footer>
						<tr>
							<td
								colspan="4"
								align="center">
								<db:updateButton caption="update" />
							</td>
						</tr>
						<tr>
							<td>
								<db:navFirstButton caption="first" />
								<db:navPrevButton caption="previous" />
								<db:navNextButton caption="next" />
								<db:navLastButton caption="last" />
							</td>
						</tr>
				</table>
				</db:footer>
		</db:dbform>

		<%@ include file="httpSnooper.jsp"%>

	</body>
</html>























