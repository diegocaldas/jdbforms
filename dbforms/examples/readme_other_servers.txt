
======================================================
USING A WEB-SERVER (with no built in db connection pools)
for example Tomcat, available from
http://jakarta.apache.org


WITH A 3rd PARTY CONNECTION POOL MANAGER
for example PoolMan, available from
http://sourceforge.net/projects/poolman/
======================================================


Using poolman you would need to edit your  poolman.props - file
------------------------------------------------------------------------


* using MS SQL  you would add some thing like this to poolman.props
(BTW, make sure that poolman.props is in classpath)
	
db_name.n=dbformstest
db_driver.n=net.avenir.jdbc2.Driver
db_url.n=jdbc:AvenirDriver://testserver:1433/dbformstest
db_username.n=sa
db_password.n=
initialConnections.n=8
connection_timeout.n=15000
maximumsize.n=20	

(you would have to replace "n" by a valid number of a free slut of the
connection pool manager)	


	

Your ${SERLVET_ENGINE}/example/WEB-INF/dbforms-config.xml - file would contain
a dbconnection tag as follows:
------------------------------------------------------------------------

  <dbconnection
        name = "jdbc:poolman://dbformstest"
                isJndi = "false"
        class = "com.codestudio.sql.PoolMan"
  />

	