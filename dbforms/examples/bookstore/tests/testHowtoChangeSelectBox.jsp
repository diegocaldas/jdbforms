<html>
	<head>
		<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
	</head>
    <body>
	    <h1>
 		 Shows howto change selection from one select/search box while changing the value of another selectbox
	    </h1>
	      <db:dbform  
	      		maxRows="*" 
	         	followUp="/tests/howtoChangeSelectBox.jsp" 
    	     	autoUpdate="false" tableName="BOOKLISTPERAUTHOR"
	      		>
         		   <!--- To force an reload event.
         			   This is necessary because we have no given table
           			   and needs and reload event to refetch the values of the
         			   select boxes!
         			   Is first parameter here because we do not have a tablename. 
         			   In this special case the hidden input field customEvent is not 
         			   rendered!
         		   
          		   <input type="hidden" name="customEvent" value="re_0_0"/>
                            -->
               <db:header/>
               <db:body>                          
					<db:select
						fieldName="books"
                        onChange="this.form.submit();"
					>
						<db:tableData
						   name="books"
						   foreignTable="book"
						   visibleFields="title, author_id"
						   storeField="book_id"
						   format="%s"
						/>
				   	</db:select>
					<db:select 
						fieldName="author"
					>
						<db:queryData
							name="author"
							query='<%=  "SELECT DISTINCT AUTHOR_ID, NAME " + 
										"FROM AUTHOR, BOOK " +
										"WHERE " + 
										"AUTHOR.AUTHOR_ID = BOOK.AUTHOR_ID" + 
										" AND " +
										"BOOK.BOOK_ID = " +
										org.dbforms.util.ParseUtil.getParameter(request, "books", "-1")
								   %>'
							format="%s"
						/>
				   </db:select>
              </db:body>
               <db:footer/>
		</db:dbform>
   </body>
</html>
