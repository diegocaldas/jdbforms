


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
                                <h1>priority</h1>
                            </td>
                            <td align="right">
                                <a href="priority_list.jsp">[List]</a>
                                <a href="menu.jsp">[Menu]</a>
                                <a href="logout.jsp">[Log out]</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <db:dbform autoUpdate="false" followUp="/priority_list_editable_rb.jsp" maxRows="*" tableName="priority">
            <db:header>
          <table align="center" >
        
                <tr bgcolor="#CCCC00">
                    <td/>
                    <td>id<br/>
                        <db:sort fieldName="id"/>
                    </td>
                    <td>shortname</td>
                    <td>description</td>
                </tr>
                <db:errors/>
            </db:header>
            <db:body allowNew="false">
            <tr bgcolor="#<%= (i++%2==0) ? "FFFFCC" : "FFFF00" %>">
          
                <td>
                    <db:associatedRadio name="radio_priority"/>
                </td>
                <td>
                    <db:textField size="10" fieldName="id"/>
                </td>
                <td>
                    <db:textField size="30" fieldName="shortname"/>
                </td>
                <td>
                    <db:textArea wrap="virtual" rows="3" cols="40" fieldName="description"/>
                </td>
           </tr>
           
                <center>
                    <db:insertButton caption="Create new priority"/>
                </center>
            </db:body>
            <db:footer>
          </table>
        
                <table border="0" align="center">
                    <tr>
                        <td>
                            <db:updateButton style="width:55" associatedRadio="radio_priority" caption="update"/>
                        </td>
                        <td>
                            <db:deleteButton style="width:55" associatedRadio="radio_priority" caption="delete"/>
                        </td>
                        <td>
                            <db:navNewButton style="width:40" followUp="/priority_single.jsp" caption="New"/>
                        </td>
                    </tr>
                </table>
            </db:footer>
        </db:dbform>
    </body>
</html>

	

