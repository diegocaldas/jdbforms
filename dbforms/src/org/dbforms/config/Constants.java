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
 *
 * New class to hold all the different constant values spread around dbForms!
 *
 * @author hkk
 */
public class Constants {
   public static final int DATE_STYLE_DEFAULT = java.text.DateFormat.MEDIUM;
   public static final int TIME_STYLE_DEFAULT = java.text.DateFormat.SHORT;
   
   public static final String FIELDNAME_PREFIX = "f_";

   public static final String FIELDNAME_OLDVALUETAG = "o";

   public static final String FIELDNAME_PATTERNTAG = "p";

   /** DOCUMENT ME! */
   public static final String FIELDNAME_INSERTPREFIX = "ins";
   
   public static final String FIELDNAME_OVERRIDEFIELDTEST = "overridefieldcheck_";

   /** DOCUMENT ME! */
   public static final int COMPARE_NONE = 0;

   /** DOCUMENT ME! */
   public static final int COMPARE_INCLUSIVE = 1;

   /** DOCUMENT ME! */
   public static final int COMPARE_EXCLUSIVE = 2;

   /** DOCUMENT ME! */
   public static final int SEARCHMODE_NONE = 0;

   /** DOCUMENT ME! */
   public static final int SEARCHMODE_AND = 1;

   /** DOCUMENT ME! */
   public static final int SEARCHMODE_OR = 2;

   /** DOCUMENT ME! */
   public static final int SEARCH_ALGO_SHARP = 0;

   /** DOCUMENT ME! */
   public static final int SEARCH_ALGO_WEAK = 1;

   /** DOCUMENT ME! */
   public static final int SEARCH_ALGO_WEAK_START = 2;

   /** DOCUMENT ME! */
   public static final int SEARCH_ALGO_WEAK_END = 3;

   /** DOCUMENT ME! */
   public static final int SEARCH_ALGO_WEAK_START_END = 4;

   /** DOCUMENT ME! */
   public static final int SEARCH_ALGO_EXTENDED = 5;

   /** DOCUMENT ME! */
   public static final int FILTER_EQUAL = 0;

   /** DOCUMENT ME! */
   public static final int FILTER_GREATER_THEN = 1;

   /** DOCUMENT ME! */
   public static final int FILTER_GREATER_THEN_EQUAL = 3;

   /** DOCUMENT ME! */
   public static final int FILTER_SMALLER_THEN = 2;

   /** DOCUMENT ME! */
   public static final int FILTER_SMALLER_THEN_EQUAL = 4;

   /** DOCUMENT ME! */
   public static final int FILTER_LIKE = 5;

   /** DOCUMENT ME! */
   public static final int FILTER_NOT_EQUAL = 6;

   /** DOCUMENT ME! */
   public static final int FILTER_NULL = 7;

   /** DOCUMENT ME! */
   public static final int FILTER_NOT_NULL = 8;

   /** DOCUMENT ME! */
   public static final int FILTER_EMPTY = 9;

   /** DOCUMENT ME! */
   public static final int FILTER_NOT_EMPTY = 10;

   /** DOCUMENT ME! */
   public static final int ORDER_NONE = -1;

   /** DOCUMENT ME! */
   public static final int ORDER_ASCENDING = 0;

   /** DOCUMENT ME! */
   public static final int ORDER_DESCENDING = 1;
}