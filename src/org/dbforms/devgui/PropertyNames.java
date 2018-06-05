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

package org.dbforms.devgui;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public interface PropertyNames {
   // set some constant Strings to avoid typos....
   static final String ALL         = "all";
   static final String SELECTION   = "selection";
   static final String TRUESTRING  = "true";
   static final String FALSESTRING = "false";

   // following are property names:
   static final String CATALOG_SELECTION       = "catalogSelection";
   static final String SCHEMA_SELECTION        = "schemaSelection";
   static final String TABLE_SELECTION         = "tableSelection";
   static final String INCLUDE_CATALOGNAME     = "includeCatalogName";
   static final String INCLUDE_SCHEMANAME      = "includeSchemaName";
   static final String AUTOCOMMIT_MODE         = "autoCommitMode";
   static final String EXAMINE_TABLES          = "examineTables";
   static final String EXAMINE_VIEWS           = "examineViews";
   static final String EXAMINE_SYSTABS         = "examineSysTabs";
   static final String CATALOG                 = "catalog";
   static final String SCHEMA                  = "schema";
   static final String TABLE_NAME_PATTERN      = "tableNamePattern";
   static final String WRITE_STD_TYPENAMES     = "writeStdTypeNames";
   static final String USE_JAVASCRIPT_CALENDAR = "useJavaScriptCalendar";
   static final String FOREIGNKEY_DETECTION    = "foreignKeyDetection";
   static final String USE_GETIMPORTEDKEYS     = "useGetImportedKeys";
   static final String USE_GETCROSSREFERENCES  = "useGetCrossReferences";
   static final String DEACTIVATED             = "deactivated";
   static final String CONFIG_FILE             = "configFile";
   static final String STYLESHEET_DIR          = "stylesheetDir";
   static final String XSLT_ENCODING           = "xsltEncoding";
   static final String DATE_FORMAT             = "dateFormat";
}
