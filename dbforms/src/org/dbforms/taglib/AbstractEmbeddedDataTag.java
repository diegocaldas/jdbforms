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
package org.dbforms.taglib;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.ResultSetVector;
import org.dbforms.interfaces.IDataContainer;
import org.dbforms.interfaces.IEscaper;
import org.dbforms.interfaces.IFormatEmbeddedData;
import org.dbforms.interfaces.IStaticDataList;
import org.dbforms.interfaces.StaticData;

import org.dbforms.util.MessageResources;
import org.dbforms.util.ReflectionUtil;
import org.dbforms.util.SqlUtil;
import org.dbforms.util.Util;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;



/**
 * DOCUMENT ME!
 * 
 * @author $author$
 * @version $Revision$
 */
public abstract class AbstractEmbeddedDataTag extends AbstractDbBaseHandlerTag
   implements javax.servlet.jsp.tagext.TryCatchFinally, IStaticDataList  {
   private static Log          logCat           = LogFactory.getLog(AbstractEmbeddedDataTag.class
         .getName());
   private IFormatEmbeddedData printfFormat;
   private List                data;
   private String              dbConnectionName;
   private String              disableCache = "false";
   private String              format;
   private String              formatClass;
   private String              name;

   /**
	 * DOCUMENT ME!
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 */
   public void setDbConnectionName(String name) {
      dbConnectionName = name;
   }


   /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
   public String getDbConnectionName() {
      return dbConnectionName;
   }


   /**
	 * Insert the method's description here. Creation date: (2001-09-21
	 * 12:20:42)
	 * 
	 * @param newDisableCache
	 *            java.lang.String
	 */
   public void setDisableCache(java.lang.String newDisableCache) {
      disableCache = newDisableCache;
   }


   /**
	 * Insert the method's description here. Creation date: (2001-09-21
	 * 12:20:42)
	 * 
	 * @return java.lang.String
	 */
   public java.lang.String getDisableCache() {
      return disableCache;
   }


   /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
   public IEscaper getEscaper() {
      IDataContainer parent = ((IDataContainer) getParent());
      IEscaper      res = parent.getEscaper();

      return res;
   }


   /**
	 * DOCUMENT ME!
	 * 
	 * @param format
	 *            DOCUMENT ME!
	 */
   public void setFormat(java.lang.String format) {
      this.format = format;
   }


   /**
	 * Sets the formatClass.
	 * 
	 * @param formatClass
	 *            The formatClass to set
	 */
   public void setFormatClass(String formatClass) {
      this.formatClass = formatClass;
   }


   /**
	 * Returns the formatClass.
	 * 
	 * @return String
	 */
   public String getFormatClass() {
      return formatClass;
   }


   /**
	 * set the name of the embedded data. every embedded data entity on a jsp
	 * page has to have a unique name. this name is used for storing (caching)
	 * and retrieving data in Page-Scope. this is useful if a tag gets evaluated
	 * many times -> we do not want the queries etc. to be executed every time!
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 */
   public void setName(String name) {
      this.name = name;
   }


   /**
	 * returns the unique name of the embedded data
	 * 
	 * @return DOCUMENT ME!
	 */
   public String getName() {
      return name;
   }



   /**
	 * DOCUMENT ME!
	 */
   public void doFinally() {
      name                = null;
      dbConnectionName    = null;
      format              = null;
      printfFormat        = null;
      formatClass         = null;
      disableCache        = "false";
   }


   /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws JspException
	 *             DOCUMENT ME!
	 * @throws IllegalArgumentException
	 *             DOCUMENT ME!
	 */
   public int doStartTag() throws JspException {
      /**
		 * Grunikiewicz.philip 2001-09-21 Sometimes a developer may want to
		 * execute the embedded data query for each row. i.e. not cache the
		 * result set. Example: Table contains 1000+ of rows, use queryData with
		 * qualified 'where' clause. Data cannot be cached because each
		 * subsequent row need to execute a different query (whereClause with
		 * different parameters)
		 */
      printfFormat = null;

      if (!Util.isNull(format) || !Util.isNull(getFormatClass())) {
         if (Util.isNull(getFormatClass())) {
            setFormatClass(getConfig().getDefaultFormatterClass());
         }

         if (Util.isNull(format)) {
            setFormat("%s");
         }

         if (format.indexOf('%') < 0) // try cheap compatibility mode for old
										// applications without '%'// within
										// patterns
          {
            StringBuffer newFormat = new StringBuffer();

            for (int j = 0; j < format.length(); j++) {
               if (format.charAt(j) == 's') {
                  newFormat.append("%s");
               } else {
                  newFormat.append(format.charAt(j));
               }
            }

            format = newFormat.toString();

            // was 's bla bla s -- s' is now '%s blabla %s -- %s'
         }

         try {
            printfFormat = (IFormatEmbeddedData) ReflectionUtil.newInstance(getFormatClass());
            printfFormat.setLocale(MessageResources.getLocale(
                  (HttpServletRequest) pageContext.getRequest()));
            printfFormat.setFormat(format);
         } catch (Exception e) {
            logCat.error("cannot create the new printfFormat ["
               + getFormatClass() + "]", e);
         }
      }

      int result = SKIP_BODY;
      data = null;

      // If disableCache not activated, was the data generated by another
      // instance on the same page yet?
      if (useCache()) {
         data = (List) pageContext.getAttribute(name, PageContext.PAGE_SCOPE);
      }

      // if not, we do it
      if (data == null) {
         result = EVAL_BODY_BUFFERED;
         logCat.info("generating Embeddeddata " + name);

         // take Config-Object from application context - this object should
         // have been
         // initalized by Config-Servlet on Webapp/server-startup!
         DbFormsConfig config = null;

         try {
            config = DbFormsConfigRegistry.instance().lookup();
         } catch (Exception e) {
            logCat.error(e);
            throw new JspException(e);
         }

         Connection con = null;

         try {
            con = config.getConnection(dbConnectionName);
         } catch (Exception e) {
            throw new JspException(e);
         }

         try {
            data = fetchData(con);

            // Always store data in pageContext - Maybe we need it later.
            // So we can change dynamic disableCache
            pageContext.setAttribute(name, data, PageContext.PAGE_SCOPE);

            // cache result for further loops
         } catch (SQLException sqle) {
            throw new JspException("Database error in EmbeddedData.fetchData "
               + sqle.toString());
         } finally {
            SqlUtil.closeConnection(con);
         }
      } else {
         logCat.info(" Embeddeddata " + name + " already generated");
      }

      ((IDataContainer) getParent()).setEmbeddedData(data);

      // DbBaseMultiTag are: select, radio, checkbox!
      return result;
   }


   /**
	 * this method is implemented by subclasses in order to match the user's
	 * need for specific data.
	 * 
	 * @param con
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
   protected abstract List fetchData(Connection con) throws SQLException;

   /**
	 * DOCUMENT ME!
	 * 
	 * @param pair
	 *            DOCUMENT ME!
	 */
   public void addElement(StaticData pair) {
      data.add(pair);
   }




   /**
	 * formatEmbeddedResultRows() formats a result set accornding to a
	 * eventually given format string. If no format string is given, the output
	 * format is a comma separated list of values. This method is called by
	 * subclasses TableData and QueryData
	 * 
	 * @param rsv
	 *            result set vector to be formatted
	 * 
	 * @return a vector of key-value pairs, the values eventually formatted
	 *         according to a given format string
	 */
   protected List formatEmbeddedResultRows(ResultSetVector rsv) {
      List    result                     = new java.util.Vector();
      boolean resultSuccessFullyFormated = false;

      if (printfFormat != null) {
         try {
            rsv.moveFirst();

            for (int i = 0; i < rsv.size(); i++) {

               String[] currentRow = rsv.getCurrentRow();
               String   htKey = currentRow[0];

               Object[] objs = rsv.getCurrentRowAsObjects();

               Object[] objs2 = new Object[objs.length - 1];

               for (int j = 0; j < objs2.length; j++) {
                  if ((objs[j] instanceof String) || (objs[j] instanceof Byte)
                           || (objs[j] instanceof java.lang.Integer)
                           || (objs[j] instanceof Short)
                           || (objs[j] instanceof Float)
                           || (objs[j] instanceof Long)
                           || (objs[j] instanceof Double)) {
                     objs2[j] = objs[(j + 1)];
                  } else {
                     objs2[j] = currentRow[j + 1];

                     // use String representation instead
                  }
               }

               String htValue = printfFormat.sprintf(objs2);
               result.add(new StaticData(htKey, htValue));
               rsv.moveNext();
            }

            resultSuccessFullyFormated = true;
         } catch (IllegalArgumentException ex) {
            logCat.error("Could not format result using format '" + format
               + "', error message is " + ex.getMessage());
            logCat.error(
               "Using fallback method of comma separated list instead");
            result = new java.util.Vector();
         } catch (NullPointerException npe) // npe will be thrown if null//
											// value returned from database
          {
            logCat.error("Could not format result using format '" + format
               + "', error message is " + npe.getMessage());
            logCat.error(
               "Using fallback method of comma separated list instead");
            result = new java.util.Vector();
         }
      }

      if (!resultSuccessFullyFormated) // no format given or formatting failed
       {
         rsv.moveFirst();

         for (int i = 0; i < rsv.size(); i++) {

            String[]     currentRow = rsv.getCurrentRow();
            String       htKey      = currentRow[0];
            StringBuffer htValueBuf = new StringBuffer();

            for (int j = 1; j < currentRow.length; j++) {
               htValueBuf.append(currentRow[j]);

               if (j < (currentRow.length - 1)) {
                  htValueBuf.append(", ");
               }
            }

            String htValue = htValueBuf.toString(); //

            result.add(new StaticData(htKey, htValue));
            rsv.moveNext();
         }
      }

      return result;
   }


   /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
   protected boolean useCache() {
      return !(Util.getTrue(this.getDisableCache()));
   }
}
