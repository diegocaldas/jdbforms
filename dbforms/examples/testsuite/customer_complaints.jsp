

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
        <db:dbform autoUpdate="false" followUp="/customer_complaints.jsp" maxRows="1" tableName="customer">
            <db:header/>
            <db:errors/>
            <db:body allowNew="false">
                <table width="400" align="center" border="0">
                    <tr bgcolor="#FFFF99">
                        <td>id</td>
                        <td>
                            <db:label fieldName="id"/>
                        </td>
                    </tr>
                    <tr bgcolor="#FFFFCC">
                        <td>firstname</td>
                        <td>
                            <db:label fieldName="firstname"/>
                        </td>
                    </tr>
                    <tr bgcolor="#FFFF99">
                        <td>lastname</td>
                        <td>
                            <db:label fieldName="lastname"/>
                        </td>
                    </tr>
                    <tr bgcolor="#FFFFCC">
                        <td>address</td>
                        <td>
                            <db:label fieldName="address"/>
                        </td>
                    </tr>
                    <tr bgcolor="#FFFF99">
                        <td>pcode</td>
                        <td>
                            <db:label fieldName="pcode"/>
                        </td>
                    </tr>
                    <tr bgcolor="#FFFFCC">
                        <td>city</td>
                        <td>
                            <db:label fieldName="city"/>
                        </td>
                    </tr>
                </table>
                <br/>
                
                
                	<!-- begin of subform -->                
				<%
				int i=0;
				%>

					<db:dbform autoUpdate="false" parentField="id" childField="customer_id" followUp="/customer_complaints.jsp" maxRows="*" tableName="complaint">
							<db:header>
						<table align="center" >

									<tr valign="top" bgcolor="#CCCC00">
											<td/>
											<td>complaint_id<br/>
													<db:sort fieldName="complaint_id"/>
											</td>

											<td>usermessage</td>
											<td>incomingdate</td>
											<td>priority</td>
									</tr>
									<db:errors/>
							</db:header>
							<db:body allowNew="false">
							<tr bgcolor="#<%= (i++%2==0) ? "FFFFCC" : "FFFF99" %>">

									<td>
											<db:associatedRadio name="radio_complaint"/>
									</td>
									<td>
											<db:textField size="11" fieldName="complaint_id"/>
									</td>

									<td>
											<db:textArea wrap="virtual" rows="3" cols="40" fieldName="usermessage"/>
									</td>
									<td>
											<db:dateField size="20" fieldName="incomingdate"/>
									</td>
									<td>
											<db:textField size="11" fieldName="priority"/>
									</td>
						 </tr>

							</db:body>
							<db:footer>
						</table>

									<table border="0" align="center">
											<tr bgcolor="#CCCC00">
													<td>
															<db:updateButton style="width:55" associatedRadio="radio_complaint" caption="update"/>
													</td>
													<td>
															<db:deleteButton style="width:55" associatedRadio="radio_complaint" caption="delete"/>
													</td>
											</tr>
									</table>
									<table width="400" align="center" border="0">
											<tr bgcolor="#CCCC00">
													<td colspan="2">Input new data</td>
											</tr>


											<tr bgcolor="#FFFFCC">
													<td>usermessage</td>
													<td>
															<db:textArea wrap="virtual" rows="3" cols="40" fieldName="usermessage"/>
													</td>
											</tr>
											<tr bgcolor="#FFFF99">
													<td>incomingdate</td>
													<td>
															<db:dateField size="20" fieldName="incomingdate"/>
													</td>
											</tr>
											<tr bgcolor="#FFFFCC">
													<td>priority</td>
													<td>
															<db:textField size="11" fieldName="priority"/>
													</td>
											</tr>
											<tr bgcolor="#CCCC00">
													<td colspan="2">
															<center>
																	<db:insertButton caption="Create new complaint"/>
															</center>
													</td>
											</tr>
									</table>
							</db:footer>
					</db:dbform>              

			<!-- end of subform -->                
                
                
                
                
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
            </db:footer>
        </db:dbform>
    </body>
</html>

	

