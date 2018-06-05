<%@page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
	<head>
	    <db:base />
	</head>
  <body>
  	<br/>Some chinese chars   : &#25152;&#26377;&#32593;&#31449;
  	<br/>Some japanese chars  : &#12454;&#12455;&#12502;&#20840;&#20307;&#12363;&#12425;&#26908;&#32034;
  	<br/>Some portuguese chars: Pesquisar p&aacute;ginas em portugu&ecirc;s &ccedil;
  	<br/>
       <db:dbform autoUpdate="false"
                  maxRows="1"
       >
          <db:header>
				<%
				   String s    = request.getParameter("f1");
				   if (s == null)
				      s = "";
				   String enc  = org.apache.commons.lang.StringEscapeUtils.escapeHtml(s);
				   String uenc = org.apache.commons.lang.StringEscapeUtils.unescapeHtml(enc);
				%>
				<br/><db:textField fieldName="f1" nullFieldValue="" />
				<br/>Plain = <%=s%>
				<br/>Encoded = <%= enc %>
				<br/>Decoded = <%= uenc %>
				<br/><db:navReloadButton />
				<br/>Look the page source to see exact data without browser rendering
          </db:header>
          <db:body>
          </db:body>
          <db:footer>
          </db:footer>
       </db:dbform>


	<%@ include file="httpSnooper.jsp" %> 
  </body>
</html>