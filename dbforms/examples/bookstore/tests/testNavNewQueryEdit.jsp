<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
	<head>
		<db:base/>
	</head>
	<body>
		<db:dbform 
			multipart="false" 
			autoUpdate="false" 
			followUp="/tests/testNavNewQueryEdit.jsp" 
			maxRows="1" tableName="BOOK_QUERY">
			<db:header>
				<table>
			</db:header>
			<db:body>
				<tr>
					<td><db:textField fieldName="BOOK_ID"/></td>
					<td><db:textField fieldName="ISBN"/></td>
					<td><db:textField fieldName="AUTHOR_ID"/></td>
					<td><db:textField fieldName="TITLE"/></td>
				</tr>
			</db:body>
			<db:footer>
				</table>
				<table>
					<tr>
						<td>
		        	         <db:updateButton style="width:100" caption="Save"/>
        		    	     <db:deleteButton style="width:100" caption="Delete"/>
			                 <db:insertButton style="width:100" caption="Insert" showAlways="false" />
						</td>
					</tr>
				</table>
			</db:footer>
		</db:dbform>

<%@ include file="httpSnooper.jsp" %> 

	</body>
</html>    

