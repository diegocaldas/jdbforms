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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.EventInfo;
import org.dbforms.config.Table;
import org.dbforms.config.TableEvents;

import org.dbforms.event.eventtype.EventTypeUtil;

import org.dbforms.util.ReflectionUtil;
import org.dbforms.util.Util;

import java.util.HashMap;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;



/**
 * The EventFactory abstract class provides the interface and the
 * implementation     of protected methods used by eventFactory subclasses
 * (see NavEventFactory and DatabaseEventFactory).
 *
 * @author Luca Fossato
 *
 */
public abstract class EventFactory {
   /** logging category */
   private static Log logCat = LogFactory.getLog(EventFactory.class);

   /** classes used as "non keyInfo" constructor arguments types */
   protected static final Class[] constructorArgsTypes = new Class[] {
                                                            String.class,
                                                            HttpServletRequest.class,
                                                            DbFormsConfig.class
                                                         };

   /** map of supported event */
   protected HashMap eventInfoMap = null;

   /**
    * Creates a new EventFactory object.
    */
   protected EventFactory() {
      eventInfoMap = new HashMap();

      try {
         initializeEvents();
      } catch (Exception e) {
         logCat.error("::EventFactory - cannot initialize the factory events", e);
      }
   }

   /**
    * Create and return a new event.
    *
    * @param action the action string that identifies the web event
    * @param request the HttpServletRequest object
    * @param config the DbForms config object
    *
    * @return a new navigation event
    */
   public abstract WebEvent createEvent(String             action,
                                        HttpServletRequest request,
                                        DbFormsConfig      config);


   /**
    * Add a new EventInfo object into the factory. <br>
    * The EventInfo name must be unique.
    *
    * @param einfo the EventInfo object to add to
    */
   public void addEventInfo(EventInfo einfo) {
      if (eventInfoMap != null) {
         // note: events are registered using their id value (if that value is not null),
         // or their type value (if the id is null or empty).
         String id = einfo.getId();

         // event info override;
         if (eventInfoMap.containsKey(id)) {
            EventInfo prevEinfo = (EventInfo) eventInfoMap.get(id);

            if (prevEinfo != null) {
               String prevClassName = prevEinfo.getClassName();
               logCat.warn(new StringBuffer("::addEventInfo - the event information having id, class [").append(id).append(", ").append(einfo
                                                                                                                                        .getClassName()).append("] overrides the event class [").append(prevClassName).append("]").toString());
            }
         }

         // event info registration;
         eventInfoMap.put(id, einfo);
         logCat.info(new StringBuffer("::addEventInfo - event info having id, type, class [").append(id).append(", ").append(einfo
                                                                                                                             .getType()).append(", ").append(einfo
                                                                                                                                                             .getClassName()).append("] registered"));
      }
   }


   /**
    * PROTECTED methods here
    *
    * @throws Exception DOCUMENT ME!
    */
   /**
    * Initialize the default events.
    *
    * @exception Exception if any error occurs
    */
   protected abstract void initializeEvents() throws Exception;


   /**
    * Instance a new DatabaseEvent object.
    *
    * @param einfo the EventInfo object
    * @param constructorArgsTypes array of constructor argument classes
    * @param constructorArgs array of constructor argument objects
    *
    * @return the event object, or null if any problem occurs
    */
   protected WebEvent getEvent(EventInfo einfo,
                               Class[]   constructorArgsTypes,
                               Object[]  constructorArgs) {
      WebEvent event = null;

      if (einfo != null) {
         try {
            event = (WebEvent) ReflectionUtil.newInstance(einfo.getClassName(),
                                                          constructorArgsTypes,
                                                          constructorArgs);

            // set a new Properties object into the event;
            event.setProperties(new Properties(einfo.getProperties()));
            event.setType(einfo.getType());
         } catch (Exception e) {
            logCat.error("::getEvent - cannot create the new event [" + einfo
                         + "]", e);
         }
      }

      return event;
   }


   /**
    * Get the Event identifier from the destination table related to the input
    * action string
    *
    * @param request the request object
    * @param action the action string
    *
    * @return the Event identifier  from the destination table, or null if any
    *         error occurs
    */
   protected String getEventIdFromDestinationTable(HttpServletRequest request,
                                                   String             action) {
      DbFormsConfig config    = null;
      Table         table     = null;
      String        eventId   = null;
      String        eventType = EventTypeUtil.getEventType(action)
                                             .getEventType();

      try {
         config = DbFormsConfigRegistry.instance()
                                       .lookup();
      } catch (Exception e) {
         logCat.error("::getEventIdFromDestinationTable - cannot get the config object from the DbFormsConfigRegistry");
      }

      if (config != null) {
         // try to retrieve a valid  target table name from the request;
         // if it's null, try to retrieve the table id from the action string.
         String tableName = EventHelper.getDestinationTableName(request, action);

         if (!Util.isNull(tableName)) {
            table = config.getTableByName(tableName);
         } else {
            int tableId = EventHelper.getTableId(action);
            table = config.getTable(tableId);
         }

         if (!Util.isNull(eventType)) {
            TableEvents tableEvents;

            if (table != null) {
               tableEvents = table.getTableEvents();
            } else {
               tableEvents = new TableEvents();
            }

            eventId = tableEvents.getEventId(eventType);
         }
      }

      logCat.info("::getEventIdFromDestinationTable - eventId = [" + eventId
                  + "]");

      return eventId;
   }


   /**
    * Get the Event identifier from the destination table related to the input
    * action string
    *
    * @param table the table object
    * @param action the action string
    *
    * @return the Event identifier  from the destination table, or null if any
    *         error occurs
    */
   protected String getEventIdFromDestinationTable(Table  table,
                                                   String action) {
      TableEvents tableEvents = table.getTableEvents();
      String      eventType = EventTypeUtil.getEventType(action)
                                           .getEventType();
      String      eventId = tableEvents.getEventId(eventType);

      logCat.info("::getEventIdFromDestinationTable - eventId = [" + eventId
                  + "]");

      return eventId;
   }


   /**
    * Get the EventInfo object having the input identifier.
    *
    * @param id the EventInfo identifier
    *
    * @return the EventInfo object having the input identifier, or null if that
    *         object does not exist
    */
   protected EventInfo getEventInfo(String id) {
      EventInfo einfo = null;

      if ((eventInfoMap != null) && (eventInfoMap.containsKey(id))) {
         einfo = (EventInfo) eventInfoMap.get(id);
      } else {
         logCat.error("::getEventInfo - event having id [" + id
                      + "] is not registered into the factory, returning a NULL event");
      }

      return einfo;
   }
}
