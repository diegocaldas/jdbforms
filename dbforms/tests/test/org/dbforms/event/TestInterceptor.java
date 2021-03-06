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

import org.dbforms.config.Interceptor;
import org.dbforms.config.Table;
import org.dbforms.interfaces.DbEventInterceptorData;
import org.dbforms.interfaces.IDbEventInterceptor;

import org.dbforms.util.AbstractTestCase;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class TestInterceptor extends AbstractTestCase {
   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testInterceptor() throws Exception {
      Interceptor interceptor = new Interceptor();
      interceptor.setClassName("interceptors.InterceptorTest");
      assertTrue("Class name should be interceptors.InterceptorTest",
                 interceptor.getClassName().equals("interceptors.InterceptorTest"));

      Table table = new Table();
      table.addInterceptor(interceptor);

      int i = table.getInterceptors()
                   .size();
      assertEquals("count interceptors", i, 1);

      DbEventInterceptorData data = new DbEventInterceptorData(null, null,
                                                               null, table);

      table.processInterceptors(IDbEventInterceptor.PRE_INSERT, data);
      assertTrue("preInsert", interceptors.InterceptorTest.preInsertCalled);

      table.processInterceptors(IDbEventInterceptor.POST_INSERT, data);
      assertTrue("postInsert", interceptors.InterceptorTest.postInsertCalled);

      table.processInterceptors(IDbEventInterceptor.PRE_UPDATE, data);
      assertTrue("preUpdate", interceptors.InterceptorTest.preUpdateCalled);

      table.processInterceptors(IDbEventInterceptor.POST_UPDATE, data);
      assertTrue("postUpdate", interceptors.InterceptorTest.postUpdateCalled);

      table.processInterceptors(IDbEventInterceptor.PRE_DELETE, data);
      assertTrue("preUpdate", interceptors.InterceptorTest.preDeleteCalled);

      table.processInterceptors(IDbEventInterceptor.POST_DELETE, data);
      assertTrue("postUpdate", interceptors.InterceptorTest.postDeleteCalled);

      table.processInterceptors(IDbEventInterceptor.PRE_SELECT, data);
      assertTrue("preSelectUpdate", interceptors.InterceptorTest.preSelectCalled);

      table.processInterceptors(IDbEventInterceptor.POST_SELECT, data);
      assertTrue("postSelectUpdate",
                 interceptors.InterceptorTest.postSelectCalled);

      table.processInterceptors(IDbEventInterceptor.PRE_ADDROW, data);
      assertTrue("preAddRow", interceptors.InterceptorTest.preAddRowCalled);

      table.processInterceptors(IDbEventInterceptor.POST_ADDROW, data);
      assertTrue("postAddRow", interceptors.InterceptorTest.postAddRowCalled);
   }


   /**
    * DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public void testInterceptorClassic() throws Exception {
      Interceptor interceptor = new Interceptor();
      interceptor.setClassName("interceptors.InterceptorTestClassic");
      assertTrue("Class name should be interceptors.InterceptorTestClassic",
                 interceptor.getClassName().equals("interceptors.InterceptorTestClassic"));

      Table table = new Table();
      table.addInterceptor(interceptor);

      int i = table.getInterceptors()
                   .size();
      assertEquals("count interceptors", i, 1);

      DbEventInterceptorData data = new DbEventInterceptorData(null, null,
                                                               null, table);

      table.processInterceptors(IDbEventInterceptor.PRE_INSERT, data);
      assertTrue("preInsert",
                 interceptors.InterceptorTestClassic.preInsertCalled);

      table.processInterceptors(IDbEventInterceptor.POST_INSERT, data);
      assertTrue("postInsert",
                 interceptors.InterceptorTestClassic.postInsertCalled);

      table.processInterceptors(IDbEventInterceptor.PRE_UPDATE, data);
      assertTrue("preUpdate",
                 interceptors.InterceptorTestClassic.preUpdateCalled);

      table.processInterceptors(IDbEventInterceptor.POST_UPDATE, data);
      assertTrue("postUpdate",
                 interceptors.InterceptorTestClassic.postUpdateCalled);

      table.processInterceptors(IDbEventInterceptor.PRE_DELETE, data);
      assertTrue("preUpdate",
                 interceptors.InterceptorTestClassic.preDeleteCalled);

      table.processInterceptors(IDbEventInterceptor.POST_DELETE, data);
      assertTrue("postUpdate",
                 interceptors.InterceptorTestClassic.postDeleteCalled);

      table.processInterceptors(IDbEventInterceptor.PRE_SELECT, data);
      assertTrue("preSelectUpdate",
                 interceptors.InterceptorTestClassic.preSelectCalled);

      table.processInterceptors(IDbEventInterceptor.POST_SELECT, data);
      assertTrue("postSelectUpdate",
                 interceptors.InterceptorTestClassic.postSelectCalled);
   }
}
