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

package org.dbforms.event.classic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.*;

import org.dbforms.event.AbstractNavigationEvent;
import org.dbforms.interfaces.DbEventInterceptorData;

import java.sql.*;

import javax.servlet.http.*;



/**
 * DOCUMENT ME!
 *
 * @author Joe Peer
 *
 * @deprecated <p>
 */
public class NavFirstEvent extends AbstractNavigationEvent {
   static Log logCat = LogFactory.getLog(NavFirstEvent.class.getName()); // logging category for this class

   /**
    * Creates a new NavFirstEvent object.
    *
    * @param action DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public NavFirstEvent(String             action,
                        HttpServletRequest request,
                        DbFormsConfig      config) {
      super(action, request, config);
   }


   /**
    * Creates a new NavFirstEvent object.
    *
    * @param table DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public NavFirstEvent(Table              table,
                        HttpServletRequest request,
                        DbFormsConfig      config) {
      super(table, request, config);
   }

   /**
    * Process the current event.
    *
    * @param childFieldValues FieldValue array used to restrict a set of data
    * @param orderConstraint FieldValue array used to build a cumulation of
    *        rules for ordering (sorting) and restricting fields to the actual
    *        block of data
    * @param firstPosition DOCUMENT ME!
    * @param sqlFilterParams a string identifying the last resultset position
    * @param count record count
    * @param firstPosition a string identifying the first resultset position
    * @param lastPosition DOCUMENT ME!
    * @param dbConnectionName name of the used db connection. Can be used to
    *        get an own db connection, e.g. to hold it during the session (see
    *        DataSourceJDBC for example!)
    * @param con the JDBC Connection object
    *
    * @return a ResultSetVector object
    *
    * @exception SQLException if any error occurs
    */
   public ResultSetVector processEvent(FieldValue[] childFieldValues,
                                       FieldValue[] orderConstraint,
                                       String       sqlFilter,
                                       FieldValue[] sqlFilterParams,
                                       int          count,
                                       String       firstPosition,
                                       String       lastPosition,
                                       DbEventInterceptorData interceptorData)
                                throws SQLException {
      // just select from table in given order
      return getTable()
                .doConstrainedSelect(childFieldValues,
                                     orderConstraint, sqlFilter,
                                     sqlFilterParams, Constants.COMPARE_NONE,
                                     count, interceptorData);
   }
}
