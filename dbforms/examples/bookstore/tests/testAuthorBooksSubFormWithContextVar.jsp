<html>
	<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
    <%@page import="org.dbforms.taglib.DbFormContext"  %>
   
	<head>
	   <db:base />
	   <target="_top">
	</head>
   <body>
      <db:dbform autoUpdate="false" 
                 followUp="/tests/testAuthorBooksSubFormWithContextVar.jsp" 
                 maxRows="1" 
                 tableName='<%="AUTHOR" %>'
                 name="AUTHOR"
      >
         <db:header>
	         <h1 align="center">Edit Authors</h1>
	         <db:errors/>
         </db:header>
         <db:body>
            <table class="fixed" align="center">
               <tr class="even">
                     <td style="width:300px">ID</td>
			         <td style="width:100px"><%=((DbFormContext)dbforms.get("AUTHOR")).getCurrentRow().get("AUTHOR_ID")%>&nbsp;</td>
               </tr>
               <tr class="odd">
                    <td style="width:300px">NAME</td>
                    <td style="width:300px"><%=((DbFormContext)dbforms.get("AUTHOR")).getCurrentRow().get("NAME")%></td>
               </tr>
               <tr class="even">
                    <td>ORGANISATION</td>
                    <td>
                       <%=((DbFormContext)dbforms.get("AUTHOR")).getCurrentRow().get("ORGANISATION")%>
                    </td>
               </tr>
            <tr class="button">
               <td colspan="2" style="text-align:center">
                  &nbsp;
               </td>
            </tr>
            <tr class="button">
               <td colspan="2" style="text-align:center">
                 <db:navFirstButton style="width:100" caption="<< First"/>
                 <db:navPrevButton  style="width:100" caption="<  Previous"/>
                 <db:navNextButton  style="width:100" caption=">  Next"/>
                 <db:navLastButton  style="width:100" caption=">> Last"/>
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
		      		followUp="/tests/testAuthorBooksSubForm.jsp"   
		      		maxRows="*" 
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
        	          <td>
        	            <%=((DbFormContext)dbforms.get("BOOK")).getCurrentRow().get("BOOK_ID")%>
        	          </td>
            	       <td>
        	            <%=((DbFormContext)dbforms.get("BOOK")).getCurrentRow().get("ISBN")%>
            	      </td>
                	  <td>
        	            <%=((DbFormContext)dbforms.get("BOOK")).getCurrentRow().get("TITLE")%>
                	  </td>
                	  <td>
                	  	&nbsp;
					  </td>
	               </tr>			              		 				
				 </db:body>
		         <db:footer>
					<table>
		         </db:footer>
				</db:dbform>	
			</td></tr>
         </db:body>
         <db:footer>
           </table>
         </db:footer>
      </db:dbform>

<%@ include file="httpSnooper.jsp" %> 

   </body>
</html>

	
	

	
