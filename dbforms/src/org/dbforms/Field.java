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

package org.dbforms;

import java.util.*;
import org.dbforms.util.*;

import org.apache.log4j.Category;

/****
 * <p>
 * This class represents a field tag in dbforms-config.xml
 * </p>
 *
 */

public class Field {

	static Category logCat = Category.getInstance(Field.class.getName());
	// logging category for this class

	//------------------------ Constants ----------------------------------------------------------

	public static boolean ORDER_ASCENDING = false;
	public static boolean ORDER_DESCENDING = true;

	//------------------------ Properties ---------------------------------------------------------

	private int id; // the id of this field (for dbforms-internal use)
	private String name; // the field-name, as provided in xml-config file

	private String fieldType; // the type of the fiels
	private int type; // integer representation of the "fieldType"-value

	private String autoInc;
	// string supplied by the user indicating if the field is AUTO_INCREMENTING
	private boolean isAutoInc; // stores if the field is AUTOINCremental

	private String isKey;
	// string supplied by the user indicating if the field is a KEY
	private boolean key = false; // stores if the field is a KEY

	private String sortable;
	// string supplied by the user indicating if the field is SORTABLE
	private boolean _isSortable = false; // stores if the field is sortable

	private String directory;
	// used only for DISKBLOB: holds the directory uploaded files should be stored to

	private String encoding;
	// used only for DISKBLOB: if "true" -> then files will be renamed to ensure that every file is unique and no file overwrites another. default is "false" (keep original names)


	//------------------------ property access methods --------------------------------------------

	/**
	 * sets the id of this field-object
	 * (this method is called by Table on init.)
	 */
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
		setType(fieldType);
	}

	/**
	maps the field type description to internal value
	we need this information in oder to call the appropriate PreparedStatement.setXxx(..) - Methods
	*/
	public void setType(String aType) {
		aType = aType.toLowerCase();
		if (aType.startsWith("int")
			|| aType.startsWith("smallint")
			|| aType.startsWith("tinyint"))
			type = FieldTypes.INTEGER;
		else
			if (aType.startsWith("char")
				|| aType.startsWith("varchar")
				|| aType.startsWith("nvarchar")
				|| aType.startsWith("longchar"))
				type = FieldTypes.CHAR;
			else
				if (aType.startsWith("numeric") || aType.startsWith("number"))
					type = FieldTypes.NUMERIC;
				else
					if (aType.startsWith("date"))
						type = FieldTypes.DATE;
					else
						if (aType.startsWith("timestamp"))
							type = FieldTypes.TIMESTAMP;
						else
							if (aType.startsWith("double"))
								type = FieldTypes.DOUBLE;
							else
								if (aType.startsWith("float") || aType.startsWith("real"))
									type = FieldTypes.FLOAT;
								else
									if (aType.startsWith("blob") || aType.startsWith("image"))
										type = FieldTypes.BLOB;
									else
										if (aType.startsWith("diskblob"))
											type = FieldTypes.DISKBLOB;
	}

	public int getType() {
		return type;
	}

	public void setAutoInc(String autoInc) {
		this.autoInc = autoInc;
		this.isAutoInc =
			autoInc.equalsIgnoreCase("true") || autoInc.equalsIgnoreCase("yes");
	}

	public boolean getIsAutoInc() {
		return isAutoInc;
	}

	public void setIsKey(String isKey) {
		this.isKey = isKey;
		this.key = isKey.equalsIgnoreCase("true") || isKey.equalsIgnoreCase("yes");
	}

	public boolean isKey() {
		return key;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getDirectory() {
		return directory;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setSortable(String sortable) {
		logCat.info("***sortable setter called***");
		_isSortable = (!"false".equals(sortable));
	}
	/*
		public String getSortable() {
			return sortable;
		}*/

	public boolean isFieldSortable() {
		return _isSortable;
	}


	// --------------------- utility methods -------------------------------------------------------

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
		buf.append(" ! issortable=");
		buf.append(_isSortable);
		buf.append(" directory=");
		buf.append(directory);
		return buf.toString();
	}
}