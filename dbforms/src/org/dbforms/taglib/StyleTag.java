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

import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;



/**
 * Renders an dbforms style tag
 *
 * @author Joe Peer
 */
public class StyleTag extends TagSupportWithScriptHandler
   implements javax.servlet.jsp.tagext.TryCatchFinally {
   private Hashtable params;
   private String    paramList;
   private String    part;

   // --------------------- properties ------------------------------------------------------------------
   private String template;

   //private String templateBegin, templateEnd;
   private String templateBase;
   private String templateBaseDir;

   /**
    * DOCUMENT ME!
    *
    * @param pageContext DOCUMENT ME!
    */
   public void setPageContext(final javax.servlet.jsp.PageContext pageContext) {
      super.setPageContext(pageContext);

      templateBase = pageContext.getServletContext()
                                .getInitParameter("templateBase");

      if (templateBase == null) {
         templateBase = "templates";
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param paramList DOCUMENT ME!
    */
   public void setParamList(String paramList) {
      this.paramList = paramList;
      this.params    = parseParams(paramList);
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getParamList() {
      return paramList;
   }


   /**
    * DOCUMENT ME!
    *
    * @param part DOCUMENT ME!
    */
   public void setPart(String part) {
      this.part = part;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getPart() {
      return part;
   }


   /**
    * DOCUMENT ME!
    *
    * @param template DOCUMENT ME!
    */
   public void setTemplate(String template) {
      this.template        = template;
      this.templateBaseDir = templateBase + "/" + template + "/";

      //this.templateBegin = templateBaseDir + template + "_begin.jsp";
      //this.templateEnd =  templateBaseDir + template + "_end.jsp";
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getTemplate() {
      return template;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException DOCUMENT ME!
    */
   public int doAfterBody() throws JspException {
      return SKIP_BODY; // gets only rendered 1 time
   }


   /**
    * DOCUMENT ME!
    *
    * @param t DOCUMENT ME!
    *
    * @throws Throwable DOCUMENT ME!
    */
   public void doCatch(Throwable t) throws Throwable {
      throw t;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws JspException DOCUMENT ME!
    */
   public int doEndTag() throws JspException {
      try {
         if (bodyContent != null) {
            bodyContent.writeOut(bodyContent.getEnclosingWriter());
         }

         HttpServletRequest request = (HttpServletRequest) pageContext
                                      .getRequest();

         if (params != null) {
            request.setAttribute("styleparams", params);
         }

         request.setAttribute("baseDir", templateBaseDir);

         pageContext.include(templateBaseDir + template + "_" + part + ".jsp");
      } catch (Exception ioe) {
         throw new JspException("Problem 2 including template end - "
                                + ioe.toString());
      }

      return EVAL_PAGE;
   }


   /**
    * DOCUMENT ME!
    */
   public void doFinally() {
      params          = null;
      templateBase    = null;
      templateBaseDir = null;
      template        = null;
      paramList       = null;
      part            = null;
   }


   /**

                              */
   public int doStartTag() throws JspException {
      /*
                      try {
                              if(params!=null) pageContext.getRequest().setAttribute("styleparams", params);
                              pageContext.getRequest().setAttribute("baseDir", templateBaseDir);


                //pageContext.forward(templateBegin);

                HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
                HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

                request.getRequestDispatcher(templateBegin).include(request, response);

                //pageContext.getOut().flush();
                      } catch(IOException ioe) {
                              throw new JspException("Problem 1including template begin - "+ioe.toString());
                      } catch(ServletException se) {}
      */
      return EVAL_BODY_BUFFERED;
   }


   /**
    * this method splits a string of the form "param1 = 'foo1', param2 = foo2,
    * param3=foo3" into a hashtable containing following key/value pairs: {
    * ("param1"/"foo1"), ("param2"/"foo2"), ("param3"/"foo3") } #fixme:
    * primitive algorithm! breaks if params contains komma - signs. fix it
    *
    * @param s DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   private Hashtable parseParams(String s) {
      Hashtable result = new Hashtable();

      // break into main (key/value)- tokens
      StringTokenizer st = new StringTokenizer(s, ",");

      while (st.hasMoreTokens()) {
         String token = st.nextToken(); // a key-value pair in its orignal string-shape

         int    equalSignIndex = token.indexOf('=');

         // peeling out the key
         String key = token.substring(0, equalSignIndex)
                           .trim();

         // peeling out the value (which may or not be embedded in single quotes)
         String value = token.substring(equalSignIndex + 1)
                             .trim();

         if ((value.charAt(0) == '\'')
                   && (value.charAt(value.length() - 1) == '\'')) // get out of any single quotes
          {
            value = value.substring(1, value.length() - 1);
         }

         result.put(key, value);
      }

      return result;
   }
}
