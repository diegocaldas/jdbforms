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
import java.util.ArrayList;
import org.apache.log4j.Category;
import org.dbforms.event.*;



/**
 *  Abstract composite EventTypeStrategy class.
 *
 * @author  Luca Fossato
 * @created  28 novembre 2002
 */
public abstract class CompositeEventTypeStrategy implements EventTypeStrategy
{
   /** the list of EventTypeStrategy objects */
   protected ArrayList strategyList = new ArrayList();

   /**
    *  Adds an EventTypeStrategy object to the Strategy list
    *
    * @param  strategy the EventTypeStrategy object to add to
    */
   public void add(EventTypeStrategy strategy)
   {
      strategyList.add(strategy);
   }


   /**
    *  Remove an EventTypeStrategy object to the Strategy list
    *
    * @param  strategy the EventTypeStrategy object to remove to
    * @return true if the object had removed from the list; false otherwise
    */
   public boolean remove(EventTypeStrategy strategy)
   {
      return strategyList.remove(strategy);
   }


   /**
    *  Gets the child EventTypeStrategy having the input index
    *
    * @return  the child EventTypeStrategy having the input index
    */
   public EventTypeStrategy getChild(int index)
   {
      return (EventTypeStrategy) strategyList.get(index);
   }
}
