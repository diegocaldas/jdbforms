<html>
	<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
	<head>
	   <db:base />
	   <target="_top">
	</head>
   <body>
      <db:dbform autoUpdate="false" 
                 followUp="/tests/testAuthorBooksSubFormSingle.jsp" 
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
                 <db:navNewButton   style="width:100" caption="New"  showAlwaysInFooter="false"/>
                 <db:navCopyButton  style="width:100" caption="Copy" showAlwaysInFooter="false"/>
                 &nbsp;
               </td>
            </tr>
		    <tr>
				<td colspan="2" style="text-align:center">
					<h2>sub form</h2>
				</td>
			</tr>
		    <tr><td colspan="2" style="text-align:center">
			    <db:dbform 
		      		autoUpdate="false" 
		      		followUp="/tests/testAuthorBooksSubFormSingle.jsp"   
		      		maxRows="1" 
		      		tableName="BOOK"
		     		parentField="AUTHOR_ID"
		     		childField="AUTHOR_ID"	 			
			      	orderBy="ISBN" 
			    >
		         <db:header>
					<table>
				 </db:header>
		         <db:body allowNew="true">
    	           <tr>
        	          <td><db:label fieldName="BOOK_ID"/>&nbsp;</td>
            	      <td><db:textField fieldName="ISBN"/>&nbsp;</td>
                	  <td><db:textField fieldName="TITLE" /> </td>
                	  <td>
							 <db:updateButton style="width:100" caption="Save"/>
							 <db:deleteButton style="width:100" caption="Delete"/>
							 <db:insertButton style="width:100" caption="Insert" showAlways="false" />
					  </td>
	               </tr>			              		 				
				 </db:body>
		         <db:footer>
					  <tr class="button">
					   	<td colspan="4" style="text-align:center">
						 	<db:navFirstButton style="width:100" caption="<< First"/>
					   		<db:navPrevButton  style="width:100" caption="<  Previous"/>
							<db:navNextButton  style="width:100" caption=">  Next"/>
						 	<db:navLastButton  style="width:100" caption=">> Last"/>
						 	<db:navNewButton   style="width:100" caption="New"/>
		                 	<db:navCopyButton  style="width:100" caption="Copy"/>
						 	&nbsp;
					   	</td>
					  </tr>
					<table>
		         </db:footer>
				</db:dbform>	
			</td></tr>
		 </db:footer>
         </db:body>
         <db:footer>
           </table>
         </db:footer>
      </db:dbform>

<%@ include file="httpSnooper.html" %> 

   </body>
</html>

	
	

	
