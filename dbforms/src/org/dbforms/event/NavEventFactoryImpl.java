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

import org.dbforms.*;
import org.dbforms.util.*;
import javax.servlet.http.*;
import java.sql.*;
import org.apache.log4j.Category;



/**
 *  This is the Bounded Navigation Event Factory.
 *  <br>
 *  Provides bounded navigation events.
 *
 * @author  Luca Fossato <fossato@pow2.com>
 * @created  25 novembre 2002
 */
public class NavEventFactoryImpl extends NavEventFactory
{
    /** an handle to the unique NavigationEventFactory instance */
    private static NavEventFactory instance = null;

    /** classes used as constructor arguments types */
    private static Class[] constructorArgsTypes2 = new Class[]
    {
        Table.class, DbFormsConfig.class
    };


    /**
     *  Default constructor.
     */
    public NavEventFactoryImpl()
    {
    }


    /**
     *  Get the instance of the NavEventFactoryImpl class.
     *
     * @return  the instance of NavEventFactoryImpl class.
     */
    public static synchronized NavEventFactory instance()
    {
        if (instance == null)
        {
            instance = new NavEventFactoryImpl();
        }

        return instance;
    }


    /**
     *  create and return a new navigation event
     *
     * @param  action the action string that identifies the web event
     * @param  request the HttpServletRequest object
     * @param  config the DbForms config object
     * @return  a new navigation event
     */
    public NavigationEvent createEvent(String action, HttpServletRequest request, DbFormsConfig config)
    {
        Object[] constructorArgs = new Object[] { action, request, config };

        return createEvent(action, constructorArgsTypes, constructorArgs);
    }


    /**
     *  create and return a new navigation event
     *
     * @param  action the action string that identifies the web event
     * @param  table the Table object
     * @param  config the DbForms config object
     * @return  a new navigation event
     */
    public NavigationEvent createEvent(String action, Table table, DbFormsConfig config)
    {
        Object[] constructorArgs = new Object[] { table, config };

        return createEvent(action, constructorArgsTypes2, constructorArgs);
    }


    /**
     *  Create the navigation event
     *
     * @param  action               the action String
     * @param  constructorArgsTypes classes used as constructor arguments types
     * @param  constructorArgs      the NavigationEvent constructor arguments
     */
    private NavigationEvent createEvent(String action, Class[] constructorArgsTypes, Object[] constructorArgs)
    {
        String param = getEventParameter(action);
        EventInfo einfo = getEventInfo(param); // param should be the ID !!

        logCat.info("::createEvent - got event [" + einfo + "] from param [" + param + "]");

        return (NavigationEvent) getEvent(einfo, constructorArgsTypes, constructorArgs);
    }

    // -------------------------------------------------------------------------------------------------
    //  /**
    //   *  Create the NavNextEvent object.
    //   *
    //   * @param  action Description of the Parameter
    //   * @param  request Description of the Parameter
    //   * @param  config Description of the Parameter
    //   * @return  the NavNextEvent object.
    //   */
    //  public NavNextEvent createNavNextEvent(String action, HttpServletRequest request, DbFormsConfig config)
    //  {
    //    return new BoundedNavNextEventImpl(action, request, config);
    //  }
    //
    //
    //  /**
    //   * DOCUMENT ME!
    //   *
    //   * @param  table DOCUMENT ME!
    //   * @param  config DOCUMENT ME!
    //   * @return  DOCUMENT ME!
    //   */
    //  public NavNextEvent createNavNextEvent(Table table, DbFormsConfig config)
    //  {
    //    return new BoundedNavNextEventImpl(table, config);
    //  }
    //
    //
    //  /**
    //   *  Create the NavNextEvent object.
    //   *
    //   * @param  action Description of the Parameter
    //   * @param  request Description of the Parameter
    //   * @param  config Description of the Parameter
    //   * @return  the NavNextEvent object.
    //   */
    //  public NavPrevEvent createNavPrevEvent(String action, HttpServletRequest request, DbFormsConfig config)
    //  {
    //    return new BoundedNavPrevEventImpl(action, request, config);
    //  }
    //
    //
    //  /**
    //   * DOCUMENT ME!
    //   *
    //   * @param  table DOCUMENT ME!
    //   * @param  config DOCUMENT ME!
    //   * @return  DOCUMENT ME!
    //   */
    //  public NavPrevEvent createNavPrevEvent(Table table, DbFormsConfig config)
    //  {
    //    return new BoundedNavPrevEventImpl(table, config);
    //  }
}
