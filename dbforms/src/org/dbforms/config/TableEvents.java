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
package org.dbforms.config;
import java.util.HashMap;
import org.apache.log4j.Category;

import org.dbforms.event.eventtype.EventType;



/**
 *  Handles the events related to the linked table object.
 *
 * @author  Luca Fossato
 * @created  30 november 2002
 */
public class TableEvents
{
   private static Category logCat   = Category.getInstance(TableEvents.class
         .getName());
   private Table           table    = null;
   private HashMap         eventMap = null;
   private boolean         doLog    = false;

   /**
    *  Default constructor.
    */
   public TableEvents()
   {
      eventMap = new HashMap();

      try
      {
         // set the default database events;
         addEventInfo(EventType.EVENT_DATABASE_DELETE);
         addEventInfo(EventType.EVENT_DATABASE_INSERT);
         addEventInfo(EventType.EVENT_DATABASE_UPDATE);

         // set the default navigation events;
         addEventInfo(EventType.EVENT_NAVIGATION_FIRST);
         addEventInfo(EventType.EVENT_NAVIGATION_GOTO);
         addEventInfo(EventType.EVENT_NAVIGATION_LAST);
         addEventInfo(EventType.EVENT_NAVIGATION_NEW);
         addEventInfo(EventType.EVENT_NAVIGATION_COPY);
         addEventInfo(EventType.EVENT_NAVIGATION_NEXT);
         addEventInfo(EventType.EVENT_NAVIGATION_PREV);
			addEventInfo(EventType.EVENT_NAVIGATION_RELOAD);
      }
      catch (Exception e)
      {
         logCat.error("::Table - cannot link a TableEvents object to this table",
            e);
      }

      // enable log for digester;
      doLog = true;
   }

   /**
    *  Gets the table attribute of the TableEvent object
    *
    * @return  The table value
    */
   public Table getTable()
   {
      return table;
   }


   /**
    *  Sets the table attribute of the TableEvent object
    *
    * @param  table The new table value
    */
   public void setTable(Table table)
   {
      this.table = table;
   }


   /**
    *  Get the event id related to the input event type.
    *
    * @param eventType the event type
    * @return the event id related to the input event type, or null if
    *         the object does not exist
    */
   public String getEventId(String eventType)
   {
      String    id    = null;
      EventInfo einfo = getEventInfo(eventType);

      if (einfo != null)
      {
         id = einfo.getId();
      }

      return id;
   }


   /**
    *  Set a new event for the linked Table.
    *
    * @param einfo the  event info
    */
   public void addEventInfo(EventInfo einfo)
   {
      String eventType = einfo.getType();

      // store the event info object using its event type as the map key;
      if (eventMap.containsKey(eventType))
      {
         eventMap.remove(eventType);
      }

      eventMap.put(eventType, einfo);

      if (doLog)
      {
         logCat.info("::addEventInfo - set a new eventInfo with type, id ["
            + eventType + ", " + einfo.getId() + "]");
      }
   }


   /**
    *  PRIVATE methods here
    */
   /**
    *  Set a new event for the linked Table
    *
    * @param eventType  the event type string
    * @throws Exception if the system try to register two events with the same type
    */
   private void addEventInfo(String eventType) throws Exception
   {
      EventInfo einfo = new EventInfo();
      einfo.setTypeByObject(eventType);
      einfo.setId(eventType);
      addEventInfo(einfo);
   }


   /**
    *  Get the eventInfo object related to the input event type.
    *
    * @param eventType the event type string
    * @return the eventInfo object related to the input event type, or null if
    *         the object does not exist
    */
   private EventInfo getEventInfo(String eventType)
   {
      EventInfo einfo = null;

      if (eventMap.containsKey(eventType))
      {
         einfo = (EventInfo) eventMap.get(eventType);
      }

      return einfo;
   }
}
