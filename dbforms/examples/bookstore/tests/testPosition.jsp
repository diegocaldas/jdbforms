<html>
	<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
	<head>
	   <db:base />
	   <target="_top">
	</head>
   <body>
      <% int i=1; %>			
      <h1 align="center">Test db:position</h1>
      <db:dbform 
      	maxRows="*"
      	tableName="AUTHORVIEW" 
      	followUp="/tests/testPosition.jsp" 
      	autoUpdate="false" 
      >
	      	<db:header>
	             <db:errors/>			
				<table>	
			</db:header>
			<db:body allowNew="false">
			   <tr>
                 <td>
                   <a 
                   		href="<db:linkURL href="/tests/testPositionAuthorViewEdit.jsp" 
		                   		tableName="AUTHORVIEW">
	   		                    <db:position fieldName="AUTHOR_ID" value='<%=(String)currentRow_AUTHORVIEW.get("AUTHOR_ID")%>'/>
         				      </db:linkURL>"
				   >
	   	               	test with AUTHORVIEW&nbsp;<db:label fieldName="AUTHOR_ID"/>&nbsp;<db:label fieldName="NAME"/>
         	       </a>
                 </td>
                 <td>   
                   <a 
                   		href="<db:linkURL 
                   				href="/tests/testPositionAuthorEdit.jsp" 
		                   		tableName="AUTHOR"
        		           		childField="AUTHOR_ID"
                		   		parentField="AUTHOR_ID"
                   			>
   	                    		<db:position fieldName="AUTHOR_ID" value='<%=(String)currentRow_AUTHORVIEW.get("AUTHOR_ID")%>'/>
			       	       </db:linkURL>"
				   >
	   	               	test with AUTHOR&nbsp;<db:label fieldName="AUTHOR_ID"/>&nbsp;<db:label fieldName="NAME"/>
         	       </a>
                 </td>
              </tr>
			</db:body>
			<db:footer>
				</table>
	       </db:footer>
	</db:dbform>		
   </body>
</html>
