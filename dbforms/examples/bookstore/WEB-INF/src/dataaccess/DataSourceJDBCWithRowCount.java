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

package dataaccess;

import org.dbforms.config.ResultSetVector;

import org.dbforms.event.datalist.dao.DataSourceJDBC;
import org.dbforms.interfaces.DbEventInterceptorData;

import java.sql.SQLException;


public class DataSourceJDBCWithRowCount extends DataSourceJDBC {

   
   public DataSourceJDBCWithRowCount() {
   	   super();
   	   this.setCalcRowCount(true);
   }
   /**
    * DOCUMENT ME!
    *
    * @param interceptorData DOCUMENT ME!
    * @param startRow DOCUMENT ME!
    * @param count DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws SQLException DOCUMENT ME!
    */
   protected ResultSetVector getResultSetVector(DbEventInterceptorData interceptorData,
                                                int                    startRow,
                                                int                    count)
                                         throws SQLException {
      ResultSetVector res = super.getResultSetVector(interceptorData, startRow,
                                                     count);
      res.setAttribute("ROWCOUNTALL", new Integer(getRowCount()));

      return res;
   }


}
