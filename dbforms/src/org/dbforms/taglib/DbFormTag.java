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

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.dbforms.*;
import org.dbforms.util.*;
import org.dbforms.event.*;
import org.dbforms.validation.ValidatorConstants;
import org.dbforms.validation.DbFormsValidatorUtil;
import org.apache.commons.validator.ValidatorResources;
import org.apache.log4j.Category;



/**
 * <p>this is the root element of a data manipulation form</p>
 *
 * @author  Joachim Peer <j.peer@gmx.net>
 * @created  16 novembre 2002
 */
public class DbFormTag extends BodyTagSupport implements TryCatchFinally
{
    /** logging category for this class */
    static Category logCat = Category.getInstance(DbFormTag.class.getName());

    /** access data defined in dbforms-config.xml */
    private DbFormsConfig config;

    /** the id of the underlying table */
    private int tableId;

    /** the underlying table */
    private Table table;

    /** the name of the  underlying table */
    private String tableName;

    /** count of this fomr (n ||) */
    private String maxRows;

    /** count (multiplicity, view-mode) of this form (n || -1), whereby n E N (==1,2..z) */
    private int count;

    /** the data to be rendered */
    private ResultSetVector resultSetVector;

    /** String formatted like {1:3:345-2:4:hugo} */
    private String position;

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

    /** multipart must be set to "true" if the form contains file-upload tags or if the form may be invoked by a form containing file-upload tags */
    private boolean _isMultipart = false;

    /** site to be invoked after action - nota bene: this followUp may be overruled by "followUp"-attributes of actionButtons */
    private String followUp;

    /** site to be invoked after action if previous form contained errors- nota bene: this followUp may be overruled by "followUp"-attributes of actionButtons */
    private String followUpOnError;

    /** pedant to the html-target attribute in html-form tag: the target frame to jump to */
    private String target;

    /**
     * if "true", at every action (navigation, insert, update, etc.) all input fields of ALL currently rendered rowsets are parsed and updated.
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

    /** used in sub-form: this data structure holds the linked childfield(s) and their current values it/they derive from the main form */
    private FieldValue[] childFieldValues;

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
    private String dbConnectionName;

    /** Bradley's multiple connection stuff [fossato <fossato@pow2.com> 20021105] */
    private Connection con;

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

    /** List of all child field name with assosciate generated name. Ex:  "champ1" : "f_0_3@root_3" */
    private Hashtable childFieldNames = new Hashtable();

    /** Used to avoid creation of same javascript function. */
    private Hashtable javascriptDistinctFunctions = new Hashtable();

    /** Indicate if the form is in read-only mode */
    private String readOnly = "false";

    /** Keep trace of witch event is use */
    private WebEvent webEvent = null;


    /**
     *  Sets the tableName attribute of the DbFormTag object
     *
     * @param  tableName The new tableName value
     */
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
        this.table = config.getTableByName(tableName);
        this.tableId = table.getId();
    }


    /**
     *  Gets the tableName attribute of the DbFormTag object
     *
     * @return  The tableName value
     */
    public String getTableName()
    {
        return tableName;
    }


    /**
     *  Gets the table attribute of the DbFormTag object
     *
     * @return  The table value
     */
    public Table getTable()
    {
        return table;
    }


    /**
     *  Sets the maxRows attribute of the DbFormTag object
     *
     * @param  maxRows The new maxRows value
     */
    public void setMaxRows(String maxRows)
    {
        this.maxRows = maxRows;

        if (maxRows.trim().equals("*"))
        {
            this.count = 0;
        }
        else
        {
            this.count = Integer.parseInt(maxRows);
        }
    }


    /**
     *  Gets the maxRows attribute of the DbFormTag object
     *
     * @return  The maxRows value
     */
    public String getMaxRows()
    {
        return maxRows;
    }


    /**
     *  Gets the count attribute of the DbFormTag object
     *
     * @return  The count value
     */
    public int getCount()
    {
        return count;
    }


    /**
     *  Gets the currentCount attribute of the DbFormTag object
     *
     * @return  The currentCount value
     */
    public int getCurrentCount()
    {
        return currentCount;
    }


    /**  Description of the Method */
    public void increaseCurrentCount()
    {
        currentCount++;
    }


    /**  Description of the Method */
    public void updatePositionPath()
    {
        StringBuffer positionPathBuf = new StringBuffer();

        positionPathBuf.append(this.currentCount);
        positionPathBuf.append("@");
        positionPathBuf.append(this.positionPathCore);
        this.positionPath = positionPathBuf.toString();

        // ie. "5" + "@" + "root", "5" + "@" + "123@35@root"
    }


    /**
     *  Gets the positionPath attribute of the DbFormTag object
     *
     * @return  The positionPath value
     */
    public String getPositionPath()
    {
        return positionPath;
    }


    /**
     *  Gets the positionPathCore attribute of the DbFormTag object
     *
     * @return  The positionPathCore value
     */
    public String getPositionPathCore()
    {
        return positionPathCore;

        // ie.  "root",  "123@35@root"
    }


    /**
     *  Sets the followUp attribute of the DbFormTag object
     *
     * @param  followUp The new followUp value
     */
    public void setFollowUp(String followUp)
    {
        this.followUp = followUp;
    }


    /**
     *  Gets the followUp attribute of the DbFormTag object
     *
     * @return  The followUp value
     */
    public String getFollowUp()
    {
        return followUp;
    }


    /**
     *  Sets the target attribute of the DbFormTag object
     *
     * @param  target The new target value
     */
    public void setTarget(String target)
    {
        this.target = target;
    }


    /**
     *  Gets the target attribute of the DbFormTag object
     *
     * @return  The target value
     */
    public String getTarget()
    {
        return target;
    }


    /**
     *  Sets the autoUpdate attribute of the DbFormTag object
     *
     * @param  autoUpdate The new autoUpdate value
     */
    public void setAutoUpdate(String autoUpdate)
    {
        this.autoUpdate = autoUpdate;
    }


    /**
     *  Gets the autoUpdate attribute of the DbFormTag object
     *
     * @return  The autoUpdate value
     */
    public String getAutoUpdate()
    {
        return autoUpdate;
    }


    /**
     *  Sets the parentField attribute of the DbFormTag object
     *
     * @param  parentField The new parentField value
     */
    public void setParentField(String parentField)
    {
        this.parentField = parentField;
    }


    /**
     *  Gets the parentField attribute of the DbFormTag object
     *
     * @return  The parentField value
     */
    public String getParentField()
    {
        return parentField;
    }


    /**
     *  Sets the childField attribute of the DbFormTag object
     *
     * @param  childField The new childField value
     */
    public void setChildField(String childField)
    {
        this.childField = childField;
    }


    /**
     *  Gets the childField attribute of the DbFormTag object
     *
     * @return  The childField value
     */
    public String getChildField()
    {
        return childField;
    }


    /**
     *  Sets the orderBy attribute of the DbFormTag object
     *
     * @param  orderBy The new orderBy value
     */
    public void setOrderBy(String orderBy)
    {
        this.orderBy = orderBy;
    }


    /**
     *  Gets the orderBy attribute of the DbFormTag object
     *
     * @return  The orderBy value
     */
    public String getOrderBy()
    {
        return orderBy;
    }


    /**
     *  Sets the filter attribute of the DbFormTag object
     *
     * @param  filter The new filter value
     */
    public void setFilter(String filter)
    {
        this.filter = filter;
        initFilterFieldValues();
    }


    /**
     *  Gets the filter attribute of the DbFormTag object
     *
     * @return  The filter value
     */
    public String getFilter()
    {
        return filter;
    }


    /**
     *  Sets the gotoPrefix attribute of the DbFormTag object
     *
     * @param  gotoPrefix The new gotoPrefix value
     */
    public void setGotoPrefix(String gotoPrefix)
    {
        this.gotoPrefix = gotoPrefix;
    }


    /**
     *  Gets the gotoPrefix attribute of the DbFormTag object
     *
     * @return  The gotoPrefix value
     */
    public String getGotoPrefix()
    {
        return gotoPrefix;
    }


    /**
     *  Sets the gotoHt attribute of the DbFormTag object
     *
     * @param  gotoHt The new gotoHt value
     */
    public void setGotoHt(Hashtable gotoHt)
    {
        this.gotoHt = gotoHt;

        //this.gotoHt = (Hashtable) pageContext.getAttribute(gotoPos);
    }


    /**
     *  Gets the gotoHt attribute of the DbFormTag object
     *
     * @return  The gotoHt value
     */
    public Hashtable getGotoHt()
    {
        return gotoHt;
    }


    /**
     *  Sets the multipart attribute of the DbFormTag object
     *
     * @param  multipart The new multipart value
     */
    public void setMultipart(String multipart)
    {
        this.multipart = multipart;
        this._isMultipart = "true".equals(multipart);
    }


    /**
     *  Gets the multipart attribute of the DbFormTag object
     *
     * @return  The multipart value
     */
    public String getMultipart()
    {
        return multipart;
    }


    /**
     *  Description of the Method
     *
     * @return  Description of the Return Value
     */
    public boolean hasMultipartCapability()
    {
        return this._isMultipart;
    }


    /**
     *  Sets the dbConnectionName attribute of the DbFormTag object
     *
     * @param  dbConnectionName The new dbConnectionName value
     */
    public void setDbConnectionName(String dbConnectionName)
    {
        this.dbConnectionName = dbConnectionName;
    }


    /**
     *  Gets the dbConnectionName attribute of the DbFormTag object
     *
     * @return  The dbConnectionName value
     */
    public String getDbConnectionName()
    {
        return dbConnectionName;
    }


    /**
     *  Sets the connection attribute of the DbFormTag object
     *
     * @param  con The new connection value
     */
    public void setConnection(Connection con)
    {
        this.con = con;
    }


    /**
     *  Gets the connection attribute of the DbFormTag object
     *
     * @return  The connection value
     */
    public Connection getConnection()
    {
        return con;
    }


    /**
     *  Sets the footerReached attribute of the DbFormTag object
     *
     * @param  footerReached The new footerReached value
     */
    public void setFooterReached(boolean footerReached)
    {
        this.footerReached = footerReached;
    }


    /**
     *  Gets the footerReached attribute of the DbFormTag object
     *
     * @return  The footerReached value
     */
    public boolean getFooterReached()
    {
        return footerReached;
    }


    /**
     *  Sets the localWebEvent attribute of the DbFormTag object
     *
     * @param  localWebEvent The new localWebEvent value
     */
    public void setLocalWebEvent(String localWebEvent)
    {
        this.localWebEvent = localWebEvent;
    }


    /**
     *  Gets the localWebEvent attribute of the DbFormTag object
     *
     * @return  The localWebEvent value
     */
    public String getLocalWebEvent()
    {
        return localWebEvent;
    }


    /**
     *  Gets the resultSetVector attribute of the DbFormTag object
     *
     * @return  The resultSetVector value
     */
    public ResultSetVector getResultSetVector()
    {
        return resultSetVector;
    }


    /**
     *  Gets the subForm attribute of the DbFormTag object
     *
     * @return  The subForm value
     */
    public boolean isSubForm()
    {
        return isSubForm;
    }


    /**
     *  Gets the childElementOutput attribute of the DbFormTag object
     *
     * @return  The childElementOutput value
     */
    public StringBuffer getChildElementOutput()
    {
        return childElementOutput;
    }


    /**
     *  Sets the whereClause attribute of the DbFormTag object
     *
     * @param  wc The new whereClause value
     */
    public void setWhereClause(String wc)
    {
        this.whereClause = wc;
    }


    /**
     *  Gets the whereClause attribute of the DbFormTag object
     *
     * @return  The whereClause value
     */
    public String getWhereClause()
    {
        return whereClause;
    }


    /**
     *  Sets the formValidatorName attribute of the DbFormTag object
     *
     * @param  fv The new formValidatorName value
     */
    public void setFormValidatorName(String fv)
    {
        this.formValidatorName = fv;
    }


    /**
     *  Gets the formValidatorName attribute of the DbFormTag object
     *
     * @return  The formValidatorName value
     */
    public String getFormValidatorName()
    {
        return formValidatorName;
    }


    /**
     *  Sets the captionResource attribute of the DbFormTag object
     *
     * @param  res The new captionResource value
     */
    public void setCaptionResource(String res)
    {
        this.captionResource = res;
    }


    /**
     *  Gets the captionResource attribute of the DbFormTag object
     *
     * @return  The captionResource value
     */
    public String getCaptionResource()
    {
        return captionResource;
    }


    /**
     *  Sets the javascriptValidation attribute of the DbFormTag object
     *
     * @param  jsv The new javascriptValidation value
     */
    public void setJavascriptValidation(String jsv)
    {
        this.javascriptValidation = jsv;
    }


    /**
     *  Gets the javascriptValidation attribute of the DbFormTag object
     *
     * @return  The javascriptValidation value
     */
    public String getJavascriptValidation()
    {
        return javascriptValidation;
    }


    /**
     *  Sets the javascriptFieldsArray attribute of the DbFormTag object
     *
     * @param  jfa The new javascriptFieldsArray value
     */
    public void setJavascriptFieldsArray(String jfa)
    {
        this.javascriptFieldsArray = jfa;
    }


    /**
     *  Gets the javascriptFieldsArray attribute of the DbFormTag object
     *
     * @return  The javascriptFieldsArray value
     */
    public String getJavascriptFieldsArray()
    {
        return javascriptFieldsArray;
    }


    /**
     * DOCUMENT ME!
     *
     * @param  jsvs DOCUMENT ME!
     */
    public void setJavascriptValidationSrcFile(String jsvs)
    {
        this.javascriptValidationSrcFile = jsvs;
    }


    /**
     *  Gets the javascriptValidationSrcFile attribute of the DbFormTag object
     *
     * @return  The javascriptValidationSrcFile value
     */
    public String getJavascriptValidationSrcFile()
    {
        return javascriptValidationSrcFile;
    }


    /**
     *  Sets the readOnly attribute of the DbFormTag object
     *
     * @param  readOnly The new readOnly value
     */
    public void setReadOnly(String readOnly)
    {
        this.readOnly = readOnly;
    }


    /**
     *  Gets the readOnly attribute of the DbFormTag object
     *
     * @return  The readOnly value
     */
    public String getReadOnly()
    {
        return readOnly;
    }


    /**
     *  Adds a feature to the JavascriptFunction attribute of the DbFormTag object
     *
     * @param  jsFctName The feature to be added to the JavascriptFunction attribute
     * @param  jsFct The feature to be added to the JavascriptFunction attribute
     */
    public void addJavascriptFunction(String jsFctName, StringBuffer jsFct)
    {
        if (!existJavascriptFunction(jsFctName))
        {
            javascriptDistinctFunctions.put(jsFctName, jsFct);
        }
    }


    /**
     *  Description of the Method
     *
     * @param  jsFctName Description of the Parameter
     * @return  Description of the Return Value
     */
    public boolean existJavascriptFunction(String jsFctName)
    {
        return javascriptDistinctFunctions.containsKey(jsFctName);
    }


    /**
     *  Adds a feature to the ChildName attribute of the DbFormTag object
     *
     * @param  tableFieldName The feature to be added to the ChildName attribute
     * @param  dbFormGeneratedName The feature to be added to the ChildName attribute
     */
    public void addChildName(String tableFieldName, String dbFormGeneratedName)
    {
        //	childFieldNames.put(tableFieldName, dbFormGeneratedName);
        childFieldNames.put(dbFormGeneratedName, tableFieldName);
    }


    /**
     *  Description of the Method
     *
     * @param  str Description of the Parameter
     */
    public void appendToChildElementOutput(String str)
    {
        this.childElementOutput.append(str);
    }


    /**
     *  Sets the pageContext attribute of the DbFormTag object
     *
     * @param  pc The new pageContext value
     */
    public void setPageContext(PageContext pc)
    {
        super.setPageContext(pc);
        config = (DbFormsConfig) pageContext.getServletContext().getAttribute(DbFormsConfig.CONFIG);

        if (config == null)
        {
            throw new IllegalArgumentException("Troubles with DbForms config xml file: can not find CONFIG object in application context! check system configuration! check if application crashes on start-up!");
        }

        // Bradley's version doesn't have this code... [fossato <fossato@pow2.com> 20021105]
        //                DbConnection aDbConnection = config.getDbConnection();
        //		if (aDbConnection == null)
        //			throw new IllegalArgumentException("Troubles in your DbForms config xml file: DbConnection not properly included - check manual!");
        //		con = aDbConnection.getConnection();
        //		logCat.debug("Created new connection - " + con);
        //
        //		if (con == null)
        //			throw new IllegalArgumentException(
        //				"JDBC-Troubles: was not able to create connection, using the following DbConnection:"
        //					+ aDbConnection.toString());
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
    public int doStartTag()
    {
        try
        {
            // ---- Bradley's multiple connection stuff [fossato <fossato@pow2.com> [20021105] ----
            DbConnection aDbConnection = config.getDbConnection(dbConnectionName);

            if (aDbConnection == null)
            {
                throw new IllegalArgumentException("DbConnection named '" + dbConnectionName + "' is not configured properly.");
            }

            con = aDbConnection.getConnection();
            logCat.debug("Created new connection - " + con);

            if (con == null)
            {
                throw new IllegalArgumentException("JDBC-Troubles:  was not able to create " + "connection, using the following " + "dbconnection - " + aDbConnection);
            }

            // ---- Bradley's multiple connection stuff end ---------------------------------------
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
            if ((table != null) && !table.hasUserPrivileg(request, GrantedPrivileges.PRIVILEG_SELECT))
            {
                logCat.debug("pos3");
                out.println("Sorry, viewing data from table " + table.getName() + " is not granted for this session.");

                return SKIP_BODY;
            }

            logCat.debug("pos4");

            // part II/b - processing interceptors
            if ((table != null) && table.hasInterceptors())
            {
                try
                {
                    logCat.debug("pos5");
                    table.processInterceptors(DbEventInterceptor.PRE_SELECT, request, null, config, con);
                }
                catch (Exception sqle)
                {
                    logCat.debug("pos6");
                    out.println("Sorry, viewing data from table " + table.getName() + " would violate a condition.<BR><BR>" + sqle.getMessage());

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
            if (parentForm == null)
            {
                tagBuf.append("<form name=\"dbform\" action=\"");

                //Check if developer has overriden action
                if ((this.getAction() != null) && (this.getAction().trim().length() > 0))
                {
                    tagBuf.append(this.getAction());
                }
                else
                {
                    tagBuf.append(response.encodeURL(request.getContextPath() + "/servlet/control"));
                }

                tagBuf.append("\"");

                // 20020703-HKK: Check if developer has set onSubmit
                if ((this.getonSubmit() != null) && (this.getonSubmit().trim().length() > 0))
                {
                    tagBuf.append("onSubmit=\"");
                    tagBuf.append(getonSubmit());
                    tagBuf.append("\" ");
                }

                if (target != null)
                {
                    tagBuf.append(" target=\"");
                    tagBuf.append(target);
                    tagBuf.append("\"");
                }

                tagBuf.append(" method=\"post\"");

                if (_isMultipart)
                {
                    tagBuf.append(" enctype=\"multipart/form-data\"");
                }

                if (getJavascriptValidation().equals("true"))
                {
                    String validationFct = getFormValidatorName();

                    validationFct = Character.toUpperCase(validationFct.charAt(0)) + validationFct.substring(1, validationFct.length());
                    tagBuf.append(" onsubmit=\"return validate" + validationFct + "(this);\" ");
                }

                tagBuf.append(">");

                // supports RFC 1867 - multipart upload, if some database-fields represent filedata
                if (tableName == null)
                {
                    // if form is an emptyform -> we've fineshed yet - cancel all further activities!
                    out.println(tagBuf.toString());

                    return EVAL_BODY_TAG;
                }

                positionPathCore = "root";
            }
            else
            {
                // if sub-form, we dont write out html tags; this has been done already by a parent form
                this.isSubForm = true;
                positionPathCore = parentForm.getPositionPath();

                // If whereClause is not supplied by developer
                // determine the value(s) of the linked field(s)
                if ((this.getWhereClause() == null) || (this.getWhereClause().trim().length() == 0))
                {
                    initChildFieldValues();

                    if (childFieldValues == null)
                    {
                        return SKIP_BODY;
                    }
                }
            }

            // write out involved table
            tagBuf.append("<input type=\"hidden\" name=\"invtable\" value=\"" + tableId + "\">");

            // ---- Bradley's multiple connection stuff [fossato <fossato@pow2.com> 20021105] ----
            // write out the name of the involved dbconnection.
            tagBuf.append("<input type='hidden' name='invname_" + tableId + "' value='" + dbConnectionName + "'>");

            // ---- Bradley's multiple connection stuff end ---------------------------------------
            // write out the autoupdate-policy of this form
            tagBuf.append("<input type=\"hidden\" name=\"autoupdate_" + tableId + "\" value=\"" + autoUpdate + "\">");

            // write out the followup-default for this table
            tagBuf.append("<input type=\"hidden\" name=\"fu_" + tableId + "\" value=\"" + followUp + "\">");

            // write out the followupOnError-default for this table
            if ((getFollowUpOnError() != null) && (getFollowUpOnError().trim().length() > 0))
            {
                tagBuf.append("<input type=\"hidden\" name=\"fue_" + tableId + "\" value=\"" + getFollowUpOnError() + "\">");
            }

            // write out the formValidatorName
            if ((getFormValidatorName() != null) && (getFormValidatorName().trim().length() > 0))
            {
                tagBuf.append("<input type=\"hidden\" name=\"" + ValidatorConstants.FORM_VALIDATOR_NAME + "\" value=\"" + getFormValidatorName() + "\">");
            }

            tagBuf.append("<input type=\"hidden\" name=\"source\" value=\"");
            tagBuf.append(request.getRequestURI());

            if (request.getQueryString() != null)
            {
                tagBuf.append("?").append(request.getQueryString());
            }

            tagBuf.append("\">");

            // Allow to send action dynamicaly from javascript
            tagBuf.append("<input type=\"hidden\" name=\"customEvent\">");

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

            if (this.overrulingOrder != null)
            {
                // if developer provided orderBy - Attribute in <db:dbform> - tag
                orderConstraint = overrulingOrder;
                orderFields = overrulingOrderFields;
                useDefaultOrder = false;
                logCat.info("using OverrulingOrder");
            }
            else
            {
                // if developer provided orderBy - Attribute globally in dbforms-config.xml - tag
                FieldValue[] tmpOrderConstraint = table.getDefaultOrder();

                orderConstraint = new FieldValue[tmpOrderConstraint.length];

                for (int i = 0; i < tmpOrderConstraint.length; i++)
                {
                    orderConstraint[i] = (FieldValue) tmpOrderConstraint[i].clone();

                    // cloning is necessary to keep things thread-safe! (we manipulate some fields in this structure.)
                }

                orderFields = table.getDefaultOrderFields();
                logCat.info("using DefaultOrder");
            }

            // an orderBY - clause is a MUST. we can't query well without it.
            if (orderConstraint == null)
            {
                throw new IllegalArgumentException("OrderBy-Clause must be specified either in table-element in config.xml or in dbform-tag on jsp view");
            }

            // III/2:
            // is there a POSITION we are supposed to navigate to?
            // positions are key values: for example "2", oder "2~454"
            //String position = pageContext.getRequest().getParameter("pos_"+tableId);
            String firstPosition = ParseUtil.getParameter(request, "firstpos_" + tableId);
            String lastPosition = ParseUtil.getParameter(request, "lastpos_" + tableId);

            if (firstPosition == null)
            {
                firstPosition = lastPosition;
            }

            if (lastPosition == null)
            {
                lastPosition = firstPosition;
            }

            // if we are in a subform we must check if the fieldvalue-list provided in the
            // position strings is valid in the current state
            // it might be invalid if the position of the parent form has been changed (by a navigation event)
            // (=> the position-strings of childforms arent valid anymore)
            if ((childFieldValues != null) && (firstPosition != null))
            {
                if (!checkLinkage(childFieldValues, firstPosition))
                {
                    // checking one of the 2 strings is sufficient
                    // the position info is out of date. we dont use it.
                    firstPosition = null;
                    lastPosition = null;
                }
            }

            logCat.info("firstposition " + firstPosition);
            logCat.info("lastPosition " + lastPosition);

            FieldValue[] mergedFieldValues = null;

            if (childFieldValues == null)
            {
                mergedFieldValues = filterFieldValues;
            }
            else if (filterFieldValues == null)
            {
                mergedFieldValues = childFieldValues;
            }
            else
            {
                mergedFieldValues = new FieldValue[childFieldValues.length + filterFieldValues.length];
                System.arraycopy(childFieldValues, 0, mergedFieldValues, 0, childFieldValues.length);
                System.arraycopy(filterFieldValues, 0, mergedFieldValues, childFieldValues.length, filterFieldValues.length);
            }

            // if we just habe a search request we do not need any other constraints
            FieldValue[] searchFieldValues = initSearchFieldValues();

            if (searchFieldValues != null)
            {
                mergedFieldValues = searchFieldValues;
            }

            /*

            in the code above we examined lots of information which determinates  _which_ resultset
            gets retrieved and the way this operation will be done.

            which are the "parameters" we have (eventually!) retrieved
            ============================================================

            *)         WebEvent Object in request: if the jsp containing this tag was invoked by
            the controller, then there is a Event which has been processed (DatebasEvents)
            or which waits to be processed (NavigationEvents, including GotoEvent)

            *)         firstPos, lastPos: Strings containing key-fieldValues and indicating a line
            to go to if we have no other information. this may happen if a subform gets
            navigated. the parentForm is not involved in the operation but must be able
            to "navigate" to its new old position.
            [#checkme: risk of wrong interpreation if jsp calls jsp - compare source tags?]

            *)         mergedFieldValues: this is a cumulation of all rules which restrict the
            result set in any way. it is build of

            -         childFieldValues: restricting a set in a subform that all "childFields" in the
            resultset match their respective "parentFields" in main form. (for instance
            if customerID == 100, we only want to select orders from orders-table
            involving customerID 100)

            -        filterFieldValues: if a filter is applied to the resultset we always need
            to select the _filtered_ resultset

            -         searchFieldValues: if a search is performed we just want to show fields
            belonging to the search result (naturally ;=)

            *)         orderConstraint: this is a cumulation of rules for ordering (sorting)
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


            *)         count: this is a property of DbFormTag. Its relevance is that certain operations
            need to be performed differently if count==0, which means the form is an
            "endless form".

            */
            // III/3: fetching data (compare description above)
            // this code is still expermintal, pre alpha! We need to put this logic into
            // etter (more readable, maintainable) code  (evt. own method or class)!
            // is there a NAVIGATION event (like "nav to first row" or "nav to previous row", or "nav back 4 rows"?
            // if so...
            webEvent = (WebEvent) request.getAttribute("webEvent");

            // test: use BoundedNavEventFactoryImpl [Fossato <fossato@pow2.com>, 2001/11/08]
            NavEventFactory nef = BoundedNavEventFactoryImpl.instance();

            // if there comes no web event from controller, then we check if there is
            // a local event defined on the jsp
            // #fixme!
            // Interimistic solution
            // must be more flexible in final version
            if ((webEvent == null) && (localWebEvent != null))
            {
                if ("navFirst".equals(localWebEvent))
                {
                    logCat.debug("instantiating local we:" + localWebEvent);
                    webEvent = new NavFirstEvent(this.table, this.config);
                }
                else if ("navLast".equals(localWebEvent))
                {
                    logCat.debug("instantiating local we:" + localWebEvent);
                    webEvent = new NavLastEvent(this.table, this.config);
                }
                else if ("navPrev".equals(localWebEvent))
                {
                    logCat.debug("instantiating local we:" + localWebEvent);

                    //webEvent = new NavPrevEvent(this.table, this.config);
                    webEvent = nef.createNavPrevEvent(this.table, this.config);

                    // [Fossato <fossato@pow2.com>, 2001/11/08]
                }
                else if ("navNext".equals(localWebEvent))
                {
                    logCat.debug("instantiating local we:" + localWebEvent);

                    //webEvent = new NavNextEvent(this.table, this.config);
                    webEvent = nef.createNavNextEvent(this.table, this.config);

                    // [Fossato <fossato@pow2.com>, 2001/11/08]
                }
                else if ("navNew".equals(localWebEvent))
                {
                    logCat.debug("instantiating local we:" + localWebEvent);
                    webEvent = new NavNewEvent(this.table, this.config);
                }

                // Setted with localWebEvent attribute.
                if (webEvent != null)
                {
                    request.setAttribute("webEvent", webEvent);
                }
            }

            if ((webEvent != null) && webEvent instanceof NavigationEvent)
            {
                NavigationEvent navEvent = (NavigationEvent) webEvent;

                if ((navEvent != null) && (navEvent.getTableId() == tableId))
                {
                    logCat.info("§§§ NAV/I §§§");
                    logCat.info("about to process nav event:" + navEvent.getClass().getName());
                    resultSetVector = navEvent.processEvent(mergedFieldValues, orderConstraint, count, firstPosition, lastPosition, con);

                    if (navEvent instanceof NavNewEvent)
                    {
                        setFooterReached(true);
                    }

                    // if we have no navigation event for this table
                }
                else
                {
                    logCat.info("§§§ NAV/II §§§");

                    if (firstPosition != null)
                    {
                        table.fillWithValues(orderConstraint, firstPosition);
                    }

                    // if there is a navigation event but not for this table,
                    // then just navigate to a position (if it exists) or just select all data
                    // (if no pos or if endless form)

                    /**
                     * Grunikiewicz.philip@hydro.qc.ca
                     * 2001-11-28
                     *
                     * If the whereClause attribute has been specified, ignore all and
                     * apply the whereClause directly in the query.
                     *
                     */
                    if ((this.getWhereClause() != null) && (this.getWhereClause().trim().length() > 0))
                    {
                        logCat.info("Free form Select about to be executed");
                        resultSetVector = table.doFreeFormSelect(table.getFields(), this.getWhereClause(), this.getTableList(), count, con);
                    }
                    else
                    {
                        // PG - Check if developer specified to bypass Navigation infrastructure
                        resultSetVector = table.doConstrainedSelect(table.getFields(), mergedFieldValues, orderConstraint, ((firstPosition == null) || (count == 0) || ("true".equals(getBypassNavigation()))) ? FieldValue.COMPARE_NONE : FieldValue.COMPARE_INCLUSIVE, count, con);
                    }
                }
            }

            // or is there a possibiliy of a GOTO EVENT?
            // we need to parse request using the given goto prefix
            else if ((gotoPrefix != null) && (gotoPrefix.length() > 0))
            {
                logCat.info("§§§ NAV GOTO §§§");

                Vector v = ParseUtil.getParametersStartingWith(request, gotoPrefix);

                gotoHt = new Hashtable();

                for (int i = 0; i < v.size(); i++)
                {
                    String paramName = (String) v.elementAt(i);
                    String fieldName = paramName.substring(gotoPrefix.length());

                    logCat.debug("fieldName=" + fieldName);

                    String fieldValue = ParseUtil.getParameter(request, paramName);

                    logCat.debug("fieldValue=" + fieldValue);

                    if ((fieldName != null) && (fieldValue != null))
                    {
                        gotoHt.put(fieldName, fieldValue);
                    }
                }

                if (gotoHt.size() > 0)
                {
                    String positionString = table.getPositionStringFromFieldAndValueHt(gotoHt);
                    GotoEvent ge = new GotoEvent(positionString, table);

                    resultSetVector = ge.processEvent(mergedFieldValues, orderConstraint, count, firstPosition, lastPosition, con);
                }
            }

            // we got a goto-hashtable directly from the attribute
            else if (gotoHt != null)
            {
                if (gotoHt.size() > 0)
                {
                    String positionString = table.getPositionStringFromFieldAndValueHt(gotoHt);
                    GotoEvent ge = new GotoEvent(positionString, table);

                    resultSetVector = ge.processEvent(mergedFieldValues, orderConstraint, count, firstPosition, lastPosition, con);
                }
            }

            //
            // kindof "else" branch
            //
            if (((webEvent == null) || !(webEvent instanceof NavigationEvent)) && ((gotoHt == null) || (gotoHt.size() == 0)))
            {
                if ((firstPosition != null) && ((overrulingOrder == null) || webEvent instanceof DatabaseEvent || webEvent instanceof NoopEvent || webEvent instanceof EmptyEvent || webEvent instanceof ReloadEvent))
                {
                    // we have someting to look for, and we have _no_ change of search criteria #fixme - explain better

                    /**
                     * Grunikiewicz.philip@hydro.qc.ca
                     * 2001-11-28
                     *
                     * If the whereClause attribute has been specified, ignore all and
                     * apply the whereClause directly in the query.
                     *
                     */
                    if ((this.getWhereClause() != null) && (this.getWhereClause().trim().length() > 0))
                    {
                        logCat.info("Free form Select about to be executed");
                        resultSetVector = table.doFreeFormSelect(table.getFields(), this.getWhereClause(), this.getTableList(), count, con);
                    }
                    else
                    {
                        logCat.info("§§§ B/I §§§");
                        table.fillWithValues(orderConstraint, firstPosition);
                        resultSetVector = table.doConstrainedSelect(table.getFields(), mergedFieldValues, orderConstraint, ((count == 0) || ("true".equals(getBypassNavigation()))) ? FieldValue.COMPARE_NONE : FieldValue.COMPARE_INCLUSIVE, count, con);
                    }

                    // in endless forms we never need to to compares
                    logCat.info("we had someting to look for:" + firstPosition);
                }
                else
                {
                    // we have no navigation object and no position we are supposed to go. so let's go to the first row.
                    logCat.info("§§§ B/II §§§");

                    Vector errors = (Vector) request.getAttribute("errors");

                    if ((count != 0) && (webEvent != null) && webEvent instanceof InsertEvent && (errors != null) && (errors.size() > 0))
                    {
                        logCat.info("B/II/1 redirecting to insert mode after faild db operation");

                        //resultSetVector = new ResultSetVector();
                        //resultSetVector.setPointer(0);
                        resultSetVector = null;
                        setFooterReached(true);
                    }
                    else
                    {
                        //if ((this.getWhereClause() != null) && (this.getWhereClause().trim().length() > 0))
                        if (!Util.isNull(getWhereClause()))
                        {
                            logCat.info("Free form Select about to be executed");
                            //logCat.info("Free form Select: tableList is null ? [" +  Util.isNull(getTableList()) + "]");  // DEBUG ONLY (fossato)

                            // if tableList is null, use the tableName attribute (fossato@pow2.com [2002.11.16])
                            String myTables = tableList;
                            if (Util.isNull(myTables))
                                myTables = tableName;

                            //resultSetVector = table.doFreeFormSelect(table.getFields(), this.getWhereClause(), this.getTableList(), count, con);
                            resultSetVector = table.doFreeFormSelect(table.getFields(), whereClause, myTables, count, con);
                        }
                        else
                        {
                            logCat.info("§§§ B/II/2 §§§");
                            resultSetVector = table.doConstrainedSelect(table.getFields(), mergedFieldValues, orderConstraint, ((firstPosition == null) || ("true".equals(getBypassNavigation()))) ? FieldValue.COMPARE_NONE : FieldValue.COMPARE_INCLUSIVE, count, con);
                        }
                    }
                }
            }

            // *** DONE! We have now the underlying data, and this data is accessible to all sub-elements (labels, textFields, etc. of this form ***
            // *************************************************************
            //  Part IV - Again, some WebEvent infrastructural stuff:
            //  write out data indicating the position we have
            //  navigated to withing the rowset
            // *************************************************************
            // we process interceptor again (post-select)
            // #checkme: is the overhead of a POST_SELECT interceptor necessary or a luxury? => use cases!
            if ((table != null) && table.hasInterceptors())
            {
                try
                {
                    // process the interceptors associated to this table
                    table.processInterceptors(DbEventInterceptor.POST_SELECT, request, null, config, con);
                }
                catch (SQLException sqle)
                {
                    // PG = 2001-12-04
                    // No need to add extra comments, just re-throw exceptions as SqlExceptions
                    throw new SQLException(sqle.getMessage());
                }
                catch (MultipleValidationException mve)
                {
                    // PG, 2001-12-14
                    // Support for multiple error messages in one interceptor
                    throw new MultipleValidationException(mve.getMessages());
                }
            }

            // End of interceptor processing
            // determinate new position-strings (== value of the first and the last row of the current view)
            if (resultSetVector != null)
            {
                resultSetVector.setPointer(0);
                firstPosition = table.getPositionString(resultSetVector);
                resultSetVector.setPointer(resultSetVector.size() - 1);
                lastPosition = table.getPositionString(resultSetVector);
                resultSetVector.setPointer(0);
            }

            if (!footerReached)
            {
                // if not in insert mode
                if (firstPosition != null)
                {
                    tagBuf.append("<input type=\"hidden\" name=\"firstpos_" + tableId + "\" value=\"" + firstPosition + "\">");
                }

                if (lastPosition != null)
                {
                    tagBuf.append("<input type=\"hidden\" name=\"lastpos_" + tableId + "\" value=\"" + lastPosition + "\">");
                }
            }

            // construct TEI variables for access from JSP
            // # jp 27-06-2001: replacing "." by "_", so that SCHEMATA can be used
            pageContext.setAttribute("searchFieldNames_" + tableName.replace('.', '_'), table.getNamesHashtable("search"));
            pageContext.setAttribute("searchFieldModeNames_" + tableName.replace('.', '_'), table.getNamesHashtable("searchmode"));
            pageContext.setAttribute("searchFieldAlgorithmNames_" + tableName.replace('.', '_'), table.getNamesHashtable("searchalgo"));

            // #fixme:
            // this is a weired crazy workaround [this code is also used in DbBodyTag!!]
            // why?
            // #fixme: explaination! -> initBody, spec, jsp container synchronizing variables, etc.
            if (!ResultSetVector.isEmptyOrNull(resultSetVector))
            {
                pageContext.setAttribute("rsv_" + tableName.replace('.', '_'), resultSetVector);
                pageContext.setAttribute("currentRow_" + tableName.replace('.', '_'), resultSetVector.getCurrentRowAsHashtable());
                pageContext.setAttribute("position_" + tableName.replace('.', '_'), table.getPositionString(resultSetVector));
            }

            out.println(tagBuf.toString());
        }
        catch (IOException e)
        {
            return SKIP_BODY;
        }
        catch (SQLException ne)
        {
            // now get also the stack trace (fossato@pow2.com [2002.11.09])
            logCat.error("exception", ne);

            // all the sql exceptions ! (fossato@pow2.com [2002.11.09])
            int i = 0;

            while ((ne = ne.getNextException()) != null)
                logCat.error("nested SQLException (" + (i++) + ")", ne);

            return SKIP_BODY;
        }
        catch (MultipleValidationException mve)
        {
            logCat.error(mve);

            return SKIP_BODY;
        }

        return EVAL_BODY_TAG;
    }


    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     * @throws  JspException DOCUMENT ME!
     */
    public int doAfterBody() throws JspException
    {
        if ((resultSetVector == null) || (resultSetVector.size() == 0))
        {
            return SKIP_BODY;
        }

        // rsv may be null in empty-forms (where not tableName attribute is provided)
        if (!footerReached)
        {
            return EVAL_BODY_TAG;
        }
        else
        {
            return SKIP_BODY;
        }
    }


    /**
     *   D O    E N D T A G
     *
     * @return  Description of the Return Value
     * @exception  JspException Description of the Exception
     */
    public int doEndTag() throws JspException
    {
        JspWriter jspOut = pageContext.getOut();

        // avoid to call getOut each time (Demeter law)
        try
        {
            if (bodyContent != null)
            {
                bodyContent.writeOut(bodyContent.getEnclosingWriter());
            }

            logCat.debug("pageContext.getOut()=" + pageContext.getOut());
            logCat.debug("childElementOutput=" + childElementOutput);

            // hidden fields and other stuff coming from child elements get written out
            if (childElementOutput != null)
            {
                jspOut.println(childElementOutput.toString());
            }

            if (parentForm == null)
            {
                jspOut.println("</form>");
            }

            /** Generate Javascript validation methods & calls */
            if ((getFormValidatorName() != null) && (getFormValidatorName().length() > 0) && (getJavascriptValidation() != null) && getJavascriptValidation().equals("true"))
            {
                jspOut.println(generateJavascriptValidation());
            }

            /**
             *  Generate Javascript array of fields.
             *  To help developper to work with DbForms fields name.
             *
             *  Ex: champ1 => f_0_1@root_4
             */
            if ((getJavascriptFieldsArray() != null) && getJavascriptFieldsArray().equals("true"))
            {
                jspOut.println(generateJavascriptFieldsArray());
            }

            /**  Write generic Javascript functions created from childs tag */
            if (javascriptDistinctFunctions.size() > 0)
            {
                jspOut.println("\n<SCRIPT language=\"javascript\">\n");

                Enumeration enum = javascriptDistinctFunctions.keys();

                while (enum.hasMoreElements())
                {
                    String aKey = (String) enum.nextElement();
                    StringBuffer sbFonction = (StringBuffer) javascriptDistinctFunctions.get(aKey);

                    jspOut.println(sbFonction);
                }

                jspOut.println("\n</SCRIPT>\n");
            }
        }
        catch (IOException ioe)
        {
            logCat.error(ioe);
        }

        logCat.info("end reached of " + tableName);

        return EVAL_PAGE;
    }


    /**
     * DOCUMENT ME!
     *
     * @param  p DOCUMENT ME!
     */
    public void setParent(Tag p)
    {
        super.setParent(p);
        this.parentForm = (DbFormTag) findAncestorWithClass(this, DbFormTag.class);
    }


    //------------------------ business, helper & utility methods --------------------------------

    /**
     * initialise datastructures containing informations about how table should be orderd
     * the information is specified in the JSP this tags lives in. this declaration OVERRULES
     * other default declarations eventually done in XML config!
     * (compara Table.java !)
     *
     * @param  request Description of the Parameter
     */
    private void initOverrulingOrder(HttpServletRequest request)
    {
        HttpServletRequest localRequest = request;

        // if page A links to page B (via a gotoButton, for instance) then we do not
        // want  A's order constraints get applied to B
        if (localRequest != null)
        {
            String refSource = localRequest.getRequestURI();

            if (request.getQueryString() != null)
            {
                refSource += ("?" + request.getQueryString());
            }

            String sourceTag = ParseUtil.getParameter(localRequest, "source");

            logCat.info("!comparing page " + refSource + " TO " + sourceTag);

            /*
            if(sourceTag==null || !sourceTag.equals(refSource)) {
            localRequest = null;
            }*/
            if ((sourceTag != null) && !refSource.equals(sourceTag))
            {
                localRequest = null;
            }
        }

        logCat.debug("orderBy=" + orderBy);
        logCat.debug("localRequest=" + localRequest);

        // if we have neither an orderby clause nor a request we may use then we cant create orderconstraint
        if ((orderBy == null) && (localRequest == null))
        {
            return;
        }

        // otherwise we can:
        overrulingOrder = table.createOrderFieldValues(orderBy, localRequest, false);
        overrulingOrderFields = new Vector();

        if (overrulingOrder != null)
        {
            for (int i = 0; i < overrulingOrder.length; i++)
                overrulingOrderFields.addElement(overrulingOrder[i].getField());
        }
    }


    /** DOCUMENT ME! */
    public void initChildFieldValues()
    {
        // if parent form has no data, we can not render a subform!
        if (ResultSetVector.isEmptyOrNull(parentForm.getResultSetVector()))
        {
            childFieldValues = null;

            // childFieldValues remains null
            return;
        }

        // 1 to n fields may be mapped
        Vector childFieldNames = ParseUtil.splitString(childField, ",;~");
        Vector parentFieldNames = ParseUtil.splitString(parentField, ",;~");

        // do some basic checks
        // deeper checks like Datatyp-compatibility,etc not done yet
        int len = childFieldNames.size();

        if ((len == 0) || (len != parentFieldNames.size()))
        {
            return;
        }

        // get parent-table
        // we need it in order to fetch the actual values of the mapped fields
        // for example if we mapped child::cust_id to parent::id, then we need
        // to know the current value of parent::id in order to do a contrained select
        // for our subform
        Table parentTable = parentForm.getTable();

        childFieldValues = new FieldValue[len];

        for (int i = 0; i < len; i++)
        {
            String parentFieldName = (String) parentFieldNames.elementAt(i);
            Field parentField = parentTable.getFieldByName(parentFieldName);
            String childFieldName = (String) childFieldNames.elementAt(i);
            Field childField = this.table.getFieldByName(childFieldName);
            String currentParentFieldValue = null;

            if (!ResultSetVector.isEmptyOrNull(parentForm.getResultSetVector()))
            {
                currentParentFieldValue = parentForm.getResultSetVector().getField(parentField.getId());
            }

            childFieldValues[i] = new FieldValue(childField, currentParentFieldValue, true);
        }
    }


    /** DOCUMENT ME! */
    public void initFilterFieldValues()
    {
        // 1 to n fields may be mapped
        Vector keyValPairs = ParseUtil.splitString(filter, ",;");

        // ~ no longer used as separator!
        int len = keyValPairs.size();

        filterFieldValues = new FieldValue[len];

        for (int i = 0; i < len; i++)
        {
            int operator = 0;
            boolean isLogicalOR = false;
            int jump = 1;
            String aKeyValPair = (String) keyValPairs.elementAt(i);

            // i.e "id=2"
            logCat.debug("initFilterFieldValues: aKeyValPair = " + aKeyValPair);

            // Following code could be optimized, however I did not want to make too many changes...
            int n;

            // Check for Not Equal
            if ((n = aKeyValPair.indexOf("<>")) != -1)
            {
                // Not Equal found! - Store the operation for use later on
                operator = FieldValue.FILTER_NOT_EQUAL;
                jump = 2;
            }
            else if ((n = aKeyValPair.indexOf(">=")) != -1)
            {
                // Check for GreaterThanEqual
                // GreaterThenEqual found! - Store the operation for use later on
                operator = FieldValue.FILTER_GREATER_THEN_EQUAL;
                jump = 2;
            }
            else if ((n = aKeyValPair.indexOf('>')) != -1)
            {
                // Check for GreaterThan
                // GreaterThen found! - Store the operation for use later on
                operator = FieldValue.FILTER_GREATER_THEN;
            }
            else if ((n = aKeyValPair.indexOf("<=")) != -1)
            {
                // Check for SmallerThenEqual
                // SmallerThenEqual found! - Store the operation for use later on
                operator = FieldValue.FILTER_SMALLER_THEN_EQUAL;
                jump = 2;
            }
            else if ((n = aKeyValPair.indexOf('<')) != -1)
            {
                // Check for SmallerThen
                // SmallerThen found! - Store the operation for use later on
                operator = FieldValue.FILTER_SMALLER_THEN;
            }
            else if ((n = aKeyValPair.indexOf('=')) != -1)
            {
                // Check for equal
                // Equal found! - Store the operator for use later on
                operator = FieldValue.FILTER_EQUAL;
            }
            else if ((n = aKeyValPair.indexOf('~')) != -1)
            {
                // Check for LIKE
                // LIKE found! - Store the operator for use later on
                operator = FieldValue.FILTER_LIKE;
            }
            else if ((n = aKeyValPair.toUpperCase().indexOf("NOTISNULL")) != -1)
            {
                // Check for not is null
                // LIKE found! - Store the operator for use later on
                jump = 9;
                operator = FieldValue.FILTER_NOT_NULL;
            }
            else if ((n = aKeyValPair.toUpperCase().indexOf("ISNULL")) != -1)
            {
                // Check for null
                // LIKE found! - Store the operator for use later on
                jump = 6;
                operator = FieldValue.FILTER_NULL;
            }

            //  PG - At this point, I have set my operator and I should have a valid index.
            //	Note that the original code did not handle the posibility of not finding an index
            //	(value = -1)...
            String fieldName = aKeyValPair.substring(0, n).trim();

            // i.e "id"
            logCat.debug("Filter field=" + fieldName);

            if (fieldName.charAt(0) == '|')
            {
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
            filterFieldValues[i] = new FieldValue(filterField, value, false, operator, isLogicalOR);
            logCat.debug("and fv is =" + filterFieldValues[i].toString());
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public FieldValue[] initSearchFieldValues()
    {
        FieldValue[] fieldValues;
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        Vector searchFieldNames = ParseUtil.getParametersStartingWith(request, "search_" + this.tableId);

        if ((searchFieldNames == null) || (searchFieldNames.size() == 0))
        {
            return null;
        }

        Vector mode_and = new Vector();
        Vector mode_or = new Vector();

        for (int i = 0; i < searchFieldNames.size(); i++)
        {
            String searchFieldName = (String) searchFieldNames.elementAt(i);

            //. i.e search_1_12
            // 20020927-HKK-TODO: Whats when there is more then onesearch field whith the same name?
            //                    Maybe we should parse all of them ....
            String aSearchFieldValue = ParseUtil.getParameter(request, searchFieldName);

            // ie. search_1_12 is mapped to "john"
            if ((aSearchFieldValue != null) && (aSearchFieldValue.trim().length() > 0))
            {
                int firstUnderscore = searchFieldName.indexOf('_');
                int secondUnderscore = searchFieldName.indexOf('_', firstUnderscore + 1);
                int tableId = Integer.parseInt(searchFieldName.substring(firstUnderscore + 1, secondUnderscore));

                // is equal to tableid, off course
                int fieldId = Integer.parseInt(searchFieldName.substring(secondUnderscore + 1));
                Field f = table.getField(fieldId);
                String aSearchMode = ParseUtil.getParameter(request, "searchmode_" + tableId + "_" + fieldId);
                int mode = ("and".equals(aSearchMode)) ? DbBaseHandlerTag.SEARCHMODE_AND : DbBaseHandlerTag.SEARCHMODE_OR;
                String aSearchAlgorithm = ParseUtil.getParameter(request, "searchalgo_" + tableId + "_" + fieldId);

                // 20021019-HKK: new searching
                aSearchFieldValue = aSearchFieldValue.trim();

                // Check for operator
                int algorithm = FieldValue.SEARCH_ALGO_SHARP;
                int operator = FieldValue.FILTER_EQUAL;

                if (aSearchAlgorithm.startsWith("sharpLT"))
                {
                    operator = FieldValue.FILTER_SMALLER_THEN;
                }
                else if (aSearchAlgorithm.startsWith("sharpLE"))
                {
                    operator = FieldValue.FILTER_SMALLER_THEN_EQUAL;
                }
                else if (aSearchAlgorithm.startsWith("sharpGT"))
                {
                    operator = FieldValue.FILTER_GREATER_THEN;
                }
                else if (aSearchAlgorithm.startsWith("sharpGE"))
                {
                    operator = FieldValue.FILTER_GREATER_THEN_EQUAL;
                }
                else if (aSearchAlgorithm.startsWith("sharpNE"))
                {
                    operator = FieldValue.FILTER_NOT_EQUAL;
                }
                else if (aSearchAlgorithm.startsWith("sharpNULL"))
                {
                    operator = FieldValue.FILTER_NULL;
                }
                else if (aSearchAlgorithm.startsWith("sharpNOTNULL"))
                {
                    operator = FieldValue.FILTER_NOT_NULL;
                }
                else if (aSearchAlgorithm.startsWith("weakStartEnd"))
                {
                    algorithm = FieldValue.SEARCH_ALGO_WEAK_END;
                    operator = FieldValue.FILTER_LIKE;
                }
                else if (aSearchAlgorithm.startsWith("weakStart"))
                {
                    algorithm = FieldValue.SEARCH_ALGO_WEAK_START;
                    operator = FieldValue.FILTER_LIKE;
                }
                else if (aSearchAlgorithm.startsWith("weakEnd"))
                {
                    algorithm = FieldValue.SEARCH_ALGO_WEAK_END;
                    operator = FieldValue.FILTER_LIKE;
                }
                else if (aSearchAlgorithm.startsWith("weak"))
                {
                    algorithm = FieldValue.SEARCH_ALGO_WEAK;
                    operator = FieldValue.FILTER_LIKE;
                }

                if ((aSearchAlgorithm == null) || (aSearchAlgorithm.toLowerCase().indexOf("extended") == -1))
                {
                    // Extended not found, only append field
                    FieldValue fv = new FieldValue(f, aSearchFieldValue, true, operator);

                    fv.setSearchMode(mode);
                    fv.setSearchAlgorithm(algorithm);

                    if (mode == DbBaseHandlerTag.SEARCHMODE_AND)
                    {
                        mode_and.addElement(fv);
                    }
                    else
                    {
                        mode_or.addElement(fv);
                    }
                }
                else if (aSearchFieldValue.indexOf("-") != -1)
                {
                    // delimiter found in SearchFieldValue, create something like
                    algorithm = FieldValue.SEARCH_ALGO_EXTENDED;

                    StringTokenizer st = new StringTokenizer(" " + aSearchFieldValue + " ", "-");
                    int tokenCounter = 0;

                    while (st.hasMoreTokens())
                    {
                        aSearchFieldValue = st.nextToken().trim();
                        tokenCounter++;

                        if (aSearchFieldValue.length() > 0)
                        {
                            switch (tokenCounter)
                            {
                                case 1:
                                    operator = FieldValue.FILTER_GREATER_THEN_EQUAL;

                                    break;

                                case 2:
                                    operator = FieldValue.FILTER_SMALLER_THEN_EQUAL;

                                    break;

                                default:
                                    operator = -1;

                                    break;
                            }

                            if (operator != -1)
                            {
                                FieldValue fv = new FieldValue(f, aSearchFieldValue, true, operator);

                                fv.setSearchMode(mode);
                                fv.setSearchAlgorithm(algorithm);

                                if (mode == DbBaseHandlerTag.SEARCHMODE_AND)
                                {
                                    mode_and.addElement(fv);
                                }
                                else
                                {
                                    mode_or.addElement(fv);
                                }
                            }
                        }
                    }
                }
                else
                {
                    // parse special chars in SearchFieldValue
                    int jump = 0;

                    // Check for Not Equal
                    if (aSearchFieldValue.startsWith("<>"))
                    {
                        algorithm = FieldValue.SEARCH_ALGO_EXTENDED;
                        operator = FieldValue.FILTER_NOT_EQUAL;
                        jump = 2;

                        // Check for not equal
                    }
                    else if (aSearchFieldValue.startsWith("!="))
                    {
                        // GreaterThenEqual found! - Store the operation for use later on
                        algorithm = FieldValue.SEARCH_ALGO_EXTENDED;
                        operator = FieldValue.FILTER_NOT_EQUAL;
                        jump = 2;

                        // Check for GreaterThanEqual
                    }
                    else if (aSearchFieldValue.startsWith(">="))
                    {
                        // GreaterThenEqual found! - Store the operation for use later on
                        algorithm = FieldValue.SEARCH_ALGO_EXTENDED;
                        operator = FieldValue.FILTER_GREATER_THEN_EQUAL;
                        jump = 2;

                        // Check for GreaterThan
                    }
                    else if (aSearchFieldValue.startsWith(">"))
                    {
                        // GreaterThen found! - Store the operation for use later on
                        algorithm = FieldValue.SEARCH_ALGO_EXTENDED;
                        operator = FieldValue.FILTER_GREATER_THEN;

                        // Check for SmallerThenEqual
                    }
                    else if (aSearchFieldValue.startsWith("<="))
                    {
                        // SmallerThenEqual found! - Store the operation for use later on
                        algorithm = FieldValue.SEARCH_ALGO_EXTENDED;
                        operator = FieldValue.FILTER_SMALLER_THEN_EQUAL;
                        jump = 2;

                        // Check for SmallerThen
                    }
                    else if (aSearchFieldValue.startsWith("<"))
                    {
                        // SmallerThen found! - Store the operation for use later on
                        algorithm = FieldValue.SEARCH_ALGO_EXTENDED;
                        operator = FieldValue.FILTER_SMALLER_THEN;
                        jump = 1;

                        // Check for equal
                    }
                    else if (aSearchFieldValue.startsWith("="))
                    {
                        // Equal found! - Store the operator for use later on
                        algorithm = FieldValue.SEARCH_ALGO_EXTENDED;
                        operator = FieldValue.FILTER_EQUAL;
                        jump = 1;
                    }
                    else if (aSearchFieldValue.startsWith("[NULL]"))
                    {
                        algorithm = FieldValue.SEARCH_ALGO_EXTENDED;
                        operator = FieldValue.FILTER_NULL;
                        jump = 0;
                    }
                    else if (aSearchFieldValue.startsWith("[!NULL]"))
                    {
                        algorithm = FieldValue.SEARCH_ALGO_EXTENDED;
                        operator = FieldValue.FILTER_NOT_NULL;
                        jump = 0;
                    }

                    if (jump > 0)
                    {
                        aSearchFieldValue = aSearchFieldValue.substring(jump).trim();
                    }

                    FieldValue fv = new FieldValue(f, aSearchFieldValue, true, operator);

                    fv.setSearchMode(mode);
                    fv.setSearchAlgorithm(algorithm);

                    if (mode == DbBaseHandlerTag.SEARCHMODE_AND)
                    {
                        mode_and.addElement(fv);
                    }
                    else
                    {
                        mode_or.addElement(fv);
                    }
                }
            }
        }

        int andBagSize = mode_and.size();
        int orBagSize = mode_or.size();
        int criteriaFieldCount = andBagSize + orBagSize;

        logCat.info("criteriaFieldCount=" + criteriaFieldCount);

        if (criteriaFieldCount == 0)
        {
            return null;
        }

        // now we construct the fieldValues array
        // we ensure that the searchmodes are not mixed up
        fieldValues = new FieldValue[criteriaFieldCount];

        int i = 0;

        for (i = 0; i < andBagSize; i++)
        {
            fieldValues[i] = (FieldValue) mode_and.elementAt(i);
        }

        for (int j = 0; j < orBagSize; j++)
        {
            fieldValues[j + i] = (FieldValue) mode_or.elementAt(j);
        }

        return fieldValues;
    }


    /**
     * This method is only used if this class is instantiated as sub-form (== embedded in another
     * form's body tag)
     *
     * this method gets called by input-tags like "DbTextFieldTag" and others. they signalize that
     * _they_ will generate the tag for the controller, not this form.
     * see produceLinkedTags()
     *
     * @param  f Description of the Parameter
     */
    public void strikeOut(Field f)
    {
        // childFieldValues may be null, if we have
        // a free form select using attribute whereClause
        if (childFieldValues != null)
        {
            for (int i = 0; i < childFieldValues.length; i++)
            {
                if ((f == childFieldValues[i].getField()) && childFieldValues[i].getRenderHiddenHtmlTag())
                {
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
    public String produceLinkedTags()
    {
        StringBuffer buf = new StringBuffer();

        // childFieldValues may be null, if we have
        // a free form select using attribute whereClause
        if (childFieldValues != null)
        {
            for (int i = 0; i < childFieldValues.length; i++)
            {
                if (childFieldValues[i].getRenderHiddenHtmlTag())
                {
                    buf.append("<input type=\"hidden\" name=\"f_");
                    buf.append(tableId);
                    buf.append("_");

                    //buf.append(footerReached ? "ins"+frozenCumulatedCount : new Integer(frozenCumulatedCount).toString());
                    buf.append("ins");

                    if (positionPathCore != null)
                    {
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
    }


    /**
     * if we are in a subform we must check if the fieldvalue-list provided in the
     * position strings is valid in the current state
     * it might be invalid if the position of the parent form has been changed (by a navigation event)
     * (=> the position-strings of childforms arent valid anymore)
     *
     * @param  childFieldValues Description of the Parameter
     * @param  aPosition Description of the Parameter
     * @return  Description of the Return Value
     */
    private boolean checkLinkage(FieldValue[] childFieldValues, String aPosition)
    {
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
        for (int i = 0; i < childFieldValues.length; i++)
        {
            String actualValue = childFieldValues[i].getFieldValue();

            logCat.debug("actualValue=" + actualValue);

            Field f = childFieldValues[i].getField();

            logCat.debug("f.getName=" + f.getName());
            logCat.debug("f.getId=" + f.getId());

            FieldValue aFieldValue = (FieldValue) ht.get(new Integer(f.getId()));

            if (aFieldValue == null)
            {
                throw new IllegalArgumentException("ERROR: Make sure that field " + f.getName() + " is a KEY of the table " + table.getName() + "! Otherwise you can not use it as PARENT/CHILD LINK argument!");
            }

            String valueInPos = aFieldValue.getFieldValue();

            logCat.info("comparing " + actualValue + " TO " + valueInPos);

            if (!actualValue.trim().equals(valueInPos.trim()))
            {
                return false;
            }
        }

        return true;
    }

    private java.lang.String redisplayFieldsOnError = "false";

    /**
     * grunikiewicz.philip@hydro.qc.ca
     * Creation date: (2001-05-31 13:11:00)
     *
     * @return  java.lang.String
     */
    public java.lang.String getRedisplayFieldsOnError()
    {
        return redisplayFieldsOnError;
    }


    /**
     * DOCUMENT ME!
     *
     * @param  newRedisplayFieldsOnError DOCUMENT ME!
     */
    public void setRedisplayFieldsOnError(java.lang.String newRedisplayFieldsOnError)
    {
        redisplayFieldsOnError = newRedisplayFieldsOnError;
    }

    private java.lang.String bypassNavigation = "false";

    /**
     * grunikiewicz.philip@hydro.qc.ca
     * Creation date: (2001-08-17 10:25:56)
     *
     * @return  java.lang.String
     */
    public java.lang.String getBypassNavigation()
    {
        return bypassNavigation;
    }


    /**
     * DOCUMENT ME!
     *
     * @param  newBypassNavigation DOCUMENT ME!
     */
    public void setBypassNavigation(java.lang.String newBypassNavigation)
    {
        bypassNavigation = newBypassNavigation;
    }

    // 20020703-HKK: Added onSubmit
    private java.lang.String onSubmit;

    /**
     * Insert the method's description here.
     * Creation date: (2001-10-22 18:17:25)
     *
     * @return  java.lang.String
     */
    public java.lang.String getonSubmit()
    {
        return onSubmit;
    }


    /**
     * DOCUMENT ME!
     *
     * @param  newonSubmit DOCUMENT ME!
     */
    public void setonSubmit(java.lang.String newonSubmit)
    {
        onSubmit = newonSubmit;
    }

    private java.lang.String action;

    /**
     * Insert the method's description here.
     * Creation date: (2001-10-22 18:17:25)
     *
     * @return  java.lang.String
     */
    public java.lang.String getAction()
    {
        return action;
    }


    /**
     * DOCUMENT ME!
     *
     * @param  newAction DOCUMENT ME!
     */
    public void setAction(java.lang.String newAction)
    {
        action = newAction;
    }


    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getTableList()
    {
        return tableList;
    }


    /**
     * DOCUMENT ME!
     *
     * @param  tableList DOCUMENT ME!
     */
    public void setTableList(String tableList)
    {
        this.tableList = tableList;
    }


    /**
     * Gets the followUpOnError
     *
     * @return  Returns a String
     */
    public String getFollowUpOnError()
    {
        return followUpOnError;
    }


    /**
     * Sets the followUpOnError
     *
     * @param  followUpOnError The followUpOnError to set
     */
    public void setFollowUpOnError(String followUpOnError)
    {
        this.followUpOnError = followUpOnError;
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
    private StringBuffer generateJavascriptFieldsArray()
    {
        // This section looks hard to understand, but to avoid using
        // synchronized object like keySet ... don't want to alter
        // childFieldNames hashtable.
        // We use different step of enumeration.
        StringBuffer result = new StringBuffer();
        String key = null;
        String val = null;
        String tmp = null;
        String values = "";

        result.append("<SCRIPT language=\"javascript\">\n");
        result.append("<!-- \n\n");
        result.append("    var dbFormFields = new Array();\n");

        Hashtable fields = new Hashtable();
        Enumeration enum = childFieldNames.keys();

        //
        // Loop in each keys "f_0_0@root_2" and create hashtable of unique fieldnames
        //
        while (enum.hasMoreElements())
        {
            key = (String) enum.nextElement();
            val = (String) childFieldNames.get(key);
            values = "";

            if (fields.containsKey(val))
            {
                values = (String) fields.get(val);
            }

            fields.put(val, values + ";" + key);
        }

        enum = fields.keys();

        //
        // Loop for each fieldname and generate text for javascript Array
        //
        // Ex: dbFormFields["DESCRIPTIONDEMANDE"] = new Array("f_0_0@root_4", "f_0_1@root_4", "f_0_insroot_4");
        //
        while (enum.hasMoreElements())
        {
            key = (String) enum.nextElement();
            val = (String) fields.get(key);
            result.append("    dbFormFields[\"").append(key).append("\"] = new Array(");

            // Sort the delimited string and return an ArrayList of it.
            ArrayList arrValues = sortFields(val);

            if (arrValues.size() == 1)
            {
                result.append("\"").append((String) arrValues.get(0)).append("\"");
            }
            else
            {
                for (int i = 0; i <= (arrValues.size() - 1); i++)
                {
                    result.append("\"").append((String) arrValues.get(i)).append("\"");

                    if (i != (arrValues.size() - 1))
                    {
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

        return result;
    }


    /**
     * Generate  the Javascript of Validation fields
     *
     * @return  Description of the Return Value
     */
    private StringBuffer generateJavascriptValidation()
    {
        ValidatorResources vr = (ValidatorResources) pageContext.getServletContext().getAttribute(ValidatorConstants.VALIDATOR);
        DbFormsErrors errors = (DbFormsErrors) pageContext.getServletContext().getAttribute(DbFormsErrors.ERRORS);

        return DbFormsValidatorUtil.getJavascript(getFormValidatorName(), MessageResources.getLocale((HttpServletRequest) pageContext.getRequest()), childFieldNames, vr, getJavascriptValidationSrcFile(), errors);
    }


    /**
     *  Use by generateJavascriptFieldsArray() to sort the order
     *  of field name.
     *
     * @param  str Description of the Parameter
     * @return  Description of the Return Value
     */
    private ArrayList sortFields(String str)
    {
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

        while (token.hasMoreTokens())
        {
            tmp = (String) token.nextToken();

            if (tmp.indexOf("@root") != -1)
            {
                arr.add(tmp);
            }
            else
            {
                insroot = tmp;
            }
        }

        if (insroot != null)
        {
            arr.add(insroot);
        }

        //String[] result = (String[]) arr.toArray(new String[arr.size()]);
        if (arr.size() == 1)
        {
            return arr;
        }

        for (int i = 0; i <= (arr.size() - 2); i++)
        {
            tmp1 = (String) arr.get(i);

            for (int j = i + 1; j <= (arr.size() - 1); j++)
            {
                tmp2 = (String) arr.get(j);

                if ((tmp1.indexOf("@root") != -1) && (tmp2.indexOf("@root") != -1))
                {
                    try
                    {
                        ident1 = Integer.parseInt(tmp1.substring(tmp1.indexOf("_", 2) + 1, tmp1.indexOf("@")));
                        ident2 = Integer.parseInt(tmp2.substring(tmp2.indexOf("_", 2) + 1, tmp2.indexOf("@")));
                    }
                    catch (Exception e)
                    {
                        ident1 = -1;
                        ident2 = -1;
                    }

                    if (ident2 < ident1)
                    {
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
     *
     * @param  name Description of the Parameter
     * @return  The childFieldValue value
     */
    public String getChildFieldValue(String name)
    {
        ResultSetVector result = null;
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        if (webEvent instanceof ReloadEvent)
        {
            Field field = (Field) getTable().getFieldByName(name);

            if (field == null)
            {
                logCat.warn("Field name : " + name + " is not present in Table " + getTable().getName());
            }

            String keyIndex = (getFooterReached()) ? ("ins" + getPositionPathCore()) : getPositionPath();
            StringBuffer buf = new StringBuffer();

            buf.append("f_");
            buf.append(getTable().getId());
            buf.append("_");
            buf.append(keyIndex);
            buf.append("_");
            buf.append(field.getId());

            return request.getParameter(buf.toString());
        }

        if (name.indexOf(":") != -1)
        {
            name = name.substring(name.indexOf("."), name.length());

            if (parentForm != null)
            {
                result = parentForm.getResultSetVector();
            }
            else
            {
                result = getResultSetVector();
            }
        }
        else
        {
            result = getResultSetVector();
        }

        if (result != null)
        {
            return result.getField(name);
        }
        else
        {
            return null;
        }
    }


    /** DOCUMENT ME! */
    public void doFinally()
    {
        logCat.info("doFinally called");
        SqlUtil.closeConnection(con);
    }


    /**
     * DOCUMENT ME!
     *
     * @param  t DOCUMENT ME!
     * @throws  Throwable DOCUMENT ME!
     */
    public void doCatch(Throwable t) throws Throwable
    {
        logCat.info("doCatch called - " + t.toString());
        throw t;
    }
}
