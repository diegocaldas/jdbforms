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

package org.dbforms.event;

import java.sql.Connection;

import org.apache.cactus.JspTestCase;
import org.apache.cactus.WebRequest;



import org.dbforms.config.DbFormsConfigRegistry;

import org.dbforms.servlets.ConfigServlet;


/**
 * Tests of the <code>Validation Event</code> class.
 * 
 * @author Henner Kollmann
 * 
 */
public class TestUpdateEvent extends JspTestCase
{

   /**
    * In addition to creating the tag instance and adding the pageContext to
    * it, this method creates a BodyContent object and passes it to the tag.
    * @throws Exception DOCUMENT ME!
    */
   public void setUp() throws Exception
   {
		DbFormsConfigRegistry.instance().register(null);
      config.setInitParameter("dbformsConfig", "/WEB-INF/dbforms-config.xml");
      config.setInitParameter("log4j.configuration", 
                              "/WEB-INF/log4j.properties");
      ConfigServlet configServlet = new ConfigServlet();
      configServlet.init(config);
   }


   //-------------------------------------------------------------------------

      /**
    * DOCUMENT ME!
    *
    * @param theRequest DOCUMENT ME!
    */
   public void beginUpdate(WebRequest theRequest) throws Exception
   {
      theRequest.addParameter("f_0_0@root_2", "organisation 1");
      theRequest.addParameter("of_0_0@root_2", "");
      theRequest.addParameter("f_0_0@root_1", "Eco, Umberto");
      theRequest.addParameter("of_0_0@root_1", "");
   }

   public void testUpdate() throws Exception 
   {
      DatabaseEvent evt = DatabaseEventFactoryImpl.instance()
            .createUpdateEvent(
                  0, 
                  "0@root", 
                  request, 
                  DbFormsConfigRegistry.instance().lookup());
      Connection con = DbFormsConfigRegistry.instance().lookup().getConnection();
      evt.processEvent(con);            
   }


}