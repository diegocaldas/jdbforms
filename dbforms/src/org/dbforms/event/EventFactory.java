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
import org.dbforms.util.Util;
import org.dbforms.util.ReflectionUtil;



/**
 * EventFactory class.
 *
 * @author  Luca Fossato
 * @created  20 novembre 2002
 */
public abstract class EventFactory
{
    /** logging category */
    protected static Category logCat = Category.getInstance(EventFactory.class.getName());

    /** map of supported event */
    protected HashMap eventInfoMap = null;

    /** classes used as "non keyInfo" constructor arguments types */
    protected static Class[] constructorArgsTypes = new Class[]
    {
        String.class, HttpServletRequest.class, DbFormsConfig.class
    };


    /**
     *  Creates a new EventFactory object.
     */
    protected EventFactory()
    {
        eventInfoMap = new HashMap();
    }


    /**
     * DEPRECATED !!
     *
     *  Gets the eventInfoMap attribute of the DatabaseEventFactory object
     *
     * @return  The eventInfoMap value
     */
    //public HashMap getEventInfoMap()
    //{
    //    return eventInfoMap;
    //}


    /**
     * DEPRECATED !!
     *
     *  Sets the eventInfoMap attribute of the DatabaseEventFactory object
     *
     * @param  eventInfoMap The new eventInfoMap value
     */
    //public void setEventInfoMap(HashMap eventInfoMap)
    //{
    //    this.eventInfoMap = eventInfoMap;
    //}


    /**
     *  Get the EventInfo object having the input identifier.
     *
     * @param  id the EventInfo identifier
     * @return the EventInfo object having the input identifier, or null
     *         if that object does not exist
     */
    public EventInfo getEventInfo(String id)
    {
        EventInfo einfo = null;

        if ((eventInfoMap != null) && (eventInfoMap.containsKey(id)))
        {
            einfo = (EventInfo) eventInfoMap.get(id);
        }
        else
        {
            logCat.error("::getEventInfo - event having id [" + id + "] is not registered into the factory, returning a NULL event");
        }

        return einfo;
    }


    /**
     *  Add a new EventInfo object into the factory.
     *  <br>
     *  The EventInfo name must be unique.
     *
     * @param  einfo the EventInfo object to add to
     * @exception  Exception Description of the Exception
     */
    public void addEventInfo(EventInfo einfo) throws Exception
    {
        if (eventInfoMap != null)
        {
            String id = einfo.getId();

            if (eventInfoMap.containsKey(id))
            {
                throw new Exception("a database event information having id [" + id + "] is already registered into the factory");
            }

            eventInfoMap.put(id, einfo);
            logCat.info("::addEventInfo - event info having id, type [" + id + ", " + einfo.getType() + "] registered");
        }
    }


    /**
     *  Instance a new DatabaseEvent object
     *
     * @param  einfo the EventInfo object. Warning: can be null.
     * @param  constructorArgsTypes array of constructor argument classes
     * @param  constructorArgs array of constructor argument objects
     * @return  the event object, or null if any problem occurs
     */
    protected WebEvent getEvent(EventInfo einfo, Class[] constructorArgsTypes, Object[] constructorArgs)
    {
        WebEvent event = null;

        if (einfo != null)
        {
            try
            {
                event = (WebEvent) ReflectionUtil.newInstance(einfo.getClassName(), constructorArgsTypes, constructorArgs);
            }
            catch (Exception e)
            {
                logCat.error("::getEvent - cannot create the new event [" + einfo + "]", e);
            }
        }

        return event;
    }
}
