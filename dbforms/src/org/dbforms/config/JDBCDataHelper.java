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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Clob;
import java.sql.Types;
import java.sql.SQLException;
import org.apache.log4j.Category;

import org.dbforms.util.Escaper;
import org.dbforms.util.FileHolder;

/**
 * <p>
 * this utility-class provides convenience methods for SQL related tasks
 * </p>
 * 
 * @author Joe Peer
 * @author Eric Pugh
 */
public class JDBCDataHelper {
	// logging category for this class
	private static Category logCat = Category.getInstance(JDBCDataHelper.class
			.getName());

	/**
	 * this utility-method assigns a particular value to a place holder of a
	 * PreparedStatement. it tries to find the correct setXxx() value, accoring
	 * to the field-type information represented by "fieldType". quality: this
	 * method is bloody alpha (as you might see :=)
	 * 
	 * @param ps
	 *            DOCUMENT ME!
	 * @param col
	 *            DOCUMENT ME!
	 * @param value
	 *            DOCUMENT ME!
	 * @param fieldType
	 *            DOCUMENT ME!
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	public static void fillWithData(PreparedStatement ps, Escaper escaper,
			int col, Object value, int fieldType, int blobStrategy)
			throws SQLException {
		logCat.debug("fillPreparedStatement( ps, " + col + ", " + value + ", "
				+ fieldType + ")...");
		switch (fieldType) {
		case 0:
			throw new SQLException("illegal type!");
		case FieldTypes.BLOB:
			if (value == null) {
				ps.setNull(col, java.sql.Types.BLOB);
			} else {
				//FileHolder fileHolder = (FileHolder) value;
				if (blobStrategy == Table.BLOB_CLASSIC) {
					FileHolder fileHolder = (FileHolder) value;
					try {
						ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
						ObjectOutputStream out = new ObjectOutputStream(byteOut);
						out.writeObject(fileHolder);
						out.flush();

						byte[] buf = byteOut.toByteArray();
						byteOut.close();
						out.close();

						ByteArrayInputStream bytein = new ByteArrayInputStream(
								buf);
						int byteLength = buf.length;
						ps.setBinaryStream(col, bytein, byteLength);

						// store fileHolder as a whole (this way we don't lose
						// file meta-info!)
					} catch (IOException ioe) {
						ioe.printStackTrace();
						logCat.info(ioe.toString());
						throw new SQLException(
								"error storing BLOB in database (BLOB_CLASSIC MODE) - "
										+ ioe.toString(), null, 2);
					}

				}
				// case TABLE.BLOB_INTERCEPTOR: direct storage, the rest is dont
				// by interceptor
				else {
					if (value instanceof FileHolder) { // if we have a file
													   // upload
						FileHolder fileHolder = (FileHolder) value;
						ps.setBinaryStream(col, fileHolder
								.getInputStreamFromBuffer(), fileHolder
								.getFileLength());
					} else { // if the blob field is updated from within
							 // textarea
						byte[] data = ((String) value).getBytes();
						ps.setBinaryStream(col, new ByteArrayInputStream(data),
								data.length);
					}
				}

			}
			break;
		case FieldTypes.DISKBLOB:
			ps.setObject(col, (escaper == null) ? value : escaper
					.escapeJDBC((String) value), FieldTypes.CHAR);
			break;
		case FieldTypes.CHAR:
			ps.setObject(col, (escaper == null) ? value : escaper
					.escapeJDBC((String) value), FieldTypes.CHAR);
			break;
		default:
			ps.setObject(col, value, fieldType);
			break;
		}
	}

	public static Object getData(ResultSet rs, Escaper escaper, int col)
			throws SQLException {
		Object res = null;
		switch (rs.getMetaData().getColumnType(col)) {
		case Types.CLOB: {
			String s;
			Clob tmpObj = (Clob) rs.getObject(col);
			if (tmpObj != null) {
				s = tmpObj.getSubString(1, (int) tmpObj.length());
			} else {
				s = null;
			}
			res = (escaper == null) ? s : escaper.unescapeJDBC(s);
			break;
		}
		case Types.LONGVARCHAR:
		case Types.CHAR:
		case Types.VARCHAR:
			String s = rs.getString(col);
			res = (escaper == null) ? s : escaper.unescapeJDBC(s);
			break;
		default: {
			Object tmpObj = rs.getObject(col);
			res = tmpObj;
			break;
		}
		}
		return res;
	}

}