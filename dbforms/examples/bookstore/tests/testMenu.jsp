<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>

<html xmlns:db="http://www.wap-force.com/dbforms">
<head>
<db:base/>
<title>Main Menu Form</title>
<link href="dbforms.css" rel="stylesheet"/>
</head>
<body>
	<db:dbform followUp="testMenu.jsp">
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
</body>
</html>
