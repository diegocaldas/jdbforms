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

import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

import org.dbforms.config.Table;



/**
 * Holds a list of DataSourceFactory object in the session context.
 * Needed by the navigation events to store the datasource 
 * by a per session mode.
 * So it is possible to reuse the data between different calls 
 * and it's not neccessary to refetch again.
 *
 * @author hkk
 */
public class DataSourceList
{
   /**
    * Hashtable to hold all DataSource objects.
    * Key is queryString.
    */
   private Hashtable ht;


   /**
	* Private constructor.
	* <br>
	* Use <code>getInstance</code> to get an instance of 
	* the DataSourceList object.
	*/
   private DataSourceList()
   {
	  super();
	  ht = new Hashtable();
   }


   /**
    * Returns an unique instance of this class for each session
    * 
    * @param request the request object
    */
   public static DataSourceList getInstance(HttpServletRequest request)
   {
      DataSourceList ds = 
        (DataSourceList)request.getSession().getAttribute("DataSourceList");

      if (ds == null)
      {
         ds = new DataSourceList();
         request.getSession().setAttribute("DataSourceList", ds);
      }

      return ds;
   }


   /**
    * Adds a DataSource object to the list. 
    * If object exists in the Hashtable close first!
    * 
    * @param table the table object
    * @param request the request object
    * @param ds  the DataSourceFactory object to store into the list
    */
   public void put(Table table, HttpServletRequest request, DataSourceFactory ds)
      throws SQLException
   {
      ht.put(getKey(table, request), ds);
   }


   /**
    * Get a DataSource object. 
    *
    * @param table   the table object
    * @param request the request object
    *
    * @return the DataSource object related to the input table
    */
   public DataSourceFactory get(Table table, HttpServletRequest request)
   {
      DataSourceFactory result = (DataSourceFactory) ht.get(getKey(table, request));

      return result;
   }


   /**
    * Remove a DataSource object from the list. 
    *
    * @param table  the table object
    * @param request the request object
    *
    * @return the DataSource object related to the input table.
    *         Note that the returned DataSource object has just been closed
    *         by this method.
    *
    * @throws SQLException DOCUMENT ME!
    */
   public DataSourceFactory remove(Table table, HttpServletRequest request)
   {
      DataSourceFactory result = 
         (DataSourceFactory) ht.remove(getKey(table, request));

      if (result != null)
      {
         result.close();
      }

      return result;
   }
   
   
   /**
	* Get the key string.
	* 
	* @param table  the table object
	* @param request the request object
	* @return the key string (a.k.a. the queryString)
	*/
   private String getKey(Table table, HttpServletRequest request)
   {
	  String refSource = request.getRequestURI();
	  refSource = refSource + "?" + table.getName();
	  return refSource;
   }
}
