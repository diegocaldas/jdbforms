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
import java.util.Iterator;

import org.dbforms.config.Field;

import org.dbforms.interfaces.IPresetFormValues;
import org.dbforms.interfaces.DbEventInterceptorData;

public class HowtoUsePresetFiltersInterceptor implements IPresetFormValues {

	
	public void presetFormValues(Map properties, DbEventInterceptorData data) {
		if (data.getRequest().getAttribute("webEvent") == null) {
			Iterator iter = properties.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				String value = (String) properties.get(key);
				Field f = data.getTable().getFieldByName(key);
				if (f != null) {
					key = f.getSearchFieldName();
				}
				data.getRequest().setAttribute(key, value);
			}
		}
	}

}
