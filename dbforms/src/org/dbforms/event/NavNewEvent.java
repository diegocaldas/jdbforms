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
import org.dbforms.util.ResultSetVector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Category;



/**
 * <p>
 * This event signalizes to the framework that the user wants to initializa a
 * new dataset
 * </p>
 * #fixme: lousy description
 * 
 * @author Joe Peer
 */
public class NavNewEvent extends NavigationEvent
{
   private static Category logCat = Category.getInstance(NavNewEvent.class.getName()); // logging category for this class

   /**
    * Creates a new NavNewEvent object.
    * 
    * @param action DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public NavNewEvent(String action, HttpServletRequest request, 
                      DbFormsConfig config)
   {
      super(action, request, config);
   }


   /**
    * Creates a new NavNewEvent object.
    * 
    * @param table DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public NavNewEvent(Table table, HttpServletRequest request, 
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
    * @param firstPosition DOCUMENT ME!
    * @param lastPosition DOCUMENT ME!
    * @param con DOCUMENT ME!
    * @param dbConnectionName DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    * @throws SQLException DOCUMENT ME!
    */
   public ResultSetVector processEvent(FieldValue[] childFieldValues, 
                                       FieldValue[] orderConstraint, int count, 
                                       String firstPosition, 
                                       String lastPosition, Connection con, 
                                       String dbConnectionName)
                                throws SQLException
   {
      //	rsv.setPointer( rsv.size() ); //#fixme - this is not well thought through
      logCat.info("processed NavNewEvent");

      return null;
   }
}