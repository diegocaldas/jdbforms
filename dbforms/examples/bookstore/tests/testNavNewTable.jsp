<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<% int i=0; %>
<html>
<head>
	<db:base/>
</head>
	<body>

		<db:dbform 
			multipart="false" 
			autoUpdate="false" 
			followUp="/tests/testNavNewTable.jsp" 
			maxRows="*" tableName="BOOK"
		>
			<db:header>
				<db:errors/>  
				<table>
			</db:header>
			<db:body allowNew="false">
				<tr>
					<td><%= currentRow_BOOK.get("BOOK_ID") %>&nbsp;</td>
					<td><%= currentRow_BOOK.get("ISBN") %>&nbsp;</td>
					<td><%= currentRow_BOOK.get("AUTHOR_ID") %>&nbsp;</td>
					<td><%= currentRow_BOOK.get("TITLE") %>&nbsp;</td>
					<td>				
					    <a
			           		href="<db:linkURL 
			           					href="/tests/testAuthorBooksSubForm.jsp" 
			           					tableName="AUTHOR"	
        		   						singleRow="YES">
    	       	         					<db:position 
    	       	         							fieldName="AUTHOR_ID" 
    	       	         							value="<%=(String)currentRow_BOOK.get("AUTHOR_ID")%>"/>
						   	       </db:linkURL>"
						>
				        	<db:dataLabel fieldName="AUTHOR_ID">
				        		<db:tableData 
				        			name="data_author" 
				        			foreignTable="AUTHOR" 
				        			visibleFields="NAME" 
				        			storeField="AUTHOR_ID"/> 
				        		</db:dataLabel>
			           </a>
			        </td>					
				</tr>
			</db:body>
			<db:footer>
				</table>     
				<db:navNewButton
						followUp="/tests/testNavNewTableEdit.jsp" caption="Insert new ..."/>
				</db:footer>
		</db:dbform>

<%@ include file="httpSnooper.jsp" %> 

	</body>
</html>    

	    
	        
	        
	        
	        
                            
	        
	    

	    
	    
	    
	    
	        
	        
	        
	        
	        
	        
	    
	    
	    
