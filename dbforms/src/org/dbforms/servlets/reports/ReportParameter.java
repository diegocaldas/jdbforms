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

package org.dbforms.servlets.reports;

/**
 * Helper class send as parameter to JasperReports. So it is not neccesary to
 * send all the stuff in different parameters
 * 
 */
import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;

import org.dbforms.util.MessageResources;
import org.dbforms.util.ParseUtil;



/**
 * DOCUMENT ME!
 * 
 * @version $Revision$
 * @author $author$
 */
public class ReportParameter
{
   private HttpServletRequest request;
   private Connection         connection;
   private String             reportPath;
   private String             contextPath;

   /**
    * Creates a new ReportParameter object.
   admin    * 
    * @param request DOCUMENT ME!
    * @param connection DOCUMENT ME!
    * @param reportPath DOCUMENT ME!
    * @param contextPath DOCUMENT ME!
    */
   public ReportParameter(HttpServletRequest request, Connection connection, 
                          String reportPath, String contextPath)
   {
      this.request     = request;
      this.connection  = connection;
      this.reportPath  = reportPath;
      this.contextPath = contextPath;
   }

   /**
    * Returns a message
    * 
    * @param msg DOCUMENT ME!
    * 
    * @return String
    */
   public String getMessage(String msg)
   {
      return MessageResources.getMessage(request, msg);
   }


   /**
    * Returns a request parameter
    * 
    * @param param DOCUMENT ME!
    * 
    * @return String
    */
   public String getParameter(String param)
   {
      return ParseUtil.getParameter(request, param);
   }


   /**
    * Returns the connection.
    * 
    * @return Connection
    */
   public Connection getConnection()
   {
      return connection;
   }


   /**
    * Returns the reportPath.
    * 
    * @return String
    */
   public String getReportPath()
   {
      return reportPath;
   }


   /**
    * @return String
    */
   public String getContextPath()
   {
      return contextPath;
   }
}