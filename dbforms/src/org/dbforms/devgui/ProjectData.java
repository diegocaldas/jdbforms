/*
 *  $Header$
 *  $Revision$
 *  $Date$
 *
 *  DbForms - a Rapid Application Development Framework
 *  Copyright (C) 2001 Joachim Peer <j.peer@gmx.net> et al.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */
/*
 *  Project.java
 *
 *  Created on 26. April 2001, 15:14
 */
package org.dbforms.devgui;

import java.io.*;

import java.util.*;

/**
 * @author     Joe Peer
 *
 * Changes:
 * - 2002-03-4: usage of default property and constant string values
 *                   via new Interface Propertynames (dikr)
 * @created    October 18, 2002
 */

public class ProjectData implements Serializable, PropertyNames {

    private File file;

    private boolean unsavedChanges = false;

    private Properties props;

    private static Properties defaultProps;
    // contains default values for properties


    // set default property values:

    static {

        defaultProps = new Properties();

        defaultProps.setProperty(INCLUDE_SCHEMANAME, FALSESTRING);

        defaultProps.setProperty(INCLUDE_CATALOGNAME, FALSESTRING);

        defaultProps.setProperty(AUTOCOMMIT_MODE, TRUESTRING);

        defaultProps.setProperty(CATALOG_SELECTION, ALL);

        defaultProps.setProperty(SCHEMA_SELECTION, ALL);

        defaultProps.setProperty(TABLE_SELECTION, ALL);

        defaultProps.setProperty(EXAMINE_TABLES, TRUESTRING);

        defaultProps.setProperty(EXAMINE_VIEWS, TRUESTRING);

        defaultProps.setProperty(EXAMINE_SYSTABS, FALSESTRING);

        defaultProps.setProperty(WRITE_STD_TYPENAMES, FALSESTRING);

    }



    /** Creates new Project */

    public ProjectData() {

        this.props = new Properties(defaultProps);

        String dbFormsHomeStr = System.getProperty("DBFORMS_HOME");

        if (dbFormsHomeStr != null) {

            setProperty(STYLESHEET_DIR, dbFormsHomeStr + 
               System.getProperties().getProperty("file.separator") + "xsl-stylesheets");

        }

        unsavedChanges = false;

    }



    /**
     *Constructor for the ProjectData object
     *
     * @param  props  Description of the Parameter
     */
    public ProjectData(Properties props) {

        this.props = props;

        unsavedChanges = false;

        // If a user hasn't specified a STYLESHEET_DIR, then guess at one!
        String dbFormsHomeStr = props.getProperty(STYLESHEET_DIR);

        if ("".equals(dbFormsHomeStr)) {

            dbFormsHomeStr = System.getProperty("DBFORMS_HOME");

            if (dbFormsHomeStr != null) {

                setProperty(STYLESHEET_DIR, dbFormsHomeStr + 
                      System.getProperties().getProperty("file.separator") + "xsl-stylesheets");

                unsavedChanges = true;

            }

        }

    }



    /**
     *  Gets the property attribute of the ProjectData object
     *
     * @param  prop  Description of the Parameter
     * @return       The property value
     */
    public String getProperty(String prop) {

        return props.getProperty(prop, "");
        // return default value "" instead of null if not found

    }



    /**
     *  Sets the property attribute of the ProjectData object
     *
     * @param  key    The new property value
     * @param  value  The new property value
     */
    public void setProperty(String key, String value) {

        String oldValue = getProperty(key);

        if (!oldValue.equals(value)) {

            props.setProperty(key, value);

            this.unsavedChanges = true;

            System.out.println("unsaved state look like this:" + this.toString());

        }

    }



    /**
     *  Gets the unsaved attribute of the ProjectData object
     *
     * @return    The unsaved value
     */
    public boolean isUnsaved() {

        return unsavedChanges;
    }



    /**
     *  Sets the file attribute of the ProjectData object
     *
     * @param  file  The new file value
     */
    public void setFile(File file) {

        this.file = file;

    }



    /**
     *  Gets the file attribute of the ProjectData object
     *
     * @return    The file value
     */
    public File getFile() {

        return file;
    }



    /**
     *  Description of the Method
     *
     * @param  f                Description of the Parameter
     * @return                  Description of the Return Value
     * @exception  IOException  Description of the Exception
     */
    public static ProjectData loadFromDisc(File f)
        throws IOException {

        Properties l_props = new Properties(defaultProps);

        l_props.load(new FileInputStream(f));

        ProjectData pd = new ProjectData(l_props);

        pd.setFile(f);

        return pd;
    }



    /**
     *  Description of the Method
     *
     * @param  f                Description of the Parameter
     * @exception  IOException  Description of the Exception
     */
    public void storeToDisc(File f)
        throws IOException {

        this.props.store(new FileOutputStream(f), "DbForms DevGUI Property File");

        this.file = f;

        this.unsavedChanges = false;

    }



    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    public String toString() {

        StringBuffer buf = new StringBuffer();

        Enumeration enum = props.propertyNames();

        boolean first = true;

        while (enum.hasMoreElements()) {

            if (first) {
                first = false;
            }
            else {
                buf.append(", ");
            }

            String aPropsName = (String) enum.nextElement();

            buf.append(aPropsName);

            buf.append("=");

            buf.append(this.getProperty(aPropsName));

        }

        return buf.toString();
    }

}
