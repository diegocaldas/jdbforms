


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
        <db:dbform autoUpdate="false" followUp="/pets_list_editable_rb.jsp" maxRows="*" tableName="pets" multipart="true">
            <db:header>
          <table align="center" >
        
                <tr bgcolor="#CCCC00">
                    <td/>
                    <td>name</td>
                    <td>portrait_pic</td>
                    <td>story</td>
                </tr>
                <db:errors/>
            </db:header>
            <db:body allowNew="false">
            <tr bgcolor="#<%= (i++%2==0) ? "FFFFCC" : "FFFF00" %>">
          
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
           
                <center>
                    <db:insertButton caption="Create new pets"/>
                </center>
            </db:body>
            <db:footer>
          </table>
        
                <table border="0" align="center">
                    <tr>
                        <td>
                            <db:updateButton style="width:55" associatedRadio="radio_pets" caption="update"/>
                        </td>
                        <td>
                            <db:deleteButton style="width:55" associatedRadio="radio_pets" caption="delete"/>
                        </td>
                        <td>
                            <db:navNewButton style="width:40" followUp="/pets_single.jsp" caption="New"/>
                        </td>
                    </tr>
                </table>
            </db:footer>
        </db:dbform>
    </body>
</html>

	

