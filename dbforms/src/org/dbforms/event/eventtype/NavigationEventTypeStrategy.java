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



/**
 * Database EventTypeStrategy class. Provides implementations of the super class
 * methods for Navigation events.
 *
 * @author  Luca Fossato
 * @created  28 novembre 2002
 */
public class NavigationEventTypeStrategy implements EventTypeStrategy
{

   /**
    *  Gets the EventTypeStrategy identifier.
    *
    * @return  the EventTypeStrategy identifier
    */
   public String getId()
   {
      return "NavigationEventTypeStrategy";
   }


   /**
    *  Gets the event group value.
    *
    * @param  eventString the string that identifies an event type
    * @return  The event group value, or <code>EventType.EVENT_UNDEFINED</code>
    */
   public int getEventGroup(String eventString)
   {
      int eventGroup = EventType.EVENT_UNDEFINED;

      // could implement this with an hash map;
      if (
					   eventString.startsWith(EventType.EVENT_NAVIGATION_TRANSFER_FIRST)
					|| eventString.startsWith(EventType.EVENT_NAVIGATION_TRANSFER_PREV)
					|| eventString.startsWith(EventType.EVENT_NAVIGATION_TRANSFER_NEXT)
					|| eventString.startsWith(EventType.EVENT_NAVIGATION_TRANSFER_LAST) 
					|| eventString.startsWith(EventType.EVENT_NAVIGATION_TRANSFER_NEW)
					|| eventString.startsWith(EventType.EVENT_NAVIGATION_TRANSFER_RELOAD)
					|| eventString.startsWith(EventType.EVENT_NAVIGATION_TRANSFER_GOTO)
					|| eventString.startsWith(EventType.EVENT_NAVIGATION_TRANSFER_COPY)
               || eventString.equals(EventType.EVENT_NAVIGATION_FIRST)
               || eventString.equals(EventType.EVENT_NAVIGATION_PREV)
               || eventString.equals(EventType.EVENT_NAVIGATION_NEXT)
               || eventString.equals(EventType.EVENT_NAVIGATION_LAST) 
               || eventString.equals(EventType.EVENT_NAVIGATION_NEW)
					|| eventString.equals(EventType.EVENT_NAVIGATION_RELOAD)
					|| eventString.equals(EventType.EVENT_NAVIGATION_GOTO)
					|| eventString.equals(EventType.EVENT_NAVIGATION_COPY)
               || eventString.equals(EventType.EVENT_PAGE_RELOAD)
               || eventString.equals("goto")
			)
      {
         eventGroup = EventType.EVENT_GROUP_NAVIGATION;
      }

      return eventGroup;
   }


   /**
    *  Gets the event type value
    *
    * @param  eventString the string that identifies an event type
    * @return  The event type value, or <code>EventType.EVENT_UNDEFINED</code>
    */
   public String getEventType(String eventString)
   {
      String eventType = String.valueOf(EventType.EVENT_UNDEFINED);

      if (eventString.startsWith(EventType.EVENT_NAVIGATION_TRANSFER_FIRST) 
      			|| eventString.equals(EventType.EVENT_NAVIGATION_FIRST))
      {
         eventType = EventType.EVENT_NAVIGATION_FIRST;
      }
      else if (eventString.startsWith(EventType.EVENT_NAVIGATION_TRANSFER_PREV)
               || eventString.equals(EventType.EVENT_NAVIGATION_PREV))
      {
         eventType = EventType.EVENT_NAVIGATION_PREV;
      }
      else if (eventString.startsWith(EventType.EVENT_NAVIGATION_TRANSFER_NEXT)
               || eventString.equals(EventType.EVENT_NAVIGATION_NEXT))
      {
         eventType = EventType.EVENT_NAVIGATION_NEXT;
      }
      else if (eventString.startsWith(EventType.EVENT_NAVIGATION_TRANSFER_LAST)
               || eventString.equals(EventType.EVENT_NAVIGATION_LAST))
      {
         eventType = EventType.EVENT_NAVIGATION_LAST;
      }
      else if (eventString.startsWith(EventType.EVENT_NAVIGATION_TRANSFER_NEW)
               || eventString.equals(EventType.EVENT_NAVIGATION_NEW))
      {
         eventType = EventType.EVENT_NAVIGATION_NEW;
      }
      else if (eventString.startsWith(EventType.EVENT_NAVIGATION_TRANSFER_COPY)
               || eventString.equals(EventType.EVENT_NAVIGATION_COPY))
      {
         eventType = EventType.EVENT_NAVIGATION_COPY;
      }
		else if (eventString.startsWith(EventType.EVENT_NAVIGATION_TRANSFER_RELOAD)
					|| eventString.equals(EventType.EVENT_NAVIGATION_RELOAD))
		{
			eventType = EventType.EVENT_NAVIGATION_RELOAD;
		}
      else if (
                  	eventString.startsWith(EventType.EVENT_NAVIGATION_TRANSFER_GOTO) 
						|| eventString.equals(EventType.EVENT_NAVIGATION_GOTO)
						|| eventString.equals("goto")
					)
				   
      {
         eventType = EventType.EVENT_NAVIGATION_GOTO;
      }

      return eventType;
   }
}
