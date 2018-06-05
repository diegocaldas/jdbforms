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

package org.dbforms.taglib;

import org.dbforms.config.ResultSetVector;

import java.util.Map;



/**
 * This is the context of the dbform tag. Used as TEI class element. In the
 * pagecontext is holded a map named dbform with name of form as  key and this
 * context as object.
 *
 * @author Henner Kollmann
 */
public class DbFormContext {
   private Map             currentRow;
   private Map             searchFieldAlgorithmNames;
   private Map             searchFieldModeNames;
   private Map             searchFieldNames;
   private ResultSetVector rsv;
   private String          position;

   /**
    * Creates a new DbFormContext object.
    *
    * @param searchFieldNames DOCUMENT ME!
    * @param searchFieldModeNames DOCUMENT ME!
    * @param searchFieldAlgorithmNames DOCUMENT ME!
    * @param rsv DOCUMENT ME!
    */
   public DbFormContext(Map             searchFieldNames,
                        Map             searchFieldModeNames,
                        Map             searchFieldAlgorithmNames,
                        ResultSetVector rsv) {
      this.searchFieldNames          = searchFieldNames;
      this.searchFieldModeNames      = searchFieldModeNames;
      this.searchFieldAlgorithmNames = searchFieldAlgorithmNames;
      this.rsv                       = rsv;
   }

   /**
    * DOCUMENT ME!
    *
    * @param map
    */
   public void setCurrentRow(Map map) {
      currentRow = map;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public Map getCurrentRow() {
      return currentRow;
   }


   /**
    * DOCUMENT ME!
    *
    * @param string
    */
   public void setPosition(String string) {
      position = string;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public String getPosition() {
      return position;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public ResultSetVector getRsv() {
      return rsv;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public Map getSearchFieldAlgorithmNames() {
      return searchFieldAlgorithmNames;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public Map getSearchFieldModeNames() {
      return searchFieldModeNames;
   }


   /**
    * DOCUMENT ME!
    *
    * @return
    */
   public Map getSearchFieldNames() {
      return searchFieldNames;
   }
}
