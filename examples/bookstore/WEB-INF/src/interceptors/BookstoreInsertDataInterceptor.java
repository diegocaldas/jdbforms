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
 * Foundation, Inc., 59 TemplePlace, Suite 330, Boston, MA  02111-1307 USA
 */

package interceptors;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.FieldValue;
import org.dbforms.config.FieldValues;
import org.dbforms.config.Table;
import org.dbforms.config.ValidationException;

import org.dbforms.event.DbEventInterceptorSupport;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;



/**
 * @author hkk
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu
 * ändern: Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und
 * Kommentare
 */
public class BookstoreInsertDataInterceptor extends DbEventInterceptorSupport {
   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param table DOCUMENT ME!
    * @param fieldValues DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    */
   public int preInsert(HttpServletRequest request,
                        Table              table,
                        FieldValues        fieldValues,
                        DbFormsConfig      config,
                        Connection         con) throws ValidationException {
      long       new_id    = 0;
      String     fieldName = table.getName() + "_ID";
      FieldValue fv        = fieldValues.get(fieldName);
      if (fv != null) {
         new_id = ((Integer) fv.getFieldValueAsObject()).intValue();
      }

      if (new_id == 0) {
         String qry = "select max(" + fieldName + ") from " + table.getName();

         try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(qry);
            rs.next();
            new_id = rs.getLong(1);
            stmt.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }

         new_id++;
         setValue(table, fieldValues, fieldName, String.valueOf(new_id));
      }

      return GRANT_OPERATION;
   }
}
