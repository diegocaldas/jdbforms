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
							<db:textField  fieldName="NAME"/> 
							<db:file  fieldName="FILE"/> 
						</td>
					</tr>
					<tr>
						<td>
							<db:insertButton 
								showAlways="false" 
								caption="insert" 
							/> 
							<db:updateButton 
								caption="update" 
							/> 
						</td>
					</tr>
			</db:body>
			<db:footer>
				</table>
			</db:footer>
		</db:dbform>
</body>
</html>    

	    
	        
	        
	        
	        
                            
	        
	    

	    
	    
	    
	    
	        
	        
	        
	        
	        
	        
	    
	    
	    
