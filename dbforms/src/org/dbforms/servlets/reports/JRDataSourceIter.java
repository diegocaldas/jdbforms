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

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.util.IGetSession;

/**
 */
import java.util.Iterator;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class JRDataSourceIter implements JRDataSource, IGetSession {
   private static Log  logCat = LogFactory.getLog(JRDataSourceIter.class
                                                  .getName());
   Object              current     = null;
   private Iterator    iter;
   private PageContext pageContext;
   private int         rownum      = 0;

   /**
    * DOCUMENT ME!
    *
    * @param iter
    * @param pageContext
    */
   public JRDataSourceIter(Iterator    iter,
                           PageContext pageContext) {
      this.pageContext = pageContext;
      this.iter        = iter;
      rownum           = 0;
   }

   /**
    * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
    */
   public Object getFieldValue(JRField field) throws JRException {
      Object o;

      String search = field.getName();

      if (search.startsWith("context__")) {
         logCat.debug("Trying to find data for Page Context value: " + search);
         o = getPageContextValue(search);
      } else if (search.startsWith("internal__")) {
         logCat.debug("Trying to find data for session value: " + search);
         o = getInternalValue(search);
      } else if (search.startsWith("session__")) {
         logCat.debug("Trying to find data for session value: " + search);
         o = getSessionValue(search);
      } else {
         logCat.debug("Trying to find data for field named: " + search);
         o = getCurrentValue(search);
      }

      return o;
   }


   /* (non-Javadoc)
    * @see org.dbforms.servlets.reports.IGetSession#getSession()
    */
   public HttpSession getSession() {
      return pageContext.getSession();
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
   private Object getCurrentValue(String search) {
      Object obj = null;

      try {
         if (current != null) {
            // complex, current is a container or a bean
            String sa = search;
            sa = sa.replaceAll("__", ".");
            logCat.debug("calling PropertyUtils.getProperty " + current + " "
                         + sa);
            obj = PropertyUtils.getProperty(current, sa);
         }
      } catch (Exception e) {
         logCat.error("getCurrentValue: " + e);
      }

      return obj;
   }


   /**
    * DOCUMENT ME!
    *
    * @param search
    *
    * @return
    */
   private Object getInternalValue(String search) {
      Object obj = null;

      try {
         search = search.substring(search.indexOf("__") + 2);
         logCat.debug("Trying to find data for internal var : " + search);

         if (search.equalsIgnoreCase("rownum")) {
            return new Integer(rownum);
         }
      } catch (Exception e) {
         logCat.error("getPageContextValue: " + e);
      }

      return obj;
   }


   /**
    * DOCUMENT ME!
    *
    * @param search
    *
    * @return
    */
   private Object getPageContextValue(String search) {
      Object obj = null;
      int    pos;

      try {
         search = search.substring(search.indexOf("__") + 2);
         logCat.debug("Trying to find data for page context var : " + search);
         pos = search.indexOf("__");

         if (pos == -1) {
            // simple type, 'search' is an object in the session
            obj = pageContext.findAttribute(search);
         } else {
            // complex, 'search' is really a bean
            String[] sa   = search.split("__", 2);
            Object   bean = pageContext.findAttribute(sa[0]);
            sa[1] = sa[1].replaceAll("__", ".");
            logCat.debug("calling PropertyUtils.getProperty " + sa[0] + " "
                         + sa[1]);
            obj = PropertyUtils.getProperty(bean, sa[1]);
         }
      } catch (Exception e) {
         logCat.error("getPageContextValue: " + e);
      }

      return obj;
   }


   /**
    * DOCUMENT ME!
    *
    * @param search
    *
    * @return
    */
   private Object getSessionValue(String search) {
      Object obj = null;
      int    pos;

      try {
         HttpSession session = this.pageContext.getSession();
         search = search.substring(search.indexOf("__") + 2);
         logCat.debug("Trying to find data for session var : " + search);
         pos = search.indexOf("__");

         if (pos == -1) {
            // simple type, 'search' is an object in the session
            obj = session.getAttribute(search);
         } else {
            // complex, 'search' is really a bean
            String[] sa   = search.split("__", 2);
            Object   bean = session.getAttribute(sa[0]);
            sa[1] = sa[1].replaceAll("__", ".");
            logCat.debug("calling PropertyUtils.getProperty " + sa[0] + " "
                         + sa[1]);
            obj = PropertyUtils.getProperty(bean, sa[1]);
         }
      } catch (Exception e) {
         logCat.error("getSessionValue: " + e);
      }

      return obj;
   }
}
