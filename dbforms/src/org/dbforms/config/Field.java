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

package org.dbforms.config;
import java.text.Format;
import java.text.DateFormat;
import java.util.Locale;
import org.dbforms.util.Util;



/**
 * This class represents a field tag in dbforms-config.xml.
 *
 * @author foxat
 */
public class Field
{
   /** the id of this field (for dbforms-internal use) */
   private int id;

   /** the field-name, as provided in xml-config file */
   private String name;

   /** integer representation of the "fieldType"-value */
   private int type;

   /** stores if the field is AUTOINCremental */
   private boolean isAutoInc;

   /** stores if the field is a KEY */
   private boolean key = false;

   /** stores if the field is sortable */
   private boolean isSortable = false;

   /**
    * used only for DISKBLOB: holds the directory uploaded files should be
    * stored to
    */
   private String directory;

   /**
    * used only for DISKBLOB: if "true" -> then files will be renamed to ensure
    * that every file is unique and no file overwrites another. default is
    * "false" (keep original names)
    */
   private boolean encoded;

   /** */
   private String expression;

   /**
    * sets the id of this field-object (this method is called by Table on
    * init).
    *
    * @param id The new id value
    */
   public void setId(int id)
   {
      this.id = id;
   }


   /**
    * Gets the id attribute of the Field object
    *
    * @return The id value
    */
   public int getId()
   {
      return id;
   }


   /**
    * Sets the name attribute of the Field object
    *
    * @param name The new name value
    */
   public void setName(String name)
   {
      this.name = name;
   }


   /**
    * Gets the name attribute of the Field object
    *
    * @return The name value
    */
   public String getName()
   {
      return name;
   }


   /**
    * Maps the field type description to internal value. <br>
    * We need this information in oder to call the appropriate
    * PreparedStatement.setXxx(..) methods <br>
    * this method is called by the digester framework to set the fieldType!
    *
    * @param fieldType the type string value (example: "int", "char",
    *        "numeric", etc)
    */
   public void setFieldType(String fieldType)
   {
		fieldType = fieldType.toLowerCase();
		if (fieldType.startsWith("int") || fieldType.startsWith("smallint")
					 || fieldType.startsWith("tinyint"))
		{
			type = FieldTypes.INTEGER;
		}
		else if (fieldType.startsWith("char") || fieldType.startsWith("varchar")
							|| fieldType.startsWith("nvarchar")
							|| fieldType.startsWith("longchar")
							|| fieldType.startsWith("text"))
		{
			type = FieldTypes.CHAR;
		}
		else if (fieldType.startsWith("numeric")
							|| fieldType.startsWith("number"))
		{
			type = FieldTypes.NUMERIC;
		}
		else if (fieldType.startsWith("date"))
		{
			type = FieldTypes.DATE;
		}
		else if (fieldType.startsWith("timestamp"))
		{
			type = FieldTypes.TIMESTAMP;
		}
		else if (fieldType.startsWith("time"))
		{
			type = FieldTypes.TIME;
		}
		else if (fieldType.startsWith("double") || fieldType.startsWith("float"))
		{
			type = FieldTypes.DOUBLE;
		}
		else if (fieldType.startsWith("real"))
		{
			type = FieldTypes.FLOAT;
		}
		else if (fieldType.startsWith("blob") || fieldType.startsWith("image"))
		{
			type = FieldTypes.BLOB;
		}
		else if (fieldType.startsWith("diskblob"))
		{
			type = FieldTypes.DISKBLOB;
		}
   }

	public void setType(Class clazz) {
		if (clazz.isAssignableFrom(java.lang.Integer.class))
			type = FieldTypes.INTEGER;
		else if (clazz.isAssignableFrom(java.lang.Long.class))
			type = FieldTypes.INTEGER;
		else if (clazz.isAssignableFrom(java.lang.String.class))
			type = FieldTypes.CHAR;
		else if (clazz.isAssignableFrom(java.math.BigDecimal.class))
			type = FieldTypes.NUMERIC;
		else if (clazz.isAssignableFrom(java.sql.Date.class))
			type = FieldTypes.DATE;
		else if (clazz.isAssignableFrom(java.sql.Time.class))
			type = FieldTypes.TIME;
		else if (clazz.isAssignableFrom(java.sql.Timestamp.class))
			type = FieldTypes.TIMESTAMP;
		else if (clazz.isAssignableFrom(java.lang.Double.class))
			type = FieldTypes.DOUBLE;
		else if (clazz.isAssignableFrom(java.lang.Float.class))
			type = FieldTypes.FLOAT;
	}

   /**
    * Gets the type attribute of the Field object as numeric value. <br>
    * It's read only because the field type is set by the digester during
    * initialize!
    *
    * @return The type value
    */
   public int getType()
   {
      return type;
   }


   /**
    * Sets the autoInc attribute of the Field object
    *
    * @param autoInc The new autoInc value
    */
   public void setAutoInc(String autoInc)
   {
      this.isAutoInc = autoInc.equalsIgnoreCase("true")
                       || autoInc.equalsIgnoreCase("yes");
   }


   /**
    * Gets the isAutoInc attribute of the Field object
    *
    * @return The isAutoInc value
    */
   public boolean isAutoInc()
   {
      return isAutoInc;
   }


   /**
    * Sets the isKey attribute of the Field object
    *
    * @param isKey The new isKey value
    */
   public void setIsKey(String isKey)
   {
      this.key = isKey.equalsIgnoreCase("true")
                 || isKey.equalsIgnoreCase("yes");
   }


   /**
    * Gets the key attribute of the Field object
    *
    * @return The key value
    */
   public boolean getKey()
   {
      return key;
   }


   /**
    * Sets the directory attribute of the Field object
    *
    * @param directory The new directory value
    */
   public void setDirectory(String directory)
   {
      this.directory = directory;
   }


   /**
    * Gets the directory attribute of the Field object
    *
    * @return The directory value
    */
   public String getDirectory()
   {
      return directory;
   }


   /**
    * Sets the encoding attribute of the Field object
    *
    * @param encoding The new encoding value
    */
   public void setEncoding(String encoding)
   {
      this.encoded = encoding.equalsIgnoreCase("true")
                     || encoding.equalsIgnoreCase("yes");
   }


   /**
    * Gets the encoding attribute of the Field object
    *
    * @return The encoding value
    */
   public boolean isEncoded()
   {
      return encoded;
   }


   /**
    * Sets the sortable attribute of the Field object
    *
    * @param sortable The new sortable value
    */
   public void setSortable(String sortable)
   {
      this.isSortable = sortable.equalsIgnoreCase("true")
                        || sortable.equalsIgnoreCase("yes");
   }


   /**
    * Gets the fieldSortable attribute of the Field object
    *
    * @return The fieldSortable value
    */
   public boolean isSortable()
   {
      return isSortable;
   }


   /**
    * Sets the expression attribute of the Field object
    *
    * @param expression The new expression value
    */
   public void setExpression(String expression)
   {
      this.expression = expression;
   }


   /**
    * Gets the expression attribute of the Field object
    *
    * @return The expression value
    */
   public String getExpression()
   {
      return expression;
   }


   /**
    * DOCUMENT ME!
    *
    * @param pattern DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public Format getFormat(String pattern, Locale locale)
   {
      Format res   = null;
      int    dateStyle = DateFormat.MEDIUM;
	  int    timeStyle = DateFormat.SHORT;
      if (!Util.isNull(pattern))
      {
         if ("short".startsWith(pattern.toLowerCase()))
         {
			dateStyle = DateFormat.SHORT;
         }
         else if ("long".startsWith(pattern.toLowerCase()))
         {
			dateStyle = DateFormat.LONG;
         }
         else if ("full".startsWith(pattern.toLowerCase()))
         {
			dateStyle = DateFormat.FULL;
         }
      }

      switch (getType())
      {
         case FieldTypes.INTEGER:
            res = java.text.NumberFormat.getIntegerInstance(locale);

            if (!Util.isNull(pattern))
            {
               ((java.text.DecimalFormat) res).applyPattern(pattern);
            }

            break;

         case FieldTypes.NUMERIC:
         case FieldTypes.DOUBLE:
         case FieldTypes.FLOAT:
            res = java.text.NumberFormat.getInstance(locale);

            if (!Util.isNull(pattern))
            {
               ((java.text.DecimalFormat) res).applyPattern(pattern);
            }

            break;

         case FieldTypes.DATE:
            res = java.text.DateFormat.getDateInstance(dateStyle, locale);

            if (!Util.isNull(pattern))
            {
               ((java.text.SimpleDateFormat) res).applyPattern(pattern);
            }

            break;

         case FieldTypes.TIME:
            res = java.text.DateFormat.getTimeInstance(timeStyle, locale);

            if (!Util.isNull(pattern))
            {
               ((java.text.SimpleDateFormat) res).applyPattern(pattern);
            }

            break;

         case FieldTypes.TIMESTAMP:
            res = java.text.DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);

            if (!Util.isNull(pattern))
            {
               ((java.text.SimpleDateFormat) res).applyPattern(pattern);
            }

            break;

         default:
            break;
      }

      return res;
   }

   public static int parseFieldType(String fieldType) {
		int type = 0;
        return type;
   }
   
   /**
    * Get the String representation of this Field object.
    *
    * @return the String representation of this Field object
    */
   public String toString()
   {
      StringBuffer buf = new StringBuffer();

      buf.append("name=");
      buf.append(name);
      buf.append(" type=");
      buf.append(type);
      buf.append(" key=");
      buf.append(key);
      buf.append(" isAutoinc=");
      buf.append(isAutoInc);
      buf.append(" issortable=");
      buf.append(isSortable);
      buf.append(" directory=");
      buf.append(directory);
      buf.append(" expression=");
      buf.append(expression);

      return buf.toString();
   }


   /**
    * Dump the fieldValue objects contained into the input FieldValue array.
    *
    * @param fv the FieldValue array to dump
    *
    * @return the String object containing the dumped data, or null if the
    *         input array is null
    */
   public static final String dumpFieldValueArray(FieldValue[] fv)
   {
      String s = null;

      if (fv != null)
      {
         StringBuffer sb = new StringBuffer();

         for (int i = 0; i < fv.length; i++)
         {
            FieldValue f = fv[i];
            sb.append("  fv[").append(i).append("] = {").append(f.toString())
              .append("}\n");
         }

         s = sb.toString();
      }

      return s;
   }
}