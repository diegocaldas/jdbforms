<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
	<head>
		<db:base/>
	</head>
	<body>
		<db:dbform 
			autoUpdate="false" 
			followUp="/tests/testBOOKSSingle.jsp" 
			maxRows="1" tableName="BOOK">
			<db:header>
				<db:errors/>
				<table>
			</db:header>
			<db:body>
				<tr>
					<td><db:textField fieldName="BOOK_ID"/></a>&nbsp;</td>
					<td><db:textField fieldName="ISBN"/>&nbsp;</td>
					<td><db:textField fieldName="AUTHOR_ID"/>&nbsp;</td>
					<td><db:textField fieldName="TITLE"/>&nbsp;</td>
				</tr>
			</db:body>
			<db:footer>
				</table>
				<table>
		            <tr class="button">
        		       <td >
		               	 <db:updateButton caption="Save"/>
        		         <db:deleteButton caption="Delete"/>
                		 <db:insertButton caption="Insert" showAlways="false" />
		               </td>
        		    </tr>
					<tr>
						<td>
							<db:navFirstButton caption="first"/>
							<db:navPrevButton  caption="previous"/>
							<db:navNextButton  caption="next"/>
							<db:navLastButton  caption="last"/>
							<db:navReloadButton  caption="reload"/>
		                	<db:navNewButton   caption="New" showAlwaysInFooter="false" />
		                </td>
					</tr>
				</table>
			</db:footer>
		</db:dbform>

<%@ include file="httpSnooper.jsp" %>

	</body>
</html>    

