<html>
	<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
	<head>
	   <db:base />
	   <target="_top">
	</head>
   <body>
   
      <db:dbform autoUpdate="false" 
                 followUp="/tests/testAuthorBooksSubFormWithFilter.jsp" 
                 maxRows="1" 
                 tableName="AUTHOR"
      >
   		   <input type="hidden" name="customEvent" value="re_0_0"/>
		 <db:header>
	         <h1 align="center">Edit Authors</h1>

            <table class="fixed" align="center">
	         <db:errors/>
         </db:header>
         <db:body allowNew="false">
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
		      		followUp="/tests/testAuthorBooksSubFormWithFilter.jsp"   
		      		maxRows="*" 
		      		tableName="BOOK_QUERY"
		     		parentField="AUTHOR_ID"
		     		childField="AUTHOR_ID"	 			
			      	orderBy="ISBN" 
			    >
		         <db:header>		        
					<table>
					<tr>
						<td colspan="4">
							Subform filter:&nbsp;							
							<db:filter>
								<db:filterCondition label="title like">
									TITLE LIKE ?
									<db:filterValue/>
								</db:filterCondition>
								<db:filterCondition label="title not unique">
									(SELECT COUNT(*) FROM BOOK B1 WHERE B1.TITLE = BOOK.TITLE) > 1
								</db:filterCondition>
							</db:filter>
						</td>
					</tr>
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
         </db:body>
         <db:footer>
               <tr class="button">
               <td colspan="2">
 			   main form form filter:&nbsp;							
	         <db:filter>
	         	<db:filterCondition label="author name like">
	         		NAME LIKE ?
	         		<db:filterValue />
	         	</db:filterCondition>
	         	<db:filterCondition label="ID >= V1 AND ID <= V2">
	         		AUTHOR_ID >= ? AND AUTHOR_ID <= ?
	         		<db:filterValue label="V1" type="numeric"/>
	         		<db:filterValue label="V2" type="numeric"/>
	         	</db:filterCondition>
	         	<db:filterCondition label="author is">
	         		NAME = ?
	         		<db:filterValue type="select">
	         			<db:queryData name="q1" query="select distinct name as n1, name as n2 from author where AUTHOR_ID < 100 order by name"/>
	         		</db:filterValue>
	         	</db:filterCondition>
	         	<db:filterCondition label="author who write more than N books">
	         		(SELECT COUNT(*) FROM BOOK WHERE BOOK.AUTHOR_ID = AUTHOR.AUTHOR_ID) > ?
	         		<db:filterValue type="select" customEntry="10,10,true">
	         			<db:staticData name="p1">
	         				<db:staticDataItem key="1" value="0"/>
	         				<db:staticDataItem key="1" value="1"/>
	         				<db:staticDataItem key="2" value="2"/>
	         				<db:staticDataItem key="3" value="3"/>
	         				<db:staticDataItem key="4" value="4"/>
	         			</db:staticData>
	         		</db:filterValue>
	         	</db:filterCondition>
	         	<db:filterCondition label="now is after date">
	         		CURRENT_DATE > ?
	         		<db:filterValue type="date" useJsCalendar="true" />
	         	</db:filterCondition>
	         	<db:filterCondition label="now is after timestamp">
	         		CURRENT_TIMESTAMP > ?
	         		<db:filterValue type="timestamp" />
	         	</db:filterCondition>
	         	<db:filterCondition label="filter without user input">
	         		AUTHOR_ID > 1
	         	</db:filterCondition>
	         </db:filter>
	           </td>
               </tr>
           </table>
         </db:footer>
      </db:dbform>

<%@ include file="httpSnooper.jsp" %>

   </body>
</html>

	
	

	
