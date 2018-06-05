<% 
String version = getServletContext().getMajorVersion() + "." + getServletContext().getMinorVersion(); 
String redirectURL = "testBOOKSListDynaBug." + version + ".jsp";
response.sendRedirect(redirectURL);
%>
