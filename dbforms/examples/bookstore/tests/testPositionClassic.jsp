<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
	<head>
	   <db:base />
	   <target="_top">
	</head>
   <body>
      <% int i=1; %>			
      <h1 align="center">Test db:position</h1>
      <db:dbform 
      	maxRows="1"
      	tableName="AUTHOR_CLASSIC" 
      	followUp="/tests/testPositionClassic.jsp" 
      	autoUpdate="false" 
      >
	      	<db:header>
	             <db:errors/>			
				<table>	
			</db:header>
			<db:body allowNew="false">
			   <tr>
			   	  <td>
			   	  	<db:label fieldName="AUTHOR_ID" />
			   	  </td>	
			   	  <td>
			   	  	<db:label fieldName="NAME" />
			   	  </td>	
                 <td>   
					<db:gotoButton
		                   		caption="gotoEditView"        
                   				destination="/tests/testPositionAuthorEditClassic.jsp" 
		                   		destTable="AUTHOR_CLASSIC"
		                   		destPos='<%= position_AUTHOR_CLASSIC%>'
                   	/>
                 </td>
              </tr>
			</db:body>
			<db:footer>
		            <tr>
		               <td colspan="3">
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
