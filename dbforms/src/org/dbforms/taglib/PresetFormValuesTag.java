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

import java.util.HashMap;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbforms.util.ReflectionUtil;
import org.dbforms.util.Util;

import org.dbforms.interfaces.DbEventInterceptorData;
import org.dbforms.interfaces.IPresetFormValues;
import org.dbforms.interfaces.IPropertyMap;

/**
 * 
 * @author Henner Kollmann
 * 
 * &lt;db:presetFormValues class=""
 */
public class PresetFormValuesTag extends AbstractScriptHandlerTag
		implements IPropertyMap, javax.servlet.jsp.tagext.TryCatchFinally {

	private static final String sessionKey = "dbforms.org.tag.PresetFormValuesTag";
    private static Log logCat = LogFactory.getLog(IsWebEventTag.class.getName());

	private String className = null;

	private HashMap properties = new HashMap();

	/**
	 * classname of a class implementing the CustomFormatter Interface
	 * 
	 * @param className
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	public static void presetFormValues(DbEventInterceptorData data)
			throws JspException {
		PageContext pageContext = (PageContext) data
				.getAttribute(DbEventInterceptorData.PAGECONTEXT);
		if (pageContext != null) {
			PresetFormValuesTag tag = (PresetFormValuesTag) pageContext
					.getAttribute(sessionKey);
			if (tag != null) {
				tag.privatePresetFormValues(data);
			}
		}
	}

	public void privatePresetFormValues(DbEventInterceptorData data)
			throws JspException {
		if (!Util.isNull(className)) {
			try {
				IPresetFormValues cf = (IPresetFormValues) ReflectionUtil
						.newInstance(className);
				cf.presetFormValues(properties, data);
			} catch (Exception e) {
			   logCat.error("privatePresetFormValues", e);
			}
		}
	}

	public int doStartTag() throws JspException {
		properties.clear();
		return super.doStartTag();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public int doEndTag() throws JspException {
		if (!Util.isNull(className)) {
			pageContext.setAttribute(sessionKey, this);
		}
		return EVAL_PAGE;
	}

	/**
	 * DOCUMENT ME!
	 */
	public void doFinally() {
		super.doFinally();
	}

	public void addProperty(String name, String value) {
		properties.put(name, value);
	}

}
