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

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.FieldValues;
import org.dbforms.config.Table;
import org.dbforms.config.ValidationException;

import org.dbforms.event.DbEventInterceptorSupport;

import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;





/**
 * @author hkk
 *
 * To change this generated comment go to Window>Preferences>Java>Code
 * Generation>Code and Comments
 */
public class InterceptorTestClassic extends DbEventInterceptorSupport {
    public static boolean preInsertCalled = false;
    public static boolean preSelectCalled = false;
    public static boolean preUpdateCalled = false;
    public static boolean preDeleteCalled = false;
    public static boolean postUpdateCalled = false;
    public static boolean postSelectCalled = false;
    public static boolean postInsertCalled = false;
    public static boolean postDeleteCalled = false;

	/* (non-Javadoc)
	 * @see org.dbforms.event.DbEventInterceptorSupport#postDelete(javax.servlet.http.HttpServletRequest, org.dbforms.config.DbFormsConfig, java.sql.Connection)
	 */
	public void postDelete(HttpServletRequest request, DbFormsConfig config,
			Connection con) {
		postDeleteCalled = true;
	}

	public void postInsert(HttpServletRequest request, DbFormsConfig config,
			Connection con) {
        postInsertCalled = true;
	}

	public void postSelect(HttpServletRequest request, DbFormsConfig config,
			Connection con) {
        postSelectCalled = true;
}

	public void postUpdate(HttpServletRequest request, DbFormsConfig config,
			Connection con) {
        postUpdateCalled = true;
	}

	public int preDelete(HttpServletRequest request, Table table,
			FieldValues fieldValues, DbFormsConfig config, Connection con)
			throws ValidationException {
        preDeleteCalled = true;
    	return GRANT_OPERATION;
	}

	public int preInsert(HttpServletRequest request,
                        Table              table,
                        FieldValues        fieldValues,
                        DbFormsConfig      config,
                        Connection         con) throws ValidationException {
        preInsertCalled = true;
    	return GRANT_OPERATION;
   }

   public int preSelect(HttpServletRequest request,
                        DbFormsConfig      config,
                        Connection         con) {
       preSelectCalled = true;
      return GRANT_OPERATION;
   }


   public int preUpdate(HttpServletRequest request,
                        Table              table,
                        FieldValues        fieldValues,
                        DbFormsConfig      config,
                        Connection         con) throws ValidationException {
        preUpdateCalled = true;
        return GRANT_OPERATION;
      }
}
