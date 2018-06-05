


<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<%int i=0; %>
<html xmlns:db="http://www.wap-force.net/dbforms">
    <head>
        <db:base/>
        <link href="dbforms.css" rel="stylesheet"/>
    </head>
    <body bgcolor="#99CCFF">
        <table align="center" border="0" width="100%" cellspacing="0" cellpadding="1" bgcolor="#999900">
            <tr>
                <td>
                    <table border="0" width="100%" cellspacing="0" cellpadding="3" bgcolor="#999900">
                        <tr bgcolor="#CCCC00">
                            <td>
                                <h1>service</h1>
                            </td>
                            <td align="right">
                                <a href="service_list.jsp">[List]</a>
                                <a href="menu.jsp">[Menu]</a>
                                <a href="logout.jsp">[Log out]</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <db:dbform autoUpdate="false" followUp="/service_list_editable.jsp" maxRows="*" tableName="service">
            <db:header>
          <table align="center" >
        
                <tr bgcolor="#CCCC00">
                    <td>id<br/>
                        <db:sort fieldName="id"/>
                    </td>
                    <td>name</td>
                    <td>description</td>
                    <td colspan="2">action</td>
                </tr>
            </db:header>
            <db:errors/>
            <db:body>
            <tr bgcolor="#<%= (i++%2==0) ? "FFFFCC" : "FFFF99" %>">
          
                <td>
                    <db:textField size="11" fieldName="id"/>
                </td>
                <td>
                    <db:textField size="30" fieldName="name"/>
                </td>
                <td>
                    <db:textArea wrap="virtual" rows="3" cols="40" fieldName="description"/>
                </td>
                <td>
                    <db:updateButton style="width:55" caption="update"/>
                </td>
                <td>
                    <db:deleteButton style="width:55" caption="delete"/>
                </td>
           </tr>
           
                <center>
                    <db:insertButton caption="Create new service"/>
                </center>
            </db:body>
            <db:footer>
          </table>
        
                <table border="0" align="center">
                    <tr>
                        <td>
                            <db:navNewButton style="width:40" followUp="/service_single.jsp" caption="New"/>
                        </td>
                    </tr>
                </table>
            </db:footer>
        </db:dbform>
    </body>
</html>

	
	

	
