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
import java.util.HashMap;



/**
 *  EventType context class.
 *  Uses strategy to select the event type value from an input
 *  string.
 *
 * @author  Luca Fossato
 * @created  28 novembre 2002
 */
public class EventType
{
   /** value of the database event */
   public static final int EVENT_GROUP_DATABASE = 0;

   /** value of the navigation event */
   public static final int EVENT_GROUP_NAVIGATION = 1;

   /**  value of the navigation goto event */
   public static final int EVENT_UNDEFINED = -1;

   /**  value of the database insert event */
   public static final String EVENT_DATABASE_INSERT = "insert";

   /**  value of the database update event */
   public static final String EVENT_DATABASE_UPDATE = "update";

   /**  value of the database delete event */
   public static final String EVENT_DATABASE_DELETE = "delete";

   /**  value of the navigation first event */
   public static final String EVENT_NAVIGATION_FIRST = "navFirst";

   /**  value of the navigation previous event */
   public static final String EVENT_NAVIGATION_PREV = "navPrev";

   /**  value of the navigation next event */
   public static final String EVENT_NAVIGATION_NEXT = "navNext";

   /**  value of the navigation last event */
   public static final String EVENT_NAVIGATION_LAST = "navLast";

   /**  value of the navigation new event */
   public static final String EVENT_NAVIGATION_NEW = "navNew";

   /**  value of the navigation goto event */
   public static final String EVENT_NAVIGATION_GOTO = "navGoto";

   /** the string that identifies the event type */
   private String eventString = null;

   /** eventType strategy class */
   private EventTypeStrategy eventTypeStrategy = null;

   /**
    *  Constructor; set the input EventTypeStrategy object as
    *  the default strategy.
    *
    * @param  eventTypeStrategy an EventTypeStrategy object
    */
   public EventType(EventTypeStrategy eventTypeStrategy)
   {
      this.eventTypeStrategy = eventTypeStrategy;
   }

   /**
    *  Gets the eventString attribute of the EventType object
    *
    * @return  The eventString value
    */
   public String getEventString()
   {
      return eventString;
   }


   /**
    *  Sets the eventString attribute of the EventType object
    *
    * @param  eventString The new eventString value
    */
   public void setEventString(String eventString)
   {
      this.eventString = eventString;
   }


   /**
    *  Gets the eventTypeStrategy attribute of the EventType object
    *
    * @return  The eventTypeStrategy value
    */
   public EventTypeStrategy getEventTypeStrategy()
   {
      return eventTypeStrategy;
   }


   /**
    *  Sets the eventTypeStrategy attribute of the EventType object
    *
    * @param  eventTypeStrategy The new eventTypeStrategy value
    */
   public void setEventTypeStrategy(EventTypeStrategy eventTypeStrategy)
   {
      this.eventTypeStrategy = eventTypeStrategy;
   }


   /**
    *  Gets the event group value
    *
    * @return  The event group value
    */
   public int getEventGroup()
   {
      int eventGroup = EVENT_UNDEFINED;

      if (eventTypeStrategy != null)
      {
         eventGroup = eventTypeStrategy.getEventGroup(eventString);
      }

      return eventGroup;
   }


   /**
    *  Gets the event type value
    *
    * @return  The event type value
    */
   public String getEventType()
   {
      String eventValue = String.valueOf(EVENT_UNDEFINED);

      if (eventTypeStrategy != null)
      {
         eventValue = eventTypeStrategy.getEventType(eventString);
      }

      return eventValue;
   }
}
