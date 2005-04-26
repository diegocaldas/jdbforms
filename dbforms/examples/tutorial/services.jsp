<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>            
<head>
  <db:base/>
</head>

<body>
  <db:errors/>                                                            

  <db:dbform tableName="services" maxRows="*" 
             followUp="/services.jsp">

    <db:header>                        
      <db:gotoButton caption="Menu" destination="/menu.jsp" />
      <h1>Services We Provide</h1>
      <center>
        <h3>Existing Service Definitions</h3>
      </center>

      <table border="5" width="60%" align="CENTER">
      <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Description</th>
        <th>Actions</th>
      </tr>                                                   
    </db:header>

    <db:body>
    <tr>
      <td>
        <db:textField fieldName="id" size="5"/>
      </td>
      <td>
        <db:textField fieldName="name" size="20" 
                      maxlength="30"/>
      </td>
      <td>
        <db:textField fieldName="description" size="24" 
                      maxlength="255"/>
      </td>
      <td>                                    
        <db:updateButton caption="Update"/>
        <db:deleteButton caption="Delete"/>
      </td>
    </tr>
    </db:body>

    <db:footer>
    </table>

    <center><h3>Enter New Service Definition</h3></center>

    <table align="center" border="3">    
    <tr>
      <td>Id</td>
      <td>
        <db:textField size="5" fieldName="id"/>
      </td>
    </tr>
    <tr>
      <td>Name</td>
      <td>
        <db:textField size="20" maxlength="30" fieldName="name"/>
      </td>
    </tr>
    <tr>
      <td>Description</td>
      <td>         
        <db:textArea rows="4" cols="20" wrap="virtual" 
                     fieldName="description"/>
      </td>
    </tr>
    </table>
    <br>
    <center>
      <db:insertButton caption="Store New Service Definition"
                       showAlways="true"/>
    </center>
    </db:footer>
  </db:dbform>
</body>
</html>    
