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

package interceptors;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.FieldValue;
import org.dbforms.config.FieldValues;
import org.dbforms.config.Table;
import org.dbforms.config.ValidationException;

import org.dbforms.event.DbEventInterceptorSupport;

import org.dbforms.util.ParseUtil;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;



/**
 * @author Viviana
 * @version
 */
public class BLOBTESTEventInterceptor extends DbEventInterceptorSupport {
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
   public int preUpdate(HttpServletRequest request,
                        Table              table,
                        FieldValues        fieldValues,
                        DbFormsConfig      config,
                        Connection         con) throws ValidationException {
      String deleteImage1 = ParseUtil.getParameter(request, "delete_image1");

      if ("true".equalsIgnoreCase(deleteImage1)) {
         //here something that deletes the field "FILE" of the "BLOBTEST"
         // table
         //without deleting the entire record (in my table I have a lot of
         // others fields
         //I would preserve)
         //this way don't work for me
         FieldValue fv = fieldValues.get("FILE");
         fv.setFileHolder(null);

         //this way don't work for me
         setValue(table, fieldValues, "FILE", null);
      }

      return GRANT_OPERATION;
   }
}
