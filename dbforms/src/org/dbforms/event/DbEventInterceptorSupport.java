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
package org.dbforms.event;
import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;

import org.dbforms.config.DbEventInterceptor;
import org.dbforms.config.ValidationException;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.FieldValues;
import org.dbforms.config.FieldValue;
import org.dbforms.config.Field;
import org.dbforms.config.Table;

/**
* convenience class
* 
* @author Joe Peer
*/
public class DbEventInterceptorSupport implements DbEventInterceptor
{

	/**
	 * adds or replace a value in the fieldValues
	 * @param table wich should be used to lookup for the fieldName
	 * @param fieldValues to add/replace value to
	 * @param fieldName to add/replace value
	 * @param value to add/replace
	 */
	protected void setValue(Table table, FieldValues fieldValues, String fieldName, String value) {
		FieldValue fv = fieldValues.get(fieldName);
		Field f = table.getFieldByName(fieldName);
		if (f != null) {
			if (fv == null) {
				fv = new FieldValue(f, value);
				fieldValues.put(fv);
			} else {
				fv.setFieldValue(value);
			}
		}
	}


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param fieldValues DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    */
   public int preInsert(HttpServletRequest request,  Table table, FieldValues fieldValues,
      DbFormsConfig config, Connection con) throws ValidationException
   {
      return GRANT_OPERATION;
   }


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    */
   public void postInsert(HttpServletRequest request, DbFormsConfig config,
      Connection con)
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param fieldValues DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    */
   public int preUpdate(HttpServletRequest request, Table table, 
         FieldValues fieldValues, DbFormsConfig config, Connection con)
      throws ValidationException
   {
      return GRANT_OPERATION;
   }


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    */
   public void postUpdate(HttpServletRequest request, DbFormsConfig config,
      Connection con)
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param fieldValues DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    */
   public int preDelete(HttpServletRequest request,  Table table, FieldValues fieldValues,
      DbFormsConfig config, Connection con) throws ValidationException
   {
      return GRANT_OPERATION;
   }


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    */
   public void postDelete(HttpServletRequest request, DbFormsConfig config,
      Connection con)
   {
   }


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    */
   public int preSelect(HttpServletRequest request, DbFormsConfig config,
      Connection con) throws ValidationException
   {
      return GRANT_OPERATION;
   }


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param con DOCUMENT ME!
    */
   public void postSelect(HttpServletRequest request, DbFormsConfig config,
      Connection con)
   {
   }
}
