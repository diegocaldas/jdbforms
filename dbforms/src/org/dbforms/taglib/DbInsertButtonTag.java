/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * DbForms - a Rapid Application Development Framework
 * Copyright (C) 2001 Joachim Peer <j.peer@gmx.net> et al.
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

import org.dbforms.*;

import org.apache.log4j.Category;

/****
 *
 * <p>this tag renders an "Insert"-button.
 *
 * #fixme - define abstract base class [should be fixed in release 0.6]
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */

public class DbInsertButtonTag extends DbBaseButtonTag {

	static Category logCat =
		Category.getInstance(DbInsertButtonTag.class.getName());
	// logging category for this class

	private static int uniqueID;

	static {
		uniqueID = 1;
	}

	public int doStartTag() throws javax.servlet.jsp.JspException {

		DbInsertButtonTag.uniqueID++; // make sure that we don't mix up buttons

		logCat.info("pos DbInsertButtonTag 1");

		if (!parentForm.getFooterReached())
			return SKIP_BODY; //  contrary to dbUpdate and dbDelete buttons!

		logCat.info("pos DbInsertButtonTag 2");

		try {

			logCat.info("pos DbInsertButtonTag 3");

			StringBuffer tagBuf = new StringBuffer();
			StringBuffer tagNameBuf = new StringBuffer();

			tagNameBuf.append("ac_insert_");
			tagNameBuf.append(table.getId());
			tagNameBuf.append("_");
			tagNameBuf.append(parentForm.getPositionPathCore());

			// PG - Render the name unique
			tagNameBuf.append("_");
			tagNameBuf.append(uniqueID);

			String tagName = tagNameBuf.toString();

			if (followUp != null) {
				tagBuf.append(getDataTag(tagName, "fu", followUp));
			}

			if (followUpOnError != null) {
				tagBuf.append(getDataTag(tagName, "fue", followUpOnError));
			}

			//tagBuf.append( getDataTag(tagName, "id", Integer.toString(parentForm.getFrozenCumulatedCount())) );

			tagBuf.append(getButtonBegin());
			tagBuf.append(" name=\"");
			tagBuf.append(tagName);
			tagBuf.append("\">");

			pageContext.getOut().write(tagBuf.toString());

		} catch (java.io.IOException ioe) {
			throw new JspException("IO Error: " + ioe.getMessage());
		}

		if (choosenFlavor == FLAVOR_MODERN)
			return EVAL_BODY_TAG;
		else
			return SKIP_BODY;
	}

	public int doEndTag() throws javax.servlet.jsp.JspException {

		if (!parentForm.getFooterReached())
			return EVAL_PAGE;

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

}