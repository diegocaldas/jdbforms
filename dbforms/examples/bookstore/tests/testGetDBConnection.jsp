<% 
String version = getServletContext().getMajorVersion() + "." + getServletContext().getMinorVersion(); 
this.log(version);
System.out.print(version);
String redirectURL = "testGetDBConnection." + version + ".jsp";
response.sendRedirect(redirectURL);
%>
