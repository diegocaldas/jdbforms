package org.dbforms.taglib;

import org.dbforms.event.*;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
//import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Category;

public class IsWebEvent extends TagSupport
{
	// logging category for this class
	static Category logCat = Category.getInstance(IsWebEvent.class.getName());

    private Boolean value;
    private String event;
    
    public IsWebEvent()
    {
        value = new Boolean("true");
        event = null;
    }


    public int doStartTag() throws JspException {

        WebEvent webEvent = (WebEvent) pageContext.getRequest().getAttribute("webEvent");
        			
        if(webEvent==null || event==null || value == null){
        	logCat.debug("Can't do IsWebEvent with  webEvent: "+webEvent+"  event: "+event+"   value: "+value);
        	return SKIP_BODY;
        }
        
        String className = webEvent.getClass().getName();
        className = className.substring("org.dbforms.event.".length(), className.length());
        
        boolean eventNameMatch = className.toUpperCase().indexOf( event.toUpperCase() )!=-1;
 
        if(logCat.isDebugEnabled()) 
        	logCat.debug(" IsLocalWebEvent webEvent className: "+className+"    event: "+event+"  value: "+value);
 
        return (value.booleanValue()==eventNameMatch)? EVAL_BODY_INCLUDE : SKIP_BODY;
    }

    public final void setEvent(String str)
    {
        event = str;
    }

    public final void setValue(String str)
    {
        value = new Boolean(str);
    }


}
