<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
   <head>
   </head>
   <body>
      <h1>
         Tests retrieving of caption resources
      </h1>
      <db:dbform  
	      	maxRows="1" 
	         followUp="/tests/testApplicationResources.jsp" 
    	     	 autoUpdate="false" tableName="BOOKLISTPERAUTHOR"
    	     	captionResource="true"
	      		>
         <db:header/>
         <db:body>
            <table>
               <tr>
                  <td>
                     Radio test
                  </td>
                  <td colspan='2' class="blueTableRowBGColor">
                     <db:radio fieldName="books" growDirection="horizontal" defaultValue="0">
                        <db:staticData name="data-books">
                           <db:staticDataItem key="false" value="books.struts"/>
                           <db:staticDataItem key="true" value="books.dbforms"/>
                        </db:staticData>
                     </db:radio>
                  </td>
               </tr>
               <tr>
                  <td>
                     Test select data
                  </td>
                  <td>
                     <db:select fieldName="author" size="1">
                        <db:staticData name="data-author">
                           <db:staticDataItem key="Credit" value="author.hk"/>
                           <db:staticDataItem key="Debit" value="author.jm"/>
                        </db:staticData>
                     </db:select>
                  </td>
               </tr>
               <tr>
                  <td>
                     Test image button
                  </td>
                  <td>
					 <db:deleteButton flavor="image" 
						confirmMessage="msg.confirmDelete" 
						src="btnDelete.gif" 
						alt="button.delete" 
					/>
                  </td>
               </tr>
            </table>
         </db:body>
         <db:footer/>
      </db:dbform>
   </body>
</html>
