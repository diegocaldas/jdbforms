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

package org.dbforms.event.datalist;
import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Category;
import org.dbforms.config.FieldValue;
import org.dbforms.config.ResultSetVector;
import org.dbforms.config.Table;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.event.NavigationEvent;
import org.dbforms.event.datalist.dao.DataSourceList;
import org.dbforms.event.datalist.dao.DataSourceFactory;



/**
 * This event scrolls the current ResultSet to the previous row of data. <br>
 * Provides bounded navigation. <br>
 * Works with new factory classes
 *
 * @author Henner Kollmann
 */
public class NavPrevEvent extends NavigationEvent
{
   // logging category for this class
   private static Category logCat = Category.getInstance(
                                             NavPrevEvent.class.getName());
   /**
    * Constructor.
    *
    * @param action  the action string
    * @param request the request object
    * @param config  the config object
    */
   public NavPrevEvent(String action, HttpServletRequest request, 
                       DbFormsConfig config)
   {
      super(action, request, config);
   }


   /**
    * Constructor used for call from localevents.
    *
    * @param table the Table object
    * @param request DOCUMENT ME!
    * @param config the config object
    */
   public NavPrevEvent(Table table, HttpServletRequest request, 
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
    * @param firstPosition         a string identifying the first resultset position
    * @param lastPosition          a string identifying the last resultset position
    * @param dbConnectionName   name of the used db connection. Can be used to
    *                           get an own db connection, e.g. to hold it during the
    *                           session (see DataSourceJDBC for example!)
    * @param con                the JDBC Connection object
    *
    * @return a ResultSetVector object
    *
    * @exception SQLException if any error occurs
    */
   public ResultSetVector processEvent(FieldValue[] filterFieldValues, 
                                       FieldValue[] orderConstraint, 
                                       String sqlFilter, 
                                       FieldValue[] sqlFilterParams, int count, 
                                       String firstPosition, 
                                       String lastPosition, 
                                       String dbConnectionName, Connection con)
                                throws SQLException
   {
      logCat.info("==>NavPrevEvent.processEvent");

      DataSourceList    ds  = DataSourceList.getInstance(request);
      DataSourceFactory qry = ds.get(getTable(), request);

      if (qry == null)
      {
         qry = new DataSourceFactory(dbConnectionName, con, getTable());
         qry.setSelect(filterFieldValues, orderConstraint, sqlFilter, 
                       sqlFilterParams);
         ds.put(getTable(), request, qry);
      }

      String          position = getTable()
                                    .getKeyPositionString(getTable()
                                                             .getFieldValues(firstPosition));
      ResultSetVector res = qry.getPrev(position, count);

      // change behavior to navFirst if navPrev finds no data
      if (ResultSetVector.isNull(res))
      {
         res = qry.getFirst(count);
      }

      return res;
   }
}