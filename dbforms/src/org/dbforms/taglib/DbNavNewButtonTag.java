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
import org.dbforms.util.*;

import org.apache.log4j.Category;

/****
 *
 * <p>this tag renders an "new"-button.
 *
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */

public class DbNavNewButtonTag extends DbBaseButtonTag {

  static Category logCat = Category.getInstance(DbNavNewButtonTag.class.getName()); // logging category for this class

  public int doStartTag() throws javax.servlet.jsp.JspException {

   // if(parentForm.getFooterReached() && ResultSetVector.isEmptyOrNull(parentForm.getResultSetVector()) ) return EVAL_PAGE;

		try {

    	  StringBuffer tagBuf = new StringBuffer();
				String tagName = "ac_new_"+table.getId();

				if(followUp != null) {
					tagBuf.append( getDataTag(tagName, "fu", followUp) );
				}

				tagBuf.append(getButtonBegin());
    	  tagBuf.append(" name=\"");
    	  tagBuf.append(tagName);
    	  tagBuf.append("\">");

 	  	  pageContext.getOut().write(tagBuf.toString());

		} catch(java.io.IOException ioe) {
			throw new JspException("IO Error: "+ioe.getMessage());
		}

		if(choosenFlavor == FLAVOR_MODERN)
			return EVAL_BODY_TAG;
		else
    	return SKIP_BODY;
  }


  public int doEndTag() throws javax.servlet.jsp.JspException {

		if(choosenFlavor == FLAVOR_MODERN) {

			try {
				if(bodyContent != null)
					bodyContent.writeOut(bodyContent.getEnclosingWriter());

			  pageContext.getOut().write( "</button>" );
			} catch(java.io.IOException ioe) {
				throw new JspException("IO Error: "+ioe.getMessage());
			}
		}

		return EVAL_PAGE;
  }


}


