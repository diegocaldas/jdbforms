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
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.apache.log4j.Category;

import org.dbforms.util.FileHolder;
import org.dbforms.util.TimeUtil;
import org.dbforms.util.Util;

/**
 * <p>
 * this utility-class provides convenience methods for SQL related tasks
 * </p>
 * 
 * @author Joe Peer
 * @author Eric Pugh
 */
public class SqlUtil
{
    // logging category for this class
    private static Category logCat =
        Category.getInstance(SqlUtil.class.getName());


    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static java.sql.Date createAppropriateDate(Object value)
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

        Date result = null;

        try
        {
            SimpleDateFormat sdf =
                DbFormsConfigRegistry.instance().lookup().getDateFormatter();
            result =
                new Date(
                    TimeUtil.parseDate(sdf.toPattern(), valueStr).getTime());
        }
        catch (Exception exc)
        {
            result = null;
        }

        // if date is key, then have the standard format yyyy-MM-dd
        if (result == null)
        {
            try
            {
                result = java.sql.Date.valueOf(valueStr);
            }
            catch (Exception exc)
            {
                result = null;
            }
        }

        if (result == null)
        {
            try
            {
                // Maybe date has been returned as a timestamp?
                result = new Date(Timestamp.valueOf(valueStr).getTime());
            }
            catch (Exception exc)
            {
                result = null;
            }
        }

        //String s = result.toString();

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static java.sql.Timestamp createAppropriateTimeStamp(Object value)
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

        Timestamp result = null;

        try
        {
            SimpleDateFormat sdf =
                DbFormsConfigRegistry.instance().lookup().getDateFormatter();
            result =
                new Timestamp(
                    TimeUtil.parseDate(sdf.toPattern(), valueStr).getTime());
        }
        catch (Exception exc)
        {
            result = null;
        }

		// if date is key, then have the standard format yyyy-MM-dd
		if (result == null)
		{
			 try
			 {
				  result = java.sql.Timestamp.valueOf(valueStr);
			 }
			 catch (Exception exc)
			 {
				  result = null;
			 }
		}

        if (result == null)
        {
            try
            {
                // Maybe date has been returned as a timestamp?
                result = Timestamp.valueOf(valueStr);
            }
            catch (Exception exc)
            {
                result = null;
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static java.math.BigDecimal createAppropriateNumeric(Object value)
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
     * this utility-method assigns a particular value to a place holder of a
     * PreparedStatement. it tries to find the correct setXxx() value, accoring
     * to the field-type information represented by "fieldType". quality: this
     * method is bloody alpha (as you might see :=)
     * @param ps DOCUMENT ME!
     * @param col DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param fieldType DOCUMENT ME!
     * @throws SQLException DOCUMENT ME!
     */
    public static void fillPreparedStatement(
        PreparedStatement ps,
        int col,
        Object value,
        int fieldType)
        throws SQLException
    {
        try
        {
            logCat.debug(
                "fillPreparedStatement( ps, "
                    + col
                    + ", "
                    + value
                    + ", "
                    + fieldType
                    + ")...");

            //Check for hard-coded NULL
            if ("$null$".equals(value))
            {
                value = null;
            }

            // the challenge with this is that sometimes we want a "" string to
            // actually not be null, but to be "".  This fails!
            if ((fieldType != FieldTypes.BLOB) && Util.isNull((String) value))
            {
                value = null;
            }

            if (value != null)
            {
                switch (fieldType)
                {
                    case FieldTypes.INTEGER :
                        ps.setInt(col, Integer.parseInt((String) value));

                        break;

                    case FieldTypes.NUMERIC :
                        ps.setBigDecimal(col, createAppropriateNumeric(value));

                        break;

                    case FieldTypes.CHAR :
                        ps.setString(col, (String) value);

                        break;

                    case FieldTypes.DATE :
                        ps.setDate(col, createAppropriateDate(value));

                        break;

                        //2002/10/01-HKK: Do the same for timestamp!
                    case FieldTypes.TIMESTAMP :
                        ps.setTimestamp(col, createAppropriateTimeStamp(value));

                        break;

                    case FieldTypes.DOUBLE :
                        ps.setDouble(
                            col,
                            Double.valueOf((String) value).doubleValue());

                        break;

                    case FieldTypes.FLOAT :
                        ps.setFloat(
                            col,
                            Float.valueOf((String) value).floatValue());

                        break;

                    case FieldTypes.BLOB :

                        FileHolder fileHolder = (FileHolder) value;

                        try
                        {
                            ByteArrayOutputStream byteOut =
                                new ByteArrayOutputStream();
                            ObjectOutputStream out =
                                new ObjectOutputStream(byteOut);
                            out.writeObject(fileHolder);
                            out.flush();

                            byte[] buf = byteOut.toByteArray();
                            byteOut.close();
                            out.close();

                            ByteArrayInputStream bytein =
                                new ByteArrayInputStream(buf);
                            int byteLength = buf.length;
                            ps.setBinaryStream(col, bytein, byteLength);

                            // store fileHolder as a whole (this way we don't lose file meta-info!)
                        }
                        catch (IOException ioe)
                        {
                            ioe.printStackTrace();
                            logCat.info(ioe.toString());
                            throw new SQLException(
                                "error storing BLOB in database - "
                                    + ioe.toString(),
                                null,
                                2);
                        }

                        break;

                    case FieldTypes.DISKBLOB :
                        ps.setString(col, (String) value);

                        break;

                    default :
                        ps.setObject(col, value); //#checkme
                }
            }
            else
            {
                switch (fieldType)
                {
                    case FieldTypes.INTEGER :
                        ps.setNull(col, java.sql.Types.INTEGER);

                        break;

                    case FieldTypes.NUMERIC :
                        ps.setNull(col, java.sql.Types.NUMERIC);

                        break;

                    case FieldTypes.CHAR :
                        ps.setNull(col, java.sql.Types.CHAR);

                        break;

                    case FieldTypes.DATE :
                        ps.setNull(col, java.sql.Types.DATE);

                        break;

                    case FieldTypes.TIMESTAMP :
                        ps.setNull(col, java.sql.Types.TIMESTAMP);

                        break;

                    case FieldTypes.DOUBLE :
                        ps.setNull(col, java.sql.Types.DOUBLE);

                        break;

                    case FieldTypes.FLOAT :
                        ps.setNull(col, java.sql.Types.FLOAT);

                        break;

                    case FieldTypes.BLOB :
                        ps.setNull(col, java.sql.Types.BLOB);

                    case FieldTypes.DISKBLOB :
                        ps.setNull(col, java.sql.Types.CHAR);

                    default :
                        ps.setObject(col, value); //#checkme

                        //ps.setNull(col, java.sql.Types.OTHER);
                }
            }
        }
        catch (Exception e)
        {
            StringBuffer msgSB =
                new StringBuffer(
                    "Field type seems to be incorrect - " + e.toString());

            if (fieldType == 0)
            {
                msgSB.append(
                    " Double check your dbforms-config.xml, as the field type was not populated.");
            }

            throw new SQLException(msgSB.toString(), null, 1);
        }
    }

    /**
     * Close the input connection
     * 
     * @param con the connection to close
     */
    public static final void closeConnection(Connection con)
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
                logCat.error(
                    "::closeConnection - cannot close the input connection",
                    e);
            }
        }
    }

    /**
     * Get a connection using the connection name specified into the xml
     * configuration file.
     * 
     * @param config            the DbFormsConfig object
     * @param dbConnectionName  the name of the DbConnection element
     * 
     * @return a JDBC connection object
     * 
     * @throws IllegalArgumentException if any error occurs
     */
    public static final Connection getConnection(
        DbFormsConfig config,
        String dbConnectionName)
        throws IllegalArgumentException
    {
        DbConnection dbConnection = null;
        Connection con = null;

        //  get the DbConnection object having the input name;
        if ((dbConnection = config.getDbConnection(dbConnectionName)) == null)
        {
            throw new IllegalArgumentException(
                "No DbConnection object configured with name '"
                    + dbConnectionName
                    + "'");
        }

        // now try to get the JDBC connection from the retrieved DbConnection object;
        if ((con = dbConnection.getConnection()) == null)
        {
            throw new IllegalArgumentException(
                "JDBC-Troubles:  was not able to create connection from "
                    + dbConnection);
        }

        return con;
    }

    /**
     * Log the SQLException stacktrace and do the same for all the nested
     * exceptions.
     * 
     * @param e  the SQL exception to log
     */
    public static final void logSqlException(SQLException e)
    {
        logSqlException(e, null);
    }

    /**
     * Log the SQLException stacktrace (adding the input description to  the
     * first log statement) and do the same for all the nested exceptions.
     * 
     * @param e    the SQL exception to log
     * @param desc the exception description
     */
    public static final void logSqlException(SQLException e, String desc)
    {
        int i = 0;
        String excDesc = "::logSqlExceptionSQL - main SQL exception";

        // adding the input description to the main log statement;
        if (!Util.isNull(desc))
        {
            excDesc += (" [" + desc + "]");
        }

        logCat.error(excDesc, e);

        while ((e = e.getNextException()) != null)
            logCat.error(
                "::logSqlException - nested SQLException (" + (i++) + ")",
                e);
    }
}