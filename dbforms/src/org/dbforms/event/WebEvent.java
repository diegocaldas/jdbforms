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

import java.util.Properties;
import java.sql.*;
import javax.servlet.http.*;

import org.apache.log4j.Category;

import org.dbforms.*;



/**
 * Abstract base class for all web-events.
 * <br>
 * Implementations of this class will be generated by the controller/EventEngine by
 * dispatchting the incoming HTTP-request from the client. In fact, the WebEvent classes itselves
 * may provide methods (constructors) to help parsing the request data.
 * <br>
 * WebEvents may be processed by the controller and/or by the custom tags at JSP-side.
 *
 * @author  Joe Peer <j.peer@gmx.net>
 * @created  4 dicembre 2002
 */
public abstract class WebEvent
{
    /** logging category for this class */
    static Category logCat = Category.getInstance(WebEvent.class.getName());

    /** the  HttpServletRequest object */
    protected HttpServletRequest request;

    /** the configuration object */
    protected DbFormsConfig config;

    /** table identifier that tells on which table does the event operate on */
    protected int tableId;

	/** table  that tells on which table does the event operate on */
	protected Table table;

    /** followUp URL string */
    protected String followUp;

    /** followUp URL string used when an error occurs */
    protected String followUpOnError;

    /** event properties */
    protected Properties properties = null;

	/** type of event */
	private String type = "UNDEFINED";

	public WebEvent(int tableId, HttpServletRequest request, DbFormsConfig config) {
		this.tableId = tableId;
		this.table = config.getTable(tableId);
		this.request = request;
		this.config = config;
	}

    /**
     *  Gets the config attribute of the WebEvent object
     *
     * @return  The config value
     */
    public DbFormsConfig getConfig()
    {
        return config;
    }


    /**
     *  Gets the followUp attribute of the WebEvent object
     *
     * @return  The followUp value
     */
    public String getFollowUp()
    {
        return followUp;
    }


    /**
     *  Gets the followUpOnError attribute of the WebEvent object
     *
     * @return  The followUpOnError value
     */
    public String getFollowUpOnError()
    {
        return followUpOnError;
    }


    /**
     *  Gets the request attribute of the WebEvent object
     *
     * @return  The request value
     */
    public HttpServletRequest getRequest()
    {
        return request;
    }

	/**
	 *  sets the request attribute of the WebEvent object
    *
    *   @param  request The new request value
	 *
	 */
	public void setRequest(HttpServletRequest request)
	{
		 this.request = request;
	}


    /**
     *  Gets the tableId attribute of the WebEvent object
     *
     * @return  The tableId value
     */
    public int getTableId()
    {
        return tableId;
    }


    /**
     *  Sets the followUp attribute of the WebEvent object
     *
     * @param  followUp The new followUp value
     */
    public void setFollowUp(String followUp)
    {
        this.followUp = followUp;
    }


    /**
     *  Sets the followUpOnError attribute of the WebEvent object
     *
     * @param  followUpOnError The new followUpOnError value
     */
    public void setFollowUpOnError(String followUpOnError)
    {
        this.followUpOnError = followUpOnError;
    }


    /**
     *  Gets the properties attribute of the WebEvent object
     *
     * @return  The properties value
     */
    public Properties getProperties()
    {
        return properties;
    }


    /**
     *  Sets the properties attribute of the WebEvent object
     *
     * @param  properties The new properties value
     */
    public void setProperties(Properties properties)
    {
        this.properties = properties;
    }



    /**
     * Check if the current user has got the input privilege
     *
     * @param  privileg the privilege value
     * @return  true  if the current user has got the input privilege,
     *         false otherwise
     */
    protected boolean hasUserPrivileg(int privileg)
    {
        return config.getTable(tableId).hasUserPrivileg(request, privileg);
    }


	/**
	 * @return String
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * @param type The type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}