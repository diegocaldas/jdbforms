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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.dbforms.util;

import java.sql.*;
import java.io.*; // mainly used for BLOB-Operations
import org.apache.log4j.Category;

/****
 *
 * <p>this utility-class provides convenience methods for SQL related tasks</p>
 *
 * @author Joe Peer <j.peer@gmx.net>
 */

public class SqlUtil {

  static Category logCat = Category.getInstance(SqlUtil.class.getName()); // logging category for this class


  private static java.sql.Date createAppropriateDate(Object value) {
	if(value==null) return null;
	String valueStr = ((String) value).trim();

	if(valueStr.length() == 0) return null;

	Date result = java.sql.Date.valueOf(valueStr);
	return result;
  }

  private static java.math.BigDecimal createAppropriateNumeric(Object value) {

	if(value==null) return null;
	String valueStr = ((String) value).trim();

	if(valueStr.length() == 0) return null;

	return new java.math.BigDecimal(valueStr);
  }

  /**
  this utility-method assigns a particular value to a place holder of a PreparedStatement
  it tries to find the determinate the correct setXxx() value, accoring to the field-type inforamtion
  represented by "fieldType"

  quality: this method is bloody alpha (as you migth see :=)
  */
	public static void fillPreparedStatement(PreparedStatement ps, int col, Object value, int fieldType)
	throws SQLException {
		try {

			logCat.debug("fillPreparedStatement( ps, "+col+", "+value+", "+fieldType+")...");

			switch(fieldType) {
				case FieldTypes.INTEGER: ps.setInt(col, Integer.parseInt((String)value)); break;
				case FieldTypes.NUMERIC: ps.setBigDecimal(col, createAppropriateNumeric(value)); break;
				case FieldTypes.CHAR: ps.setString(col, (String)value); break;
				case FieldTypes.DATE: ps.setDate(col, createAppropriateDate(value)); break; //#checkme
				case FieldTypes.TIMESTAMP: ps.setTimestamp(col, java.sql.Timestamp.valueOf((String)value)); break;
				case FieldTypes.DOUBLE: ps.setDouble(col, Double.valueOf((String)value).doubleValue()); break;
				case FieldTypes.FLOAT: ps.setFloat(col, Float.valueOf((String)value).floatValue()); break;
				case FieldTypes.BLOB:
					FileHolder fileHolder = (FileHolder) value;

					try {
						ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	  	    	ObjectOutputStream out = new ObjectOutputStream(byteOut);
	  	    	out.writeObject(fileHolder);
	  	    	out.flush();
	  	    	byte[] buf = byteOut.toByteArray();

	  	    	byteOut.close();
	  	    	out.close();

	  	    	ByteArrayInputStream bytein = new ByteArrayInputStream(buf);
	  	    	int byteLength = buf.length;

						ps.setBinaryStream(col, bytein, byteLength); // store fileHolder as a whole (this way we don't lose file meta-info!)
					} catch(IOException ioe) {
						ioe.printStackTrace();
						logCat.info(ioe.toString());
						throw new SQLException("error storing BLOB in database - "+ioe.toString());
					}

					break;
				case FieldTypes.DISKBLOB: ps.setString(col, (String)value); break;

				default: ps.setObject(col, value); //#checkme
			}

		} catch(Exception e) {
			throw new SQLException("Field type seems to be incorrect - "+e.toString());
		}
  }
}