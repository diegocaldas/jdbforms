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

package org.dbforms.servlets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.Field;
import org.dbforms.config.FieldTypes;
import org.dbforms.config.Table;

import org.dbforms.util.ParseUtil;
import org.dbforms.util.SqlUtil;
import org.dbforms.util.Util;
import org.dbforms.util.FileHolder; 

import java.io.IOException;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * #fixme - add appropriate exception-handling..
 *
 * @author joe peer
 */
public class FileServlet extends HttpServlet {
   // logging category for this class
   private static Log    logCat = LogFactory.getLog(FileServlet.class.getName());

   /**
    * Process the HTTP Get request
    *
    * @param request Description of the Parameter
    * @param response Description of the Parameter
    *
    * @exception ServletException Description of the Exception
    * @exception IOException Description of the Exception
    */
   public void doGet(HttpServletRequest  request,
                     HttpServletResponse response)
              throws ServletException, IOException {

    // take Config-Object from application context - this object should have
    // been
    // initalized by Config-Servlet on Webapp/server-startup!
    DbFormsConfig config = null;
   	try {
       config = DbFormsConfigRegistry.instance()
                                     .lookup();
    } catch (Exception e) {
       logCat.error(e);
       throw new ServletException(e);
    }

   	try {
      	String tf           = request.getParameter("tf");
         String keyValuesStr = request.getParameter("keyval");
         int    tableId      = Integer.parseInt(ParseUtil.getEmbeddedString(tf,
                                                                            0,
                                                                            '_'));
         Table table   = config.getTable(tableId);
         int   fieldId = Integer.parseInt(ParseUtil.getEmbeddedString(tf, 1, '_'));
         Field        field = table.getField(fieldId);

         StringBuffer queryBuf         = new StringBuffer();
         String       dbConnectionName = request.getParameter("invname_"
                                                              + tableId);
         Connection   con = config.getConnection(dbConnectionName);

         // JPeer 03/2004 - optional parameter
         String      nameField = request.getParameter("nf");

         InputStream is       = null;
         String      fileName = null;

         queryBuf.append("SELECT ");
         queryBuf.append(field.getName());

         if (nameField != null) {
            queryBuf.append(", ");
            queryBuf.append(nameField);
         }

         queryBuf.append(" FROM ");
         queryBuf.append(table.getName());
         queryBuf.append(" WHERE ");
         queryBuf.append(table.getWhereClauseForKeyFields());

         // example: SELECT imageNameField FROM myTable WHERE myTable.key = ?
         logCat.info("::doGet - query is [" + queryBuf + "]");

         PreparedStatement ps = con.prepareStatement(queryBuf.toString());
         table.populateWhereClauseWithKeyFields(keyValuesStr, ps, 1);

         ResultSet rs = ps.executeQuery();

         if (rs.next()) {
            // use the filesystem;
            if (field.getType() == FieldTypes.DISKBLOB) {
               fileName = rs.getString(1);
               is       = SqlUtil.readDiskBlob(fileName, field.getDirectory(),
                                               request.getParameter("defaultValue"));
            }
            // use the rdbms;
            else if (field.getType() == FieldTypes.BLOB) {
               // if no fileholder is used (new BLOB model)
               if (nameField != null) {
                  fileName = rs.getString(2);
                  is = SqlUtil.readDbFieldBlob(rs);
               } else {
                  FileHolder fh =  SqlUtil.readFileHolderBlob(rs);
                  is = fh.getInputStreamFromBuffer();
                  fileName = fh.getFileName();
               }
            }
         } else {
            logCat.info("::doGet - we have got no result using query "
                        + queryBuf);
         }

         if (is != null) {
            writeToClient(request, response, fileName, is);
         }

         SqlUtil.closeConnection(con);
      } catch (SQLException sqle) {
         logCat.error("::doGet - SQL exception", sqle);
      }
   }


   /**
    * Process the HTTP Post request
    *
    * @param request Description of the Parameter
    * @param response Description of the Parameter
    *
    * @exception ServletException Description of the Exception
    * @exception IOException Description of the Exception
    */
   public void doPost(HttpServletRequest  request,
                      HttpServletResponse response)
               throws ServletException, IOException {
      doGet(request, response);
   }



   /**
    * Write the content of the input file to the client.
    *
    * @param request DOCUMENT ME!
    * @param response Description of the Parameter
    * @param fileName Description of the Parameter
    * @param is Description of the Parameter
    *
    * @exception IOException Description of the Exception
    */
   private void writeToClient(HttpServletRequest  request,
                              HttpServletResponse response,
                              String              fileName,
                              InputStream         is) throws IOException {
      String contentType = request.getSession()
                                  .getServletContext()
                                  .getMimeType(fileName);
      logCat.info("::writeToClient- writing to client:" + fileName + " ct="
                  + contentType);

      if (!Util.isNull(contentType)) {
         response.setContentType(contentType);
      }

      response.setHeader("Cache-control", "private"); // w/o this MSIE fails

      // to "Open" the file
      response.setHeader("Content-Disposition",
                         "attachment; fileName=\"" + fileName + "\"");

      ServletOutputStream out  = response.getOutputStream();
      byte[]              b    = new byte[1024];
      int                 read;

      while ((read = is.read(b)) != -1)
         out.write(b, 0, read);

      out.close();
   }
}
