<% 
String version = getServletContext().getMajorVersion() + "." + getServletContext().getMinorVersion(); 
/*
this.log(version);
System.out.print(version);
*/
version = "2.3";
String redirectURL = "testGetDBConnection."  + version + ".jsp";
response.sendRedirect(redirectURL);
%>
