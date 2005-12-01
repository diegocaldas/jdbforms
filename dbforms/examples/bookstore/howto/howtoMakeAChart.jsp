<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<%@ taglib uri="/WEB-INF/cewolf.tld" prefix="cewolf" %>
<html>
	<head>
		<db:base/>
	</head>
	<body>
	<db:dbform 
			multipart="false" 
			autoUpdate="false" 
			followUp="/howto/howtoMakeaPieChart.jsp" 
			maxRows="*" tableName="BOOKCOUNT">
			<db:header>
				<db:errors/>  
				<table>
			</db:header>
			<db:body allowNew="false">
				<tr>
					<td><db:label fieldName="NAME"/></a>&nbsp;</td>
					<td><db:label fieldName="C"/>&nbsp;</td>
				</tr>
			</db:body>
			<db:footer>
				</table>
				<cewolf:chart 
					id="pieChart" 
					title='Books per Author' 
					type="pie"
				>
				    <cewolf:data>
				        <db:pieData  
				        	categoryField="NAME" 
				        	dataField="C"
				        />
				    </cewolf:data>
				</cewolf:chart>
				<cewolf:img chartid="pieChart" renderer="/cewolf" width="800" height="400"/>

				<cewolf:chart 
					id="categoryChart" 
					title='Books per Author' 
					type="category"
				>
				    <cewolf:data>
				        <db:categoryData  
				        	categoryField="NAME" 
				        	dataField="C"
				        />
				    </cewolf:data>
				</cewolf:chart>
				<cewolf:img chartid="categoryChart" renderer="/cewolf" width="800" height="400"/>

			</db:footer>
		</db:dbform>
</body>
</html>    

	    
	        
	        
	        
	        
                            
	        
	    

	    
	    
	    
	    
	        
	        
	        
	        
	        
	        
	    
	    
	    
