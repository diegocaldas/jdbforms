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
import org.dbforms.event.*;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

//import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.log4j.Category;



/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public class IsWebEvent extends TagSupport
{
    // logging category for this class
    static Category logCat = Category.getInstance(IsWebEvent.class.getName());
    private Boolean value;
    private String event;

    /**
     * Creates a new IsWebEvent object.
     */
    public IsWebEvent()
    {
        value = new Boolean("true");
        event = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws JspException DOCUMENT ME!
     */
    public int doStartTag() throws JspException
    {
        WebEvent webEvent = (WebEvent) pageContext.getRequest().getAttribute("webEvent");

        if ((webEvent == null) || (event == null) || (value == null))
        {
            logCat.debug("Can't do IsWebEvent with  webEvent: " + webEvent + "  event: " + event + "   value: " + value);

            return SKIP_BODY;
        }

        String className = webEvent.getClass().getName();
        className = className.substring("org.dbforms.event.".length(), className.length());

        boolean eventNameMatch = className.toUpperCase().indexOf(event.toUpperCase()) != -1;

        if (logCat.isDebugEnabled())
        {
            logCat.debug(" IsLocalWebEvent webEvent className: " + className + "    event: " + event + "  value: " + value);
        }

        return (value.booleanValue() == eventNameMatch) ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }


    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     */
    public final void setEvent(String str)
    {
        event = str;
    }


    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     */
    public final void setValue(String str)
    {
        value = new Boolean(str);
    }
}