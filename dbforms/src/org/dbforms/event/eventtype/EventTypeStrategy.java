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
 * EventTypeStrategy interface. Defines the strategy to identify an event group
 * or type value from an input event string.
 *
 * @author Luca Fossato
 *
 */
public interface EventTypeStrategy {
   /**
    * Gets the event group value.
    *
    * @param eventString the string that identifies an event type
    *
    * @return The event group value, or <code>EventType.EVENT_UNDEFINED</code>
    */
   int getEventGroup(String eventString);


   /**
    * Gets the event type value
    *
    * @param eventString the string that identifies an event type
    *
    * @return The event type value, or <code>EventType.EVENT_UNDEFINED</code>
    */
   String getEventType(String eventString);


   /**
    * Gets the EventTypeStrategy identifier.
    *
    * @return the EventTypeStrategy identifier
    */
   String getId();
}
