<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
   <head>
   </head>
   <body>
      <h1>
         Tests search and calc fields
      </h1>
      <db:dbform  
	      	maxRows="*" 
	         followUp="/tests/testSearchAndCalcFields.jsp" 
  	     	 autoUpdate="false" 
  	     	 tableName="BOOKLISTPERAUTHOR"
    	     captionResource="true"
       >
         <db:header>
         </db:header>
         <db:body>
         <db:label fieldName="ROW_NUM"/>&nbsp;<db:label fieldName="BOOK_ID"/>&nbsp;<db:label fieldName="ISBN"/>&nbsp;<db:label fieldName="TITLE"/>&nbsp;<db:label fieldName="AUTHOR_ID"/>&nbsp;<db:label fieldName="ISBN_TITLE"/>&nbsp;<br/>
         </db:body>
         <db:footer>
            <br/><br/>Search for AUTHOR_ID: <db:search fieldName="AUTHOR_ID"/> 
         </db:footer>
      </db:dbform>
   </body>
</html>
