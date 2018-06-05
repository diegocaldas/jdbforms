

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
        <db:dbform autoUpdate="false" followUp="/customers_pets.jsp" maxRows="1" tableName="customer" multipart="true">
            <db:header>
            
                <center>customer navigation:</center>
               
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
       
            
            </db:header>
            <db:errors/>
            <db:body allowNew="false">
                <table width="400" align="center" border="0">
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
                </table>
                <br/>











			<%int i=0; %>
			
        <db:dbform autoUpdate="false" followUp="/customers_pets.jsp" maxRows="*" tableName="pets" parentField="id" childField="customer" multipart="true" >
            <db:header>
          <table align="center" >
        
                <tr valign="top" bgcolor="#CCCC00">
                    <td/>
                    <td>name</td>
                    <td>portrait_pic</td>
                    <td>story</td>
                </tr>
                <db:errors/>
            </db:header>
            <db:body allowNew="false">
            <tr bgcolor="#<%= (i++%2==0) ? "FFFFCC" : "FFFF99" %>">
          
                <td>
                    <db:associatedRadio name="radio_pets"/>
                </td>
                <td>
                    <db:textField size="30" fieldName="name"/>
                </td>
                <td><a href="<db:blobURL fieldName="portrait_pic" />" target="_blank" >[view]</a><br/>
                    <db:file fieldName="portrait_pic"/>
                </td>
                <td><a href="<db:blobURL fieldName="story" />" target="_blank" >[view]</a><br/>
                    <db:file fieldName="story"/>
                </td>
           </tr>
           
            </db:body>
            <db:footer>
          </table>
        
                <table border="0" align="center">
                    <tr bgcolor="#CCCC00">
                        <td>
                            <db:updateButton style="width:55" associatedRadio="radio_pets" caption="update"/>
                        </td>
                        <td>
                            <db:deleteButton style="width:55" associatedRadio="radio_pets" caption="delete"/>
                        </td>
                    </tr>
                </table>
                <table width="400" align="center" border="0">
                    <tr bgcolor="#CCCC00">
                        <td colspan="2">Input new data</td>
                    </tr>
                    <tr bgcolor="#FFFFCC">
                        <td>name</td>
                        <td>
                            <db:textField size="30" fieldName="name"/>
                        </td>
                    </tr>
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
                    <tr bgcolor="#CCCC00">
                        <td colspan="2">
                            <center>
                                <db:insertButton caption="Create new pets"/>
                            </center>
                        </td>
                    </tr>
                </table>
            </db:footer>
        </db:dbform>


















            </db:body>
            <db:footer>

            </db:footer>
        </db:dbform>
    </body>
</html>

	

