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

import java.util.List;
import java.util.ArrayList;

import org.dbforms.interfaces.StaticData;
import org.dbforms.util.AbstractHttpTestCase;

// definition of test class
public class TestValidationServer extends AbstractHttpTestCase {
    // Test method generated from the MaxQ Java generator
    public TestValidationServer(String name) {
        super(name);
    }
    public void testTestValidationServer() throws Exception {
        List list;
        println("TestValidationServer");
        get("http://localhost/bookstore/tests/testAuthorEditWithValidationServer.jsp");
        printResponse();
        assertTrue(responseContains("<td>1&nbsp;</td>"));
        assertTrue(responseContains("Eco, Umberto"));

        list = new ArrayList();
        list.add(new StaticData("invtable", "0"));
        list.add(new StaticData("invname_0", ""));
        list.add(new StaticData("autoupdate_0", "TRUE"));
        list.add(new StaticData("fu_0", "/tests/testAuthorEditWithValidationServer.jsp"));
        list.add(new StaticData("lang", "de"));
        list.add(new StaticData("country", ""));
        list.add(new StaticData("formValidatorName_0", "AUTHOR"));
        list.add(new StaticData("source", "/bookstore/tests/testAuthorEditWithValidationServer.jsp"));
        list.add(new StaticData("customEvent", ""));
        list.add(new StaticData("firstpos_0", "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+12"));
        list.add(new StaticData("lastpos_0", "0%3A1%3A1-1%3A12%3AEco%2C+Umberto-2%3A15%3Aorganisation+12"));
        list.add(new StaticData("f_0_0@root_1", "Eco, Umberto"));
        list.add(new StaticData("of_0_0@root_1", "Eco, Umberto"));
        list.add(new StaticData("f_0_0@root_2", "organisation 12"));
        list.add(new StaticData("of_0_0@root_2", "organisation 12"));
        list.add(new StaticData("ac_next_0_4", ">  Next"));
        list.add(new StaticData("k_0_0@root", "0%3A1%3A1"));
        post("http://localhost/bookstore/servlet/control;jsessionid=3DA69BC699D0A32C8E88C7159A1FC806", list);
        printResponse();
        assertTrue(responseContains("<td>2&nbsp;</td>"));
        assertTrue(responseContains("Douglas, Adam"));
        assertTrue(responseContains("organisation 2"));

        
        list = new ArrayList();
        list.add(new StaticData("invtable", "0"));
        list.add(new StaticData("invname_0", ""));
        list.add(new StaticData("autoupdate_0", "TRUE"));
        list.add(new StaticData("fu_0", "/tests/testAuthorEditWithValidationServer.jsp"));
        list.add(new StaticData("lang", "de"));
        list.add(new StaticData("country", ""));
        list.add(new StaticData("formValidatorName_0", "AUTHOR"));
        list.add(new StaticData("source", "/bookstore/tests/testAuthorEditWithValidationServer.jsp"));
        list.add(new StaticData("customEvent", ""));
        list.add(new StaticData("firstpos_0", "0%3A1%3A2-1%3A13%3ADouglas%2C+Adam-2%3A14%3Aorganisation+2"));
        list.add(new StaticData("lastpos_0", "0%3A1%3A2-1%3A13%3ADouglas%2C+Adam-2%3A14%3Aorganisation+2"));
        list.add(new StaticData("f_0_0@root_1", ""));
        list.add(new StaticData("of_0_0@root_1", "Douglas, Adam"));
        list.add(new StaticData("f_0_0@root_2", "organisation 2"));
        list.add(new StaticData("of_0_0@root_2", "organisation 2"));
        list.add(new StaticData("ac_next_0_10", ">  Next"));
        list.add(new StaticData("k_0_0@root", "0%3A1%3A2"));
        post("http://localhost/bookstore/servlet/control", list);
        printResponse();
        assertTrue(responseContains("<td>2&nbsp;</td>"));
        assertTrue(responseContains("field name required:NAME"));
        assertTrue(responseContains("organisation 2"));
        

    }

}
