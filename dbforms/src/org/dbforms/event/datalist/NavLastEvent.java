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


/****
 *
 * <p>This event scrolls the current ResultSet to its last row of data</p>
 *
 * <p>Works with new factory classes</p>
 *
 * @author Henner Kollmann <Henner.Kollmann@gmx.de>
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
    * DOCUMENT ME!
    *
    * @param childFieldValues DOCUMENT ME!
    * @param orderConstraint DOCUMENT ME!
    * @param count DOCUMENT ME!
    * @param firstPost DOCUMENT ME!
    * @param lastPos DOCUMENT ME!
    * @param con DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public ResultSetVector processEvent(FieldValue[] childFieldValues,
      FieldValue[] orderConstraint, int count, String firstPosition,
      String lastPosition, Connection con, String dbConnectionName)
      throws SQLException
   {
      logCat.info("==>NavLastEvent.processEvent");

      DataSourceList    ds  = DataSourceList.getInstance(request);
      DataSourceFactory qry = ds.get(table, request);

      return qry.getLast(count);
   }
}
