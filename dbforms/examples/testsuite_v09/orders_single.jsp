<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>

<%
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
%>

<html xmlns:db="http://www.wap-force.com/dbforms">
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
                                <h1>orders</h1>
                            </td>
                            <td align="right">
                                <a href="orders_list.jsp">[List]</a>
                                <a href="menu.jsp">[Menu]</a>
                                <a href="logout.jsp">[Log out]</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <db:dbform autoUpdate="false" followUp="/orders_single.jsp" maxRows="1" tableName="orders">
            <db:header/>
            <db:errors/>
            <db:body>
                <table width="400" align="center" border="0">
                    <tr bgcolor="#FFFFCC">
                        <td>customer_id</td>
                        <td>
                            <db:textField size="11" fieldName="customer_id"/>
                        </td>
                    </tr>
                    <tr bgcolor="#FFFF99">
                        <td>service_id</td>
                        <td>
                            <db:textField size="11" fieldName="service_id"/>
                        </td>
                    </tr>
                    <tr bgcolor="#FFFFCC">
                        <td>orderdate</td>
                        <td>
                            <db:dateLabel fieldName="orderdate" format="<%= sdf %>" />
                            <db:dateField size="10" fieldName="orderdate" />
                        </td>
                    </tr>
                </table>
                <br/>
                <center>
                    <db:insertButton caption="Create new orders"/>
                </center>
            </db:body>
            <db:footer>
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
                            <db:updateButton style="width:90" caption="Update"/>
                        </td>
                        <td>
                            <db:deleteButton style="width:90" caption="Delete"/>
                        </td>
                        <td>
                            <db:navNewButton style="width:40" caption="New"/>
                        </td>
                    </tr>
                </table>
            </db:footer>
        </db:dbform>
    </body>
</html>

	

