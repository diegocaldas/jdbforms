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

// these 3 we need for formfield auto-population
import java.text.Format;
import java.util.Vector;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.Constants;
import org.dbforms.config.Field;
import org.dbforms.config.FieldTypes;
import org.dbforms.config.ResultSetVector;
import org.dbforms.event.WebEvent;
import org.dbforms.event.eventtype.EventType;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.MessageResources;
import org.dbforms.util.MessageResourcesInternal;
import org.dbforms.util.Util;
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
public abstract class DbBaseHandlerTag extends TagSupportWithScriptHandler {
   private static Category logCat = Category.getInstance(DbBaseHandlerTag.class.getName());

   private Field field;
   private String fieldName;
   private String defaultValue;
   private Format format;
   private String pattern;
   private String nullFieldValue;
   private String maxlength = null;
   private DbFormTag parentForm;

   /** Named Style class associated with component for read-only mode. */
   private String readOnlyStyleClass = null;
   private String readOnly = "false";

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
      return "true".equalsIgnoreCase(readOnly);
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
      Format f = getFormat();
      if (f == null)
         return null;
      if (f instanceof java.text.DecimalFormat)
         return ((java.text.DecimalFormat) f).toPattern();
      else if (f instanceof java.text.SimpleDateFormat)
         return ((java.text.SimpleDateFormat) f).toPattern();
      else
         return null;
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
   public Format getFormat() {
      if ((format == null) && (getField() != null)) {
         format = getField().getFormat(pattern, getLocale());
      }

      return this.format;
   }

   protected Locale getLocale() {
      return getParentForm().getLocale();
   }
   /**
    * DOCUMENT ME!
    *
    * @param format DOCUMENT ME!
    */
   public void setFormat(Format format) {
      this.format = format;
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected String typicalDefaultValue() {
      if (getField() != null) {
         switch (field.getType()) {
            case org.dbforms.config.FieldTypes.INTEGER :
               return "0";

            case org.dbforms.config.FieldTypes.NUMERIC :
               return "0";

            case org.dbforms.config.FieldTypes.DOUBLE :
               return "0.0";

            case org.dbforms.config.FieldTypes.FLOAT :
               return "0.0";

            default :
               return "";

               // in all other cases we just leave the formfield empty
         }
      } else {
         return "";
      }
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
         Object[] objectRow = res.getCurrentRowAsObjects();
         if (objectRow != null)
            fieldValueObj = objectRow[getField().getId()];
      } else {
         // try to get old value if we have an unbounded field!
         fieldValueObj = ParseUtil.getParameter((HttpServletRequest) pageContext.getRequest(), getFormFieldName());
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
         if (fieldValueObj.getClass().isArray() && "byte".equals(fieldValueObj.getClass().getComponentType().toString())) {
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
                     res = getFormat().format(fieldValueObj);
                  } catch (Exception e) {
                     logCat.error(
								  "field type: " + getField().getType() + "\n" 
								+ "object type: " + fieldValueObj.getClass().getName() + "\n"
								+ e.getMessage()
                           );
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

      return res.trim();
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
      HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
      Vector errors = (Vector) request.getAttribute("errors");
      WebEvent we = getParentForm().getWebEvent();

      // Are we in Update mode
      if (!getParentForm().getFooterReached()) {
         // Check if attribute 'redisplayFieldsOnError' has been set to true
         // and is this jsp displaying an error?
         if ((getParentForm().hasRedisplayFieldsOnErrorSet() && (errors != null) && (errors.size() > 0)) || ((we != null) && (we.getType() == EventType.EVENT_PAGE_RELOAD))) {
            // Yes - redisplay posted data
            String oldValue = ParseUtil.getParameter(request, getFormFieldName());
            if (oldValue != null)
               return oldValue;
         }
         return getFormattedFieldValue();
      } else {
         // the form field is in 'insert-mode'
         if (((we != null) && (we.getType() == EventType.EVENT_NAVIGATION_COPY))) {
            String copyValue = ParseUtil.getParameter(request, getFormFieldNameForCopyEvent());
            if (copyValue != null)
               return copyValue;
         }

         if ((we != null) && (we.getType() == EventType.EVENT_NAVIGATION_RELOAD) || (errors != null) && (errors.size() > 0)) {
            String oldValue = ParseUtil.getParameter(request, getFormFieldName());
            if (oldValue != null)
               return oldValue;
            // Patch to reload checkbox, because when unchecked checkbox is null
            // If unchecked, return anything different of typicalDefaultValue() ...
            if (this instanceof DbCheckboxTag)
               return typicalDefaultValue() + "_";
         }
         if (defaultValue != null)
            // default value defined by jsp-developer (provided via the "value" attribute of the tag)
            return defaultValue;

         // fill out empty fields so that there are no plain field-syntax errors
         // on database operations...
         return typicalDefaultValue();
      }
   }

   /**
      generates the decoded name for the html-widget.
    */
   protected String getFormFieldName() {
      StringBuffer buf = new StringBuffer();

      if ((getParentForm().getTable() != null) && (getField() != null)) {
         String keyIndex = (getParentForm().getFooterReached()) ? (Constants.FIELDNAME_INSERTPREFIX + getParentForm().getPositionPathCore()) : getParentForm().getPositionPath();

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
    * @return
    */
   public DbFormsConfig getConfig() {
      try {
         return DbFormsConfigRegistry.instance().lookup();
      } catch (Exception e) {
         logCat.error(e);
         return null;
      }
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
         res = MessageResourcesInternal.getMessage("dbforms.nodata", getLocale());
      // Resolve message if captionResource=true in the Form Tag
      if ((getParentForm() != null) && getParentForm().hasCaptionResourceSet()) {
         res = MessageResources.getMessage(res, getLocale());
      }

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
      writeOutOldValue();
   }

   /**
    * writes out the field value in hidden field _old
    */
   private void writeOutOldValue() throws JspException {
      try {
         StringBuffer tagBuf = new StringBuffer();
         tagBuf.append("<input type=\"hidden\" name=\"");
         tagBuf.append(Constants.FIELDNAME_OLDVALUETAG + getFormFieldName());
         tagBuf.append("\" value=\"");
         tagBuf.append(getFormFieldValue());
         tagBuf.append("\" />");
         pageContext.getOut().write(tagBuf.toString());
      } catch (java.io.IOException ioe) {
         throw new JspException("IO Error: " + ioe.getMessage());
      }
   }

   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      field = null;
      defaultValue = null;
      format = null;
      pattern = null;
      nullFieldValue = null;
      maxlength = null;
      readOnlyStyleClass = null;
      readOnly = "false";
      super.doFinally();
   }
}