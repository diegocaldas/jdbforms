<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html xmlns:db="http://www.wap-force.com/dbforms">
	<head>
		<db:base/>
	</head>
	<body>
		<db:dbform 
			multipart="true" 
			autoUpdate="false" 
			followUp="/tests/testDISKBLOBS.jsp" 
			maxRows="1" 
			tableName="BLOBTEST">
			<db:header>
				<table>
			</db:header>
			<db:body allowNew="true">
					<tr>
						<td>
							<db:file  fieldName="FILE"/> 
						</td>
						<td>
							<db:updateButton 
								caption="update" 
							/> 
						</td>
					</tr>
			</db:body>
			<db:footer>
					<tr>
						<td>
							<db:file  fieldName="FILE"/> 
						</td>
						<td>
							<db:insertButton 
								showAlways="true" 
								caption="insert" 
							/> 
						</td>
					</tr>
				</table>
			</db:footer>
		</db:dbform>
</body>
</html>    

	    
	        
	        
	        
	        
                            
	        
	    

	    
	    
	    
	    
	        
	        
	        
	        
	        
	        
	    
	    
	    
