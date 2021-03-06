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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.util.Util;

import java.io.Serializable;
import java.util.Properties;



/**
 * Event information class
 *
 * @author Luca Fossato
 *
 */
public class EventInfo implements Serializable{
   /** logging category */
   private static Log        logCat     = LogFactory.getLog(EventInfo.class.getName());
   private Properties properties = null;
   private String     className  = null;
   private String     id         = null;
   private String     type       = null;

   /**
    * Default constructor.
    */
   public EventInfo() {
      properties = new Properties();
   }


   /**
    * Constructor.
    *
    * @param type the event type
    * @param className the full qualified bname of the event class
    */
   public EventInfo(String type,
                    String className) {
      this();
      this.type      = type;
      this.className = className;
   }

   /**
    * Sets the className attribute of the EventInfo object
    *
    * @param className The new className value
    */
   public void setClassName(String className) {
      this.className = className;
   }


   /**
    * Gets the className attribute of the EventInfo object
    *
    * @return The className value
    */
   public String getClassName() {
      return className;
   }


   /**
    * Sets the id attribute of the EventInfo object
    *
    * @param id The new id value
    */
   public void setId(String id) {
      this.id = id;
   }


   /**
    * Gets the id attribute of the EventInfo object
    *
    * @return The id value
    */
   public String getId() {
      //return id;
      return (!Util.isNull(id)) ? id
                                : type;
   }


   /**
    * Gets the properties attribute of the EventInfo object
    *
    * @return The properties value
    */
   public Properties getProperties() {
      return properties;
   }


   /**
    * Sets the type attribute of the EventInfo object
    *
    * @param type The new type value
    */
   public void setType(String type) {
      this.type = type;
   }


   /**
    * Gets the type attribute of the EventInfo object
    *
    * @return The type value
    */
   public String getType() {
      return type;
   }


   /**
    * Adds a new property to this EventInfo object.
    *
    * @param property The feature to be added to the Property attribute
    */
   public void addProperty(String name, String value) {
      properties.put(name, value);
      logCat.info("::addProperty - added the property [" + name + ", " + value
                  + "] to event [" + getId() + "]");
   }


   /**
    * Return the String representation of this object.
    *
    * @return the String representation of this object
    */
   public String toString() {
      return new StringBuffer("event: id = ").append(getId())
                                             .append("; type = ")
                                             .append(type)
                                             .append("; className = ")
                                             .append(className)
                                             .toString();
   }
}
