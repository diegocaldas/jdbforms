<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<html>
   <head>
      <db:base/>
   </head>
   <body>
      <db:dbform
      multipart="true"
      autoUpdate="false"
      followUp="/tests/testBLOBINTERCEPTOR.jsp"
      maxRows="*"
      tableName="BLOBINTERCEPTORTEST">
         <db:header>
            <table>
            </db:header>
            <db:body allowNew="false">
               <tr>
                  <td>
                     <db:textField  fieldName="NAME" />
                     <db:label  fieldName="FILENAME" />
                     <db:file  fieldName="FILEDATA" />
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
                     <db:textField  fieldName="NAME" />
                     <db:file  fieldName="FILEDATA"/>
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
