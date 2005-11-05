<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
	<head>
	   <db:base />
	</head>
   <body >
      <db:dbform autoUpdate="false" 
                 followUp="/tests/testAuthorEditWithValidationServer.jsp" 
                 maxRows="1" 
                 tableName="AUTHOR"
                 autoUpdate="TRUE"
                 formValidatorName="AUTHOR"
                 javascriptValidation="false"
                 redisplayFieldsOnError="true"
      >
         <db:header>
	         <h1 align="center">Edit Authors</h1>
	         <db:errors/>
         </db:header>
         <db:body>
            <table>
               <tr>
                     <td>ID</td>
			         <td><db:label fieldName="AUTHOR_ID"/>&nbsp;</td>
               </tr>
               <tr>
                    <td>NAME</td>
                    <td><db:textField fieldName="NAME"/></td>
               </tr>
               <tr>
                    <td>ORGANISATION</td>
                    <td><db:textField fieldName="ORGANISATION"/>
                    </td>
               </tr>
         </db:body>
         <db:footer>
            <tr>
               <td colspan="2">
                 <db:updateButton caption="Save"/>
                 <db:deleteButton caption="Delete"/>
                 <db:insertButton caption="Insert" showAlways="false" />
               </td>
            </tr>
            <tr>
               <td colspan="2">
                 <db:navFirstButton caption="<< First"/>
                 <db:navPrevButton  caption="<  Previous"/>
                 <db:navNextButton  caption=">  Next"/>
                 <db:navLastButton  caption=">> Last"/>
                 <db:navNewButton   caption="New" showAlwaysInFooter="false" />
                 &nbsp;
               </td>
            </tr>
            </table>
         </db:footer>
      </db:dbform>
<%@ include file="httpSnooper.jsp" %>
   </body>
</html>
