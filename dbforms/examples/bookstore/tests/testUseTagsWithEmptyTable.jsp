<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
	<head>
	   <db:base />
	   <target="_top">
	</head>
    <body>
	  <h1>Use Tags with empty table</h1>
 	  <db:dbform
      	followUp="testUseTagsWithEmptyTable.jsp" 
 	  >
			<db:select 
					id="select"
					fieldName="select"
					size="10"
			>
					<db:queryData
						name="AUTHOR"
						query="Select AUTHOR_ID, NAME from AUTHOR" 
					/>
			</db:select>
			<db:radio
					id="radio"
					fieldName="radio"
					value="2"
			>
					<db:queryData
						name="AUTHOR"
						query="Select AUTHOR_ID, NAME from AUTHOR" 
					/>
			</db:radio>
 	  </db:dbform> 
    </body>
</html>