


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
        <db:dbform autoUpdate="false" followUp="/customer_list_editable_filter.jsp" maxRows="2" tableName="customer" filter="id>10">
            <db:header>
          <table align="center" >
        
                <tr bgcolor="#CCCC00">
                    <td>id<br/>
                    <db:sort fieldName="id"/>
                    </td>
                    <td>firstname</td>
                    <td>lastname</td>
                    <td>address</td>
                    <td>pcode</td>
                    <td>city</td>
                    <td colspan="2">action</td>
                </tr>
            </db:header>
            <db:errors/>
            <db:body>
            <tr bgcolor="#<%= (i++%2==0) ? "FFFFCC" : "FFFF99" %>">
          
                <td>
                    <db:textField size="5" fieldName="id"/>
                </td>
                <td>
                    <db:textField size="20" fieldName="firstname"/>
                </td>
                <td>
                    <db:textField size="20" fieldName="lastname"/>
                </td>
                <td>
                    <db:textField size="23" fieldName="address"/>
                </td>
                <td>
                    <db:textField size="6" fieldName="pcode"/>
                </td>
                <td>
                    <db:textField size="20" fieldName="city"/>
                </td>
                <td>
                    <db:updateButton style="width:55" caption="update"/>
                </td>
                <td>
                    <db:deleteButton style="width:55" caption="delete"/>
                </td>
           </tr>
           
                <center>
                    <db:insertButton caption="Create new customer"/>
                </center>
            </db:body>
            <db:footer>
          </table>
        
                <table border="0" align="center">
                    <tr>
                        <td>
                            <db:navFirstButton style="width:90" caption="&lt;&lt; First"/>
                        </td>
                        <td>
                            <db:navPrevButton style="width:90" caption="&lt; Previous"/>
                        </td>
                        <td>
                            <db:navNextButton style="width:90" caption="Next &gt;"/>
                        </td>
                        <td>
                            <db:navLastButton style="width:90" caption="Last &gt;&gt;"/>
                        </td>
                    </tr>
                </table>
                <table border="0" align="center">
                    <tr>
                        <td>
                            <db:navNewButton style="width:40" caption="New"/>
                        </td>
                    </tr>
                </table>        
        
            </db:footer>
        </db:dbform>
    </body>
</html>

	

