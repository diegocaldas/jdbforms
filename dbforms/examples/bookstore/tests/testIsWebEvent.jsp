<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html xmlns:db="http://www.wap-force.com/dbforms">
	<head>
		<db:base/>
	</head>
	<body>
		<db:dbform
		  localWebEvent="navLast"
			followUp="<%=request.getServletPath()%>" 
			maxRows="1"
			tableName="BOOK">
			<db:header>
				<db:errors/>  
				<table>
			</db:header>
			<db:body allowNew="false">
				<tr>
					<td><db:label fieldName="BOOK_ID"/>&nbsp;</td>
					<td><db:label fieldName="ISBN"/>&nbsp;</td>
					<td><db:label fieldName="AUTHOR_ID"/>&nbsp;</td>
					<td><db:label fieldName="TITLE"/>&nbsp;</td>
				</tr>
				<tr>
					<td colspan="4">
					  <db:isWebEvent event="navLast">
					    this should be printed
					  </db:isWebEvent>
					</td>
				</tr>
				<tr>
					<td colspan="4">
					  <db:isWebEvent event="navFirst" value="false">
					    this should also be printed
					  </db:isWebEvent>
					</td>
				</tr>
				<tr>
					<td colspan="4">
					  <db:isWebEvent event="navFirst" >
					    this should not be printed
					  </db:isWebEvent>
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

	    
	        
	        
	        
	        
                            
	        
	    

	    
	    
	    
	    
	        
	        
	        
	        
	        
	        
	    
	    
	    
