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
      maxRows="*"
      tableName="BLOBTEST">
         <db:header>
            <table>
            </db:header>
            <db:body allowNew="false">
               <tr>
                  <td>
                     <db:textField  fieldName="NAME" />
                     <db:file  fieldName="FILE" />
                     <db:label  fieldName="FILE" />
                     <db:checkbox id="delete_image1" fieldName="delete_image1" value="true" />
                  </td>
                  <td>
                    <db:updateButton
                caption="update"
              />
                    <db:navCopyButton
                caption="copy"
              />
                  </td>
               </tr>
            </db:body>
            <db:footer>
               <tr>
                  <td>
                     <db:textField  fieldName="NAME" />
                     <db:file  fieldName="FILE"/>
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
