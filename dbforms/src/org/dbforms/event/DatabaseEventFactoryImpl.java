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
import java.util.Vector;
import javax.servlet.http.*;

import org.apache.log4j.Category;

import org.dbforms.util.ParseUtil;
import org.dbforms.util.ReflectionUtil;
import org.dbforms.config.*;
import org.dbforms.event.classic.*;
import org.dbforms.event.datalist.*;
import org.dbforms.event.eventtype.EventType;



/**
 * DatabaseEventFactoryImpl class.
 * Create DatabaseEvent objects.
 *
 * @author  Luca Fossato
 * @created  20 novembre 2002
 */
public class DatabaseEventFactoryImpl extends DatabaseEventFactory
{
   /** classes used as "keyInfo" constructor arguments types */
   private static Class[] keyInfoConstructorArgsTypes = new Class[]
      {
         Integer.class,
         String.class,
         HttpServletRequest.class,
         DbFormsConfig.class
      };

   /** an handle to the unique DatabaseEventFactory instance */
   private static DatabaseEventFactory instance = null;

   /**
    *  Get the instance of DatabaseEventFactory class.
    *
    * @return  the instance of DAO class.
    */
   public static synchronized DatabaseEventFactory instance()
   {
      if (instance == null)
      {
         instance = new DatabaseEventFactoryImpl();
      }

      return instance;
   }


   /**
    *  create and return a new database event
    *
    * @param  action the action string that identifies the web event
    * @param  request the HttpServletRequest object
    * @param  config the DbForms config object
    * @return  a new database event, or null if any error occurs
    */
   public WebEvent createEvent(String action, HttpServletRequest request,
      DbFormsConfig config)
   {
      WebEvent event           = null;
      Object[] constructorArgs = null;

      // get the event id of the destination table
      String    eventId = getEventIdFromDestinationTable(request, action);
      EventInfo einfo = getEventInfo(eventId);

      // debug
      logCat.info("::createEvent - got event [" + einfo + "] from action ["
         + action + "]");

      // instance "normal" database events;
      if (!isKeyInfoEvent(action))
      {
         constructorArgs    = new Object[]
            {
               action,
               request,
               config
            };
         event              = getEvent(einfo, constructorArgsTypes,
               constructorArgs);
      }

      // instance "keyInfo" database events;
      else
      {
         KeyInfo kInfo = getKeyInfo(action, request, config);

         // args are: tableId, keyId, request, config
         constructorArgs    = new Object[]
            {
               new Integer(kInfo.getTableId()),
               kInfo.getKeyId(),
               request,
               config
            };
         event = getEvent(einfo, keyInfoConstructorArgsTypes, constructorArgs);
      }

      return event;
   }


   /**
    *  Create and return a new UpdateEvent as secondary event.
    *
    * @param  tableId the table identifier
    * @param  keyId   the key   identifier
    * @param  request the HttpServletRequest object
    * @param  config  the DbForms config object
    * @return  The updateEvent object
    */
   public DatabaseEvent createUpdateEvent(int tableId, String keyId,
      HttpServletRequest request, DbFormsConfig config)
   {
      DatabaseEvent event           = null;
      Object[]      constructorArgs = new Object[]
         {
            new Integer(tableId),
            keyId,
            request,
            config
         };
      Table       table       = config.getTable(tableId);
      TableEvents tableEvents = table.getTableEvents();
      String      eventId     = tableEvents.getEventId(EventType.EVENT_DATABASE_UPDATE);
      EventInfo   einfo       = getEventInfo(eventId);
      event = (DatabaseEvent) getEvent(einfo, keyInfoConstructorArgsTypes,
            constructorArgs);

      return event;
   }


   /**
    *  PRIVATE METHODS here
    */
   /**
    *  Checck if the input action string
    *  defines a "keyInfo event"
    *
    * @param action the action string
    * @return true  if the input action string
    *               defines a "keyInfo event";
    *         false otherwise
    */
   private boolean isKeyInfoEvent(String action)
   {
      boolean keyInfo = false;

      if ((action.startsWith("ac_updatear_"))
               || action.startsWith("ac_deletear_"))
      {
         keyInfo = true;
      }

      //logCat.info("::isKeyInfoEvent - action [" + action + "] is related to a keyInfo event ? [" + keyInfo + "]");
      return keyInfo;
   }


   /**
    *  Get key informations for "key info" database events
    *
    * @param  action  the action string
    * @param  request the HttpServletRequest object
    * @return a keyInfo object containing the informations related to
    *         the tableId and the keyId taken from the input action string,
    *         or null if any error occurs
    */
   private KeyInfo getKeyInfo(String action, HttpServletRequest request,
      DbFormsConfig config)
   {
      KeyInfo keyInfo             = null;
      String  associatedRadioName = ParseUtil.getParameter(request,
            "data" + action + "_arname");
      String  keyString           = ParseUtil.getParameter(request,
            associatedRadioName);

      if (keyString != null)
      {
         int    tableId = ParseUtil.getEmbeddedStringAsInteger(keyString, 0, '_');
         String keyId = ParseUtil.getEmbeddedString(keyString, 1, '_');
         keyInfo = new KeyInfo();
         keyInfo.setTableId(tableId);
         keyInfo.setKeyId(keyId);
      }

      return keyInfo;
   }


   /**
    *  Initialize the default events.
    *
    * @exception Exception if any error occurs
    */
   protected void initializeEvents() throws Exception
   {
      addEventInfo(new EventInfo(EventType.EVENT_DATABASE_DELETE,
            "org.dbforms.event.classic.DeleteEvent"));
      addEventInfo(new EventInfo(EventType.EVENT_DATABASE_INSERT,
            "org.dbforms.event.classic.InsertEvent"));
      addEventInfo(new EventInfo(EventType.EVENT_DATABASE_UPDATE,
            "org.dbforms.event.classic.UpdateEvent"));
   }
}
