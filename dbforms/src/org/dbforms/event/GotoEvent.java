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

import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import org.dbforms.*;
import org.dbforms.util.*;



/****
 *
 *  This event forces the controller to forward the current request
 *  to a Request-Dispatcher specified by the Application-Developer
 *  in a "org.dbforms.taglib.DbGotoButton".
 *
 * @author Joe Peer <j.peer@gmx.net>
 */
public abstract class GotoEvent extends NavigationEvent
{

    /**
     * DOCUMENT ME!
     *
     * @param childFieldValues DOCUMENT ME!
     * @param orderConstraint DOCUMENT ME!
     * @param count DOCUMENT ME!
     * @param firstPosition DOCUMENT ME!
     * @param lastPosition DOCUMENT ME!
     * @param con DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public abstract ResultSetVector processEvent(FieldValue[] childFieldValues, FieldValue[] orderConstraint, int count, String firstPosition, String lastPosition, Connection con) throws SQLException;
}
