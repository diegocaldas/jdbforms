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
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Field;
import org.dbforms.config.ResultSetVector;
import org.dbforms.event.NavCopyEvent;
import org.dbforms.event.WebEvent;
import org.dbforms.event.eventtype.EventType;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.MessageResources;
import javax.servlet.http.HttpServletRequest;
import org.dbforms.util.Util;




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
public abstract class DbBaseHandlerTag extends TagSupportWithScriptHandler
{

   private DbFormsConfig config;
   private String fieldName;
   private Field field;
   private String value;
   private Format format;
   private String nullFieldValue;
   private String maxlength = null;




   /**
    * DOCUMENT ME!
    *
    * @param fieldName DOCUMENT ME!
    */
   public void setFieldName(String fieldName)
   {
      this.fieldName = fieldName;

      if (getParentForm().getTable() != null)
      {
         this.field = getParentForm().getTable().getFieldByName(fieldName);
      }
      else
      {
         this.field = null;
      }

      if (getParentForm().isSubForm() && (this.field != null))
      {
         // tell parent that _this_ class will generate the html tag, not DbBodyTag!
			getParentForm().strikeOut(this.field);
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getFieldName()
   {
      return fieldName;
   }


   /**
   "value" is only used if parent tag is in "insert-mode" (footer, etc.)
           otherwise this tag takes the current value from the database result!
           */
   public String getValue()
   {
      return value;
   }


   /**
    * DOCUMENT ME!
    *
    * @param value DOCUMENT ME!
    */
   public void setValue(String value)
   {
      this.value = value;
   }

	/**
	 * generates the decoded name for the old value of the html-widget.
	 * 
	 * @return String
	 */
	private String getFormFieldNameOld()
	{
		return "o" + getFormFieldName();
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
	private void writeOutOldValue() throws JspException
	{
		try
		{
			StringBuffer tagBuf = new StringBuffer();
			tagBuf.append("<input type=\"hidden\" name=\"");
			tagBuf.append(getFormFieldNameOld());
			tagBuf.append("\" value=\"");
			tagBuf.append(getFormFieldValue());
			tagBuf.append("\" />");
			pageContext.getOut().write(tagBuf.toString());
		}	
		catch (java.io.IOException ioe)
		{
			throw new JspException("IO Error: " + ioe.getMessage());
		}

	}

   /**
   formatting a value
   */
   public Format getFormat()
   {
      return this.format;
   }


   /**
    * DOCUMENT ME!
    *
    * @param format DOCUMENT ME!
    */
   public void setFormat(Format format)
   {
      this.format = format;
   }



   /**
    * DOCUMENT ME!
    *
    * @param pageContext DOCUMENT ME!
    */
   public void setPageContext(final javax.servlet.jsp.PageContext pageContext)
   {
      super.setPageContext(pageContext);
      config = (DbFormsConfig) pageContext.getServletContext().getAttribute(DbFormsConfig.CONFIG);
   }



	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	protected String typicalDefaultValue()
	{
		if (field != null)
		{
			switch (field.getType())
			{
				case org.dbforms.config.FieldTypes.INTEGER:
					return "0";

				case org.dbforms.config.FieldTypes.NUMERIC:
					return "0";

				case org.dbforms.config.FieldTypes.DOUBLE:
					return "0.0";

				case org.dbforms.config.FieldTypes.FLOAT:
					return "0.0";

				default:
					return "";

				// in all other cases we just leave the formfield empty
			}
		}
		else
		{
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
      if (res != null) 
        fieldValueObj =res.getCurrentRowAsObjects()[getField().getId()];
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

      String fieldValue = (getNullFieldValue()==null) ? typicalDefaultValue(): getNullFieldValue();
      if (!ResultSetVector.isNull(getParentForm().getResultSetVector()))
      {
          Object fieldValueObj = getFieldObject();
          if (fieldValueObj != null)
          {
              // Fossato, 20002-08-29
              // uses the format class to format this tag's value;
             if (getFormat() != null)
             {
                fieldValue = format.format(fieldValueObj);
             }
             else
             {
                // if column object returned by database is of type 
                // 'array of byte: byte[]' (which can happen in case 
                // of eg. LONGVARCHAR columns), method toString would 
                // just return a sort of String representation
                // of the array's address. So in this case it is 
                // better to create a String using a corresponding
                // String constructor:
                if (fieldValue.getClass().isArray()
                         && "byte".equals(fieldValueObj.getClass()
                                                      .getComponentType()
                                                      .toString()))
                {
                   fieldValue = new String((byte[]) fieldValueObj);
                }
                else
                {
                   fieldValue = fieldValueObj.toString();
                }
             }
          }
      }
      return fieldValue.trim();
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
   protected String getFormFieldValue()
   {
      HttpServletRequest request = (HttpServletRequest) this.pageContext
         .getRequest();
      Vector             errors = (Vector) request.getAttribute("errors");
      WebEvent           we     = (WebEvent) request.getAttribute("webEvent");

      // Are we in Update mode
      if (!getParentForm().getFooterReached())
      {
         // Check if attribute 'redisplayFieldsOnError' has been set to true
         // and is this jsp displaying an error?
         if (("true".equals(getParentForm().getRedisplayFieldsOnError())
                  && (errors != null) && (errors.size() > 0))
                  || (we.getType() == EventType.EVENT_NAVIGATION_RELOAD))
         {
            // Yes - redisplay posted data
            String oldValue = ParseUtil.getParameter(request, getFormFieldName());
            return Util.isNull(oldValue)?typicalDefaultValue():oldValue;
         }
         else
         {
            return getFieldValue();
         }
      }
      else
      {
         if (we instanceof NavCopyEvent)
         {
		    String copyValue = ParseUtil.getParameter(request, getFormFieldNameForCopyEvent());
            if (copyValue != null)
                return copyValue;
		   } 

         // the form field is in 'insert-mode'
         if (we.getType() == EventType.EVENT_NAVIGATION_RELOAD)
         {
            String oldValue = ParseUtil.getParameter(request, getFormFieldName());

            if (oldValue != null)
            {
               return oldValue;
            }

            // Patch to reload checkbox, because when unchecked checkbox is null
            // If unchecked, return anything different of typicalDefaultValue() ...
            if (this instanceof DbCheckboxTag)
            {
               return typicalDefaultValue() + "_";
            }
         }

         //JOACHIM! CAN THE FOLLOWING LINE BE REMOVED? IT SEEMS TO BE OBSOLETE...
         if (value != null)
         {
            // default value defined by jsp-developer (provided via the "value" attribute of the tag)
            return value;
         }
         else
         { //#fixme: perform jsp/form equality check to avoid confision in cross-jsp actions

            if ((errors != null) && (errors.size() > 0))
            {
               // an insert error occured. this is the typical use case for automatic field-repopulation
               String oldValue = ParseUtil.getParameter(request,
                     getFormFieldName());

               if (oldValue != null)
               {
                  return oldValue;
               }
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
   protected String getFormFieldName()
   {
      StringBuffer buf = new StringBuffer();

      if ((getParentForm().getTable() != null) && (field != null))
      {
         String keyIndex = (getParentForm().getFooterReached())
            ? ("ins" + getParentForm().getPositionPathCore())
            : getParentForm().getPositionPath();

         buf.append("f_");
         buf.append(getParentForm().getTable().getId());
         buf.append("_");
         buf.append(keyIndex);
         buf.append("_");
         buf.append(field.getId());
      }
      else
      {
         buf.append(getFieldName());
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
      return config;
   }

   /**
    *  Sets the nullFieldValue attribute of the DbLabelTag object
    *
    * @param  nullFieldValue The new nullFieldValue value
    */
   public void setNullFieldValue(String nullFieldValue)
   {
       this.nullFieldValue = nullFieldValue;
   }

   /**
    *  Gets the nullFieldValue attribute of the DbLabelTag object
    *
    * @return  The nullFieldValue value
    */
   public String getNullFieldValue()
   {
      String res = nullFieldValue;
      // Resolve message if captionResource=true in the Form Tag
      if (getParentForm().getCaptionResource().equals("true"))
      {
          res = MessageResources.getMessage( (HttpServletRequest) pageContext.getRequest(), res);
      }
       return res;
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
   public String getMaxlength()
   {
       return maxlength;
   }

   /**
    * Sets the maxlength
    *
    * @param  maxlength The maxlength to set
    */
   public void setMaxlength(String maxlength)
   {
       this.maxlength = maxlength;
   }

}
