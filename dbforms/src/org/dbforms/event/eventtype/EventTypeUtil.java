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
 * EventType utility class
 * 
 * @author Luca Fossato
 * @created 28 novembre 2002
 */
public class EventTypeUtil
{
   /**
    * Set the EventType class to get the event group and event type values from
    * the input action string. <br>
    * Set the EventTypeStrategyImpl class as strategy class.
    * 
    * @param action DOCUMENT ME!
    * 
    * @return a configured EventType class
    */
   public static EventType getEventType(String action)
   {
      EventType eventType = new EventType(new DbFormsEventTypeStrategy());
      eventType.setEventString(action);

      return eventType;
   }
}