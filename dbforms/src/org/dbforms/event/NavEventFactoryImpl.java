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
import org.dbforms.config.EventInfo;
import org.dbforms.config.Table;

import org.dbforms.event.eventtype.EventType;

import javax.servlet.http.HttpServletRequest;



/**
 * Implementation of a Navigation Event Factory.
 *
 * @author Luca Fossato
 *
 */
public class NavEventFactoryImpl extends NavEventFactory {
   /** an handle to the unique NavigationEventFactory instance */
   private static NavEventFactory instance = null;
   private static Log             logCat = LogFactory.getLog(NavEventFactoryImpl.class);

   /**
    * Default constructor.
    */
   public NavEventFactoryImpl() {
   }

   /**
    * Get the instance of the NavEventFactoryImpl class.
    *
    * @return the instance of NavEventFactoryImpl class.
    */
   public static synchronized NavEventFactory instance() {
      if (instance == null) {
         instance = new NavEventFactoryImpl();
      }

      return instance;
   }


   /**
    * create and return a new navigation event
    *
    * @param action the action string that identifies the web event
    * @param request the HttpServletRequest object
    * @param config the DbForms config object
    *
    * @return a new navigation event
    */
   public WebEvent createEvent(String             action,
                               HttpServletRequest request,
                               DbFormsConfig      config) {
      Object[] constructorArgs = new Object[] {
                                    action,
                                    request,
                                    config
                                 };
      String   eventId = getEventIdFromDestinationTable(request, action);
      EventInfo einfo  = getEventInfo(eventId);

      // debug
      logCat.info("::createEvent - got event [" + einfo + "] from action ["
                  + action + "]");

      return getEvent(einfo, constructorArgsTypes, constructorArgs);
   }


   /**
    * create and return a new navigation event. Called from local web event in
    * DbForms!
    *
    * @param action the action string that identifies the web event
    * @param request DOCUMENT ME!
    * @param config the DbForms config object
    * @param table the Table object
    *
    * @return a new navigation event
    */
   public NavigationEvent createEvent(String             action,
                                      HttpServletRequest request,
                                      DbFormsConfig      config,
                                      Table              table) {
      Object[] constructorArgs = new Object[] {
                                    table,
                                    request,
                                    config
                                 };
      String   eventId = getEventIdFromDestinationTable(table, action);
      EventInfo einfo  = getEventInfo(eventId);

      // debug
      logCat.info("::createEvent - got event [" + einfo + "] from action ["
                  + action + "]");

      return (NavigationEvent) getEvent(einfo, actionConstructorArgsTypes,
                                        constructorArgs);
   }


   /**
    * Create and return a new navGoto event. Used by the view (DbFormTag) to
    * create a gotoEvent
    *
    * @param table the Table object
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    * @param positionString the position string object
    *
    * @return a new navGoto event
    */
   public NavigationEvent createGotoEvent(Table              table,
                                          HttpServletRequest request,
                                          DbFormsConfig      config,
                                          String             positionString) {
      String    eventId = table.getTableEvents()
                               .getEventId(EventType.EVENT_NAVIGATION_GOTO);
      EventInfo einfo           = getEventInfo(eventId);
      Object[]  constructorArgs = new Object[] {
                                     table,
                                     request,
                                     config,
                                     positionString
                                  };

      return (NavigationEvent) getEvent(einfo, goToConstructorArgsTypes,
                                        constructorArgs);
   }


   /**
    * Create and return a new navGoto event. Used by the view (DbFormTag) to
    * create a gotoEvent for free form select
    *
    * @param table the Table object
    * @param request the position string object
    * @param config DOCUMENT ME!
    * @param whereClause DOCUMENT ME!
    * @param tableList DOCUMENT ME!
    *
    * @return a new navGoto event
    */
   public NavigationEvent createGotoEvent(Table              table,
                                          HttpServletRequest request,
                                          DbFormsConfig      config,
                                          String             whereClause,
                                          String             tableList) {
      String    eventId = table.getTableEvents()
                               .getEventId(EventType.EVENT_NAVIGATION_GOTO);
      EventInfo einfo           = getEventInfo(eventId);
      Object[]  constructorArgs = new Object[] {
                                     table,
                                     request,
                                     config,
                                     whereClause,
                                     tableList
                                  };

      return (NavigationEvent) getEvent(einfo, goToConstructorArgsTypes2,
                                        constructorArgs);
   }


   /**
    * Initialize the default events.
    *
    * @exception Exception if any error occurs
    */
   protected void initializeEvents() throws Exception {
      addEventInfo(new EventInfo(EventType.EVENT_NAVIGATION_NEW,
                                 "org.dbforms.event.NavNewEvent"));
      addEventInfo(new EventInfo(EventType.EVENT_NAVIGATION_COPY,
                                 "org.dbforms.event.NavCopyEvent"));
      addEventInfo(new EventInfo(EventType.EVENT_NAVIGATION_GOTO,
                                 "org.dbforms.event.datalist.GotoEvent"));
      addEventInfo(new EventInfo(EventType.EVENT_NAVIGATION_FIRST,
                                 "org.dbforms.event.datalist.NavFirstEvent"));
      addEventInfo(new EventInfo(EventType.EVENT_NAVIGATION_LAST,
                                 "org.dbforms.event.datalist.NavLastEvent"));
      addEventInfo(new EventInfo(EventType.EVENT_NAVIGATION_NEXT,
                                 "org.dbforms.event.datalist.NavNextEvent"));
      addEventInfo(new EventInfo(EventType.EVENT_NAVIGATION_PREV,
                                 "org.dbforms.event.datalist.NavPrevEvent"));

      addEventInfo(new EventInfo(EventType.EVENT_NAVIGATION_RELOAD,
                                 "org.dbforms.event.datalist.ReloadEvent"));
   }
}
