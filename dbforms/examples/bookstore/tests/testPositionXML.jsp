<html>
	<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
	<head>
	   <db:base />
	   <target="_top">
	</head>
   <body>
      <% int i=1; %>			
      <h1 align="center">Test db:position with XML data</h1>
      <db:dbform 
      	maxRows="*"
      	tableName="XMLBOOKS" 
      	followUp="testPositionXML.jsp" 
      	autoUpdate="false" 
      >
	      	<db:header>
	             <db:errors/>			
			</db:header>
	
			<db:body allowNew="false">
                <db:label fieldName="AUTHOR"/><br/>
                <%=(String)currentRow_XMLBOOKS.get("AUTHOR")%><br/>
                <db:linkURL href="/authorsEditAlias.jsp" tableName="AUTHORVIEW">
						<!-- Attention: field to reference must be a key field in the target table! -->
   	                    <db:position fieldName="AUTHOR_ID" value='<%=(String)currentRow_XMLBOOKS.get("AUTHOR_ID")%>'/>
        	      </db:linkURL>
			</db:body>
			<db:footer>
	       </db:footer>
	</db:dbform>		
   </body>
</html>
