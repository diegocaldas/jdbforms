<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<% int i=0; %>
<html xmlns:db="http://www.wap-force.com/dbforms">
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
				</tr>
			</db:body>
			<db:footer>
				</table>     
				<db:navNewButton
						followUp="/tests/testNavNewTableEdit.jsp" caption="Insert new ..."/>
				</db:footer>
		</db:dbform>

<%@ include file="httpSnooper.html" %> 

	</body>
</html>    

	    
	        
	        
	        
	        
                            
	        
	    

	    
	    
	    
	    
	        
	        
	        
	        
	        
	        
	    
	    
	    
