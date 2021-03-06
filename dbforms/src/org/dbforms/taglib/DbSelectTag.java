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

import org.dbforms.event.AbstractWebEvent;
import org.dbforms.event.eventtype.EventType;
import org.dbforms.interfaces.IDataContainer;
import org.dbforms.interfaces.StaticData;

import org.dbforms.util.*;

import java.util.List;

import javax.servlet.http.*;
import javax.servlet.jsp.*;

/**
 * <p>
 * This tag renders a html SELECT element including embedding OPTION elements.
 * </p>
 * 
 * @author Joachim Peer
 * @author Philip Grunikiewicz
 */
public class DbSelectTag extends AbstractDbBaseHandlerTag implements
		IDataContainer, javax.servlet.jsp.tagext.TryCatchFinally {
	private List embeddedData = null;

	private String customEntry;

	private String ifEmptyDontDraw = "false";

	private String ifEmptyItem = null;

	private String overrideReadOnly = "false";

	private String selectedIndex;

	private String size;

	private boolean disabled = false; 
	
	/**
	 * Sets the customEntry
	 * 
	 * @param customEntry
	 *            The customEntry to set
	 */
	public void setCustomEntry(String customEntry) {
		this.customEntry = customEntry;
	}

	/**
	 * Gets the customEntry
	 * 
	 * @return Returns a String
	 */
	public String getCustomEntry() {
		return customEntry;
	}

	/**
	 * This method is a "hookup" for EmbeddedData - Tags which can assign the
	 * lines of data they loaded (by querying a database, or by rendering
	 * data-subelements, etc. etc.) and make the data available to this tag.
	 * [this method is defined in Interface DataContainer]
	 * 
	 * @param embeddedData
	 *            DOCUMENT ME!
	 */
	public void setEmbeddedData(List embeddedData) {
		this.embeddedData = embeddedData;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getFormattedFieldValue() {
		String res = getFieldValue();

		if (res == null) {
			res = ParseUtil.getParameter((HttpServletRequest) this.pageContext
					.getRequest(), getFormFieldName());
		}

		return res;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param b
	 */
	public void setIfEmptyDontDraw(String b) {
		ifEmptyDontDraw = b;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return
	 */
	public boolean hasIfEmptyDontDrawSet() {
		return Util.getTrue(ifEmptyDontDraw);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param string
	 */
	public void setIfEmptyItem(String string) {
		ifEmptyItem = string;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return
	 */
	public String getIfEmptyItem() {
		return ifEmptyItem;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param b
	 */
	public void setOverrideReadOnly(String b) {
		overrideReadOnly = b;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param selectedIndex
	 *            DOCUMENT ME!
	 */
	public void setSelectedIndex(String selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getSelectedIndex() {
		return selectedIndex;
	}

	/**
	 * Set the size of this field (synonym for <code>setCols()</code>).
	 * 
	 * @param size
	 *            The new size
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * Return the size of this field (synonym for <code>getCols()</code>).
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getSize() {
		return size;
	}

    /**
	 * if disabled is set to true, the generated drop down list
	 * will be greyed out, i.e. read-only
	 *
	 * @param b
	 */
	public void setDisabled(boolean b) {
		disabled = b;
	}


    /**
     * @return true if the field is read only
     */
    public boolean isDisabled() {
        return disabled;
    }

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws javax.servlet.jsp.JspException
	 *             DOCUMENT ME!
	 * @throws JspException
	 *             DOCUMENT ME!
	 */
	public int doEndTag() throws javax.servlet.jsp.JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		List errors = (List) request.getAttribute("errors");
		AbstractWebEvent we = getParentForm().getWebEvent();

		StringBuffer tagBuf = new StringBuffer();
		StringBuffer selectedOptions = new StringBuffer();
		int embeddedDataSize = 0;

		String currentValue = getFormFieldValue();

		if (embeddedData != null) {
			// PG, 2001-12-14
			// Is their a custom entry? Display first...
			String ce = null;

			if (((ce = this.getCustomEntry()) != null)
					&& (ce.trim().length() > 0)) {
				boolean isSelected = false;
				String aKey = org.dbforms.util.StringUtil
						.getEmbeddedStringWithoutDots(ce, 0, ',');
				String aValue = org.dbforms.util.StringUtil
						.getEmbeddedStringWithoutDots(ce, 1, ',');

           	    // is captionResource is activated, retrieve the value from the MessageResources bundle
				if (getParentForm().hasCaptionResourceSet()) {
                  aValue = MessageResources.getMessage(request,aValue);
	            }

				
				// Check if we are in redisplayFieldsOnError mode and errors
				// have occured
				// If so, only set to selected if currentRow is equal to custom
				// row.
				if ((getParentForm().hasRedisplayFieldsOnErrorSet()
						&& (errors != null) && (errors.size() > 0))
						|| ((we != null) && (we.getType()
								.equals(EventType.EVENT_NAVIGATION_RELOAD)))) {
					isSelected = (currentValue.equals(aKey));
				} else {
					isSelected = "true".equals(org.dbforms.util.StringUtil
							.getEmbeddedStringWithoutDots(ce, 2, ','));
				}

				if (isSelected) {
					selectedOptions.append("-").append(aKey);
					/*
					 * Philip Grunikiewicz 2005-12-05
					 * 
					 * If in INSERT Mode and custom entry is set as default
					 * 
					 * Henner Kollmann 2005-12-15 
					 * 
					 * Only if this is a valid field!
					 */
					if (getParentForm().isFooterReached() && getField() != null) {
						currentValue = aKey;
					}
				}

				tagBuf.append(generateTagString(aKey, aValue, isSelected));
			}

			embeddedDataSize = embeddedData.size();

			// check for special 'IfEmpty' processing. if used skip the
			// datadriven loop
			boolean drawIt = true;
			String me = null;

			if (embeddedDataSize == 0) {
				if (hasIfEmptyDontDrawSet()) {
					drawIt = false;
				} else if ((me = getIfEmptyItem()) != null) {
					drawIt = false;

					String aKey = org.dbforms.util.StringUtil
							.getEmbeddedStringWithoutDots(me, 0, ',');
					String aValue = org.dbforms.util.StringUtil
							.getEmbeddedStringWithoutDots(me, 1, ',');

					// always selected, since no other items
					selectedOptions.append("-").append(aKey);
					tagBuf.append(generateTagString(aKey, aValue, true));
				}
			}

			if (drawIt) {
				for (int i = 0; i < embeddedDataSize; i++) {
					StaticData aKeyValuePair = (StaticData) embeddedData.get(i);
					String aKey = aKeyValuePair.getKey();
					String aValue = aKeyValuePair.getValue();

					// select, if datadriven and data matches with current value
					// OR if explicitly set by user
					boolean isSelected = aKey.equals(currentValue);

					if (isSelected) {
						selectedOptions.append("-").append(aKey);
					}

					tagBuf.append(generateTagString(aKey, aValue, isSelected));
				}
			}
		}

		tagBuf.append("</select>");

		//
		// For read-only mode. Add unique generic javascript function.
		// Reset to the default selected values (in case of multiselection)
		// using javascript function on client side.
		//
		if (((!hasOverrideReadOnlySet()) && hasReadOnlySet())
				|| getParentForm().hasReadOnlySet()) {
			selectedOptions.append("-");
			
			setDisabled(true);
		}

		// For generation Javascript Validation. Need original and modified
		// fields name
		getParentForm().addChildName(getName(), getFormFieldName());

		try {
			if ((embeddedDataSize > 0) || !hasIfEmptyDontDrawSet()) {
				pageContext.getOut().write(generateSelectHeader());
				pageContext.getOut().write(tagBuf.toString());
				writeOutSpecialValues();
			}
		} catch (java.io.IOException ioe) {
			throw new JspException("IO Error: " + ioe.getMessage());
		}

		return EVAL_PAGE;
	}

	/**
	 * DOCUMENT ME!
	 */
	public void doFinally() {
		embeddedData = null;
		selectedIndex = null;
		customEntry = null;
		size = null;
		ifEmptyDontDraw = "false";
		ifEmptyItem = null;
		overrideReadOnly = "false";
		super.doFinally();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws javax.servlet.jsp.JspException
	 *             DOCUMENT ME!
	 */
	public int doStartTag() throws javax.servlet.jsp.JspException {
		return EVAL_BODY_BUFFERED;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return
	 */
	public boolean hasOverrideReadOnlySet() {
		return overrideReadOnly.equalsIgnoreCase("true");
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected String typicalDefaultValue() {
		String val;

		// Lets check if the selectedIndex parameter has been input
		if (((val = this.getSelectedIndex()) != null)
				&& (val.trim().length() != 0)) {
			return val;
		}

		// No selectedIndex - business as usual...
		return (super.typicalDefaultValue());
	}

	/**
	 * generates the html select start tag including its attributes
	 *
	 * @return the complete start tag
	 * @throws javax.servlet.jsp.JspException
	 */
	private String generateSelectHeader() throws javax.servlet.jsp.JspException {
		StringBuffer tagBuf = new StringBuffer();

		tagBuf.append("<select name=\"");

		tagBuf.append(getFormFieldName());
		tagBuf.append("\"");

		if (size != null) {
			tagBuf.append(" size=\"");
			tagBuf.append(size);
			tagBuf.append("\"");
		}

		if (getAccessKey() != null) {
			tagBuf.append(" accesskey=\"");
			tagBuf.append(getAccessKey());
			tagBuf.append("\"");
		}

		if (getTabIndex() != null) {
			tagBuf.append(" tabindex=\"");
			tagBuf.append(getTabIndex());
			tagBuf.append("\"");
		}
       
        if (isDisabled()) {
            tagBuf.append(" disabled");
        }

		tagBuf.append(prepareStyles());
		tagBuf.append(prepareEventHandlers());
		tagBuf.append(">");

		return tagBuf.toString();
	}

	private String generateTagString(String value, String description,
			boolean selected) {
		StringBuffer tagBuf = new StringBuffer();
		tagBuf.append("<option value=\"");
		tagBuf.append(escapeHTML(value));
		tagBuf.append("\"");

		if (selected) {
			tagBuf.append(" selected=\"selected\" ");
		}

		// 20021203-HKK: Removed unneeded blank
		tagBuf.append(">");
		tagBuf.append(escapeHTML(description));

		// 20021025-HKK: Appended </option>
		tagBuf.append("</option>");

		return tagBuf.toString();
	}
}
