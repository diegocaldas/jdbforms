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

import org.dbforms.util.SqlUtil;

import java.sql.Connection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;



/**
 * Grunikiewicz.philip 2001-12-18 Obtain a connection (from the connection
 * pool) using same settings defined in dbForms-config.xml file
 */
public class DbGetConnectionTag extends AbstractScriptHandlerTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private transient Connection conn;
   private String               dbConnectionName;

   /**
    * DOCUMENT ME!
    *
    * @param connection
    */
   public void setConn(Connection connection) {
      conn = connection;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public Connection getConn() {
      return conn;
   }


   /**
    * DOCUMENT ME!
    *
    * @param name DOCUMENT ME!
    */
   public void setDbConnectionName(String name) {
      dbConnectionName = name;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getDbConnectionName() {
      return dbConnectionName;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws javax.servlet.jsp.JspException DOCUMENT ME!
    * @throws JspException DOCUMENT ME!
    */
   public int doEndTag() throws javax.servlet.jsp.JspException {
      try {
         // get the connection and place it in attribute;
         SqlUtil.closeConnection(this.getConn());
         this.setConn(null);
         pageContext.removeAttribute(getId(), PageContext.PAGE_SCOPE);
      } catch (Exception e) {
         throw new JspException("Connection error" + e.getMessage());
      }

      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      dbConnectionName = null;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException DOCUMENT ME!
    * @throws IllegalArgumentException DOCUMENT ME!
    */
   public int doStartTag() throws JspException {
      try {
         // get the connection and place it in attribute;
         this.setConn(getConfig().getConnection(dbConnectionName));
         pageContext.setAttribute(getId(), this.getConn(),
                                  PageContext.PAGE_SCOPE);
      } catch (Exception e) {
         throw new JspException("Connection error" + e.getMessage());
      }

      return EVAL_BODY_INCLUDE;
   }
}
