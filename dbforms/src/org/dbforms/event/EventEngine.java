/*
 * $Header$
 * $Revision$
 * $Date$
 *
 * DbForms - a Rapid Application Development Framework
 * Copyright (C) 2001 Joachim Peer <j.peer@gmx.net> et al.
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.dbforms.event;

import java.util.*;
import javax.servlet.http.*;

import org.dbforms.*;
import org.dbforms.util.*;

import org.apache.log4j.Category;

/****
 *
 * <p>This class is invoked by the Controller-Servlet. It parses a request to find out
 * which Event(s) need to be instanciated. The fine-grained parsing (parsing of additional
 * data, etc) is done by the WebEvent-Object itself (in order to hide complexity from this
 * class and to keep the framework open for implementations of new Event-classes)
 * </p>
 *
 * @author Joe Peer <j.peer@gmx.net>
 */

public class EventEngine {

    static Category logCat = Category.getInstance(EventEngine.class.getName()); // logging category for this class

	private HttpServletRequest request;
	private DbFormsConfig config;
	Vector involvedTables;

	public EventEngine(HttpServletRequest request, DbFormsConfig config) {
		this.request = request;
		this.config = config;
		involvedTables = parseInvolvedTables();
	}


  /**
  <p>find out which tables where on the jsp</p>
  <p>(one jsp file may contain multiple dbforms, and each forms could contain many
  subforms nested inside!)</p>
  */
	private Vector parseInvolvedTables() {

		String[] invTables = ParseUtil.getParameterValues(request, "invtable");

		if(invTables==null) // in empty forms, for example, we don't have any involved tables..!
			return null;

		Vector result = new Vector();
		for(int i=0; i<invTables.length; i++) {
			int tableIndex = Integer.parseInt(invTables[i]);
			Table t = config.getTable(tableIndex);
			result.addElement(t);
		}
		return result;
	}

	public Vector getInvolvedTables() {
		return involvedTables;
	}


  /**
  <p>result of this method depends on which Database- or Navigation - button was clicked
  and should reflect the users' intention (is that to pathetic? ;=)
  </p>

  <p>this is alpha code. the goal for version 1.0 is to make it more flexible and open it
  for new code - it would be great to add new Web-Events without even compiling this class.
  </p>

  <p>if you have a hint for me on this topics please mail me: <j.peer@gmx.net></p>
  */
	public WebEvent generatePrimaryEvent() {


		WebEvent e = null;

		String action = ParseUtil.getFirstParameterStartingWith(request, "ac_");
		if(action==null) {
			logCat.info("##### N O O P   ELEMENT ######");
			e = new NoopEvent();

			//#fixme
			String sourcePath = ParseUtil.getParameter(request, "source");

			int firstSlash = sourcePath.indexOf('/');
			if(firstSlash!=-1) {
				int secondSlash = sourcePath.indexOf('/', firstSlash+1);
				if(secondSlash != -1) {
					sourcePath = sourcePath.substring(secondSlash);
				}
			}

			e.setFollowUp(sourcePath);
			logCat.info("followup="+e.getFollowUp());
			return e;
		}


		logCat.info("*** action = "+action+" ***");

		if(action.startsWith("ac_insert")) {

			logCat.info("about to instanciate InsertEvent");
		  e = new InsertEvent(action, request, config);

		} else if(action.startsWith("ac_update_")) {

			logCat.info("about to instanciate UpdateEvent");
			e = new UpdateEvent(action, request, config);

		} else if(action.startsWith("ac_updatear_")) {

			logCat.info("about to instanciate UpdateEvent with key-info in associated radio");
			String associatedRadioName = ParseUtil.getParameter(request, "data"+action+"_arname");
			String keyString = ParseUtil.getParameter(request, associatedRadioName);

			if(keyString!=null) {
				int tableId = ParseUtil.getEmbeddedStringAsInteger(keyString, 0, '_');
				String keyId = ParseUtil.getEmbeddedString(keyString, 1, '_');
				e = new UpdateEvent(tableId, keyId, request, config);
			} else {
				logCat.info("EmptyEvent installed1");
				e = new EmptyEvent(action, request);
				((Vector) request.getAttribute("errors")).addElement(new WebEventException("Radio button '"+associatedRadioName+"' needs to be selected!"));
			}

		} else if(action.startsWith("ac_delete_")) {

			logCat.info("about to instanciate DeleteEvent");
			e = new DeleteEvent(action, request, config);

		} else if(action.startsWith("ac_deletear_")) {

			logCat.info("about to instanciate DeleteEvent with key-info in associated radio");
			String associatedRadioName = ParseUtil.getParameter(request, "data"+action+"_arname");
			String keyString = ParseUtil.getParameter(request, associatedRadioName);

			if(keyString!=null) {
				int tableId = ParseUtil.getEmbeddedStringAsInteger(keyString, 0, '_');
				String keyId = ParseUtil.getEmbeddedString(keyString, 1, '_');
				e = new DeleteEvent(tableId, keyId, request, config);
			} else {
				logCat.info("EmptyEvent installed2");
				e = new EmptyEvent(action, request);
				((Vector) request.getAttribute("errors")).addElement(new WebEventException("Radio button '"+associatedRadioName+"' needs to be selected!"));
			}

		} else if(action.startsWith("ac_first_")) {

			logCat.info("about to instanciate NavFirstEvent");
			e = new NavFirstEvent(action, request, config);

		} else if(action.startsWith("ac_prev_")) {

			logCat.info("about to instanciate NavPrevEvent");
			e = new NavPrevEvent(action, request, config);

		} else if(action.startsWith("ac_next_")) {

			logCat.info("about to instanciate NavNextEvent");
			e = new NavNextEvent(action, request, config);

		} else if(action.startsWith("ac_last_")) {

			logCat.info("about to instanciate NavLastEvent");
			e = new NavLastEvent(action, request, config);

		} else if(action.startsWith("ac_new_")) {

			logCat.info("about to instanciate NavNewEvent");
			e = new NavNewEvent(action, request, config);

		} else if(action.startsWith("ac_goto_")) {

			logCat.info("about to instanciate GotoEvent");
			e = new GotoEvent(action, request, config);

		}
		// now we have to find the followup-site the app-developer wants us to display.
		// there are 2 possibilities
		// §1 the submitted action button has a "follow-up"  - attribute.
		// §2 the submitted <dbforms:form> has set a follow-up attribute
		// §1 overrules §2
		// by the way: follow ups will not be determined for secundary events!
		// check if §1-followup exists:
		String followUp = ParseUtil.getParameter(request, "data"+action+"_fu");
		if(followUp == null) { // if not...
			// ...then check if §2-followup exists (should always exist!)
			followUp = ParseUtil.getParameter(request, "fu_"+e.getTableId());
		}

		logCat.info("setting follow up to:"+followUp);

		e.setFollowUp(followUp);

		return e;
	}

  /**
  <p>independet of the primary - Action there may be some data to update
  accoring to some changes in the input-widgets! in fact, all secunary events
  are (sql-) UPDATES!</p>

  <p>interferrences between primary and secundary events could lead to problems</p>
  <ul>
  <li>i.e. primary event is the "delete" of row "n". if the controller would try to
  update "n", it would raise an sql expcetion, because the table does not exist any more
  <li>to avoid such problems, the parameter "exclude" will be checked for each update and
  if it affects the same data, the update-event will not be created.
  </ul>
  */
	public Enumeration generateSecundaryEvents(WebEvent exclude)	{

		Vector result = new Vector();
		Vector vAc = ParseUtil.getParametersStartingWith(request, "autoupdate_");

		int excludeTableId = -1;
		String excludeKeyId = null;
		boolean collissionDanger = false;

		// first of all, we check if there is some real potential for collisions in the "to exclude"-event
		if(exclude instanceof UpdateEvent || exclude instanceof DeleteEvent) {
			collissionDanger = true;
			excludeTableId = exclude.getTableId();

			//#checkme - this style of OOP doesn't look not very well
			if(exclude instanceof UpdateEvent)
				excludeKeyId = ((UpdateEvent) exclude).getKeyId();
			else
				excludeKeyId = ((DeleteEvent) exclude).getKeyId();
		}

		for(int i = 0; i<vAc.size(); i++) {
			String param = (String) vAc.elementAt(i);

			// auto-updating may be disabled, so we have to check:
			if(ParseUtil.getParameter(request, param).equalsIgnoreCase("true")) {

				// let's find the id of the next table we may update
				int tableId = Integer.parseInt(param.substring("autoupdate_".length()));

				// we can only update existing rowsets. so we just look for key-values

				String paramStub = "k_"+tableId+"_";
				Enumeration keysOfCurrentTable = ParseUtil.getParametersStartingWith(request, paramStub).elements();

				while(keysOfCurrentTable.hasMoreElements()) {
					String aKeyParam = (String) keysOfCurrentTable.nextElement();
					String keyId = aKeyParam.substring(paramStub.length());

					logCat.info("autoaupdate debug info: keyId="+keyId+" excludeKeyId="+excludeKeyId);

					if(!collissionDanger || excludeTableId!=tableId || !keyId.equals(excludeKeyId) ) {

						UpdateEvent e = new UpdateEvent(tableId, keyId, request, config);
						result.addElement(e);

					}
				}

			}
		}

		return result.elements();

	}



}