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
package org.dbforms.util;

import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.xml.sax.Attributes;



/**
 *  ObjectCreationFactory used by Digester to return the instance
 *  of a singleton class.
 *
 * @author  Luca Fossato
 * @created  21 november 2002
 */
public class SingletonClassFactoryCreate extends AbstractObjectCreationFactory {
   /** logging category */
   private static Log logCat = LogFactory.getLog(SingletonClassFactoryCreate.class
                                                 .getName());

   /** full qualified name of the singleton class to instance */
   private String className = null;

   /**
    *  EventFactoryCreate constructor.
    *
    * @param  className the name of the singleton class to instance
    */
   public SingletonClassFactoryCreate(String className) {
      super();
      setClassName(className);
   }

   /**
    *  Sets the className attribute of the EventFactoryCreate object
    *
    * @param  className The new className value
    */
   public void setClassName(String className) {
      this.className = className;
   }


   /**
    *  Gets the className attribute of the EventFactoryCreate object
    *
    * @return  The className value
    */
   public String getClassName() {
      return className;
   }


   /**
    *  Get the unique instance of the Singleton class.
    *
    * @param  attributes not used
    * @return  the unique instance of the singleton class
    *         specified by the <code>className</code> member attribute
    */
   public Object createObject(Attributes attributes) {
      Object obj = null;

      try {
         obj = ReflectionUtil.invoke(className, "instance", null, null);
      } catch (Exception e) {
         logCat.error("::createObject - cannot instance the class ["
                      + className + "]", e);
      }

      return obj;
   }
}
