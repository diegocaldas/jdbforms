<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html xmlns:db="http://www.wap-force.com/dbforms">
	<head>
		<db:base/>
	</head>
	<body>
		<db:dbform 
			multipart="false" 
			autoUpdate="true" 
			followUp="/tests/testBOOKSListAutoUpdate.jsp" 
			maxRows="*" tableName="BOOK">
			<db:header>
				<db:errors/>  
				<table>
			</db:header>
			<db:body allowNew="true">
				<tr>
					<td><db:textField fieldName="BOOK_ID"/></a>&nbsp;</td>
					<td><db:textField fieldName="ISBN"/>&nbsp;</td>
					<td><db:textField fieldName="AUTHOR_ID"/>&nbsp;</td>
					<td><db:textField fieldName="TITLE"/>&nbsp;</td>
				</tr>
			</db:body>
			<db:footer>
				<tr>
					<td colspan="4" align="center">
						<db:updateButton />
					</td>
				</tr>
				</table>
			</db:footer>
		</db:dbform>

<%@ include file="httpSnooper.html" %> 

</body>
</html>    

	    
	        
	        
	        
	        
                            
	        
	    

	    
	    
	    
	    
	        
	        
	        
	        
	        
	        
	    
	    
	    
