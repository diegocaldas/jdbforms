<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
<head>
  <db:base/>
</head>

<body>

  <!-- show any database errors here -->
  <db:errors/>

  <db:dbform tableName="customers" maxRows="1"
             followUp="/customer_all_OK.jsp">

  <db:header>
    <db:gotoButton caption="Menu" destination="/menu.jsp" />
    <h1>All Customer Information</h1>
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
    <!-- table embedding the first sub form -->
    <table align="center" border="1">
    <tr>
      <td>
        <center><p><b>Orders</b></p></center>

        <!-------- first sub form begin ------->
        <db:dbform tableName="orders" 
                   maxRows="2" parentField="id"
                   childField="customer_id" 
                   followUp="/customer_all_OK.jsp">

          <db:header>
            <!-- Show existing orders of services for the customer -->
            <table width="100%">
            <tr>
              <td>Service</td>
              <td>Order Date</td>
            </tr>
          </db:header>

          <db:body allowNew="false">
            <tr>
              <td>
                <db:textField fieldName="service_id"/>
              </td>
              <td>
                <db:textField fieldName="orderdate" size="14"/>
              </td>
            </tr>
          </db:body>

          <db:footer>
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
        <!-------- first sub form end -------->

      </td>
    </tr>
    </table>
    <!-- end of table embedding the first sub form -->

    <br>
    <!-- table embedding the second sub form -->
    <table align="center" border="1">
    <tr>
      <td>
        <center><p><b>Complaints</b></p></center>

        <!-------- second sub form begin ------->
        <db:dbform tableName="complaints" maxRows="2" 
                   parentField="id" childField="customer_id" 
                   followUp="/customer_all_OK.jsp">

          <db:header>
            <!-- Show existing complaints of the customer -->
            <table>
            <tr>
              <td valign="top">User's Message</td>
              <td>Priority<br/><db:sort fieldName="priority"/></td>
              <td>Incoming Date<br/><db:sort 
                                      fieldName="incomingdate"/></td>
            </tr>
          </db:header>

          <db:body allowNew="false">
            <tr>
              <td valign="top">
                <db:textArea fieldName="usermessage" 
                             cols="32" rows="2" wrap="virtual"/>
              </td>
              <td valign="top">
                <db:select fieldName="priority">
                  <db:tableData
                    name          = "some_priorities"
                    foreignTable  = "priorities"
                    visibleFields = "shortname"
                    storeField    = "id"
                  />
                </db:select>
              </td>
              <td valign="top">
                <db:textField fieldName="incomingdate" size="14"/>
              </td>
            </tr>
          </db:body>

          <db:footer>
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
        <!-------- second sub form end -------->

      </td>
    </tr>
    </table>
    <!-- end of table embedding the second sub form -->


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

