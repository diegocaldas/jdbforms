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
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.FileNameMap;
import java.net.URLConnection;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.Field;
import org.dbforms.config.Table;
import org.dbforms.util.FieldTypes;
import org.dbforms.util.FileHolder;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.SqlUtil;
import org.apache.log4j.Category;



/**
 *#fixme - add appropriate exception-handling..
 *
 * @author joe peer
 *
 */
public class FileServlet extends HttpServlet
{
   // logging category for this class
   private static Category logCat = Category.getInstance(
                                             FileServlet.class.getName());
   private DbFormsConfig   config;
   private FileNameMap     fileNameMap;

   /**
    * Initialize this servlet.
    *
    * @exception  ServletException Description of the Exception
    */
   public void init() throws ServletException
   {
      // take Config-Object from application context - this object should have been
      // initalized by Config-Servlet on Webapp/server-startup!
      config      = (DbFormsConfig) getServletContext()
                                       .getAttribute(DbFormsConfig.CONFIG);
      fileNameMap = URLConnection.getFileNameMap();
   }


   /**
    *  Process the HTTP Post request
    *
    * @param  request Description of the Parameter
    * @param  response Description of the Parameter
    * @exception  ServletException Description of the Exception
    * @exception  IOException Description of the Exception
    */
   public void doPost(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException
   {
      doGet(request, response);
   }


   /**
    *  Process the HTTP Get request
    *
    * @param  request Description of the Parameter
    * @param  response Description of the Parameter
    * @exception  ServletException Description of the Exception
    * @exception  IOException Description of the Exception
    */
   public void doGet(HttpServletRequest request, HttpServletResponse response)
              throws ServletException, IOException
   {
      String       tf           = request.getParameter("tf");
      String       keyValuesStr = request.getParameter("keyval");
      int          tableId      = Integer.parseInt(ParseUtil.getEmbeddedString(
                                                            tf, 0, '_'));
      Table        table   = config.getTable(tableId);
      int          fieldId = Integer.parseInt(ParseUtil.getEmbeddedString(tf, 1, 
                                                                          '_'));
      Field        field = table.getField(fieldId);

      StringBuffer queryBuf         = new StringBuffer();
      String       dbConnectionName = request.getParameter("invname_" + tableId);
      Connection   con              = SqlUtil.getConnection(config, 
                                                            dbConnectionName);

      queryBuf.append("SELECT ");
      queryBuf.append(field.getName());
      queryBuf.append(" FROM ");
      queryBuf.append(table.getName());
      queryBuf.append(" WHERE ");
      queryBuf.append(table.getWhereClauseForPS());


      // example: SELECT imageNameField FROM myTable WHERE myTable.key = ?
      logCat.info("::doGet - query is [" + queryBuf + "]");

      try
      {
         PreparedStatement ps = con.prepareStatement(queryBuf.toString());
         table.populateWhereClauseForPS(keyValuesStr, ps, 1);

         ResultSet rs = ps.executeQuery();

         if (rs.next())
         {
            // use the filesystem;
            if (field.getType() == FieldTypes.DISKBLOB)
            {
               readDiskBlob(rs.getString(1), field.getDirectory(), request, 
                            response);
            }

            // use the rdbms;
            else if (field.getType() == FieldTypes.BLOB)
            {
               readDbFieldBlob(rs, response);
            }
         }
         else
         {
            logCat.info("::doGet - we have got no result using query "
                        + queryBuf);
         }
      }
      catch (SQLException sqle)
      {
         logCat.error("::doGet - SQL exception", sqle);
      }

      // The connection should not be null - If it is, then you might have
      // an infrastructure problem! Be sure to look into this!
      // Hint: check out your pool manager's performance!
      finally
      {
         SqlUtil.closeConnection(con);
      }
   }


   /**
    *  Read the database field and write to the client its content
    *
    * @param  rs Description of the Parameter
    * @param  request  Description of the Parameter
    * @param  response Description of the Parameter
    * @exception  IOException Description of the Exception
    * @exception  SQLException Description of the Exception
    */
   private void readDbFieldBlob(ResultSet rs, HttpServletResponse response)
                         throws IOException, SQLException
   {
      logCat.info("READING BLOB");

      try
      {
         Object o = rs.getObject(1);

         // if the object the JDBC driver returns to us implements
         // the java.sql.Blob interface, then we use the BLOB object
         // which wraps the binary stream of our FileHolder:
         if (o != null)
         {
            if (o instanceof java.sql.Blob)
            {
               Blob              blob = rs.getBlob(1);
               ObjectInputStream ois = new ObjectInputStream(
                                                blob.getBinaryStream());
               FileHolder        fh = (FileHolder) ois.readObject();
               writeToClient(response, fh.getFileName(), 
                             fh.getInputStreamFromBuffer());
            }

            /*
              else if(o instanceof java.sql.Clob)
              {
                Clob clob = rs.getClob(1);
                ObjectInputStream ois = new ObjectInputStream(clob.getAsciiStream());
                FileHolder fh = (FileHolder) ois.readObject();
                writeToClient(response, fh.getFileName(), fh.getInputStreamFromBuffer());
              }
            */

            // otherwise we are aquiring the stream directly:
            else
            {
               InputStream       blobIS = rs.getBinaryStream(1);
               ObjectInputStream ois = new ObjectInputStream(blobIS);
               FileHolder        fh  = (FileHolder) ois.readObject();
               writeToClient(response, fh.getFileName(), 
                             fh.getInputStreamFromBuffer());
            }
         }
         else
         {
            logCat.warn("::readDbFieldBlob - blob null, no response sent");
         }
      }
      catch (ClassNotFoundException cnfe)
      {
         logCat.error("::readDbFieldBlob - class not found", cnfe);
         throw new IOException("error:" + cnfe.toString());
      }
   }


   /**
    *  Read the blob field from the filesystem and write to the client its content.
    *
    * @param  fileName Description of the Parameter
    * @param  directory Description of the Parameter
    * @param  request  Description of the Parameter
    * @param  response Description of the Parameter
    * @exception  FileNotFoundException Description of the Exception
    * @exception  IOException Description of the Exception
    */
   private void readDiskBlob(String fileName, String directory, 
                             HttpServletRequest request, 
                             HttpServletResponse response)
                      throws FileNotFoundException, IOException
   {
      logCat.info(new StringBuffer("READING DISKBLOB\n  directory = [").append(
                           directory).append("]\n").append("  fileName = [")
                                                                       .append(fileName)
                                                                       .append("]\n")
                                                                       .append("  defaultValue = [")
                                                                       .append(request.getParameter(
                                                                                        "defaultValue"))
                                                                       .append("]\n")
                                                                       .toString());

      if ((fileName == null) || (fileName.trim().length() == 0))
      {
         if ((fileName = request.getParameter("defaultValue")) != null)
         {
            logCat.info(
                     "::readDiskBlob - database data is null; use the default value ["
                     + fileName + "]");
         }
      }

      // directory or fileName can be null!
      //if ((directory != null) && (fileName != null))
      if (fileName != null)
      {
         fileName = fileName.trim();

         File file = new File(directory, fileName);

         if (file.exists())
         {
            logCat.info("::readDiskBlob - file found ["
                        + file.getAbsoluteFile() + "]");

            FileInputStream fis = new FileInputStream(file);
            writeToClient(response, fileName, fis);
         }
         else
         {
            logCat.error("::readDiskBlob - file ["
                         + (directory + "/" + fileName) + "] not found");
         }
      }
      else
      {
         logCat.warn("::readDiskBlob - file name or directory value is null");
      }
   }


   /**
    *  Write the content of the input file to the client.
    *
    * @param  response Description of the Parameter
    * @param  fileName Description of the Parameter
    * @param  is Description of the Parameter
    * @exception  IOException Description of the Exception
    */
   private void writeToClient(HttpServletResponse response, String fileName, 
                              InputStream is) throws IOException
   {
      String contentType = fileNameMap.getContentTypeFor(fileName);
      logCat.info("::writeToClient- writing to client:" + fileName + " ct="
                  + contentType);
      response.setContentType(contentType);

      ServletOutputStream out  = response.getOutputStream();
      byte[]              b    = new byte[1024];
      int                 read;

      while ((read = is.read(b)) != -1)
         out.write(b, 0, read);

      out.close();
   }
}