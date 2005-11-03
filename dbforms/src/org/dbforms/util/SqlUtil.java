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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dbforms.util.external.FileUtil;
/**
 * <p>
 * this utility-class provides convenience methods for SQL related tasks
 * </p>
 * 
 * @author Joe Peer
 * @author Eric Pugh
 */
public class SqlUtil {
    // logging category for this class
    private static Log logCat = LogFactory.getLog(SqlUtil.class.getName());

    /**
     * Close the input connection
     * 
     * @param con
     *            the connection to close
     */
    public static final void closeConnection(Connection con) {
        if (con != null) {
            try {
                SqlUtil.logCat.debug("About to close connection - " + con);
                con.close();
                SqlUtil.logCat.debug("Connection closed");
            } catch (SQLException e) {
                SqlUtil
                        .logSqlException(e,
                                "::closeConnection - cannot close the input connection");
            }
        }
    }

    /**
     * Log the SQLException stacktrace (adding the input description to the
     * first log statement) and do the same for all the nested exceptions.
     * 
     * @param e
     *            the SQL exception to log
     * @param desc
     *            the exception description
     */
    public static final void logSqlException(SQLException e, String desc) {
        int i = 0;
        String excDesc = "::logSqlExceptionSQL - main SQL exception";

        // adding the input description to the main log statement;
        if (!Util.isNull(desc)) {
            excDesc += (" [" + desc + "]");
        }

        SqlUtil.logCat.error(excDesc, e);

        while ((e = e.getNextException()) != null)
            SqlUtil.logCat.error("::logSqlException - nested Exception ("
                    + (i++) + ")", e);
    }

    /**
     * Log the SQLException stacktrace and do the same for all the nested
     * exceptions.
     * 
     * @param e
     *            the SQL exception to log
     */
    public static final void logSqlException(SQLException e) {
        SqlUtil.logSqlException(e, null);
    }

    /**
     * Read the database field and write to the client its content
     * 
     * @param rs
     *            Description of the Parameter
     * @param fileName
     *            is the filename or NULL in the classic (Fileholder-based) BLOB
     *            handling
     * @exception IOException
     *                Description of the Exception
     * @exception SQLException
     *                Description of the Exception
     */
    public static FileHolder readFileHolderBlob(ResultSet rs)
            throws IOException, SQLException {
        logCat.info("READING FILEHOLDER");
        Object o = rs.getObject(1);
        if (o == null) {
            logCat.warn("::readDbFieldBlob - blob null, no response sent");
            return null;
        }
        logCat.info("o instanceof ..." + o.getClass().getName());
        // if the object the JDBC driver returns to us implements
        // the java.sql.Blob interface, then we use the BLOB object
        // which wraps the binary stream of our FileHolder:
        try {
            if (o instanceof java.sql.Blob) {
                Blob blob = rs.getBlob(1);
                ObjectInputStream ois = new ObjectInputStream(blob
                        .getBinaryStream());
                FileHolder fh = (FileHolder) ois.readObject();
                return fh;
            }
            // otherwise we are aquiring the stream directly:
            else {
                // old ("classic") mode
                InputStream blobIS = rs.getBinaryStream(1);
                ObjectInputStream ois = new ObjectInputStream(blobIS);
                FileHolder fh = (FileHolder) ois.readObject();
                return fh;
            }
        } catch (ClassNotFoundException cnfe) {
            logCat.error("::readDbFieldBlob - class not found", cnfe);
            throw new IOException("error:" + cnfe.toString());
        }

    }

    /**
     * Read the database field and write to the client its content
     * 
     * @param rs
     *            Description of the Parameter
     * @param fileName
     *            is the filename or NULL in the classic (Fileholder-based) BLOB
     *            handling
     * @exception IOException
     *                Description of the Exception
     * @exception SQLException
     *                Description of the Exception
     */
    public static InputStream readDbFieldBlob(ResultSet rs) throws IOException,
            SQLException {
        logCat.info("READING BLOB");
        Object o = rs.getObject(1);
        if (o == null) {
            logCat.warn("::readDbFieldBlob - blob null, no response sent");
            return null;
        }
        logCat.info("o instanceof ..." + o.getClass().getName());
        // if the object the JDBC driver returns to us implements
        // the java.sql.Blob interface, then we use the BLOB object
        // which wraps the binary stream of our FileHolder:
        if (o instanceof java.sql.Blob) {
            return ((Blob) o).getBinaryStream();
        }
        // otherwise we are aquiring the stream directly:
        else {
            // new mode
            return rs.getBinaryStream(1);
        }
    }

    /**
     * Read the blob field from the filesystem and write to the client its
     * content.
     * 
     * @param fileName
     *            Description of the Parameter
     * @param directory
     *            Description of the Parameter
     * @param defVal
     *            Default value of the file tag
     * @exception FileNotFoundException
     *                Description of the Exception
     * @exception IOException
     *                Description of the Exception
     */
    public static FileInputStream readDiskBlob(String fileName,
            String directory, String defVal) throws FileNotFoundException,
            IOException {
        logCat.info(new StringBuffer("READING DISKBLOB\n  directory = [")
                .append(directory).append("]\n").append("  fileName = [")
                .append(fileName).append("]\n").append("  defaultValue = [")
                .append(defVal).append("]\n").toString());

        if (Util.isNull(fileName)) {
            if ((fileName = defVal) != null) {
                logCat
                        .info("::readDiskBlob - database data is null; use the default value ["
                                + fileName + "]");
               	fileName = fileName.replace('/', File.separatorChar);
               	fileName = fileName.replace('\\', File.separatorChar);
                String s = FileUtil.dirname(fileName);
                if (!Util.isNull(s)) {
                	directory = s;
                	fileName = FileUtil.removePath(fileName);
                }
            }
        }

        // directory or fileName can be null!
        //if ((directory != null) && (fileName != null))
        if (fileName != null) {
            fileName = fileName.trim();
            File file = new File(directory, fileName);
            if (file.exists()) {
                logCat.info("::readDiskBlob - file found ["
                        + file.getAbsoluteFile() + "]");
                return new FileInputStream(file);
            } else {
                logCat.error("::readDiskBlob - file ["
                        + (directory + "/" + fileName) + "] not found");

                return null;
            }
        } else {
            logCat
                    .warn("::readDiskBlob - file name or directory value is null");

            return null;
        }
    }
}
