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
import java.sql.*;
import javax.servlet.http.*;
import org.dbforms.*;
import org.apache.log4j.Category;



/****
 *
 * <p>abstract base class for all web-events</p>
 *
 * <p>implementations of this class will be generated by the controller/EventEngine by
 * dispatchting the incoming HTTP-request from the client. In fact, the WebEvent classes itselves
 * may provide methods (constructors) to help parsing the request data.</p>
 *
 * <p>WebEvents may be processed by the controller and/or by the custom tags at JSP-side</p>
 *
 * @author Joe Peer <j.peer@gmx.net>
 */
public abstract class WebEvent
{
    static Category logCat = Category.getInstance(WebEvent.class.getName()); // logging category for this class

    /** DOCUMENT ME! */
    protected HttpServletRequest request;

    /** DOCUMENT ME! */
    protected DbFormsConfig config;

    /** DOCUMENT ME! */
    protected int tableId; // which table does the event operate on?

    /** DOCUMENT ME! */
    protected String followUp;

    /** DOCUMENT ME! */
    protected String followUpOnError;

    /**
     * DOCUMENT ME!
     *
     * @param tableId DOCUMENT ME!
     */
    public void setTableId(int tableId)
    {
        this.tableId = tableId;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTableId()
    {
        return tableId;
    }


    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     */
    public void setConfig(DbFormsConfig config)
    {
        this.config = config;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DbFormsConfig getConfig()
    {
        return config;
    }


    /**
     * DOCUMENT ME!
     *
     * @param followUp DOCUMENT ME!
     */
    public void setFollowUp(String followUp)
    {
        this.followUp = followUp;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFollowUp()
    {
        return followUp;
    }


    /**
     * DOCUMENT ME!
     *
     * @param privileg DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean hasUserPrivileg(int privileg)
    {
        return config.getTable(tableId).hasUserPrivileg(request, privileg);
    }


    /**
     * Gets the followUpOnError
     * @return Returns a String
     */
    public String getFollowUpOnError()
    {
        return followUpOnError;
    }


    /**
     * Sets the followUpOnError
     * @param followUpOnError The followUpOnError to set
     */
    public void setFollowUpOnError(String followUpOnError)
    {
        this.followUpOnError = followUpOnError;
    }
}