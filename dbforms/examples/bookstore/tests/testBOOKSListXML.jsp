<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html xmlns:db="http://www.wap-force.com/dbforms">
	<head>
		<db:base/>
	</head>
	<body>
		<db:dbform 
			multipart="false" 
			autoUpdate="false" 
			followUp="/tests/testBOOKSListXML.jsp" 
			maxRows="*" tableName="XMLBOOKS">
			<db:header>
				<db:errors/>  
				<table  border="1">
			</db:header>
			<db:body allowNew="false">
				<tr>
					<td><%=currentRow_XMLBOOKS.get("ISBN") %>&nbsp;</td>
					<td><%=currentRow_XMLBOOKS.get("TITLE") %>&nbsp;</td>
					<td><%=currentRow_XMLBOOKS.get("AUTHOR_ID") %>&nbsp;</td>
					<td><%=currentRow_XMLBOOKS.get("AUTHOR_NAME") %>&nbsp;</td>
					<td><%=currentRow_XMLBOOKS.get("AUTHOR_SURNAME") %>&nbsp;</td>
				</tr>
			</db:body>
			<db:footer>
				</table>
			</db:footer>
		</db:dbform>

<%@ include file="httpSnooper.html" %> 

</body>
</html>    

	    
	        
	        
	        
	        
                            
	        
	    

	    
	    
	    
	    
	        
	        
	        
	        
	        
	        
	    
	    
	    
