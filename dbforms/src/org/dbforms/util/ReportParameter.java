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

/**
 * 
 * Helper class send as parameter to JasperReports. So it is not neccesary to
 * send all the stuff in different parameters
 * 
 * @author hkk
 *
 */
import javax.servlet.http.*;
import java.sql.*;
import org.dbforms.util.*;

public class ReportParameter {

	private HttpServletRequest request;
	private Connection connection;
	private String reportPath;

	public ReportParameter(HttpServletRequest request, Connection connection, String reportPath) {
		this.request = request;
		this.connection = connection;
		this.reportPath = reportPath;
	}

	
	/**
	 * Returns a message
	 * @return String
	 */
	public String getMessage(String msg) {
		return MessageResources.getMessage(request, msg);
	}

	/**
	 * Returns a request parameter
	 * @return String
	 */
	public String getParameter(String param) {
		return ParseUtil.getParameter(request, param);
	}

	/**
	 * Returns the connection.
	 * @return Connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Returns the reportPath.
	 * @return String
	 */
	public String getReportPath() {
		return reportPath;
	}

}