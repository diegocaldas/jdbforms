<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
	<head>
	    <db:base />
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	</head>
  <body>
  	Some chinese chars: &#25152;&#26377;&#32593;&#31449;
  	<br/>
       <db:dbform autoUpdate="false"
                  maxRows="1"
       >
          <db:header>
          </db:header>
          <db:body>
          </db:body>
          <db:footer>
				<%
				   String s    = request.getParameter("f1");
				   if (s == null)
				      s = "";
				   String enc  = org.apache.commons.lang.StringEscapeUtils.escapeHtml(s);
				   String uenc = org.apache.commons.lang.StringEscapeUtils.unescapeHtml(enc);
				%>
				<br/><db:textField fieldName="f1" />
				<br/>Plain = <%=s%>
				<br/>Encoded = <%= enc %>
				<br/>Decoded = <%= uenc %>
				<br/><db:navReloadButton />
				<br/>Look the page source to see exact data without browser rendering
          </db:footer>
       </db:dbform>


	<%@ include file="httpSnooper.jsp" %> 
  </body>
</html>