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

import org.dbforms.config.DbEventInterceptor;
import org.dbforms.config.Interceptor;
import org.dbforms.config.Table;
import org.dbforms.config.DbEventInterceptorData;
import org.dbforms.config.DbFormsConfig;

import org.dbforms.util.AbstractTestCase;



/**
 * Description of the Class
 *
 * @author epugh
 *
 * @deprecated May 3, 2002
 */
public class TestInterceptor extends AbstractTestCase {
   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testInterceptor() throws Exception {
      Interceptor interceptor = new Interceptor();
      interceptor.setClassName("interceptors.InterceptorTestClassic");
      assertTrue("Class name should be interceptors.InterceptorTestClassic",
                 interceptor.getClassName().equals("interceptors.InterceptorTestClassic"));

      Table table = new Table();
      table.setConfig(new DbFormsConfig(null));
      table.addInterceptor(interceptor);

      int i = table.getInterceptors()
                   .size();
      assertEquals("count interceptors", i, 1);
      DbEventInterceptorData data = new DbEventInterceptorData(null, null, null);

      table.processInterceptors(DbEventInterceptor.PRE_INSERT, data);
      assertTrue("preInsert", interceptors.InterceptorTestClassic.preInsertCalled);

      table.processInterceptors(DbEventInterceptor.POST_INSERT, data);
      assertTrue("postInsert", interceptors.InterceptorTestClassic.postInsertCalled);

      table.processInterceptors(DbEventInterceptor.PRE_UPDATE, data);
      assertTrue("preUpdate", interceptors.InterceptorTestClassic.preUpdateCalled);

      table.processInterceptors(DbEventInterceptor.POST_UPDATE, data);
      assertTrue("postUpdate", interceptors.InterceptorTestClassic.postUpdateCalled);

      table.processInterceptors(DbEventInterceptor.PRE_DELETE, data);
      assertTrue("preUpdate", interceptors.InterceptorTestClassic.preDeleteCalled);

      table.processInterceptors(DbEventInterceptor.POST_DELETE, data);
      assertTrue("postUpdate", interceptors.InterceptorTestClassic.postDeleteCalled);

      table.processInterceptors(DbEventInterceptor.PRE_SELECT, data);
      assertTrue("preSelectUpdate", interceptors.InterceptorTestClassic.preSelectCalled);

      table.processInterceptors(DbEventInterceptor.POST_SELECT, data);
      assertTrue("postSelectUpdate", interceptors.InterceptorTestClassic.postSelectCalled);

	  
   }
}
