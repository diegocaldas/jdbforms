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
 *  This is the Navigation Event Factory.
 *  <br>
 *  Provides unbounded navigation events.
 *
 *  @author Luca Fossato <fossato@pow2.com>
 */
public class NavEventFactoryImpl extends NavEventFactory
{
    /**
     *  Default constructor.
     */
    public NavEventFactoryImpl()
    {
    }

    /**
     *  Get the instance of the NavEventFactoryImpl class.
     *
     * @return the instance of NavEventFactoryImpl class.
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
     *  Create the NavNextEvent object.
     *
     *  @return the NavNextEvent object.
     */
    public NavNextEvent createNavNextEvent(String action, HttpServletRequest request, DbFormsConfig config)
    {
        return new NavNextEventImpl(action, request, config);
    }


    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     * @param config DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NavNextEvent createNavNextEvent(Table table, DbFormsConfig config)
    {
        return new NavNextEventImpl(table, config);
    }


    /**
     *  Create the NavNextEvent object.
     *
     *  @return the NavNextEvent object.
     */
    public NavPrevEvent createNavPrevEvent(String action, HttpServletRequest request, DbFormsConfig config)
    {
        return new NavPrevEventImpl(action, request, config);
    }


    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     * @param config DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NavPrevEvent createNavPrevEvent(Table table, DbFormsConfig config)
    {
        return new NavPrevEventImpl(table, config);
    }
}