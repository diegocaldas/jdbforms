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
import org.dbforms.config.Table;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.event.NavigationEvent;
import org.dbforms.event.datalist.dao.DataSourceList;
import org.dbforms.event.datalist.dao.DataSourceFactory;
import org.dbforms.util.ResultSetVector;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.FieldValue;
import org.dbforms.util.Util;



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
   private static Category logCat    = Category.getInstance(
                                                NavPrevEvent.class.getName());
   private int             stepWidth = 1;

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

      String stepWidthStr = ParseUtil.getParameter(request, 
                                                   "data" + action + "_sw");

      if (stepWidthStr != null)
      {
         stepWidth = Integer.parseInt(stepWidthStr);
      }
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
    * @param childFieldValues FieldValue array used to restrict a set in a
    *        subform where all "childFields" in the  resultset match their
    *        respective "parentFields" in main form
    * @param orderConstraint FieldValue array used to build a cumulation of
    *        rules for ordering (sorting) and restricting fields
    * @param count record count
    * @param firstPosition a string identifying the first resultset position
    * @param lastPosition a string identifying the last resultset position
    * @param con the JDBC Connection object
    * @param dbConnectionName DOCUMENT ME!
    * 
    * @return a ResultSetVector object
    * 
    * @exception SQLException if any error occurs
    */
   public ResultSetVector processEvent(FieldValue[] childFieldValues, 
                                       FieldValue[] orderConstraint,
                                       String sqlFilter, 
                                       int count, 
                                       String firstPosition, 
                                       String lastPosition, Connection con)
                                throws SQLException
   {
      logCat.info("==>NavPrevEvent.processEvent");

      DataSourceList    ds       = DataSourceList.getInstance(request);
      DataSourceFactory qry      = ds.get(table, request);
      if (qry == null)
      {
          qry = new DataSourceFactory(con, table, childFieldValues, orderConstraint, sqlFilter);
          ds.put(table, request, qry);
      }      
      String            position = table.getKeyPositionString(
                                            table.getFieldValues(firstPosition));
      ResultSetVector   res      = qry.getPrev(position, count);

      // change behavior to navFirst if navPrev finds no data
      if (Util.isNull(res))
      {
         res = qry.getFirst(count);
      }

      return res;
   }
}