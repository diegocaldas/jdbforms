<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
	<head>
	   <db:base />
	</head>
   <body >
   	  <db:presetFormValues className="interceptors.BookStoreFormInterceptor"
   	  >
   	  	<db:property
			name="field1"
			value="test1"
   	  	/>
   	  	<db:property
			name="field2"
			value="test2"
   	  	/>
   	  </db:presetFormValues>
      <db:dbform autoUpdate="false" 
                 followUp="/tests/testBooksWithInterceptor.jsp" 
                 maxRows="1" 
                 tableName="BOOKWITHINTERCEPTOR"
                  redisplayFieldsOnError="true" 
                 
      >
         <db:header>
	         <h1 align="center">Book with Interceptor</h1>
         </db:header>
         <db:errors/>
         <db:body>
            <table class="fixed" align="center">
               <tr class="even">
                     <td style="width:300px">ID</td>
			         <td style="width:100px"><db:label fieldName="BOOK_ID"/>&nbsp;</td>
               </tr>
               <tr class="odd">
                    <td style="width:300px">ISBN</td>
                    <td style="width:300px"><db:textField size="25" fieldName="ISBN"/></td>
               </tr>
               <tr class="even">
                    <td>TITLE</td>
                    <td><db:textField size="25" fieldName="TITLE"/>
                    </td>
               </tr>
         </db:body>
         <db:footer>
            <tr class="button">
               <td colspan="2" style="text-align:center">
                 <db:updateButton style="width:100" caption="Save"/>
                 <db:deleteButton style="width:100" caption="Delete"/>
                 <db:insertButton style="width:100" caption="Insert" showAlways="false" />
               </td>
            </tr>
            <tr class="button">
               <td colspan="2" style="text-align:center">
                 <db:navFirstButton style="width:100" caption="<< First"/>
                 <db:navPrevButton  style="width:100" caption="<  Previous"/>
                 <db:navNextButton  style="width:100" caption=">  Next"/>
                 <db:navLastButton  style="width:100" caption=">> Last"/>
                 <db:navNewButton   style="width:100" caption="New"/>
                 &nbsp;
               </td>
            </tr>
            </table>
            <p>To test insert exception move to new page and enter the string "exception" as ISBN. This will generate an exception</p>
         </db:footer>
      </db:dbform>

<%@ include file="httpSnooper.jsp" %> 

   </body>
</html>

	
	

	
