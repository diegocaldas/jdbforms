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

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Table;

import javax.servlet.http.HttpServletRequest;



/**
 * The NavEventFactory abstract class provides the interface for a  Navigation
 * Event concrete class (see NavEventFactoryImpl).
 *
 * @author Luca Fossato
 */
public abstract class NavEventFactory extends EventFactory {
   /** classes used as constructor arguments types */
   static final Class[] actionConstructorArgsTypes = new Class[] {
                                                                  Table.class,
                                                                  HttpServletRequest.class,
                                                                  DbFormsConfig.class
                                                               };

   /** classes used as constructor arguments types */
   static final Class[] goToConstructorArgsTypes = new Class[] {
                                                                Table.class,
                                                                HttpServletRequest.class,
                                                                DbFormsConfig.class,
                                                                String.class
                                                             };

   /** classes used as constructor arguments types */
   static final Class[] goToConstructorArgsTypes2 = new Class[] {
                                                                 Table.class,
                                                                 HttpServletRequest.class,
                                                                 DbFormsConfig.class,
                                                                 String.class,
                                                                 String.class
                                                              };

   /**
    * Create and return a new navigation event.
    *
    * @param action the action string that identifies the web event
    * @param request the HttpServletRequest object
    * @param config the DbForms config object
    * @param table to use
    *
    * @return a new navigation event
    */
   public abstract NavigationEvent createEvent(String             action,
                                               HttpServletRequest request,
                                               DbFormsConfig      config,
                                               Table              table);


   /**
    * Create and return a new navGoto event.
    *
    * @param table the Table object
    * @param request the request object
    * @param config the configuration object
    * @param positionString the position string object
    *
    * @return a new navGoto event
    */
   public abstract NavigationEvent createGotoEvent(Table              table,
                                                   HttpServletRequest request,
                                                   DbFormsConfig      config,
                                                   String             positionString);


   /**
    * Create and return a new navGoto event.
    *
    * @param table the Table object
    * @param request the request object
    * @param config the configuration object
    * @param whereClause the SQL where clause
    * @param tableList the list of tables involved into the event procession
    *
    * @return a new navGoto event
    */
   public abstract NavigationEvent createGotoEvent(Table              table,
                                                   HttpServletRequest request,
                                                   DbFormsConfig      config,
                                                   String             whereClause,
                                                   String             tableList);
}
