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
/*
 * Created on 03.08.2003
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.dbforms.util;

import java.util.Locale;



/**
 * @author hkk
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestMessageResourcesInternal extends AbstractTestCase {
   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testMessageResourceInternal() throws Exception {
      MessageResources.setSubClass("resources");

      String s = MessageResourcesInternal.getMessage("dbforms.testmessage",
                                                     Locale.ENGLISH);
      assertTrue(s.equals("this is a test"));
      s = MessageResourcesInternal.getMessage("dbforms.testmessage",
                                              Locale.GERMAN);
      assertTrue(s.equals("dies ist ein test"));
   }
}
