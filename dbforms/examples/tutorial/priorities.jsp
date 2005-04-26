<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
<head>
  <db:base/>
</head>

<body>
<db:errors/>

<db:dbform tableName="priorities" maxRows="*" 
  followUp="/priorities.jsp" autoUpdate="true">

  <db:header>                            
    <db:gotoButton caption="Menu" destination="/menu.jsp" />
    <h1>Priority Definitions</h1>
    <center><h3>Existing Priority Definitions</h3></center>
    <table border="5" width="60%" align="CENTER">
    <tr>
      <th>Id</th>
      <th>Short Name</th>
      <th>Description</th>
      <th>Actions</th>
    </tr>                        
  </db:header>

  <db:body>
    <tr>
      <td><db:label fieldName="id"/></td>
      <td><db:textField fieldName="shortname"/></td>
      <td><db:textField fieldName="description"/></td>
      <td>
        <db:updateButton caption="update"/>
        <db:deleteButton caption="delete"/>
      </td>
    </tr>
  </db:body>

  <db:footer>
    </table>

    <center><h3>Enter New Priority Definition</h3></center>

    <table border="3" align="center">
    <tr>
      <td>Id</td>
      <td><db:textField size="3" fieldName="id"/></td>
    </tr>
    <tr>
      <td>Short-Name</td>
      <td><db:textField fieldName="shortname"/></td>
    </tr>
    <tr>
      <td>Description</td>
      <td><db:textArea rows="3" cols="20" 
                       fieldName="description"/></td>
    </tr>
    </table>

    <br>
    <center>                                    
      <db:insertButton caption="Store New Priority Definition"
                       showAlways="true"/>
    </center>
  </db:footer>

</db:dbform>
</body>
</html>    
