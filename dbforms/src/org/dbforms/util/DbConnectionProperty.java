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

package org.dbforms.util;

/**
 *
 * Adds property support to DbConnection
 *
 *
 * @author Henner Kollmann (Henner.Kollmann@gmx.de)
 */
public class DbConnectionProperty
{
    private String name;
    private String value;

    /**
     * Returns the name.
     * @return String
     */
    public String getName()
    {
        return name;
    }


    /**
     * Returns the value.
     * @return String
     */
    public String getValue()
    {
        return value;
    }


    /**
     * Sets the name.
     * @param name The name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Sets the value.
     * @param value The value to set
     */
    public void setValue(String value)
    {
        this.value = value;
    }
}