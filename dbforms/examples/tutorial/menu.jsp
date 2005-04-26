<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
<head>
  <db:base/>
</head>

<body>

  <h1>Menu</h1>

  <db:dbform followUp="/menu.jsp">
  <center>
  <p>
    <db:gotoButton caption="Edit Services Table"
                   destination="/services.jsp"/>
  </p>
  <p>
    <db:gotoButton caption="Edit Priority Table"
                   destination="/priorities.jsp"/>
  </p>
  <p>
    <db:gotoButton caption="Customer List"
                   destination="/customer_list.jsp"/>
  </p>
  <p>
    <db:gotoButton caption="Browse Customer Orders"
                   destination="/customer_orders.jsp"/>
  </p>
  <p>
    <db:gotoButton caption="Browse Customer Complaints"
                   destination="/customer_complaints.jsp"/>
  </p>
  <p>
    <db:gotoButton caption="Problem with Navigation"
                   destination="/customer_all.jsp"/>
  </p>
  <p>
    <db:gotoButton caption="Navigation Works OK"
                   destination="/customer_all_OK.jsp"/>
  </p>
  </center>
  </db:dbform>

</body>
</html>
