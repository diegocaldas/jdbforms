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

package org.dbforms.dom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.interfaces.IDOMFactory;

import org.dbforms.util.ReflectionUtil;


/**
 * abstract class to hide the implemtation details of the various dom
 * implementations.
 *
 * @author Henner Kollmann
 */
public class DOMFactory {
   private static final ThreadLocal singlePerThread = new ThreadLocal();
   private static Log               logCat = LogFactory.getLog(DOMFactory.class
                                                               .getName());
   private static String            factoryClass = "org.dbforms.dom.DOMFactoryXALANImpl";

   /**
    * Creates a new DOMFactory object.
    */
   private DOMFactory() {
   }

   /**
    * Get the thread singelton instance
    *
    * @return a DOMFactory instance per thread
    */
   public static IDOMFactory instance() {
      IDOMFactory fact = (IDOMFactory) singlePerThread.get();
      if (fact == null) {
         try {
            fact = (IDOMFactory) ReflectionUtil.newInstance(factoryClass);
         } catch (Exception e) {
            logCat.error("instance", e);
         }

         if (fact == null) {
            fact = new DOMFactoryXALANImpl();
         }
         singlePerThread.set(fact);
      }
      return fact;
   }

   /**
    * Sets another dom factory to use.
    * Must be called before the first call to instance()!!
    *
    * @param string
    */
   public static void setFactoryClass(String string) {
      singlePerThread.set(null);
      factoryClass = string;
   }
   
}
