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


/**
 *  KeyInfo class.
 *
 * @author  Luca Fossato
 * @created  23 novembre 2002
 */
public class KeyInfo
{
    private int tableId;
    private String keyId;

    /**
     *   Constructor for the KeyInfo object
     */
    public KeyInfo()
    {
    }

    /**
     *  Gets the tableId attribute of the KeyInfo object
     *
     * @return  The tableId value
     */
    public int getTableId()
    {
        return tableId;
    }


    /**
     *  Sets the tableId attribute of the KeyInfo object
     *
     * @param  tableId The new tableId value
     */
    public void setTableId(int tableId)
    {
        this.tableId = tableId;
    }


    /**
     *  Sets the keyId attribute of the KeyInfo object
     *
     * @param  keyId The new keyId value
     */
    public void setKeyId(String keyId)
    {
        this.keyId = keyId;
    }


    /**
     *  Gets the keyId attribute of the KeyInfo object
     *
     * @return  The keyId value
     */
    public String getKeyId()
    {
        return keyId;
    }
}
