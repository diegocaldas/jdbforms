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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.List;



/**
 * external data to be nested into radio, checkbox or select - tag! (useful
 * only in conjunction with radio, checkbox or select - tag)
 *
 * <p>
 * this tag provides data to radio, checkbox or select - tags. it may be used
 * for cross-references to other tables.
 * </p>
 *
 * <p>
 * this tag provides similar functionlaity to "TabData", but as it allows to
 * formulate free querys including all SQL statements your RDBMS supports you
 * have much more flexibility using this tag.
 * </p>
 *
 * <p>
 * query building convention: first column is the "key" column for the
 * radio/check/select elements, all other colums are just "data" columns
 * visible to the user  example: SELECT DISTINCT customer.id, customer.name,
 * customer.adress, debitors.debit FROM customer INNER JOIN id ON  (SELECT id
 * FROM debitors WHERE debit>100000) ORDER BY debit DESC
 * </p>
 * - "id" will be threaten as key-value in select box, "name and address will
 * be shown in select box
 *
 * @author Joachim Peer
 */
public class QueryData extends EmbeddedData
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private static Log logCat = LogFactory.getLog(QueryData.class.getName());

   // logging category for this class
   private String query;

   /**
    * DOCUMENT ME!
    *
    * @param query DOCUMENT ME!
    */
   public void setQuery(String query) {
      this.query = query;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getQuery() {
      return query;
   }


   /**
    * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
    */
   public void doCatch(Throwable t) throws Throwable {
      throw t;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      query = null;
      super.doFinally();
   }


   /**
    * returns Hashtable with data. Its keys represent the "value"-fields for
    * the DataContainer-Tag, its values represent the visible fields for the
    * Multitags. (DataContainer are: select, radio, checkbox and a special
    * flavour of Label).
    *
    * @param con DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   protected List fetchData(Connection con) throws SQLException {
      logCat.info("about to execute user defined query:" + query);

      ResultSetVector   rsv = null;
      PreparedStatement ps = con.prepareStatement(query);

      try {
         rsv = new ResultSetVector(getEscaper(), ps.executeQuery());
      } finally {
         ps.close(); // #JP Jun 27, 2001
      }

      return formatEmbeddedResultRows(rsv);
   }
}
