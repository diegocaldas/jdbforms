/*
 * Created on May 30, 2004
 *
 */
package org.dbforms.taglib;

import java.util.HashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.dbforms.util.Formatter;

/**
 * @author Neal Katz
 *
 * <db:setCustomFormater name="foo" class="" arg="" reset="" >
 * arg and reset are optional
 */
public class SetCustomFormatterTag
	extends TagSupportWithScriptHandler
	implements javax.servlet.jsp.tagext.TryCatchFinally {
	static final String sessionKey = "Tag.CustomFormatter.map";
	String name = null;
	String className = null;
	Object arg = null;
	boolean reset = false;

	/** name to use, other tags will use this as the value for the customFormatter attribute
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/** classname of a class implementing the CustomFormatter Interface
	 * @param className
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/** optional argument passed to CustomFormatter instance init()
	 * @param obj
	 */
	public void setArg(Object obj) {
		this.arg = obj;
	}
	/** default false, if true go thru the class loading process everytime
	 * the tag is executed. If false, then the class will only be loaded once
	 * (i.e. avoid multiple re-initialization).
	 * @param b
	 */
//	public void setReset(boolean b) {
//		reset = b;
//	}
	public int doStartTag() throws javax.servlet.jsp.JspException {
		return SKIP_BODY;
	}
	/**
	 * @param string
	 */
	public int doEndTag() throws JspException {
		if ((name != null) && (name.length() > 0)) {
			HttpSession session = pageContext.getSession();
			HashMap hm = (HashMap) session.getAttribute(sessionKey);
			if (hm == null) {
				hm = new HashMap();
				session.setAttribute(sessionKey, hm);
			}
			try {
				Formatter cf = null;
				// see if it is already loaded
				cf = (Formatter) hm.get(name);
				if (cf == null) {
					//load it
					Class cl = Class.forName(className);
					cf = (Formatter) cl.newInstance();					
					cf.setFormat(arg.toString());
					hm.put(name, cf);
				}
			} catch (ClassNotFoundException e) {
				throw new JspException(e.getLocalizedMessage());
			} catch (InstantiationException e) {
				throw new JspException(e.getLocalizedMessage());
			} catch (IllegalAccessException e) {
				throw new JspException(e.getLocalizedMessage());
			}
		}
		return EVAL_PAGE;
	}
	public void doFinally() {
		this.arg = null;
		this.className = null;
		this.name = null;
		this.reset = false;
		super.doFinally();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch(java.lang.Throwable)
	 */
	public void doCatch(Throwable t) throws Throwable {
		throw t;
	}


}
