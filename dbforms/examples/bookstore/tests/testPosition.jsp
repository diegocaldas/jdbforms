<html>
	<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
	<head>
	   <db:base />
	   <target="_top">
	</head>
   <body>
      <% int i=1; %>			
      <h1 align="center">Test db:position</h1>
      <db:dbform 
      	maxRows="*"
      	tableName="AUTHORVIEW" 
      	followUp="authorsList.jsp" 
      	autoUpdate="false" 
      >
	      	<db:header>
	             <db:errors/>			
			</db:header>
			<db:body allowNew="false">
                   <a href="<db:linkURL href="/authorsEditAlias.jsp" tableName="AUTHORVIEW">
   	                    <db:position fieldName="AUTHOR_ID" value='<%=(String)currentRow_AUTHORVIEW.get("AUTHOR_ID")%>'/>
         	       </db:linkURL>">
	   	               	authorsEditAlias.jsp&nbsp;<db:label fieldName="AUTHOR_ID"/>&nbsp;<db:label fieldName="NAME"/>
         	       </a>&nbsp;
                   <a href="<db:linkURL 
                   		href="/authorsEdit.jsp" 
                   		tableName="AUTHOR"
                   		childField="AUTHOR_ID"
                   		parentField="AUTHOR_ID"
                   	>
   	                    <db:position fieldName="AUTHOR_ID" value='<%=(String)currentRow_AUTHORVIEW.get("AUTHOR_ID")%>'/>
         	       </db:linkURL>">
	   	               	authorsEdit.jsp&nbsp;<db:label fieldName="AUTHOR_ID"/>&nbsp;<db:label fieldName="NAME"/>
         	       </a><br>
			</db:body>
			<db:footer>
	       </db:footer>
	</db:dbform>		
   </body>
</html>
