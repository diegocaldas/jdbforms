/*
 * Created on 03.10.2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.dbforms.taglib;

import org.dbforms.util.PageContextBuffer;
import javax.servlet.Servlet;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Category;


/**
 * @author hkk
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DbBaseButtonFactory
{
   private DbBaseButtonTag btn;
   private PageContextBuffer pageContext;
	static Category logCat = Category.getInstance(DbBaseButtonFactory.class.getName());

   
   public DbBaseButtonFactory(PageContext parentContext, TagSupport parent, Class clazz) throws JspException
   {
      try
      {
			btn = (DbBaseButtonTag) clazz.newInstance();	
			pageContext = new PageContextBuffer();
			pageContext.initialize((Servlet)parentContext.getPage(), parentContext.getRequest(), parentContext.getResponse(), null, true, 0, true);
			btn.setPageContext(pageContext);
			btn.setParent(parent);
      } catch (Exception e) 
      {
         throw new JspException(e);
      }
   }
   
   public DbBaseButtonTag getButton() 
   {
      return btn;
   }
   
   public StringBuffer render() throws JspException
   {
		if (btn.doStartTag() != TagSupport.SKIP_BODY)
			btn.doEndTag();
      return pageContext.getBuffer();
   }
   

}
