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

import java.util.Properties;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 *  Single Connection provider.
 *  <br>
 *  provides one connection for all
 * 
 * @author Henner Kollmann
 * 
 */
public class SingleConnectionProvider extends ConnectionProvider {
   private static SingleConnectionWrapper _con;
   /**
    *  Default constructor.
    *
    * @exception  Exception Description of the Exception
    * @throws  Exception because of the <code>throws Exception</code> clause
    *          of the  <code>init</code> method.
    */
   public SingleConnectionProvider() throws Exception {
      super();
   }

   /**
    *  Get a JDBC Connection
    *
    * @return  a JDBC Connection
    * @exception  SQLException Description of the Exception
    */
   protected synchronized Connection getConnection() throws SQLException {
      if (_con == null) {
         Properties props = getPrefs().getProperties();
         Connection con = null;

         // uses custom jdbc properties;
         if ((props != null) && !props.isEmpty()) {
            props.put("user", getPrefs().getUser());
            props.put("password", getPrefs().getPassword());
            con = DriverManager.getConnection(getPrefs().getJdbcURL(), props);
         }

         // "plain" flavour;
         else {
            con = DriverManager.getConnection(getPrefs().getJdbcURL(), getPrefs().getUser(), getPrefs().getPassword());
         }

         _con = new SingleConnectionWrapper(con);
      }
      return _con;
   }

   /**
    *  Initialize the ConnectionProvider.
    *
    * @throws  Exception if any error occurs
    */
   protected void init() throws Exception {
      Class.forName(getPrefs().getJdbcDriver()).newInstance();
   }

   private class SingleConnectionWrapper implements Connection {

      private Connection _conn;
      private List list = new ArrayList();

      public SingleConnectionWrapper(Connection con) {
         _conn = con;
      }

      protected void closeReally() throws SQLException {
         Iterator iter = list.iterator();
         while (iter.hasNext()) {
            Statement stmt = (Statement) iter.next();
            stmt.close();
         }
         _conn.close();
      }

      /**
       * closing of the connection statements is not allowed here.
       * closing will close all opened resultset too!
       */
      public void close() throws SQLException {}

      public Statement createStatement() throws SQLException {
         Statement res = _conn.createStatement();
         list.add(res);
         return res;
      }

      public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
         Statement res = _conn.createStatement(resultSetType, resultSetConcurrency);
         list.add(res);
         return res;
      }

      public PreparedStatement prepareStatement(String sql) throws SQLException {
         PreparedStatement res = _conn.prepareStatement(sql);
         list.add(res);
         return res;
      }

      public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
         PreparedStatement res = _conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
         list.add(res);
         return res;
      }

      public CallableStatement prepareCall(String sql) throws SQLException {
         CallableStatement res = _conn.prepareCall(sql);
         list.add(res);
         return res;
      }

      public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
         CallableStatement res = _conn.prepareCall(sql, resultSetType, resultSetConcurrency);
         list.add(res);
         return res;
      }

      public void clearWarnings() throws SQLException {
         _conn.clearWarnings();
      }

      public void commit() throws SQLException {
         _conn.commit();
      }

      public boolean getAutoCommit() throws SQLException {
         return _conn.getAutoCommit();
      }

      public String getCatalog() throws SQLException {
         return _conn.getCatalog();
      }

      public DatabaseMetaData getMetaData() throws SQLException {
         return _conn.getMetaData();
      }

      public int getTransactionIsolation() throws SQLException {
         return _conn.getTransactionIsolation();
      }

      public Map getTypeMap() throws SQLException {
         return _conn.getTypeMap();
      }

      public SQLWarning getWarnings() throws SQLException {
         return _conn.getWarnings();
      }

      public boolean isClosed() throws SQLException {
         return _conn.isClosed();
      }

      public boolean isReadOnly() throws SQLException {
         return _conn.isReadOnly();
      }

      public String nativeSQL(String sql) throws SQLException {
         return _conn.nativeSQL(sql);
      }

      public void rollback() throws SQLException {
         _conn.rollback();
      }

      public void setAutoCommit(boolean autoCommit) throws SQLException {
         _conn.setAutoCommit(autoCommit);
      }

      public void setCatalog(String catalog) throws SQLException {
         _conn.setCatalog(catalog);
      }

      public void setReadOnly(boolean readOnly) throws SQLException {
         _conn.setReadOnly(readOnly);
      }

      public void setTransactionIsolation(int level) throws SQLException {
         _conn.setTransactionIsolation(level);
      }

      public void setTypeMap(Map map) throws SQLException {
         _conn.setTypeMap(map);
      }

      public int getHoldability() throws SQLException {
         return _conn.getHoldability();
      }

      public void setHoldability(int holdability) throws SQLException {
         _conn.setHoldability(holdability);
      }

      public java.sql.Savepoint setSavepoint() throws SQLException {
         return _conn.setSavepoint();
      }

      public java.sql.Savepoint setSavepoint(String name) throws SQLException {
         return _conn.setSavepoint(name);
      }

      public void rollback(java.sql.Savepoint savepoint) throws SQLException {
         _conn.rollback(savepoint);
      }

      public void releaseSavepoint(java.sql.Savepoint savepoint) throws SQLException {
         _conn.releaseSavepoint(savepoint);
      }

      public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
         throws SQLException {
         Statement res = _conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
         list.add(res);
         return res;
      }

      public PreparedStatement prepareStatement(
         String sql,
         int resultSetType,
         int resultSetConcurrency,
         int resultSetHoldability)
         throws SQLException {
         PreparedStatement res = _conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
         list.add(res);
         return res;
      }

      public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
         throws SQLException {
         CallableStatement res = _conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
         list.add(res);
         return res;
      }

      public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
         PreparedStatement res = _conn.prepareStatement(sql, autoGeneratedKeys);
         list.add(res);
         return res;
      }

      public PreparedStatement prepareStatement(String sql, int columnIndexes[]) throws SQLException {
         PreparedStatement res = _conn.prepareStatement(sql, columnIndexes);
         list.add(res);
         return res;
      }

      public PreparedStatement prepareStatement(String sql, String columnNames[]) throws SQLException {
         PreparedStatement res = _conn.prepareStatement(sql, columnNames);
         list.add(res);
         return res;
      }
   }

}
