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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.FieldTypes;
import org.dbforms.config.Table;

import org.dbforms.util.FileHolder;
import org.dbforms.util.SqlUtil;

import java.io.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.jsp.*;

/**
 * #fixme docu to come
 * 
 * @author Joe Peer
 */
public class DbBlobContentTag extends AbstractDbBaseHandlerTag implements
        javax.servlet.jsp.tagext.TryCatchFinally {
    private static Log logCat = LogFactory.getLog(DbBlobContentTag.class);

    private String dbConnectionName;

    /**
     * DOCUMENT ME!
     * 
     * @param string
     */
    public void setDbConnectionName(String string) {
        dbConnectionName = string;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return
     */
    public String getDbConnectionName() {
        return dbConnectionName;
    }


    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws javax.servlet.jsp.JspException
     *             DOCUMENT ME!
     * @throws IllegalArgumentException
     *             DOCUMENT ME!
     * @throws JspException
     *             DOCUMENT ME!
     */
    public int doEndTag() throws javax.servlet.jsp.JspException {
        try {
            if (getParentForm().isFooterReached()) {
                return EVAL_PAGE; // nothing to do when no data available..
            }

            String keyVal = getKeyVal();
            StringBuffer queryBuf = new StringBuffer();
            queryBuf.append("SELECT ");
            queryBuf.append(getField().getName());
            queryBuf.append(" FROM ");
            queryBuf.append(getParentForm().getTable().getQueryFrom());
            queryBuf.append(" WHERE ");
            queryBuf.append(getParentForm().getTable()
                    .getWhereClauseForKeyFields(keyVal));
            logCat.info("blobcontent query- " + queryBuf.toString());

            StringBuffer contentBuf = new StringBuffer();

            try {
                Connection con = getConfig().getConnection(dbConnectionName);
                PreparedStatement ps = con
                        .prepareStatement(queryBuf.toString());
                getParentForm().getTable().populateWhereClauseWithKeyFields(
                        keyVal, ps, 1);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    InputStream is = null;
                    String fileName = null;

                    if (getField().getType() == FieldTypes.DISKBLOB) {
                        fileName = rs.getString(1);
                        is = SqlUtil.readDiskBlob(fileName, getField()
                                .getDirectory(), null);
                    } else if (getField().getTable().getBlobHandlingStrategy() == Table.BLOB_CLASSIC) {
                        FileHolder fh = SqlUtil.readFileHolderBlob(rs);
                        is = fh.getInputStreamFromBuffer();
                    } else {
                        is = SqlUtil.readDbFieldBlob(rs);
                    }
                    if (is != null) {
                        try {
                            BufferedReader br = new BufferedReader(
                                    new InputStreamReader(is));
                            char[] c = new char[1024];
                            int read;

                            while ((read = br.read(c)) != -1) {
                                contentBuf.append(c, 0, read);
                            }
                        } finally {
                            is.close();
                        }
                    }
                } else {
                    logCat.info("fs- we have got no result" + queryBuf);
                }

                SqlUtil.closeConnection(con);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }

            pageContext.getOut().write(escapeHTML(contentBuf.toString()));
        } catch (java.io.IOException ioe) {
            throw new JspException("IO Error: " + ioe.getMessage());
        }

        return EVAL_PAGE;
    }

    /**
     * DOCUMENT ME!
     */
    public void doFinally() {
        dbConnectionName = null;
        super.doFinally();
    }

    // ------------------------------------------------------ Protected Methods
    // DbForms specific

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    private String getKeyVal() {
        return getParentForm().getTable().getKeyPositionString(
                getParentForm().getResultSetVector());
    }
}
