package org.dbforms.taglib;

import org.dbforms.util.MessageResources;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.ServletContext;


public class MessageTag extends TagSupport {

	private static MessageResources messages = null;
	
	private String key = null;

	public void setKey(String newKey) {
		key = newKey;
	}

	public String getKey() {
		return key;
	}
	
	public int doStartTag() throws javax.servlet.jsp.JspException {
		return SKIP_BODY;
  	} 
	
	public int doEndTag() throws JspException {
		
		if(getKey()!=null){
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			ServletContext application = pageContext.getServletContext();
			
			String message = MessageResources.getMessage( getKey(), request.getLocale());
			
			try{
				if(message!=null) {
					pageContext.getOut().write(message);
				} else {
					pageContext.getOut().write(getKey());
				}
			} catch (java.io.IOException ioe) {
				throw new JspException("IO Error: " + ioe.getMessage());
			}

		}			
	
		return EVAL_PAGE;
	}
}

