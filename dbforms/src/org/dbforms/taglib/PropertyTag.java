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

import org.dbforms.interfaces.IPropertyMap;

import javax.servlet.jsp.JspException;


/** 
 * Tag &lt;attribute&gt; which defines a key/value pair which is set in the parent
 * tag. This must implement the Parameterized interface.
 * @see IPropertyMap
 * @author  Henner Kollmann 
 */
public class PropertyTag extends AbstractDbBaseHandlerTag
	implements javax.servlet.jsp.tagext.TryCatchFinally {

    private String name;
    private String value;

    public int doStartTag() throws JspException {
        if ((getParent() != null)
                  && getParent() instanceof IPropertyMap) {
           ((IPropertyMap) getParent()).addProperty(name, value);
           } else {
           throw new JspException("AttributeTag element must be placed inside a IParameterized element!");
        }
        return SKIP_BODY;
     }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String val) {
        this.value = val;
    }

    
    /**
     * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
     */
    public void doCatch(Throwable t) throws Throwable {
       throw t;
    }

    
    /**
     * @see javax.servlet.jsp.tagext.TryCatchFinally#doFinally(java.lang.Throwable)
     */
    public void doFinally() {
    	name   = null;
       value = null;
       super.doFinally();
    }


}
