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

package org.dbforms.servlets.reports;

import dori.jasper.engine.JRDataSource;
import dori.jasper.engine.JRException;
import dori.jasper.engine.JRField;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.ResultSetVector;

import org.dbforms.util.IGetSession;

/**
 */
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;



/**
 * use a ResultSetVector as data source.  gets field data from multiple
 * sources, not just DBform fields context : prefix context__xxx  . Uses
 * pageContext.findAttribute(xxx) session : prefix session__xxx . Uses
 * pageContext.getSession().findAttribute(xxx) internal : prefix internal_zzz
 * dbforms : no prefix    For internal, only internal__rownum is currently
 * defined For content and session, xxxx may be a 'simple' type or 'complex'
 * type a 'complex' type is an object supporting javabean access, instead of
 * the usual '.' notation, use '__' instead. The double _ is used since '.'
 * notation would not work with JasperReports  ex. &lt?
 * session.putAttribute("adate". new Date()); ?&gt session__adate__month ,
 * will be accessed as adate.getMonth();
 *
 * @author Neal Katz
 */
public class JRDataSourceRSV implements JRDataSource, IGetSession {
   private static Log      logCat = LogFactory.getLog(JRDataSourceRSV.class
                                                      .getName());
   private PageContext     pageContext;
   private ResultSetVector rsv;
   private int             rownum = 0;

   /**
    * Constructor for JRDataSourceRSV.
    *
    * @param rsv DOCUMENT ME!
    * @param pageContext DOCUMENT ME!
    */
   public JRDataSourceRSV(ResultSetVector rsv,
                          PageContext     pageContext) {
      this.rsv = rsv;
      this.rsv.setPointer(-1);
      this.pageContext = pageContext;
      rownum           = 0;
   }

   /**
    * @see dori.jasper.engine.JRDataSource#getFieldValue(dori.jasper.engine.JRField)
    *      Philip Grunikiewicz 2004-01-13  Because I had fields defined
    *      (dbforms-config.xml) in mix case (ie: creditLimit) and in my XML
    *      file, my field was in uppercase (ie: CREDITLIMIT), my field could
    *      not be found.   Added some logging to help out debugging this type
    *      of problem.
    */
   public Object getFieldValue(JRField field) throws JRException {
      Object o;

      String search = field.getName();

      if (search.startsWith("context__")) {
         logCat.debug("Trying to find data for Page Context value: " + search);
         o = getPageContextValue(search);
      } else if (search.startsWith("internal__")) {
         logCat.debug("Trying to find data for internal value: " + search);
         o = getInternalValue(search);
      } else if (search.startsWith("session__")) {
         logCat.debug("Trying to find data for session value: " + search);
         o = getSessionValue(search);
      } else {
         logCat.debug("Trying to find data for field named: " + search);
         o = getFieldValue(field.getName());

         if (o == null) {
            logCat.debug("Field not found in dbforms-config, trying field renamed to uppercase: "
                         + search.toUpperCase());
            o = getFieldValue(search.toUpperCase());
         }

         if (o == null) {
            logCat.debug("Field not found in dbforms-config, trying field renamed to lowercase: "
                         + search.toLowerCase());
            o = getFieldValue(field.getName().toLowerCase());
         }
      }

      /*
           // Try class conversation if the classes do not match!
           if ((o != null) && (o.getClass() != field.getValueClass()))
           {
             try
             {
               Object[] constructorArgs      = new Object[]
               {
                  o.toString()
               };
               Class[]  constructorArgsTypes = new Class[]
               {
                  String.class
               };
               o = ReflectionUtil.newInstance(field.getValueClass(),
                                       constructorArgsTypes,
                                       constructorArgs);
             }
             catch (Exception e)
             {
               ;
             }
           }
      */
      return o;
   }


   /* (non-Javadoc)
    * @see org.dbforms.servlets.reports.IGetSession#getSession()
    */
   public HttpSession getSession() {
      return pageContext.getSession();
   }


   /**
    * @see dori.jasper.engine.JRDataSource#next()
    */
   public boolean next() throws JRException {
      if (ResultSetVector.isNull(rsv)) {
         return false;
      }

      if (rsv.getPointer() == (rsv.size() - 1)) {
         return false;
      }

      rsv.increasePointer();
      rownum++;

      return true;
   }


   private Object getFieldValue(String fieldName) {
      Object o = null;

      try {
         o = rsv.getFieldAsObject(fieldName);
      } catch (Exception e) {
         logCat.error("getFieldValue", e);
      }

      return o;
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
      Object      obj     = null;
      int         pos;
      HttpSession session = this.pageContext.getSession();

      try {
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
