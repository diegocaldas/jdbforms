<html>
	<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
	<%@ taglib uri="/WEB-INF/c-el.tld" prefix="c" %>
	<head>
		<script src ="/bookstore/jscal/calendar.js" type="text/javascript"></script>
		<db:base />
		<target="_top">
	</head>
	<body>
		First output with jstl.
		<c:out value="Test output from  jstl" />
		<db:dbform 
		autoUpdate="false"
		maxRows="1"
		tableName="LIBRARY"
		>
			<db:header>
				<h1 align="center">Edit Libraries</h1>
				<p align="center">Using simple dbform on simple table. maxRows=1; allowNew=true; autoUpdate=false</p>
				
				<table class="fixed" align="center">
				<db:errors/>
			</db:header>
			<db:body allowNew="true">
				<tr class="even">
					<td style="width:300px">ID from jstl</td>
					<td style="width:100px">
					  <!-- Access to the currentRow via JSTL [not so useful], db:label, textField, etc. are better to do this!-->
						<c:out value="${currentRow_LIBRARY.LIBRARY_ID}" />
					</td>
				</tr>
				<tr class="even">
					<td>COMPANY NAME</td>
					<td>
					<db:textField size="25" fieldName="COMPANYNAME"/>
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
			</db:body>
			<db:footer>
				</table>
			</db:footer>
		</db:dbform>
	
	<%@ include file="httpSnooper.jsp" %>
		
	</body>
</html>





