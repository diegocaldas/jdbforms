

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
                                <h1>customer</h1>
                            </td>
                            <td align="right">
                                <a href="customer_list.jsp">[List]</a>
                                <a href="menu.jsp">[Menu]</a>
                                <a href="logout.jsp">[Log out]</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <db:dbform autoUpdate="false" followUp="/customer_list_and_single.jsp" maxRows="*" tableName="customer">
            <db:header>
          <table align="center" >
        
                <tr valign="top" bgcolor="#CCCC00">
                    <td/>
                    <td>id<br/>
                        <db:sort fieldName="id"/>
                    </td>
                    <td>firstname</td>
                    <td>lastname</td>
                    <td>address</td>
                    <td>pcode</td>
                    <td>city</td>
                </tr>
                <db:errors/>
            </db:header>
            <db:body allowNew="false">
            <tr bgcolor="#<%= (i++%2==0) ? "FFFFCC" : "FFFF99" %>">
          
                <td>
                    <db:associatedRadio name="radio_customer"/>
                </td>
                <td>
                    <db:textField size="11" fieldName="id"/>
                </td>
                <td>
                    <db:textField size="50" fieldName="firstname"/>
                </td>
                <td>
                    <db:textField size="50" fieldName="lastname"/>
                </td>
                <td>
                    <db:textField size="30" fieldName="address"/>
                </td>
                <td>
                    <db:textField size="10" fieldName="pcode"/>
                </td>
                <td>
                    <db:textField size="40" fieldName="city"/>
                </td>
           </tr>
           
                <center>
                    <db:insertButton caption="Create new customer"/>
                </center>
            </db:body>
            <db:footer>
          </table>
        
                <table border="0" align="center">
                    <tr bgcolor="#CCCC00">
                        <td>
                            <db:updateButton style="width:55" associatedRadio="radio_customer" caption="update"/>
                        </td>
                        <td>
                            <db:deleteButton style="width:55" associatedRadio="radio_customer" caption="delete"/>
                        </td>
                    </tr>
                </table>
                <table width="400" align="center" border="0">
                    <tr bgcolor="#CCCC00">
                        <td colspan="2">Input new data</td>
                    </tr>
                    <tr bgcolor="#FFFF99">
                        <td>id</td>
                        <td>
                            <db:textField size="11" fieldName="id"/>
                        </td>
                    </tr>
                    <tr bgcolor="#FFFFCC">
                        <td>firstname</td>
                        <td>
                            <db:textField size="50" fieldName="firstname"/>
                        </td>
                    </tr>
                    <tr bgcolor="#FFFF99">
                        <td>lastname</td>
                        <td>
                            <db:textField size="50" fieldName="lastname"/>
                        </td>
                    </tr>
                    <tr bgcolor="#FFFFCC">
                        <td>address</td>
                        <td>
                            <db:textField size="30" fieldName="address"/>
                        </td>
                    </tr>
                    <tr bgcolor="#FFFF99">
                        <td>pcode</td>
                        <td>
                            <db:textField size="10" fieldName="pcode"/>
                        </td>
                    </tr>
                    <tr bgcolor="#FFFFCC">
                        <td>city</td>
                        <td>
                            <db:textField size="40" fieldName="city"/>
                        </td>
                    </tr>
                    <tr bgcolor="#CCCC00">
                        <td colspan="2">
                            <center>
                                <db:insertButton caption="Create new customer"/>
                            </center>
                        </td>
                    </tr>
                </table>
            </db:footer>
        </db:dbform>
    </body>
</html>

	

