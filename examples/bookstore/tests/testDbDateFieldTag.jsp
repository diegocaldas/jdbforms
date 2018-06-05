<%@ taglib
   uri="/WEB-INF/dbforms.tld"
   prefix="db"%>

<html>
   <head>
      <db:base />
      <!-- main calendar program -->
      <script
         type="text/javascript"
         src="<%=org.dbforms.util.Util.getBaseURL(request) + "dbformslib/jscal/calendar.js" %>"
      >
      </script>
   </head>
   <body>




      <db:dbform
         tableName="TIMEPLAN"
         maxRows="*"
         autoUpdate="true"
         captionResource="false">
         <db:header>
            <table>
               </db:header>
               <db:body>
                  <tr>
                     <td>


                        A Date
                        <db:dateField
                           fieldName="TIME"
                           useJsCalendar="true"
                           pattern="yyyy-MM-d-EEEE-hh" />


                     </td>
                  </tr>
               </db:body>
               <db:footer>
            </table>
            </db:footer>
      </db:dbform>


   </body>
</html>
