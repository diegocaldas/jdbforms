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
package org.dbforms.event.classic;
import org.dbforms.*;
import org.dbforms.config.*;
import org.dbforms.event.NavigationEvent;
import org.dbforms.util.*;
import java.sql.*;
import javax.servlet.http.*;

import org.CVS.*;
import org.apache.log4j.Category;



/****
 *
 * <p>This event scrolls the current ResultSet to its first row of data</p>
 *
 * @author Joe Peer <j.peer@gmx.net>
 */
public class NavFirstEvent extends NavigationEvent
{
   static Category logCat = Category.getInstance(NavFirstEvent.class.getName()); // logging category for this class

   /**
    * Creates a new NavFirstEvent object.
    *
    * @param action DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public NavFirstEvent(String action, HttpServletRequest request,
      DbFormsConfig config)
   {
      super(action, request, config);
   }


   /**
    * Creates a new NavFirstEvent object.
    *
    * @param table DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public NavFirstEvent(Table table, HttpServletRequest request,
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
      // just select from table in given order
      return table.doConstrainedSelect(table.getFields(), childFieldValues,
         orderConstraint, Constants.COMPARE_NONE, count, con);
   }
}
