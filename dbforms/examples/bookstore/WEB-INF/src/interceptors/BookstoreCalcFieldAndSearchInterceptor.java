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
package interceptors;
import org.dbforms.config.MultipleValidationException;
import org.dbforms.config.ResultSetVector;
import org.dbforms.config.ValidationException;

import org.dbforms.event.DbEventInterceptorSupport;
import org.dbforms.interfaces.DbEventInterceptorData;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class BookstoreCalcFieldAndSearchInterceptor
   extends DbEventInterceptorSupport {
   /**
    * DOCUMENT ME!
    *
    * @param data DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    * @throws MultipleValidationException DOCUMENT ME!
    */
   public int preAddRow(DbEventInterceptorData data)
      throws ValidationException, MultipleValidationException {
       ResultSetVector rsv          = (ResultSetVector) data.getAttribute(DbEventInterceptorData.RESULTSET);
       Object[]        row          = (Object[]) data.getAttribute(DbEventInterceptorData.OBJECTROW);
       int             colISBN      = rsv.getFieldIndex("ISBN");
       int res;
       if (row[colISBN] == null) {
       	  res = IGNORE_OPERATION;
       } else {
    	  res = GRANT_OPERATION;
       }
       return res;
   }


   /**
    * DOCUMENT ME!
    *
    * @param data DOCUMENT ME!
    *
    * @throws ValidationException DOCUMENT ME!
    * @throws MultipleValidationException DOCUMENT ME!
    */
   public void postAddRow(DbEventInterceptorData data) {
      ResultSetVector rsv          = (ResultSetVector) data.getAttribute(DbEventInterceptorData.RESULTSET);
      Object[]        row          = (Object[]) data.getAttribute(DbEventInterceptorData.OBJECTROW);
      int             colISBN      = rsv.getFieldIndex("ISBN");
      int             colTITEL     = rsv.getFieldIndex("TITLE");
      int             colSBN_TITEL = rsv.getFieldIndex("ISBN_TITLE");
      row[colSBN_TITEL] = "+-" + row[colISBN] + "-CALC-" + row[colTITEL] + "-+";

      int     colROWNUM = rsv.getFieldIndex("ROW_NUM");
      Integer oRowNum = (Integer) data.getAttribute("ROWNUM");
      int     rowNum  = 0;

      if (oRowNum != null) {
         rowNum = oRowNum.intValue();
      }

      rowNum++;
      oRowNum = new Integer(rowNum);
      data.setAttribute("ROWNUM", oRowNum);
      row[colROWNUM] = oRowNum;
   }
}
