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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
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
 * <p>This tag renders a html CHECKBOX element or a whole group of them</p>
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */


public class DbCheckboxTag extends DbBaseHandlerTag implements DataContainer  {

    static Category logCat = Category.getInstance(DbCheckboxTag.class.getName()); // logging category for this class

	private Vector embeddedData=null;

	private String checked; // only needed if parentForm is in "insert-mode", otherwise the DbForms-Framework determinates whether a checkbox should be selected or not.
	private String growDirection; // only needed if we habe a whole "group" of DbRadioTags; default = null == horizontal

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getChecked() {
		return checked;
	}

	public void setGrowDirection(String growDirection) {
		this.growDirection = growDirection;
	}

	public String getGrowDirection() {
		return growDirection;
	}

  /**
  This method is a "hookup" for EmbeddedData - Tags which can assign the lines of data they loaded
  (by querying a database, or by rendering data-subelements, etc. etc.) and make the data
  available to this tag.
  [this method is defined in Interface DataContainer]
  */
  public void setEmbeddedData(Vector embeddedData) {
	 this.embeddedData = embeddedData;
  }

  public int doStartTag() throws javax.servlet.jsp.JspException {
    return EVAL_BODY_TAG;
  }

	private String generateTagString(String value, String description, boolean selected) {

    StringBuffer tagBuf = new StringBuffer();
    tagBuf.append("<input type=\"checkbox\" name=\"");
    tagBuf.append(getFormFieldName());
    tagBuf.append("\" value =\"");
    tagBuf.append(value);
		tagBuf.append("\" ");

		if(selected) tagBuf.append(" checked ");

		if (accessKey != null) {
			tagBuf.append(" accesskey=\"");
			tagBuf.append(accessKey);
			tagBuf.append("\"");
		}

		if (tabIndex != null) {
			tagBuf.append(" tabindex=\"");
			tagBuf.append(tabIndex);
			tagBuf.append("\"");
		}

		tagBuf.append(prepareEventHandlers());
		tagBuf.append(">");
		tagBuf.append(description);

		return tagBuf.toString();
	}


  public int doEndTag() throws javax.servlet.jsp.JspException {

		StringBuffer tagBuf = new StringBuffer();

		// current Value from Database; or if no data: explicitly set by user; or ""
		String currentValue = getFormFieldValue();

		if(embeddedData==null) { // no embedded data is nested in this tag

			// select, if datadriven and data matches with current value OR if explicitly set by user
			boolean isSelected = (!parentForm.getFooterReached() && value!=null && value.equals(currentValue)) || "true".equals(checked);

			tagBuf.append( generateTagString(value, "", isSelected) );

		} else {

			int embeddedDataSize = embeddedData.size();
			for(int i=0; i<embeddedDataSize; i++) {

				KeyValuePair aKeyValuePair = (KeyValuePair) embeddedData.elementAt(i);
				String aKey = aKeyValuePair.getKey();
				String aValue = aKeyValuePair.getValue();

				// select, if datadriven and data matches with current value OR if explicitly set by user
				boolean isSelected = aKey.equals(currentValue);

				tagBuf.append(generateTagString(aKey, aValue, isSelected));

				// how should the input-tags be separeted
				if(i<embeddedDataSize-1) {
					if("vertical".equals(growDirection)) {
						tagBuf.append("<br>");
					} else if("horziontal ".equals(growDirection)) {
						tagBuf.append("&nbsp;");
					}
				}

			}

		}

		try {
		  pageContext.getOut().write(tagBuf.toString());
		} catch(java.io.IOException ioe) {
			throw new JspException("IO Error: "+ioe.getMessage());
		}

		return EVAL_PAGE;
  }


}


