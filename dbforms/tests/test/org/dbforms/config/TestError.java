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

import org.dbforms.util.AbstractTestCase;



/**
 *  Description of the Class
 *
 * @author     epugh
 * @created    May 3, 2002
 */
public class TestError extends AbstractTestCase {
   private Error e = null;

   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void setUp() throws Exception {
      super.setUp();
      e = new Error();
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testAddMessage() throws Exception {
      e.setMessage("EN", "EN Error");
      assertTrue("Make sure we don't have a message", e.getMessage("GB") == null);

      Message m = new Message();
      m.setMessage("GB Error");
      m.setLanguage("GB");
      e.addMessage(m);
      assertTrue("Make sure we get back appropriate message for lang",
                 e.getMessage("GB").equals("GB Error"));
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testGetSetID() throws Exception {
      e.setId("ERR_01");
      assertTrue("ID should be ERR_01", e.getId().equals("ERR_01"));
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testSetMessage() throws Exception {
      e.setMessage("EN", "EN Error");
      e.setMessage("FR", "FR Error");
      assertTrue("Make sure we get back appropriate message for lang",
                 e.getMessage("FR").equals("FR Error"));
      assertTrue("Make sure we get back appropriate message for lang",
                 e.getMessage("EN").equals("EN Error"));
   }
}
