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

import org.dbforms.util.StringUtil;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;



/**
 * <p>
 * This class represents a "granted-privileges"-tag in dbforms-config.xml
 * </p>
 *
 * @author Joachim Peer
 */
public class GrantedPrivileges {
   private static Log logCat = LogFactory.getLog(GrantedPrivileges.class
                                                 .getName()); // logging category for this class

   /** DOCUMENT ME! */
   public static final int PRIVILEG_SELECT = 0;

   /** DOCUMENT ME! */
   public static final int PRIVILEG_INSERT = 1;

   /** DOCUMENT ME! */
   public static final int PRIVILEG_UPDATE = 2;

   /** DOCUMENT ME! */
   public static final int PRIVILEG_DELETE = 3;
   private Vector[]        grantedRoles;

   /**
    * Creates a new GrantedPrivileges object.
    */
   public GrantedPrivileges() {
      grantedRoles = new Vector[4];

      //conditions = new Hashtable();
   }

   /**
    * DOCUMENT ME!
    *
    * @param delete DOCUMENT ME!
    */
   public void setDelete(String delete) {
      logCat.info("delete");
      grantedRoles[PRIVILEG_DELETE] = StringUtil.splitString(delete, ",;~");
   }


   /**
    * DOCUMENT ME!
    *
    * @param insert DOCUMENT ME!
    */
   public void setInsert(String insert) {
      logCat.info("insert");
      grantedRoles[PRIVILEG_INSERT] = StringUtil.splitString(insert, ",;~");
   }


   /**
    * DOCUMENT ME!
    *
    * @param select DOCUMENT ME!
    */
   public void setSelect(String select) {
      logCat.info("select");
      grantedRoles[PRIVILEG_SELECT] = StringUtil.splitString(select, ",;~");
   }


   /**
    * DOCUMENT ME!
    *
    * @param update DOCUMENT ME!
    */
   public void setUpdate(String update) {
      logCat.info("update");
      grantedRoles[PRIVILEG_UPDATE] = StringUtil.splitString(update, ",;~");
   }


   /**
    * DOCUMENT ME!
    *
    * @param request DOCUMENT ME!
    * @param privileg DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public boolean hasUserPrivileg(HttpServletRequest request,
                                  int                privileg) {
      if (grantedRoles[privileg] == null) {
         return true; // if no constraints specified -> wildcard access ;-)
      }

      for (int i = 0; i < grantedRoles[privileg].size(); i++) {
         String aGrantedRole = (String) grantedRoles[privileg].elementAt(i);

         if (request.isUserInRole(aGrantedRole)) {
            return true; // if the user is InRole(aGrantedRole) then let him go on :=)
         }
      }

      return false; // otherwise we must deny the operation
   }
}
