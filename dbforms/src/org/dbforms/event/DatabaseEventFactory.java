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

import java.util.HashMap;
import javax.servlet.http.*;

import org.apache.log4j.Category;

import org.dbforms.*;
import org.dbforms.event.eventtype.EventType;



/**
 * DatabaseEventFactory class.
 * Create DatabaseEvent objects.
 *
 * @author  Luca Fossato
 * @created  20 novembre 2002
 */
public abstract class DatabaseEventFactory extends EventFactory
{
    /** classes used as "keyInfo" constructor arguments types */
    protected static Class[] keyInfoConstructorArgsTypes = new Class[]
    {
        Integer.class, String.class, HttpServletRequest.class,  DbFormsConfig.class
    };


    /**
     *  Create and return a new database event
     *
     * @param  action the action string that identifies the web event
     * @param  request the HttpServletRequest object
     * @param  config the DbForms config object
     * @return  a new database event
     */
    public abstract DatabaseEvent createEvent(String             action,
                                              HttpServletRequest request,
                                              DbFormsConfig      config);


    /**
     *  Create and return a new UpdateEvent as secondary event.
     *
     * @param  tableId the table identifier
     * @param  keyId   the key   identifier
     * @param  request the HttpServletRequest object
     * @param  config  the DbForms config object
     * @return  The updateEvent object
     */
    public abstract UpdateEvent createUpdateEvent(int                tableId,
                                                  String             keyId,
                                                  HttpServletRequest request,
                                                  DbFormsConfig      config);


    /**
     *  Initialize the default events.
     *
     * @exception Exception if any error occurs
     */
    protected void initializeEvents() throws Exception
    {
        addEventInfo(new EventInfo(EventType.EVENT_DATABASE_DELETE,  "org.dbforms.event.DeleteEvent"));
        addEventInfo(new EventInfo(EventType.EVENT_DATABASE_INSERT,  "org.dbforms.event.InsertEvent"));
        addEventInfo(new EventInfo(EventType.EVENT_DATABASE_UPDATE,  "org.dbforms.event.UpdateEvent"));
    }
}
