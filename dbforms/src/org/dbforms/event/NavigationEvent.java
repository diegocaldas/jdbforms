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
import java.sql.*;

import org.CVS.*;
import org.dbforms.*;
import org.dbforms.config.*;
import org.dbforms.util.*;
import javax.servlet.http.*;



/**
 * Abstract base class for all web-events related to navigation.
 *
 * @author Joe Peer <j.peer@gmx.net>
 */
public abstract class NavigationEvent extends WebEvent
{
   /**
    *
    *   called by event engine
    *
    */
   public NavigationEvent(String action, HttpServletRequest request,
      DbFormsConfig config)
   {
      super(ParseUtil.getEmbeddedStringAsInteger(action, 2, '_'), request,
         config);
   }


   /**
    *
    *   called by DbFormsTag to create local web event
    *
    */
   public NavigationEvent(Table table, HttpServletRequest request,
      DbFormsConfig config)
   {
      super(table.getId(), request, config);
   }

   /**
    *  Process the current event.
    *
    * @param  childFieldValues FieldValue array used to restrict a set in a subform where
    *                          all "childFields" in the  resultset match their respective
    *                          "parentFields" in main form
    * @param  orderConstraint FieldValue array used to build a cumulation of rules for ordering
    *                         (sorting) and restricting fields
    * @param  count           record count
    * @param  firstPosition   a string identifying the first resultset position
    * @param  lastPosition    a string identifying the last resultset position
    * @param  con             the JDBC Connection object
    * @return  a ResultSetVector object
    * @exception  SQLException if any error occurs
    */
   public abstract ResultSetVector processEvent(FieldValue[] childFieldValues,
      FieldValue[] orderConstraint, int count, String firstPost,
      String lastPos, Connection con, String dbConnectionName)
      throws SQLException;
}
