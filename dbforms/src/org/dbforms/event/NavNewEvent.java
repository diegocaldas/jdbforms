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



/****
 *
 * <p>This event signalizes to the framework that the user wants to initializa a new dataset</p>
 * #fixme: lousy description
 *
 * @author Joe Peer <j.peer@gmx.net>
 */
public class NavNewEvent extends NavigationEvent
{
    static Category logCat = Category.getInstance(NavNewEvent.class.getName()); // logging category for this class

    /**
     * Creates a new NavNewEvent object.
     *
     * @param action DOCUMENT ME!
     * @param request DOCUMENT ME!
     * @param config DOCUMENT ME!
     */
    public NavNewEvent(String action, HttpServletRequest request, DbFormsConfig config)
    {
        this.config = config;
        tableId = ParseUtil.getEmbeddedStringAsInteger(action, 2, '_');
        this.table = config.getTable(tableId);
    }


    /**
     * Creates a new NavNewEvent object.
     *
     * @param table DOCUMENT ME!
     * @param config DOCUMENT ME!
     */
    public NavNewEvent(Table table, DbFormsConfig config)
    {
        this.table = table;
        this.tableId = table.getId();
        this.config = config;
    }

    /**
     * DOCUMENT ME!
     *
     * @param childFieldValues DOCUMENT ME!
     * @param orderConstraint DOCUMENT ME!
     * @param count DOCUMENT ME!
     * @param firstPost DOCUMENT ME!
     * @param lastPos DOCUMENT ME!
     * @param con DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ResultSetVector processEvent(FieldValue[] childFieldValues, FieldValue[] orderConstraint, int count, String firstPost, String lastPos, Connection con)
    {
        //	rsv.setPointer( rsv.size() ); //#fixme - this is not well thought through
        logCat.info("processed NavNewEvent");

        return null;
    }
}