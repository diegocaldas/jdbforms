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


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class Reference {
   /** Holds value of property foreign. */
   private String foreign;

   /** Holds value of property local. */
   private String local;

   /**
    * Creates a new instance of ReferenceColumnPair
    */
   public Reference() {
   }


   /**
    * Creates a new Reference object.
    *
    * @param local DOCUMENT ME!
    * @param foreign DOCUMENT ME!
    */
   public Reference(String local,
                    String foreign) {
      this.local   = local;
      this.foreign = foreign;
   }

   /**
    * Setter for property foreign.
    *
    * @param foreign New value of property foreign.
    */
   public void setForeign(String foreign) {
      this.foreign = foreign;
   }


   /**
    * Getter for property foreign.
    *
    * @return Value of property foreign.
    */
   public String getForeign() {
      return this.foreign;
   }


   /**
    * Setter for property local.
    *
    * @param local New value of property local.
    */
   public void setLocal(String local) {
      this.local = local;
   }


   /**
    * Getter for property local.
    *
    * @return Value of property local.
    */
   public String getLocal() {
      return this.local;
   }
}
