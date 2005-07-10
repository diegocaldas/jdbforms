<% 
String version = getServletContext().getMajorVersion() + "." + getServletContext().getMinorVersion(); 
String redirectURL = "testGetDBConnection." + version + ".jsp";
response.sendRedirect(redirectURL);
%>
