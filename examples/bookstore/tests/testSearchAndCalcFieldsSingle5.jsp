<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
   <head>
   </head>
   <body>
      <h1>
         Tests search and calc fields Single
      </h1>
      <db:dbform  
	      	maxRows="5" 
	         followUp="/tests/testSearchAndCalcFieldsSingle.jsp" 
  	     	 autoUpdate="false" 
  	     	 tableName="BOOKLISTPERAUTHOR"
       >
         <db:header>
         </db:header>
         <db:body>
         <db:label fieldName="ROW_NUM"/>&nbsp;<db:label fieldName="BOOK_ID"/>&nbsp;<db:label fieldName="ISBN"/>&nbsp;<db:label fieldName="TITLE"/>&nbsp;<db:label fieldName="AUTHOR_ID"/>&nbsp;<db:label fieldName="ISBN_TITLE"/>&nbsp;<br/>
         </db:body>
         <db:footer>
							<db:navFirstButton caption="first"/>
							<db:navPrevButton  caption="previous"/>
							<db:navNextButton  caption="next"/>
							<db:navLastButton  caption="last"/>
         </db:footer>
      </db:dbform>
   </body>
</html>
