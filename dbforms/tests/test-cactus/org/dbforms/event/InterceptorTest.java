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
package org.dbforms.event;

import org.dbforms.config.MultipleValidationException;
import org.dbforms.config.ValidationException;
import org.dbforms.config.ResultSetVector;
import org.dbforms.interfaces.DbEventInterceptorData;

public class InterceptorTest extends DbEventInterceptorSupport {
   public int preAddRow(DbEventInterceptorData data)
                 throws ValidationException, MultipleValidationException {
      ResultSetVector rsv = (ResultSetVector) data.getAttribute(DbEventInterceptorData.RESULTSET);
      Object[] row = (Object []) data.getAttribute(DbEventInterceptorData.OBJECTROW);  
      int colISBN = rsv.getFieldIndex("ISBN");
      int colTITEL = rsv.getFieldIndex("TITLE");
      int colSBN_TITEL = rsv.getFieldIndex("ISBN_TITLE");
      row[colSBN_TITEL] = createTestText(row[colISBN], row[colTITEL]);
      return GRANT_OPERATION;
   }

   static String createTestText(Object s1, Object s2) {
         return s1 + "---" + s2;
   }
}
