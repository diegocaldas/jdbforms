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
import java.util.Vector;
import org.dbforms.util.Util;
import org.dbforms.util.MessageResourcesInternal;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.Escaper;
import org.dbforms.util.ReflectionUtil;
import org.apache.log4j.Category;

/**
 * This class represents a field tag in dbforms-config.xml.
 * 
 * @author foxat
 */
public class Field {
	private static Category logCat = Category.getInstance(Field.class.getName());

	/** the id of this field (for dbforms-internal use) */
	private int id;

	/** the field-name, as provided in xml-config file */
	private String name;

	/** integer representation of the "fieldType"-value */
	private int type;

	/** stores if the field is AUTOINCremental */
	private boolean isAutoInc = false;

	/** stores if the field is a KEY */
	private boolean key = false;

	/** stores if the field is sortable */
	private boolean isSortable = false;

	/**
	 * used only for DISKBLOB: holds the directory uploaded files should be
	 * stored to
	 */
	private String directory = null;

	/**
	 * used only for DISKBLOB: if "true" -> then files will be renamed to ensure
	 * that every file is unique and no file overwrites another. default is
	 * "false" (keep original names)
	 */
	private boolean encoded = false;

	/** the field-size */
	private int size = -1;

	private String expression = null;
	private String fieldType = null;
	private String escaperClass = null;
	private Escaper escaper = null;
	private Table table = null;
	/**
	 * sets the id of this field-object (this method is called by Table on
	 * init).
	 * 
	 * @param id The new id value
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the id attribute of the Field object
	 * 
	 * @return The id value
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the name attribute of the Field object
	 * 
	 * @param name The new name value
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the name attribute of the Field object
	 * 
	 * @return The name value
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getFieldType() {
		return fieldType;
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
	public void setFieldType(String aFieldType) {
		this.fieldType = aFieldType.toLowerCase();
		if (fieldType.startsWith("char")
			|| fieldType.startsWith("varchar")
			|| fieldType.startsWith("nvarchar")
			|| fieldType.startsWith("longchar")
			|| fieldType.startsWith("long varchar")
			|| fieldType.startsWith("text")) {
			type = FieldTypes.CHAR;
		} else if (
			fieldType.startsWith("int")
				|| fieldType.startsWith("smallint")
				|| fieldType.startsWith("long")
				|| fieldType.startsWith("tinyint")) {
			type = FieldTypes.INTEGER;
		} else if (
			fieldType.startsWith("numeric")
				|| fieldType.startsWith("number")
				|| fieldType.startsWith("decimal")) {
			type = FieldTypes.NUMERIC;
		} else if (fieldType.startsWith("date")) {
			type = FieldTypes.DATE;
		} else if (fieldType.startsWith("timestamp")) {
			type = FieldTypes.TIMESTAMP;
		} else if (fieldType.startsWith("time")) {
			type = FieldTypes.TIME;
		} else if (
			fieldType.startsWith("double") || fieldType.startsWith("float")) {
			type = FieldTypes.DOUBLE;
		} else if (fieldType.startsWith("real")) {
			type = FieldTypes.FLOAT;
		} else if (
			fieldType.startsWith("blob") || fieldType.startsWith("image")) {
			type = FieldTypes.BLOB;
		} else if (fieldType.startsWith("diskblob")) {
			type = FieldTypes.DISKBLOB;
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param obj DOCUMENT ME!
	 */
	public void setTypeByObject(Object obj) {
		if (obj == null) {
			return;
		}
		Class clazz = obj.getClass();
		Vector v = ParseUtil.splitString(clazz.getName().toLowerCase(), ".");
		fieldType = (String) v.lastElement();
		if (clazz.isAssignableFrom(java.lang.Integer.class)) {
			type = FieldTypes.INTEGER;
		} else if (clazz.isAssignableFrom(java.lang.Long.class)) {
			type = FieldTypes.INTEGER;
		} else if (clazz.isAssignableFrom(java.lang.String.class)) {
			type = FieldTypes.CHAR;
		} else if (clazz.isAssignableFrom(java.math.BigDecimal.class)) {
			type = FieldTypes.NUMERIC;
		} else if (clazz.isAssignableFrom(java.sql.Date.class)) {
			type = FieldTypes.DATE;
		} else if (clazz.isAssignableFrom(java.sql.Time.class)) {
			type = FieldTypes.TIME;
		} else if (clazz.isAssignableFrom(java.sql.Timestamp.class)) {
			type = FieldTypes.TIMESTAMP;
		} else if (clazz.isAssignableFrom(java.lang.Double.class)) {
			type = FieldTypes.DOUBLE;
		} else if (clazz.isAssignableFrom(java.lang.Float.class)) {
			type = FieldTypes.FLOAT;
		}
	}

	/**
	 * Gets the type attribute of the Field object as numeric value. <br>
	 * It's read only because the field type is set by the digester during
	 * initialize!
	 * 
	 * @return The type value
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the autoInc attribute of the Field object
	 * 
	 * @param autoInc The new autoInc value
	 */
	public void setAutoInc(String autoInc) {
		this.isAutoInc =
			autoInc.equalsIgnoreCase("true") || autoInc.equalsIgnoreCase("yes");
	}

	/**
	 * Gets the isAutoInc attribute of the Field object
	 * 
	 * @return The isAutoInc value
	 */
	public boolean hasAutoIncSet() {
		return isAutoInc;
	}

	/**
	 * Sets the isKey attribute of the Field object
	 * 
	 * @param isKey The new isKey value
	 */
	public void setIsKey(String isKey) {
		this.key =
			isKey.equalsIgnoreCase("true") || isKey.equalsIgnoreCase("yes");
	}

	/**
	 * Gets the key attribute of the Field object
	 * 
	 * @return The key value
	 */
	public boolean getKey() {
		return key;
	}

	/**
	 * Sets the directory attribute of the Field object
	 * 
	 * @param directory The new directory value
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	/**
	 * Gets the directory attribute of the Field object
	 * 
	 * @return The directory value
	 */
	public String getDirectory() {
		return directory;
	}

	/**
	 * Sets the encoding attribute of the Field object
	 * 
	 * @param encoding The new encoding value
	 */
	public void setEncoding(String encoding) {
		this.encoded =
			encoding.equalsIgnoreCase("true") || encoding.equalsIgnoreCase("yes");
	}

	/**
	 * Gets the encoding attribute of the Field object
	 * 
	 * @return The encoding value
	 */
	public boolean hasEncodedSet() {
		return encoded;
	}

	/**
	 * Sets the sortable attribute of the Field object
	 * 
	 * @param sortable The new sortable value
	 */
	public void setSortable(String sortable) {
		this.isSortable =
			sortable.equalsIgnoreCase("true") || sortable.equalsIgnoreCase("yes");
	}

	/**
	 * Gets the fieldSortable attribute of the Field object
	 * 
	 * @return The fieldSortable value
	 */
	public boolean hasSortableSet() {
		return isSortable;
	}

	/**
	 * Sets the expression attribute of the Field object
	 * 
	 * @param expression The new expression value
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * Gets the expression attribute of the Field object
	 * 
	 * @return The expression value
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param pattern DOCUMENT ME!
	 * @param locale DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Format getFormat(String pattern, Locale locale) {
		switch (getType()) {
			case FieldTypes.INTEGER :
			case FieldTypes.NUMERIC :
			case FieldTypes.DOUBLE :
			case FieldTypes.FLOAT :
			case FieldTypes.DATE :
			case FieldTypes.TIME :
			case FieldTypes.TIMESTAMP :
				break;
			default :
				return null;
		}
		Format res = null;
		int dateStyle = Constants.DATE_STYLE_DEFAULT;
		int timeStyle = Constants.TIME_STYLE_DEFAULT;
		if (Util.isNull(pattern)) {
			pattern =
				MessageResourcesInternal.getMessage(
					"dbforms.pattern." + getFieldType(),
					locale);
		}
		if (!Util.isNull(pattern)) {
			if ("short".startsWith(pattern.toLowerCase())) {
				dateStyle = DateFormat.SHORT;
				pattern = MessageResourcesInternal.getMessage("dbforms.pattern." + getFieldType() + "." + pattern, locale);
			} else if ("medium".startsWith(pattern.toLowerCase())) {
				dateStyle = DateFormat.MEDIUM;
				pattern = MessageResourcesInternal.getMessage("dbforms.pattern." + getFieldType() + "." + pattern, locale);
			} else if ("long".startsWith(pattern.toLowerCase())) {
				dateStyle = DateFormat.LONG;
				pattern = MessageResourcesInternal.getMessage("dbforms.pattern." + getFieldType() + "." + pattern, locale);
			} else if ("full".startsWith(pattern.toLowerCase())) {
				dateStyle = DateFormat.FULL;
				pattern = MessageResourcesInternal.getMessage("dbforms.pattern." + getFieldType() + "." + pattern, locale);
			}
		}

		switch (getType()) {
			case FieldTypes.INTEGER :
				res = java.text.NumberFormat.getIntegerInstance(locale);
				((java.text.DecimalFormat) res).setParseIntegerOnly(true);
				if (!Util.isNull(pattern)) {
					((java.text.DecimalFormat) res).applyPattern(pattern);
				}
				break;

			case FieldTypes.NUMERIC :
			case FieldTypes.DOUBLE :
			case FieldTypes.FLOAT :
				res = java.text.NumberFormat.getNumberInstance(locale);
				if (!Util.isNull(pattern)) {
					((java.text.DecimalFormat) res).applyPattern(pattern);
				}
				break;

			case FieldTypes.DATE :
				res = java.text.DateFormat.getDateInstance(dateStyle, locale);
				if (!Util.isNull(pattern)) {
					((java.text.SimpleDateFormat) res).applyPattern(pattern);
				}
				break;

			case FieldTypes.TIME :
				res = java.text.DateFormat.getTimeInstance(timeStyle, locale);
				if (!Util.isNull(pattern)) {
					((java.text.SimpleDateFormat) res).applyPattern(pattern);
				}
				break;

			case FieldTypes.TIMESTAMP :
				res =
					java.text.DateFormat.getDateTimeInstance(
						dateStyle,
						timeStyle,
						locale);
				if (!Util.isNull(pattern)) {
					((java.text.SimpleDateFormat) res).applyPattern(pattern);
				}
				break;

			default :
				break;
		}

		return res;
	}

	/**
	 * Get the String representation of this Field object.
	 * 
	 * @return the String representation of this Field object
	 */
	public String toString() {
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
	public static final String dumpFieldValueArray(FieldValue[] fv) {
		String s = null;

		if (fv != null) {
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < fv.length; i++) {
				FieldValue f = fv[i];
				sb.append("  fv[").append(i).append("] = {").append(
					f.toString()).append(
					"}\n");
			}

			s = sb.toString();
		}

		return s;
	}

	/**
	 * Sets the size attribute of the Field object
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Gets the size attribute of the Field object
	 */
	public int getSize() {
		return size;
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

	public Escaper getEscaper() {
		if (escaper == null) {
			String s = getEscaperClass();
			if (!Util.isNull(s)) {
				try {
					escaper = (Escaper) ReflectionUtil.newInstance(s);
				} catch (Exception e) {
					logCat.error("cannot create the new escaper [" + s + "]", e);
				}
			}
			if (escaper == null) {
				if (getTable() == null) {
					try {
						escaper =
							DbFormsConfigRegistry.instance().lookup().getEscaper();
					} catch (Exception e) {
						logCat.error("cannot create the new default escaper", e);
					}
				} else {
					escaper = getTable().getEscaper();
				}
			}
		}
		return escaper;
	}

	/**
	 * @return
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * @param table
	 */
	public void setTable(Table table) {
		this.table = table;
	}

}