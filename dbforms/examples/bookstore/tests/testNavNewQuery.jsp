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
			followUp="/tests/testNavNewQuery.jsp" 
			maxRows="*" tableName="BOOK_QUERY"
		>
			<db:header>
				<db:errors/>  
				<table>
			</db:header>
			<db:body allowNew="false">
				<tr>
					<td><%= currentRow_BOOK_QUERY.get("BOOK_ID") %>&nbsp;</td>
					<td><%= currentRow_BOOK_QUERY.get("ISBN") %>&nbsp;</td>
					<td><%= currentRow_BOOK_QUERY.get("AUTHOR_ID") %>&nbsp;</td>
					<td><%= currentRow_BOOK_QUERY.get("TITLE") %>&nbsp;</td>
				</tr>
			</db:body>
			<db:footer>
				</table>     
				<db:navNewButton
						followUp="/tests/testNavNewQueryEdit.jsp" caption="Insert new ..."/>
				</db:footer>
		</db:dbform>

<%@ include file="httpSnooper.jsp" %> 

	</body>
</html>    

	    
	        
	        
	        
	        
                            
	        
	    

	    
	    
	    
	    
	        
	        
	        
	        
	        
	        
	    
	    
	    
