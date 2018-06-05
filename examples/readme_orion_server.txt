
======================================================
USING ORION APPLICATION SERVER 
(www.orionserver.com, free developer license!)
======================================================


Using orion you would need to edit your  {$ORION}/config/data-sources.xml
- file
------------------------------------------------------------------------


* using MYSQL with their driver available from www.mysql.com you would add
some thing like this:
	
	<data-source 
		name="dbforms example database"
		class="com.evermind.sql.ConnectionDataSource"
		location="jdbc/dbformstest"
		pooled-location="jdbc/dbformstest"
		url="jdbc:mysql:///dbformstest"
		connection-driver="org.gjt.mm.mysql.Driver"
		username=""
		password=""
	/>		
	

* using MS SQL server with a commercial JDBC driver you would add
some thing like this:
	
	<data-source 
		name="dbforms example database"
		class="com.evermind.sql.ConnectionDataSource"
		location="jdbc/dbformstest"
		pooled-location="jdbc/dbformstest"		
		url="jdbc:AvenirDriver://itp011:1433/dbformstest"
		connection-driver="net.avenir.jdbc2.Driver"
		username="sa"
		password=""
	/>	
	
	

Your {$ORION}/example/WEB-INF/dbforms-config.xml - file would contain
a dbconnection tag as follows:
------------------------------------------------------------------------

  <dbconnection 
  	name = "jdbc/dbformstest"
	isJndi = "true"  	  	
  />

	
	
