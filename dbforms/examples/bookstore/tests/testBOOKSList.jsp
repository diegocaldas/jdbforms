<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html xmlns:db="http://www.wap-force.com/dbforms">
	<head>
		<db:base/>
	</head>
	<body>
		<db:dbform 
			multipart="false" 
			autoUpdate="false" 
			followUp="/tests/testBOOKSList.jsp" 
			maxRows="*" tableName="BOOK">
			<db:header>
				<db:errors/>  
				<table>
			</db:header>
			<db:body allowNew="false">
				<tr>
					<td><%= currentRow_BOOK.get("BOOK_ID") %></a>&nbsp;</td>
					<td><%=currentRow_BOOK.get("ISBN") %>&nbsp;</td>
					<td><%=currentRow_BOOK.get("AUTHOR_ID") %>&nbsp;</td>
					<td><%=currentRow_BOOK.get("TITLE") %>&nbsp;</td>
				</tr>
			</db:body>
			<db:footer>
				</table>
			</db:footer>
		</db:dbform>
</body>
</html>    

	    
	        
	        
	        
	        
                            
	        
	    

	    
	    
	    
	    
	        
	        
	        
	        
	        
	        
	    
	    
	    