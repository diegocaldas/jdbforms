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

package org.dbforms.event;

import java.sql.SQLException;
import java.sql.Connection;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Table;

import org.dbforms.util.FieldValue;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.ResultSetVector;

import javax.servlet.http.HttpServletRequest;



/**
 * Abstract base class for all web-events related to navigation.
 * 
 * @author Joe Peer
 */
public abstract class NavigationEvent extends WebEvent
{
   /**
    * called by event engine
    * @param action DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public NavigationEvent(String action, HttpServletRequest request, 
                          DbFormsConfig config)
   {
      super(ParseUtil.getEmbeddedStringAsInteger(action, 2, '_'), request, 
            config);
   }


   /**
    * called by DbFormsTag to create local web event
    * @param table DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public NavigationEvent(Table table, HttpServletRequest request, 
                          DbFormsConfig config)
   {
      super(table.getId(), request, config);
   }

	/**
	 * Process the current event.
	 * 
	 * @param filterFieldValues 	FieldValue array used to restrict a set of data
	 * @param orderConstraint 	FieldValue array used to build a cumulation of
	 *        					rules for ordering (sorting) and restricting fields
	 * 							to the actual block of data 
	 * @param count           	record count
	 * @param firstPosition   		a string identifying the first resultset position
	 * @param lastPosition    		a string identifying the last resultset position
	 * @param dbConnectionName   name of the used db connection. Can be used to
	 *                           get an own db connection, e.g. to hold it during the 
	 *                           session (see DataSourceJDBC for example!) 
	 * @param con             	the JDBC Connection object
	 * 
	 * @return a ResultSetVector object
	 * 
	 * @exception SQLException if any error occurs
	 */
   public abstract ResultSetVector processEvent(FieldValue[] filterFieldValues, 
                                                FieldValue[] orderConstraint,
                                                String sqlFilter, 
                                                int count, 
                                                String firstPosition, 
                                                String lastPosition, 
                                                String dbConnectionName,
                                                Connection con)
                                         throws SQLException;
}