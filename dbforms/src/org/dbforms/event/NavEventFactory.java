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

import org.dbforms.*;
import org.dbforms.util.*;
import javax.servlet.http.*;
import java.sql.*;
import org.apache.log4j.Category;



/**
 *  NavigationeEvent Factory.
 *
 * @author Luca Fossato <fossato@pow2.com>
 */
public abstract class NavEventFactory extends EventFactory
{
    /**
     *  Create and return a new navigation event.
     *
     * @param  action the action string that identifies the web event
     * @param  request the HttpServletRequest object
     * @param  config the DbForms config object
     * @return  a new navigation event
     */
    public abstract NavigationEvent createEvent(String             action,
                                                HttpServletRequest request,
                                                DbFormsConfig      config);

    /**
     *  Create and return a new navigation event.
     *
     * @param  action the action string that identifies the web event
     * @param  table the Table object
     * @param  config the DbForms config object
     * @return  a new navigation event
     */
    public abstract NavigationEvent createEvent(String action, Table table, DbFormsConfig config);
}
