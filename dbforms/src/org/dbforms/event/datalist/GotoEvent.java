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
import org.dbforms.util.FieldValues;
import org.dbforms.util.Util;


/****
 *
 *  This event forces the controller to forward the current request
 *  to a Request-Dispatcher specified by the Application-Developer
 *  in a "org.dbforms.taglib.DbGotoButton".
 *
 * Works with new factory classes
 *
 * @author Henner Kollmann <Henner.Kollmann@gmx.de>
 */
public class GotoEvent extends NavigationEvent
{
   // logging category for this class;
   static Category logCat = Category.getInstance(GotoEvent.class.getName());

   // where to go in associated table
   private String position;
   private Table  srcTable;
   private String childField;
   private String parentField;
   private String whereClause = null;
   private String tableList   = null;

   /**
   * <p>constructor - parses the event details</p>
   * <p>Depending on the way the attributes where provided by the developer,  different
   * ways are used for resolving the dispatcher the user wants to get called and the position
   * he wants the ResultSet to be scrolled to.
   * </p>
   */
   public GotoEvent(String action, HttpServletRequest request,
      DbFormsConfig config)
   {
      // create dummy action so that tableId will be parsed to -1!
      // table and tableId will be parsed here!
      super("data_data_-1", request, config);

      String destTable = ParseUtil.getParameter(request,
            "data" + action + "_destTable");

      if (destTable == null)
      {
         this.tableId = -1;

         return; // if the user wants a simple, dumb link and we want no form to be navigated through
      }

      //# fixme: decision for *1* of the 2 approaches should be met soon!! (either id- OR name-based lookup)
      this.table = config.getTableByName(destTable);

      if (table == null)
      {
         this.table = config.getTable(Integer.parseInt(destTable));
      }

      this.tableId = table.getId();

      String srcTable = ParseUtil.getParameter(request,
            "data" + action + "_srcTable");

      if (srcTable != null)
      {
         this.srcTable = config.getTableByName(srcTable);

         if (this.srcTable == null)
         {
            this.srcTable = config.getTable(Integer.parseInt(srcTable));
         }

         childField     = ParseUtil.getParameter(request,
               "data" + action + "_childField");
         parentField    = ParseUtil.getParameter(request,
               "data" + action + "_parentField");
      }

      // the position to go to within the destination-jsp's-table	can be given
      // more or less directly
      String destPos = ParseUtil.getParameter(request,
            "data" + action + "_destPos");

      // the direct way - i.e. "1:5:value"
      if (destPos != null)
      {
         this.position = destPos;
      }
      else
      {
         String keyToDestPos = ParseUtil.getParameter(request,
               "data" + action + "_keyToDestPos");

         // the 1-leveled indirect way: i.e. "k_1_1" whereby k_1_1 leads to "1:2:23"
         if (keyToDestPos != null)
         {
            this.position = ParseUtil.getParameter(request, keyToDestPos);
         }
         else
         {
            String keyToKeyToDestPos = ParseUtil.getParameter(request,
                  "data" + action + "_keyToKeyToDestPos");

            // the 2-leveled indirect way: i.e. "my_sel" wherby "mysel" leads to "1_1",
            // which leads to "1:2:23"
            if (keyToKeyToDestPos != null)
            {
               String widgetValue = ParseUtil.getParameter(request,
                     keyToKeyToDestPos); // i.e. "1_1"
               this.position = (String) ParseUtil.getParameter(request,
                     "k_" + widgetValue); // i.e. 1:2:23
            }
         }
      }

      logCat.info("--->pos=" + position);
   }


   /**
    * this constructer is not called by the controller but, actually, BY THE VIEW
    * for example if the FormTag "gotoPrefix" attribute is set an a GotoEvent needs to be
    *  instanciated
    */
   public GotoEvent(Table table, HttpServletRequest request,
      DbFormsConfig config, String position)
   {
      super(table, request, config);
      this.position = table.getKeyPositionString(table.getFieldValues(position));
   }


   /**
    * this constructer is not called by the controller but, actually, BY THE VIEW
    * for example if the FormTag needs a free form select, this constructor is called
    */
   public GotoEvent(Table table, HttpServletRequest request,
      DbFormsConfig config, String whereClause, String tableList)
   {
      super(table, request, config);
      this.whereClause    = whereClause;
      this.tableList      = tableList;
   }

   /**
    * DOCUMENT ME!
    *
    * @param childFieldValues DOCUMENT ME!
    * @param orderConstraint DOCUMENT ME!
    * @param count DOCUMENT ME!
    * @param firstPosition DOCUMENT ME!
    * @param lastPosition DOCUMENT ME!
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
      // get the DataSourceList from the session
      logCat.info("==> GotoEvent.processEvent");
  	  position = Util.decode(position);
      if (!Util.isNull(position) && (srcTable != null)
               && !Util.isNull(childField) && !Util.isNull(parentField))
      {
         FieldValues fv = table.mapChildFieldValues(srcTable, parentField,
               childField, position);
         position = table.getKeyPositionString(fv);
         if (fv != null) 
            childFieldValues = fv.toArr();
      }

      DataSourceList ds = DataSourceList.getInstance(request);
      ds.remove(table, request);
      DataSourceFactory qry;
      if (Util.isNull(whereClause))
      {
         qry = new DataSourceFactory(config, dbConnectionName, table,
               childFieldValues, orderConstraint);
      }
      else
      {
         qry = new DataSourceFactory(config, dbConnectionName, table,
               tableList, whereClause);
      }

      ds.put(table, request, qry);
      return qry.getCurrent(position, count);
   }
}