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

package org.dbforms.xmldb;
import java.sql.*;
import java.io.*;
import java.util.*;



/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public class DbTool
{
    private String propertyFile;
    private String outputFile;
    private String driverClass;
    private String dbURL;

    /**
     * Creates a new DbTool object.
     *
     * @param propertyFile DOCUMENT ME!
     * @param outputFile DOCUMENT ME!
     */
    public DbTool(String propertyFile, String outputFile)
    {
        this.propertyFile = propertyFile;
        this.outputFile = outputFile;
    }

    private Connection createConnection() throws SQLException, ClassNotFoundException, InstantiationException, IOException, IllegalAccessException
    {
        Properties props = new Properties();
        props.load(new FileInputStream(new File(propertyFile)));

        this.driverClass = props.getProperty("connection-class");
        this.dbURL = props.getProperty("connection-url");

        String dbUser = props.getProperty("username");
        String dbPwd = props.getProperty("password");

        System.out.println("driverClass=" + driverClass);
        System.out.println("dbURL=" + dbURL);
        System.out.println("dbUser=" + dbUser);
        System.out.println("dbPwd=" + dbPwd);

        Class.forName(driverClass).newInstance();

        return DriverManager.getConnection(dbURL, dbUser, dbPwd);
    }


    /**
     * DOCUMENT ME!
     */
    public void createXMLOutput()
    {
        try
        {
            StringBuffer result = new StringBuffer();
            result.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n\n<dbforms-config>\n");

            Connection con = createConnection();

            if (con == null)
            {
                System.exit(1);
            }

            DatabaseMetaData dbmd = con.getMetaData();
            String[] types = { "TABLE", "VIEW" };
            ResultSet tablesRS = dbmd.getTables("", "", "", types);

            while (tablesRS.next())
            {
                String tableName = tablesRS.getString(3);

                result.append("\t<table name=\"");
                result.append(tableName);
                result.append("\">\n");

                ResultSet rsKeys = dbmd.getPrimaryKeys("", "", tableName);
                Vector keys = new Vector();

                while (rsKeys.next())
                {
                    String columnName = rsKeys.getString(4);
                    keys.addElement(columnName);
                }

                rsKeys.close();

                ResultSet rsFields = dbmd.getColumns("", "", tableName, null);

                while (rsFields.next())
                {
                    String columnName = rsFields.getString(4);
                    short dataType = rsFields.getShort(5);
                    String typeName = rsFields.getString(6);
                    int columnSize = rsFields.getInt(7);
                    String isNullable = rsFields.getString(18);

                    result.append("\t\t<field name=\"");
                    result.append(columnName);
                    result.append("\" fieldType=\"");
                    result.append(typeName);
                    result.append("\" size=\"");
                    result.append(columnSize);
                    result.append("\"");

                    if (keys.contains(columnName))
                    {
                        result.append(" isKey=\"true\"");
                    }

                    result.append("/>\n");
                }

                rsFields.close();

                result.append("\n\t\t<!-- add \"granted-privileges\" element for security constraints -->\n\n\t</table>\n\n");
            }

            tablesRS.close();

            result.append("\t<!-- ========== Connection =================================== -->\n");
            result.append("\t<!--\n");
            result.append("\tuncomment this if you have access to JNDI of an application server (see users guide for more info)\n");
            result.append("\t<dbconnection\n");
            result.append("\t\tname = \"jdbc/dbformstest\"\n");
            result.append("\t\tisJndi = \"true\"\n");
            result.append("\t/>\n");
            result.append("\t-->\n\n");

            result.append("\t<dbconnection\n");
            result.append("\t\tname   = \"" + dbURL + "\"\n");
            result.append("\t\tisJndi = \"false\"\n");
            result.append("\t\tconClass  = \"" + driverClass + "\"\n");
            result.append("\t/>\n");
            result.append("</dbforms-config>");

            FileOutputStream os = new FileOutputStream(new File(outputFile));
            ByteArrayInputStream is = new ByteArrayInputStream(result.toString().getBytes());

            byte[] b = new byte[1024];
            int read;

            while ((read = is.read(b)) != -1)
            {
                os.write(b, 0, read);
            }

            os.close();
            System.out.println("finished");
        }
        catch (Exception e)
        {
            System.out.println("Error:" + e.toString());
            e.printStackTrace();
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.out.println("usage: java DbTool propertyFile outputFile\n\nexample:\njava DbTool db.properties config.xml");
            System.exit(1);
        }

        new DbTool(args[0], args[1]).createXMLOutput();
    }
}