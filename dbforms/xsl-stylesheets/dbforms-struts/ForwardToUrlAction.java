/*
 * Created on 01.07.2004
 */
package com.hutchison.at.webui.admin.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Guenther Wutzl www.milestonemedia.at
 * This action forwards to the jsp-page given over the requestparameter 
 * "forward".
 */
public class ForwardToUrlAction extends Action {
	
	public ActionForward onExecute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		String forward = request.getParameter("forward");
		return new ActionForward(forward);
	}
}