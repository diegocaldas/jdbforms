<html>
	<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
	<head>
	   <db:base />
	   <target="_top">
	</head>
   <body>
      <% int i=1; %>			
      <h1 align="center">List of authors and books</h1>
      <db:dbform tableName="BOOKSPERAUTHOR" followUp="authorsBooksList.jsp" autoUpdate="false" >
	      	<db:header>
	             <db:errors/>			
	             <table class="fixed" align="center" style="width:800px">
	                <tr class="header">
	                   <td style="width:25px"><input type="image" src="one.gif" /></td>
	                   <td style="width:175px">NAME<dbE:sort fieldName="NAME"  upPicture="_icons/up.gif" downPicture="_icons/down.gif" /></td>
	                   <td style="width:175px">ORGANISATION<dbE:sort fieldName="ORGANISATION" upPicture="_icons/up.gif" downPicture="_icons/down.gif" /></td>
	                   <td style="width:175px">ISBN<dbE:sort fieldName="ORGANISATION" upPicture="_icons/up.gif" downPicture="_icons/down.gif" /></td>
	                   <td style="width:175px">TITLE<dbE:sort fieldName="ORGANISATION" upPicture="_icons/up.gif" downPicture="_icons/down.gif" /></td>
	                   <td>&nbsp;</td>
	                </tr>
	             </table>
		         <table class="fixed" align="center" style="width:800px">
			</db:header>
			<db:body allowNew="false">
				   <% String bg = (i % 2 == 0) ? "even" : "odd"; i++; %>		
		           <tr class="<%= bg %>">
			         <td style="width:25px;text-align:center;"><db:associatedRadio name="currentRow" /></td>
			         <td style="width:175px"><db:label fieldName="NAME"/>&nbsp;</td>
			         <td style="width:175px"><db:label fieldName="ORGANISATION"/>&nbsp;</td>
			         <td style="width:175px"><db:label fieldName="ISBN"/>&nbsp;</td>
			         <td style="width:175px"><db:label fieldName="TITLE"/>&nbsp;</td>
	                 <td>&nbsp;</td>
			      </tr>			              		 				
			</db:body>
			<db:footer>
	  			</table>
	             <table class="fixed" align="center" style="width:800px">
	                <tr class="header">
	                   <td style="width:25px">&nbsp;</td>
	                   <td style="width:175px"><db:search fieldName="NAME" searchAlgo="weakEnd" style="width:150px" /></td>
	                   <td style="width:175px">
		                 	<db:searchCombo 
		                 		fieldName="ORGANISATION"
								style="width:150px"
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
	                     <db:gotoButton destination="/authorsEdit.jsp"  caption="Edit" 	destTable="AUTHOR" keyToKeyToDestPos="currentRow"  />
	                   </td>
	                </tr>
	             </table>      
	       </db:footer>
	</db:dbform>		
   </body>
</html>
