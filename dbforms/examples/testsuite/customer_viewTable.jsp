


<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<%int i=0; %>
<%
   boolean showSearchRow;
   String sr=request.getParameter("showSearchRow");
   if (sr==null)
      showSearchRow=false;
   else
      showSearchRow=(new Boolean(sr)).booleanValue();

%>
<html xmlns:db="http://www.wap-force.net/dbforms">
    <head>
        <db:base/>
        <link href="dbforms.css" rel="stylesheet"/>
         <script language="javascript1.1">
         <!--
            function switchSearch(newValue) {
               document.dbform.showSearchRow.value=newValue;
               document.dbform.submit();
            }
         -->
         </script>
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
                     <a href="javascript:switchSearch('<%=!showSearchRow%>')"> [Filtri <%=(showSearchRow)?"OFF":"ON" %>]</a>
                  
                                <a href="menu.jsp">[Menu]</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <db:dbform autoUpdate="false" followUp="/customer_viewTable.jsp" maxRows="5" tableName="customer">
            <db:header>
          <table align="center" >
          
          <INPUT type="hidden" name="showSearchRow" value="<%=showSearchRow%>">
        
                <tr bgcolor="#ACACAC">
                    <td>
                        <db:sort fieldName="id"/>
                    </td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td colspan="2">Ordina per</td>
                </tr>
             <% if (showSearchRow) { %>
           
                <tr bgcolor="#CECECE">
                    <td>
                        <table>
                            <tr>
                                <td>
                                          <INPUT type="text" name="<%= searchFieldNames_customer.get("id") %>" size="10" ></td>
                                <td>
                                          <INPUT type="radio" name="<%= searchFieldModeNames_customer.get("id") %>" checked value="and" >AND</td>
                            </tr>
                            <tr>
                                <td>
                                          <INPUT type="checkbox" name="<%= searchFieldAlgorithmNames_customer.get("id") %>" value="weak" size="10" > Weak</td>
                                <td>
                                          <INPUT type="radio" name="<%= searchFieldModeNames_customer.get("id") %>" value="or" >OR</td>
                            </tr>
                        </table>
                    </td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td colspan="2">
                                          <input type="button" value="Applica!" onClick="javascript:document.dbform.submit()">
                           </td>
                </tr>
                  <% } %>
          
                <tr bgcolor="#CCCC00">
                    <td>id</td>
                    <td>firstname</td>
                    <td>lastname</td>
                    <td>address</td>
                    <td>pcode</td>
                    <td>city</td>
                    <td colspan="2"/>
                </tr>
            </db:header>
            <db:errors/>
            <db:body>
            <tr bgcolor="#<%= (i++%2==0) ? "FFFFCC" : "FFFF99" %>">
          <td><a href="<db:linkURL href="/customer_single_ro.jsp" tableName="customer" position="<%= position_customer %>"/>" ><%= currentRow_customer.get("id") %></a>&nbsp;</td><td><%= currentRow_customer.get("firstname") %>&nbsp;</td><td><%= currentRow_customer.get("lastname") %>&nbsp;</td><td><%= currentRow_customer.get("address") %>&nbsp;</td><td><%= currentRow_customer.get("pcode") %>&nbsp;</td><td><%= currentRow_customer.get("city") %>&nbsp;</td><td><a href="<db:linkURL href="/customer_single.jsp" tableName="customer" position="<%= position_customer %>"/>" ><IMG src="edit.gif" border="0" > </a></td>
                <td>
                    <db:deleteButton style="border:0" src="delete.gif" alt="cancella" flavor="image" caption="delete"/>
                </td>
              </tr>
           
                <center>
                    <db:insertButton caption="Nuovo customer"/>
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
                            <db:navNewButton style="width:40" followUp="/customer_single.jsp" caption="New"/>
                        </td>
                    </tr>
                </table>
            </db:footer>
        </db:dbform>
    </body>
</html>

	

