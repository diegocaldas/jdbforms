<html>
	<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
	<head>
	   <db:base />
	   <target="_top">
	</head>
   <body>
      <% int i=1; %>			
      <h1 align="center">List of books</h1>
      <db:dbform 
      	tableName="XMLBOOKS" 
      	followUp="booksListXML.jsp" 
      	autoUpdate="false" 
      >
	      	<db:header>
	             <db:errors/>			
	             <table class="fixed" align="center" style="width:800px">
	                <tr class="header">
	                   <td style="width:25px"><input type="image" src="one.gif" /></td>
	                   <td style="width:250px">ISBN&nbsp;</td>
	                   <td style="width:250px">TITLE&nbsp;</td>
	                   <td style="width:250px">AUTHOR&nbsp;</td>
	                   <td>&nbsp;</td>
	                </tr>
	             </table>
		         <table class="fixed" align="center" style="width:800px">
			</db:header>
			<db:body allowNew="false">
				   <% String bg = (i % 2 == 0) ? "even" : "odd"; i++; %>		
		           <tr class="<%= bg %>" >
			         <td style="width:25px;text-align:center;"><db:associatedRadio name="currentRow" /></td>
			         <td style="width:250px"><db:label fieldName="ISBN"/>&nbsp;</td>
			         <td style="width:250px"><db:label fieldName="TITLE"/>&nbsp;</td>
			         <td style="width:250px"><db:label fieldName="AUTHOR"/>&nbsp;</td>
	                 <td>&nbsp;</td>
			      </tr>			              		 				
			</db:body>
			<db:footer>
	  			</table>
	       </db:footer>
	</db:dbform>		
   </body>
</html>
