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
package org.dbforms.event.eventtype;


import org.apache.log4j.Category;


/**
 * Database EventTypeStrategy class. Provides implementations of the super class
 * methods for Database events.
 *
 * @author  Luca Fossato
 * @created  28 novembre 2002
 */
public class DatabaseEventTypeStrategy implements EventTypeStrategy
{
   /** logging category */
   protected static Category logCat = Category.getInstance(DatabaseEventTypeStrategy.class
         .getName());

   /**
    *  Gets the EventTypeStrategy identifier.
    *
    * @return  the EventTypeStrategy identifier
    */
   public String getId()
   {
      return "DatabaseEventTypeStrategy";
   }


   /**
    *  Gets the eventGroup attribute of the EventTypeStrategyImpl object
    *
    * @param  eventString Description of the Parameter
    * @return  The eventGroup value, or EventType.EVENT_UNDEFINED otherwise
    */
   public int getEventGroup(String eventString)
   {
      int eventGroup = EventType.EVENT_UNDEFINED;

      if (        (eventString.startsWith("ac_insert_"))
               || (eventString.startsWith("ac_update_"))
               || (eventString.startsWith("ac_updatear_"))
               || (eventString.startsWith("ac_delete_"))
               || (eventString.startsWith("ac_deletear_"))
         )
      {
         eventGroup = EventType.EVENT_GROUP_DATABASE;
      }

      return eventGroup;
   }


   /**
    *  Gets the eventType attribute of the EventTypeStrategyImpl object
    *
    * @param  eventString Description of the Parameter
    * @return  The eventType value
    */
   public String getEventType(String eventString)
   {
      String eventType = String.valueOf(EventType.EVENT_UNDEFINED);

      if (eventString.startsWith("ac_insert_"))
      {
         eventType = EventType.EVENT_DATABASE_INSERT;
      }
      else if (eventString.startsWith("ac_update_")
               || eventString.startsWith("ac_updatear_"))
      {
         eventType = EventType.EVENT_DATABASE_UPDATE;
      }
      else if (eventString.startsWith("ac_delete_")
               || eventString.startsWith("ac_deletear_"))
      {
         eventType = EventType.EVENT_DATABASE_DELETE;
      }

      return eventType;
   }
}
