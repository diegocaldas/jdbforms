

<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
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
                                <h1>pets</h1>
                            </td>
                            <td align="right">
                                <a href="pets_list.jsp">[List]</a>
                                <a href="menu.jsp">[Menu]</a>
                                <a href="logout.jsp">[Log out]</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <db:dbform autoUpdate="false" followUp="/pets_single.jsp" maxRows="1" tableName="pets" multipart="true">
            <db:header/>
            <db:errors/>
            <db:body>
                <table width="400" align="center" border="0">
                    <tr bgcolor="#FFFF99">
                        <td>name</td>
                        <td>
                            <db:textField size="30" fieldName="name"/>
                        </td>
                    </tr>
                    
                    <tr bgcolor="#FFFFCC">
                        <td>customer</td>
                        <td>
                          <db:select fieldName="customer">
															<db:tableData 
																name = "fkCust"
																foreignTable = "customer"
																visibleFields = "id,firstname,lastname"
																storeField = "id"
															/>                     
													</db:select>
                    
                    <tr bgcolor="#FFFF99">
                        <td>portrait_pic</td>
                        <td>
                            <db:file fieldName="portrait_pic"/>
                        </td>
                    </tr>
                    <tr bgcolor="#FFFFCC">
                        <td>story</td>
                        <td>
                            <db:file fieldName="story"/>
                        </td>
                    </tr>
                </table>
                <br/>
                <center>
                    <db:insertButton caption="Create new pets"/>
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

	

