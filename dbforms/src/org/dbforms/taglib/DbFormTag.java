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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */

package org.dbforms.taglib;

import java.util.*;
import java.sql.*;
import java.io.*;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.dbforms.*;
import org.dbforms.util.*;
import org.dbforms.event.*;

import org.apache.log4j.Category;

/****
 *
 * <p>this is the root element of a data manipulation form</p>
 *
 * @author Joachim Peer <j.peer@gmx.net>
 */

public class DbFormTag extends BodyTagSupport {

	static Category logCat = Category.getInstance(DbFormTag.class.getName());
	// logging category for this class

	private DbFormsConfig config; // access data defined in dbforms-config.xml

	private int tableId; // the id of the underlying table
	private Table table; // the underlying table
	private String tableName; // the name of the  underlying table
	private String maxRows; // count of this fomr (n ||)

	private int count;
	// count (multiplicity, view-mode) of this form (n || -1), whereby n E N (==1,2..z)
	private ResultSetVector resultSetVector; // the data to be rendered
	private String position; // String formatted like {1:3:345-2:4:hugo}
	private int currentCount = 0;
	// holds information about how many times the body of this tag has been rendered
	private String positionPath, positionPathCore;
	// important in subforms: 5th row of subform in 2nd row of main form has path: "5@2"; in mainforms
	private boolean footerReached = false;
	// if rendering of header and body of the form is completed and only the footer needs to be rendered yet

	private String multipart; // is either "true" or "false"
	private boolean _isMultipart = false;
	// multipart must be set to "true" if the form contains file-upload tags or if the form may be invoked by a form containing file-upload tags

	private String followUp;
	// site to be invoked after action - nota bene: this followUp may be overruled by "followUp"-attributes of actionButtons
	private String followUpOnError;
	// site to be invoked after action if previous form contained errors- nota bene: this followUp may be overruled by "followUp"-attributes of actionButtons
	
	private String target;
	// pedant to the html-target attribute in html-form tag: the target frame to jump to
	private String autoUpdate = "false";
	// if "true", at every action (navigation, insert, update, etc.) all input fields of ALL currently rendered rowsets are parsed and updated. many rows may be affected.
	// if "false", updates are only performed if an explicite "update"- action is launched (normally by hittig the updateAction-button). Up to 1 row may be affected.

	private DbFormTag parentForm;
	private String parentField;
	// used in sub-form:  field(s) in the main form that is/are linked to this form
	private String childField;
	// used in sub-form:  field(s) in this forme that is/are linked to the parent form
	private FieldValue[] childFieldValues;
	// used in sub-form: this data structure holds the linked childfield(s) and their current values it/they derive from the main form
	private FieldValue[] filterFieldValues;
	private boolean isSubForm = false;

	private String orderBy;
	private String filter;
	private String gotoPrefix;
	//private String gotoPos;
	private Hashtable gotoHt = null;
	private FieldValue[] overrulingOrder;
	private Vector overrulingOrderFields;

	private String localWebEvent;

	private Connection con;

	private StringBuffer childElementOutput; // #fixme: description

	private String whereClause; // Free-form select query
	private String tableList; // Supply table name list
	//----------------- Property Getters and Setters ----------------------------------------------

	// (most of them get called by the JSP container)

	public void setTableName(String tableName) {
		this.tableName = tableName;
		this.table = config.getTableByName(tableName);
		this.tableId = table.getId();
	}

	public String getTableName() {
		return tableName;
	}

	public Table getTable() {
		return table;
	}

	public void setMaxRows(String maxRows) {
		this.maxRows = maxRows;
		if (maxRows.trim().equals("*"))
			this.count = 0;
		else
			this.count = Integer.parseInt(maxRows);
	}

	public String getMaxRows() {
		return maxRows;
	}

	// --- navigation & positioning stuff

	public int getCount() {
		return count;
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public void increaseCurrentCount() {
		currentCount++;
	}

	// this method gets called at every body-evaluation in order to keep pos.path up-to-date
	public void updatePositionPath() {
		StringBuffer positionPathBuf = new StringBuffer();
		positionPathBuf.append(this.currentCount);
		positionPathBuf.append("@");
		positionPathBuf.append(this.positionPathCore);
		this.positionPath = positionPathBuf.toString();
		// ie. "5" + "@" + "root", "5" + "@" + "123@35@root"
	}

	public String getPositionPath() {
		return positionPath;
	}

	public String getPositionPathCore() {

		return positionPathCore; // ie.  "root",  "123@35@root"

	}

	public void setFollowUp(String followUp) {
		this.followUp = followUp;
	}

	public String getFollowUp() {
		return followUp;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTarget() {
		return target;
	}

	public void setAutoUpdate(String autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

	public String getAutoUpdate() {
		return autoUpdate;
	}

	public void setParentField(String parentField) {
		this.parentField = parentField;
	}

	public String getParentField() {
		return parentField;
	}

	public void setChildField(String childField) {
		this.childField = childField;
	}

	public String getChildField() {
		return childField;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setFilter(String filter) {
		this.filter = filter;

		initFilterFieldValues();
	}

	public String getFilter() {
		return filter;
	}

	public void setGotoPrefix(String gotoPrefix) {
		this.gotoPrefix = gotoPrefix;
	}

	public String getGotoPrefix() {
		return gotoPrefix;
	}

	public void setGotoHt(Hashtable gotoHt) {
		this.gotoHt = gotoHt;
		//this.gotoHt = (Hashtable) pageContext.getAttribute(gotoPos);
	}

	public Hashtable getGotoHt() {
		return gotoHt;
	}

	public void setMultipart(String multipart) {
		this.multipart = multipart;

		this._isMultipart = "true".equals(multipart);
	}

	public String getMultipart() {
		return multipart;
	}

	public boolean hasMultipartCapability() {
		return this._isMultipart;
	}
	/*
		public boolean isMultipartCapable() {
			return isMultipart;
		}*/

	public void setConnection(Connection con) {
		this.con = con;
	}

	public Connection getConnection() {
		return con;
	}

	public void setFooterReached(boolean footerReached) {
		this.footerReached = footerReached;
	}

	public boolean getFooterReached() {
		return footerReached;
	}

	public void setLocalWebEvent(String localWebEvent) {
		this.localWebEvent = localWebEvent;
	}

	public String getLocalWebEvent() {
		return localWebEvent;
	}

	public ResultSetVector getResultSetVector() {
		return resultSetVector;
	}

	public boolean isSubForm() {
		return isSubForm;
	}

	public StringBuffer getChildElementOutput() {
		return childElementOutput;
	}

	public void setWhereClause(String wc) {
		this.whereClause = wc;
	}

	public String getWhereClause() {
		return whereClause;
	}

	// this method is called by child elements (i.e. <db:body>, etc.) to append some tags
	// the cumulated string gets written at DbFormTag:doEndTag()
	public void appendToChildElementOutput(String str) {
		this.childElementOutput.append(str);
	}

	//----------------- Taglib Lifecyle + infrastructural methods --------------------------------

	public void setPageContext(PageContext pc) {
		super.setPageContext(pc);
		config =
			(DbFormsConfig) pageContext.getServletContext().getAttribute(
				DbFormsConfig.CONFIG);
		if (config == null)
			throw new IllegalArgumentException("Troubles with DbForms config xml file: can not find CONFIG object in application context! check system configuration! check if application crashes on start-up!");
		DbConnection aDbConnection = config.getDbConnection();
		if (aDbConnection == null)
			throw new IllegalArgumentException("Troubles in your DbForms config xml file: DbConnection not properly included - check manual!");
		con = aDbConnection.getConnection();
		logCat.debug("Created new connection - " + con);

		if (con == null)
			throw new IllegalArgumentException(
				"JDBC-Troubles: was not able to create connection, using the following DbConnection:"
					+ aDbConnection.toString());
	}

	/***************************************************
	* Grunikiewicz.philip@hydro.qc.ca
	* 2001-08-17
	*
	* Added an attribute to allow developer to bypass navigation:
	*  	If we are not going to use navigation, eliminate the
	* 	creation of, and execution of "fancy!" queries.
	*
	*
	* Grunikiewicz.philip@hydro.qc.ca
	* 2001-10-22
	*
	* Sometimes we use dbForms to simply display information on screen but wish to call another
	* servlet to process the post.  I've added the 'action' attribute to allow a developer to
	* set the wanted action instead of defaulting to the dbforms control servlet.
	* Important: Use a fully qualified path.
	* 
	* 
	* Grunikiewicz.philip@hydro.qc.ca
	* 2001-11-28
	* 
	* Introducing Free-form select queries.  Using the whereClause attribute, developers
	* are now able to specify the ending of an actual DbForms query.  Dbforms generates 
	* the "Select From" part, and the developers have the responsibility 
	* of completing the query. ie: adding the whereClause, an orderBy, etc...
	* Note that using this function renders navigation impossible!
	*
	****************************************************/

	public int doStartTag() {

		try {

			// *************************************************************
			//  Part I - checking user access right, processing interceptor
			// *************************************************************
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

			logCat.info("servlet path = " + request.getServletPath());
			logCat.info("servlet getPathInfo = " + request.getPathInfo());
			logCat.info("servlet getPathTranslated = " + request.getPathTranslated());
			logCat.info("servlet getContextPath = " + request.getContextPath());

			logCat.debug("pos1");
			// part I/a - security

			JspWriter out = pageContext.getOut();

			logCat.debug("pos2");

			// check user privilege
			if (table != null
				&& !table.hasUserPrivileg(request, GrantedPrivileges.PRIVILEG_SELECT)) {
				logCat.debug("pos3");
				out.println(
					"Sorry, viewing data from table "
						+ table.getName()
						+ " is not granted for this session.");
				return SKIP_BODY;
			}

			logCat.debug("pos4");

			// part II/b - processing interceptors

			if (table != null && table.hasInterceptors()) {
				try {
					logCat.debug("pos5");
					table.processInterceptors(
						DbEventInterceptor.PRE_SELECT,
						request,
						null,
						config,
						con);
				} catch (Exception sqle) {
					logCat.debug("pos6");
					out.println(
						"Sorry, viewing data from table "
							+ table.getName()
							+ " would violate a condition.");
					return SKIP_BODY;
				}
			}

			logCat.debug("pos7");

			// *************************************************************
			//  Part II - WebEvent infrastructural stuff ----
			//  write out data for the controller so that it
			//  knows what it deals with if user hits a button
			// *************************************************************

			StringBuffer tagBuf = new StringBuffer();

			// explicitly re-set all instance variables which get changed during evaluation loops
			// and which are not reset by the jsp container trough setXxx() methods and
			logCat.info("resetting values of tag");
			currentCount = 0;
			position = null;
			footerReached = false;
			resultSetVector = null;
			childElementOutput = new StringBuffer();

			logCat.debug("first steps finished");

			// if main form
			// we write out the form-tag which points to the controller-servlet
			if (parentForm == null) {

				tagBuf.append("<form name=\"dbform\" action=\"");

				//Check if developer has overriden action
				if (this.getAction() != null && this.getAction().trim().length() > 0) {
					tagBuf.append(this.getAction());
				} else {
					tagBuf.append(
						response.encodeURL(request.getContextPath() + "/servlet/control"));
				}

				tagBuf.append("\"");

				if (target != null) {
					tagBuf.append(" target=\"");
					tagBuf.append(target);
					tagBuf.append("\"");
				}
				tagBuf.append(" method=\"post\"");

				if (_isMultipart)
					tagBuf.append(" enctype=\"multipart/form-data\"");

				tagBuf.append(">");
				// supports RFC 1867 - multipart upload, if some database-fields represent filedata

				if (tableName == null) {
					// if form is an emptyform -> we've fineshed yet - cancel all further activities!
					out.println(tagBuf.toString());
					return EVAL_BODY_TAG;
					;
				}

				positionPathCore = "root";

			} else { // if sub-form, we dont write out html tags; this has been done already by a parent form
				this.isSubForm = true;

				positionPathCore = parentForm.getPositionPath();

				// If whereClause is not supplied by developer
				// determine the value(s) of the linked field(s)				
				if ((this.getWhereClause() == null)
					|| (this.getWhereClause().trim().length() == 0)) {
					initChildFieldValues();
					if (childFieldValues == null)
						return SKIP_BODY;
				}
			} // write out involved table
			tagBuf.append(
				"<input type=\"hidden\" name=\"invtable\" value=\"" + tableId + "\">");
			// write out the autoupdate-policy of this form
			tagBuf.append(
				"<input type=\"hidden\" name=\"autoupdate_"
					+ tableId
					+ "\" value=\""
					+ autoUpdate
					+ "\">");
			// write out the followup-default for this table
			tagBuf.append(
				"<input type=\"hidden\" name=\"fu_"
					+ tableId
					+ "\" value=\""
					+ followUp
					+ "\">");
					
					
			// write out the followupOnError-default for this table
			if (getFollowUpOnError() != null && getFollowUpOnError().trim().length() > 0)
			{
			tagBuf.append(
				"<input type=\"hidden\" name=\"fue_"
					+ tableId
					+ "\" value=\""
					+ getFollowUpOnError()
					+ "\">");
			}	
					
			// write out source-tag
			tagBuf.append(
				"<input type=\"hidden\" name=\"source\" value=\""
					+ request.getRequestURI()
					+ "\">");
			// *************************************************************
			//  Part III - fetching Data. This data is provided to all sub-
			//  elements of this form.
			// *************************************************************
			this.initOverrulingOrder(request);
			// III/1: first we must determinate the ORDER clause to apply to the query
			// the order may be specified in the dbforms-config.xml file or in the current JSP file.
			boolean useDefaultOrder = true;
			// tells us which definintion of field order (global or local) to use
			FieldValue[] orderConstraint;
			Vector orderFields;
			if (this.overrulingOrder != null) {
				// if developer provided orderBy - Attribute in <db:dbform> - tag
				orderConstraint = overrulingOrder;
				orderFields = overrulingOrderFields;
				useDefaultOrder = false;
				logCat.info("using OverrulingOrder");
			} else {
				// if developer provided orderBy - Attribute globally in dbforms-config.xml - tag
				FieldValue[] tmpOrderConstraint = table.getDefaultOrder();
				orderConstraint = new FieldValue[tmpOrderConstraint.length];
				for (int i = 0; i < tmpOrderConstraint.length; i++) {
					orderConstraint[i] = (FieldValue) tmpOrderConstraint[i].clone();
					// cloning is necessary to keep things thread-safe! (we manipulate some fields in this structure.)
				}
				orderFields = table.getDefaultOrderFields();
				logCat.info("using DefaultOrder");
			} // an orderBY - clause is a MUST. we can't query well without it.
			if (orderConstraint == null)
				throw new IllegalArgumentException("OrderBy-Clause must be specified either in table-element in config.xml or in dbform-tag on jsp view");
			// III/2:
			// is there a POSITION we are supposed to navigate to?
			// positions are key values: for example "2", oder "2~454"
			//String position = pageContext.getRequest().getParameter("pos_"+tableId);
			String firstPosition = ParseUtil.getParameter(request, "firstpos_" + tableId);
			String lastPosition = ParseUtil.getParameter(request, "lastpos_" + tableId);
			if (firstPosition == null)
				firstPosition = lastPosition;
			if (lastPosition == null)
				lastPosition = firstPosition;
			// if we are in a subform we must check if the fieldvalue-list provided in the
			// position strings is valid in the current state
			// it might be invalid if the position of the parent form has been changed (by a navigation event)
			// (=> the position-strings of childforms arent valid anymore)
			if (childFieldValues != null && firstPosition != null) {
				if (!checkLinkage(childFieldValues,
					firstPosition)) { // checking one of the 2 strings is sufficient
					// the position info is out of date. we dont use it.
					firstPosition = null;
					lastPosition = null;
				}
			}

			logCat.info("firstposition " + firstPosition);
			logCat.info("lastPosition " + lastPosition);
			FieldValue[] mergedFieldValues = null;
			if (childFieldValues == null) {
				mergedFieldValues = filterFieldValues;
			} else
				if (filterFieldValues == null) {
					mergedFieldValues = childFieldValues;
				} else {
					mergedFieldValues =
						new FieldValue[childFieldValues.length + filterFieldValues.length];
					System.arraycopy(
						childFieldValues,
						0,
						mergedFieldValues,
						0,
						childFieldValues.length);
					System.arraycopy(
						filterFieldValues,
						0,
						mergedFieldValues,
						childFieldValues.length,
						filterFieldValues.length);
				} // if we just habe a search request we do not need any other constraints
			FieldValue[] searchFieldValues = initSearchFieldValues();
			if (searchFieldValues != null) {
				mergedFieldValues = searchFieldValues;
			} /*
			
			in the code above we examined lots of information which determinates  _which_ resultset
			gets retrieved and the way this operation will be done.
			
			which are the "parameters" we have (eventually!) retrieved
			============================================================
			
			*) 	WebEvent Object in request: if the jsp containing this tag was invoked by
				the controller, then there is a Event which has been processed (DatebasEvents)
				or which waits to be processed (NavigationEvents, including GotoEvent)
			
			*) 	firstPos, lastPos: Strings containing key-fieldValues and indicating a line
				to go to if we have no other information. this may happen if a subform gets
				navigated. the parentForm is not involved in the operation but must be able
				to "navigate" to its new old position.
				[#checkme: risk of wrong interpreation if jsp calls jsp - compare source tags?]
			
			*) 	mergedFieldValues: this is a cumulation of all rules which restrict the
				result set in any way. it is build of
			
				- 	childFieldValues: restricting a set in a subform that all "childFields" in the
					resultset match their respective "parentFields" in main form. (for instance
					if customerID == 100, we only want to select orders from orders-table
					involving customerID 100)
			
				-	filterFieldValues: if a filter is applied to the resultset we always need
					to select the _filtered_ resultset
			
				- 	searchFieldValues: if a search is performed we just want to show fields
				    belonging to the search result (naturally ;=)
			
			*) 	orderConstraint: this is a cumulation of rules for ordering (sorting)
				and restricting fields.
			
				one part of it is built either from
				a) orderBy - clause of dbform element
				b) orderbY - definition in xml config (XPath: dbform-config/table/field)
				this part tells dbforms which orderby-clause to create
			
				but if we combine this "order constraint" with actual values (the keys
				of a row, for example through "firstPos") then we can build very powerful
				queries allowing us to select exectly what we need. the order plays an important
				role in this game, because the "order constraint" serves us as tool to
				make decisions if a row has to be BEFORE or AFTER an other.
				(compare the rather complex methods FieldValue.getWhereAfterClause(),
				FieldValue.populateWhereAfterClause() and FieldValue.fillWithValues() which
				are doing most of that stuff describe above)
			
			
			*) 	count: this is a property of DbFormTag. Its relevance is that certain operations
				need to be performed differently if count==0, which means the form is an
				"endless form".
			
			*/ // III/3: fetching data (compare description above)
			// this code is still expermintal, pre alpha! We need to put this logic into
			// etter (more readable, maintainable) code  (evt. own method or class)!
			// is there a NAVIGATION event (like "nav to first row" or "nav to previous row", or "nav back 4 rows"?
			// if so...
			WebEvent webEvent = (WebEvent) request.getAttribute("webEvent");

			// test: use BoundedNavEventFactoryImpl [Fossato <fossato@pow2.com>, 2001/11/08]
			NavEventFactory nef = BoundedNavEventFactoryImpl.instance();

			// if there comes no web event from controller, then we check if there is
			// a local event defined on the jsp
			// #fixme!
			// Interimistic solution
			// must be more flexible in final version
			if (webEvent == null && localWebEvent != null) {
				if ("navFirst".equals(localWebEvent)) {
					logCat.debug("instantiating local we:" + localWebEvent);
					webEvent = new NavFirstEvent(this.table, this.config);
				} else
					if ("navLast".equals(localWebEvent)) {
						logCat.debug("instantiating local we:" + localWebEvent);
						webEvent = new NavLastEvent(this.table, this.config);
					} else
						if ("navPrev".equals(localWebEvent)) {
							logCat.debug("instantiating local we:" + localWebEvent);
							//webEvent = new NavPrevEvent(this.table, this.config);
							webEvent = nef.createNavPrevEvent(this.table, this.config);
							// [Fossato <fossato@pow2.com>, 2001/11/08]						
						} else
							if ("navNext".equals(localWebEvent)) {
								logCat.debug("instantiating local we:" + localWebEvent);
								//webEvent = new NavNextEvent(this.table, this.config);
								webEvent = nef.createNavNextEvent(this.table, this.config);
								// [Fossato <fossato@pow2.com>, 2001/11/08]														
							} else
								if ("navNew".equals(localWebEvent)) {
									logCat.debug("instantiating local we:" + localWebEvent);
									webEvent = new NavNewEvent(this.table, this.config);
								}
			}

			if (webEvent != null && webEvent instanceof NavigationEvent) {

				NavigationEvent navEvent = (NavigationEvent) webEvent;
				if (navEvent != null && navEvent.getTableId() == tableId) {

					logCat.info("§§§ NAV/I §§§");
					logCat.info("about to process nav event:" + navEvent.getClass().getName());
					resultSetVector =
						navEvent.processEvent(
							mergedFieldValues,
							orderConstraint,
							count,
							firstPosition,
							lastPosition,
							con);
					if (navEvent instanceof NavNewEvent)
						setFooterReached(true);
					// if we have no navigation event for this table
				} else {

					logCat.info("§§§ NAV/II §§§");
					if (firstPosition != null) {
						table.fillWithValues(orderConstraint, firstPosition);
					} // if there is a navigation event but not for this table, 
					// then just navigate to a position (if it exists) or just select all data 
					// (if no pos or if endless form)
					/******************************************************************************************* 
					 * Grunikiewicz.philip@hydro.qc.ca
					 * 2001-11-28
					 *
					 * If the whereClause attribute has been specified, ignore all and 
					 * apply the whereClause directly in the query.
					 * 
					 ******************************************************************************************/

					if (this.getWhereClause() != null
						&& this.getWhereClause().trim().length() > 0) {
						logCat.info("Free form Select about to be executed");
						resultSetVector =
							table.doFreeFormSelect(
								table.getFields(),
								this.getWhereClause(),
								this.getTableList(),
								count,
								con);
					} else {
						// PG - Check if developer specified to bypass Navigation infrastructure
						resultSetVector =
							table.doConstrainedSelect(
								table.getFields(),
								mergedFieldValues,
								orderConstraint,
								(firstPosition == null || count == 0 || ("true".equals(getBypassNavigation())))
									? FieldValue.COMPARE_NONE
									: FieldValue.COMPARE_INCLUSIVE,
								count,
								con);
					}
				}

			} // or is there a possibiliy of a GOTO EVENT?
			// we need to parse request using the given goto prefix
			else
				if (gotoPrefix != null && gotoPrefix.length() > 0) {

					logCat.info("§§§ NAV GOTO §§§");
					Vector v = ParseUtil.getParametersStartingWith(request, gotoPrefix);
					gotoHt = new Hashtable();
					for (int i = 0; i < v.size(); i++) {

						String paramName = (String) v.elementAt(i);
						String fieldName = paramName.substring(gotoPrefix.length());
						logCat.debug("fieldName=" + fieldName);
						String fieldValue = ParseUtil.getParameter(request, paramName);
						logCat.debug("fieldValue=" + fieldValue);
						if (fieldName != null && fieldValue != null)
							gotoHt.put(fieldName, fieldValue);
					}

					if (gotoHt.size() > 0) {
						String positionString = table.getPositionStringFromFieldAndValueHt(gotoHt);
						GotoEvent ge = new GotoEvent(positionString, table);
						resultSetVector =
							ge.processEvent(
								mergedFieldValues,
								orderConstraint,
								count,
								firstPosition,
								lastPosition,
								con);
					}

				} // we got a goto-hashtable directly from the attribute
			else
				if (gotoHt != null) {

					if (gotoHt.size() > 0) {
						String positionString = table.getPositionStringFromFieldAndValueHt(gotoHt);
						GotoEvent ge = new GotoEvent(positionString, table);
						resultSetVector =
							ge.processEvent(
								mergedFieldValues,
								orderConstraint,
								count,
								firstPosition,
								lastPosition,
								con);
					}

				} //
			// kindof "else" branch
			//
			if ((webEvent == null || !(webEvent instanceof NavigationEvent))
				&& (gotoHt == null || gotoHt.size() == 0)) {

				if ((firstPosition != null)
					&& (overrulingOrder == null
						|| webEvent instanceof DatabaseEvent
						|| webEvent instanceof NoopEvent
						|| webEvent instanceof EmptyEvent)) {
					// we have someting to look for, and we have _no_ change of search criteria #fixme - explain better
					/******************************************************************************************* 
					 * Grunikiewicz.philip@hydro.qc.ca
					 * 2001-11-28
					 *
					 * If the whereClause attribute has been specified, ignore all and 
					 * apply the whereClause directly in the query.
					 * 
					 ******************************************************************************************/

					if (this.getWhereClause() != null
						&& this.getWhereClause().trim().length() > 0) {
						logCat.info("Free form Select about to be executed");
						resultSetVector =
							table.doFreeFormSelect(
								table.getFields(),
								this.getWhereClause(),
								this.getTableList(),
								count,
								con);
					} else {

						logCat.info("§§§ B/I §§§");
						table.fillWithValues(orderConstraint, firstPosition);
						resultSetVector =
							table.doConstrainedSelect(
								table.getFields(),
								mergedFieldValues,
								orderConstraint,
								(count == 0 || ("true".equals(getBypassNavigation())))
									? FieldValue.COMPARE_NONE
									: FieldValue.COMPARE_INCLUSIVE,
								count,
								con);
					} // in endless forms we never need to to compares
					logCat.info("we had someting to look for:" + firstPosition);
				} else {
					// we have no navigation object and no position we are supposed to go. so let's go to the first row.
					logCat.info("§§§ B/II §§§");
					Vector errors = (Vector) request.getAttribute("errors");
					if (count != 0
						&& webEvent != null
						&& webEvent instanceof InsertEvent
						&& errors != null
						&& errors.size() > 0) {
						logCat.info("B/II/1 redirecting to insert mode after faild db operation");
						//resultSetVector = new ResultSetVector();
						//resultSetVector.setPointer(0);
						resultSetVector = null;
						setFooterReached(true);
					} else { /******************************************************************************************* 
					* Grunikiewicz.philip@hydro.qc.ca
					* 2001-11-28
					*
					* If the whereClause attribute has been specified, ignore all and 
					* apply the whereClause directly in the query.
					* 
					******************************************************************************************/

						if (this.getWhereClause() != null
							&& this.getWhereClause().trim().length() > 0) {
							logCat.info("Free form Select about to be executed");
							resultSetVector =
								table.doFreeFormSelect(
									table.getFields(),
									this.getWhereClause(),
									this.getTableList(),
									count,
									con);
						} else {
							logCat.info("§§§ B/II/2 §§§");
							resultSetVector =
								table.doConstrainedSelect(
									table.getFields(),
									mergedFieldValues,
									orderConstraint,
									(firstPosition == null || ("true".equals(getBypassNavigation())))
										? FieldValue.COMPARE_NONE
										: FieldValue.COMPARE_INCLUSIVE,
									count,
									con);
						}
					}

				}

			} // *** DONE! We have now the underlying data, and this data is accessible to all sub-elements (labels, textFields, etc. of this form ***
			// *************************************************************
			//  Part IV - Again, some WebEvent infrastructural stuff:
			//  write out data indicating the position we have
			//  navigated to withing the rowset
			// *************************************************************
			// we process interceptor again (post-select)
			// #checkme: is the overhead of a POST_SELECT interceptor necessary or a luxury? => use cases!
			if (table != null && table.hasInterceptors()) {
				try {
					// process the interceptors associated to this table
					table.processInterceptors(
						DbEventInterceptor.POST_SELECT,
						request,
						null,
						config,
						con);
				} catch (SQLException sqle) {
					// PG = 2001-12-04
					// No need to add extra comments, just re-throw exceptions as SqlExceptions
					throw new SQLException(sqle.getMessage());
				} catch (MultipleValidationException mve) {
					// PG, 2001-12-14
					// Support for multiple error messages in one interceptor
					throw new MultipleValidationException(mve.getMessages());
				}
			} // End of interceptor processing
			// determinate new position-strings (== value of the first and the last row of the current view)
			if (resultSetVector != null) {
				resultSetVector.setPointer(0);
				firstPosition = table.getPositionString(resultSetVector);
				resultSetVector.setPointer(resultSetVector.size() - 1);
				lastPosition = table.getPositionString(resultSetVector);
				resultSetVector.setPointer(0);
			}

			if (!footerReached) { // if not in insert mode
				if (firstPosition != null)
					tagBuf.append(
						"<input type=\"hidden\" name=\"firstpos_"
							+ tableId
							+ "\" value=\""
							+ firstPosition
							+ "\">");
				if (lastPosition != null)
					tagBuf.append(
						"<input type=\"hidden\" name=\"lastpos_"
							+ tableId
							+ "\" value=\""
							+ lastPosition
							+ "\">");
			} // construct TEI variables for access from JSP
			// # jp 27-06-2001: replacing "." by "_", so that SCHEMATA can be used
			pageContext.setAttribute(
				"searchFieldNames_" + tableName.replace('.', '_'),
				getNamesHashtable("search"));
			pageContext.setAttribute(
				"searchFieldModeNames_" + tableName.replace('.', '_'),
				getNamesHashtable("searchmode"));
			pageContext.setAttribute(
				"searchFieldAlgorithmNames_" + tableName.replace('.', '_'),
				getNamesHashtable("searchalgo"));
			// #fixme:
			// this is a weired crazy workaround [this code is also used in DbBodyTag!!]
			// why?
			// #fixme: explaination! -> initBody, spec, jsp container synchronizing variables, etc.
			if (!ResultSetVector.isEmptyOrNull(resultSetVector)) {

				pageContext.setAttribute("rsv_" + tableName.replace('.', '_'), resultSetVector);
				pageContext.setAttribute(
					"currentRow_" + tableName.replace('.', '_'),
					resultSetVector.getCurrentRowAsHashtable());
				pageContext.setAttribute(
					"position_" + tableName.replace('.', '_'),
					table.getPositionString(resultSetVector));
			}

			out.println(tagBuf.toString());
		} catch (IOException e) {
			return SKIP_BODY;
		} catch (SQLException ne) {
			logCat.error(ne);
			return SKIP_BODY;
		} catch (MultipleValidationException mve) {
			logCat.error(mve);
			return SKIP_BODY;
		}

		return EVAL_BODY_TAG;
	}

	public int doAfterBody() throws JspException {

		if (resultSetVector == null || resultSetVector.size() == 0)
			return SKIP_BODY;
		// rsv may be null in empty-forms (where not tableName attribute is provided)
		if (!footerReached) {
			return EVAL_BODY_TAG;
		} else {
			return SKIP_BODY;
		}
	}

	public int doEndTag() throws JspException {
		try {
			if (bodyContent != null)
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			logCat.debug("pageContext.getOut()=" + pageContext.getOut());
			logCat.debug("childElementOutput=" + childElementOutput);
			// hidden fields and other stuff coming from child elements get written out
			if (childElementOutput != null)
				pageContext.getOut().println(childElementOutput.toString());
			if (parentForm == null)
				pageContext.getOut().println("</form>");
		} catch (IOException ioe) {
			logCat.error(ioe);
		}

		logCat.info("end reached of " + tableName);
		return EVAL_PAGE;
	}

	public void release() {
		super.release();
		followUp = null;
		followUpOnError = null;		
		// The connection should not be null - If it is, then you might have an infrastructure problem!
		// Be sure to look into this!  Hint: check out your pool manager's performance! 
		if (con != null) {

			try {
				logCat.debug("About to close connection - " + con);
				con.close();
				logCat.debug("Connection closed");
			} catch (java.sql.SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	public void setParent(Tag p) {
		super.setParent(p);
		this.parentForm = (DbFormTag) findAncestorWithClass(this, DbFormTag.class);
	} //------------------------ business, helper & utility methods --------------------------------
	/**
	 * initialise datastructures containing informations about how table should be orderd
	 * the information is specified in the JSP this tags lives in. this declaration OVERRULES
	 * other default declarations eventually done in XML config!
	 * (compara Table.java !)
	 */
	private void initOverrulingOrder(HttpServletRequest request) {

		HttpServletRequest localRequest = request;
		// if page A links to page B (via a gotoButton, for instance) then we do not
		// want  A's order constraints get applied to B
		if (localRequest != null) {

			String refSource = localRequest.getRequestURI();
			String sourceTag = ParseUtil.getParameter(localRequest, "source");
			logCat.info("!comparing page " + refSource + " TO " + sourceTag);
			/*
							if(sourceTag==null || !sourceTag.equals(refSource)) {
								localRequest = null;
							}*/

			if (sourceTag != null && !refSource.equals(sourceTag)) {
				localRequest = null;
			}

		}

		logCat.debug("orderBy=" + orderBy);
		logCat.debug("localRequest=" + localRequest);
		// if we have neither an orderby clause nor a request we may use then we cant create orderconstraint
		if (orderBy == null && localRequest == null)
			return;
		// otherwise we can:
		overrulingOrder = table.createOrderFieldValues(orderBy, localRequest, false);
		overrulingOrderFields = new Vector();
		if (overrulingOrder != null) {
			for (int i = 0; i < overrulingOrder.length; i++)
				overrulingOrderFields.addElement(overrulingOrder[i].getField());
		}
	} /**
	* this method is only used if this class is instantiated as sub-form (== embedded in another
	* form's body tag)
	*
	* it initializes the data array childFieldValues, which holds the linked childfield(s) and
		 * their current values it/they derive from the main form
	*/
	public void initChildFieldValues() { // if parent form has no data, we can not render a subform!
		if (ResultSetVector.isEmptyOrNull(parentForm.getResultSetVector())) {
			childFieldValues = null;
			// childFieldValues remains null
			return;
		} // 1 to n fields may be mapped
		Vector childFieldNames = ParseUtil.splitString(childField, ",;~");
		Vector parentFieldNames = ParseUtil.splitString(parentField, ",;~");
		// do some basic checks
		// deeper checks like Datatyp-compatibility,etc not done yet
		int len = childFieldNames.size();
		if (len == 0 || len != parentFieldNames.size())
			return;
		// get parent-table
		// we need it in order to fetch the actual values of the mapped fields
		// for example if we mapped child::cust_id to parent::id, then we need
		// to know the current value of parent::id in order to do a contrained select
		// for our subform
		Table parentTable = parentForm.getTable();
		childFieldValues = new FieldValue[len];
		for (int i = 0; i < len; i++) {

			String parentFieldName = (String) parentFieldNames.elementAt(i);
			Field parentField = parentTable.getFieldByName(parentFieldName);
			String childFieldName = (String) childFieldNames.elementAt(i);
			Field childField = this.table.getFieldByName(childFieldName);
			String currentParentFieldValue = null;
			if (!ResultSetVector.isEmptyOrNull(parentForm.getResultSetVector())) {
				currentParentFieldValue =
					parentForm.getResultSetVector().getField(parentField.getId());
			}

			childFieldValues[i] = new FieldValue(childField, currentParentFieldValue, true);
		}
	} /**
	grunikiewicz.philip@hydro.qc.ca
	2001-04-26
	
	Original code only took into consideration an = (equal) as operator.
	In order to support >,<,<=,>= (for date intervals), I needed to modify the code
	as follows...
	
		1. I added an instance variable (operator) in the FieldValue class
			to 'remember'the specified operator.
		2. I added a new constructor in the FieldValue class to support the
			setting (initialization) of this new instance variable.
		3. I modified the code below to look for either =,<,<=,>,>= as delimiters.
		4. In the FieldValue class, I replaced  the getWhereEqualsClause() by the
			getWhereClause() method to support the new set of operators.
	
	
	grunikiewicz.philip@hydro.qc.ca
	2001-05-17
	
	Added support for the LIKE operator
	Removed ~ (tilde) as a separator value
	~ (tilde) can be used to specify a LIKE operation.
	
	ie:
		aFieldVal~startWith%
	
	
	grunikiewicz.philip@hydro.qc.ca
	2001-09-11
	
	By default, all filter definitions are ANDed together.
	I added support for logical OR.  Developers may prefix their filter 
	definition with the '|' symbol.  This was a quick and simple way of adding support for OR!
	
	Exemple:
	
		aFieldVal=3, |anotherField>3
	
	would produce the following:
	
		... where aFieldVal=3 OR anotherField>3
		
	
	The FieldValue constructor now contains an additional parameter to indicate 
	that the use of an OR is required
	
		
	========================================================*/

	public void initFilterFieldValues() {
		// 1 to n fields may be mapped
		Vector keyValPairs = ParseUtil.splitString(filter, ",;");
		// ~ no longer used as separator!
		int len = keyValPairs.size();
		filterFieldValues = new FieldValue[len];
		for (int i = 0; i < len; i++) {

			int operator = 0;
			boolean isLogicalOR = false;
			int jump = 1;
			String aKeyValPair = (String) keyValPairs.elementAt(i);
			// i.e "id=2"
			logCat.debug("initFilterFieldValues: aKeyValPair = " + aKeyValPair);
			// Following code could be optimized, however I did not want to make too many changes...
			int n;
			// Check for Not Equal
			if ((n = aKeyValPair.indexOf("<>")) != -1) {
				// Not Equal found! - Store the operation for use later on
				operator = FieldValue.FILTER_NOT_EQUAL;
				jump = 2;
			} else { // Check for GreaterThanEqual
				if ((n = aKeyValPair.indexOf(">=")) != -1) {
					// GreaterThenEqual found! - Store the operation for use later on
					operator = FieldValue.FILTER_GREATER_THEN_EQUAL;
					jump = 2;
				} else { // Check for GreaterThan
					if ((n = aKeyValPair.indexOf('>')) != -1) {
						// GreaterThen found! - Store the operation for use later on
						operator = FieldValue.FILTER_GREATER_THEN;
					} else { // Check for SmallerThenEqual
						if ((n = aKeyValPair.indexOf("<=")) != -1) {
							// SmallerThenEqual found! - Store the operation for use later on
							operator = FieldValue.FILTER_SMALLER_THEN_EQUAL;
							jump = 2;
						} else { // Check for SmallerThen
							if ((n = aKeyValPair.indexOf('<')) != -1) {
								// SmallerThen found! - Store the operation for use later on
								operator = FieldValue.FILTER_SMALLER_THEN;
							} else { // Check for equal
								if ((n = aKeyValPair.indexOf('=')) != -1) {
									// Equal found! - Store the operator for use later on
									operator = FieldValue.FILTER_EQUAL;
								} else { // Check for LIKE
									if ((n = aKeyValPair.indexOf('~')) != -1) {
										// LIKE found! - Store the operator for use later on
										operator = FieldValue.FILTER_LIKE;
									}
								}
							}

						}
					}

				}
			} //  PG - At this point, I have set my operator and I should have a valid index.
			//	Note that the original code did not handle the posibility of not finding an index
			//	(value = -1)...
			String fieldName = aKeyValPair.substring(0, n);
			// i.e "id"
			logCat.debug("Filter field=" + fieldName);
			if (fieldName.charAt(0) == '|') {
				// This filter must be associated to a logical OR, clean out the indicator...
				fieldName = fieldName.substring(1);
				isLogicalOR = true;
			}

			Field filterField = this.table.getFieldByName(fieldName);
			// Increment by 1 or 2 depending on operator
			String value = aKeyValPair.substring(n + jump);
			// i.e. "2"
			logCat.debug("Filter value=" + value);
			// Create a new instance of FieldValue and set the operator variable
			filterFieldValues[i] =
				new FieldValue(filterField, value, false, operator, isLogicalOR);
			logCat.debug("and fv is =" + filterFieldValues[i].toString());
		}
	} /**
	this methods reads search parameters (provided by search-tags, see docu, advance topics/search)
	and build an array of FieldValues containing the search terms the user provided
	#fixme: extend: add attribute SEARCH_ALGORITHM = sharp | weak
	*/
	public FieldValue[] initSearchFieldValues() {

		FieldValue[] fieldValues;
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		Vector searchFieldNames =
			ParseUtil.getParametersStartingWith(request, "search_" + this.tableId);
		if (searchFieldNames == null || searchFieldNames.size() == 0)
			return null;
		Vector mode_and = new Vector();
		Vector mode_or = new Vector();
		for (int i = 0; i < searchFieldNames.size(); i++) {

			String searchFieldName = (String) searchFieldNames.elementAt(i);
			//. i.e search_1_12
			String aSearchFieldValue = ParseUtil.getParameter(request, searchFieldName);
			// ie. search_1_12 is mapped to "john"
			if (aSearchFieldValue != null && aSearchFieldValue.trim().length() > 0) {

				int firstUnderscore = searchFieldName.indexOf('_');
				int secondUnderscore = searchFieldName.indexOf('_', firstUnderscore + 1);
				int tableId =
					Integer.parseInt(
						searchFieldName.substring(firstUnderscore + 1, secondUnderscore));
				// is equal to tableid, off course
				int fieldId = Integer.parseInt(searchFieldName.substring(secondUnderscore + 1));
				Field f = table.getField(fieldId);
				String aSearchMode =
					ParseUtil.getParameter(request, "searchmode_" + tableId + "_" + fieldId);
				int mode =
					("and".equals(aSearchMode))
						? DbBaseHandlerTag.SEARCHMODE_AND
						: DbBaseHandlerTag.SEARCHMODE_OR;
				String aSearchAlgorithm =
					ParseUtil.getParameter(request, "searchalgo_" + tableId + "_" + fieldId);
				int algorithm =
					("weak".equals(aSearchAlgorithm))
						? FieldValue.SEARCH_ALGO_WEAK
						: FieldValue.SEARCH_ALGO_SHARP;
				FieldValue fv = new FieldValue(f, aSearchFieldValue, true);
				fv.setSearchMode(mode);
				fv.setSearchAlgorithm(algorithm);
				if (mode == DbBaseHandlerTag.SEARCHMODE_AND)
					mode_and.addElement(fv);
				else
					mode_or.addElement(fv);
			}
		}

		int andBagSize = mode_and.size();
		int orBagSize = mode_or.size();
		int criteriaFieldCount = andBagSize + orBagSize;
		logCat.info("criteriaFieldCount=" + criteriaFieldCount);
		if (criteriaFieldCount == 0)
			return null;
		// now we construct the fieldValues array
		// we ensure that the searchmodes are not mixed up
		fieldValues = new FieldValue[criteriaFieldCount];
		int i = 0;
		for (i = 0; i < andBagSize; i++) {
			fieldValues[i] = (FieldValue) mode_and.elementAt(i);
		}

		for (int j = 0; j < orBagSize; j++) {
			fieldValues[j + i] = (FieldValue) mode_or.elementAt(j);
		}

		return fieldValues;
	} /**
	* This method is only used if this class is instantiated as sub-form (== embedded in another
	* form's body tag)
	*
	* this method gets called by input-tags like "DbTextFieldTag" and others. they signalize that
	* _they_ will generate the tag for the controller, not this form.
	* see produceLinkedTags()
	*/
	public void strikeOut(Field f) {

		// childFieldValues may be null, if we have
		// a free form select using attribute whereClause
		if (childFieldValues != null) {

			for (int i = 0; i < childFieldValues.length; i++) {
				if (f == childFieldValues[i].getField()
					&& childFieldValues[i].getRenderHiddenHtmlTag()) {
					childFieldValues[i].setRenderHiddenHtmlTag(false);
					logCat.info("stroke out field:" + f.getName());
					return;
				}
			}
		}
	} /**
	* This method is only used if this class is instantiated as sub-form:
	* all linked fields which have not been stroke out get rendered as hidden tags. This is possible
	* because we know the values of the parent form which they are linked with.
	*
	* this method behaves differently if called from body or from footer!
	*
	* #fixme: example or link to docu
	*/
	public String produceLinkedTags() {

		StringBuffer buf = new StringBuffer();
		// childFieldValues may be null, if we have
		// a free form select using attribute whereClause
		if (childFieldValues != null) {
			for (int i = 0; i < childFieldValues.length; i++) {
				if (childFieldValues[i].getRenderHiddenHtmlTag()) {
					buf.append("<input type=\"hidden\" name=\"f_");
					buf.append(tableId);
					buf.append("_");
					//buf.append(footerReached ? "ins"+frozenCumulatedCount : new Integer(frozenCumulatedCount).toString());
					buf.append("ins");
					if (positionPathCore != null) {
						buf.append(positionPathCore);
					}
					buf.append("_");
					buf.append(childFieldValues[i].getField().getId());
					buf.append("\" value=\"");
					buf.append(childFieldValues[i].getFieldValue());
					buf.append("\">");
				}
			}
		}
		return buf.toString();
	} /**
	* if we are in a subform we must check if the fieldvalue-list provided in the
	* position strings is valid in the current state
	* it might be invalid if the position of the parent form has been changed (by a navigation event)
	* (=> the position-strings of childforms arent valid anymore)
	
	private boolean checkLinkage(FieldValue[] childFieldValues, String aPosition) {
	
		// at first build a array of the provided values
		StringTokenizer st = new StringTokenizer(aPosition, "~");
		int cnt = st.countTokens();
		String[] values = new String[cnt];
		for(int i=0; i<cnt; i++) values[i] = st.nextToken();
	
		for(int i=0; i<childFieldValues.length; i++) {
	
			String actualValue = childFieldValues[i].getFieldValue();
			Field f = childFieldValues[i].getField();
			String valueInPos = values[f.getId()];
	
			logCat.info("comparing "+actualValue+" TO "+valueInPos);
	
			if(!actualValue.trim().equals(valueInPos.trim())) return false;
		}
	
		return true;
	}
	*/ /**
		 * if we are in a subform we must check if the fieldvalue-list provided in the
		 * position strings is valid in the current state
		 * it might be invalid if the position of the parent form has been changed (by a navigation event)
		 * (=> the position-strings of childforms arent valid anymore)
		 */
	private boolean checkLinkage(FieldValue[] childFieldValues, String aPosition) {
		// at first build a hashtable of the provided values
		Hashtable ht = table.getFieldValuesFromPositionAsHt(aPosition);
		/* just for debugging...
		    if(ht==null) {
				logCat.debug("cl hashtable  null");
			}
			else {
				logCat.debug("cl hashtable  not null");
				Enumeration enum = ht.keys();
				while(enum.hasMoreElements()) {
					Integer aKey = (Integer) enum.nextElement();
					logCat.debug("cl key="+aKey.intValue());
					FieldValue aFV = (FieldValue) ht.get(aKey);
					logCat.debug("cl fieldValue="+aFV.getField().getName()+", "+aFV.getFieldValue());
				}
			}
		*/

		for (int i = 0; i < childFieldValues.length; i++) {

			String actualValue = childFieldValues[i].getFieldValue();
			logCat.debug("actualValue=" + actualValue);
			Field f = childFieldValues[i].getField();
			logCat.debug("f.getName=" + f.getName());
			logCat.debug("f.getId=" + f.getId());
			FieldValue aFieldValue = (FieldValue) ht.get(new Integer(f.getId()));
			if (aFieldValue == null)
				throw new IllegalArgumentException(
					"ERROR: Make sure that field "
						+ f.getName()
						+ " is a KEY of the table "
						+ table.getName()
						+ "! Otherwise you can not use it as PARENT/CHILD LINK argument!");
			String valueInPos = aFieldValue.getFieldValue();
			logCat.info("comparing " + actualValue + " TO " + valueInPos);
			if (!actualValue.trim().equals(valueInPos.trim()))
				return false;
		}

		return true;
	} /**
	
	*/
	private Hashtable getNamesHashtable(String core) {

		Hashtable result = new Hashtable();
		Enumeration enum = this.table.getFields().elements();
		while (enum.hasMoreElements()) {
			Field f = (Field) enum.nextElement();
			String fieldName = f.getName();
			int fieldId = f.getId();
			StringBuffer sb = new StringBuffer(core);
			sb.append("_");
			sb.append(tableId);
			sb.append("_");
			sb.append(fieldId);
			result.put(fieldName, sb.toString());
			// in PHP slang we would call that an "associative array" :=)
		}

		return result;
	}

	private java.lang.String redisplayFieldsOnError = "false";
	/**
	 * grunikiewicz.philip@hydro.qc.ca
	 * Creation date: (2001-05-31 13:11:00)
	 * @return java.lang.String
	 */
	public java.lang.String getRedisplayFieldsOnError() {
		return redisplayFieldsOnError;
	} /**
	* grunikiewicz.philip@hydro.qc.ca
	* Creation date: (2001-05-31 13:11:00)
	* @param newRedisplayFieldsOnError java.lang.String
	*/
	public void setRedisplayFieldsOnError(
		java.lang.String newRedisplayFieldsOnError) {
		redisplayFieldsOnError = newRedisplayFieldsOnError;
	}

	private java.lang.String bypassNavigation = "false";
	/**
	 * grunikiewicz.philip@hydro.qc.ca
	 * Creation date: (2001-08-17 10:25:56)
	 * @return java.lang.String
	 */
	public java.lang.String getBypassNavigation() {
		return bypassNavigation;
	} /**
	* grunikiewicz.philip@hydro.qc.ca
	* Creation date: (2001-08-17 10:25:56)
	* @param newBypassNavigation java.lang.String
	*/
	public void setBypassNavigation(java.lang.String newBypassNavigation) {
		bypassNavigation = newBypassNavigation;
	}

	private java.lang.String action;
	/**
	 * Insert the method's description here.
	 * Creation date: (2001-10-22 18:17:25)
	 * @return java.lang.String
	 */
	public java.lang.String getAction() {
		return action;
	} /**
	* Insert the method's description here.
	* Creation date: (2001-10-22 18:17:25)
	* @param newAction java.lang.String
	*/
	public void setAction(java.lang.String newAction) {
		action = newAction;
	} /**
	* Gets the tableList
	* @return Returns a String
	*/
	public String getTableList() {
		return tableList;
	} /**
	* Sets the tableList
	* @param tableList The tableList to set
	*/
	public void setTableList(String tableList) {
		this.tableList = tableList;
	}

	/**
	 * Gets the followUpOnError
	 * @return Returns a String
	 */
	public String getFollowUpOnError() {
		return followUpOnError;
	}
	/**
	 * Sets the followUpOnError
	 * @param followUpOnError The followUpOnError to set
	 */
	public void setFollowUpOnError(String followUpOnError) {
		this.followUpOnError = followUpOnError;
	}

}