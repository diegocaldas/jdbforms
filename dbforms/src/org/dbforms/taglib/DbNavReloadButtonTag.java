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

import javax.servlet.jsp.JspException;

import org.dbforms.event.eventtype.EventType;

/****
 *
 * <p>this tag renders an "reload"-button.
 *
 *
 * @author Henner Kollmann <Henner.Kollmann@gmx.de>
 */
public class DbNavReloadButtonTag extends DbBaseButtonTag implements javax.servlet.jsp.tagext.TryCatchFinally {
	private String forceReload = null;

	public void doFinally() {
		super.doFinally();
	}

	/**
	 * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
	 */
	public void doCatch(Throwable t) throws Throwable {
		throw t;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws javax.servlet.jsp.JspException DOCUMENT ME!
	 * @throws JspException DOCUMENT ME!
	 */
	public int doStartTag() throws javax.servlet.jsp.JspException {
		super.doStartTag();

		try {
			StringBuffer tagBuf = new StringBuffer();
			StringBuffer tagName = new StringBuffer(EventType.EVENT_NAVIGATION_TRANSFER_RELOAD);
			if (getTable() != null)
				tagName.append(getTable().getId());
			if (hasForceReloadSet()) {
				tagName.append("_force");
			} else if (getParentForm().getFooterReached()) {
				tagName.append("_ins");
			}
			tagName.append("_");
			tagName.append(Integer.toString(getUniqueID()));

			if (getFollowUp() != null) {
				tagBuf.append(getDataTag(tagName.toString(), "fu", getFollowUp()));
			}

			if (getFollowUpOnError() != null) {
				tagBuf.append(getDataTag(tagName.toString(), "fue", getFollowUpOnError()));
			}

			tagBuf.append(getButtonBegin());
			tagBuf.append(" name=\"");
			tagBuf.append(tagName);
			tagBuf.append(getButtonEnd());

			pageContext.getOut().write(tagBuf.toString());
		} catch (java.io.IOException ioe) {
			throw new JspException("IO Error: " + ioe.getMessage());
		}

		if (getChoosenFlavor() == FLAVOR_MODERN) {
			return EVAL_BODY_BUFFERED;
		} else {
			return SKIP_BODY;
		}
	}

	/**
	 * @return
	 */
	public String getForceReload() {
		return forceReload;
	}

	/**
	 * @param string
	 */
	public void setForceReload(String string) {
		forceReload = string;
	}

	public boolean hasForceReloadSet() {
		return "true".equalsIgnoreCase(forceReload);
	}
}
