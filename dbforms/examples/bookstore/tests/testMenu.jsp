<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>

<html xmlns:db="http://www.wap-force.com/dbforms">
<head>
<db:base/>
<title>Main Menu Form</title>
</head>
<body>
	<db:dbform followUp="testMenu.jsp">
		<table>
			<tr>
				<td>
					<db:gotoButton 
						caption="test goto menu page 1"
						destination="/tests/testMenuPage1.jsp" 
					/>
				</td>
				<td>
					<db:gotoButton 
						caption="test goto menu page 2"
						destination="/tests/testMenuPage2.jsp" 
					/>
				</td>
			</tr>

			<tr>
				<td>
					<db:gotoButton 
						caption="test goto testPositionAuthorEdit.jsp"
						destination="/tests/testPositionAuthorEdit.jsp" 
					/>
				</td>
				<td>
					<db:gotoButton 
						caption="test goto testPositionAuthorEdit.jsp with AUTHORVIEW"
						destination="/tests/testPositionAuthorEdit.jsp"
						destTable="AUTHOR_VIEW" 
					/>
				</td>
			</tr>


			<tr>
				<td>
					<db:navNewButton 
						followUp="/tests/testMenuPage1.jsp" 
						destTable="AUTHOR" 
						caption="testMenuPage1"
					/>
				</td>
				<td>
					<db:navNewButton 
						followUp="/tests/testMenuPage2.jsp" 
						destTable="AUTHOR"
						caption="testMenuPage2"
					/>
				</td>
			</tr>

		</table>
	</db:dbform>

<%@ include file="httpSnooper.jsp" %> 

</body>
</html>
