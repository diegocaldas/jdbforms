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

package org.dbforms.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.log4j.Category;

import org.dbforms.util.FileHolder;

/**
 * <p>
 * this utility-class provides convenience methods for SQL related tasks
 * </p>
 * 
 * @author Joe Peer
 * @author Eric Pugh
 */
public class JDBCDataHelper {
   // logging category for this class
   private static Category logCat = Category.getInstance(JDBCDataHelper.class.getName());

   /**
    * this utility-method assigns a particular value to a place holder of a
    * PreparedStatement. it tries to find the correct setXxx() value, accoring
    * to the field-type information represented by "fieldType". quality: this
    * method is bloody alpha (as you might see :=)
    * @param ps DOCUMENT ME!
    * @param col DOCUMENT ME!
    * @param value DOCUMENT ME!
    * @param fieldType DOCUMENT ME!
    * @throws SQLException DOCUMENT ME!
    */
   public static void fillPreparedStatement(PreparedStatement ps, int col, Object value, int fieldType) throws SQLException {
      logCat.debug("fillPreparedStatement( ps, " + col + ", " + value + ", " + fieldType + ")...");
      switch (fieldType) {
         case 0:
            throw new SQLException("illegal type!");      
         case FieldTypes.BLOB :
            if (value == null) {
               ps.setNull(col, java.sql.Types.BLOB);
            } else {
               FileHolder fileHolder = (FileHolder) value;
               try {
                  ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                  ObjectOutputStream out = new ObjectOutputStream(byteOut);
                  out.writeObject(fileHolder);
                  out.flush();

                  byte[] buf = byteOut.toByteArray();
                  byteOut.close();
                  out.close();

                  ByteArrayInputStream bytein = new ByteArrayInputStream(buf);
                  int byteLength = buf.length;
                  ps.setBinaryStream(col, bytein, byteLength);

                  // store fileHolder as a whole (this way we don't lose file meta-info!)
               } catch (IOException ioe) {
                  ioe.printStackTrace();
                  logCat.info(ioe.toString());
                  throw new SQLException("error storing BLOB in database - " + ioe.toString(), null, 2);
               }
            }
            break;
         default :
            ps.setObject(col, value);
      }
   }
}