<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
	<head>
	   <db:base />
	</head>
   <body >
      <db:dbform autoUpdate="false" 
                 followUp="/tests/testPositionAuthorViewEdit.jsp" 
                 maxRows="1" 
                 tableName="AUTHOR_VIEW"
                 
      >
         <db:header>
	         <h1 align="center">Edit Authors View</h1>
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
         </db:footer>
      </db:dbform>

<%@ include file="httpSnooper.jsp" %> 

   </body>
</html>

	
	

	
