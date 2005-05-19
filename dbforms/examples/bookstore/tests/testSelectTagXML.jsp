<html>
	<head>
		<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
	</head>
    <body>
	      <db:dbform  
	      		maxRows="*" 
	         	followUp="/tests/testSelectTagXML.jsp" 
    	     	autoUpdate="false" 
	      		>
					<db:select
						fieldName="books"
					>
						<db:confTableData
						   name="XMLBOOKS"
						   foreignTable="XMLBOOKS"
						   visibleFields="ISBN, TITLE"
						   storeField="BOOK_ID"
						/>
				   	</db:select>
		</db:dbform>
   </body>
</html>
