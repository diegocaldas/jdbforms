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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.DbEventInterceptorData;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.FieldValue;
import org.dbforms.config.FieldValues;
import org.dbforms.config.ResultSetVector;
import org.dbforms.config.Table;

import org.dbforms.event.NavigationEvent;
import org.dbforms.event.datalist.dao.DataSourceFactory;
import org.dbforms.event.datalist.dao.DataSourceSessionList;

import org.dbforms.util.ParseUtil;
import org.dbforms.util.Util;

import java.io.UnsupportedEncodingException;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;



/**
 * This event forces the controller to forward the current request to a
 * Request-Dispatcher specified by the Application-Developer in a
 * "org.dbforms.taglib.DbGotoButton". Works with new factory classes
 *
 * @author Henner Kollmann
 */
public class GotoEvent extends NavigationEvent {
   // logging category for this class;
   private static Log logCat      = LogFactory.getLog(GotoEvent.class.getName());
   private String     childField;
   private String     parentField;

   // where to go in associated table
   private String  position;
   private String  tableList   = null;
   private String  whereClause = null;
   private Table   srcTable;
   private boolean singleRow   = false;

   /**
    * Constructor - parses the event details. <br>
    * Depending on the way the attributes where provided by the developer,
    * different ways are used for resolving the dispatcher the user wants to
    * get called and the position he wants the ResultSet to be scrolled to.
    *
    * @param action the action string
    * @param request the request object
    * @param config the config object
    */
   public GotoEvent(String action, HttpServletRequest request,
      DbFormsConfig config) {
      // create dummy action so that tableId will be parsed to -1!
      // table and tableId will be parsed here!
      super("data_data_-1", request, config);

      String destTable = ParseUtil.getParameter(request,
            "data" + action + "_destTable");

      // if the user wants a simple, dumb link and we want no form to be navigated through
      if (destTable == null) {
         setTable(null);

         return;
      }

      // # fixme: decision for *1* of the 2 approaches 
      //          should be met soon!! (either id- OR name-based lookup)
      setTable(config.getTableByName(destTable));

      if (getTable() == null) {
         setTable(config.getTable(Integer.parseInt(destTable)));
      }

      String psrcTable = ParseUtil.getParameter(request,
            "data" + action + "_srcTable");

      singleRow = "true".equals(ParseUtil.getParameter(request,
               "data" + action + "_singleRow"));

      if (psrcTable != null) {
         this.srcTable = config.getTableByName(psrcTable);

         if (this.srcTable == null) {
            this.srcTable = config.getTable(Integer.parseInt(psrcTable));
         }

         childField    = ParseUtil.getParameter(request,
               "data" + action + "_childField");
         parentField = ParseUtil.getParameter(request,
               "data" + action + "_parentField");
      }

      // the position to go to within the destination-jsp's-table can be given
      // more or less directly
      String destPos = ParseUtil.getParameter(request,
            "data" + action + "_destPos");

      // the direct way - i.e. "1:5:value"
      if (destPos != null) {
         this.position = destPos;
      } else {
         String keyToDestPos = ParseUtil.getParameter(request,
               "data" + action + "_keyToDestPos");

         // the 1-leveled indirect way: i.e. "k_1_1" whereby k_1_1 leads to "1:2:23"
         if (keyToDestPos != null) {
            this.position = ParseUtil.getParameter(request, keyToDestPos);
         } else {
            String keyToKeyToDestPos = ParseUtil.getParameter(request,
                  "data" + action + "_keyToKeyToDestPos");

            // the 2-leveled indirect way: i.e. "my_sel" wherby "mysel" leads to "1_1",
            // which leads to "1:2:23"
            if (keyToKeyToDestPos != null) {
               String widgetValue = ParseUtil.getParameter(request,
                     keyToKeyToDestPos); // i.e. "1_1"
               this.position = ParseUtil.getParameter(request,
                     "k_" + widgetValue); // i.e. "1:2:23"
            }
         }
      }

      logCat.info("--->pos=" + position);
   }


   /**
    * This constructor is not called by the controller but, actually, BY THE
    * VIEW for example if the FormTag "gotoPrefix" attribute is set an a
    * GotoEvent needs to be instanciated.
    *
    * @param table the input table
    * @param request request the request object
    * @param config the config object
    * @param position the position string
    */
   public GotoEvent(Table table, HttpServletRequest request,
      DbFormsConfig config, String position) {
      super(table, request, config);
      this.position = table.getKeyPositionString(table.getFieldValues(position));
   }


   /**
    * This constructer is not called by the controller but, actually, BY THE
    * VIEW for example if the FormTag needs a free form select, this
    * constructor is called.
    *
    * @param table the input table
    * @param request request the request object
    * @param config the config object
    * @param whereClause the SQL where clause
    * @param tableList the table list
    */
   public GotoEvent(Table table, HttpServletRequest request,
      DbFormsConfig config, String whereClause, String tableList) {
      super(table, request, config);
      this.whereClause    = whereClause;
      this.tableList      = tableList;
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
      FieldValue[] orderConstraint, String sqlFilter,
      FieldValue[] sqlFilterParams, int count, String firstPosition,
      String lastPosition, DbEventInterceptorData interceptorData)
      throws SQLException {
      // get the DataSourceList from the session
      logCat.info("==> GotoEvent.processEvent");

      FieldValues fv;

      if (!Util.isNull(position)) {
         try {
            position = Util.decode(position, getRequest().getCharacterEncoding());
         } catch (UnsupportedEncodingException e) {
            logCat.error(e);
            throw new SQLException(e.getMessage());
         }

         if ((srcTable != null) && !Util.isNull(childField)
                  && !Util.isNull(parentField)) {
            fv = getTable().mapChildFieldValues(srcTable, parentField,
                  childField, position);
         } else {
            fv = getTable().getFieldValues(position);
         }

         position = getTable().getKeyPositionString(fv);

         if (singleRow && (fv != null)) {
            childFieldValues = fv.toArray();
         }
      }

      DataSourceSessionList ds = DataSourceSessionList.getInstance(getRequest());
      ds.remove(getTable(), getRequest());

      DataSourceFactory qry = new DataSourceFactory((String) interceptorData
            .getAttribute(DbEventInterceptorData.CONNECTIONNAME),
            interceptorData.getConnection(), getTable());

      if (Util.isNull(whereClause)) {
         qry.setSelect(childFieldValues, orderConstraint, sqlFilter,
            sqlFilterParams);
      } else {
         qry.setSelect(tableList, whereClause);
      }

      ds.put(getTable(), getRequest(), qry);

      return qry.getCurrent(interceptorData, position, count);
   }
}
