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

/*
 * Created on 01.07.2004
 */
package com.hutchison.at.webui.admin.struts.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * @author Guenther Wutzl www.milestonemedia.at
 * This action forwards to the jsp-page given over the requestparameter
 * "forward".
 */
public class ForwardToUrlAction extends Action {
   /**
    * DOCUMENT ME!
    *
    * @param mapping DOCUMENT ME!
    * @param form DOCUMENT ME!
    * @param request DOCUMENT ME!
    * @param response DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    *
    * @throws Exception DOCUMENT ME!
    */
   public ActionForward onExecute(ActionMapping       mapping,
                                  ActionForm          form,
                                  HttpServletRequest  request,
                                  HttpServletResponse response)
                           throws Exception {
      String forward = request.getParameter("forward");

      return new ActionForward(forward);
   }
}
