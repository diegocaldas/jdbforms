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

package org.dbforms.taglib;
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.dbforms.util.MessageResources;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import org.dbforms.*;
import org.apache.log4j.Category;



/****
 *
 * abstract base class for buttons
 * supports 3 types of Buttons: #fixme - docu
 *
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */
public abstract class DbBaseButtonTag extends DbBaseHandlerTag
{
    static Category logCat = Category.getInstance(DbBaseButtonTag.class.getName()); // logging category for this class

    /** DOCUMENT ME! */
    protected static final int FLAVOR_STANDARD = 0;

    /** DOCUMENT ME! */
    protected static final int FLAVOR_IMAGE = 1;

    /** DOCUMENT ME! */
    protected static final int FLAVOR_MODERN = 2;

    /** DOCUMENT ME! */
    protected String followUp;

    /** DOCUMENT ME! */
    protected String followUpOnError;

    /** DOCUMENT ME! */
    protected Table table;

    /** DOCUMENT ME! */
    protected String flavor; // the user desides which html tags/constructions should be used to render this tag

    /** DOCUMENT ME! */
    protected int choosenFlavor = 0; // default = standard

    /** DOCUMENT ME! */
    protected String caption; // used if flavor is "standard"

    /** DOCUMENT ME! */
    protected String src; // used if flavor is "image"

    /** DOCUMENT ME! */
    protected String alt; // used if flavor is "image"

    /** DOCUMENT ME! */
    protected String border; // used to set html border attribute"

    /**
     * DOCUMENT ME!
     *
     * @param flavor DOCUMENT ME!
     */
    public void setFlavor(String flavor)
    {
        this.flavor = flavor;

        if ("image".equals(flavor))
        {
            choosenFlavor = FLAVOR_IMAGE;
        }
        else if ("modern".equals(flavor))
        {
            choosenFlavor = FLAVOR_MODERN;
        }
        else
        {
            choosenFlavor = FLAVOR_STANDARD;
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFlavor()
    {
        return flavor;
    }


    /**
     * DOCUMENT ME!
     *
     * @param caption DOCUMENT ME!
     */
    public void setCaption(String caption)
    {
        String message = null;

        this.caption = caption;

        // If the caption is not null and the resources="true" attribut
        if ((caption != null) && parentForm.getCaptionResource().equals("true"))
        {
            try
            {
                Locale locale = MessageResources.getLocale((HttpServletRequest) pageContext.getRequest());

                message = MessageResources.getMessage(caption, locale);

                if (message != null)
                {
                    this.caption = message;
                }
            }
            catch (Exception e)
            {
                logCat.debug("setCaption(" + caption + ") Exception : " + e.getMessage());
            }
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCaption()
    {
        return caption;
    }


    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     */
    public void setSrc(String src)
    {
        this.src = src;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSrc()
    {
        return src;
    }


    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     */
    public void setAlt(String src)
    {
        this.alt = src;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAlt()
    {
        return alt;
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
     * @param pageContext DOCUMENT ME!
     */
    public void setPageContext(final javax.servlet.jsp.PageContext pageContext)
    {
        super.setPageContext(pageContext);
    }


    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     */
    public void setParent(final javax.servlet.jsp.tagext.Tag parent)
    {
        super.setParent(parent);
        table = parentForm.getTable();
    }


    /**
    returns beginnings of tags with attributes defining type/value/[src/alt - if image]
    */
    protected String getButtonBegin()
    {
        StringBuffer buf = new StringBuffer();

        switch (choosenFlavor)
        {
            case FLAVOR_IMAGE:
                buf.append("<input type=\"image\" ");

                if (src != null)
                {
                    buf.append(" src=\"");
                    buf.append(src);
                    buf.append("\" ");
                }

                if (alt != null)
                {
                    buf.append(" alt=\"");
                    buf.append(alt);
                    buf.append("\" ");
                }

                break;

            case FLAVOR_MODERN:
                buf.append("<button type=\"submit\" ");

                if (caption != null)
                {
                    buf.append(" value=\""); // not visible - neither on ie nor netscape (?) [only tags embedded in this tags get rendered !]
                    buf.append(caption);
                    buf.append("\" ");
                }

                break;

            default: // FLAVOR_STANDARD
                buf.append("<input type=\"submit\" ");

                if (caption != null)
                {
                    buf.append(" value=\""); // not very useful: this is _not_ visible!
                    buf.append(caption);
                    buf.append("\" ");
                }
        }


        //logCat.info("prepareEventHandlers()="+prepareEventHandlers());
        buf.append(prepareEventHandlers());
        buf.append(prepareStyles());

        return buf.toString();
    }


    /**
    renders tag containing additional information about that button:
    ie followUp, associatedRadio, etc.
    */
    protected String getDataTag(String primaryTagName, String dataKey, String dataValue)
    {
        StringBuffer tagBuf = new StringBuffer();
        tagBuf.append("<input type=\"hidden\" name=\"data");
        tagBuf.append(primaryTagName);
        tagBuf.append("_");
        tagBuf.append(dataKey);
        tagBuf.append("\" value =\"");
        tagBuf.append(dataValue);
        tagBuf.append("\">");

        return tagBuf.toString();
    }


    /**
     * Prepares the style attributes for inclusion in the component's HTML tag.
     * @return The prepared String for inclusion in the HTML tag.
     */
    protected String prepareStyles()
    {
        StringBuffer styles = new StringBuffer(super.prepareStyles());

        if (border != null)
        {
            styles.append(" border=\"");
            styles.append(border);
            styles.append("\"");
        }

        return styles.toString();
    }


    /**
     * Gets the border
     * @return Returns a String
     */
    public String getBorder()
    {
        return border;
    }


    /**
     * Sets the border
     * @param border The border to set
     */
    public void setBorder(String border)
    {
        this.border = border;
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