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

package org.dbforms;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.dbforms.util.*;
import org.dbforms.event.*;
import org.apache.log4j.Category;

/****
 * <p>
 * This servlets is the Controller component in the Model-View-Controller - architecture
 * used by the dbforms-framework. Every request goes through this component and its event
 * dispatching facilities.
 * </p>
 *
 * @author Joe Peer <joepeer@excite.com>
 */


public class Controller extends HttpServlet {

  static Category logCat = Category.getInstance(Controller.class.getName()); // logging category for this class

	private DbFormsConfig config;
	private int maxUploadSize = 102400; // 100KB default upload size

  /**
   * Initialize this servlet.
   */

  public void init() throws ServletException {

		// take Config-Object from application context - this object should have been
		// initalized by Config-Servlet on Webapp/server-startup!
		config = (DbFormsConfig) getServletContext().getAttribute(DbFormsConfig.CONFIG);

		// if existing and valid, override default maxUploadSize, which determinates how
		// big the http/multipart uploads may get
		String maxUploadSizeStr = this.getServletConfig().getInitParameter("maxUploadSize");
		if(maxUploadSizeStr!=null) {
		  try {
			this.maxUploadSize = Integer.parseInt(maxUploadSizeStr);
		  } catch(NumberFormatException nfe) {
			logCat.error("maxUploadSize not a valid number => using default.");
		  }
		}
  }


  /**
   * Process an HTTP "GET" request.
   *
   * @param request The servlet request we are processing
   * @param response The servlet response we are creating
   *
   * @exception IOException if an input/output error occurs
   * @exception ServletException if a servlet exception occurs
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		process(request, response);
	}


  /**
   * Process an HTTP "POST" request.
   *
   * @param request The servlet request we are processing
   * @param response The servlet response we are creating
   *
   * @exception IOException if an input/output error occurs
   * @exception ServletException if a servlet exception occurs
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
	  process(request, response);
  }

  private void process(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {

    // create RFC-1867 data wrapper for the case the form was sent in multipart mode
    // this is needed because common HttpServletRequestImpl - classes do not support it
    // the whole application has to access Request via the "ParseUtil" class
    // this is also true for jsp files written by the user
    // #fixme taglib needed for convenient access to ParseUtil wrapper methods
    String contentType = request.getContentType();
		if(contentType!=null && contentType.startsWith("multipart")) {
			try {
			  logCat.debug("before new multipartRequest");
			  MultipartRequest multipartRequest = new MultipartRequest(request, maxUploadSize);
			  logCat.debug("after new multipartRequest");
			  request.setAttribute("multipartRequest", multipartRequest);
			} catch(IOException ioe) {
			  logCat.debug("Check if uploaded file(s) exceeded allowed size.");
			  sendErrorMessage("Check if uploaded file(s) exceeded allowed size.", request, response);
			  return;
			}
		}

		Connection con = config.getDbConnection().getConnection();
		try {

			Vector errors = new Vector();
			request.setAttribute("errors", errors);

			EventEngine engine = new EventEngine(request, config);
			WebEvent e = engine.generatePrimaryEvent(); // primary event can be any kind of event (database, navigation...)

			if(e instanceof DatabaseEvent) {
				try {
					((DatabaseEvent) e).processEvent(con);
				} catch(SQLException sqle) {
					sqle.printStackTrace();
					errors.addElement(sqle);
				}
			} else {

			// currently, we support db events ONLY
			// but in future there may be events with processEvent() method which do not need a jdbc con!
			// (you may think: "what about navigation events?" - well they are created by the
			// controller but they get executed in the referncing "DbFormTag" at the jsp -- that's why we
			// do not any further operations on them right here...we just put them into the request)

			}

			// secundary Events are always database events
			// (in fact, they all are SQL UPDATEs)
			if(engine.getInvolvedTables()!=null) { // may be null if empty form!
				Enumeration tableEnum = engine.getInvolvedTables().elements();
				while(tableEnum.hasMoreElements()) {
					Table t = (Table) tableEnum.nextElement();

					Enumeration eventEnum = engine.generateSecundaryEvents(e);
					while(eventEnum.hasMoreElements()) {
						DatabaseEvent dbE = (DatabaseEvent) eventEnum.nextElement();
						try {
							dbE.processEvent(con);
						} catch(SQLException sqle2) {
							errors.addElement(sqle2);
						}
					}
				}
			}

			// send as info to dbForms (=> Taglib)
			//if(e instanceof NavigationEvent) {
				request.setAttribute("webEvent", e);
			//}

			request.getRequestDispatcher(e.getFollowUp()).forward(request, response);

		} finally {
			try {
			  con.close();
			}	catch(SQLException sqle3) {
				sqle3.printStackTrace();
			}
		}
  }

  private void sendErrorMessage(String message, HttpServletRequest request, HttpServletResponse response) {
	try {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		out.println("<html><body><h1>ERROR:</h1><p>");
		out.println(message);
		out.println("</p></body></html>");
	} catch(IOException ioe) {
		logCat.error("!!!senderror message crashed!!!"+ioe);
	}
  }

}