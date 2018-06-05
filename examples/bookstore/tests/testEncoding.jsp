<%@page contentType="text/html;charset=UTF-8"%>
<html>
	<head>
	</head>
  <body>
  	<br/>Some chinese chars   : &#25152;&#26377;&#32593;&#31449;
  	<br/>Some japanese chars  : &#12454;&#12455;&#12502;&#20840;&#20307;&#12363;&#12425;&#26908;&#32034;
  	<br/>Some portuguese chars: Pesquisar p&aacute;ginas em portugu&ecirc;s &ccedil;
  	<br/>
	<form method="post">
		<%
		   String s    = org.dbforms.util.ParseUtil.getParameter(request, "f1");
		   if (s == null)
		      s = "";
		   String enc  = org.apache.commons.lang.StringEscapeUtils.escapeHtml(s);
		   String uenc = org.apache.commons.lang.StringEscapeUtils.unescapeHtml(enc);
		%>
		<br/><input type="text" name="f1" value="<%=s%>" />
		<br/>Plain = <%=s%>
		<br/>Encoded = <%= enc %>
		<br/>Decoded = <%= uenc %>
		<br/><input type="submit" />
		<br/>Look the page source to see exact data without browser rendering
	</form>
	<%@ include file="httpSnooper.jsp" %> 
  </body>
</html>