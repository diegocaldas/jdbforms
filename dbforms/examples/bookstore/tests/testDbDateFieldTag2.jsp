<%@ taglib
   uri="/WEB-INF/dbforms.tld"
   prefix="db"%>

<html>
   <head>
      <db:base />

      <!-- calendar stylesheet -->
      <link
         rel="stylesheet"
         type="text/css"
         media="all"
         href="<%=org.dbforms.util.Util.getBaseURL(request) + "dbformslib/jscal2/calendar-win2k-cold-1.css" %>"
         title="win2k-cold-1" />

      <!-- main calendar program -->
      <script
         type="text/javascript"
         src="<%=org.dbforms.util.Util.getBaseURL(request) + "dbformslib/jscal2/calendar.js" %>">
     </script>

      <!-- language file for the calendar -->
      <script
         type="text/javascript"
         src="<%=org.dbforms.util.Util.getBaseURL(request) + "dbformslib/jscal2/lang/calendar-en.js" %>"
      </script>
      <!-- calendar helper functions -->
      <script
         type="text/javascript"
         src="<%=org.dbforms.util.Util.getBaseURL(request) + "dbformslib/jscal2/calendar-helper.js" %>"
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
                           classicCalendar="false"
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
