<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>

<html xmlns:db="http://www.wap-force.com/dbforms">
<head>
<db:base/>
<title>Main Menu Form</title>
</head>
<body>
	<db:dbform followUp="testMenu.jsp">
		<db:gotoButton 
			caption="test goto menu page"
			destination="/tests/testMenuPage1.jsp" 
		/>
		<db:gotoButton 
			caption="test goto testPositionAuthorEdit.jsp"
			destination="/tests/testPositionAuthorEdit.jsp" 
		/>
		<db:gotoButton 
			caption="test goto testPositionAuthorEdit.jsp with AUTHORVIEW"
			destination="/tests/testPositionAuthorEdit.jsp"
			destTable="AUTHOR_VIEW" 
		/>
		<db:navNewButton 
			followUp="/tests/testMenuPage1.jsp" 
			destTable="AUTHOR" 
			caption="testMenuPage1"
		/>
		<db:navNewButton 
			followUp="/tests/testMenuPage2.jsp" 
			destTable="AUTHOR"
			caption="testMenuPage2"
		/>
	</db:dbform>

<%@ include file="httpSnooper.jsp" %> 

</body>
</html>
