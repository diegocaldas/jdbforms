<html>
	<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
	<head>
	   <db:base />
	   <target="_top">
	</head>
   <body >
      <db:dbform autoUpdate="false" 
                 followUp="/authorsEditWithFilter.jsp" 
                 maxRows="1" 
                 tableName="AUTHOR"
      >
         <h1 align="center">Edit Authors</h1>
         <db:header>
                <tr class="header">
	                   <td style="width:25px">search</td>
	                   <td style="width:250px"><db:search fieldName="NAME" searchAlgo="weakEnd" style="width:225px" /></td>

                    <td colspan="2">&nbsp;</td>
	            </tr>
	         <db:errors/>
         </db:header>
         <db:body>
            <table class="fixed" align="center">
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

	
	

	
