<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db"%>
<%@ taglib uri="/WEB-INF/cewolf.tld" prefix="cewolf"%>
<html>
<head>
<db:base />
</head>
<body>
<db:dbform multipart="false" autoUpdate="false"
	followUp="/howto/howtoMakeaPieChart.jsp" maxRows="*"
	tableName="BOOKCOUNT">
	<db:header>
		<db:errors />
		<h1>Data</h1>
		<table>
			</db:header>
			<db:body allowNew="false">
				<tr>
					<td><db:label fieldName="NAME" />&nbsp;</td>
					<td><db:label fieldName="C" />&nbsp;</td>
				</tr>
			</db:body>
			<db:footer>
		</table>
		<h1>pi chart example</h1>
		<cewolf:chart id="pieChart" title='Books per Author' type="pie3D">
			<cewolf:data>
				<db:pieData categoryField="NAME" dataField="C" />
			</cewolf:data>
		</cewolf:chart>
		<cewolf:img chartid="pieChart" renderer="/cewolf" width="800"
			height="400" />

		<h1>category chart example</h1>
		<cewolf:chart id="categoryChart" title='Books per Author'
			type="verticalbar3d">
			<cewolf:data>
				<db:categoryData seriesField="NAME" dataField="C" />
			</cewolf:data>
		</cewolf:chart>
		<cewolf:img chartid="categoryChart" renderer="/cewolf" width="800"
			height="400" />

		</db:footer>
</db:dbform>
</body>
</html>























