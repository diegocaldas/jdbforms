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
import javax.servlet.http.HttpServletRequest;
import org.dbforms.config.DbFormsConfig;



/**
 * ReloadEvent is used to reload the current page with data from Request
 * object. When you want to do action on different field when one of them
 * change. Use for field manipulation server side. <br>
 * Example: Select child change when Select parent change.
 * 
 * @author Eric Beaumier
 */
public class ReloadEvent extends WebEvent
{

   /**
    * Creates a new ReloadEvent object.
    * @param tableId DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param config DOCUMENT ME!
    */
   public ReloadEvent(int tableId, HttpServletRequest request, 
                      DbFormsConfig config)
   {
      super(tableId, request, config);
   }
}