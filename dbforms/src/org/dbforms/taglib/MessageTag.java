package org.dbforms.taglib;

import org.dbforms.util.MessageResources;
import org.dbforms.util.ParseUtil;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.ServletContext;

/*****
    2002-09-23 HKK: Extented to support parameters
 ****/

public class MessageTag extends TagSupport {

   private static MessageResources messages = null;

   private String key = null;
   private String param = null;

   public void setKey(String newKey) {
      key = newKey;
   }

   public String getKey() {
      return key;
   }

   public void setParam(String newParam) {
      param = newParam;
   }

   public String getParam() {
      return param;
   }

   public int doStartTag() throws javax.servlet.jsp.JspException {
      return SKIP_BODY;
   }

   public int doEndTag() throws JspException {

      if (getKey() != null) {
         Locale locale = MessageResources.getLocale((HttpServletRequest) pageContext.getRequest());
         String message;
         if ((param == null) || (param.length() == 0)) {
            message = MessageResources.getMessage(getKey(), locale);
         } else {
            message = MessageResources.getMessage(getKey(), locale, splitString(param, ","));
         }
         try {
            if (message != null) {
               pageContext.getOut().write(message);
            } else {
               pageContext.getOut().write(getKey());
               if (param != null) {
                  pageContext.getOut().write("&nbsp;");
                  pageContext.getOut().write(param);
               }
            }
         } catch (java.io.IOException ioe) {
            throw new JspException("IO Error: " + ioe.getMessage());
         }
      }
      return EVAL_PAGE;
   }

   private String[] splitString(String str, String delimeter) {
      StringTokenizer st = new StringTokenizer(str, delimeter);
      int i = 0;
      String[] result = new String[st.countTokens()];
      while (st.hasMoreTokens()) {
         result[i] = st.nextToken();
         i++;
      }
      return result;
   }

}
