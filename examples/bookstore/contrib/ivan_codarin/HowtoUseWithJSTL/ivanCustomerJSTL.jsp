<html>
	<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
		<%@ taglib uri="/WEB-INF/c-el.tld" prefix="c" %>
	<head>
		<script src ="/jscal/calendar.js" type="text/javascript"></script>
		<db:base />
	</head>
	<body>
		<db:dbform 
		autoUpdate="false"
		maxRows="1"
		tableName="CUSTOMER"
		>
			<db:header>
				<h1 align="center">Edit Customers</h1>
				<p align="center">Using simple dbform on simple table. maxRows=1; allowNew=true; autoUpdate=false</p>
				
				<table class="fixed"  align="center">
				<db:errors/>
			</db:header>
			<db:body allowNew="true">
			
			<c:if test="${currentRow_CUSTOMER.CUSTOMER_ID %2 == 1}" >
				<c:set  var="backcolor" value="#dddddd" />
			</c:if>
			<c:if test='${currentRow_CUSTOMER.CUSTOMER_ID %2 == 0}'>
				<c:set  var="backcolor" value="#999999" />
			</c:if>
			
				<tr class="even" bgcolor=<c:out value="${backcolor}"/> >
					<td style="width:300px">ID</td>
					<td style="width:100px">
						<c:out value="${currentRow_CUSTOMER.CUSTOMER_ID}" />
					</td>
				</tr>
				<tr class="even" bgcolor=<c:out value="${backcolor}"/> >
					<td>CUSTOMER NAME</td>
					<td><db:textField size="25" fieldName="NAME"/>
					</td>
				</tr>
				<tr class="even" bgcolor=<c:out value="${backcolor}"/> class="button">
					<td colspan="2" style="text-align:center">
						<db:updateButton style="width:100" caption="Save"/>
						<db:deleteButton style="width:100" caption="Delete"/>
						<db:insertButton style="width:100" caption="Insert" showAlways="false" />
					</td>
				</tr>
				<tr>
					<td colspan="2"><hr/></td>
				</tr>
			</db:body>
			<db:footer>
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

				</table>
			</db:footer>
		</db:dbform>
	
	<%@ include file="httpSnooper.jsp" %>
		
	</body>
</html>





