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

import org.dbforms.util.AbstractTestCase;

import java.util.Locale;



/**
 *  Description of the Class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestFieldValue extends AbstractTestCase {
   Field      fAuthorId             = null;
   Field      fBookId               = null;
   FieldValue fvLogicalOR           = null;
   FieldValue fvLogicalORLikeFilter = null;
   FieldValue fvNotLogicalOR        = null;

   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void setUp() throws Exception {
      super.setUp();
      fAuthorId = new Field();
      fAuthorId.setName("AUTHOR_ID");
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
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testClone() throws Exception {
      FieldValue fv = new FieldValue(fAuthorId, "10");

      FieldValue fvClone = (FieldValue) fv.clone();
      assertTrue("Make sure toStrings match",
                 fv.toString().equals(fvClone.toString()));
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testCon() throws Exception {
      FieldValue fv = new FieldValue(fAuthorId, "10");
      assertTrue("make sure we get our field back.",
                 fv.getField().equals(fAuthorId));
      assertTrue("make sure we get our field value.",
                 fv.getFieldValue().equals("10"));
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testInvert() throws Exception {
      fvLogicalOR.setSortDirection(Constants.ORDER_ASCENDING);
      fvNotLogicalOR.setSortDirection(Constants.ORDER_ASCENDING);
      fvLogicalORLikeFilter.setSortDirection(Constants.ORDER_ASCENDING);

      FieldValue fvLogicalOROrig           = (FieldValue) fvLogicalOR.clone();
      FieldValue fvNotLogicalOROrig        = (FieldValue) fvNotLogicalOR.clone();
      FieldValue fvLogicalORLikeFilterOrig = (FieldValue) fvLogicalORLikeFilter
                                             .clone();

      FieldValue[] fvs = {
                            fvLogicalOR,
                            fvNotLogicalOR,
                            fvLogicalORLikeFilter
                         };

      FieldValue.invert(fvs);
      assertTrue("fv was flipped",
                 fvLogicalOR.getSortDirection() != fvLogicalOROrig
                                                   .getSortDirection());
      assertTrue("fv was flipped",
                 fvNotLogicalOR.getSortDirection() != fvNotLogicalOROrig
                                                      .getSortDirection());
      assertTrue("fv was flipped",
                 fvLogicalORLikeFilter.getSortDirection() != fvLogicalORLikeFilterOrig
                                                             .getSortDirection());
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testLogicalOr() throws Exception {
      FieldValue fv = new FieldValue(fAuthorId, "10");
      assertTrue("Make sure this is not a logical OR.", !fv.getLogicalOR());
      fv.setLogicalOR(true);
      assertTrue("make sure this is a logical OR.", fv.getLogicalOR());
   }


   /**
    * DOCUMENT ME!
    */
   public void testNull() {
      Field f = new Field();
      f.setName("TESTINT");
      f.setFieldType("int");

      FieldValue fv = new FieldValue(f, "");
      fv.setLocale(Locale.ENGLISH);

      Integer i = (Integer) fv.getFieldValueAsObject();
      assertTrue(i == null);
   }
}
