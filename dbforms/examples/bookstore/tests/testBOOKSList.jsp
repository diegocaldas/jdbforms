<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
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
				<tr>
					<td><db:sort fieldName="BOOK_ID"/>&nbsp;</td>
					<td><db:sort fieldName="ISBN"/>&nbsp;</td>
					<td><db:sort fieldName="AUTHOR_ID"/>&nbsp;</td>
					<td><db:sort fieldName="TITLE"/>&nbsp;</td>
				</tr>
			</db:header>
			<db:body allowNew="false">
				<tr>
					<td><db:label fieldName="BOOK_ID"/></a>&nbsp;</td>
					<td><db:label fieldName="ISBN"/>&nbsp;</td>
					<td><db:label fieldName="AUTHOR_ID"/>&nbsp;</td>
					<td><db:label fieldName="TITLE"/>&nbsp;</td>
				</tr>
			</db:body>
			<db:footer>
				</table>
			</db:footer>
		</db:dbform>

<%@ include file="httpSnooper.jsp" %> 

</body>
</html>    

	    
	        
	        
	        
	        
                            
	        
	    

	    
	    
	    
	    
	        
	        
	        
	        
	        
	        
	    
	    
	    
