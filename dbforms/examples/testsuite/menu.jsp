<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %><html xmlns:db="http://www.wap-force.com/dbforms">
    <head>
        <db:base/>
        <title>Menu</title>
        <link href="dbforms.css" rel="stylesheet"/>
    </head>
    <body bgcolor="#99CCFF">
        <db:errors/>
        <table height="100%" width="100%">
            <tr>
                <td/>
            </tr>
            <tr>
                <td>
                    <center>
                        <db:dbform followUp="/menu.jsp">
                            <center>
                                <h1>M A I N M E N U</h1>
                                
                                <p>Note: 95% of the code for these JSP-pages where <b>automatically generated</b> by XSL transformations on database metadata using the tool <b>DevGui</b>, which is included in DbForms distribution 0.9+. The rest (5%) was done by hand, using the DbForms JSP-Tag-library</p>
                                
                                <hr/>
                                <table cellspacing="20">
                                
                                    
                                                              
                                
	<tr valign="TOP">
                                        <td valign="TOP">
                                            <p>
                                                <db:gotoButton style="width:150" destination="/complaint_list.jsp" caption="complaint (list)"/>
                                                <db:gotoButton style="width:150" destination="/complaint_single.jsp" caption="complaint (single)"/>
                                                <db:gotoButton style="width:150" destination="/complaint_single_ro.jsp" caption="complaint (single ro)"/>
                                                <db:gotoButton style="width:150" destination="/complaint_list_editable.jsp" caption="complaint (list edit)"/>
                                                <db:gotoButton style="width:150" destination="/complaint_list_editable_rb.jsp" caption="complaint (list edit rb)"/>
                                                <db:gotoButton style="width:150" destination="/complaint_list_and_single.jsp" caption="complaint (list and single)"/>
                                            </p>
                                        </td>
                                    </tr>

	<tr valign="TOP">
                                        <td valign="TOP">
                                            <p>
                                                <db:gotoButton style="width:150" destination="/customer_list.jsp" caption="customer (list)"/>
                                                <db:gotoButton style="width:150" destination="/customer_single.jsp" caption="customer (single)"/>
                                                <db:gotoButton style="width:150" destination="/customer_single_ro.jsp" caption="customer (single ro)"/>
                                                <db:gotoButton style="width:150" destination="/customer_list_editable.jsp" caption="customer (list edit)"/>
                                                <db:gotoButton style="width:150" destination="/customer_list_editable_rb.jsp" caption="customer (list edit rb)"/>
                                                <db:gotoButton style="width:150" destination="/customer_list_and_single.jsp" caption="customer (list and single)"/>
                                            </p>
                                        </td>
                                    </tr>

	<tr valign="TOP">
                                        <td valign="TOP">
                                            <p>
                                                <db:gotoButton style="width:150" destination="/orders_list.jsp" caption="orders (list)"/>
                                                <db:gotoButton style="width:150" destination="/orders_single.jsp" caption="orders (single)"/>
                                                <db:gotoButton style="width:150" destination="/orders_single_ro.jsp" caption="orders (single ro)"/>
                                                <db:gotoButton style="width:150" destination="/orders_list_editable.jsp" caption="orders (list edit)"/>
                                                <db:gotoButton style="width:150" destination="/orders_list_editable_rb.jsp" caption="orders (list edit rb)"/>
                                                <db:gotoButton style="width:150" destination="/orders_list_and_single.jsp" caption="orders (list and single)"/>
                                            </p>
                                        </td>
                                    </tr>

	<tr valign="TOP">
                                        <td valign="TOP">
                                            <p>
                                                <db:gotoButton style="width:150" destination="/pets_list.jsp" caption="pets (list)"/>
                                                <db:gotoButton style="width:150" destination="/pets_single.jsp" caption="pets (single)"/>
                                                <db:gotoButton style="width:150" destination="/pets_single_ro.jsp" caption="pets (single ro)"/>
                                                <db:gotoButton style="width:150" destination="/pets_list_editable.jsp" caption="pets (list edit)"/>
                                                <db:gotoButton style="width:150" destination="/pets_list_editable_rb.jsp" caption="pets (list edit rb)"/>
                                                <db:gotoButton style="width:150" destination="/pets_list_and_single.jsp" caption="pets (list and single)"/>
                                            </p>
                                        </td>
                                    </tr>

	<tr valign="TOP">
                                        <td valign="TOP">
                                            <p>
                                                <db:gotoButton style="width:150" destination="/priority_list.jsp" caption="priority (list)"/>
                                                <db:gotoButton style="width:150" destination="/priority_single.jsp" caption="priority (single)"/>
                                                <db:gotoButton style="width:150" destination="/priority_single_ro.jsp" caption="priority (single ro)"/>
                                                <db:gotoButton style="width:150" destination="/priority_list_editable.jsp" caption="priority (list edit)"/>
                                                <db:gotoButton style="width:150" destination="/priority_list_editable_rb.jsp" caption="priority (list edit rb)"/>
                                                <db:gotoButton style="width:150" destination="/priority_list_and_single.jsp" caption="priority (list and single)"/>
                                            </p>
                                        </td>
                                    </tr>

	<tr valign="TOP">
                                        <td valign="TOP">
                                            <p>
                                                <db:gotoButton style="width:150" destination="/service_list.jsp" caption="service (list)"/>
                                                <db:gotoButton style="width:150" destination="/service_single.jsp" caption="service (single)"/>
                                                <db:gotoButton style="width:150" destination="/service_single_ro.jsp" caption="service (single ro)"/>
                                                <db:gotoButton style="width:150" destination="/service_list_editable.jsp" caption="service (list edit)"/>
                                                <db:gotoButton style="width:150" destination="/service_list_editable_rb.jsp" caption="service (list edit rb)"/>
                                                <db:gotoButton style="width:150" destination="/service_list_and_single.jsp" caption="service (list and single)"/>
                                            </p>
                                        </td>
                                    </tr>

	
                
                               
																	 
											            <tr>
										                <td>                
										                <b>... and here some examples of nested forms:</b>
										                </td>
										              </tr>
											            <tr>
										                <td>                
										                <ul>
										                <li><a href="customer_complaints.jsp">customers complaints</a> (shows customers and their associated complaints)
										                <li><a href="customers_pets.jsp">customers pets</a> (shows customers and their associated pets)
										                </ul>
										                
										                <p>
										                <a href="complaint_viewTable.jsp">complaint_viewTable.jsp</a><br>
																		<a href="customer_viewTable.jsp">customer_viewTable.jsp</a><br>
																		<a href="orders_viewTable.jsp">orders_viewTable.jsp</a> <br>
																		<a href="pets_viewTable.jsp">pets_viewTable.jsp</a><br>
																		<a href="priority_viewTable.jsp">priority_viewTable.jsp</a><br>
																		<a href="service_viewTable.jsp">service_viewTable.jsp</a><br>
										                </p>
										                
										                </td>
										              </tr>										              
										              
                                
</table>																

                            </center>
                        </db:dbform>
                    </center>
                </td>
            </tr>
            <tr>
                <td/>
            </tr>
        </table>
    </body>
</html>
