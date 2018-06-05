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

import java.io.Serializable;
import java.rmi.MarshalledObject;


public class TestSerializable  extends org.dbforms.util.AbstractTestCase {

	
    private void  doIt(Serializable obj)  {
       assertNotNull(obj);
       MarshalledObject mo = null;
       try {
          mo = new MarshalledObject(obj);
       } catch (Exception e) {
          assertTrue(obj.getClass().getName() + " " +e.getMessage(), false);	
       }
       assertNotNull(obj.getClass().getName(), mo);
       long i = mo.hashCode();
       assertTrue(obj.getClass().getName(), i != 0);
    }

    public void testSerializable() throws Exception {
    	doIt(new Field());
    	doIt(new Table());
    	doIt(new ResultSetVector());
    }

}
