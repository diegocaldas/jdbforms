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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * DbPanel.java
 *
 * Created on 26. April 2001, 15:42
 */

package com.itp.dbforms.devgui;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.*;
import javax.swing.*;

/**
 *
 * @author  Joachim Peer <joepeer@excite.com>
 * @version
 */
public class DbPanel extends PropertyPanel implements ActionListener {


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel jLabel1;
    private JTextField tf_jdbcDriver;
    private JLabel jLabel5;
    private JTextField tf_jdbcURL;
    private JLabel jLabel6;
    private JTextField tf_username;
    private JLabel jLabel7;
    private JTextField tf_password;
    private JButton b_testConnection;

    // End of variables declaration//GEN-END:variables

    /** Creates new form DbPanel */
    public DbPanel(DevGui parent) {
		super(parent.getProjectData());
        initComponents ();
        doLayout();
    }

	public void setNewProjectData(ProjectData projectData) {
		this.projectData = projectData;

		tf_jdbcDriver.setText(projectData.getProperty("jdbcDriver"));
		tf_jdbcURL.setText(projectData.getProperty("jdbcURL"));
		tf_username.setText(projectData.getProperty("username"));
		tf_password.setText(projectData.getProperty("password"));
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents



        jLabel1 = new javax.swing.JLabel();
        tf_jdbcDriver = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tf_jdbcURL = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tf_username = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tf_password = new javax.swing.JTextField();




			setLayout(new BorderLayout());

			JPanel panel_top = new JPanel();
			panel_top.setLayout(new GridLayout(5,2));
			add(BorderLayout.NORTH, panel_top);


		// property jdbc driver
        jLabel1.setText("JDBC Driver Class");
        panel_top.add(jLabel1);
        addAFocusListener(tf_jdbcDriver, "jdbcDriver");
        panel_top.add(tf_jdbcDriver);

		// property jdbcuUrl
        jLabel5.setText("JDBC URL");
        panel_top.add(jLabel5);
        addAFocusListener(tf_jdbcURL, "jdbcURL");
        panel_top.add(tf_jdbcURL);

		// property userName
        jLabel6.setText("Username");
        panel_top.add(jLabel6);
        addAFocusListener(tf_username, "username");
        panel_top.add(tf_username);

		// property userName
        jLabel7.setText("Password");
        panel_top.add(jLabel7);
 		addAFocusListener(tf_password, "password");
        panel_top.add(tf_password);


		b_testConnection = new JButton("Test connection");
		b_testConnection.addActionListener(this);
		b_testConnection.setToolTipText("Check if the configuration provided in this dialog is correct.");
		panel_top.add(b_testConnection);

    }//GEN-END:initComponents



    private void testConnection() {
		  String jdbcDriver = projectData.getProperty("jdbcDriver");
		  String jdbcURL = projectData.getProperty("jdbcURL");
		  String username = projectData.getProperty("username");
		  String password = projectData.getProperty("password");

    	  Connection con = null;
			try {
				con = XMLConfigGenerator.createConnection(jdbcDriver, jdbcURL, username, password);
				if(con==null) JOptionPane.showMessageDialog(this, "No connection", "db failure", JOptionPane.ERROR_MESSAGE); else
				JOptionPane.showMessageDialog(this, "Database connection successfully installed.", "Success", JOptionPane.PLAIN_MESSAGE);

			} catch(Exception e) {
				if(con==null) JOptionPane.showMessageDialog(this, "Error:"+e.toString(), "db failure", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} finally {
				try {
					if(con!=null) con.close();
				}	catch(SQLException sqle) {}
			}
	}

    public void actionPerformed(ActionEvent e)  {
	  if(e.getSource() == b_testConnection) {
		testConnection();
	  }
	}

}
