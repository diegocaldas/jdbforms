<html>
	<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
	<head>
	   <db:base />
	   <target="_top">
	</head>
   <body >
      <db:dbform autoUpdate="false" 
                 followUp="/authorBooksSubForm.jsp" 
                 maxRows="1" 
                 tableName="AUTHOR"
                 
      >
         <db:header>
	         <h1 align="center">Edit Authors</h1>
         </db:header>
         <db:errors/>
         <db:body>
            <table class="fixed" align="center">
               <tr class="even">
                     <td style="width:300px">ID</td>
			         <td style="width:100px"><db:label fieldName="AUTHOR_ID"/>&nbsp;</td>
               </tr>
               <tr class="odd">
                    <td style="width:300px">NAME</td>
                    <td style="width:300px"><db:textField size="25" fieldName="NAME"/></td>
               </tr>
               <tr class="even">
                    <td>ORGANISATION</td>
                    <td><db:textField size="25" fieldName="ORGANISATION"/>
                    </td>
               </tr>
		    <db:dbform 
		      		autoUpdate="false" 
		      		followUp=""  
		      		maxRows="*" 
		      		tableName="BOOK"
		     		parentField="AUTHOR_ID"
		     		childField="AUTHOR_ID"	 			
			      	orderBy="ISBN" 
		    >
		         <db:header>
				 </db:header>
		         <db:body allowNew="false">
    	           <tr>
        	          <td style="width:145px"><db:label fieldName="BOOK_ID"/>&nbsp;</td>
            	      <td style="width:130px"><db:label fieldName="ISBN"/>&nbsp;</td>
                	  <td style="width:145px"><db:label fieldName="TITLE" /> </td>
	               </tr>			              		 				
				 </db:body>
		         <db:footer>
				 </db:footer>
			</db:dbform>	
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
            <tr class="button">
               <td colspan="2" style="text-align:center">
                 <db:gotoButton style="width:200" caption="Back to list" destination="/authorsList.jsp"/>
               </td>
            </tr>
            </table>
         </db:footer>
      </db:dbform>
   </body>
</html>

	
	

	
