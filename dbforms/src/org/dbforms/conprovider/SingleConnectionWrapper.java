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

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLWarning;
import java.sql.Statement;

public class SingleConnectionWrapper implements Connection {

   private Connection _conn;
   private List list = new ArrayList();

   public SingleConnectionWrapper(Connection con) {
      _conn = con;
   }

   public void closeReally() throws SQLException {
      synchronized (_conn) {
         if (!_conn.isClosed())
            _conn.close();
      }
   }

   /**
    * 
    * Close only staments of this connection!
    * 
    */
   public void close() throws SQLException {
      synchronized (_conn) {
         Iterator iter = list.iterator();
         while (iter.hasNext()) {
            Statement stmt = (Statement) iter.next();
            stmt.close();
         }
      }
   }

   public Statement createStatement() throws SQLException {
      synchronized (_conn) {
         Statement res = _conn.createStatement();
         list.add(res);
         return res;
      }
   }

   public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
      synchronized (_conn) {
         Statement res = _conn.createStatement(resultSetType, resultSetConcurrency);
         list.add(res);
         return res;
      }
   }

   public PreparedStatement prepareStatement(String sql) throws SQLException {
      synchronized (_conn) {
         PreparedStatement res = _conn.prepareStatement(sql);
         list.add(res);
         return res;
      }
   }

   public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
      synchronized (_conn) {
         PreparedStatement res = _conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
         list.add(res);
         return res;
      }
   }

   public CallableStatement prepareCall(String sql) throws SQLException {
      synchronized (_conn) {
         CallableStatement res = _conn.prepareCall(sql);
         list.add(res);
         return res;
      }
   }

   public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
      synchronized (_conn) {
         CallableStatement res = _conn.prepareCall(sql, resultSetType, resultSetConcurrency);
         list.add(res);
         return res;
      }
   }

   public void clearWarnings() throws SQLException {
      synchronized (_conn) {
         _conn.clearWarnings();
      }
   }

   public void commit() throws SQLException {
      synchronized (_conn) {
         _conn.commit();
      }
   }

   public boolean getAutoCommit() throws SQLException {
      synchronized (_conn) {
         return _conn.getAutoCommit();
      }
   }

   public String getCatalog() throws SQLException {
      synchronized (_conn) {
         return _conn.getCatalog();
      }
   }

   public DatabaseMetaData getMetaData() throws SQLException {
      synchronized (_conn) {
         return _conn.getMetaData();
      }
   }

   public int getTransactionIsolation() throws SQLException {
      synchronized (_conn) {
         return _conn.getTransactionIsolation();
      }
   }

   public Map getTypeMap() throws SQLException {
      synchronized (_conn) {
         return _conn.getTypeMap();
      }
   }

   public SQLWarning getWarnings() throws SQLException {
      synchronized (_conn) {
         return _conn.getWarnings();
      }
   }

   public boolean isClosed() throws SQLException {
      synchronized (_conn) {
         return _conn.isClosed();
      }
   }

   public boolean isReadOnly() throws SQLException {
      synchronized (_conn) {
         return _conn.isReadOnly();
      }
   }

   public String nativeSQL(String sql) throws SQLException {
      synchronized (_conn) {
         return _conn.nativeSQL(sql);
      }
   }

   public void rollback() throws SQLException {
      synchronized (_conn) {
         _conn.rollback();
      }
   }

   public void setAutoCommit(boolean autoCommit) throws SQLException {
      synchronized (_conn) {
         _conn.setAutoCommit(autoCommit);
      }
   }

   public void setCatalog(String catalog) throws SQLException {
      synchronized (_conn) {
         _conn.setCatalog(catalog);
      }
   }

   public void setReadOnly(boolean readOnly) throws SQLException {
      synchronized (_conn) {
         _conn.setReadOnly(readOnly);
      }
   }

   public void setTransactionIsolation(int level) throws SQLException {
      synchronized (_conn) {
         _conn.setTransactionIsolation(level);
      }
   }

   public void setTypeMap(Map map) throws SQLException {
      synchronized (_conn) {
         _conn.setTypeMap(map);
      }
   }

   public int getHoldability() throws SQLException {
      synchronized (_conn) {
         return _conn.getHoldability();
      }
   }

   public void setHoldability(int holdability) throws SQLException {
      synchronized (_conn) {
         _conn.setHoldability(holdability);
      }
   }

   public java.sql.Savepoint setSavepoint() throws SQLException {
      synchronized (_conn) {
         return _conn.setSavepoint();
      }
   }

   public java.sql.Savepoint setSavepoint(String name) throws SQLException {
      synchronized (_conn) {
         return _conn.setSavepoint(name);
      }
   }

   public void rollback(java.sql.Savepoint savepoint) throws SQLException {
      synchronized (_conn) {
         _conn.rollback(savepoint);
      }
   }

   public void releaseSavepoint(java.sql.Savepoint savepoint) throws SQLException {
      synchronized (_conn) {
         _conn.releaseSavepoint(savepoint);
      }
   }

   public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
      throws SQLException {
      synchronized (_conn) {
         Statement res = _conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
         list.add(res);
         return res;
      }
   }

   public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
      throws SQLException {
      synchronized (_conn) {
         PreparedStatement res = _conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
         list.add(res);
         return res;
      }
   }

   public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
      throws SQLException {
      synchronized (_conn) {
         CallableStatement res = _conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
         list.add(res);
         return res;
      }
   }

   public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
      synchronized (_conn) {
         PreparedStatement res = _conn.prepareStatement(sql, autoGeneratedKeys);
         list.add(res);
         return res;
      }
   }

   public PreparedStatement prepareStatement(String sql, int columnIndexes[]) throws SQLException {
      synchronized (_conn) {
         PreparedStatement res = _conn.prepareStatement(sql, columnIndexes);
         list.add(res);
         return res;
      }
   }

   public PreparedStatement prepareStatement(String sql, String columnNames[]) throws SQLException {
      synchronized (_conn) {
         PreparedStatement res = _conn.prepareStatement(sql, columnNames);
         list.add(res);
         return res;
      }
   }
}
