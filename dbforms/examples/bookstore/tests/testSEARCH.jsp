<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
   <head>
      <db:base/>
   </head>
   <body>
      <db:errors/>
      <db:dbform 
				multipart="false" 
				autoUpdate="false" 
				followUp="/tests/testSEARCHRESULTS.jsp" 
				maxRows="*" 
				tableName="BOOK"
			>
         <db:header>
            <h1>
               Search Page
            </h1>
            <!-- Search Result Display -->
            <!-- Search Box, Individual Table -->
            <table cellspacing="0" cellpadding="1" width="550">
               <tr bgcolor="#F7A629">
                  <td bgcolor="#F7A629">
                     <b>Search Book Name
                     </b></td>
                  <td colspan="2" bgcolor="#F7A629">
                     &nbsp;
                  </td>
                  <td bgcolor="#F7A629">
                     <input value="Search!" type="button" onClick="javascript:document.dbform.submit()">
                     <db:gotoButton caption="Search!" destination="/tests/testSEARCHRESULTS.jsp" />
                  </td>
               </tr>
               <tr bgcolor="#CCBBCC">
                  <td>
                     Field
                  </td>
                  <td>
                     Value
                  </td>
                  <td>
                     Combining mode
                  </td>
                  <td>
                     Search Algorithm
                  </td>
               </tr>
               <tr bgcolor="#BBCCBB">
                  <td>
                     Book Name
                  </td>
                  <td>
                     <input type="text" name="<%=searchFieldNames_BOOK.get("TITLE") %>" size="17" />
                  </td>
                  <td></td>
                  <td>
                     <input type="checkbox" name="<%= searchFieldAlgorithmNames_BOOK.get("TITLE")%>" size="30" value="weak">
                     Weak.
                  </td>
               </tr>
            </table>
            <br><br><table border="5" width="60%">
               <tr bgcolor="#CCBBCC">
                  <td>
                     ID
                  </td>
                  <td>
                     ISBN
                  </td>
                  <td>
                     AUTHOR ID
                  </td>
                  <td>
                     Book Title
                  </td>
               </tr>
            </db:header>
            <db:body allowNew="false">
            </db:body>
            <db:footer>
            </table>
         </db:footer>
      </db:dbform>
   </body>
</html>
