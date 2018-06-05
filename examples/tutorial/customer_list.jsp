<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
<head>                    
  <db:base/>
</head>                
<body>                    
  <db:errors/>                                       

  <db:dbform tableName="customers" maxRows="*" 
             followUp="/customer_list.jsp"
             autoUpdate="false">

    <db:header>
    <db:gotoButton caption="Menu" destination="/menu.jsp" />
      <h1>Customer List</h1>
      <table align="center" cellspacing="6">
      <tr>
        <td><b></b></td>
        <td><b>First Name</b></td>
        <td><b>Last Name</b></td>
        <td><b>Address</b></td>
        <td><b>P-Code</b></td>
        <td><b>City</b></td>
        <td><b>Action</b></td>
      </tr>                       
    </db:header>                                                

    <db:body allowNew="false">
      <tr>
        <td><db:associatedRadio name="r_customerkey" /></td>
        <td><db:label fieldName="firstname"/></td>
        <td><db:label fieldName="lastname"/></td>                                
        <td><db:label fieldName="address"/></td>
        <td><db:label fieldName="pcode"/></td>
        <td><db:label fieldName="city"/></td>
        <td><db:deleteButton caption="delete"/></td>
      </tr>
    </db:body>                        

    <db:footer>
      </table>
      <p>
      <center>

      Show

      <db:gotoButton caption="Orders" 
                     destination="/customer_orders.jsp" 
                     destTable="customers" 
                     keyToKeyToDestPos="r_customerkey"/>

      <db:gotoButton caption="Complaints " 
        destination="/customer_complaints.jsp"
        destTable="customers" 
        keyToKeyToDestPos="r_customerkey"/>

      <db:gotoButton caption="All Information "
                     destination="/customer_all.jsp" 
                     destTable="customers"
                     keyToKeyToDestPos="r_customerkey"/>

      of the selected customer!

      </center>
      </p>                        
    </db:footer>

  </db:dbform>
</body>
</html>
