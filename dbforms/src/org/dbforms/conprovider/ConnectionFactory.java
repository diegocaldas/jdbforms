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


import java.sql.*;
import org.apache.log4j.Category;


/**
 *  ConnectionFactory class.
 *  <br>
 *  Provides SQL Connection objects using the
 *  underlying ConnectionProvider instance.
 */
public class ConnectionFactory
{
   /** Log4j category */
   private Category cat = Category.getInstance(this.getClass());

   /** an handle to the unique ConnectionFactory instance. */
   private static ConnectionFactory instance = null;

   /** default ConnectionProvider instance */
   private ConnectionProvider provider = null;

   /**
    *   Constructor for the ConnectionFactory object
    */
   private ConnectionFactory()
   {
   }

   /**
    *  Get the unique instance of ConnectionFactory class.
    *
    * @return  the instance of ConnectionFactory class
    */
   public static synchronized ConnectionFactory instance()
   {
      if (instance == null)
      {
         instance = new ConnectionFactory();
      }

      return instance;
   }


   /**
    *  Set the ConnectionProvider object
    *
    * @param  prefs     the connection provider preferences object
    * @throws Exception if any error occurs
    */
   public synchronized void setProvider(ConnectionProviderPrefs prefs)
      throws Exception
   {
      String providerClass = prefs.getConnectionProviderClass();
      provider = (ConnectionProvider) Class.forName(providerClass).newInstance();
      provider.setPrefs(prefs);
      provider.init();

      cat.info("::setProvider - ConnectionProvider [" + providerClass
               + "] successfully set and initialized");
   }


   /**
    *  Get a connection object from the underlying ConnectionProvider object.
    *
    * @return  the connection object from the underlying ConnectionProvider object
    * @throws  SQLException if any error occurs
    */
   public Connection getConnection() throws SQLException
   {
      return provider.getConnection();
   }


   /**
    *  Get a "transactional" JDBC connection from the underlying default ConnectionProvider.
    *
    * @param  isolationLevel the isolation level to set the connection to
    * @return  the new "transactional" connection object
    *         from the underlying default ConnectionProvider
    * @throws  SQLException if any error occurs
    */
   public Connection getConnection(int isolationLevel)
      throws SQLException
   {
      return provider.getConnection(isolationLevel);
   }
}
