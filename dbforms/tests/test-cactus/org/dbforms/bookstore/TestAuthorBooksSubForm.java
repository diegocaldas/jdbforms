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

package org.dbforms.bookstore;

import org.dbforms.interfaces.StaticData;
import org.dbforms.util.AbstractHttpTestCase;
import org.dbforms.util.MessageResources;
import org.dbforms.util.MessageResourcesInternal;

import java.util.ArrayList;

// imports
import java.util.List;
import java.util.Locale;



// definition of test class
public class TestAuthorBooksSubForm extends AbstractHttpTestCase {
   private String nodata = null;

   // Test method generated from the MaxQ Java generator
   public TestAuthorBooksSubForm(String name) {
      super(name);

      if (nodata == null) {
         MessageResources.setSubClass("resources");
         nodata = MessageResourcesInternal.getMessage("dbforms.nodata",
                                                      Locale.getDefault());
      }
      println("nodata is " + nodata);
   }

   /**
    * Start the tests.
    *
    * @param theArgs
    *            the arguments. Not used
    */
   public static void main(String[] theArgs) {
      junit.textui.TestRunner.main(new String[] {
                                      TestAuthorBooksSubForm.class.getName()
                                   });
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testNavCopyMain() throws Exception {
      List list;
      get("http://localhost/bookstore/tests/testAuthorBooksSubForm.jsp");
      printResponse();
      assertTrue(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_0"));
      assertTrue(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_0"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "0"));
      list.add(new StaticData("invname_0", "null"));
      list.add(new StaticData("autoupdate_0", "false"));
      list.add(new StaticData("fu_0", "/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_0",
                                "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
      list.add(new StaticData("lastpos_0",
                                "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
      list.add(new StaticData("f_0_0@root_1", "Eco, Umberto"));
      list.add(new StaticData("f_0_0@root_2", "organisation 11"));
      list.add(new StaticData("ac_copy_0", "Copy"));
      list.add(new StaticData("invtable", "1"));
      list.add(new StaticData("invname_1", "null"));
      list.add(new StaticData("autoupdate_1", "false"));
      list.add(new StaticData("fu_1", "/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_1",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("lastpos_1",
                                "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
      list.add(new StaticData("f_1_0@0@root_1", "3-423-12445-4"));
      list.add(new StaticData("f_1_0@0@root_3", "Die Insel des vorigen Tages"));
      list.add(new StaticData("f_1_1@0@root_1", "3-423-12445-5"));
      list.add(new StaticData("f_1_1@0@root_3", "Das Foucaltsche Pendel"));
      list.add(new StaticData("k_1_0@0@root",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("f_1_ins0@root_2", "1"));
      list.add(new StaticData("k_1_1@0@root",
                                "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
      list.add(new StaticData("f_1_ins0@root_2", "1"));
      list.add(new StaticData("k_0_0@root", "0%3A1%3A1"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertFalse(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_0"));
      assertFalse(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_0"));
      assertTrue(responseContains("<td style=\"width:100px\">" + nodata
                                  + "&nbsp;</td>"));
      assertTrue(responseContains("<input type=\"text\" name=\"f_0_insroot_1\" value=\"Eco, Umberto\"  size=\"25\"/>"));
      assertTrue(responseContains("<input type=\"text\" name=\"f_0_insroot_2\" value=\"organisation 11\"  size=\"25\"/>"));
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testNavCopySub() throws Exception {
      List list;

      get("http://localhost/bookstore/tests/testAuthorBooksSubForm.jsp");
      printResponse();
      assertTrue(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_1"));
      assertTrue(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_1"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "0"));
      list.add(new StaticData("invname_0", "null"));
      list.add(new StaticData("autoupdate_0", "false"));
      list.add(new StaticData("fu_0", "/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_0",
                                "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
      list.add(new StaticData("lastpos_0",
                                "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
      list.add(new StaticData("f_0_0@root_1", "Eco, Umberto"));
      list.add(new StaticData("f_0_0@root_2", "organisation 11"));
      list.add(new StaticData("invtable", "1"));
      list.add(new StaticData("invname_1", "null"));
      list.add(new StaticData("autoupdate_1", "false"));
      list.add(new StaticData("fu_1", "/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_1",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("lastpos_1",
                                "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
      list.add(new StaticData("f_1_0@0@root_1", "3-423-12445-4"));
      list.add(new StaticData("f_1_0@0@root_3", "Die Insel des vorigen Tages"));
      list.add(new StaticData("f_1_1@0@root_1", "3-423-12445-5"));
      list.add(new StaticData("f_1_1@0@root_3", "Das Foucaltsche Pendel"));
      list.add(new StaticData("ac_copy_1", "Copy"));
      list.add(new StaticData("k_1_0@0@root",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("f_1_ins0@root_2", "1"));
      list.add(new StaticData("k_1_1@0@root",
                                "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
      list.add(new StaticData("f_1_ins0@root_2", "1"));
      list.add(new StaticData("k_0_0@root", "0%3A1%3A1"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("<td>" + nodata + "&nbsp;</td>"));
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_ins0@root_1\" value=\"3-423-12445-4\" />"));
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_ins0@root_3\" value=\"Die Insel des vorigen Tages\" />"));
      assertTrue(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_1"));
      assertTrue(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_1"));
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testNavNewInSubForm() throws Exception {
      List list;

      get("http://localhost/bookstore/tests/testAuthorBooksSubForm.jsp");
      assertTrue(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_1"));
      assertTrue(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_1"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "0"));
      list.add(new StaticData("invname_0", "null"));
      list.add(new StaticData("autoupdate_0", "false"));
      list.add(new StaticData("fu_0", "/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_0",
                                "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
      list.add(new StaticData("lastpos_0",
                                "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
      list.add(new StaticData("f_0_0@root_1", "Eco, Umberto"));
      list.add(new StaticData("f_0_0@root_2", "organisation 11"));
      list.add(new StaticData("invtable", "1"));
      list.add(new StaticData("invname_1", "null"));
      list.add(new StaticData("autoupdate_1", "false"));
      list.add(new StaticData("fu_1", "/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_1",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("lastpos_1",
                                "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
      list.add(new StaticData("f_1_0@0@root_1", "3-423-12445-4"));
      list.add(new StaticData("f_1_0@0@root_3", "Die Insel des vorigen Tages"));
      list.add(new StaticData("f_1_1@0@root_1", "3-423-12445-5"));
      list.add(new StaticData("f_1_1@0@root_3", "Das Foucaltsche Pendel"));
      list.add(new StaticData("ac_new_1", "New"));
      list.add(new StaticData("k_1_0@0@root",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("f_1_ins0@root_2", "1"));
      list.add(new StaticData("k_1_1@0@root",
                                "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
      list.add(new StaticData("f_1_ins0@root_2", "1"));
      list.add(new StaticData("k_0_0@root", "0%3A1%3A1"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_1"));
      assertTrue(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_1"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "0"));
      list.add(new StaticData("invname_0", "null"));
      list.add(new StaticData("autoupdate_0", "false"));
      list.add(new StaticData("fu_0", "/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_0",
                                "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
      list.add(new StaticData("lastpos_0",
                                "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
      list.add(new StaticData("f_0_0@root_1", "Eco, Umberto"));
      list.add(new StaticData("f_0_0@root_2", "organisation 11"));
      list.add(new StaticData("ac_next_0", ">  Next"));
      list.add(new StaticData("invtable", "1"));
      list.add(new StaticData("invname_1", "null"));
      list.add(new StaticData("autoupdate_1", "false"));
      list.add(new StaticData("fu_1", "/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("f_1_ins0@root_1", ""));
      list.add(new StaticData("f_1_ins0@root_3", ""));
      list.add(new StaticData("k_1_0@0@root", "null"));
      list.add(new StaticData("f_1_ins0@root_2", "1"));
      list.add(new StaticData("k_0_0@root", "0%3A1%3A1"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("<input type=\"text\" name=\"f_0_0@root_1\" value=\"Douglas, Adam\"  size=\"25\"/>"));
      assertTrue(responseContains("<input type=\"text\" name=\"f_1_0@0@root_1\" value=\"42-6\" />"));
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testNavNewMain() throws Exception {
      List list;

      get("http://localhost/bookstore/tests/testAuthorBooksSubForm.jsp");
      printResponse();

      list = new ArrayList();
      list.add(new StaticData("invtable", "0"));
      list.add(new StaticData("invname_0", "null"));
      list.add(new StaticData("autoupdate_0", "false"));
      list.add(new StaticData("fu_0", "/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_0",
                                "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
      list.add(new StaticData("lastpos_0",
                                "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
      list.add(new StaticData("f_0_0@root_1", "Eco, Umberto"));
      list.add(new StaticData("f_0_0@root_2", "organisation 11"));
      list.add(new StaticData("ac_next_0", ">  Next"));
      list.add(new StaticData("invtable", "1"));
      list.add(new StaticData("invname_1", "null"));
      list.add(new StaticData("autoupdate_1", "false"));
      list.add(new StaticData("fu_1", "/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_1",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("lastpos_1",
                                "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
      list.add(new StaticData("f_1_0@0@root_1", "3-423-12445-4"));
      list.add(new StaticData("f_1_0@0@root_3", "Die Insel des vorigen Tages"));
      list.add(new StaticData("f_1_1@0@root_1", "3-423-12445-5"));
      list.add(new StaticData("f_1_1@0@root_3", "Das Foucaltsche Pendel"));
      list.add(new StaticData("k_1_0@0@root",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("f_1_ins0@root_2", "1"));
      list.add(new StaticData("k_1_1@0@root",
                                "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
      list.add(new StaticData("f_1_ins0@root_2", "1"));
      list.add(new StaticData("k_0_0@root", "0%3A1%3A1"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();

      list = new ArrayList();
      list.add(new StaticData("invtable", "0"));
      list.add(new StaticData("invname_0", "null"));
      list.add(new StaticData("autoupdate_0", "false"));
      list.add(new StaticData("fu_0", "/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_0",
                                "0%3A1%3A2-1%3A13%3ADouglas%2C+Adam-2%3A14%3Aorganisation+2"));
      list.add(new StaticData("lastpos_0",
                                "0%3A1%3A2-1%3A13%3ADouglas%2C+Adam-2%3A14%3Aorganisation+2"));
      list.add(new StaticData("f_0_0@root_1", "Douglas, Adam"));
      list.add(new StaticData("f_0_0@root_2", "organisation 2"));
      list.add(new StaticData("ac_next_0", ">  Next"));
      list.add(new StaticData("invtable", "1"));
      list.add(new StaticData("invname_1", "null"));
      list.add(new StaticData("autoupdate_1", "false"));
      list.add(new StaticData("fu_1", "/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_1",
                                "0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1"));
      list.add(new StaticData("lastpos_1",
                                "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
      list.add(new StaticData("f_1_0@0@root_1", "42-1"));
      list.add(new StaticData("f_1_0@0@root_3",
                                "Hijacking through the Galaxy 1"));
      list.add(new StaticData("f_1_1@0@root_1", "42-2"));
      list.add(new StaticData("f_1_1@0@root_3",
                                "Hijacking through the Galaxy 2"));
      list.add(new StaticData("f_1_2@0@root_1", "42-3"));
      list.add(new StaticData("f_1_2@0@root_3",
                                "Hijacking through the Galaxy 3"));
      list.add(new StaticData("f_1_3@0@root_1", "42-4"));
      list.add(new StaticData("f_1_3@0@root_3",
                                "Hijacking through the Galaxy 4"));
      list.add(new StaticData("f_1_4@0@root_1", "42-5"));
      list.add(new StaticData("f_1_4@0@root_3",
                                "Luca\'s favorite thing to eat is  "));
      list.add(new StaticData("f_1_5@0@root_1", "42-6"));
      list.add(new StaticData("f_1_5@0@root_3",
                                "Hijacking through the Galaxy 6"));
      list.add(new StaticData("k_1_0@0@root",
                                "0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1"));
      list.add(new StaticData("f_1_ins0@root_2", "2"));
      list.add(new StaticData("k_1_1@0@root",
                                "0%3A1%3A4-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+2"));
      list.add(new StaticData("f_1_ins0@root_2", "2"));
      list.add(new StaticData("k_1_2@0@root",
                                "0%3A1%3A5-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+3"));
      list.add(new StaticData("f_1_ins0@root_2", "2"));
      list.add(new StaticData("k_1_3@0@root",
                                "0%3A1%3A6-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+4"));
      list.add(new StaticData("f_1_ins0@root_2", "2"));
      list.add(new StaticData("k_1_4@0@root",
                                "0%3A1%3A8-2%3A1%3A2-3%3A58%3ALuca%27s+favorite+thing+to+eat+is++%22delicious+Italian+pasta%22"));
      list.add(new StaticData("f_1_ins0@root_2", "2"));
      list.add(new StaticData("k_1_5@0@root",
                                "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
      list.add(new StaticData("f_1_ins0@root_2", "2"));
      list.add(new StaticData("k_0_0@root", "0%3A1%3A2"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_0"));
      assertTrue(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_0"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "0"));
      list.add(new StaticData("invname_0", "null"));
      list.add(new StaticData("autoupdate_0", "false"));
      list.add(new StaticData("fu_0", "/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_0",
                                "0%3A1%3A2-1%3A13%3ADouglas%2C+Adam-2%3A14%3Aorganisation+2"));
      list.add(new StaticData("lastpos_0",
                                "0%3A1%3A2-1%3A13%3ADouglas%2C+Adam-2%3A14%3Aorganisation+2"));
      list.add(new StaticData("f_0_0@root_1", "Douglas, Adam"));
      list.add(new StaticData("f_0_0@root_2", "organisation 2"));
      list.add(new StaticData("ac_new_0", "New"));
      list.add(new StaticData("invtable", "1"));
      list.add(new StaticData("invname_1", "null"));
      list.add(new StaticData("autoupdate_1", "false"));
      list.add(new StaticData("fu_1", "/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_1",
                                "0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1"));
      list.add(new StaticData("lastpos_1",
                                "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
      list.add(new StaticData("f_1_0@0@root_1", "42-1"));
      list.add(new StaticData("f_1_0@0@root_3",
                                "Hijacking through the Galaxy 1"));
      list.add(new StaticData("f_1_1@0@root_1", "42-2"));
      list.add(new StaticData("f_1_1@0@root_3",
                                "Hijacking through the Galaxy 2"));
      list.add(new StaticData("f_1_2@0@root_1", "42-3"));
      list.add(new StaticData("f_1_2@0@root_3",
                                "Hijacking through the Galaxy 3"));
      list.add(new StaticData("f_1_3@0@root_1", "42-4"));
      list.add(new StaticData("f_1_3@0@root_3",
                                "Hijacking through the Galaxy 4"));
      list.add(new StaticData("f_1_4@0@root_1", "42-5"));
      list.add(new StaticData("f_1_4@0@root_3",
                                "Luca\'s favorite thing to eat is  "));
      list.add(new StaticData("f_1_5@0@root_1", "42-6"));
      list.add(new StaticData("f_1_5@0@root_3",
                                "Hijacking through the Galaxy 6"));
      list.add(new StaticData("k_1_0@0@root",
                                "0%3A1%3A3-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+1"));
      list.add(new StaticData("f_1_ins0@root_2", "2"));
      list.add(new StaticData("k_1_1@0@root",
                                "0%3A1%3A4-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+2"));
      list.add(new StaticData("f_1_ins0@root_2", "2"));
      list.add(new StaticData("k_1_2@0@root",
                                "0%3A1%3A5-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+3"));
      list.add(new StaticData("f_1_ins0@root_2", "2"));
      list.add(new StaticData("k_1_3@0@root",
                                "0%3A1%3A6-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+4"));
      list.add(new StaticData("f_1_ins0@root_2", "2"));
      list.add(new StaticData("k_1_4@0@root",
                                "0%3A1%3A8-2%3A1%3A2-3%3A58%3ALuca%27s+favorite+thing+to+eat+is++%22delicious+Italian+pasta%22"));
      list.add(new StaticData("f_1_ins0@root_2", "2"));
      list.add(new StaticData("k_1_5@0@root",
                                "0%3A1%3A9-2%3A1%3A2-3%3A30%3AHijacking+through+the+Galaxy+6"));
      list.add(new StaticData("f_1_ins0@root_2", "2"));
      list.add(new StaticData("k_0_0@root", "0%3A1%3A2"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertFalse(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_0"));
      assertFalse(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_0"));
      assertTrue(responseContains("<td style=\"width:100px\">" + nodata
                                  + "&nbsp;</td>"));
      assertTrue(responseContains("<input type=\"text\" name=\"f_0_insroot_1\" value=\"\"  size=\"25\"/>"));
      assertTrue(responseContains("<input type=\"text\" name=\"f_0_insroot_2\" value=\"\"  size=\"25\"/>"));
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testNavNewSub() throws Exception {
      List list;

      get("http://localhost/bookstore/tests/testAuthorBooksSubForm.jsp");
      printResponse();
      assertTrue(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_1"));
      assertTrue(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_1"));

      list = new ArrayList();
      list.add(new StaticData("invtable", "0"));
      list.add(new StaticData("invname_0", "null"));
      list.add(new StaticData("autoupdate_0", "false"));
      list.add(new StaticData("fu_0", "/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_0",
                                "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
      list.add(new StaticData("lastpos_0",
                                "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+11"));
      list.add(new StaticData("f_0_0@root_1", "Eco, Umberto"));
      list.add(new StaticData("f_0_0@root_2", "organisation 11"));
      list.add(new StaticData("invtable", "1"));
      list.add(new StaticData("invname_1", "null"));
      list.add(new StaticData("autoupdate_1", "false"));
      list.add(new StaticData("fu_1", "/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("source",
                                "/bookstore/tests/testAuthorBooksSubForm.jsp"));
      list.add(new StaticData("customEvent", ""));
      list.add(new StaticData("firstpos_1",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("lastpos_1",
                                "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
      list.add(new StaticData("f_1_0@0@root_1", "3-423-12445-4"));
      list.add(new StaticData("f_1_0@0@root_3", "Die Insel des vorigen Tages"));
      list.add(new StaticData("f_1_1@0@root_1", "3-423-12445-5"));
      list.add(new StaticData("f_1_1@0@root_3", "Das Foucaltsche Pendel"));
      list.add(new StaticData("ac_new_1", "New"));
      list.add(new StaticData("k_1_0@0@root",
                                "0%3A1%3A1-2%3A1%3A1-3%3A27%3ADie+Insel+des+vorigen+Tages"));
      list.add(new StaticData("f_1_ins0@root_2", "1"));
      list.add(new StaticData("k_1_1@0@root",
                                "0%3A1%3A2-2%3A1%3A1-3%3A22%3ADas+Foucaltsche+Pendel"));
      list.add(new StaticData("f_1_ins0@root_2", "1"));
      list.add(new StaticData("k_0_0@root", "0%3A1%3A1"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("<input type=\"submit\"  value=\"New\"  style=\"width:100\" name=\"ac_new_1"));
      assertTrue(responseContains("<input type=\"submit\"  value=\"Copy\"  style=\"width:100\" name=\"ac_copy_1"));
   }
}
