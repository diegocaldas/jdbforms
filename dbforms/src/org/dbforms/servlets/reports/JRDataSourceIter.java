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

/**
 * Use an iterator as data source  HOWTO 1) create a Collection of X. Where X
 * is a complex object with 'fields', X can either be a Set or a Bean. The
 * container must support Iterator(). Object X must be supported  by apache
 * bean utils 2) somewhere in pagecontext (like session) put the Collection
 * with key "jasper.input" 3) invoke  gets field data from multiple sources,
 * not just DBform fields context : prefix context__xxx  . Uses
 * pageContext.findAttribute(xxx) session : prefix session__xxxx . Uses
 * pageContext.getSession().findAttribute(xxx) internal : prefix internal_zzz
 * attribute of iterator object : no prefix    For internal, only
 * internal__rownum is currently defined For content and session, xxxx may be
 * a 'simple' type or 'complex' type a 'complex' type is an object supporting
 * javabean access, instead of the usual '.' notation, use '__' instead.
 */
package org.dbforms.servlets.reports;

import java.util.Iterator;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public final class JRDataSourceIter extends JRDataSourceAbstract {
   private static Log  logCat = LogFactory.getLog(JRDataSourceIter.class
                                                  .getName());
   Object              current     = null;
   private Iterator    iter;
   private int         rownum      = 0;

   /**
    * DOCUMENT ME!
    *
    * @param iter
    * @param pageContext
    */
   public JRDataSourceIter(Iterator    iter) {
      this.iter        = iter;
      rownum           = 0;
   }

   /**
    * @see net.sf.jasperreports.engine.JRDataSource#next()
    */
   public boolean next() throws JRException {
      if (!iter.hasNext()) {
         return false;
      }
      current = iter.next();
      rownum++;
      return true;
   }

   /**
    * DOCUMENT ME!
    *
    * @param search
    *
    * @return
    */
   public final Object getFieldValue(String search) {
      Object obj = null;
      try {
         if (current != null) {
            // complex, current is a container or a bean
            logCat.debug("calling PropertyUtils.getProperty " + current + " "
                         + search);
            obj = PropertyUtils.getProperty(current, search);
         }
      } catch (Exception e) {
         logCat.error("getCurrentValue: " + e);
      }

      return obj;
   }
}
