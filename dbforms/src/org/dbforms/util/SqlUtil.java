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

package org.dbforms.util;

import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.Calendar;
import org.apache.log4j.Category;
import org.dbforms.DbFormsConfig;



/****
 *
 * <p>this utility-class provides convenience methods for SQL related tasks</p>
 *
 * @author Joe Peer <j.peer@gmx.net>
 */
public class SqlUtil
{
    // logging category for this class
    static Category logCat = Category.getInstance(SqlUtil.class.getName());

    /**
     *
     */
    private static java.sql.Date createAppropriateDate(Object value)
    {
        if (value == null)
        {
            return null;
        }

        String valueStr = ((String) value).trim();

        if (valueStr.length() == 0)
        {
            return null;
        }

        SimpleDateFormat sdf = DbFormsConfig.getDateFormatter();
        Date result = null;

        try
        {
            result = new java.sql.Date(sdf.parse(valueStr).getTime());
        }
        catch (Exception exc)
        {
            result = null;
        }

        if (result == null)
        {
            // Maybe date has been returned as a timestamp?
            try
            {
            }
            catch (java.lang.IllegalArgumentException ex)
            {
                // Try date
                result = java.sql.Date.valueOf(valueStr);
            }
        }

        return result;
    }


    /**
     * 2002/10/01-HKK: Do the same for timestamp!
     */
    private static java.sql.Timestamp createAppropriateTimeStamp(Object value)
    {
        if (value == null)
        {
            return null;
        }

        String valueStr = ((String) value).trim();

        if (valueStr.length() == 0)
        {
            return null;
        }

        SimpleDateFormat sdf = DbFormsConfig.getDateFormatter();
        Timestamp result = null;
        try
        {
           result = new Timestamp(TimeUtil.parseDate(sdf.toPattern(), valueStr).getTime());
        }
        catch (Exception exc)
        {
            result = null;
        }

        if (result == null)
        {
            // Maybe date has been returned as a timestamp?
            result = java.sql.Timestamp.valueOf(valueStr);
        }

        return result;
    }


    /**
     *
     */
    private static java.math.BigDecimal createAppropriateNumeric(Object value)
    {
        if (value == null)
        {
            return null;
        }

        String valueStr = ((String) value).trim();

        if (valueStr.length() == 0)
        {
            return null;
        }

        return new java.math.BigDecimal(valueStr);
    }


    /**
     *  this utility-method assigns a particular value to a place holder of a PreparedStatement.
     *  it tries to find the correct setXxx() value, accoring to the field-type information
     *  represented by "fieldType".
     *
     *  quality: this method is bloody alpha (as you migth see :=)
     */
    public static void fillPreparedStatement(PreparedStatement ps, int col, Object val, int fieldType) throws SQLException
    {
        try
        {
            logCat.debug("fillPreparedStatement( ps, " + col + ", " + val + ", " + fieldType + ")...");

            Object value = null;

            //Check for hard-coded NULL
            if (!("$null$".equals(val)))
            {
                value = val;
            }

                // 20021128-hKK:Check for empty string!
                if ( (fieldType != FieldTypes.BLOB) && Util.isNull((String) value))
                    value = null;

            if (value != null)
            {
                switch (fieldType)
                {
                    case FieldTypes.INTEGER:
                        ps.setInt(col, Integer.parseInt((String) value));
                        break;

                    case FieldTypes.NUMERIC:
                        ps.setBigDecimal(col, createAppropriateNumeric(value));
                        break;

                    case FieldTypes.CHAR:
                        ps.setString(col, (String) value);
                        break;

                    case FieldTypes.DATE:
                        ps.setDate(col, createAppropriateDate(value));
                        break;

                    //2002/10/01-HKK: Do the same for timestamp!
                    case FieldTypes.TIMESTAMP:
                        ps.setTimestamp(col, createAppropriateTimeStamp(value));
                        break;

                    case FieldTypes.DOUBLE:
                        ps.setDouble(col, Double.valueOf((String) value).doubleValue());
                        break;

                    case FieldTypes.FLOAT:
                        ps.setFloat(col, Float.valueOf((String) value).floatValue());
                        break;

                    case FieldTypes.BLOB:
                        FileHolder fileHolder = (FileHolder) value;
                        try
                        {
                            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                            ObjectOutputStream out = new ObjectOutputStream(byteOut);
                            out.writeObject(fileHolder);
                            out.flush();

                            byte[] buf = byteOut.toByteArray();
                            byteOut.close();
                            out.close();

                            ByteArrayInputStream bytein = new ByteArrayInputStream(buf);
                            int byteLength = buf.length;
                            ps.setBinaryStream(col, bytein, byteLength);

                            // store fileHolder as a whole (this way we don't lose file meta-info!)
                        }
                        catch (IOException ioe)
                        {
                            ioe.printStackTrace();
                            logCat.info(ioe.toString());
                            throw new SQLException("error storing BLOB in database - " + ioe.toString(), null, 2);
                        }

                        break;

                    case FieldTypes.DISKBLOB:
                        ps.setString(col, (String) value);
                        break;

                    default:
                        ps.setObject(col, value); //#checkme
                }
            }
            else
            {
                switch (fieldType)
                {
                    case FieldTypes.INTEGER:
                        ps.setNull(col, java.sql.Types.INTEGER);

                        break;

                    case FieldTypes.NUMERIC:
                        ps.setNull(col, java.sql.Types.NUMERIC);

                        break;

                    case FieldTypes.CHAR:
                        ps.setNull(col, java.sql.Types.CHAR);

                        break;

                    case FieldTypes.DATE:
                        ps.setNull(col, java.sql.Types.DATE);

                        break;

                    case FieldTypes.TIMESTAMP:
                        ps.setNull(col, java.sql.Types.TIMESTAMP);

                        break;

                    case FieldTypes.DOUBLE:
                        ps.setNull(col, java.sql.Types.DOUBLE);

                        break;

                    case FieldTypes.FLOAT:
                        ps.setNull(col, java.sql.Types.FLOAT);

                        break;

                    case FieldTypes.BLOB:
                        ps.setNull(col, java.sql.Types.BLOB);

                    case FieldTypes.DISKBLOB:
                        ps.setNull(col, java.sql.Types.CHAR);

                    default:
                        ps.setNull(col, java.sql.Types.OTHER);
                }
            }
        }
        catch (Exception e)
        {
            throw new SQLException("Field type seems to be incorrect - " + e.toString(), null, 1);
        }
    }


    /**
     * Close the input connection
     *
     * @param con the connection to close
     */
    public final static void closeConnection(Connection con)
    {
        if (con != null)
        {
            try
            {
                logCat.debug("About to close connection - " + con);
                con.close();
                logCat.debug("Connection closed");
            }
            catch (SQLException e)
            {
                logCat.error("::closeConnection - cannot close the input connection", e);
            }
        }
    }


    /**
     *  Get a connection using the connection name
     *  specified into the xml configuration file.
     *
     * @param config            the DbFormsConfig object
     * @param dbConnectionName  the name of the DbConnection element
     * @return a JDBC connection object
     * @throws IllegalArgumentException if any error occurs
     */
    public static final Connection getConnection(DbFormsConfig config, String dbConnectionName)
      throws IllegalArgumentException
    {
        DbConnection dbConnection = null;
        Connection   con          = null;

        //  get the DbConnection object having the input name;
        if ((dbConnection = config.getDbConnection(dbConnectionName)) == null)
          throw new IllegalArgumentException("No DbConnection object configured with name '"
                                             + dbConnectionName + "'");

        // now try to get the JDBC connection from the retrieved DbConnection object;
        if ((con = dbConnection.getConnection()) == null)
          throw new IllegalArgumentException("JDBC-Troubles:  was not able to create connection from "
                                              + dbConnection);

        return con;
    }


    /**
     *  Log the SQLException stacktrace and do the same for all the
     *  nested exceptions.
     */
    public static final void logSqlException(SQLException e)
    {
        int i = 0;

        logCat.error("::logSqlExceptionSQL - exception", e);

        while ((e = e.getNextException()) != null)
            logCat.error("::logSqlException - nested SQLException (" + (i++) + ")", e);
    }


	/**
	 *  save parsing of a string to date value.
	 * 
	 *  will set missing fields to default values:
	 *      missing year becomes current year
	 *      missing month becomes current month
	 *      missing day becomes current day
	 *      missing time becomes 00:00:00 
	 *
	 * @param sdf  DateFormat to use
	 *         s    String to parse
	 * @return the Date
	 */
	public final static java.util.Date saveStringToDate(SimpleDateFormat sdf, String s) {
		java.util.Date d = null;
		try {
			d = sdf.parse(s);
		} catch (ParseException e) {
			Calendar cal = sdf.getCalendar();
			Calendar now = Calendar.getInstance();
			if (!cal.isSet(Calendar.DAY_OF_MONTH))
				cal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
			if (!cal.isSet(Calendar.MONTH))
			   cal.set(Calendar.MONTH, now.get(Calendar.MONTH));
			if (!cal.isSet(Calendar.YEAR))
			   cal.set(Calendar.YEAR, now.get(Calendar.YEAR));
			if (!cal.isSet(Calendar.HOUR))
				cal.set(Calendar.HOUR, 0);
			if (!cal.isSet(Calendar.MINUTE))
				cal.set(Calendar.MINUTE, 0);
			if (!cal.isSet(Calendar.SECOND))
				cal.set(Calendar.SECOND, 0);
			d = cal.getTime();
		}
		return d;
	}
}
