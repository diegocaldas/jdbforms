<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html xmlns:db="http://www.wap-force.com/dbforms">
	<head>
		<db:base/>
	</head>
	<body>
		<db:dbform 
			multipart="false" 
			autoUpdate="false" 
			followUp="/howto/howtoPrintaJasperReport.jsp" 
			maxRows="*" tableName="BOOK">
			<db:header>
				<db:errors/>  
				<table>
			</db:header>
			<db:body allowNew="false">
				<tr>
					<td><db:associatedRadio name="currentRow" /></td>
					<td><db:label fieldName="BOOK_ID"/></a>&nbsp;</td>
					<td><db:label fieldName="ISBN"/>&nbsp;</td>
					<td><db:label fieldName="AUTHOR_ID"/>&nbsp;</td>
					<td><db:label fieldName="TITLE"/>&nbsp;</td>
					<td>
	                  	<db:gotoButton 
    	              			caption="print current"
								destTable="BOOK"
								keyToKeyToDestPos="currentRow"
        	          			singleRow="true"
        	          			destination="/reports/books"
                	  	/>
					</td>
				</tr>
			</db:body>
			<db:footer>
				<tr>
					<td colspan="6">
	                  	<db:gotoButton 
    	              			caption="print all"
								destTable="BOOK"
        	          			singleRow="false"
        	          			destination="/reports/books" 
                	  	/>
					</td>
				</tr>
				</table>
			</db:footer>
		</db:dbform>
</body>
</html>    

	    
	        
	        
	        
	        
                            
	        
	    

	    
	    
	    
	    
	        
	        
	        
	        
	        
	        
	    
	    
	    
