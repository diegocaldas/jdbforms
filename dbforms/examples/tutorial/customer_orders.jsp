<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
<head>
  <db:base/>
</head>

<body>

  <!-- show any database errors here -->
  <db:errors/>

  <db:dbform tableName="customers" maxRows="1"
             followUp="/customer_orders.jsp" autoUpdate="false">

  <db:header>
    <db:gotoButton caption="Menu" destination="/menu.jsp" />
    <h1>Customer Orders</h1>
  </db:header>

  <db:body>
    <table align="center">
    <tr>
      <td>Id</td>
      <td>
        <db:textField fieldName="id" size="4"/>
      </td>
    </tr>
    <tr>
      <td>First Name</td>
      <td>
        <db:textField fieldName="firstname" size="18"/>
      </td>
    </tr>
    <tr>
      <td>Last Name</td>
      <td>
        <db:textField fieldName="lastname" size="18"/>
      </td>
    </tr>
    <tr>
      <td>Address:</td>
      <td>
        <db:textField fieldName="address" size="25" />
      </td>
    </tr>
    <tr>
      <td>Postal Code - City</td>
      <td>
        <db:textField fieldName="pcode" size="6"/> -
        <db:textField fieldName="city" size="16"/>
      </td>
    </tr>
    </table>
    <br>

    <!-- table embedding the sub form -->
    <table align="center" border="1">
    <tr>
      <td>
        <center><p><b>Orders</b></p></center>

        <!-------- sub form begin ------->
        <db:dbform tableName="orders" maxRows="2" 
                   parentField="id" childField="customer_id" 
                   followUp="/customer_orders.jsp"
                   autoUpdate="false">

          <db:header>
            <!-- Show existing orders of services for the customer -->
            <table width="100%">
            <tr>
              <td width="40"></td>
              <td>Service</td>
              <td>Order Date</td>
            </tr>
          </db:header>

          <db:body allowNew="true">
            <tr>
              <td width="40">
                <db:associatedRadio name="radio_order"/>
              </td>
              <td>
                <db:select fieldName="service_id">
                  <db:tableData
                    name          = "our_services"
                    foreignTable  = "services"
                    visibleFields = "name"
                    storeField    = "id"
                  />
                </db:select>
              </td>
              <td>
                <db:textField fieldName="orderdate" size="14"/>
              </td>
            </tr>
          </db:body>

          <db:footer>
            <tr>
              <td colspan="3" align="CENTER">
                <db:updateButton caption="Update Order"
                                 associatedRadio="radio_order"/>
                <db:deleteButton caption="Delete Order"
                                 associatedRadio="radio_order"/>
                <db:insertButton caption="Store New Order"
                                 showAlways="false" />
                <db:navNewButton caption="New Order" 
                                 showAlwaysInFooter="false" />
              </td>
            </tr>
            </table>

            <br>
            <center>
              <db:navFirstButton caption="&lt;&lt; First" />
              <db:navPrevButton caption="&lt; Previous" />
              <db:navNextButton caption="Next &gt;" />
              <db:navLastButton caption="Last &gt;&gt;" />
            </center>

          </db:footer>

        </db:dbform>
        <!-------- sub form end -------->

      </td>
    </tr>
    </table>
    <!-- end of table embedding the sub form -->

    <br>
    <center>
      <db:insertButton caption="Store New Customer" />
      <db:updateButton caption="Update Customer" />
      <db:deleteButton caption="Delete Customer" />
      <db:navNewButton caption="New Customer" 
                       showAlwaysInFooter="false" />
    </center>
  </db:body>

  <db:footer>
    <br>
    <center>
      <db:navFirstButton caption="&lt;&lt; First" />
      <db:navPrevButton caption="&lt; Previous" />
      <db:navNextButton caption="Next &gt;"  />
      <db:navLastButton caption="Last &gt;&gt;" />
    </center>
  </db:footer>

  </db:dbform>
</body>
</html>

