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
import java.util.*;
import java.sql.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.dbforms.util.*;
import org.dbforms.*;
import org.apache.log4j.Category;



/*************************************************************
 * Grunikiewicz.philip@hydro.qc.ca
 * 2001-12-18
 *
 * Obtain a connection (from the connection pool) using same settings defined
 * in dbForms-config.xml file
 *
 * ***************************************************************/
public class DbGetConnection extends BodyTagSupport implements TryCatchFinally
{
   static Category logCat = Category.getInstance(DbGetConnection.class.getName());

   // logging category for this class
   private String        id;
   private Connection    con;
   private String        dbConnectionName;
   private DbFormsConfig config;

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException DOCUMENT ME!
    * @throws IllegalArgumentException DOCUMENT ME!
    */
   public int doStartTag() throws JspException
   {
      try
      {
         // get the connection and place it in attribute;
         con = SqlUtil.getConnection(config, dbConnectionName);
         pageContext.setAttribute(this.getId(), con, PageContext.PAGE_SCOPE);
      }
      catch (Exception e)
      {
         throw new JspException("Database error" + e.toString());
      }

      return EVAL_BODY_BUFFERED;
   }


   /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
   public int doAfterBody()
   {
      try
      {
         bodyContent.writeOut(bodyContent.getEnclosingWriter());
      }
      catch (IOException ioe)
      {
         ioe.printStackTrace();
      }

      return SKIP_BODY;
   }


   /**
   * Gets the id
   * @return Returns a String
   */
   public String getId()
   {
      return id;
   }


   /**
   * Sets the id
   * @param id The id to set
   */
   public void setId(String id)
   {
      this.id = id;
   }


   /**
   * DOCUMENT ME!
   *
   * @param name DOCUMENT ME!
   */
   public void setDbConnectionName(String name)
   {
      dbConnectionName = name;
   }


   /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
   public String getDbConnectionName()
   {
      return dbConnectionName;
   }


   /**
   * DOCUMENT ME!
   *
   * @param pc DOCUMENT ME!
   */
   public void setPageContext(PageContext pc)
   {
      super.setPageContext(pc);
      config = (DbFormsConfig) pageContext.getServletContext().getAttribute(DbFormsConfig.CONFIG);

      if (config == null)
      {
         throw new IllegalArgumentException(
            "Troubles with DbForms config xml file: can not find CONFIG object in application context! check system configuration! check if application crashes on start-up!");
      }
   }


   /**
   * DOCUMENT ME!
   */
   public void doFinally()
   {
      SqlUtil.closeConnection(con);
   }


   /**
   * DOCUMENT ME!
   *
   * @param t DOCUMENT ME!
   *
   * @throws Throwable DOCUMENT ME!
   */
   public void doCatch(Throwable t) throws Throwable
   {
      throw t;
   }
}
