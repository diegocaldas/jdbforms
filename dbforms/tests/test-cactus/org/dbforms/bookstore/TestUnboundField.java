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

import org.dbforms.util.AbstractHttpTestCase;
import org.dbforms.util.KeyValuePair;
import org.dbforms.util.MessageResources;
import org.dbforms.util.MessageResourcesInternal;

import java.util.*;



// definition of test class
public class TestUnboundField extends AbstractHttpTestCase {
   private static String nodata = null;

   /**
    * Creates a new TestUnboundField object.
    *
    * @param name DOCUMENT ME!
    */
   public TestUnboundField(String name) {
      super(name);

      if (nodata == null) {
         MessageResources.setSubClass("resources");
         nodata = MessageResourcesInternal.getMessage("dbforms.nodata",
                                                      Locale.getDefault());
      }
   }

   // Test method generated from the MaxQ Java generator
   public void testUnbounded() throws Exception {
      List list;

      get("http://localhost/bookstore/tests/testUnboundField.jsp");
      printResponse();
      assertTrue(responseContains("name=\"UNBOUNDED\" value=\"" + nodata + "\""));
      assertTrue(responseContains("name=\"f_0_0@root_1\" value=\"Eco, Umberto\""));

      list = new ArrayList();
      list.add(new KeyValuePair("invtable", "0"));
      list.add(new KeyValuePair("invname_0", ""));
      list.add(new KeyValuePair("autoupdate_0", "false"));
      list.add(new KeyValuePair("fu_0", "/tests/testUnboundField.jsp"));
      list.add(new KeyValuePair("lang", "de"));
      list.add(new KeyValuePair("source",
                                "/bookstore/tests/testUnboundField.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_0", "0%3A1%3A1"));
      list.add(new KeyValuePair("lastpos_0", "0%3A1%3A1"));
      list.add(new KeyValuePair("f_0_0@root_1", "Eco, Umberto"));
      list.add(new KeyValuePair("of_0_0@root_1", "Eco, Umberto"));
      list.add(new KeyValuePair("f_0_0@root_2", "organisation 11"));
      list.add(new KeyValuePair("of_0_0@root_2", "organisation 11"));
      list.add(new KeyValuePair("UNBOUNDED", "[NULL]"));
      list.add(new KeyValuePair("oUNBOUNDED", "[NULL]"));
      list.add(new KeyValuePair("ac_next_0_46", ">  Next"));
      list.add(new KeyValuePair("k_0_0@root", "0%3A1%3A1"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("name=\"UNBOUNDED\" value=\"" + nodata + "\""));
      assertTrue(responseContains("name=\"f_0_0@root_1\" value=\"Douglas, Adam\""));

      list = new ArrayList();
      list.add(new KeyValuePair("invtable", "0"));
      list.add(new KeyValuePair("invname_0", ""));
      list.add(new KeyValuePair("autoupdate_0", "false"));
      list.add(new KeyValuePair("fu_0", "/tests/testUnboundField.jsp"));
      list.add(new KeyValuePair("lang", "de"));
      list.add(new KeyValuePair("source",
                                "/bookstore/tests/testUnboundField.jsp"));
      list.add(new KeyValuePair("customEvent", ""));
      list.add(new KeyValuePair("firstpos_0", "0%3A1%3A2"));
      list.add(new KeyValuePair("lastpos_0", "0%3A1%3A2"));
      list.add(new KeyValuePair("f_0_0@root_1", "Douglas, Adam"));
      list.add(new KeyValuePair("of_0_0@root_1", "Douglas, Adam"));
      list.add(new KeyValuePair("f_0_0@root_2", "organisation 2"));
      list.add(new KeyValuePair("of_0_0@root_2", "organisation 2"));
      list.add(new KeyValuePair("UNBOUNDED", "[NULL]"));
      list.add(new KeyValuePair("oUNBOUNDED", "[NULL]"));
      list.add(new KeyValuePair("ac_next_0_52", ">  Next"));
      list.add(new KeyValuePair("k_0_0@root", "0%3A1%3A2"));
      post("http://localhost/bookstore/servlet/control", list);
      printResponse();
      assertTrue(responseContains("name=\"UNBOUNDED\" value=\"" + nodata + "\""));
      assertTrue(responseContains("name=\"f_0_0@root_1\" value=\"Douglas, Adam\""));
   }
}
