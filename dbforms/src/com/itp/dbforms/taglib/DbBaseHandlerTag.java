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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * NOTE:
 * Many parts of this class where taken from the Apache Jakarta-Struts Project
 *
 */

package com.itp.dbforms.taglib;


import javax.servlet.jsp.tagext.*;
import com.itp.dbforms.*;

// these 3 we need for formfield auto-population
import java.util.Vector;
import com.itp.dbforms.util.ParseUtil;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Category;

/**
 * <p>Base class for db-tags that render form data-elements capable of including JavaScript
 * event handlers and/or CSS Style attributes.</p>
 *
 * <p>Furthermore, this base class provides base functionality for DataBase driven form widgets:
 * it initializes the associated DbFormsConfig and provides various field-properties & methods
 * (i.e getFormFieldName and getFormFieldValue)</p>
 *
 * <p>the html/css releated properties and methods where originally done by Don Clasen for
 * Apache Groups's Jakarta-Struts project.</p>
 *
 * orginally done by Don Clasen
 * @author Joe Peer (modified and extended this class for use in DbForms-Project)
 */

public abstract class DbBaseHandlerTag extends BodyTagSupport {

    static Category logCat = Category.getInstance(DbBaseHandlerTag.class.getName()); // logging category for this class

    // ----------------------------------------------------- Instance Variables

	public static final int SEARCHMODE_NONE = 0;
	public static final int SEARCHMODE_AND = 1;
	public static final int SEARCHMODE_OR = 2;


// DbForms specific

    protected DbFormsConfig config;
  	protected String fieldName;
  	protected Field field;

	//protected String searchMode;
	//protected int parsedSearchMode = SEARCHMODE_NONE;

  	// only needed/used if parent tag is in "insert-mode" (footer, etc.)
		// otherwise this tag takes the current value from the database result!
  	protected String value;

    protected DbFormTag parentForm;


//  Navigation Management

    /** Access key character. */
    protected String accessKey = null;

    /** Tab index value. */
    protected String tabIndex = null;

//  Mouse Events

    /** Mouse click event. */
    private String onClick = null;

    /** Mouse double click event. */
    private String onDblClick = null;

    /** Mouse over component event. */
    private String onMouseOver = null;

    /** Mouse exit component event. */
    private String onMouseOut = null;

    /** Mouse moved over component event. */
    private String onMouseMove = null;

    /** Mouse pressed on component event. */
    private String onMouseDown = null;

    /** Mouse released on component event. */
    private String onMouseUp = null;

//  Keyboard Events

    /** Key down in component event. */
    private String onKeyDown = null;

    /** Key released in component event. */
    private String onKeyUp = null;

    /** Key down and up together in component event. */
    private String onKeyPress = null;

// Text Events

    /** Text selected in component event. */
    private String onSelect = null;

    /** Content changed after component lost focus event. */
    private String onChange = null;

// Focus Events

    /** Component lost focus event. */
    private String onBlur = null;

    /** Component has received focus event. */
    private String onFocus = null;

// CSS Style Support

    /** Style attribute associated with component. */
    private String style = null;

    /** Named Style class associated with component. */
    private String styleClass = null;

    // ------------------------------------------------------------- Properties


// dbForms - specifcs

	  public void setFieldName(String fieldName) {
	    this.fieldName=fieldName;
			this.field = parentForm.getTable().getFieldByName(fieldName);

			if(parentForm.isSubForm()) {
				// tell parent that _this_ class will generate the html tag, not DbBodyTag!
				parentForm.strikeOut(this.field);
			}
	  }

	  public String getFieldName() {
	    return fieldName;
	  }

  	/**
  	"value" is only used if parent tag is in "insert-mode" (footer, etc.)
		otherwise this tag takes the current value from the database result!
		*/
		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}


//  Navigation Management

    /** Sets the accessKey character. */
    public void setAccessKey(String accessKey) {
	this.accessKey = accessKey;
    }

    /** Returns the accessKey character. */
    public String getAccessKey() {
	return (this.accessKey);
    }


    /** Sets the tabIndex value. */
    public void setTabIndex(String tabIndex) {
	this.tabIndex = tabIndex;
    }

    /** Returns the tabIndex value. */
    public String getTabIndex() {
	return (this.tabIndex);
    }

// Mouse Events

    /** Sets the onClick event handler. */
    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    /** Returns the onClick event handler. */
    public String getOnClick() {
        return onClick;
    }

    /** Sets the onDblClick event handler. */
    public void setOnDblClick(String onDblClick) {
        this.onDblClick = onDblClick;
    }

    /** Returns the onDblClick event handler. */
    public String getOnDblClick() {
        return onDblClick;
    }

    /** Sets the onMouseDown event handler. */
    public void setOnMouseDown(String onMouseDown) {
        this.onMouseDown = onMouseDown;
    }

    /** Returns the onMouseDown event handler. */
    public String getOnMouseDown() {
        return onMouseDown;
    }

    /** Sets the onMouseUp event handler. */
    public void setOnMouseUp(String onMouseUp) {
        this.onMouseUp = onMouseUp;
    }

    /** Returns the onMouseUp event handler. */
    public String getOnMouseUp() {
        return onMouseUp;
    }

    /** Sets the onMouseMove event handler. */
    public void setOnMouseMove(String onMouseMove) {
        this.onMouseMove = onMouseMove;
    }

    /** Returns the onMouseMove event handler. */
    public String getOnMouseMove() {
        return onMouseMove;
    }

    /** Sets the onMouseOver event handler. */
    public void setOnMouseOver(String onMouseOver) {
        this.onMouseOver = onMouseOver;
    }

    /** Returns the onMouseOver event handler. */
    public String getOnMouseOver() {
        return onMouseOver;
    }

    /** Sets the onMouseOut event handler. */
    public void setOnMouseOut(String onMouseOut) {
        this.onMouseOut = onMouseOut;
    }

    /** Returns the onMouseOut event handler. */
    public String getOnMouseOut() {
        return onMouseOut;
    }

// Keyboard Events

    /** Sets the onKeyDown event handler. */
    public void setOnKeyDown(String onKeyDown) {
        this.onKeyDown = onKeyDown;
    }

    /** Returns the onKeyDown event handler. */
    public String getOnKeyDown() {
        return onKeyDown;
    }

    /** Sets the onKeyUp event handler. */
    public void setOnKeyUp(String onKeyUp) {
        this.onKeyUp = onKeyUp;
    }

    /** Returns the onKeyUp event handler. */
    public String getOnKeyUp() {
        return onKeyUp;
    }

    /** Sets the onKeyPress event handler. */
    public void setOnKeyPress(String onKeyPress) {
        this.onKeyPress = onKeyPress;
    }

    /** Returns the onKeyPress event handler. */
    public String getOnKeyPress() {
        return onKeyPress;
    }

// Text Events

    /** Sets the onChange event handler. */
    public void setOnChange(String onChange) {
        this.onChange = onChange;
    }

    /** Returns the onChange event handler. */
    public String getOnChange() {
        return onChange;
    }

    /** Sets the onSelect event handler. */
    public void setOnSelect(String onSelect) {
        this.onSelect = onSelect;
    }

    /** Returns the onSelect event handler. */
    public String getOnSelect() {
        return onSelect;
    }

// Focus Events

    /** Sets the onBlur event handler. */
    public void setOnBlur(String onBlur) {
        this.onBlur = onBlur;
    }

    /** Returns the onBlur event handler. */
    public String getOnBlur() {
        return onBlur;
    }

    /** Sets the onFocus event handler. */
    public void setOnFocus(String onFocus) {
        this.onFocus = onFocus;
    }

    /** Returns the onFocus event handler. */
    public String getOnFocus() {
        return onFocus;
    }

// CSS Style Support

    /** Sets the style attribute. */
    public void setStyle(String style) {
        this.style = style;
    }

    /** Returns the style attribute. */
    public String getStyle() {
        return style;
    }

    /** Sets the style class attribute. */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /** Returns the style class attribute. */
    public String getStyleClass() {
        return styleClass;
    }

    // --------------------------------------------------------- Public Methods


	// DbForms specific

	  public void setPageContext(final javax.servlet.jsp.PageContext pageContext)  {
	    super.setPageContext(pageContext);
	    config = (DbFormsConfig) pageContext.getServletContext().getAttribute(DbFormsConfig.CONFIG);
	  }

	  public void setParent(final javax.servlet.jsp.tagext.Tag parent) {
	    super.setParent(parent);

	    // between this form and its parent lies a DbHeader/Body/Footer-Tag and maybe other tags (styling, logic, etc.)
	    parentForm = (DbFormTag) findAncestorWithClass(this, DbFormTag.class);
	  }

	  /**
 		valid attribute parameters are: "none" (default), "and", "or"

	  public void setSearchMode(String searchMode) {
		 this.searchMode = searchMode;

		 if(searchMode.equals("and"))
		 	parsedSearchMode = SEARCHMODE_AND;
		 else if(searchMode.equals("or"))
			parsedSearchMode = SEARCHMODE_OR;
		 else
			parsedSearchMode = SEARCHMODE_NONE;

	  }

	  public String getSearchMode() {
		  return searchMode;
	  }

	  public int getParsedSearchMode() {
		  return parsedSearchMode;
	  }
*/


    /**
     * Release any acquired resources.
     */
    public void release() {
			super.release();
			accessKey = null;
			tabIndex = null;
			onClick = null;
			onDblClick = null;
			onMouseOver = null;
			onMouseOut = null;
			onMouseMove = null;
			onMouseDown = null;
			onMouseUp = null;
			onKeyDown = null;
			onKeyUp = null;
			onKeyPress = null;
			onSelect = null;
			onChange = null;
			onBlur = null;
			onFocus = null;
			style = null;
			styleClass = null;
    }


    // ------------------------------------------------------ Protected Methods



// DbForms specific

	protected String typicalDefaultValue() {
		switch(field.getType()) {
			case com.itp.dbforms.util.FieldTypes.INTEGER : return "0";
			case com.itp.dbforms.util.FieldTypes.DOUBLE : return "0.0";
			case com.itp.dbforms.util.FieldTypes.FLOAT : return "0.0";
			default : return ""; // in all other cases we just leave the formfield empty
		}
	}

	/**
	determinates value of the html-widget.
    */
	protected String getFormFieldValue() {

		if(!parentForm.getFooterReached()) { // data from dataBase

      		String[] currentRow = parentForm.getResultSetVector().getCurrentRow();

      		if(currentRow==null)
      			return typicalDefaultValue();
      		else {
				String curVal = currentRow[field.getId()];
      			return (curVal!=null && curVal.length()>0) ? curVal : typicalDefaultValue();
		    }

		} else { // the form field is in 'insert-mode'

			if(value != null) {

				return value;  // default value defined by jsp-developer (provided via the "value" attribute of the tag)

			} else {

				//#fixme: perform jsp/form equality check to avoid confision in cross-jsp actions

				HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();

				Vector errors = (Vector) request.getAttribute("errors");
				if(errors!=null && errors.size()>0) { // an insert error occured. this is the typical use case for automatic field-repopulation

					String oldValue = ParseUtil.getParameter(request, getFormFieldName());
					if(oldValue!=null)
						return oldValue;

				}

				// fill out empty fields so that there are no plain field-syntax errors
				// on database operations...
				return typicalDefaultValue();

			}
		}

	}

  /**
  generates the decoded name for the html-widget.
  */
	protected String getFormFieldName() {

		String keyIndex = (parentForm.getFooterReached()) ? "ins"+parentForm.getPositionPathCore() : parentForm.getPositionPath();

		StringBuffer buf = new StringBuffer();
		buf.append("f_");
    	buf.append(parentForm.getTable().getId());
    	buf.append("_");
    	buf.append(keyIndex);
    	buf.append("_");
    	buf.append(field.getId());
		return buf.toString();
	}

  /**
  generates the decoded name for the html-SEARCH widget.

	protected String getSearchFieldName() {
		StringBuffer buf = new StringBuffer();
		buf.append("search_");
    	buf.append(parentForm.getTable().getId());
    	buf.append("_");
    	buf.append(field.getId());
		return buf.toString();
	}

	protected String getSearchModeTag() {
		StringBuffer buf = new StringBuffer();
		buf.append("<input type=\"hidden\" name=\"searchmode_");
    	buf.append(parentForm.getTable().getId());
    	buf.append("_");
    	buf.append(field.getId());
    	buf.append("\" value=\"");
    	buf.append(searchMode);
    	buf.append("\">");
		return buf.toString();
	}
*/

    /**
     * Prepares the style attributes for inclusion in the component's HTML tag.
     * @return The prepared String for inclusion in the HTML tag.
     */
    protected String prepareStyles() {
        StringBuffer styles = new StringBuffer();
        if (style != null) {
            styles.append(" style=\"");
            styles.append(style);
            styles.append("\"");
        }
        if (styleClass != null) {
            styles.append(" class=\"");
            styles.append(styleClass);
            styles.append("\"");
        }
        return styles.toString();
    }

    /**
     * Prepares the event handlers for inclusion in the component's HTML tag.
     * @return The prepared String for inclusion in the HTML tag.
     */
    protected String prepareEventHandlers() {
        StringBuffer handlers = new StringBuffer();
        prepareMouseEvents(handlers);
        prepareKeyEvents(handlers);
        prepareTextEvents(handlers);
        prepareFocusEvents(handlers);
        return handlers.toString();
    }


    // -------------------------------------------------------- Private Methods


    /**
     * Prepares the mouse event handlers, appending them to the the given
     * StringBuffer.
     * @param handlers The StringBuffer that output will be appended to.
     */
    private void prepareMouseEvents(StringBuffer handlers) {
        if (onClick != null) {
            handlers.append(" onClick=\"");
            handlers.append(onClick);
            handlers.append("\"");
        }

        if (onDblClick != null) {
            handlers.append(" onDblClick=\"");
            handlers.append(onDblClick);
            handlers.append("\"");
        }

        if (onMouseOver != null) {
            handlers.append(" onMouseOver=\"");
            handlers.append(onMouseOver);
            handlers.append("\"");
        }

        if (onMouseOut != null) {
            handlers.append(" onMouseOut=\"");
            handlers.append(onMouseOut);
            handlers.append("\"");
        }

        if (onMouseMove != null) {
            handlers.append(" onMouseMove=\"");
            handlers.append(onMouseMove);
            handlers.append("\"");
        }

        if (onMouseDown != null) {
            handlers.append(" onMouseDown=\"");
            handlers.append(onMouseDown);
            handlers.append("\"");
        }

        if (onMouseUp != null) {
            handlers.append(" onMouseUp=\"");
            handlers.append(onMouseUp);
            handlers.append("\"");
        }
    }

    /**
     * Prepares the keyboard event handlers, appending them to the the given
     * StringBuffer.
     * @param handlers The StringBuffer that output will be appended to.
     */
    private void prepareKeyEvents(StringBuffer handlers) {

        if (onKeyDown != null) {
            handlers.append(" onKeyDown=\"");
            handlers.append(onKeyDown);
            handlers.append("\"");
        }

        if (onKeyUp != null) {
            handlers.append(" onKeyUp=\"");
            handlers.append(onKeyUp);
            handlers.append("\"");
        }

        if (onKeyPress != null) {
            handlers.append(" onKeyPress=\"");
            handlers.append(onKeyPress);
            handlers.append("\"");
        }
    }

    /**
     * Prepares the text event handlers, appending them to the the given
     * StringBuffer.
     * @param handlers The StringBuffer that output will be appended to.
     */
    private void prepareTextEvents(StringBuffer handlers) {

        if (onSelect != null) {
            handlers.append(" onSelect=\"");
            handlers.append(onSelect);
            handlers.append("\"");
        }

        if (onChange != null) {
            handlers.append(" onChange=\"");
            handlers.append(onChange);
            handlers.append("\"");
        }
    }

    /**
     * Prepares the focus event handlers, appending them to the the given
     * StringBuffer.
     * @param handlers The StringBuffer that output will be appended to.
     */
    private void prepareFocusEvents(StringBuffer handlers) {

        if (onBlur != null) {
            handlers.append(" onBlur=\"");
            handlers.append(onBlur);
            handlers.append("\"");
        }

        if (onFocus != null) {
            handlers.append(" onFocus=\"");
            handlers.append(onFocus);
            handlers.append("\"");
        }
    }




}
