/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * DbForms - a Rapid Application Development Framework
 * Copyright (C) 2001 Joachim Peer <j.peer@gmx.net> et al.
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
 * This event scrolls the current ResultSet to the next row of data.
 * <br>
 * Provides bounded navigation.
 *
 * @author Joe Peer <j.peer@gmx.net>, John Peterson <>
 */

public class BoundedNavNextEventImpl extends NavNextEvent
{
    /**
     *  Constructor
     */
    public BoundedNavNextEventImpl(String action, HttpServletRequest request, DbFormsConfig config)
    {
        super(action, request, config);
    }


    /**
     *  for call from localevent
     */
    public BoundedNavNextEventImpl(Table table, DbFormsConfig config)
    {
        super(table, config);
    }


    /**
     *
     */
    public ResultSetVector processEvent(FieldValue[] childFieldValues,
                                        FieldValue[] orderConstraint,
                                        int          count,
                                        String       firstPosition,
                                        String       lastPosition,
                                        Connection   con) 
        throws SQLException
    {
        ResultSetVector rsv;

        logCat.info("==>NavNextEvent");

        // select in given order everyting thats greater than lastpos
        table.fillWithValues(orderConstraint, lastPosition);
        rsv = table.doConstrainedSelect(table.getFields(),
                                        childFieldValues,
                                        orderConstraint,
                                        FieldValue.COMPARE_EXCLUSIVE,
                                        count, con);

        // change behavior to navLast if navNext finds no data
        // todo: make a option to allow original "navNew" behavior if desired
        if (rsv.size() == 0)
            {
                logCat.info("==>NavNextLastEvent");
                FieldValue.invert(orderConstraint);
                rsv = table.doConstrainedSelect(table.getFields(),
                                                childFieldValues,
                                                orderConstraint,
                                                FieldValue.COMPARE_NONE,
                                            count, con);
                FieldValue.invert(orderConstraint);
                rsv.flip();
            }
        return rsv;
    }
}
