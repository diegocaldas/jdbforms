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
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Table;
import org.dbforms.util.FieldValue;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.ResultSetVector;



/**
 * <p>
 * This event signalizes to the framework that the user wants to initialize a
 * new dataset  with values coming from current row
 * </p>
 * #fixme: lousy description
 * 
 * @version $Revision$
 * @author Stefano Borghi
 */
public class NavCopyEvent extends NavigationEvent
{
   /**
    * Creates a new NavCopyEvent object.
    * 
    * @param action DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public NavCopyEvent(String action, HttpServletRequest request, 
                       DbFormsConfig config)
   {
      super(action, request, config);
      this.config = config;
      tableId     = ParseUtil.getEmbeddedStringAsInteger(action, 2, '_');
      this.table  = config.getTable(tableId);
   }


   /**
    * Creates a new NavCopyEvent object.
    * 
    * @param table DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public NavCopyEvent(Table table, HttpServletRequest request, 
                       DbFormsConfig config)
   {
      super(table, request, config);
      this.table   = table;
      this.tableId = table.getId();
      this.config  = config;
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
   public ResultSetVector processEvent(FieldValue[] filterFieldValues, 
                                       FieldValue[] orderConstraint, 
                                       String sqlFilter, int count, 
                                       String firstPosition, String lastPosition,
									   String dbConnectionName,
                                       Connection con)
                                throws SQLException
   {
      logCat.info("processed NavCopyEvent");

      return null;
   }
}