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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * EventTypeStrategy implementation. Defines the strategy to identify an event
 * group or type value from an input event string.
 *
 * @author Luca Fossato
 *
 */
public class DbFormsEventTypeStrategy extends CompositeEventTypeStrategy {
   /** logging category */
   private static Log logCat = LogFactory.getLog(DbFormsEventTypeStrategy.class
                                                 .getName());

   /**
    * Default constructor. <br>
    * Add the DatabaseEventTypeStrategy and the NavigationEventTypeStrategy
    * objects to the composite strategy list
    */
   public DbFormsEventTypeStrategy() {
      strategyList.add(new DatabaseEventTypeStrategy());
      strategyList.add(new NavigationEventTypeStrategy());
   }

   /**
    * Gets the event group value.
    *
    * @param eventString the string that identifies an event type
    *
    * @return The event group value, or <code>EventType.EVENT_UNDEFINED</code>
    */
   public int getEventGroup(String eventString) {
      int groupValue = EventType.EVENT_UNDEFINED;

      for (int i = 0; i < strategyList.size(); i++) {
         logCat.info("::getEventGroup - using [" + getChild(i).getId()
                     + "] strategy");
         groupValue = getChild(i)
                         .getEventGroup(eventString);

         if (groupValue != EventType.EVENT_UNDEFINED) {
            break;
         }
      }

      return groupValue;
   }


   /**
    * Gets the event type value.
    *
    * @param eventString the string that identifies an event type
    *
    * @return The event type value, or <code>EventType.EVENT_UNDEFINED</code>
    */
   public String getEventType(String eventString) {
      String typeValue = String.valueOf(EventType.EVENT_UNDEFINED);

      for (int i = 0; i < strategyList.size(); i++) {
         logCat.info("::getEventType - using [" + getChild(i).getId()
                     + "] strategy");
         typeValue = getChild(i)
                        .getEventType(eventString);

         if (!typeValue.equals(String.valueOf(EventType.EVENT_UNDEFINED))) {
            break;
         }
      }

      logCat.info("::getEventType - returned the event type [" + typeValue
                  + "] from [" + eventString + "]");

      return typeValue;
   }


   /**
    * Gets the EventTypeStrategy identifier.
    *
    * @return the EventTypeStrategy identifier
    */
   public String getId() {
      return "DbFormsEventTypeStrategy";
   }
}
