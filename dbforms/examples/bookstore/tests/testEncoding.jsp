<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	</head>
  <body>
  	Some chinese chars: &aelig;&#137;&#128;&aelig;&#156;&#137;&ccedil;&frac12;&#145;&ccedil;&laquo;&#153;<br/>
	<form method="post">
		<%
		   String s    = request.getParameter("f1");
		   if (s == null)
		      s = "";
		   String enc  = org.apache.commons.lang.StringEscapeUtils.escapeHtml(s);
		   String uenc = org.apache.commons.lang.StringEscapeUtils.unescapeHtml(enc);
		%>
		<br/><input type="text" name="f1" value="<%=s%>">
		<br/>Plain = <%=s%>
		<br/>Encoded = <%= enc %>
		<br/>Decoded = <%= uenc %>
		<br/><input type="submit">
		<br/>Look the page source to see exact data without browser rendering
	</form>
	<%@ include file="httpSnooper.jsp" %> 
  </body>
</html>