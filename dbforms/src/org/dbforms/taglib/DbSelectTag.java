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
import javax.servlet.http.*;

/****
 *
 * <p>This tag renders a html SELECT element including embedding OPTION elements.</p>
 *
 * @author Joachim Peer <j.peer@gmx.net>
 * @author Philip Grunikiewicz<grunikiewicz.philip@hydro.qc.ca>
 */

public class DbSelectTag extends DbBaseHandlerTag implements DataContainer  {

	static Category logCat = Category.getInstance(DbSelectTag.class.getName()); // logging category for this class

	private Vector embeddedData=null;

	private String selectedIndex;
	private String customEntry;
	private String size;


	public void setSelectedIndex(String selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	public String getSelectedIndex() {
		return selectedIndex;
	}

	/**
 	 * Return the size of this field (synonym for <code>getCols()</code>).
 	 */
	public String getSize() {
		return size;
	}

	/**
	 * Set the size of this field (synonym for <code>setCols()</code>).
	 * @param size The new size
	 */
	public void setSize(String size) {
		this.size = size;
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

	StringBuffer tagBuf = new StringBuffer();

	tagBuf.append("<select name=\"");
	tagBuf.append(getFormFieldName());
	tagBuf.append("\"");

		if (size != null) {
			tagBuf.append(" size=\"");
			tagBuf.append(size);
			tagBuf.append("\"");
		}

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

		tagBuf.append(prepareStyles());
		tagBuf.append(prepareEventHandlers());
		tagBuf.append(">");


		try {
		  pageContext.getOut().write(tagBuf.toString());
		} catch(java.io.IOException ioe) {
			throw new JspException("IO Error: "+ioe.getMessage());
		}


	return EVAL_BODY_TAG;
  }  



	private String generateTagString(String value, String description, boolean selected) {

	StringBuffer tagBuf = new StringBuffer();
	tagBuf.append("<option value=\"");
	tagBuf.append(value);
		tagBuf.append("\"");

	if(selected) tagBuf.append(" selected");

	tagBuf.append("> ");
	tagBuf.append(description);

	return tagBuf.toString();
	}


  public int doEndTag() throws javax.servlet.jsp.JspException {
  	
  	
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		Vector errors = (Vector) request.getAttribute("errors");
  	

		StringBuffer tagBuf = new StringBuffer();

		// current Value from Database; or if no data: explicitly set by user; or ""
		String currentValue = getFormFieldValue();
		
		if(embeddedData==null) { // no embedded data is nested in this tag

			/*
				for that case we need to render option tags!
			*/

		} else {
			
			// PG, 2001-12-14
			// Is their a custom entry? Display first...
			String ce = null;
			if((ce = this.getCustomEntry()) != null && ce.trim().length()>0)
			{
				boolean isSelected = false;				
				String aKey = org.dbforms.util.ParseUtil.getEmbeddedString(ce,0,',');
				String aValue = org.dbforms.util.ParseUtil.getEmbeddedString(ce,1,',');	
				
				
				// Check if we are in redisplayFieldsOnError mode and errors have occured
				// If so, only set to selected if currentRow is equal to custom row.
				if ("true".equals(parentForm.getRedisplayFieldsOnError()) && errors != null && errors.size() > 0)
				{
					isSelected = (currentValue.equals(aKey));													
				}
				else
				{
					isSelected = "true".equals(org.dbforms.util.ParseUtil.getEmbeddedString(ce,2,','));
				}
				tagBuf.append(generateTagString(aKey, aValue, isSelected));				
			}
			

			int embeddedDataSize = embeddedData.size();
			for(int i=0; i<embeddedDataSize; i++) {

				KeyValuePair aKeyValuePair = (KeyValuePair) embeddedData.elementAt(i);
				String aKey = aKeyValuePair.getKey();
				String aValue = aKeyValuePair.getValue();

				// select, if datadriven and data matches with current value OR if explicitly set by user
				boolean isSelected = aKey.equals(currentValue);

				tagBuf.append(generateTagString(aKey, aValue, isSelected));
			}

		}

		tagBuf.append("</select>");

		// For generation Javascript Validation.  Need original and modified fields name
		parentForm.addChildName(getFieldName(), getFormFieldName());


		try {
		  pageContext.getOut().write(tagBuf.toString());
		} catch(java.io.IOException ioe) {
			throw new JspException("IO Error: "+ioe.getMessage());
		}

		return EVAL_PAGE;
  }  

	// ------------------------------------------------------ Protected Methods






/* 	grunikiewicz.philip@hydro.qc.ca
	2001-04-27

	In the case of a select tag, the selectedIndex parameter is ignored.
	Overiding this method in the DbSelectTag may not be the cleanest
	implementation however it provides a single entry point to fix this bug.

*/
	protected String typicalDefaultValue()
	{

		String val;
		// Lets check if the selectedIndex parameter has been input
		if((val=this.getSelectedIndex())!=null && val.trim().length()!=0)
			return val;

		// No selectedIndex - business as usual...
		return (super.typicalDefaultValue());
	}



	/**
	 * Gets the customEntry
	 * @return Returns a String
	 */
	public String getCustomEntry() {
		return customEntry;
	}
	/**
	 * Sets the customEntry
	 * @param customEntry The customEntry to set
	 */
	public void setCustomEntry(String customEntry) {
		this.customEntry = customEntry;
	}

}