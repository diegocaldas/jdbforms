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
import org.dbforms.config.*;
import org.dbforms.event.NavigationEvent;
import javax.servlet.http.*;
import java.sql.*;
import org.apache.log4j.Category;



/****
 *
 * @deprecated
 *
 * <p>This event scrolls the current ResultSet to its last row of data</p>
 *
 * @author Joe Peer <j.peer@gmx.net>
 */
public class NavLastEvent extends NavigationEvent
{
   static Category logCat = Category.getInstance(NavLastEvent.class.getName()); // logging category for this class

   /**
    * Creates a new NavLastEvent object.
    *
    * @param action DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public NavLastEvent(String action, HttpServletRequest request, 
                       DbFormsConfig config)
   {
      super(action, request, config);
   }


   /**
    * Creates a new NavLastEvent object.
    *
    * @param table DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public NavLastEvent(Table table, HttpServletRequest request, 
                       DbFormsConfig config)
   {
      super(table, request, config);
   }

   /**
    * Process the current event.
    *
    * @param filterFieldValues    FieldValue array used to restrict a set of data
    * @param orderConstraint    FieldValue array used to build a cumulation of
    *                       rules for ordering (sorting) and restricting fields
    *                      to the actual block of data
    * @param count              record count
    * @param firstPost         a string identifying the first resultset position
    * @param lastPos          a string identifying the last resultset position
    * @param dbConnectionName   name of the used db connection. Can be used to
    *                           get an own db connection, e.g. to hold it during the
    *                           session (see DataSourceJDBC for example!)
    * @param con                the JDBC Connection object
    *
    * @return a ResultSetVector object
    *
    * @exception SQLException if any error occurs
    */
   public ResultSetVector processEvent(FieldValue[] childFieldValues, 
                                       FieldValue[] orderConstraint, 
                                       String sqlFilter, 
                                       FieldValue[] sqlFilterParams, int count, 
                                       String firstPosition, 
                                       String lastPosition, 
                                       String dbConnectionName, Connection con)
                                throws SQLException
   {
      // select from table in inverted order
      logCat.info("==>NavLastEvent");
      FieldValue.invert(orderConstraint);

      ResultSetVector resultSetVector = getTable()
                                           .doConstrainedSelect(getTable()
                                                                   .getFields(), 
                                                                childFieldValues, 
                                                                orderConstraint, 
                                                                sqlFilter, 
                                                                sqlFilterParams, 
                                                                Constants.COMPARE_NONE, 
                                                                count, con);
      FieldValue.invert(orderConstraint);
      resultSetVector.flip();

      return resultSetVector;
   }
}