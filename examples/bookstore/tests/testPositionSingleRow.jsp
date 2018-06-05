<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
	<head>
	   <db:base />
	</head>
   <body>
      <% int i=1; %>			
      <h1 align="center">Test db:position</h1>
      <db:dbform 
      	maxRows="*"
      	tableName="AUTHOR_VIEW" 
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
		                   		tableName="AUTHOR_VIEW"
		                   		singleRow="true"
		                   	   >
	   		                    <db:position fieldName="AUTHOR_ID" value='<%=(String)currentRow_AUTHOR_VIEW.get("AUTHOR_ID")%>'/>
         				      </db:linkURL>"
				   >
	   	               	test with AUTHOR_VIEW&nbsp;<db:label fieldName="AUTHOR_ID"/>&nbsp;<db:label fieldName="NAME"/>
         	       </a>
                 </td>
                 <td>   
					<db:gotoButton
		                   		caption="gotoEditView"        
                   				destination="/tests/testPositionAuthorViewEdit.jsp" 
		                   		destTable="AUTHOR_VIEW"
		                   		destPos='<%= position_AUTHOR_VIEW%>'
		                   		singleRow="true"
                   	/>
                 </td>
                 <td>   
                   <a 
                   		href="<db:linkURL 
                   				href="/tests/testPositionAuthorEdit.jsp" 
		                   		tableName="AUTHOR"
        		           		childField="AUTHOR_ID"
                		   		parentField="AUTHOR_ID"
		                   		singleRow="true"
                   			>
   	                    		<db:position fieldName="AUTHOR_ID" value='<%=(String)currentRow_AUTHOR_VIEW.get("AUTHOR_ID")%>'/>
			       	       </db:linkURL>"
				   >
	   	               	test with AUTHOR&nbsp;<db:label fieldName="AUTHOR_ID"/>&nbsp;<db:label fieldName="NAME"/>
         	       </a>
                 </td>
				 <td>
					position: <%= position_AUTHOR_VIEW %>
				 </td>	
              </tr>
			</db:body>
			<db:footer>
				</table>
	       </db:footer>
	</db:dbform>		

<%@ include file="httpSnooper.jsp" %> 

   </body>
</html>
