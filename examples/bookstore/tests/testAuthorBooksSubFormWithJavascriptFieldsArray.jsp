<html>
	<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
	<head>
	   <db:base />
	</head>
   <body>
      <db:dbform autoUpdate="false" 
                 followUp="/tests/testAuthorBooksSubFormWithJavascriptFieldsArray.jsp" 
                 maxRows="1" 
                 tableName="AUTHOR" 
				 javascriptFieldsArray="true"
                 
      >
         <db:header>
	         <h1 align="center">Edit Authors</h1>
	         <db:errors/>
         </db:header>
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
<% 
	int iPos = 0;
%>			
			    <db:dbform 
		      		autoUpdate="false" 
		      		followUp="/tests/testAuthorBooksSubFormWithJavascriptFieldsArray.jsp"   
		      		maxRows="*" 
		      		tableName="BOOK"
		     		parentField="AUTHOR_ID"
		     		childField="AUTHOR_ID"	 			
			      	orderBy="ISBN" 
					javascriptFieldsArray="true" 
			    >
		         <db:header>
					<table>
				 </db:header>
		         <db:body allowNew="true">
    	           <tr>
        	          <td><db:label fieldName="BOOK_ID"/>&nbsp;</td>
<% 
	String strGetDbFormFieldName = "";
	String strOnFocus = "";
	strGetDbFormFieldName = "getDbFormFieldName('ISBN', ";
	if (currentRow_BOOK == null) {
		strGetDbFormFieldName += "null";
	}
	else{
		strGetDbFormFieldName += iPos;
	}
	strGetDbFormFieldName += ")";
	strOnFocus  = "javascript:";
	strOnFocus += "if (" +  strGetDbFormFieldName + " == this.name){";
	strOnFocus +=     "alert('OK! Vars are equal in name:\\n\\n' + ";
	strOnFocus +=     "'getDbFormFieldName=' + " + strGetDbFormFieldName +" + '\\n' + ";
	strOnFocus +=     "'this.name=' + this.name);";
	strOnFocus += "}";
	strOnFocus += "else{";
	strOnFocus +=     "alert('ERROR! Vars differ in name:\\n\\n' + ";
	strOnFocus +=     "'getDbFormFieldName=' + " + strGetDbFormFieldName +" + '\\n' + ";
	strOnFocus +=     "'this.name=' + this.name);";
	strOnFocus += "}";
	

%>
            	      <td>
						<db:textField fieldName="ISBN" overrideValue="Click here to check!" onClick="<%=strOnFocus%>"/>
					  	<db:textField fieldName="ISBN"/>&nbsp;
					  </td>
                	  <td><db:textField fieldName="TITLE" /> </td>
                	  <td>
							 <db:updateButton style="width:100" caption="Save"/>
							 <db:deleteButton style="width:100" caption="Delete"/>
							 <db:insertButton style="width:100" caption="Insert" showAlways="false" />
					  </td>
	               </tr>			              		 				
<% iPos++; %>							   
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
         </db:body>
         <db:footer>
           </table>
         </db:footer>
      </db:dbform>

<%@ include file="httpSnooper.jsp" %> 

   </body>
</html>

	
	

	
