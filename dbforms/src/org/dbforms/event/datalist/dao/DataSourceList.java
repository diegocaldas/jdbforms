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
import org.dbforms.Table;
import org.dbforms.util.ParseUtil;

/**
 * Holds a list of DataSourceFactory object in the session context. 
 * Needed by the navigation events to store the datasource by a per session mode.
 * So it is possible to reuse the data between different calls and it's not neccessary
 * to refetch again.
 * 
 * @author hkk
 *
 */
public class DataSourceList {
   /** 
    * Hashtable to hold all DataSource objects. 
    * Key is queryString.
    */
   private Hashtable ht;
   
	/**
	 * returns an unique instance of this class for each session 
	 */
	public static DataSourceList getInstance(HttpServletRequest request) {
	   DataSourceList ds = (DataSourceList) request.getSession().getAttribute("DataSourceList");
	   if (ds == null) {
			ds = new DataSourceList();
			request.getSession().setAttribute("DataSourceList", ds);
	   }
	   return ds;
	}

	private DataSourceList() {
	   super();
	   ht = new Hashtable();
	}
	
	
   private String getPutKey(Table table, HttpServletRequest request) {
		String refSource = request.getRequestURI(); 
		refSource = refSource + "?" + table.getName();
		return refSource;   
   }

	private String getGetKey(Table table, HttpServletRequest request) {
		String refSource = ParseUtil.getParameter(request, "source");
		if (refSource == null) {	
			refSource = getPutKey(table, request); 
		} else {
			refSource = refSource + "?" + table.getName();
		}
		return refSource;   
	}

	/**
	 * adds a DataSource object. If object exists in the Hashtable close first! 
	 */
	public void put(Table table, HttpServletRequest request, DataSourceFactory ds) throws SQLException {
	   ht.put(getPutKey(table, request), ds);
	}
	
	public DataSourceFactory get(Table table, HttpServletRequest request) {
		DataSourceFactory result = (DataSourceFactory) ht.get(getGetKey(table, request)); 
		return result;
	}

	public DataSourceFactory remove(Table table, HttpServletRequest request) throws SQLException {
		DataSourceFactory result = (DataSourceFactory) ht.remove(getGetKey(table, request)); 
		if (result != null)
		   result.close();
		return result;
	}

}
