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
import java.sql.SQLException;
import org.apache.log4j.Category;



/**
 *  ConnectionProvider base class.
 *  <br>
 *  To create a ConnectionProvider for your preferred ConnectionPooler,
 *  extend this class and implement <code>initialize</code> and
 *  <code>getConnection</code> methods.
 */
public abstract class ConnectionProvider
{
   /** Log4j category */
   private Category cat = Category.getInstance(this.getClass());

   /** ConnectionProvider preferences */
   private ConnectionProviderPrefs prefs = null;

   /**
    *  Constructor for the ConnectionProvider object.
    *
    * @exception  Exception Description of the Exception
    */
   public ConnectionProvider() throws Exception
   {
   }

   /**
    *  Gets the prefs attribute of the ConnectionProvider object
    *
    * @return  The prefs value
    */
   public ConnectionProviderPrefs getPrefs()
   {
      return prefs;
   }


   /**
    *  Sets the prefs attribute of the ConnectionProvider object
    *
    * @param  prefs The new prefs value
    */
   public void setPrefs(ConnectionProviderPrefs prefs)
   {
      this.prefs = prefs;
   }


   /**
    *  Initialize the connection pool provider.
    *
    * @exception  Exception Description of the Exception
    */
   protected abstract void init() throws Exception;


   /**
    *  Get a JDBC Connection.
    *
    * @return  a JDBC Connection
    * @exception  SQLException Description of the Exception
    */
   protected abstract Connection getConnection() throws SQLException;


   /**
    *  Get a "transactional" JDBC connection.
    *
    * @param  isolationLevel the isolation level to set the connection to
    * @return  the new "transactional" connection object
    * @throws  SQLException if any error occurs
    */
   protected Connection getConnection(int isolationLevel)
      throws SQLException
   {
      Connection con = getConnection();
      con.setTransactionIsolation(isolationLevel);
      con.setAutoCommit(false);

      return con;
   }


   /**
    *  Get the last token from the input string.
    *
    * @param  str the string containing the token
    * @param  tokenSeparator the token separator string (i.e.: "'", ":", etc)
    * @return  the last token from the input string
    */
   protected String getLastToken(String str, String tokenSeparator)
   {
      str.trim();

      return str.substring(str.lastIndexOf(tokenSeparator) + 1, str.length());
   }
   
   
   /**
    * Get the Log4J Category object
    * 
    * @return the Log4J Category object
    */
   protected Category getLog4JCategory() 
   {
      return cat;
   }
}
