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
 *  Event information class
 *
 * @author  Luca Fossato
 * @created  20 novembre 2002
 */
public class EventInfo
{
    private String id = null;
    private String param = null;
    private String className = null;
    private int type;

    /**
     *  Constructor for the EventInfo object
     */
    public EventInfo()
    {
    }


    /**
     *  Gets the id attribute of the EventInfo object
     *
     * @return  The id value
     */
    public String getId()
    {
        //return id;
        return (id != null) ? id : param;
    }


    /**
     *  Gets the param attribute of the EventInfo object
     *
     * @return  The param value
     */
    public String getParam()
    {
        return param;
    }


    /**
     *  Gets the type attribute of the EventInfo object
     *
     * @return  The type value
     */
    public int getType()
    {
        return type;
    }


    /**
     *  Gets the className attribute of the EventInfo object
     *
     * @return  The className value
     */
    public String getClassName()
    {
        return className;
    }


    /**
     *  Sets the id attribute of the EventInfo object
     *
     * @param  id The new id value
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     *  Sets the param attribute of the EventInfo object
     *
     * @param  param The new param value
     */
    public void setParam(String param)
    {
        this.param = param;
    }


    /**
     *  Sets the type attribute of the EventInfo object
     *
     * @param  type The new type value
     */
    public void setType(int type)
    {
        this.type = type;
    }


    /**
     *  Sets the className attribute of the EventInfo object
     *
     * @param  className The new className value
     */
    public void setClassName(String className)
    {
        this.className = className;
    }


    /**
     *  Return the String representation of this object.
     *
     * @return  the String representation of this object
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        sb.append("event: id = ").append(getId()).append("; param = ").append(param).append("; className = ").append(className);

        return sb.toString();
    }
}
