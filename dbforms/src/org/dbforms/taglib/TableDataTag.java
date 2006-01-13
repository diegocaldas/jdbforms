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
package org.dbforms.taglib;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.ResultSetVector;
import org.dbforms.interfaces.DbEventInterceptorData;

import org.dbforms.util.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;



/**
 * external data to be nested into radio, checkbox or select - tag! (useful
 * only in conjunction with radio, checkbox or select - tag)
 *
 * <p>
 * this tag provides data to radio, checkbox or select - tags. it may be used
 * for cross-references to other tables.
 * </p>
 *
 * @author Joachim Peer
 */
public class TableDataTag extends AbstractEmbeddedDataTag
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private static Log logCat = LogFactory.getLog(TableDataTag.class.getName());

   // logging category for this class
   private String foreignTable;
   private String orderBy;
   private String storeField;
   private String visibleFields;

   /**
    * DOCUMENT ME!
    *
    * @param foreignTable DOCUMENT ME!
    */
   public void setForeignTable(String foreignTable) {
      this.foreignTable = foreignTable;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getForeignTable() {
      return foreignTable;
   }


   /**
    * DOCUMENT ME!
    *
    * @param orderBy DOCUMENT ME!
    */
   public void setOrderBy(String orderBy) {
      this.orderBy = orderBy;
      logCat.info("setOrderBy(\"" + orderBy + "\")");
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getOrderBy() {
      return orderBy;
   }


   /**
    * DOCUMENT ME!
    *
    * @param storeField DOCUMENT ME!
    */
   public void setStoreField(String storeField) {
      this.storeField = storeField;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getStoreField() {
      return storeField;
   }


   /**
    * DOCUMENT ME!
    *
    * @param visibleFields DOCUMENT ME!
    */
   public void setVisibleFields(String visibleFields) {
      this.visibleFields = visibleFields;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getVisibleFields() {
      return visibleFields;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      foreignTable     = null;
      visibleFields    = null;
      storeField       = null;
      orderBy          = null;
      super.doFinally();
   }


   /**
    * returns Hashtable with data. Its keys represent the "value"-fields for
    * the DataContainer-Tag, its values     represent the visible fields for
    * the Multitags.     (DataContainer are: select, radio, checkbox and a
    * special flavour of Label).
    *
    * @param con DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected List fetchData(Connection con) throws SQLException {
      Vector       vf = StringUtil.splitString(visibleFields, ",;~");

      StringBuffer queryBuf = new StringBuffer();

      queryBuf.append("SELECT ");
      queryBuf.append(storeField);
      queryBuf.append(", ");

      for (int i = 0; i < vf.size(); i++) {
         queryBuf.append((String) vf.elementAt(i));

         if (i < (vf.size() - 1)) {
            queryBuf.append(", ");
         }
      }

      queryBuf.append(" FROM ");
      queryBuf.append(foreignTable);

      if (orderBy != null) {
         queryBuf.append(" ORDER BY ");
         queryBuf.append(orderBy);
      }

      logCat.info("about to execute:" + queryBuf.toString());

      PreparedStatement ps  = con.prepareStatement(queryBuf.toString());
      ResultSetVector   rsv = null;

      try {
         rsv = new ResultSetVector();

         HttpServletRequest     request = (HttpServletRequest) pageContext
            .getRequest();
         DbEventInterceptorData data = new DbEventInterceptorData(request,
               getConfig(), con, null);
         data.setAttribute(DbEventInterceptorData.PAGECONTEXT,
                pageContext);
         rsv.addResultSet(data, ps.executeQuery());
      } finally {
         ps.close(); // #JP Jun 27, 2001
      }

      return formatEmbeddedResultRows(rsv);
   }
}
