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

package org.dbforms.event.datalist.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.Table;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;



/**
 * Holds a list of DataSourceFactory object in the session context. Needed by
 * the navigation events to store the datasource  by a per session mode. So it
 * is possible to reuse the data between different calls  and it's not
 * neccessary to refetch again.
 *
 * @author hkk
 */
public class DataSourceSessionList implements HttpSessionBindingListener {
   /** Hashtable to hold all DataSource objects. Key is queryString. */

   // logging category for this class;
   private static Log logCat = LogFactory.getLog(DataSourceSessionList.class.getName());
   private Map        ht = new HashMap();

   /**
    * Private constructor. <br>
    * Use <code>getInstance</code> to get an instance of  the DataSourceList
    * object.
    */
   private DataSourceSessionList() {
      super();
   }

   /**
    * Returns an unique instance of this class for each session
    *
    * @param request the request object
    *
    * @return DOCUMENT ME!
    */
   public static synchronized DataSourceSessionList getInstance(HttpServletRequest request) {
      // try to retrieve an existant dataSourceList object from the session
      // context;
      DataSourceSessionList ds = (DataSourceSessionList) request.getSession()
                                                  .getAttribute("org.dbforms.event.datalist.dao.DataSourceSessionList");

      // if it does not exist, createn a new one and store
      // its reference into the session;
      if (ds == null) {
         ds = new DataSourceSessionList();
         request.getSession()
                .setAttribute("org.dbforms.event.datalist.dao.DataSourceSessionList",
                              ds);
      }

      return ds;
   }


   /**
    * Get a DataSourceFactory object.
    *
    * @param table the table object
    * @param request the request object
    *
    * @return the DataSourceFactory object related to the input table
    */
   public DataSourceFactory get(Table              table,
                                HttpServletRequest request) {
      synchronized (ht) {
         DataSourceFactory result = (DataSourceFactory) ht.get(getKey(table,
                                                                      request));

         return result;
      }
   }


   /**
    * Adds a DataSourceFactory object to the list.  If object exists in the
    * Hashtable close first!
    *
    * @param table the table object
    * @param request the request object
    * @param ds the DataSourceFactory object to store into the list
    *
    * @throws SQLException DOCUMENT ME!
    */
   public void put(Table              table,
                   HttpServletRequest request,
                   DataSourceFactory  ds) throws SQLException {
      synchronized (ht) {
         ht.put(getKey(table, request), ds);
      }
   }


   /**
    * Remove a DataSource object from the list.
    *
    * @param table the table object
    * @param request the request object
    *
    * @return the DataSource object related to the input table. Note that the
    *         returned DataSource object has just been closed by this method.
    */
   public DataSourceFactory remove(Table              table,
                                   HttpServletRequest request) {
      synchronized (ht) {
         DataSourceFactory result = (DataSourceFactory) ht.remove(getKey(table,
                                                                         request));

         if (result != null) {
            result.close();
         }

         return result;
      }
   }


   /**
    * Receive notification that this session will be passivated.
    *
    * @param event The session event that has occurred
    */
   public void valueBound(HttpSessionBindingEvent event) {
   }


   /**
    * Receive notification that this session was activated.
    *
    * @param event The session event that has occurred
    */
   public void valueUnbound(HttpSessionBindingEvent event) {
      synchronized (ht) {
         logCat.info("valueUnbound called");

         Iterator iter = ht.values()
                           .iterator();

         while (iter.hasNext()) {
            Object            obj = iter.next();
            DataSourceFactory qry = (DataSourceFactory) obj;
            qry.close();
         }

         ht.clear();
      }
   }


   /**
    * Get the key string used to retrieve the DataSource object  from the
    * internal hash table.
    *
    * @param table the table object
    * @param request the request object
    *
    * @return the key string (a.k.a. the queryString) used to retrieve  the
    *         DataSource object from the internal hash table
    */
   private String getKey(Table              table,
                         HttpServletRequest request) {
      String refSource = request.getRequestURI();
      refSource = refSource + "?" + table.getName();

      return refSource;
   }
}
