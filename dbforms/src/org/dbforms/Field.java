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

import org.apache.log4j.Category;import org.dbforms.util.FieldTypes;



/**
 * This class represents a field tag in dbforms-config.xml.
 *
 * @author  foxat
 * @created  23 dicembre 2002
 */
public class Field
{
    /** logging category for this class */
    static Category logCat = Category.getInstance(Field.class.getName());

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
     * used only for DISKBLOB: holds the directory uploaded files should be stored to
     */
    private String directory;

    /**
     * used only for DISKBLOB: if "true" -> then files will be renamed to ensure that
     * every file is unique and no file overwrites another. default is "false"
     * (keep original names)
     */
    private String encoding;

    /** */
    private String expression;


    /**
     * sets the id of this field-object
     * (this method is called by Table on init).
     *
     * @param  id The new id value
     */
    public void setId(int id)
    {
        this.id = id;
    }


    /**
     *  Gets the id attribute of the Field object
     *
     * @return  The id value
     */
    public int getId()
    {
        return id;
    }


    /**
     *  Sets the name attribute of the Field object
     *
     * @param  name The new name value
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     *  Gets the name attribute of the Field object
     *
     * @return  The name value
     */
    public String getName()
    {
        return name;
    }


    /**
     *  Sets the fieldType attribute of the Field object
     *
     * @param  fieldType The new fieldType value
     */
    //public void setFieldType(String fieldType)
    //{
      //  setType(fieldType);
//    }


    /**
     *  Maps the field type description to internal value.
     *  <br>
     *  We need this information in oder to call the appropriate
     *  PreparedStatement.setXxx(..) methods
     *
     * @param  aType the type string value (example: "int", "char", "numeric", etc)
     */
    public void setFieldType(String aType)
    {
        aType = aType.toLowerCase();

        if (aType.startsWith("int") || aType.startsWith("smallint") || aType.startsWith("tinyint"))
        {
            type = FieldTypes.INTEGER;
        }
        else if (aType.startsWith("char") || aType.startsWith("varchar") || aType.startsWith("nvarchar") || aType.startsWith("longchar"))
        {
            type = FieldTypes.CHAR;
        }
        else if (aType.startsWith("numeric") || aType.startsWith("number"))
        {
            type = FieldTypes.NUMERIC;
        }
        else if (aType.startsWith("date"))
        {
            type = FieldTypes.DATE;
        }
        else if (aType.startsWith("timestamp"))
        {
            type = FieldTypes.TIMESTAMP;
        }
        else if (aType.startsWith("double"))
        {
            type = FieldTypes.DOUBLE;
        }
        else if (aType.startsWith("float") || aType.startsWith("real"))
        {
            type = FieldTypes.FLOAT;
        }
        else if (aType.startsWith("blob") || aType.startsWith("image"))
        {
            type = FieldTypes.BLOB;
        }
        else if (aType.startsWith("diskblob"))
        {
            type = FieldTypes.DISKBLOB;
        }
    }


    /**
     *  Gets the type attribute of the Field object
     *
     * @return  The type value
     */
    public int getFieldType()
    {
        return type;
    }	/**	 *  Gets the type attribute of the Field object	 *	 * @return  The type value	 * @deprecated Should use getFieldType()	 */    public int getType(){    	return getFieldType();    }


    /**
     *  Sets the autoInc attribute of the Field object
     *
     * @param  autoInc The new autoInc value
     */
    public void setAutoInc(String autoInc)
    {
        this.isAutoInc = autoInc.equalsIgnoreCase("true") || autoInc.equalsIgnoreCase("yes");
    }


    /**
     *  Gets the isAutoInc attribute of the Field object
     *
     * @return  The isAutoInc value
     */
    public boolean getIsAutoInc()
    {
        return isAutoInc;
    }


    /**
     *  Sets the isKey attribute of the Field object
     *
     * @param  isKey The new isKey value
     */
    public void setIsKey(String isKey)
    {
        this.key = isKey.equalsIgnoreCase("true") || isKey.equalsIgnoreCase("yes");
    }


    /**
     *  Gets the key attribute of the Field object
     *
     * @return  The key value
     */
    public boolean isKey()
    {
        return key;
    }


    /**
     *  Sets the directory attribute of the Field object
     *
     * @param  directory The new directory value
     */
    public void setDirectory(String directory)
    {
        this.directory = directory;
    }


    /**
     *  Gets the directory attribute of the Field object
     *
     * @return  The directory value
     */
    public String getDirectory()
    {
        return directory;
    }


    /**
     *  Sets the encoding attribute of the Field object
     *
     * @param  encoding The new encoding value
     */
    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }


    /**
     *  Gets the encoding attribute of the Field object
     *
     * @return  The encoding value
     */
    public String getEncoding()
    {
        return encoding;
    }


    /**
     *  Sets the sortable attribute of the Field object
     *
     * @param  sortable The new sortable value
     */
    public void setSortable(String sortable)
    {
        logCat.info("***sortable setter called***");
        this.isSortable = sortable.equalsIgnoreCase("true") || sortable.equalsIgnoreCase("yes");
    }


    /**
     *  Gets the fieldSortable attribute of the Field object
     *
     * @return  The fieldSortable value
     */
    public boolean isFieldSortable()
    {
        return isSortable;
    }


    /**
     *  Sets the expression attribute of the Field object
     *
     * @param  expression The new expression value
     */
    public void setExpression(String expression)
    {
        this.expression = expression;
    }


    /**
     *  Gets the expression attribute of the Field object
     *
     * @return  The expression value
     */
    public String getExpression()
    {
        return expression;
    }


    /**
     *  Get the String representation of this Field object.
     *
     * @return  the String representation of this Field object
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
}
