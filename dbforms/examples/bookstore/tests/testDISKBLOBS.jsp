<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
  <head>
    <db:base/>
  </head>
  <body>
    <db:dbform
      multipart="true"
      autoUpdate="false"
      followUp="/tests/testDISKBLOBS.jsp"
      maxRows="1"
      tableName="BLOBTEST">
      <db:header>
        <table>
      </db:header>
      <db:body allowNew="true">
          <tr>
            <td>
              <db:textField  fieldName="NAME" /><db:file  fieldName="FILE" /><db:label  fieldName="FILE" /><db:blobContent  fieldName="FILE" />
              <!-- add -->
              <db:checkbox id="delete_image1" fieldName="delete_image1" value="true" />
              <!-- end add -->

            </td>
            <td>
              <db:updateButton
                caption="update"
              />
            </td>
          </tr>
      </db:body>
      <db:footer>
          <tr>
            <td>
              <db:textField  fieldName="NAME" /><db:file  fieldName="FILE"/>
            </td>
            <td>
              <db:insertButton
                showAlways="true"
                caption="insert"
              />
            </td>
          </tr>
        </table>
      </db:footer>
    </db:dbform>

<%@ include file="httpSnooper.jsp" %>

</body>
</html>























