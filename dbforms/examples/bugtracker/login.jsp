<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>



<html>

<head>	
  <db:base/>
	<link rel="stylesheet" href="dbforms.css">
</head>

<body>



	<db:style template="sourceforge" part="begin" paramList="width='300'"/>
	  <db:style template="center" part="begin" />
	  
		<!-- code for servlet 2.2 compliant web server		
		<form action="j_security_check">
		-->
		
	  <!-- code for orion app-server -->
		<form>
		
			<table align="center">
				<tr>
					<td>
						<b>Username: </b><br><input name="j_username" type="text"><br>
					</td>
				</tr>
				<tr>
					<td>
						<b>Password: </b><br><input name="j_password" type="password"><br>
					</td>
				</tr>
				<tr>
					<td align="center">
						<br><input type="submit" value="Log in!" name="j_security_check" >						
					</td>
				</tr>
			</table>
		</form>
		

		
	  <db:style template="center" part="end" />
	<db:style template="sourceforge" part="end"/>
	  	  
	  

	  
	  
  

</body>
</html>