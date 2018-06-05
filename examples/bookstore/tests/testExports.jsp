<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
	<head>
		<db:base/>
	</head>
    <h1>
		Shows howto print a JasperReport from dbforms
	</h1>
	<p>Shows the technique to print a single selected row and to print all show rows!</p>
	<body>
		<db:dbform 
			multipart="false" 
			autoUpdate="false" 
			maxRows="*" tableName="BOOK">
			<db:header>
				<db:errors/>  
				<table>
			</db:header>
			<db:body allowNew="false">
				<tr>
					<td><db:associatedRadio name="currentRow" /></td>
					<td><db:label fieldName="BOOK_ID"/>&nbsp;</td>
					<td><db:label fieldName="ISBN"/>&nbsp;</td>
					<td><db:label fieldName="AUTHOR_ID"/>&nbsp;</td>
					<td><db:label fieldName="TITLE"/>&nbsp;</td>
					<td>
	                  	<db:gotoButton 
    	              			caption="print current as PDF"
								destTable="BOOK"
								keyToKeyToDestPos="currentRow"
        	          			singleRow="true"
        	          			destination="/jasperreport/books?filename=jasper_testExports.pdf"
                	  	/>
					</td>
					<td>
	                  	<db:gotoButton 
    	              			caption="print current as CSV"
								destTable="BOOK"
								keyToKeyToDestPos="currentRow"
        	          			singleRow="true"
        	          			destination="/jasperreport/books?filename=jasper_testExports.xls&reporttype=CSV"
                	  	/>
	                  	<db:gotoButton 
    	              			caption="export current as CSV"
								destTable="BOOK"
								keyToKeyToDestPos="currentRow"
        	          			singleRow="true"
        	          			destination="/csvreport/books?filename=testExports.csv"
                	  	/>
	                  	<db:gotoButton 
    	              			caption="export current as EXCEL"
								destTable="BOOK"
								keyToKeyToDestPos="currentRow"
        	          			singleRow="true"
        	          			destination="/excelreport/books?filename=testExports.xls&sheetname=BOOKLISTS"
                	  	/>
					</td>
				</tr>
			</db:body>
			<db:footer>
				<tr>
					<td colspan="6">
	                  	<db:gotoButton 
    	              			caption="print all as PDF"
								destTable="BOOK"
        	          			singleRow="false"
        	          			destination="/jasperreport/books" 
                	  	/>
	                  	<db:gotoButton 
    	              			caption="print all as CSV"
								destTable="BOOK"
        	          			singleRow="false"
        	          			destination="/jasperreport/books?reporttype=CSV" 
                	  	/>
	                  	<db:gotoButton 
    	              			caption="export all as CSV"
								destTable="BOOK"
        	          			singleRow="false"
        	          			destination="/csvreport/books?filenname=textExportsAll.csv"
                	  	/>
	                  	<db:gotoButton 
    	              			caption="export all as EXCEL"
								destTable="BOOK"
        	          			singleRow="false"
        	          			destination="/excelreport/books?filename=testExportsAll.xls&sheetname=BOOKLISTS"
                	  	/>
					</td>
				</tr>
				</table>
			</db:footer>
		</db:dbform>
</body>
</html>    

	    
	        
	        
	        
	        
                            
	        
	    

	    
	    
	    
	    
	        
	        
	        
	        
	        
	        
	    
	    
	    
