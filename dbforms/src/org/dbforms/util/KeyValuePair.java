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


/**
this class was introduced in DbForms 0.9
it is used for holding simple data
for example in EmbeddedData - Classes
*/
package org.dbforms.util;

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

/**
this class was introduced in DbForms 0.9
it is used for holding simple data
for example in EmbeddedData - Classes
*/
public class KeyValuePair
{
    private String key;
    private String value;

    /**
     * Creates a new KeyValuePair object.
     */
    public KeyValuePair()
    {
    }


    /**
     * Creates a new KeyValuePair object.
     *
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public KeyValuePair(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     */
    public void setKey(String key)
    {
        this.key = key;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getKey()
    {
        return key;
    }


    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void setValue(String value)
    {
        this.value = value;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getValue()
    {
        return value;
    }
}