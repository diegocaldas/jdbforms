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

package org.dbforms.conprovider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbforms.util.Util;
import org.dbforms.util.ReflectionUtil;



/**
 * Single Connection provider. <br> provides one connection for all
 *
 * @author Henner Kollmann
 */
public class SingleConnectionProvider extends AbstractConnectionProvider {
   private static Connection con;
   private static Date conNextValidationDate;
   private static Log logCat = LogFactory.getLog(SinglePerThreadConnectionProvider.class);

   
   /**
    * Default constructor.
    *
    * @exception Exception Description of the Exception
    * @throws Exception because of the <code>throws Exception</code> clause of
    *         the  <code>init</code> method.
    */
   public SingleConnectionProvider() throws Exception {
      super();
   }

   /**
    * Get a JDBC Connection
    *
    * @return a JDBC Connection
    *
    * @exception SQLException Description of the Exception
    */
   protected synchronized Connection getConnection() throws SQLException {
		long validationInterval;
		// Get Validation Interval from the config and convert to seconds.
		// Default interval is six hours (21,600,000 milliseconds).
		try {
			validationInterval = Long.parseLong(getPrefs().getPoolProperties()
					.getProperty("validationInterval", "21600"));
			// Convert from seconds as expressed in property to milliseconds.
			validationInterval = validationInterval * 1000;
		} catch (NumberFormatException ex) {
			validationInterval = 21600000;
		}

		Date rightNow = new Date();
		// Initialise the validation check time, validationInterval into the
		// future from now.
		if (conNextValidationDate == null) {
			conNextValidationDate = new Date(validationInterval + rightNow.getTime());
		}

		if (con != null && conNextValidationDate.before(rightNow)) {
			conNextValidationDate.setTime(validationInterval
					+ rightNow.getTime());
			String validationQuery = getPrefs().getPoolProperties().getProperty("validationQuery", "");
			if (!Util.isNull(validationQuery)) {
				logCat.debug("Testing connection: checking validation timestamp='"
								+ rightNow.toString() + "'.");
				logCat.debug("Testing connection: next validation check='"
						+ conNextValidationDate.toString() + "'.");
				logCat.debug("Testing connection: validationQuery='"
						+ validationQuery + "'.");
				// Test the connection.
				try {
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(validationQuery);
					try {
						rs.next();
						logCat.debug("Testing connection: Connection is valid.");
					} finally {
						rs.close();
						st.close();
					}
				} catch (SQLException sqlex) {
					// Exception, so close the connection and set to null
					// so it is recreated in the body of the "if (con == null)"
					// below.
					logCat.debug("Testing connection: Connection is invalid. Forcing recreate.");
					con.close();
					con = null;
				}
			}
		}
	   
	   if (con == null) {
         Properties props = getPrefs()
                               .getProperties();

         // uses custom jdbc properties;
         if ((props != null) && !props.isEmpty()) {
            props.put("user", getPrefs().getUser());
            props.put("password", getPrefs().getPassword());
            con = DriverManager.getConnection(getPrefs().getJdbcURL(), props);
         }
         // "plain" flavour;
         else {
            con = DriverManager.getConnection(getPrefs().getJdbcURL(),
                                              getPrefs().getUser(),
                                              getPrefs().getPassword());
         }
      }

      return new SingleConnectionWrapper(con);
   }


   /**
    * Initialize the ConnectionProvider.
    *
    * @throws Exception if any error occurs
    */
   protected void init() throws Exception {
	   ReflectionUtil.newInstance(getPrefs().getJdbcDriver());
   }
}
