package interceptors;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.dbforms.event.DbEventInterceptorSupport;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.ValidationException;
import org.dbforms.config.Table;
import org.dbforms.config.FieldValues;

/**
 * @author hkk
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BookstoreWithInterceptorTest extends DbEventInterceptorSupport {

   public int preInsert(HttpServletRequest request,  Table table, FieldValues fieldValues,
      DbFormsConfig config, Connection con) throws ValidationException
   {

      Statement stmt;
      ResultSet rs = null;
      long new_id = 0;
      String strSql = "";
      String strParentID = "AUTHOR_ID";
      String strID = "BOOK_ID";
      String strTbl = "BOOK";
      if (fieldValues.get(strID) == null) {
         try {
            stmt = con.createStatement();
            strSql = "select max(" + strID + ") from " + strTbl;
            if (fieldValues.get(strParentID) != null) {
               strSql = strSql + " where " + strParentID + "=" + fieldValues.get(strParentID);
            }
            rs = stmt.executeQuery(strSql);
            rs.next();
            new_id = rs.getLong(1) + 1;
         } catch (SQLException e) {
            e.printStackTrace();
         }
         if (new_id == 0)
            throw new ValidationException("Error generating automatic IDs");
         else {
            fieldValues.remove(strID);
            setValue(table, fieldValues, strID, Long.toString(new_id));
            setValue(table, fieldValues, strParentID, Long.toString(1));
            return GRANT_OPERATION;
         }
      } else
         return GRANT_OPERATION;
   }

   public int preUpdate(HttpServletRequest request, Table table, 
         FieldValues fieldValues, DbFormsConfig config, Connection con)
      throws ValidationException
   {

      fieldValues.remove("ISBN");
      return GRANT_OPERATION;
   }

}
