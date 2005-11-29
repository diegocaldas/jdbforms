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
 * Foundation, Inc., 59 TemplePlace, Suite 330, Boston, MA  02111-1307 USA
 */

package interceptors;

import java.util.Map;

import javax.servlet.jsp.PageContext;

import org.dbforms.interfaces.DbEventInterceptorData;
import org.dbforms.interfaces.IPresetFormValues;

public class BookStoreFormInterceptor implements IPresetFormValues {

	public void presetFormValues(Map properties, DbEventInterceptorData data) {
		PageContext pageContext = (PageContext) data
				.getAttribute(DbEventInterceptorData.PAGECONTEXT);
		StringBuffer s = new StringBuffer();
		s.append(properties.toString());
		try {
			pageContext.getOut().append(s.toString());
		} catch (Exception e) {
		}
	}

}
