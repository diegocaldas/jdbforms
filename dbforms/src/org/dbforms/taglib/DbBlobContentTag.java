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
import java.io.*;
import java.sql.*;
import javax.servlet.jsp.*;

import java.sql.Connection;

import org.dbforms.config.FieldTypes;
import org.dbforms.util.SqlUtil;
import org.apache.log4j.Category;



/**
 * #fixme docu to come
 *
 * @author Joe Peer
 */
public class DbBlobContentTag extends DbBaseHandlerTag
{
   private Category logCat = Category.getInstance(this.getClass().getName());
   private String dbConnectionName;
   
   

   /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   *
   * @throws javax.servlet.jsp.JspException DOCUMENT ME!
   * @throws IllegalArgumentException DOCUMENT ME!
   * @throws JspException DOCUMENT ME!
   */
   public int doEndTag() throws javax.servlet.jsp.JspException
   {
      try
      {
         if (getParentForm().getFooterReached())
         {
            return EVAL_PAGE; // nothing to do when no data available..
         }

         StringBuffer queryBuf = new StringBuffer();
         queryBuf.append("SELECT ");
         queryBuf.append(getField().getName());
         queryBuf.append(" FROM ");
         queryBuf.append(getParentForm().getTable().getName());
         queryBuf.append(" WHERE ");
         queryBuf.append(getParentForm().getTable().getWhereClauseForPS());
         logCat.info("blobcontent query- " + queryBuf.toString());

         StringBuffer contentBuf = new StringBuffer();
         Connection   con = getConfig().getConnection(dbConnectionName);

         try
         {
            PreparedStatement ps = con.prepareStatement(queryBuf.toString());
            getParentForm().getTable().populateWhereClauseWithKeyFields(getKeyVal(), ps, 1);

            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
               if (getField().getType() == FieldTypes.DISKBLOB)
               {
                  String fileName = rs.getString(1);

                  if (fileName != null)
                  {
                     fileName = fileName.trim();
                  }

                  logCat.info("READING DISKBLOB field.getDirectory()="
                     + getField().getDirectory() + " " + "fileName=" + fileName);

                  if ((fileName == null) || (getField().getDirectory() == null)
                           || (fileName.length() == 0)
                           || (getField().getDirectory().length() == 0))
                  {
                     return EVAL_PAGE;
                  }

                  File file = new File(getField().getDirectory(), fileName);

                  if (file.exists())
                  {
                     logCat.info("fs- file found " + file.getName());

                     FileInputStream fis  = new FileInputStream(file);
                     BufferedReader  br   = new BufferedReader(new InputStreamReader(
                              fis));
                     char[]          c    = new char[1024];
                     int             read;

                     while ((read = br.read(c)) != -1)
                     {
                        contentBuf.append(c, 0, read);
                     }

                     fis.close();
                  }
                  else
                  {
                     logCat.info("fs- file not found");
                  }
               }
               else
               {
                  throw new IllegalArgumentException(
                     "DbBlobContentTag is currently only for DISKBLOBS - feel free to copy code from FileServlet.java to this place to bring this limitation to an end :=)");
               }
            }
            else
            {
               logCat.info("fs- we have got no result" + queryBuf);
            }
         }
         catch (SQLException sqle)
         {
            sqle.printStackTrace();
         }
         finally
         {
            SqlUtil.closeConnection(con);
         }

         pageContext.getOut().write(contentBuf.toString());
      }
      catch (java.io.IOException ioe)
      {
         throw new JspException("IO Error: " + ioe.getMessage());
      }

      return EVAL_PAGE;
   }

   public void doFinally()
   {
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
   private String getKeyVal()
   {
      return getParentForm().getTable().getKeyPositionString(getParentForm().getResultSetVector());
   }
   /**
    * @return
    */
   public String getDbConnectionName() {
      return dbConnectionName;
   }

   /**
    * @param string
    */
   public void setDbConnectionName(String string) {
      dbConnectionName = string;
   }

}
