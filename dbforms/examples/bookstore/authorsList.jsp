<html>
	<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
	<head>
	   <db:base />
	   <target="_top">
	</head>
   <body>
      <% int i=1; %>			
      <h1 align="center">List of authors</h1>
      <db:dbform 
      	tableName="AUTHOR" 
      	followUp="authorsList.jsp" 
      	autoUpdate="false" 
      >
	      	<db:header>
	             <db:errors/>			
	             <table class="fixed" align="center" style="width:800px">
	                <tr class="header">
	                   <td style="width:25px"><input type="image" src="one.gif" /></td>
	                   <td style="width:100px">ID&nbsp;</td>
	                   <td style="width:250px">NAME&nbsp;<db:sort fieldName="NAME"   /></td>
	                   <td style="width:250px">ORGANISATION&nbsp;<db:sort fieldName="ORGANISATION"  /></td>
	                   <td>&nbsp;</td>
	                </tr>
	             </table>
		         <table class="fixed" align="center" style="width:800px">
			</db:header>
			<db:body allowNew="false">
				   <% String bg = (i % 2 == 0) ? "even" : "odd"; i++; %>		
		           <tr class="<%= bg %>" >
			         <td style="width:25px;text-align:center;"><db:associatedRadio name="currentRow" /></td>
			         <td style="width:100px"><db:label fieldName="AUTHOR_ID"/>&nbsp;</td>
			         <td style="width:250px"><db:label fieldName="NAME"/>&nbsp;</td>
			         <td style="width:250px"><db:label fieldName="ORGANISATION"/>&nbsp;</td>
	                 <td>&nbsp;</td>
			      </tr>			              		 				
			</db:body>
			<db:footer>
	  			</table>
	             <table class="fixed" align="center" style="width:800px">
	                <tr class="header">
	                   <td style="width:25px">&nbsp;</td>
	                   <td style="width:100px"></td>
	                   <td style="width:250px"><db:search fieldName="NAME" searchAlgo="weakEnd" style="width:225px" /></td>
	                   <td style="width:250px">
		                 	<db:searchCombo 
		                 		fieldName="ORGANISATION"
								style="width:225px"
					        	customEntry=",,true"
					        	onChange="this.form.submit();"
							>
								<db:queryData
								   name="ORGANISATION"
								   query="select distinct ORGANISATION, ORGANISATION from AUTHOR where not ORGANISATION is null"
								/>
							</db:searchCombo>
				   	</td>
                    <td>&nbsp;</td>
	            </tr>
	            	<tr class="button">
	                	<td colspan="4" style="text-align:right">
	                     <input type="hidden" name="currentRow">
	                     <db:navNewButton followUp="/authorsEdit.jsp"  caption="new" destTable="AUTHOR"  />
	                     <db:gotoButton destination="/authorsEdit.jsp"  caption="edit" 	destTable="AUTHOR" keyToKeyToDestPos="currentRow"  />
	                   </td>
	                </tr>
	             </table>      
	       </db:footer>
	</db:dbform>		
   </body>
</html>
