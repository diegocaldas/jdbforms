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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class SingleConnectionWrapper implements Connection {
   private static Log logCat = LogFactory.getLog(SingleConnectionWrapper.class
                                                 .getName());
   private Connection _conn;
   private List       list = new ArrayList();
   private boolean    open = true;

   /**
    * Creates a new SingleConnectionWrapper object.
    *
    * @param con DOCUMENT ME!
    */
   public SingleConnectionWrapper(Connection con) {
      _conn = con;
   }

   /**
    * DOCUMENT ME!
    *
    * @param autoCommit DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public void setAutoCommit(boolean autoCommit) throws SQLException {
      synchronized (_conn) {
         _conn.setAutoCommit(autoCommit);
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public boolean getAutoCommit() throws SQLException {
      synchronized (_conn) {
         return _conn.getAutoCommit();
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param catalog DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public void setCatalog(String catalog) throws SQLException {
      synchronized (_conn) {
         _conn.setCatalog(catalog);
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public String getCatalog() throws SQLException {
      synchronized (_conn) {
         return _conn.getCatalog();
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public boolean isClosed() throws SQLException {
      synchronized (_conn) {
         return !open || _conn.isClosed();
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param holdability DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public void setHoldability(int holdability) throws SQLException {
      synchronized (_conn) {
         _conn.setHoldability(holdability);
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public int getHoldability() throws SQLException {
      synchronized (_conn) {
         return _conn.getHoldability();
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public DatabaseMetaData getMetaData() throws SQLException {
      synchronized (_conn) {
         return _conn.getMetaData();
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param readOnly DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public void setReadOnly(boolean readOnly) throws SQLException {
      synchronized (_conn) {
         _conn.setReadOnly(readOnly);
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public boolean isReadOnly() throws SQLException {
      synchronized (_conn) {
         return _conn.isReadOnly();
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public java.sql.Savepoint setSavepoint() throws SQLException {
      synchronized (_conn) {
         return _conn.setSavepoint();
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param name DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public java.sql.Savepoint setSavepoint(String name)
                                   throws SQLException {
      synchronized (_conn) {
         return _conn.setSavepoint(name);
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param level DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public void setTransactionIsolation(int level) throws SQLException {
      synchronized (_conn) {
         _conn.setTransactionIsolation(level);
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public int getTransactionIsolation() throws SQLException {
      synchronized (_conn) {
         return _conn.getTransactionIsolation();
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param map DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public void setTypeMap(Map map) throws SQLException {
      synchronized (_conn) {
         _conn.setTypeMap(map);
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public Map getTypeMap() throws SQLException {
      synchronized (_conn) {
         return _conn.getTypeMap();
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public SQLWarning getWarnings() throws SQLException {
      synchronized (_conn) {
         return _conn.getWarnings();
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public void clearWarnings() throws SQLException {
      synchronized (_conn) {
         _conn.clearWarnings();
      }
   }


   /**
    * Close only statements of this connection! do not close the connection!
    */
   public void close() throws SQLException {
      synchronized (_conn) {
         Iterator iter = list.iterator();

         while (iter.hasNext()) {
            Statement stmt = (Statement) iter.next();

            try {
               stmt.close();
            } catch (SQLException e) {
//               logCat.info("close", e);
            	;
            } catch (Exception e) {
               logCat.error(e);
            }
         }

         list.clear();
         open = false;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public void closeReally() throws SQLException {
      synchronized (_conn) {
         if (!_conn.isClosed()) {
            _conn.close();
         }
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public void commit() throws SQLException {
      synchronized (_conn) {
         _conn.commit();
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public Statement createStatement() throws SQLException {
      synchronized (_conn) {
         Statement res = _conn.createStatement();
         list.add(res);

         return res;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param resultSetType DOCUMENT ME!
    * @param resultSetConcurrency DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public Statement createStatement(int resultSetType,
                                    int resultSetConcurrency)
                             throws SQLException {
      synchronized (_conn) {
         Statement res = _conn.createStatement(resultSetType,
                                               resultSetConcurrency);
         list.add(res);

         return res;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param resultSetType DOCUMENT ME!
    * @param resultSetConcurrency DOCUMENT ME!
    * @param resultSetHoldability DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public Statement createStatement(int resultSetType,
                                    int resultSetConcurrency,
                                    int resultSetHoldability)
                             throws SQLException {
      synchronized (_conn) {
         Statement res = _conn.createStatement(resultSetType,
                                               resultSetConcurrency,
                                               resultSetHoldability);
         list.add(res);

         return res;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param sql DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public String nativeSQL(String sql) throws SQLException {
      synchronized (_conn) {
         return _conn.nativeSQL(sql);
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param sql DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public CallableStatement prepareCall(String sql) throws SQLException {
      synchronized (_conn) {
         CallableStatement res = _conn.prepareCall(sql);
         list.add(res);

         return res;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param sql DOCUMENT ME!
    * @param resultSetType DOCUMENT ME!
    * @param resultSetConcurrency DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public CallableStatement prepareCall(String sql,
                                        int    resultSetType,
                                        int    resultSetConcurrency)
                                 throws SQLException {
      synchronized (_conn) {
         CallableStatement res = _conn.prepareCall(sql, resultSetType,
                                                   resultSetConcurrency);
         list.add(res);

         return res;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param sql DOCUMENT ME!
    * @param resultSetType DOCUMENT ME!
    * @param resultSetConcurrency DOCUMENT ME!
    * @param resultSetHoldability DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public CallableStatement prepareCall(String sql,
                                        int    resultSetType,
                                        int    resultSetConcurrency,
                                        int    resultSetHoldability)
                                 throws SQLException {
      synchronized (_conn) {
         CallableStatement res = _conn.prepareCall(sql, resultSetType,
                                                   resultSetConcurrency,
                                                   resultSetHoldability);
         list.add(res);

         return res;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param sql DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public PreparedStatement prepareStatement(String sql)
                                      throws SQLException {
      synchronized (_conn) {
         PreparedStatement res = _conn.prepareStatement(sql);
         list.add(res);

         return res;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param sql DOCUMENT ME!
    * @param resultSetType DOCUMENT ME!
    * @param resultSetConcurrency DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public PreparedStatement prepareStatement(String sql,
                                             int    resultSetType,
                                             int    resultSetConcurrency)
                                      throws SQLException {
      synchronized (_conn) {
         PreparedStatement res = _conn.prepareStatement(sql, resultSetType,
                                                        resultSetConcurrency);
         list.add(res);

         return res;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param sql DOCUMENT ME!
    * @param resultSetType DOCUMENT ME!
    * @param resultSetConcurrency DOCUMENT ME!
    * @param resultSetHoldability DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public PreparedStatement prepareStatement(String sql,
                                             int    resultSetType,
                                             int    resultSetConcurrency,
                                             int    resultSetHoldability)
                                      throws SQLException {
      synchronized (_conn) {
         PreparedStatement res = _conn.prepareStatement(sql, resultSetType,
                                                        resultSetConcurrency,
                                                        resultSetHoldability);
         list.add(res);

         return res;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param sql DOCUMENT ME!
    * @param autoGeneratedKeys DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public PreparedStatement prepareStatement(String sql,
                                             int    autoGeneratedKeys)
                                      throws SQLException {
      synchronized (_conn) {
         PreparedStatement res = _conn.prepareStatement(sql, autoGeneratedKeys);
         list.add(res);

         return res;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param sql DOCUMENT ME!
    * @param columnIndexes DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public PreparedStatement prepareStatement(String sql,
                                             int[]  columnIndexes)
                                      throws SQLException {
      synchronized (_conn) {
         PreparedStatement res = _conn.prepareStatement(sql, columnIndexes);
         list.add(res);

         return res;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param sql DOCUMENT ME!
    * @param columnNames DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public PreparedStatement prepareStatement(String   sql,
                                             String[] columnNames)
                                      throws SQLException {
      synchronized (_conn) {
         PreparedStatement res = _conn.prepareStatement(sql, columnNames);
         list.add(res);

         return res;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param savepoint DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public void releaseSavepoint(java.sql.Savepoint savepoint)
                         throws SQLException {
      synchronized (_conn) {
         _conn.releaseSavepoint(savepoint);
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public void rollback() throws SQLException {
      synchronized (_conn) {
         _conn.rollback();
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param savepoint DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   public void rollback(java.sql.Savepoint savepoint) throws SQLException {
      synchronized (_conn) {
         _conn.rollback(savepoint);
      }
   }
}
