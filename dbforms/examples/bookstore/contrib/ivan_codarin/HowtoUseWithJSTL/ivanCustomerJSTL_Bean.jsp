<html>
	<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
	<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
		
	<jsp:useBean id="backcolor" scope="page" class="net.myPackage.IvanBackgroudColor"/>
	<!-- To use the bean in JSTL you have to define a variable with jstl c:set var filled with the value of the bean.
	     In this way later you could access to the properties of that bean -->
	<c:set var="backcolor" value="${backcolor}" />
	<c:set var="currentRow_CUSTOMER" value="${currentRow_CUSTOMER}"/>
	
	<head>
		<script src ="/bookstore/jscal/calendar.js" type="text/javascript"></script>
		<db:base />
		<target="_top">
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
			
				<br/>
				<c:out value="${currentRow_CUSTOMER}" />			
				<br/>
				OUTPUT with dbformsContext


				<br/>Setting the idNumber via scripting;
				<%
					backcolor.setIdNumber((int) Integer.parseInt((String)currentRow_CUSTOMER.get("CUSTOMER_ID")));
					out.println("<br/> Test current ID: " + (String)currentRow_CUSTOMER.get("CUSTOMER_ID"));
				%>
				<c:out value="${backcolor.idNumber}" />
	
				<br/>
				
				<!-- sets the idNumber property of the bean via JSTL SYNTAX -->
				Setting the idNumber via jstl syntax:
				<c:set target="${backcolor}" property="idNumber" value="${currentRow_CUSTOMER.CUSTOMER_ID}" />
				<c:out value="${backcolor.idNumber}" />			

				<br/>getBgColor via scripting: 
				<%=backcolor.getBgColor() %>		
				<c:out value="${backcolor.bgColor}" />
				
				<!-- sets the value of the bean via BEAN syntax -->
				<jsp:setProperty name="backcolor" property="idNumber" value="3" />
				<br/>Id Number setted by setProperty  fixed value 3- BEAN SYNTAX -
				<c:out value="${backcolor.idNumber}" />

				<!-- sets the value of the bean via JSTL syntax -->				
				<br/>Id Number setted by jstl set  fixed value 4-JSTL SYNTAX, with previous initializaziton -
				<c:set target="${backcolor}" property="idNumber" value="4" />
				<c:out value="${backcolor.idNumber}" />
				

				<tr class="even" bgcolor="<c:out value="${backcolor.bgColor}"/>" >
					<td style="width:300px">ID</td>
					<td style="width:100px">
					  JSTL_CurrentRow_ID:
						<c:out value="${currentRow_CUSTOMER.CUSTOMER_ID}" />
						&nbsp;&nbsp;&nbsp;
						JSTL_DbFormsContext_ID
						<c:out value="${dbforms.CUSTOMER.currentRow.CUSTOMER_ID}" />
						&nbsp;&nbsp;&nbsp;
						DBFORMS_ID:
						<db:label fieldName="CUSTOMER_ID" />
					</td>
				</tr>
				<tr class="even" bgcolor="<c:out value="${backcolor.bgColor}"/>" >
					<td>CUSTOMER NAME</td>
					<td><db:textField size="25" fieldName="NAME"/>
					</td>
				</tr>
				<tr class="even" bgcolor="<c:out value="${backcolor.bgColor}"/>" class="button">
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
					<td colspan="2" style="text-align:center"><db:navFirstButton style="width:100" caption="<< First"/>
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
