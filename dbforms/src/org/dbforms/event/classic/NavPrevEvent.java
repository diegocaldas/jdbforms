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
import javax.servlet.http.*;
import java.sql.*;

import org.apache.log4j.Category;

import org.dbforms.config.*;
import org.dbforms.event.*;
import org.dbforms.util.*;



/**
 *  This event scrolls the current ResultSet to the previous row of data.
 *  <br>
 *  Provides bounded navigation.
 *
 * @author  Joe Peer <j.peer@gmx.net>, John Peterson <>
 */
public class NavPrevEvent extends NavigationEvent
{
   private static Category logCat    = Category.getInstance(NavPrevEvent.class
         .getName()); // logging category for this class
   private int             stepWidth = 1;

   /**
    *  Constructor.
    *
    * @param  action  the action string
    * @param  request the request object
    * @param  config  the config object
    */
   public NavPrevEvent(String action, HttpServletRequest request,
      DbFormsConfig config)
   {
      super(action, request, config);

      String stepWidthStr = ParseUtil.getParameter(request,
            "data" + action + "_sw");

      if (stepWidthStr != null)
      {
         stepWidth = Integer.parseInt(stepWidthStr);
      }
   }


   /**
    *  Constructor used for call from localevents.
    *
    * @param  table the Table object
    * @param  config the config object
    */
   public NavPrevEvent(Table table, HttpServletRequest request,
      DbFormsConfig config)
   {
      super(table, request, config);
   }

   /**
    *  Process the current event.
    *
    * @param  childFieldValues FieldValue array used to restrict a set in a subform where
    *                          all "childFields" in the  resultset match their respective
    *                          "parentFields" in main form
    * @param  orderConstraint FieldValue array used to build a cumulation of rules for ordering
    *                         (sorting) and restricting fields
    * @param  count record count
    * @param  firstPosition a string identifying the first resultset position
    * @param  lastPosition a string identifying the last resultset position
    * @param  con the JDBC Connection object
    * @return  a ResultSetVector object
    * @exception  SQLException if any error occurs
    */
   public ResultSetVector processEvent(FieldValue[] childFieldValues,
      FieldValue[] orderConstraint, int count, String firstPosition,
      String lastPosition, Connection con, String dbConnectionName)
      throws SQLException
   {
      logCat.info("==>NavPrevEvent");

      // select in inverted order everyting thats greater than firstpos
      table.fillWithValues(orderConstraint, firstPosition);
      FieldValue.invert(orderConstraint);

      ResultSetVector resultSetVector = table.doConstrainedSelect(table
            .getFields(), childFieldValues, orderConstraint,
            Constants.COMPARE_EXCLUSIVE, count, con);

      FieldValue.invert(orderConstraint);
      resultSetVector.flip();

      // change behavior to navFirst if navPrev finds no data
      if (resultSetVector.size() == 0)
      {
         // just select from table in given order
         logCat.info("==>NavPrevFirstEvent");
         resultSetVector = table.doConstrainedSelect(table.getFields(),
               childFieldValues, orderConstraint, Constants.COMPARE_NONE,
               count, con);
      }

      return resultSetVector;
   }
}