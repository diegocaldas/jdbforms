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
 * DatabaseEventFactory class.
 * Create DatabaseEvent objects.
 *
 * @author  Luca Fossato
 * @created  20 novembre 2002
 */
public abstract class DatabaseEventFactory extends EventFactory
{
   /**
    *  Create and return a new UpdateEvent as secondary event.
    *
    * @param  tableId the table identifier
    * @param  keyId   the key   identifier
    * @param  request the HttpServletRequest object
    * @param  config  the DbForms config object
    * @return  The updateEvent object
    */
   public abstract DatabaseEvent createUpdateEvent(int tableId, String keyId,
      HttpServletRequest request, DbFormsConfig config);

   /**
    *  Create and return a new InsertEvent as secondary event.
    *
    * @param  tableId the table identifier
    * @param  request the HttpServletRequest object
    * @param  config  the DbForms config object
    * @return  The updateEvent object
    */
   public abstract DatabaseEvent createInsertEvent(int tableId, String keyId,
      HttpServletRequest request, DbFormsConfig config);
}
