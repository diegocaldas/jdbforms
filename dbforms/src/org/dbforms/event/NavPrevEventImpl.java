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
 *  This event scrolls the current ResultSet to the previous row of data.
 *  <br>
 *  Provides unbounded navigation.
 *
 * @author Joe Peer <j.peer@gmx.net>
 */

public class NavPrevEventImpl extends NavPrevEvent 
{
    /**
     *  Constructor
     */
    public NavPrevEventImpl(String action, HttpServletRequest request, DbFormsConfig config)
    {
        super(action, request, config);
    }


    /**
     *  for call from localevent
     */
    public NavPrevEventImpl(Table table, DbFormsConfig config) 
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
                                        Connection con)
        throws SQLException 
    {
        logCat.info("==>NavPrevEvent");
        
        // select in inverted order everyting thats greater than firstpos
        table.fillWithValues(orderConstraint, firstPosition);
        FieldValue.invert(orderConstraint);
        ResultSetVector resultSetVector = table.doConstrainedSelect(table.getFields(), 
                                                                    childFieldValues, 
                                                                    orderConstraint, 
                                                                    FieldValue.COMPARE_EXCLUSIVE, 
                                                                    count, con);
        FieldValue.invert(orderConstraint);
        resultSetVector.flip();
        return resultSetVector;
    }
}
