/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * DbForms - a Rapid Application Development Framework
 * Copyright (C) 2001 Joachim Peer <joepeer@excite.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */

package org.dbforms.util;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Category;

/**
 * <p>
 * this utility-class provides convenience methods for SQL related tasks
 * </p>
 * 
 * @author Joe Peer
 * @author Eric Pugh
 */
public class SqlUtil {
   // logging category for this class
   private static Category logCat = Category.getInstance(SqlUtil.class.getName());

   /**
    * Close the input connection
    * 
    * @param con the connection to close
    */
   public static final void closeConnection(Connection con) {
      if (con != null) {
         try {
            SqlUtil.logCat.debug("About to close connection - " + con);
            con.close();
            SqlUtil.logCat.debug("Connection closed");
         } catch (SQLException e) {
            SqlUtil.logSqlException(e, "::closeConnection - cannot close the input connection");
         }
      }
   }

   /**
    * Log the SQLException stacktrace (adding the input description to  the
    * first log statement) and do the same for all the nested exceptions.
    * 
    * @param e    the SQL exception to log
    * @param desc the exception description
    */
   public static final void logSqlException(SQLException e, String desc) {
      int i = 0;
      String excDesc = "::logSqlExceptionSQL - main SQL exception";

      // adding the input description to the main log statement;
      if (!Util.isNull(desc)) {
         excDesc += (" [" + desc + "]");
      }

      SqlUtil.logCat.error(excDesc, e);

      while ((e = e.getNextException()) != null)
         SqlUtil.logCat.error("::logSqlException - nested SQLException (" + (i++) + ")", e);
   }

   /**
    * Log the SQLException stacktrace and do the same for all the nested
    * exceptions.
    * 
    * @param e  the SQL exception to log
    */
   public static final void logSqlException(SQLException e) {
      SqlUtil.logSqlException(e, null);
   }
}