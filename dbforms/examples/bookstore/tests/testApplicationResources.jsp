<html>
	<head>
		<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
	</head>
    <body>
	    <h1>
 		 Shows howto change selection from one select/search box while changing the value of another selectbox
	    </h1>
	      <db:dbform  
	      		maxRows="*" 
	         	followUp="/tests/testApplicationResources.jsp" 
    	     	autoUpdate="false" tableName="BOOKLISTPERAUTHOR"
    	     	captionResource="true"
	      		>
         		   <!--- To force an reload event.
         			   This is necessary because we have no given table
           			   and needs and reload event to refetch the values of the
         			   select boxes!
         			   Is first parameter here because we do not have a tablename. 
         			   In this special case the hidden input field customEvent is not 
         			   rendered!
         		   
          		   <input type="hidden" name="customEvent" value="re_0_0"/>
                            -->
               <db:header/>
               <db:body>
<table>

    <tr>
        <td>Radio test</td>
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
        <td>Test select data</td>
		<td>
			<db:select fieldName="author" size="1">
		          <db:staticData name="data-author"> 
        			  <db:staticDataItem key="Credit" value="author.hk"/> 
		        	  <db:staticDataItem key="Debit" value="author.jm"/>
	        	  </db:staticData> 
    	    </db:select> 
		</td>
    </tr>
</table>
                       

              </db:body>
               <db:footer/>
		</db:dbform>
   </body>
</html>
