<html>
	<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
	<head>
		<script src ="/bookstore/jscal/calendar.js" type="text/javascript"></script>
		<db:base />
		<target="_top">
	</head>
	<body>
		<db:dbform
		autoUpdate="false"
		maxRows="1"
		tableName="LIBRARY"
		>
			<db:header>
				<h1 align="center">Customers of the library</h1>
				<p align="center">Using dbform Library with a nested subform Customer. maxRows=1; allowNew=true; autoUpdate=false</p>
				<table class="fixed" align="center">
				<db:errors/>
			</db:header>
			<db:body allowNew="true">
				<tr bgcolor="#dddddd" class="even">
					<td style="width:300px">ID</td>
					<td style="width:100px"><db:label fieldName="LIBRARY_ID"/>&nbsp;</td>
				</tr>
				<tr bgcolor="#dddddd"  class="even">
					<td>LIBRARY NAME</td>
					<td><db:label fieldName="COMPANYNAME"/>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align:center">
						<h2>SubForm: Customers of the library</h2>
					</td>
				</tr>
				<tr><td colspan="2" style="text-align:center">
						<db:dbform
						autoUpdate="false"
						maxRows="*"
						tableName="CUSTOMER_LIBRARY"
						parentField="LIBRARY_ID"
						childField="LIBRARY_ID"
						>
							<db:header>
								<table>
							</db:header>
							<db:body allowNew="true">
								<tr>
									<td>
										<!-- scrivo informazioni dell'autore in un hidden -->
										Library
										<!-- It is necessary to put a hidden field with the value (overrideValue) of the field(s) in PARENT-CHILD reation -->
									 <db:textField fieldName="LIBRARY_ID" hidden="true" overrideValue='<%=(String) currentRow_LIBRARY.get("LIBRARY_ID") %>' /> 
										<db:label fieldName="LIBRARY_ID"/>&nbsp;
									</td>
									<!-- A textfield to insert directly the ID of the Customer -->
									<!--
									<td>Customer<db:textField fieldName="CUSTOMER_ID"/></td>
									-->
									<!-- Creates a dropdown list to select the name of the customer, dbforms stores the CUSTOMER_ID as indicated in the attribute "storeField" -->
									<td>Customer
										<db:select fieldName="CUSTOMER_ID" >
											<db:tableData foreignTable="CUSTOMER" name="customerList" storeField="CUSTOMER_ID" visibleFields="NAME" />
										</db:select>
									
									<td>
										<db:updateButton style="width:100" caption="Save Customer"/>
										<db:deleteButton style="width:100" caption="Delete Customer"/>
										<db:insertButton style="width:100" caption="Insert Customer" showAlways="false" />
									</td>
								</tr>
							</db:body>
							<db:footer>
								<tr class="button">
									<td colspan="4" style="text-align:center">
										<db:navFirstButton style="width:100" caption="<< First Customer"/>
										<db:navPrevButton  style="width:100" caption="<  Previous Customer"/>
										<db:navNextButton  style="width:100" caption=">  Next Customer"/>
										<db:navLastButton  style="width:100" caption=">> Last Customer"/>
										<db:navNewButton   style="width:100" caption="New Customer"/>
										<db:navCopyButton  style="width:100" caption="Copy Customer"/>
										&nbsp;
									</td>
								</tr>
								<table>
							</db:footer>
						</db:dbform>
					</td></tr>
				<hr/>
				<tr class="button">
					<td colspan="2" style="text-align:center">
						<db:navFirstButton style="width:100" caption="<< First Library"/>
						<db:navPrevButton  style="width:100" caption="<  Previous Library"/>
						<db:navNextButton  style="width:100" caption=">  Next Library"/>
						<db:navLastButton  style="width:100" caption=">> Last Library"/>
						<db:navNewButton   style="width:100" caption="New Library"  showAlwaysInFooter="false"/>
						<db:navCopyButton  style="width:100" caption="Copy Library" showAlwaysInFooter="false"/>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td colspan="2">
								NOTE<br/> It is necessary to put a hidden field with the value (overrideValue) of the field(s) in PARENT-CHILD reation.<br/>
									In this case the LIBRARY_ID of the nested form is stored in a textField with hidden=true and overrideValue= (String) currentRow_LIBRARY.get("LIBRARY_ID").
									Library is the MAIN form, Customer_Library is the table on which is based the nested form.
					</td>
				</tr>
			</db:body>
			<db:footer>
				</table>
			</db:footer>
		</db:dbform>
	<%@ include file="httpSnooper.jsp" %>
	</body>
</html>
