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

import org.apache.log4j.Category;
import org.dbforms.validation.ValidatorConstants;
import org.dbforms.util.Util;
/**
 * <p>this tag renders an "copy"-button.
 *
 * @author Stefano Borghi <s.borghi@nsi-mail.it>
 * 
 * @version $Revision$
 * 
 */
public class DbNavCopyButtonTag extends DbBaseButtonTag {
	
	private static Category logCat =
		Category.getInstance(DbNavCopyButtonTag.class.getName());
	// logging category for this class

	/** Holds value of property showAlwaysInFooter. */
	private String showAlwaysInFooter = "true";

	public int doStartTag() throws javax.servlet.jsp.JspException {


		// ValidatorConstants.JS_CANCEL_SUBMIT is the javascript variable boolean to verify
		// if we do the javascript validation before submit <FORM>
		if (parentForm.getFormValidatorName() != null
			&& parentForm.getFormValidatorName().length() > 0
			&& parentForm.getJavascriptValidation().equals("true")) {
			String onclick = (getOnClick() != null) ? getOnClick() : "";
			if (onclick.lastIndexOf(";") != onclick.length() - 1)
				onclick += ";"; // be sure javascript end with ";"
			setOnClick(
				onclick + ValidatorConstants.JS_CANCEL_VALIDATION + "=true;");
		}

		if (parentForm.getFooterReached()
					&& Util.isNull(parentForm.getResultSetVector())
					&& "false".equalsIgnoreCase(showAlwaysInFooter))
		{
			// 20030521 HKK: Bug fixing, thanks to Michael Slack! 
			return SKIP_BODY;
		}

		try {
			StringBuffer tagBuf = new StringBuffer();
			String tagName = "ac_copy_" + table.getId();
			if (followUp != null) {
				tagBuf.append(getDataTag(tagName, "fu", followUp));
			}
			if (followUpOnError != null) {
				tagBuf.append(getDataTag(tagName, "fue", followUpOnError));
			}
			tagBuf.append(getButtonBegin());
			tagBuf.append(" name=\"");
			tagBuf.append(tagName);
			tagBuf.append("\">");

			pageContext.getOut().write(tagBuf.toString());
		} catch (java.io.IOException ioe) {
			throw new JspException("IO Error: " + ioe.getMessage());
		}

		if (choosenFlavor == FLAVOR_MODERN)
			return EVAL_BODY_BUFFERED;
		else
			return SKIP_BODY;
	}

	public int doEndTag() throws javax.servlet.jsp.JspException {

		if (choosenFlavor == FLAVOR_MODERN) {
			try {
				if (bodyContent != null)
					bodyContent.writeOut(bodyContent.getEnclosingWriter());
				pageContext.getOut().write("</button>");
			} catch (java.io.IOException ioe) {
				throw new JspException("IO Error: " + ioe.getMessage());
			}
		}
		return EVAL_PAGE;
	}


	/**
	 * @return
	 */
	public String getShowAlwaysInFooter()
	{
		return showAlwaysInFooter;
	}

	/**
	 * @param string
	 */
	public void setShowAlwaysInFooter(String string)
	{
		showAlwaysInFooter = string;
	}

}
