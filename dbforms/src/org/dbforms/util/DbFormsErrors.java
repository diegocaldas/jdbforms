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
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.Category;
import org.dbforms.config.error.Error;



/****
 * <p>
 * This class gets populated with data from the dbforms-errors.xml file by the ConfigServlet.
 * This class is a kind of "single point of entry" for error messages: it contains the
 * definitions of error message id's, descriptions, severity etc.
 * </p>
 *
 * @author Philip Grunikiewicz <grunikiewicz.philip@hydro.qc.ca>
 */
public class DbFormsErrors
{
   static Category logCat = Category.getInstance(DbFormsErrors.class.getName()); // logging category for this class

   /** DOCUMENT ME! */
   public static final String ERRORS        = "dbformsErrors";
   private Hashtable          errorIDHash; // for quicker lookup by ID
   private ServletConfig      servletConfig;

   /**
    * Creates a new DbFormsErrors object.
    */
   public DbFormsErrors()
   {
      logCat.info("Create instance of DbFormsErrors");
      errorIDHash = new Hashtable();
   }

   /**
    * DOCUMENT ME!
    *
    * @param error DOCUMENT ME!
    */
   public void addError(Error error)
   {
      logCat.info("error added: " + error);
      errorIDHash.put(error.getId(), error);
   }


   /**
    * DOCUMENT ME!
    *
    * @param id DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public Error getErrorById(String id)
   {
      return (Error) errorIDHash.get(id);
   }


   /**
    * DOCUMENT ME!
    *
    * @param servletConfig DOCUMENT ME!
    */
   public void setServletConfig(ServletConfig servletConfig)
   {
      this.servletConfig = servletConfig;
   }


   /**
   get access to configuration of config servlet
   */
   public ServletConfig getServletConfig()
   {
      return servletConfig;
   }


   /**
   get access to servlet context in order to interoperate with
   other components of the web application
   */
   public ServletContext getServletContext()
   {
      return servletConfig.getServletContext();
   }
}
