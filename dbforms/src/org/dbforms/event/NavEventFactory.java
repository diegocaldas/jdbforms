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
 *  NavigationeEvent Factory.
 *
 *  @author Luca Fossato <fossato@pow2.com>
 * @stereotype AbstractFactory, Singleton
 */
public abstract class NavEventFactory
{
    /** logging category for this class */
    static protected Category logCat = Category.getInstance(NavEventFactory.class.getName());

    /** singleton instance; */
    static protected NavEventFactory instance = null;

    /**
     *  Create the NavNextEvent object.
     *
     *  @return the NavNextEvent object.
     */
    public abstract NavNextEvent createNavNextEvent(String action, HttpServletRequest request, DbFormsConfig config);


    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     * @param config DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract NavNextEvent createNavNextEvent(Table table, DbFormsConfig config);


    /**
     *  Create the NavNextEvent object.
     *
     *  @return the NavNextEvent object.
     */
    public abstract NavPrevEvent createNavPrevEvent(String action, HttpServletRequest request, DbFormsConfig config);


    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     * @param config DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract NavPrevEvent createNavPrevEvent(Table table, DbFormsConfig config);
}