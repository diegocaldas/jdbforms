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
import java.util.Vector;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Date;
import java.util.Locale;
import java.sql.SQLException;
import java.sql.Connection;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;
import javax.servlet.jsp.tagext.Tag;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.dbforms.util.ParseUtil;
import org.dbforms.util.Util;
import org.dbforms.util.SqlUtil;
import org.dbforms.util.MessageResources;
import org.dbforms.util.MessageResourcesInternal;
import org.dbforms.util.TimeUtil;
import org.dbforms.config.Constants;
import org.dbforms.config.DbEventInterceptor;
import org.dbforms.config.DbFormsConfig;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.DbFormsErrors;
import org.dbforms.config.FieldTypes;
import org.dbforms.config.FieldValue;
import org.dbforms.config.FieldValues;
import org.dbforms.config.ResultSetVector;
import org.dbforms.config.Table;
import org.dbforms.config.Field;
import org.dbforms.config.GrantedPrivileges;
import org.dbforms.event.WebEvent;
import org.dbforms.event.NavigationEvent;
import org.dbforms.event.NavEventFactory;
import org.dbforms.event.NavEventFactoryImpl;
import org.dbforms.event.eventtype.EventType;
import org.dbforms.validation.ValidatorConstants;
import org.dbforms.validation.DbFormsValidatorUtil;

/**
 * This is the root element of a data manipulation form
 *
 * @author  Joachim Peer
 */
public class DbFormTag extends BodyTagSupport implements TryCatchFinally {
	/** logging category for this class */
	private static Category logCat = Category.getInstance(DbFormTag.class.getName());

	/** access data defined in dbforms-config.xml */
	private DbFormsConfig config;

	/** the id of the underlying table */
	private int tableId;

	/** the underlying table */
	private Table table;

	/** the name of the  underlying table */
	private String tableName;

	/** count of this form (n ||) */
	private String maxRows;

	/** count (multiplicity, view-mode) of this form (n || -1), whereby n E N (==1,2..z) */
	private int count;

	/** the data to be rendered */
	private ResultSetVector resultSetVector;

	/** holds information about how many times the body of this tag has been rendered */
	private int currentCount = 0;

	/** holds information about how many times the body of this tag has been rendered */
	private String positionPath;

	/** important in subforms: 5th row of subform in 2nd row of main form has path: "5@2"; in mainforms */
	private String positionPathCore;

	/** if rendering of header and body of the form is completed and only the footer needs to be rendered yet */
	private boolean footerReached = false;

	/** is either "true" or "false" */
	private String multipart;

	/** site to be invoked after action - nota bene: this followUp may be overruled by "followUp"-attributes of actionButtons */
	private String followUp;

	/** site to be invoked after action if previous form contained errors- nota bene: this followUp may be overruled by "followUp"-attributes of actionButtons */
	private String followUpOnError;

	/** pedant to the html-target attribute in html-form tag: the target frame to jump to */
	private String target;

	/**
	 * if "true", at every action (navigation, insert, update, etc.) all input fields
	 * of ALL currently rendered rowsets are parsed and updated.
	 * Many rows may be affected. If "false", updates are only performed if an explicite "update"- action is
	 * launched (normally by hittig the updateAction-button). Up to 1 row may be affected.
	 * <br>
	 * Default is: "false"
	 */
	private String autoUpdate = "false";

	/** reference to a parent DBFormTag (if any) */
	private DbFormTag parentForm;

	/** used in sub-form:  field(s) in the main form that is/are linked to this form */
	private String parentField;

	/** used in sub-form:  field(s) in this forme that is/are linked to the parent form */
	private String childField;

	/**
	 *  used in sub-form: this data structure holds the linked childfield(s)
	 *  and their current values it/they derive from the main form
	 */
	private FieldValue[] childFieldValues;

	/** subform flag */
	private boolean isSubForm = false;

	/** SQL order by string */
	private String orderBy;

	/** filter string */
	private String filter;

	/** SQL filter string */
	private String sqlFilter = null;
	private String gotoPrefix;
	private Hashtable gotoHt = null;
	private String dbConnectionName = null;
	private Vector overrulingOrderFields;
	private String localWebEvent;

	/** #fixme: description */
	private StringBuffer childElementOutput;

	/** Free-form select query */
	private String whereClause;

	/** Supply table name list */
	private String tableList;

	/** the form name to map with valdation.xml form name. */
	private String formValidatorName;

	/** support caption name resolution with ApplicationResources. */
	private String captionResource = "false";

	/** support caption name resolution with ApplicationResources. */
	private String javascriptValidation = "false";

	/**
	 * File of validation javascript for include <SCRIPT src="..."></SCRIPT>
	 * For better performance.  Else it's the webserver will generate it each time.
	 */
	private String javascriptValidationSrcFile;

	/** support caption name resolution with ApplicationResources. */
	private String javascriptFieldsArray = "false";

	/**
	 *  List of all child field name with assosciate generated name.
	 *  Ex: "champ1" : "f_0_3@root_3"
	 */
	private Hashtable childFieldNames = new Hashtable();

	/** Used to avoid creation of same javascript function. */
	private Hashtable javascriptDistinctFunctions = new Hashtable();

	/** Indicate if the form is in read-only mode */
	private String readOnly = "false";

	/** Keep trace of witch event is use */
	private WebEvent webEvent = null;

	/** NavigationEvent factory */
	private static NavEventFactory navEventFactory = NavEventFactoryImpl.instance();

	/** redisplayFieldsOnError flag */
	private String redisplayFieldsOnError = "false";

	/** represents the HTML form tag attribute AUTOCOMPLETE. */
	private String autocomplete = null;

	/** onSubmit form field  (20020703-HKK) */
	private String onSubmit;

	/** form's action attribute */
	private String action;

	/** holds the list of sub forms to validate (2003-02-04 HKK) */
	private Vector validationForms;
	private Hashtable validationFields;

	/** holds the list of fields of the sub forms (2003-02-04 HKK) */
	private Hashtable fieldNames;

	/** Holds the locale the form is created with. Can be readed from other tags */
	private Locale locale = null;

	private FieldValues orderFields = null;

	/** holds name attribute */
	private String name = null;

	/** holds id attribute */
	private String id = null;

	/** 
	 *
	 * Returns the locale the form is created with. Can be readed from other tags
	 *
	 * */
	public Locale getLocale() {
		return locale;
	}

	public FieldValues getOrderFields() {
		return orderFields;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param formName DOCUMENT ME!
	 * @param childFields DOCUMENT ME!
	 */
	public void addValidationForm(String formName, Hashtable childFields) {
		if (validationForms == null) {
			validationForms = new Vector();
		}

		validationForms.add(formName);

		if (validationFields == null) {
			validationFields = new Hashtable();
		}

		validationFields.putAll(childFields);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param fields DOCUMENT ME!
	 */
	private void addFieldNames(Hashtable fields) {
		if (fieldNames == null) {
			fieldNames = new Hashtable();
		}

		fieldNames.putAll(fields);
	}

	/**
	   /**
	 *  Sets the tableName attribute of the DbFormTag object
	 *
	 * @param  tableName The new tableName value
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
		this.table = config.getTableByName(tableName);
		this.tableId = table.getId();
	}

	/**
	 *  Gets the tableName attribute of the DbFormTag object
	 *
	 * @return  The tableName value
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 *  Gets the table attribute of the DbFormTag object
	 *
	 * @return  The table value
	 */
	public Table getTable() {
		return table;
	}

	/**
	 *  Sets the maxRows attribute of the DbFormTag object
	 *
	 * @param  maxRows The new maxRows value
	 */
	public void setMaxRows(String maxRows) {
		this.maxRows = maxRows;

		if (maxRows.trim().equals("*")) {
			this.count = 0;
		} else {
			this.count = Integer.parseInt(maxRows);
		}
	}

	/**
	 *  Gets the maxRows attribute of the DbFormTag object
	 *
	 * @return  The maxRows value
	 */
	public String getMaxRows() {
		return maxRows;
	}

	/**
	 *  Gets the count attribute of the DbFormTag object
	 *
	 * @return  The count value
	 */
	public int getCount() {
		return count;
	}

	/**
	 *  Gets the currentCount attribute of the DbFormTag object
	 *
	 * @return  The currentCount value
	 */
	public int getCurrentCount() {
		return currentCount;
	}

	/**
	 *  Increase the current count attribute
	 */
	public void increaseCurrentCount() {
		currentCount++;
	}

	/**
	 *  Update the position path.
	 *  <br>
	 *   Ie: "5" + "@" + "root", "5" + "@" + "123@35@root"
	 */
	public void updatePositionPath() {
		StringBuffer positionPathBuf = new StringBuffer();
		positionPathBuf.append(this.currentCount);
		positionPathBuf.append("@");
		positionPathBuf.append(this.positionPathCore);
		this.positionPath = positionPathBuf.toString();
	}

	/**
	 *  Gets the positionPath attribute of the DbFormTag object
	 *
	 * @return  The positionPath value
	 */
	public String getPositionPath() {
		return positionPath;
	}

	/**
	 *  Gets the positionPathCore attribute of the DbFormTag object.
	 *  <br>
	 *  Ie.  "root",  "123@35@root"
	 *
	 * @return  The positionPathCore value
	 */
	public String getPositionPathCore() {
		return positionPathCore;
	}

	/**
	 *  Sets the followUp attribute of the DbFormTag object
	 *
	 * @param  followUp The new followUp value
	 */
	public void setFollowUp(String followUp) {
		this.followUp = followUp;
	}

	/**
	 *  Gets the followUp attribute of the DbFormTag object
	 *
	 * @return  The followUp value
	 */
	public String getFollowUp() {
		return followUp;
	}

	/**
	 *  Sets the target attribute of the DbFormTag object
	 *
	 * @param  target The new target value
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 *  Gets the target attribute of the DbFormTag object
	 *
	 * @return  The target value
	 */
	public String getTarget() {
		return target;
	}

	/**
	 *  Sets the autoUpdate attribute of the DbFormTag object
	 *
	 * @param  autoUpdate The new autoUpdate value
	 */
	public void setAutoUpdate(String autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

	/**
	 *  Gets the autoUpdate attribute of the DbFormTag object
	 *
	 * @return  The autoUpdate value
	 */
	public String getAutoUpdate() {
		return autoUpdate;
	}

	/**
	 *  Sets the parentField attribute of the DbFormTag object
	 *
	 * @param  parentField The new parentField value
	 */
	public void setParentField(String parentField) {
		this.parentField = parentField;
	}

	/**
	 *  Gets the parentField attribute of the DbFormTag object
	 *
	 * @return  The parentField value
	 */
	public String getParentField() {
		return parentField;
	}

	/**
	 * Set the parent tag
	 *
	 * @param  p the parent tag
	 */
	public void setParent(Tag p) {
		super.setParent(p);
		this.parentForm = (DbFormTag) findAncestorWithClass(this, DbFormTag.class);
	}

	/**
	 *  Sets the childField attribute of the DbFormTag object
	 *
	 * @param  childField The new childField value
	 */
	public void setChildField(String childField) {
		this.childField = childField;
	}

	/**
	 *  Gets the childField attribute of the DbFormTag object
	 *
	 * @return  The childField value
	 */
	public String getChildField() {
		return childField;
	}

	/**
	 *  Sets the orderBy attribute of the DbFormTag object
	 *
	 * @param  orderBy The new orderBy value
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 *  Gets the orderBy attribute of the DbFormTag object
	 *
	 * @return  The orderBy value
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 *  Sets the filter attribute of the DbFormTag object
	 *
	 * @param  filter The new filter value
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 *  Gets the filter attribute of the DbFormTag object
	 *
	 * @return  The filter value
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * @return
	 */
	public String getSqlFilter() {
		return sqlFilter;
	}

	/**
	 * @param string
	 */
	public void setSqlFilter(String string) {
		sqlFilter = string;
	}

	/**
	 *  Sets the gotoPrefix attribute of the DbFormTag object
	 *
	 * @param  gotoPrefix The new gotoPrefix value
	 */
	public void setGotoPrefix(String gotoPrefix) {
		this.gotoPrefix = gotoPrefix;
	}

	/**
	 *  Gets the gotoPrefix attribute of the DbFormTag object
	 *
	 * @return  The gotoPrefix value
	 */
	public String getGotoPrefix() {
		return gotoPrefix;
	}

	/**
	 *  Sets the gotoHt attribute of the DbFormTag object
	 *
	 * @param  gotoHt The new gotoHt value
	 */
	public void setGotoHt(Hashtable gotoHt) {
		this.gotoHt = gotoHt;
	}

	/**
	 *  Gets the gotoHt attribute of the DbFormTag object
	 *
	 * @return  The gotoHt value
	 */
	public Hashtable getGotoHt() {
		return gotoHt;
	}

	/**
	 *  Sets the multipart attribute of the DbFormTag object
	 *
	 * @param  multipart The new multipart value
	 */
	public void setMultipart(String value) {
		this.multipart = value;
	}

	/**
	 *  Description of the Method
	 *
	 * @return  Description of the Return Value
	 */
	public boolean hasMultipartSet() {
		return "true".equalsIgnoreCase(multipart);
	}

	/**
	 *  Sets the dbConnectionName attribute of the DbFormTag object
	 *
	 * @param  dbConnectionName The new dbConnectionName value
	 */
	public void setDbConnectionName(String dbConnectionName) {
		this.dbConnectionName = dbConnectionName;
	}

	/**
	 *  Gets the dbConnectionName attribute of the DbFormTag object
	 *
	 * @return  The dbConnectionName value
	 */
	public String getDbConnectionName() {
		return dbConnectionName;
	}

	/**
	 *  Sets the footerReached attribute of the DbFormTag object
	 *
	 * @param  footerReached The new footerReached value
	 */
	public void setFooterReached(boolean footerReached) {
		this.footerReached = footerReached;
	}

	/**
	 *  Gets the footerReached attribute of the DbFormTag object
	 *
	 * @return  The footerReached value
	 */
	public boolean getFooterReached() {
		return footerReached;
	}

	/**
	 *  Sets the localWebEvent attribute of the DbFormTag object
	 *
	 * @param  localWebEvent The new localWebEvent value
	 */
	public void setLocalWebEvent(String localWebEvent) {
		this.localWebEvent = localWebEvent;
	}

	/**
	 *  Gets the localWebEvent attribute of the DbFormTag object
	 *
	 * @return  The localWebEvent value
	 */
	public String getLocalWebEvent() {
		return localWebEvent;
	}

	/**
	 *  Gets the resultSetVector attribute of the DbFormTag object
	 *
	 * @return  The resultSetVector value
	 */
	public ResultSetVector getResultSetVector() {
		return resultSetVector;
	}

	/**
	 *  Gets the subForm attribute of the DbFormTag object
	 *
	 * @return  The subForm value
	 */
	public boolean isSubForm() {
		return isSubForm;
	}

	/**
	 *  Sets the whereClause attribute of the DbFormTag object
	 *
	 * @param  wc The new whereClause value
	 */
	public void setWhereClause(String wc) {
		this.whereClause = wc;
	}

	/**
	 *  Gets the whereClause attribute of the DbFormTag object
	 *
	 * @return  The whereClause value
	 */
	public String getWhereClause() {
		return whereClause;
	}

	/**
	 *  Sets the formValidatorName attribute of the DbFormTag object
	 *
	 * @param  fv The new formValidatorName value
	 */
	public void setFormValidatorName(String fv) {
		this.formValidatorName = fv;
	}

	/**
	 *  Gets the formValidatorName attribute of the DbFormTag object
	 *
	 * @return  The formValidatorName value
	 */
	public String getFormValidatorName() {
		return formValidatorName;
	}

	/**
	 *  Sets the captionResource attribute of the DbFormTag object
	 *
	 * @param  res The new captionResource value
	 */
	public void setCaptionResource(String res) {
		this.captionResource = res;
	}

	/**
	 *  Gets the captionResource attribute of the DbFormTag object
	 *
	 * @return  The captionResource value
	 */
	public boolean hasCaptionResourceSet() {
		return "true".equalsIgnoreCase(captionResource);
	}

	/**
	 *  Sets the javascriptValidation attribute of the DbFormTag object
	 *
	 * @param  jsv The new javascriptValidation value
	 */
	public void setJavascriptValidation(String jsv) {
		this.javascriptValidation = jsv;
	}

	/**
	 *  Gets the javascriptValidation attribute of the DbFormTag object
	 *
	 * @return  The javascriptValidation value
	 */
	public boolean hasJavascriptValidationSet() {
		return "true".equalsIgnoreCase(javascriptValidation);
	}

	/**
	 *  Sets the javascriptFieldsArray attribute of the DbFormTag object
	 *
	 * @param  jfa The new javascriptFieldsArray value
	 */
	public void setJavascriptFieldsArray(String jfa) {
		this.javascriptFieldsArray = jfa;
	}

	/**
	 *  Gets the javascriptFieldsArray attribute of the DbFormTag object
	 *
	 * @return  The javascriptFieldsArray value
	 */
	public boolean hasJavascriptFieldsArraySet() {
		return "true".equalsIgnoreCase(javascriptFieldsArray);
	}

	/**
	 * Sets the javascriptFieldsArray attribute of the DbFormTag object
	 *
	 * @param  jsvs The javascriptFieldsArray value
	 */
	public void setJavascriptValidationSrcFile(String jsvs) {
		this.javascriptValidationSrcFile = jsvs;
	}

	/**
	 *  Gets the javascriptValidationSrcFile attribute of the DbFormTag object
	 *
	 * @return  The javascriptValidationSrcFile value
	 */
	public String getJavascriptValidationSrcFile() {
		return javascriptValidationSrcFile;
	}

	/**
	 *  Sets the readOnly attribute of the DbFormTag object
	 *
	 * @param  readOnly The new readOnly value
	 */
	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 *  Gets the readOnly attribute of the DbFormTag object
	 *
	 * @return  The readOnly value
	 */
	public boolean hasReadOnlySet() {
		return "true".equalsIgnoreCase(readOnly);
	}

	/**
	 *  Adds a feature to the JavascriptFunction attribute of the DbFormTag object
	 *
	 * @param  jsFctName The feature to be added to the JavascriptFunction attribute
	 * @param  jsFct The feature to be added to the JavascriptFunction attribute
	 */
	public void addJavascriptFunction(String jsFctName, StringBuffer jsFct) {
		if (!existJavascriptFunction(jsFctName)) {
			javascriptDistinctFunctions.put(jsFctName, jsFct);
		}
	}

	/**
	 *  Check if the input name of the JavaScript function exists.
	 *
	 * @param  jsFctName the name of the JS function
	 * @return  true     if the function exists, false otherwise
	 */
	public boolean existJavascriptFunction(String jsFctName) {
		return javascriptDistinctFunctions.containsKey(jsFctName);
	}

	/**
	 *  Adds a feature to the ChildName attribute of the DbFormTag object
	 *
	 * @param  tableFieldName The feature to be added to the ChildName attribute
	 * @param  dbFormGeneratedName The feature to be added to the ChildName attribute
	 */
	public void addChildName(String tableFieldName, String dbFormGeneratedName) {
		childFieldNames.put(dbFormGeneratedName, tableFieldName);
	}

	/**
	 *  Append the input string to the childElementOutput stringBuffer.
	 *
	 * @param  str the string to append
	 */
	public void appendToChildElementOutput(String str) {
		this.childElementOutput.append(str);
	}

	/**
	 * Get the redisplayFieldsOnError attribute.
	 * <br>
	 * author: grunikiewicz.philip@hydro.qc.ca<br>
	 * creation date: (2001-05-31 13:11:00)<br>
	 *
	 * @return  the redisplayFieldsOnError
	 */
	public boolean hasRedisplayFieldsOnErrorSet() {
		return "true".equalsIgnoreCase(redisplayFieldsOnError);
	}

	/**
	 * Set the redisplayFieldsOnError attribute
	 *
	 * @param  newRedisplayFieldsOnError the new redisplayFieldsOnError value
	 */
	public void setRedisplayFieldsOnError(java.lang.String newRedisplayFieldsOnError) {
		redisplayFieldsOnError = newRedisplayFieldsOnError;
	}

	/**
	 *  Get the onSubmit attribute.
	 *
	 * @return the onSubmit attribute
	 */
	public java.lang.String getOnSubmit() {
		return onSubmit;
	}

	/**
	 *  Set the onSubmit attribute value
	 *
	 * @param  newonSubmit the new onSubmit value
	 */
	public void setOnSubmit(String newonSubmit) {
		onSubmit = newonSubmit;
	}

	/**
	 * Get the action attribute value.
	 *
	 * @return the action attribute value
	 */
	public java.lang.String getAction() {
		return action;
	}

	/**
	 * Set the action attribute value.
	 *
	 * @param  newAction the new attribute value
	 */
	public void setAction(java.lang.String newAction) {
		action = newAction;
	}

	/**
	 * Get the table list attribute.
	 *
	 * @return  the table list string
	 */
	public String getTableList() {
		return tableList;
	}

	/**
	 *  Set the table list attribute value
	 *
	 * @param  tableList the new tableList value
	 */
	public void setTableList(String tableList) {
		this.tableList = tableList;
	}

	/**
	 * Gets the followUpOnError
	 *
	 * @return  Returns a String
	 */
	public String getFollowUpOnError() {
		return followUpOnError;
	}

	/**
	 * Sets the followUpOnError attribute
	 *
	 * @param  followUpOnError The followUpOnError to set
	 */
	public void setFollowUpOnError(String followUpOnError) {
		this.followUpOnError = followUpOnError;
	}

	/**
	 *  Sets the pageContext attribute of the DbFormTag object
	 *
	 * @param  pc The new pageContext value
	 */
	public void setPageContext(PageContext pc) {
		super.setPageContext(pc);

		try {
			config = DbFormsConfigRegistry.instance().lookup();
		} catch (Exception e) {
			logCat.error(e);
		}
	}

	/**
	 * Grunikiewicz.philip@hydro.qc.ca
	 * 2001-08-17
	 *
	 * Added an attribute to allow developer to bypass navigation:
	 *          If we are not going to use navigation, eliminate the
	 *         creation of, and execution of "fancy!" queries.
	 *
	 *
	 * Henner.Kollmann@gmx.de
	 * 2002-07-03
	 * Added onSubmit support
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
	 * @return  Description of the Return Value
	 */
	public int doStartTag() {
		Connection con = config.getConnection(dbConnectionName);

		try {
			// *************************************************************
			//  Part I - checking user access right, processing interceptor
			// *********ad****************************************************
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

			locale = MessageResources.getLocale(request);

			logCat.info("servlet path = " + request.getServletPath());
			logCat.info("servlet getPathInfo = " + request.getPathInfo());

			logCat.info("servlet getContextPath = " + request.getContextPath());
			logCat.info("servlet getRequestURI = " + request.getRequestURI());
			
			String strFollowUp = getFollowUp();
			if (Util.isNull(strFollowUp)) {
			   strFollowUp = request.getRequestURI();
			   String contextPath = request.getContextPath();
			   if (!Util.isNull(contextPath)) {
			      strFollowUp = strFollowUp.substring(contextPath.length(), strFollowUp.length());	
			   }
			   if(!Util.isNull(request.getQueryString())) {
			      strFollowUp += "?" + request.getQueryString();
			   }
			}
			
			logCat.debug("pos1");

			// part I/a - security
			JspWriter out = pageContext.getOut();

			logCat.debug("pos2");

			// check user privilege
			if ((table != null) && !table.hasUserPrivileg(request, GrantedPrivileges.PRIVILEG_SELECT)) {
				logCat.debug("pos3");

				String str =
					MessageResourcesInternal.getMessage("dbforms.events.view.nogrant", getLocale(), new String[] { table.getName()});
				logCat.warn(str);
				out.println(str);

				return SKIP_BODY;
			}

			logCat.debug("pos4");

			// part II/b - processing interceptors
			if ((table != null) && table.hasInterceptors()) {
				try {
					logCat.debug("pos5");
					table.processInterceptors(DbEventInterceptor.PRE_SELECT, request, null, config, con);
				} catch (Exception sqle) {
					logCat.error("pos6");
					logCat.error(sqle.getMessage(), sqle);
					out.println(sqle.getMessage());

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
			footerReached = false;
			resultSetVector = null;
			childElementOutput = new StringBuffer();
			logCat.debug("first steps finished");

			// if main form
			// we write out the form-tag which points to the controller-servlet
			if (parentForm == null) {
				tagBuf.append("<form ");
				if (!Util.isNull(getId())) {
					tagBuf.append("id=\"");
					tagBuf.append(getId());
					tagBuf.append("\" ");
				}
				tagBuf.append("name=\"dbform\" action=\"");
				//Check if developer has overriden action
				if ((this.getAction() != null) && (this.getAction().trim().length() > 0)) {
					tagBuf.append(this.getAction());
				} else {
					tagBuf.append(response.encodeURL(request.getContextPath() + "/servlet/control"));
				}

				tagBuf.append("\"");

				// JFM 20040309: additional attribute autocomplete
				if (getAutocomplete() != null) {
					tagBuf.append(" autocomplete=\"" + getAutocomplete() + "\"");
				}

				// append target element
				if (target != null) {
					tagBuf.append(" target=\"");
					tagBuf.append(target);
					tagBuf.append("\"");
				}

				tagBuf.append(" method=\"post\"");

				if (hasMultipartSet()) {
					tagBuf.append(" enctype=\"multipart/form-data\"");
				}

				String validationFct = null;

				if (hasJavascriptValidationSet()) {
					validationFct = getFormValidatorName();
					validationFct =
						Character.toUpperCase(validationFct.charAt(0)) + validationFct.substring(1, validationFct.length());
					validationFct = "validate" + validationFct + "(this)";
				}

				if (!Util.isNull(validationFct) && !Util.isNull(getOnSubmit())) {
					boolean found = false;
					String s = getOnSubmit();
					String[] cmds = StringUtils.split(s, ';');

					for (int i = 0; i < cmds.length; i++) {
						cmds[i] = cmds[i].trim();
						if (cmds[i].startsWith("return")) {
							cmds[i] = cmds[i].substring("return".length());
							cmds[i] = "return " + validationFct + " && " + cmds[i];
							found = true;

							break;
						}
					}

					s = "";

					for (int i = 0; i < cmds.length; i++) {
						s = s + cmds[i] + ";";
					}

					if (!found) {
						s = s + "return " + validationFct + ";";
					}

					tagBuf.append(" onSubmit=\"");
					tagBuf.append(s);
					tagBuf.append("\" ");
				} else if (!Util.isNull(validationFct)) {
					tagBuf.append(" onSubmit=\"");
					tagBuf.append("return " + validationFct);
					tagBuf.append("\" ");
				} else if (!Util.isNull(getOnSubmit())) {
					tagBuf.append(" onSubmit=\"");
					tagBuf.append(getOnSubmit());
					tagBuf.append("\" ");
				}

				tagBuf.append(">");

				// supports RFC 1867 - multipart upload, if some database-fields represent filedata
				if (tableName == null) {
					appendSource(request, tagBuf);

					// if form is an emptyform -> we've fineshed yet - cancel all further activities!
					out.println(tagBuf.toString());

					return EVAL_BODY_BUFFERED;
				}

				positionPathCore = "root";
			} else {
				// if sub-form, we dont write out html tags; this has been done already by a parent form
				this.isSubForm = true;
				positionPathCore = parentForm.getPositionPath();

				// If whereClause is not supplied by developer
				// determine the value(s) of the linked field(s)
				if (Util.isNull(getWhereClause())) {
					initChildFieldValues();
					if (childFieldValues == null) {
						return SKIP_BODY;
					}
				}
			}

			// write out involved table
			tagBuf.append("<input type=\"hidden\" name=\"invtable\" value=\"" + tableId + "\"/>");

			// write out the name of the involved dbconnection.
			tagBuf.append(
				"<input type='hidden' name='invname_"
					+ tableId
					+ "' value='"
					+ (Util.isNull(dbConnectionName) ? "" : dbConnectionName)
					+ "'/>");

			// write out the autoupdate-policy of this form
			tagBuf.append("<input type=\"hidden\" name=\"autoupdate_" + tableId + "\" value=\"" + autoUpdate + "\"/>");

			// write out the followup-default for this table
			tagBuf.append("<input type=\"hidden\" name=\"fu_" + tableId + "\" value=\"" + strFollowUp + "\"/>");

			// write out the locale
			tagBuf.append("<input type=\"hidden\" name=\"lang" + "\" value=\"" + locale.toString() + "\"/>");

			// write out the followupOnError-default for this table
			if (!Util.isNull(getFollowUpOnError())) {
				tagBuf.append("<input type=\"hidden\" name=\"fue_" + tableId + "\" value=\"" + getFollowUpOnError() + "\"/>");
			}

			// write out the formValidatorName
			if (!Util.isNull(getFormValidatorName())) {
				tagBuf.append(
					"<input type=\"hidden\" name=\""
						+ ValidatorConstants.FORM_VALIDATOR_NAME
						+ "_"
						+ tableId
						+ "\" value=\""
						+ getFormValidatorName()
						+ "\"/>");
			}

			appendSource(request, tagBuf);

			// Allow to send action dynamicaly from javascript
			tagBuf.append("<input type=\"hidden\" name=\"customEvent\"/>");

			// *************************************************************
			//  Part III - fetching Data. This data is provided to all sub-
			//  elements of this form.
			// *************************************************************
			// III/1:
			// initialize all of the different filters
			// retrieve sqlFilters 
			String sqlFilterString = "";
			String requestSqlFilterString = DbFilterTag.getSqlFilter(request, this.getTable().getId());
			FieldValue[] sqlFilterParams = null;

			if (!Util.isNull(getSqlFilter()) && !Util.isNull(requestSqlFilterString)) {
				sqlFilterString = " ( " + requestSqlFilterString + " ) AND ( " + getSqlFilter() + " ) ";
			} else if (!Util.isNull(getSqlFilter())) {
				sqlFilterString = getSqlFilter();
			} else if (!Util.isNull(requestSqlFilterString)) {
				sqlFilterString = requestSqlFilterString;
			}

			logCat.debug("filter to apply : " + sqlFilterString);

			if (!Util.isNull(requestSqlFilterString)) {
				sqlFilterParams = DbFilterTag.getSqlFilterParams(request, this.getTable().getId());
			}

			// overrules other default declarations eventually done in XML config;
			FieldValue[] overrulingOrder = initOverrulingOrder(request);

			// III/1: first we must determinate the ORDER clause to apply to the query
			// the order may be specified in the dbforms-config.xml file or in the current JSP file.
			// tells us which definintion of field order (global or local) to use
			FieldValue[] orderConstraint;

			// if developer provided orderBy - Attribute in <db:dbform> - tag
			if (overrulingOrder != null) {
				orderConstraint = overrulingOrder;
				logCat.info("using OverrulingOrder (dbform tag attribute)");
			}

			// if developer provided orderBy - Attribute globally in dbforms-config.xml - tag
			else {
				FieldValue[] tmpOrderConstraint = table.getDefaultOrder();
				orderConstraint = new FieldValue[tmpOrderConstraint.length];

				// cloning is necessary to keep things thread-safe!
				// (we manipulate some fields in this structure.)
				for (int i = 0; i < tmpOrderConstraint.length; i++) {
					orderConstraint[i] = (FieldValue) tmpOrderConstraint[i].clone();
				}

				logCat.info("using DefaultOrder (from config file)");
			}

			// an orderBY - clause is a MUST. we can't query well without it.
			if (orderConstraint == null) {
				throw new IllegalArgumentException("OrderBy-Clause must be specified either in table-element in config.xml or in dbform-tag on jsp view");
			}

			FieldValue[] filterFieldValues = null;

			if (!Util.isNull(filter)) {
				filterFieldValues = table.getFilterFieldArray(filter, locale);
			}

			FieldValue[] mergedFieldValues = null;

			if (childFieldValues == null) {
				mergedFieldValues = filterFieldValues;
			} else if (filterFieldValues == null) {
				mergedFieldValues = childFieldValues;
			} else {
				mergedFieldValues = new FieldValue[childFieldValues.length + filterFieldValues.length];
				System.arraycopy(childFieldValues, 0, mergedFieldValues, 0, childFieldValues.length);
				System.arraycopy(filterFieldValues, 0, mergedFieldValues, childFieldValues.length, filterFieldValues.length);
			}

			// if we just habe a search request we do not need any other constraints
			FieldValue[] searchFieldValues = initSearchFieldValues();

			if (searchFieldValues != null) {
				mergedFieldValues = searchFieldValues;
			}

			// III/2:
			// is there a POSITION we are supposed to navigate to?
			// positions are key values: for example "2", oder "2~454"
			//String position = pageContext.getRequest().getParameter("pos_"+tableId);
			String firstPosition =
				Util.decode(
					ParseUtil.getParameter(request, "firstpos_" + tableId),
					pageContext.getRequest().getCharacterEncoding());
			String lastPosition =
				Util.decode(ParseUtil.getParameter(request, "lastpos_" + tableId), pageContext.getRequest().getCharacterEncoding());

			if (firstPosition == null) {
				firstPosition = lastPosition;
			}

			if (lastPosition == null) {
				lastPosition = firstPosition;
			}

			// if we are in a subform we must check if the fieldvalue-list provided in the
			// position strings is valid in the current state;
			// it might be invalid if the position of the parent form has been
			// changed (by a navigation event)
			// (=> the position-strings of childforms arent valid anymore)
			if ((childFieldValues != null) && (firstPosition != null)) {
				if (!checkLinkage(childFieldValues, firstPosition)) {
					// checking one of the 2 strings is sufficient
					// the position info is out of date. we dont use it.
					firstPosition = null;
					lastPosition = null;
				}
			}

			logCat.info("firstposition " + firstPosition);
			logCat.info("lastPosition " + lastPosition);

			/*
			   in the code above we examined lots of information which determinates  _which_ resultset
			   gets retrieved and the way this operation will be done.
			        
			   which are the "parameters" we have (eventually!) retrieved
			   ============================================================
			        
			 *)  WebEvent Object in request: if the jsp containing this tag was invoked by
			           the controller, then there is a Event which has been processed (DatebasEvents)
			           or which waits to be processed (NavigationEvents, including GotoEvent)
			                
			 *)  firstPos, lastPos: Strings containing key-fieldValues and indicating a line
			           to go to if we have no other information. this may happen if a subform gets
			           navigated. the parentForm is not involved in the operation but must be able
			           to "navigate" to its new old position.
			           [#checkme: risk of wrong interpreation if jsp calls jsp - compare source tags?]
			                
			 *)  mergedFieldValues: this is a cumulation of all rules which restrict the
			           result set in any way. it is build of
			                
			           -  childFieldValues: restricting a set in a subform that all "childFields" in the
			           resultset match their respective "parentFields" in main form. (for instance
			           if customerID == 100, we only want to select orders from orders-table
			           involving customerID 100)
			                
			           -  filterFieldValues: if a filter is applied to the resultset we always need
			           to select the _filtered_ resultset
			                
			           -  searchFieldValues: if a search is performed we just want to show fields
			           belonging to the search result (naturally ;=)
			                
			 *) orderConstraint: this is a cumulation of rules for ordering (sorting)
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
			                
			                
			 *)  count: this is a property of DbFormTag. Its relevance is that certain operations
			           need to be performed differently if count==0, which means the form is an
			           "endless form".
			 */

			// III/3: fetching data (compare description above)
			// this code is still expermintal, pre alpha! We need to put this logic into
			// etter (more readable, maintainable) code  (evt. own method or class)!
			// is there a NAVIGATION event (like "nav to first row" or "nav to previous row", or "nav back 4 rows"?
			// if so...
			webEvent = (WebEvent) request.getAttribute("webEvent");

			// set actual request to webEvent. Otherwise webEvent will not reflect current requestURI!
			if (webEvent != null) {
				webEvent.setRequest(request);
			}

			// if there comes no web event from controller, 
			// then create a new NAVIGATION event;
			//
			// # 2002.11.xx-fossato added an event factory 
			// # 20030320-HKK:      Rewrite to use navEvent only
			if ((webEvent == null) && (getLocalWebEvent() != null)) {
				webEvent = navEventFactory.createEvent(localWebEvent, request, config, table);

				// Setted with localWebEvent attribute.
				if (webEvent != null) {
					request.setAttribute("webEvent", webEvent);
				}
			}

			// will be used to cast the webEvent to a navigation event;
			NavigationEvent navEvent = null;

			//
			// 1. possibility: webEvent is a navigation event ?
			//
			if ((webEvent != null) && webEvent instanceof NavigationEvent) {
				navEvent = (NavigationEvent) webEvent;
				if ((navEvent.getTable() == null) || (navEvent.getTable().getId() != tableId)) {
					// navigation event is not for this table,
					// then just navigate to a position (if it exists) or just select all data
					// (if no pos or if endless form). 
					// best way to do this is to delete navEvent. Then a new event will be created!
					navEvent = null;
				}
			}

			//
			// 2. possibility: webEvent is a goto event ?
			//
			if (navEvent == null) {
				// we need to parse request using the given goto prefix
				if (!Util.isNull(gotoPrefix)) {
					logCat.info("���� NAV GOTO ����");

					Vector v = ParseUtil.getParametersStartingWith(request, gotoPrefix);
					gotoHt = new Hashtable();

					for (int i = 0; i < v.size(); i++) {
						String paramName = (String) v.elementAt(i);
						String fieldName = paramName.substring(gotoPrefix.length());
						logCat.debug("fieldName=" + fieldName);

						String fieldValue = ParseUtil.getParameter(request, paramName);
						logCat.debug("fieldValue=" + fieldValue);

						if ((fieldName != null) && (fieldValue != null)) {
							gotoHt.put(fieldName, fieldValue);
						}
					}
				}

				// try to create a new GOTO event
				if ((gotoHt != null) && (gotoHt.size() > 0)) {
					String positionString = table.getPositionString(gotoHt);
					navEvent = navEventFactory.createGotoEvent(table, request, config, positionString);
				}
			}

			//
			// 3. a) error in insert event ?
			//    b) create a GOTO event using a whereClause (free form select)
			//    c) create a GOTO event using.. another constructor ;^)
			//
			if (navEvent == null) {

				// grunikiewicz.philip@hydro.qc.ca
				// 2003-12-16
				// Commented code to fix the following problem:
				// Insert invoked in sub-form, validation error, suppose to re-populate all fields (parent and child)
				// Commented code changes all field names to INSERT type - making repopulate impossible! 
				// Henner.Kollmann@gmx.de
				// 2004-01-29
				// Just commenting out do not work - insert errors on single forms are not displayed then!
				// Check if table of event and table of form matches should do the work! 
				Vector errors = (Vector) request.getAttribute("errors");
				if ((webEvent != null)
					&& EventType.EVENT_DATABASE_INSERT.equals(webEvent.getType())
					&& (errors != null)
					&& (errors.size() > 0)
					&& (webEvent.getTable().getId() == getTable().getId())) {
					// error in insert event, nothing to do!
					navEvent = null;
					resultSetVector = null;
					setFooterReached(true);
				} else if (!Util.isNull(getWhereClause())) {
					// We should do a free form select
					navEvent = navEventFactory.createGotoEvent(table, request, config, whereClause, getTableList());
				} else {
					String myPosition = (count == 0) ? null : firstPosition;
					/*
										String myPosition = null;
										if ((webEvent != null)
											&& (// we have one of the update events...
										EventType
												.EVENT_DATABASE_UPDATE
												.equals(webEvent.getType())
												|| EventType.EVENT_DATABASE_DELETE.equals(
													webEvent.getType())
												|| EventType.EVENT_DATABASE_INSERT.equals(
													webEvent.getType())
											|| // we do navigation in a sub form
										webEvent
												instanceof NavigationEvent)) {
					
											// JPeer 03-21-2004: if maxRow is set to "*" (i.e. count==0),
											// then we do NOT want any other position than null.
											// this should fix the problem of insert operations
											// in endless forms
											myPosition = (count == 0) ? null : firstPosition;
										}
					*/
					navEvent = navEventFactory.createGotoEvent(table, request, config, myPosition);

				}
			}

			// Now we have a NAVIGATION event to process
			logCat.info("��� NAV/I ���");

			if (navEvent != null) {
				logCat.info("about to process nav event:" + navEvent.getClass().getName());
				resultSetVector =
					navEvent.processEvent(
						mergedFieldValues,
						orderConstraint,
						sqlFilterString,
						sqlFilterParams,
						count,
						firstPosition,
						lastPosition,
						dbConnectionName,
						con);

				if (ResultSetVector.isNull(resultSetVector)) {
					setFooterReached(true);
				}
				orderFields = new FieldValues(orderConstraint);
			}

			// *** DONE! ***
			//  We have now the underlying data, and this data is accessible to all sub-elements
			// (labels, textFields, etc. of this form
			//
			// *************************************************************
			//  Part IV - Again, some WebEvent infrastructural stuff:
			//  write out data indicating the position we have
			//  navigated to withing the rowset
			// *************************************************************
			// we process interceptor again (post-select)
			// #checkme: is the overhead of a POST_SELECT interceptor necessary or a luxury? => use cases!
			if ((table != null) && table.hasInterceptors()) {
				// process the interceptors associated to this table
				try {
					table.processInterceptors(DbEventInterceptor.POST_SELECT, request, null, config, con);
				} catch (SQLException sqle) {
					// PG = 2001-12-04
					// No need to add extra comments, just re-throw the exception
					// but first, better to log it  !
					SqlUtil.logSqlException(sqle);
					throw sqle;
				}
			}

			// End of interceptor processing
			// determinate new position-strings (== value of the first and the last row of the current view)
			if (!ResultSetVector.isNull(resultSetVector)) {
				resultSetVector.setPointer(0);
				firstPosition = table.getPositionString(resultSetVector);
				resultSetVector.setPointer(resultSetVector.size() - 1);
				lastPosition = table.getPositionString(resultSetVector);
				resultSetVector.setPointer(0);
			}

			if (!footerReached) {
				// if not in insert mode
				if (firstPosition != null) {
					tagBuf.append(
						"<input type=\"hidden\" name=\"firstpos_"
							+ tableId
							+ "\" value=\""
							+ Util.encode(firstPosition, pageContext.getRequest().getCharacterEncoding())
							+ "\"/>");
				}

				if (lastPosition != null) {
					tagBuf.append(
						"<input type=\"hidden\" name=\"lastpos_"
							+ tableId
							+ "\" value=\""
							+ Util.encode(lastPosition, pageContext.getRequest().getCharacterEncoding())
							+ "\"/>");
				}
			}

			if (!isSubForm) {
				pageContext.setAttribute("dbforms", new Hashtable());
			}

			Map dbforms = (Map) pageContext.getAttribute("dbforms");
			DbFormContext dbContext =
				new DbFormContext(
					table.getNamesHashtable("search"),
					table.getNamesHashtable("searchmode"),
					table.getNamesHashtable("searchalgo"),
					resultSetVector);
			if (!ResultSetVector.isNull(resultSetVector)) {
				dbContext.setCurrentRow(resultSetVector.getCurrentRowAsMap());
				dbContext.setPosition(
					Util.encode(table.getPositionString(resultSetVector), pageContext.getRequest().getCharacterEncoding()));
			}
			dbforms.put(getName(), dbContext);

			// construct TEI variables for access from JSP
			// # jp 27-06-2001: replacing "." by "_", so that SCHEMATA can be used
			pageContext.setAttribute("searchFieldNames_" + tableName.replace('.', '_'), table.getNamesHashtable("search"));
			pageContext.setAttribute("searchFieldModeNames_" + tableName.replace('.', '_'), table.getNamesHashtable("searchmode"));
			pageContext.setAttribute(
				"searchFieldAlgorithmNames_" + tableName.replace('.', '_'),
				table.getNamesHashtable("searchalgo"));

			// #fixme:
			// this is a weired crazy workaround [this code is also used in DbBodyTag!!]
			// why?
			// #fixme: explaination! -> initBody, spec, jsp container synchronizing variables, etc.
			if (!ResultSetVector.isNull(resultSetVector)) {
				pageContext.setAttribute("rsv_" + tableName.replace('.', '_'), resultSetVector);
				pageContext.setAttribute("currentRow_" + tableName.replace('.', '_'), resultSetVector.getCurrentRowAsMap());
				pageContext.setAttribute(
					"position_" + tableName.replace('.', '_'),
					Util.encode(table.getPositionString(resultSetVector), pageContext.getRequest().getCharacterEncoding()));
			}

			out.println(tagBuf.toString());
		} catch (IOException e) {
			logCat.error("::doStartTag - IOException", e);

			return SKIP_BODY;
		} catch (SQLException ne) {
			SqlUtil.logSqlException(ne);

			return SKIP_BODY;
		} finally {
			SqlUtil.closeConnection(con);
		}

		return EVAL_BODY_BUFFERED;
	}

	private void appendSource(HttpServletRequest request, StringBuffer tagBuf) {
		tagBuf.append("<input type=\"hidden\" name=\"source\" value=\"");

		// J.Peer 03-19-2004: in template driven sites, we may need another
		// way of retrieving the source page, because getRequestURI may deliver
		// the template name and NOT the actual JSP...
		// to cicumvent the problem, the applicatoin may set a request attribute
		// "dbforms.source" for each templated page requested
		String reqSource = (String) request.getAttribute("dbforms.source");
		// if not set, everything works as usual
		if (reqSource == null) {
			tagBuf.append(request.getRequestURI());
		}
		// if set, we use this value instead of getRequestURI()
		else {
			tagBuf.append(reqSource);
		}

		if (request.getQueryString() != null) {
			tagBuf.append("?").append(request.getQueryString());
		}

		tagBuf.append("\"/>");
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return  DOCUMENT ME!
	 * @throws  JspException DOCUMENT ME!
	 */
	public int doAfterBody() throws JspException {
		if (ResultSetVector.isNull(resultSetVector)) {
			return SKIP_BODY;
		}

		// rsv may be null in empty-forms (where not tableName attribute is provided)
		if (!footerReached) {
			return EVAL_BODY_BUFFERED;
		} else {
			return SKIP_BODY;
		}
	}

	/**
	 *   D O    E N D T A G
	 *
	 * @return  Description of the Return Value
	 * @exception  JspException Description of the Exception
	 */
	public int doEndTag() throws JspException {
		JspWriter jspOut = pageContext.getOut();

		// avoid to call getOut each time (Demeter law)
		try {
			if (bodyContent != null) {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
				bodyContent.clearBody();
				// 2002116-HKK: workaround for duplicate rows in Tomcat 4.1
			}

			logCat.debug("pageContext.getOut()=" + pageContext.getOut());
			logCat.debug("childElementOutput=" + childElementOutput);

			// hidden fields and other stuff coming from child elements get written out
			if (childElementOutput != null) {
				jspOut.println(childElementOutput.toString());
			}

			if (parentForm == null) {
				jspOut.println("</form>");
			}

			/** Generate Javascript validation methods & calls */
			if ((getFormValidatorName() != null) && (getFormValidatorName().length() > 0) && hasJavascriptValidationSet()) {
				jspOut.println(generateJavascriptValidation());
			}

			/**
			 *  Generate Javascript array of fields.
			 *  To help developper to work with DbForms fields name.
			 *
			 *  Ex: champ1 => f_0_1@root_4
			 */
			if (hasJavascriptFieldsArraySet()) {
				jspOut.println(generateJavascriptFieldsArray());
			}

			/**  Write generic Javascript functions created from childs tag */
			if (javascriptDistinctFunctions.size() > 0) {
				jspOut.println("\n<SCRIPT language=\"javascript\">\n");

				Enumeration enum = javascriptDistinctFunctions.keys();

				while (enum.hasMoreElements()) {
					String aKey = (String) enum.nextElement();
					StringBuffer sbFonction = (StringBuffer) javascriptDistinctFunctions.get(aKey);

					jspOut.println(sbFonction);
				}

				jspOut.println("\n</SCRIPT>\n");
			}
		} catch (IOException ioe) {
			logCat.error("::doEndTag - IOException", ioe);
		}
		logCat.info("end reached of " + tableName);

		return EVAL_PAGE;
	}

	//------------------------ business, helper & utility methods --------------------------------

	/**
	 *  Initialise datastructures containing informations about how table should be ordered.
	 *  The information is specified in the JSP this tags lives in.
	 *  This declaration OVERRULES other default declarations eventually done in XML config!
	 *  (compara Table.java !)
	 *
	 * @param  request the request object
	 */
	private FieldValue[] initOverrulingOrder(HttpServletRequest request) {
		// if page A links to page B (via a gotoButton, for instance) then we do not
		// want  A's order constraints get applied to B
		if (request != null) {
			String refSource = request.getRequestURI();

			if (request.getQueryString() != null) {
				refSource += ("?" + request.getQueryString());
			}

			String sourceTag = ParseUtil.getParameter(request, "source");
			logCat.info("!comparing page " + refSource + " TO " + sourceTag);

			//if (!Util.isNull(sourceTag) && !refSource.equals(sourceTag)) {
			// request = null;
			//}
		}

		logCat.debug("orderBy=" + orderBy);

		// if we have neither an orderby clause nor a request we may use then we cant create orderconstraint
		if ((orderBy == null) && (request == null)) {
			return null;
		}

		// otherwise we can:
		FieldValue[] overrulingOrder = table.createOrderFieldValues(orderBy, request, false);
		overrulingOrderFields = new Vector();

		if (overrulingOrder != null) {
			for (int i = 0; i < overrulingOrder.length; i++)
				overrulingOrderFields.addElement(overrulingOrder[i].getField());
		}

		return overrulingOrder;
	}

	/**
	 *  Initialize child values
	 */
	private void initChildFieldValues() {
		// if parent form has no data, we can not render a subform!
		if (ResultSetVector.isNull(parentForm.getResultSetVector())) {
			childFieldValues = null;
			// childFieldValues remains null
			return;
		}
		String aPosition = parentForm.getTable().getPositionString(parentForm.getResultSetVector());
		if (Util.isNull(aPosition)) {
			childFieldValues = null;
			// childFieldValues remains null
			return;
		}
		childFieldValues = getTable().mapChildFieldValues(parentForm.getTable(), parentField, childField, aPosition).toArray();
	}

	/**
	 * Initialize the value of the search fields.
	 *
	 * @return  the field values array
	 * @todo Whats when there is more then one search field whith the same name?<br/>Maybe we should parse all of them ....
	 */
	private FieldValue[] initSearchFieldValues() {
		FieldValue[] fieldValues;
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		Vector searchFieldNames = ParseUtil.getParametersStartingWith(request, "search_" + this.tableId);

		if ((searchFieldNames == null) || (searchFieldNames.size() == 0)) {
			return null;
		}

		Vector mode_and = new Vector();
		Vector mode_or = new Vector();

		for (int i = 0; i < searchFieldNames.size(); i++) {
			String searchFieldName = (String) searchFieldNames.elementAt(i);
			String aSearchFieldPattern = ParseUtil.getParameter(request, Constants.FIELDNAME_PATTERNTAG + searchFieldName);
			String aSearchFieldValue = ParseUtil.getParameter(request, searchFieldName);
			aSearchFieldValue = aSearchFieldValue.trim();

			// ie. search_1_12 is mapped to "john"
			if (!Util.isNull(aSearchFieldValue)) {
				int firstUnderscore = searchFieldName.indexOf('_');
				int secondUnderscore = searchFieldName.indexOf('_', firstUnderscore + 1);
				int tableId = Integer.parseInt(searchFieldName.substring(firstUnderscore + 1, secondUnderscore));

				// is equal to tableid, off course
				int fieldId = Integer.parseInt(searchFieldName.substring(secondUnderscore + 1));
				Field f = table.getField(fieldId);
				String aSearchMode = ParseUtil.getParameter(request, "searchmode_" + tableId + "_" + fieldId);
				int mode = ("and".equals(aSearchMode)) ? Constants.SEARCHMODE_AND : Constants.SEARCHMODE_OR;
				String aSearchAlgorithm = ParseUtil.getParameter(request, "searchalgo_" + tableId + "_" + fieldId);

				// Check for operator
				int algorithm = Constants.SEARCH_ALGO_SHARP;
				int operator = Constants.FILTER_EQUAL;
				if (!Util.isNull(aSearchAlgorithm)) {
					if (aSearchAlgorithm.startsWith("sharpLT")) {
						operator = Constants.FILTER_SMALLER_THEN;
					} else if (aSearchAlgorithm.startsWith("sharpLE")) {
						operator = Constants.FILTER_SMALLER_THEN_EQUAL;
					} else if (aSearchAlgorithm.startsWith("sharpGT")) {
						operator = Constants.FILTER_GREATER_THEN;
					} else if (aSearchAlgorithm.startsWith("sharpGE")) {
						operator = Constants.FILTER_GREATER_THEN_EQUAL;
					} else if (aSearchAlgorithm.startsWith("sharpNE")) {
						operator = Constants.FILTER_NOT_EQUAL;
					} else if (aSearchAlgorithm.startsWith("sharpNULL")) {
						operator = Constants.FILTER_NULL;
					} else if (aSearchAlgorithm.startsWith("sharpNOTNULL")) {
						operator = Constants.FILTER_NOT_NULL;
					} else if (aSearchAlgorithm.startsWith("weakStartEnd")) {
						algorithm = Constants.SEARCH_ALGO_WEAK_START_END;
						operator = Constants.FILTER_LIKE;
					} else if (aSearchAlgorithm.startsWith("weakStart")) {
						algorithm = Constants.SEARCH_ALGO_WEAK_START;
						operator = Constants.FILTER_LIKE;
					} else if (aSearchAlgorithm.startsWith("weakEnd")) {
						algorithm = Constants.SEARCH_ALGO_WEAK_END;
						operator = Constants.FILTER_LIKE;
					} else if (aSearchAlgorithm.startsWith("weak")) {
						algorithm = Constants.SEARCH_ALGO_WEAK;
						operator = Constants.FILTER_LIKE;
					}
				}

				if ((aSearchAlgorithm == null) || (aSearchAlgorithm.toLowerCase().indexOf("extended") == -1)) {
					// Extended not found, only append field
					FieldValue fv =
						FieldValue.createFieldValueForSearching(f, aSearchFieldValue, getLocale(), operator, mode, algorithm, false);
					if (!Util.isNull(aSearchFieldPattern)) {
						fv.setPattern(aSearchFieldPattern);
					}

					if (mode == Constants.SEARCHMODE_AND) {
						mode_and.addElement(fv);
					} else {
						mode_or.addElement(fv);
					}
				} else if (aSearchFieldValue.indexOf("-") != -1) {
					// is extended searching and delimiter found in SearchFieldValue
					// create 2 searchfields
					algorithm = Constants.SEARCH_ALGO_EXTENDED;

					StringTokenizer st = new StringTokenizer(" " + aSearchFieldValue + " ", "-");
					int tokenCounter = 0;

					while (st.hasMoreTokens()) {
						aSearchFieldValue = st.nextToken().trim();
						tokenCounter++;

						if (aSearchFieldValue.length() > 0) {
							switch (tokenCounter) {
								case 1 :
									operator = Constants.FILTER_GREATER_THEN_EQUAL;
									break;

								case 2 :
									operator = Constants.FILTER_SMALLER_THEN_EQUAL;
									break;

								default :
									operator = -1;
									break;
							}

							if (operator != -1) {
								FieldValue fv =
									FieldValue.createFieldValueForSearching(
										f,
										aSearchFieldValue,
										getLocale(),
										operator,
										mode,
										algorithm,
										false);
								if (!Util.isNull(aSearchFieldPattern)) {
									fv.setPattern(aSearchFieldPattern);
								}
								if (mode == Constants.SEARCHMODE_AND) {
									mode_and.addElement(fv);
								} else {
									mode_or.addElement(fv);
								}
							}
						}
					}
				} else {
					// parse special chars in SearchFieldValue
					int jump = 0;

					// Check for Not Equal
					if (aSearchFieldValue.startsWith("<>")) {
						algorithm = Constants.SEARCH_ALGO_EXTENDED;
						operator = Constants.FILTER_NOT_EQUAL;
						jump = 2;

						// Check for not equal
					} else if (aSearchFieldValue.startsWith("!=")) {
						// GreaterThenEqual found! - Store the operation for use later on
						algorithm = Constants.SEARCH_ALGO_EXTENDED;
						operator = Constants.FILTER_NOT_EQUAL;
						jump = 2;

						// Check for GreaterThanEqual
					} else if (aSearchFieldValue.startsWith(">=")) {
						// GreaterThenEqual found! - Store the operation for use later on
						algorithm = Constants.SEARCH_ALGO_EXTENDED;
						operator = Constants.FILTER_GREATER_THEN_EQUAL;
						jump = 2;

						// Check for GreaterThan
					} else if (aSearchFieldValue.startsWith(">")) {
						// GreaterThen found! - Store the operation for use later on
						algorithm = Constants.SEARCH_ALGO_EXTENDED;
						operator = Constants.FILTER_GREATER_THEN;

						// Check for SmallerThenEqual
					} else if (aSearchFieldValue.startsWith("<=")) {
						// SmallerThenEqual found! - Store the operation for use later on
						algorithm = Constants.SEARCH_ALGO_EXTENDED;
						operator = Constants.FILTER_SMALLER_THEN_EQUAL;
						jump = 2;

						// Check for SmallerThen
					} else if (aSearchFieldValue.startsWith("<")) {
						// SmallerThen found! - Store the operation for use later on
						algorithm = Constants.SEARCH_ALGO_EXTENDED;
						operator = Constants.FILTER_SMALLER_THEN;
						jump = 1;

						// Check for equal
					} else if (aSearchFieldValue.startsWith("=")) {
						// Equal found! - Store the operator for use later on
						algorithm = Constants.SEARCH_ALGO_EXTENDED;
						operator = Constants.FILTER_EQUAL;
						jump = 1;
					} else if (aSearchFieldValue.startsWith("[NULL]")) {
						algorithm = Constants.SEARCH_ALGO_EXTENDED;
						operator = Constants.FILTER_NULL;
						jump = 0;
					} else if (aSearchFieldValue.startsWith("[!NULL]")) {
						algorithm = Constants.SEARCH_ALGO_EXTENDED;
						operator = Constants.FILTER_NOT_NULL;
						jump = 0;
					} else if (aSearchFieldValue.startsWith("[EMPTY]")) {
						algorithm = Constants.SEARCH_ALGO_EXTENDED;
						operator = Constants.FILTER_EMPTY;
						jump = 0;
					} else if (aSearchFieldValue.startsWith("[!EMPTY]")) {
						algorithm = Constants.SEARCH_ALGO_EXTENDED;
						operator = Constants.FILTER_NOT_EMPTY;
						jump = 0;
					}

					if (jump > 0) {
						aSearchFieldValue = aSearchFieldValue.substring(jump).trim();
					}

					Vector errors = (Vector) request.getAttribute("errors");

					if ((operator == Constants.FILTER_EQUAL) && (jump == 0) && (f.getType() == FieldTypes.TIMESTAMP)) {
						// found a single timestamp value. Extend it to >value and <end of day of value
						operator = Constants.FILTER_GREATER_THEN_EQUAL;

						FieldValue fv =
							FieldValue.createFieldValueForSearching(
								f,
								aSearchFieldValue,
								getLocale(),
								operator,
								mode,
								algorithm,
								false);
						if (!Util.isNull(aSearchFieldPattern)) {
							fv.setPattern(aSearchFieldPattern);
						}
						Date d = (Date) fv.getFieldValueAsObject();

						if (d == null) {
							errors.add(
								new Exception(MessageResourcesInternal.getMessage("dbforms.error.filter.invalid.date", getLocale())));
						} else {
							if (mode == Constants.SEARCHMODE_AND) {
								mode_and.addElement(fv);
							} else {
								mode_or.addElement(fv);
							}

							operator = Constants.FILTER_SMALLER_THEN_EQUAL;
							d = TimeUtil.findEndOfDay(d);
							aSearchFieldValue = d.toString();

							if (d != null) {
								fv =
									FieldValue.createFieldValueForSearching(
										f,
										aSearchFieldValue,
										getLocale(),
										operator,
										mode,
										algorithm,
										false);
								if (!Util.isNull(aSearchFieldPattern)) {
									fv.setPattern(aSearchFieldPattern);
								}
								if (mode == Constants.SEARCHMODE_AND) {
									mode_and.addElement(fv);
								} else {
									mode_or.addElement(fv);
								}
							}
						}
					} else {
						FieldValue fv =
							FieldValue.createFieldValueForSearching(
								f,
								aSearchFieldValue,
								getLocale(),
								operator,
								mode,
								algorithm,
								false);
						if (!Util.isNull(aSearchFieldPattern)) {
							fv.setPattern(aSearchFieldPattern);
						}
						Object obj = fv.getFieldValueAsObject();

						if (obj == null) {
							errors.add(
								new Exception(MessageResourcesInternal.getMessage("dbforms.error.filter.invalid", getLocale())));
						} else {
							if (mode == Constants.SEARCHMODE_AND) {
								mode_and.addElement(fv);
							} else {
								mode_or.addElement(fv);
							}
						}
					}
				}
			}
		}

		int andBagSize = mode_and.size();
		int orBagSize = mode_or.size();
		int criteriaFieldCount = andBagSize + orBagSize;
		logCat.info("criteriaFieldCount=" + criteriaFieldCount);
		if (criteriaFieldCount == 0) {
			return null;
		}

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
	}

	/**
	 *  This method gets called by input-tags like "DbTextFieldTag" and others. they signalize that
	 *  _they_ will generate the tag for the controller, not this form.
	 *  <br>
	 *  see produceLinkedTags().
	 *  <br>
	 *  This method is only used if this class is instantiated as sub-form (== embedded in another
	 *  form's body tag)
	 *
	 * @param  f  the Field object
	 */
	public void strikeOut(Field f) {
		// childFieldValues may be null, if we have
		// a free form select using attribute whereClause
		if (childFieldValues != null) {
			for (int i = 0; i < childFieldValues.length; i++) {
				if ((f == childFieldValues[i].getField()) && childFieldValues[i].getRenderHiddenHtmlTag()) {
					childFieldValues[i].setRenderHiddenHtmlTag(false);
					logCat.info("stroke out field:" + f.getName());
					return;
				}
			}
		}
	}

	/**
	* DOCUMENT ME!
	*
	* @return  DOCUMENT ME!
	*/
	public String produceLinkedTags() {
		StringBuffer buf = new StringBuffer();
		// childFieldValues m ay be null, if we have
		// a free form select using attribute whereClause
		if (childFieldValues != null) {
			for (int i = 0; i < childFieldValues.length; i++) {
				if (childFieldValues[i].getRenderHiddenHtmlTag()) {
					TextFormatter formatter = new TextFormatter(childFieldValues[i].getField(), getLocale(), null, childFieldValues[i].getFieldValueAsObject());
					buf.append("<input type=\"hidden\" name=\"");
					buf.append(formatter.getFormFieldName(this));
					buf.append("\" value=\"");
					buf.append(formatter.getFormattedFieldValue());
					buf.append("\" />");
					buf.append(formatter.renderPatternHtmlInputField());
				}
			}
		}
		return buf.toString();
	}

	/**
	 * if we are in a subform we must check if the fieldvalue-list provided in the
	 * position strings is valid in the current state it might be invalid
	 * if the position of the parent form has been changed (by a navigation event)
	 * (=> the position-strings of childforms arent valid anymore)
	 *
	 * @param  childFieldValues Description of the Parameter
	 * @param  aPosition Description of the Parameter
	 * @return  Description of the Return Value
	 */
	private boolean checkLinkage(FieldValue[] childFieldValues, String aPosition) {
		// at first build a hashtable of the provided values
		// 2003-03-29 HKK: Change from Hashtable to FieldValueTable 
		FieldValues ht = table.getFieldValues(aPosition);

		for (int i = 0; i < childFieldValues.length; i++) {
			String actualValue = childFieldValues[i].getFieldValue();
			logCat.debug("actualValue=" + actualValue);

			Field f = childFieldValues[i].getField();
			logCat.debug("f.getName=" + f.getName());
			logCat.debug("f.getId=" + f.getId());

			FieldValue aFieldValue = ht.get(f.getName());

			if (aFieldValue == null) {
				throw new IllegalArgumentException(
					"ERROR: Make sure that field "
						+ f.getName()
						+ " is a KEY of the table "
						+ table.getName()
						+ "! Otherwise you can not use it as PARENT/CHILD LINK argument!");
			}

			String valueInPos = aFieldValue.getFieldValue();

			logCat.info("comparing " + actualValue + " TO " + valueInPos);

			if (!actualValue.trim().equals(valueInPos.trim())) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Generate Javascript Array of Original field name et DbForm generated name
	 * Generate Array for each field name.
	 *
	 * Ex: dbFormFields
	 *
	 *
	 * @return  Description of the Return Value
	 */
	private StringBuffer generateJavascriptFieldsArray() {
		// This section looks hard to understand, but to avoid using
		// synchronized object like keySet ... don't want to alter
		// childFieldNames hashtable.
		// We use different step of enumeration.
		StringBuffer result = new StringBuffer();
		String key = null;
		String val = null;
		String values = "";

		Hashtable fields = new Hashtable();
		Enumeration enum = childFieldNames.keys();

		//
		// Loop in each keys "f_0_0@root_2" and create hashtable of unique fieldnames
		//
		while (enum.hasMoreElements()) {
			key = (String) enum.nextElement();
			val = (String) childFieldNames.get(key);
			values = "";

			if (fields.containsKey(val)) {
				values = (String) fields.get(val);
			}

			fields.put(val, values + ";" + key);
		}

		if (isSubForm()) {
			parentForm.addFieldNames(fields);
		} else {
			if (fieldNames != null) {
				fields.putAll(fieldNames);
			}

			result.append("<SCRIPT language=\"javascript\">\n");
			result.append("<!-- \n\n");
			result.append("    var dbFormFields = new Array();\n");
			enum = fields.keys();

			//
			// Loop for each fieldname and generate text for javascript Array
			//
			// Ex: dbFormFields["DESCRIPTIONDEMANDE"] = new Array("f_0_0@root_4", "f_0_1@root_4", "f_0_insroot_4");
			//
			while (enum.hasMoreElements()) {
				key = (String) enum.nextElement();
				val = (String) fields.get(key);
				result.append("    dbFormFields[\"").append(key).append("\"] = new Array(");

				// Sort the delimited string and return an ArrayList of it.
				ArrayList arrValues = sortFields(val);

				if (arrValues.size() == 1) {
					result.append("\"").append((String) arrValues.get(0)).append("\"");
				} else {
					for (int i = 0; i <= (arrValues.size() - 1); i++) {
						result.append("\"").append((String) arrValues.get(i)).append("\"");

						if (i != (arrValues.size() - 1)) {
							result.append(", ");
						}
					}
				}

				result.append(");\n");
			}

			result.append("\n    function getDbFormFieldName(name){ \n");
			result.append("      return getDbFormFieldName(name,null); \n");
			result.append("    }\n\n");
			result.append("\n    function getDbFormFieldName(name,pos){ \n");
			result.append("      var result = dbFormFields[name]; \n");
			result.append("      if(pos==null) return result[result.length-1]; \n");
			result.append("      return result[pos]; \n");
			result.append("    }\n");
			result.append("--></SCRIPT> \n");
		}

		return result;
	}

	/**
	 * Generate  the Javascript of Validation fields
	 *
	 * @return  Description of the Return Value
	 */
	private StringBuffer generateJavascriptValidation() {
		if (isSubForm()) {
			parentForm.addValidationForm(getFormValidatorName(), childFieldNames);

			return new StringBuffer();
		} else {
			ValidatorResources vr =
				(ValidatorResources) pageContext.getServletContext().getAttribute(ValidatorConstants.VALIDATOR);
			DbFormsErrors errors = (DbFormsErrors) pageContext.getServletContext().getAttribute(DbFormsErrors.ERRORS);
			addValidationForm(getFormValidatorName(), childFieldNames);

			return DbFormsValidatorUtil.getJavascript(
				validationForms,
				MessageResources.getLocale((HttpServletRequest) pageContext.getRequest()),
				validationFields,
				vr,
				getJavascriptValidationSrcFile(),
				errors);
		}
	}

	/**
	 *  Use by generateJavascriptFieldsArray() to sort the order
	 *  of field name.
	 *
	 * @param  str Description of the Parameter
	 * @return  Description of the Return Value
	 */
	private ArrayList sortFields(String str) {
		/*
		 *  Sort delimited string of DbForms field, and return ArraList of this result.
		 *
		 *  Ex: "f_0_1@root_1;f_0_insroot_1;f_0_0@root_1;"
		 *
		 *      result : "f_0_0@root_1"
		 *               "f_0_1@root_1"
		 *               "f_0_insroot_1;"
		 */
		ArrayList arr = new ArrayList();
		String tmp = "";
		String tmp1 = null;
		String tmp2 = null;
		String insroot = null;
		int ident1 = 0;
		int ident2 = 0;
		StringTokenizer token = new StringTokenizer(str, ";");

		while (token.hasMoreTokens()) {
			tmp = token.nextToken();

			if (tmp.indexOf("@root") != -1) {
				arr.add(tmp);
			} else {
				insroot = tmp;
			}
		}

		if (insroot != null) {
			arr.add(insroot);
		}

		//String[] result = (String[]) arr.toArray(new String[arr.size()]);
		if (arr.size() == 1) {
			return arr;
		}

		for (int i = 0; i <= (arr.size() - 2); i++) {
			tmp1 = (String) arr.get(i);

			for (int j = i + 1; j <= (arr.size() - 1); j++) {
				tmp2 = (String) arr.get(j);

				if ((tmp1.indexOf("@root") != -1) && (tmp2.indexOf("@root") != -1)) {
					try {
						ident1 = Integer.parseInt(tmp1.substring(tmp1.indexOf("_", 2) + 1, tmp1.indexOf("@")));
						ident2 = Integer.parseInt(tmp2.substring(tmp2.indexOf("_", 2) + 1, tmp2.indexOf("@")));
					} catch (Exception e) {
						ident1 = -1;
						ident2 = -1;
					}

					if (ident2 < ident1) {
						arr.set(i, tmp2);
						arr.set(j, tmp1);
						tmp1 = tmp2;
					}
				}
			}
		}

		return arr;
	}

	/**
	 *  This method allow to retreive value from resultsetVector
	 *  from current Form, parentForm or from request.
	 *
	 * @param  name Description of the Parameter
	 * @return  The childFieldValue value
	 */
/*	
	public String getChildFieldValue(String name) {
		ResultSetVector result = null;
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		if ((webEvent != null) && (webEvent.getType().equals(EventType.EVENT_NAVIGATION_RELOAD))) {
			Field field = getTable().getFieldByName(name);

			if (field == null) {
				logCat.warn("Field name : " + name + " is not present in Table " + getTable().getName());
			}

			String keyIndex =
				(getFooterReached()) ? (Constants.FIELDNAME_INSERTPREFIX + getPositionPathCore()) : getPositionPath();
			StringBuffer buf = new StringBuffer();

			buf.append(Constants.FIELDNAME_PREFIX);
			buf.append(getTable().getId());
			buf.append("_");
			buf.append(keyIndex);
			buf.append("_");
			buf.append(field.getId());

			return ParseUtil.getParameter(request, buf.toString());
		}

		if (name.indexOf(":") != -1) {
			name = name.substring(name.indexOf("."), name.length());

			if (parentForm != null) {
				result = parentForm.getResultSetVector();
			} else {
				result = getResultSetVector();
			}
		} else {
			result = getResultSetVector();
		}

		if (result != null) {
			return result.getField(name);
		} else {
			return null;
		}
	}
*/
	/** DOCUMENT ME! */
	public void doFinally() {
		logCat.info("doFinally called");

		if (validationForms != null) {
			validationForms.clear();
		}

		if (validationFields != null) {
			validationFields.clear();
		}

		if (fieldNames != null) {
			fieldNames.clear();
		}

		sqlFilter = null;
		orderFields = null;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param  t DOCUMENT ME!
	 * @throws  Throwable DOCUMENT ME!
	 */
	public void doCatch(Throwable t) throws Throwable {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		logCat.error("DbFormTag.doCatch called - " + t.toString() + "\n" + sw.toString());
		throw t;
	}

	/**
	 * @return
	 */
	public WebEvent getWebEvent() {
		return webEvent;
	}

	/**
	 * @return
	 */
	public String getName() {
		return (name != null) ? name : tableName;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param string
	 */
	public void setId(String string) {
		id = string;
	}

	/**
	 * @return autocomplete
	 */
	public String getAutocomplete() {
		return autocomplete;
	}

	/**
	 * @param string
	 */
	public void setAutocomplete(String string) {
		autocomplete = string;
	}

}