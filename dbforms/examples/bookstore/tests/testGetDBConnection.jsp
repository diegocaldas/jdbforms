<!--DOCTYPE html
	PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"-->
<%@ taglib uri="/WEB-INF/dbforms.tld" prefix="db" %>
<%@ taglib uri="/WEB-INF/sql.tld" prefix="sql" %>
<%@ taglib uri="/WEB-INF/c-rt.tld" prefix="c" %>
<head>
   <db:base/>
</head>
<html>
   <body>
      <db:setDataSource dataSource="con" />
      <sql:query dataSource="${con}" var="qry">
         select * from BOOK
      </sql:query>
      <table border="1">
         <c:forEach var="row" items="${qry.rows}">
            <tr>
               <td>
                  <c:out value="${row.isbn}"/>
                  &nbsp;
               </td>
               <td>
                  <c:out value="${row.title}"/>
                  &nbsp;
               </td>
            </tr>
         </c:forEach>
      </table>
   </body>
</html>
