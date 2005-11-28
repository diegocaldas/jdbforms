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

import org.dbforms.interfaces.IAttributeList;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for all tags referencing a scripting variable by its ID
 * which they want to parameterize further.
 * 
 * @author  Henner Kollmann 
 */
public abstract class AbstractAttributeObjectTag extends
		AbstractTagSupportWithScriptHandler implements IAttributeList,
		javax.servlet.jsp.tagext.TryCatchFinally {

	private Map params = new HashMap();

	protected void reset() {
		params = new HashMap();
	}

	protected Map getParameters() {
		return params;
	}

	public void addParameter(String name, Object value) {
		params.put(name, value);
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
		params.clear();
		super.doFinally();
	}

}
