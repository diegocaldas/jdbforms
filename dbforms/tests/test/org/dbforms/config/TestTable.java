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

package org.dbforms.config;

import org.dbforms.config.Constants;
import org.dbforms.config.Field;
import org.dbforms.config.FieldValue;



/**
 *  Description of the Class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestTable extends org.dbforms.util.AbstractTestCase {
   Field      fAuthorId             = null;
   Field      fBookId               = null;
   FieldValue fvLogicalOR           = null;
   FieldValue fvLogicalORLikeFilter = null;
   FieldValue fvNotLogicalOR        = null;
   Table      table                 = null;

   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void setUp() throws Exception {
      super.setUp();
      fAuthorId = new Field();
      fAuthorId.setName("AUTHOR_ID");
      fAuthorId.setFieldType("char");

      fBookId = new Field();
      fBookId.setName("BOOK_ID");

      fvLogicalOR = new FieldValue(fAuthorId, "10");
      fvLogicalOR.setOperator(Constants.FILTER_EQUAL);
      fvLogicalOR.setLogicalOR(true);

      fvNotLogicalOR = new FieldValue(fBookId, "10");
      fvNotLogicalOR.setOperator(Constants.FILTER_EQUAL);
      fvNotLogicalOR.setLogicalOR(false);

      fvLogicalORLikeFilter = new FieldValue(fAuthorId, "10");
      fvLogicalORLikeFilter.setOperator(Constants.FILTER_LIKE);
      fvLogicalORLikeFilter.setLogicalOR(true);

      table = new Table();
      table.setName("td");
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testGetSelectQuery() throws Exception {
      FieldValue[] fvs = {
                            fvLogicalOR,
                            fvNotLogicalOR
                         };
      String       s = table.getSelectQuery(null, null, null, "f1 >= ?",
                                            Constants.COMPARE_NONE);
      assertTrue("Test fvs Where clause equals:" + s,
                 s.indexOf("SELECT * FROM td WHERE  ( f1 >= ? )") >= 0);

      s = table.getSelectQuery(null, fvs, null, "f1 >= ?",
                               Constants.COMPARE_NONE);
      assertTrue("Test fvs Where clause equals:" + s,
                 s.indexOf("SELECT * FROM td WHERE  ( f1 >= ? )  AND  (  (  ( AUTHOR_ID =  ?  ) AND ( BOOK_ID =  ?  )  )  )") >= 0);
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testGetWhereClause() throws Exception {
      FieldValue[] fvs = {
                            fvLogicalOR,
                            fvNotLogicalOR
                         };
      String       s = table.getWhereClause(fvs);
      assertTrue("Test fvs Where clause equals: (AUTHOR_ID =  ?)  AND (BOOK_ID =  ?):"
                 + s,
                 s.indexOf("( AUTHOR_ID =  ?  ) AND ( BOOK_ID =  ?  )") >= 0);

      FieldValue[] fvs2 = {
                             fvLogicalOR,
                             fvLogicalOR
                          };
      assertTrue("Test fvs2 Where clause equals:" + table.getWhereClause(fvs2),
                 table.getWhereClause(fvs2).indexOf("AUTHOR_ID =  ?  OR AUTHOR_ID =  ?") >= 0);

      FieldValue[] fvs3 = {
                             fvLogicalORLikeFilter,
                             fvLogicalOR
                          };
      s = table.getWhereClause(fvs3);
      assertTrue("Test fvs3 Where clause equals:" + s,
                 s.indexOf("AUTHOR_ID LIKE  ?  OR AUTHOR_ID =  ?") >= 0);
   }
}
