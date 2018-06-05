<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %> 
<html>
	<head>
		<script type="text/javascript">
		<!--
			function testIfChanged(aControl) {
			   if(aControl.value != aControl.form["o" + aControl.name].value) {
			      aControl.className = "changed";
			   }
			   else {
			      aControl.className = "";
			   }
			   return true;
			}
		//-->
		</script>
		<style type="text/css">
		<!--
			.changed {
				background-color:#00FF40;
			}
		-->
		</style>
		<db:base/>
	</head>
	<body>
		<db:dbform 
			multipart="false" 
			autoUpdate="true" 
			followUp="/tests/testBOOKSListAutoUpdate.jsp" 
			javascriptValidation="true"
			formValidatorName="BOOK"
			maxRows="*" 
			tableName="BOOK"
		>
			<db:header>
				<db:errors/>  
				<table>
			</db:header>
			<db:body allowNew="true">
				<tr>
				    <td>
				       <db:dataLabel fieldName="BOOK_ID"/>&nbsp;
				    </td>
					<td>
					   <db:textField fieldName="ISBN" onChange="testIfChanged(this)"/>&nbsp;
					</td>
					<td>
					   <db:textField fieldName="AUTHOR_ID" onChange="testIfChanged(this)"/>&nbsp;
					</td>
					<td>
					   <db:textField fieldName="TITLE" onChange="testIfChanged(this)"  />&nbsp;
					</td>
					<td>
						<db:deleteButton style="width:55" caption="Delete" confirmMessage="Really?"/>
					</td>
				</tr>
			</db:body>
			<db:footer>
				<tr>
					<td><db:textField hidden="true" fieldName="BOOK_ID"/>&nbsp;</td>
					<td><db:textField fieldName="ISBN"/>&nbsp;</td>
					<td><db:textField fieldName="AUTHOR_ID"/>&nbsp;</td>
					<td><db:textField fieldName="TITLE"/>&nbsp;</td>
					<td>								
						<db:insertButton showAlways="true" caption="Add New"/>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center">
						<db:updateButton caption="update" />
					</td>
				</tr>
				</table>
			</db:footer>
		</db:dbform>

<%@ include file="httpSnooper.jsp" %>

</body>
</html>    
