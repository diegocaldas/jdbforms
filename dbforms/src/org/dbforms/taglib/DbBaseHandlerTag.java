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
import java.text.Format;
import java.util.HashMap;
import java.util.Vector;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dbforms.config.Constants;
import org.dbforms.config.Field;
import org.dbforms.config.FieldTypes;
import org.dbforms.config.ResultSetVector;
import org.dbforms.event.WebEvent;
import org.dbforms.event.eventtype.EventType;
import org.dbforms.util.Formatter;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.MessageResources;
import org.dbforms.util.MessageResourcesInternal;
import org.dbforms.util.Escaper;
import org.dbforms.util.Util;
import org.dbforms.util.ReflectionUtil;
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
 * <p>Added support for Custom Formatter class, see SetCustomFormatter.java
 * Author Neal Katz .</p>
 * <p>Added support for overrideFormFieldName attribute. 
 * useful when working with customcontrollers.
 * Author Neal Katz .</p>
 *
 * orginally done by Don Clasen
 * @author Joe Peer (modified and extended this class for use in DbForms-Project)
 */
public abstract class DbBaseHandlerTag extends TagSupportWithScriptHandler {
   private static Category logCat =
      Category.getInstance(DbBaseHandlerTag.class.getName());

   private Field field;
   private String fieldName;
   private String defaultValue;
   private String pattern;
   private Format formatter;
   private String nullFieldValue;
   private String maxlength = null;
   private DbFormTag parentForm;

   /** Named Style class associated with component for read-only mode. */
   private String readOnlyStyleClass = null;
   private String readOnly = "false";
   private String escaperClass = null;
   private Escaper escaper = null;

	// n.k. allow the FormFieldName to be overridden
	private String overrideFormFieldName;

	// n.k. Support for CustomFormatter attribute - neal katz
	private String customFormatter = null;

   /**
    * DOCUMENT ME!
    *
    * @param parent DOCUMENT ME!
    */
   public void setParent(final javax.servlet.jsp.tagext.Tag parent) {
      super.setParent(parent);
      // between this form and its parent lies a DbHeader/Body/Footer-Tag and maybe other tags (styling, logic, etc.)
      parentForm = (DbFormTag) findAncestorWithClass(this, DbFormTag.class);
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected DbFormTag getParentForm() {
      return parentForm;
   }

   /** Sets the style class attribute for read-only mode. */
   public void setReadOnlyStyleClass(String readOnlyStyleClass) {
      this.readOnlyStyleClass = readOnlyStyleClass;
   }

   /** Returns the style class attribute for read-only mode. */
   public String getReadOnlyStyleClass() {
      return readOnlyStyleClass;
   }

   /** Sets the read-only attribute. */
   public void setReadOnly(String readOnly) {
      this.readOnly = readOnly;
   }

   /** Returns the read-only attribute. */
   public boolean hasReadOnlySet() {
      return Util.getTrue(readOnly);
   }

   /**
    * DOCUMENT ME!
    *
    * @param fieldName DOCUMENT ME!
    */
   public void setFieldName(String fieldName) {
      this.fieldName = fieldName;
      if (getParentForm().getTable() != null) {
         setField(getParentForm().getTable().getFieldByName(fieldName));
      } else {
         setField(null);
      }
      if (getParentForm().isSubForm() && (this.field != null)) {
         // tell parent that _this_ class will generate the html tag, not DbBodyTag!
         getParentForm().strikeOut(this.field);
      }
   }

   /**
      "value" is only used if parent tag is in "insert-mode" (footer, etc.)
              otherwise this tag takes the current value from the database result!
    */
   public String getDefaultValue() {
      return defaultValue;
   }

   /**
    * DOCUMENT ME!
    *
    * @param value DOCUMENT ME!
    */
   public void setDefaultValue(String value) {
      this.defaultValue = value;
   }

   /**
    * @return
    */
   public String getPattern() {
      Format f = getFormatter();
      if (f == null)
         return null;
      return Util.getPattern(f);
   }

   /**
    * @return
    */
   public String getName() {
      return (getField() != null) ? getField().getName() : fieldName;
   }

   /**
    * @param string
    */
   public void setPattern(String string) {
      pattern = string;
   }

   /**
      formatting a value
    */
   public void setFormatter(Format formatter) {
      this.formatter = formatter;
   }
   /**
      formatting a value
    */
   public Format getFormatter() {
      Format res = formatter;
      if ((res == null) && (getField() != null)) {
         res = getField().getFormat(pattern, getLocale());
      }
      return res;
   }

   protected Locale getLocale() {
      return getParentForm().getLocale();
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected String typicalDefaultValue() {
      // 20030113-HKK: Change to use format too
      String res = "";
      if (getField() != null) {

         switch (field.getType()) {
            case org.dbforms.config.FieldTypes.INTEGER :
            case org.dbforms.config.FieldTypes.NUMERIC :
            case org.dbforms.config.FieldTypes.DOUBLE :
            case org.dbforms.config.FieldTypes.FLOAT :
               try {
                  res = getFormatter().format(new Double(0));
               } catch (Exception e) {
                  res = "0";
               }
               // in all other cases we just leave the formfield empty
         }
      }
      return res;
   }

   /**
    * return the object value from the database
    * @return the object
    *
    */
   protected Object getFieldObject() {
      Object fieldValueObj = null;
      ResultSetVector res = getParentForm().getResultSetVector();

      if ((res != null) && (getField() != null)) {
         fieldValueObj = res.getFieldAsObject(getField().getId());
      } else {
         // try to get old value if we have an unbounded field!
         fieldValueObj =
            ParseUtil.getParameter(
               (HttpServletRequest) pageContext.getRequest(),
               getFormFieldName());
         if (fieldValueObj == null) {
            // if we have an unbounded field and no old value then use default!
            fieldValueObj = getDefaultValue();
         } else {
            fieldValueObj =
               (getEscaper() == null)
                  ? fieldValueObj
                  : getEscaper().unescapeHTML((String) fieldValueObj);
         }
      }
      return fieldValueObj;
   }

   /**
    *
    * fetches the value from the database. if no value is given, contents of attribute
    * nullFieldValue is returned.
    *
    * @return the field value
    */
   protected String getFieldValue() {
      ResultSetVector rsv = getParentForm().getResultSetVector();
      String res = null;
      if ((getField() != null) && (rsv != null)) {
         String[] s = rsv.getCurrentRow();
         if (s != null)
            res = rsv.getCurrentRow()[getField().getId()];
      }
      return res;
   }

   /**
    *
    * fetches the value from the database. if no value is given, contents of attribute
    * nullFieldValue is returned.
    *
    * @return the field value
    */
   protected String getFormattedFieldValue() {
      Object fieldValueObj = getFieldObject();
      String res;

      if (fieldValueObj == null) {
         res = getNullFieldValue();
      } else {

         // if column object returned by database is of type 
         // 'array of byte: byte[]' (which can happen in case 
         // of eg. LONGVARCHAR columns), method toString would 
         // just return a sort of String representation
         // of the array's address. So in this case it is 
         // better to create a String using a corresponding
         // String constructor:
         if (fieldValueObj.getClass().isArray()
            && "byte".equals(
               fieldValueObj.getClass().getComponentType().toString())) {
            res = new String((byte[]) fieldValueObj);
         } else if (getField() != null) {
            switch (getField().getType()) {
               case FieldTypes.INTEGER :
               case FieldTypes.DOUBLE :
               case FieldTypes.FLOAT :
               case FieldTypes.NUMERIC :
               case FieldTypes.DATE :
               case FieldTypes.TIME :
               case FieldTypes.TIMESTAMP :
                  try {
                     res = getFormatter().format(fieldValueObj);
                  } catch (Exception e) {
                     logCat.error(
                        "field type: "
                           + getField().getType()
                           + "\n"
                           + "object type: "
                           + fieldValueObj.getClass().getName()
                           + "\n"
                           + "pattern: "
                           + getPattern()
                           + "\n"
                           + e.getMessage());
                     res = fieldValueObj.toString();
                  }

                  break;

               case FieldTypes.BLOB :
               case FieldTypes.DISKBLOB :
               case FieldTypes.CHAR :
               default :
                  res = fieldValueObj.toString();

                  break;
            }
         } else {
            res = fieldValueObj.toString();
         }
      }
      // add support for custom formatting - neal katz
      res = customFormat(res);
      return res;
   }

   /**
    * grunikiewicz.philip@hydro.qc.ca
    * 2001-05-31
    * determinates value of the html-widget.
    *
    * In a jsp which contains many input fields, it may be desirable, in the event of an error, to redisplay input data.
    * (instead of refreshing the fields from the DB) Currently dbforms implements this functionality with INSERT fields only.
    * The following describes the changes I've implemented:
    *
    *  - I've added a new attribute in the Form tag which sets the functionality (redisplayFieldsOnError=true/false)
    *  - I've modified the code below to handle the redisplay of previously posted information
    *
    */
   protected String getFormFieldValue() {
      HttpServletRequest request =
         (HttpServletRequest) this.pageContext.getRequest();
      Vector errors = (Vector) request.getAttribute("errors");
      WebEvent we = getParentForm().getWebEvent();

      // Are we in Update mode
      if (!getParentForm().getFooterReached()) {
         // Check if attribute 'redisplayFieldsOnError' has been set to true
         // and is this jsp displaying an error?
         if ((getParentForm().hasRedisplayFieldsOnErrorSet()
            && (errors != null)
            && (errors.size() > 0))
            || ((we != null)
               && EventType.EVENT_NAVIGATION_RELOAD.equals(we.getType()))) {
            // Yes - redisplay posted data
            String oldValue =
               ParseUtil.getParameter(request, getFormFieldName());
            if (oldValue != null)
               return oldValue;
         }
         return getFormattedFieldValue();
      } else {
         // the form field is in 'insert-mode'
         if (((we != null)
            && (EventType.EVENT_NAVIGATION_COPY.equals(we.getType())))) {
            String copyValue =
               ParseUtil.getParameter(request, getFormFieldNameForCopyEvent());
            if (copyValue != null)
               return copyValue;
         }

         if (((we != null)
            && EventType.EVENT_NAVIGATION_RELOAD.equals(we.getType()))
            || ((errors != null) && (errors.size() > 0))) {
            String oldValue =
               ParseUtil.getParameter(request, getFormFieldName());
            if (oldValue != null)
               return oldValue;
            // Patch to reload checkbox, because when unchecked checkbox is null
            // If unchecked, return anything different of typicalDefaultValue() ...
            if (this instanceof DbCheckboxTag)
               return typicalDefaultValue() + "_";
         }
         return getFormFieldDefaultValue();
      }
   }

   protected String getFormFieldDefaultValue() {
      if (defaultValue != null)
         // default value defined by jsp-developer (provided via the "value" attribute of the tag)
         return defaultValue;

      // fill out empty fields so that there are no plain field-syntax errors
      // on database operations...
      return typicalDefaultValue();
   }

   /**
      generates the decoded name for the html-widget.
    */
   protected String getFormFieldName() {
       if (hasOverrideFormFieldNameSet()) {
      	return getOverrideFormFieldName();
      }
      StringBuffer buf = new StringBuffer();
      if ((getParentForm().getTable() != null) && (getField() != null)) {
         String keyIndex =
            (getParentForm().getFooterReached())
               ? (Constants.FIELDNAME_INSERTPREFIX
                  + getParentForm().getPositionPathCore())
               : getParentForm().getPositionPath();
         buf.append(Constants.FIELDNAME_PREFIX);
         buf.append(getParentForm().getTable().getId());
         buf.append("_");
         buf.append(keyIndex);
         buf.append("_");
         buf.append(getField().getId());
      } else {
         buf.append(fieldName);
      }
      return buf.toString();
   }

   /**
    * generates the decoded name for the html-widget in the case of copy events.
    */
   private String getFormFieldNameForCopyEvent() {
      boolean footerReached = getParentForm().getFooterReached();
      getParentForm().setFooterReached(false);

      String name = getFormFieldName();
      getParentForm().setFooterReached(footerReached);

      return name;
   }

   /**
    *  Sets the nullFieldValue attribute of the DbLabelTag object
    *
    * @param  nullFieldValue The new nullFieldValue value
    */
   public void setNullFieldValue(String nullFieldValue) {
      this.nullFieldValue = nullFieldValue;
   }

   /**
    *  Gets the nullFieldValue attribute of the DbLabelTag object
    *
    * @return  The nullFieldValue value
    */
   private String getNullFieldValue() {
      String res = nullFieldValue;
      if (res == null)
         res =
            MessageResourcesInternal.getMessage("dbforms.nodata", getLocale());
      // Resolve message if captionResource=true in the Form Tag
      if ((getParentForm() != null)
         && getParentForm().hasCaptionResourceSet()) {
         res = MessageResources.getMessage(res, getLocale());
      }

      /*******************************************
       * Grunikiewicz.philip@hydro.qc.cq
       * 2003-12-04
       * 
       * The data being return has a value of null.  The developer has not specified a substitute. So
       * instead of crashing, lets display an empty string! 
       */
      if (res == null)
         res = "";

      return res;
   }

   protected void setField(Field field) {
      this.field = field;
   }

   /**
    * @return
    */
   public Field getField() {
      return field;
   }

   /**
    * Gets the maxlength
    *
    * @return  Returns a String
    */
   public String getMaxlength() {
      return maxlength;
   }

   /**
    * Sets the maxlength
    *
    * @param  maxlength The maxlength to set
    */
   public void setMaxlength(String maxlength) {
      this.maxlength = maxlength;
   }

   public String getStyleClass() {
      boolean readonly = hasReadOnlySet() || getParentForm().hasReadOnlySet();
      if (readonly && !Util.isNull(getReadOnlyStyleClass()))
         return getReadOnlyStyleClass();
      else
         return super.getStyleClass();
   }

   /**
    * writes out all hidden fields for the input fields
    */
   protected void writeOutSpecialValues() throws JspException {
      try {
         pageContext.getOut().write(renderOldValueHtmlInputField());
      } catch (java.io.IOException ioe) {
         throw new JspException("IO Error: " + ioe.getMessage());
      }
   }

   /**
    * writes out the field value in hidden field _old
    */
   protected String renderOldValueHtmlInputField() {
      StringBuffer tagBuf = new StringBuffer();
      tagBuf.append("<input type=\"hidden\" name=\"");
      tagBuf.append(Constants.FIELDNAME_OLDVALUETAG + getFormFieldName());
      tagBuf.append("\" value=\"");
      if (!getParentForm().getFooterReached()) {
         tagBuf.append(escapeHtml(getFormattedFieldValue()));
      } else {
         tagBuf.append(escapeHtml(getFormFieldDefaultValue()));
      }
      tagBuf.append("\" />");
      return tagBuf.toString();
   }

   /** 
	* writes out the current used format to the page
	* @throws JspException
	*/
   protected String renderPatternHtmlInputField() {
	  StringBuffer tagBuf = new StringBuffer();
	  String pattern = getPattern();
	  if (!Util.isNull(pattern)) {
		 tagBuf.append("<input type=\"hidden\" name=\"");
		 tagBuf.append(Constants.FIELDNAME_PATTERNTAG + getFormFieldName());
		 tagBuf.append("\" value=\"");
		 tagBuf.append(pattern);
		 tagBuf.append("\" />");
	  }
	  return tagBuf.toString();
   }


   public Escaper getEscaper() {
      if (escaper == null) {
         String s = getEscaperClass();
         if (!Util.isNull(s)) {
            try {
               escaper = (Escaper) ReflectionUtil.newInstance(s);
            } catch (Exception e) {
               logCat.error(
                  "cannot create the new escaper [" + s + "]",
                  e);
            }
         }
         if ((escaper == null) && (getField() != null)) {
            escaper = getField().getEscaper();
         }
         if ((escaper == null)) {
            escaper = getConfig().getEscaper();
         }
      }
      return escaper;
   }

   protected String escapeHtml(String html) {
      return (getEscaper() == null) ? html : getEscaper().escapeHTML(html);
   }

   /**
    * DOCUMENT ME!
   */
	public void doFinally() {
		formatter = null;
		field = null;
		defaultValue = null;
		pattern = null;
		nullFieldValue = null;
		maxlength = null;
		readOnlyStyleClass = null;
		readOnly = "false";
		escaperClass = null;
		escaper = null;
		customFormatter = null;
		overrideFormFieldName = null;
		super.doFinally();
	} 
   /**
   * @return
  */
   public String getEscaperClass() {
      return escaperClass;
   } 
   /**
   * @param string
   */
   public void setEscaperClass(String string) {
      escaperClass = string;
   }
	/**
	 * @return
	 */
	protected String getCustomFormatter() {
		return customFormatter;
	}

	/**
	 * @param string
	 */
	public void setCustomFormatter(String string) {
		customFormatter = string;
	}

	/**
	 * @param s
	 * @return
	 */
	public String customFormat(String s) {
		if ((customFormatter != null) && (customFormatter.length() > 0)) {
			HttpSession session = pageContext.getSession();
			HashMap hm =
				(HashMap) session.getAttribute(
					SetCustomFormatterTag.sessionKey);
			if (hm != null) {
				Object obj = hm.get(customFormatter);
				if (obj instanceof Formatter) {
					Formatter cf = (Formatter) obj;
					Object[] o = new Object[] {s, field, this};
					cf.setLocale(getLocale());
					s = cf.sprintf(o);
				}
			}
		}
		return s;
	}
	/**
	 * @return
	 */
	protected String getOverrideFormFieldName() {
		return overrideFormFieldName;
	}

	/**
	 * @param string
	 */
	public void setOverrideFormFieldName(String string) {
		overrideFormFieldName = string;
	}
	/**
	 * @return
	 */
	protected boolean hasOverrideFormFieldNameSet() {
		return overrideFormFieldName != null;
	}

}