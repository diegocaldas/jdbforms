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
import javax.servlet.http.*;
import org.dbforms.*;
import org.dbforms.config.*;
import org.dbforms.util.*;
import org.CVS.*;
import org.apache.log4j.Category;



/****
 *
 * <p>abstract base class for all web-events</p>
 *
 * <p>implementations of this class will be generated by the controller/EventEngine by
 * dispatchting the incoming HTTP-request from the client. In fact, the WebEvent classes itselves
 * may provide methods (constructors) to help parsing the request data.</p>
 *
 * <p>WebEvents may be processed by the controller and/or by the custom tags at JSP-side</p>
 *
 * @author Joe Peer <j.peer@gmx.net>
 */
public class NoopEvent extends WebEvent
{
   static Category logCat = Category.getInstance(NoopEvent.class.getName()); // logging category for this class

   /**
    * Creates a new NoopEvent object.
    */
   public NoopEvent(int tableId, HttpServletRequest request,
      DbFormsConfig config)
   {
      super(tableId, request, config);
   }
}
