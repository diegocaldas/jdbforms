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

import org.dbforms.util.ParseUtil;
import org.dbforms.util.StringUtil;

import javax.servlet.http.HttpServletRequest;



/**
 * EventUtil class.
 *
 * @author Luca Fossato
 *
 */
public class EventHelper {
   /**
    * Gets the name of the destination table.
    *
    * @param request the request object
    * @param action the action string
    *
    * @return the destination table string
    */
   public static final String getDestinationTableName(HttpServletRequest request,
                                                      String             action) {
      return ParseUtil.getParameter(request, "data" + action + "_destTable");
   }


   /**
    * Gets the table id value.
    *
    * @param action the action string
    *
    * @return the table id value
    */
   public static final int getTableId(String action) {
      int res = -1;

      try {
         res = StringUtil.getEmbeddedStringAsInteger(action, 2, '_');
      } catch (Exception e) {
         res = -1;
      }

      return res;
   }
}
