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

	/*
	 * WebAppPanel.java
	 *
	 * Created on 26. April 2001, 15:42
	 */

	package org.dbforms.devgui;

	import java.io.*;
	import java.awt.*;
	import java.awt.event.*;
	import javax.swing.*;

	/**
	 *
	 * @author  Joachim Peer <j.peer@gmx.net>
	 * @version
	 */
	public class ConfigFilePanel extends PropertyPanel implements ActionListener {


		// GUI Variables declaration
		private JLabel jLabel1;
		private JTextField tf_configFile, tf_catalog, tf_schemaPattern, tf_tableNamePattern;
		private JButton b_generate, b_browse;
		private EditorPanel panel_editor;
		private DevGui parent;

		/** Creates new form WebAppPanel */
		public ConfigFilePanel(DevGui parent) {
			super(parent.getProjectData());
			this.parent = parent;
			initComponents ();
        	doLayout();
		}

		public void setNewProjectData(ProjectData projectData) {
			this.projectData = projectData;

			String webAppRoot = projectData.getProperty("webAppRoot");
			String configFile = projectData.getProperty("configFile");

			if(!"".equals(configFile)) {
				tf_configFile.setText(configFile);

				File f = new File (configFile);
				if(f.exists() && f.isFile()) {
					panel_editor.setFile(f);
				}

			} else {
				if(!"".equals(webAppRoot)) {
					tf_configFile.setText( FileNameTool.normalize(webAppRoot)  + "WEB-INF" + parent.getFileSeparator() );
				}
			}
		}


		/** This method is called from within the constructor to
		 * initialize the form.

		 */
		private void initComponents() {

			jLabel1 = new javax.swing.JLabel();

			setLayout(new BorderLayout());

			JPanel panel_top = new JPanel();
			panel_top.setLayout(new GridLayout(3,4));
			add(BorderLayout.NORTH, panel_top);

			jLabel1.setText("DbForms-Config File");
			panel_top.add(jLabel1);

			tf_configFile = new JTextField();

			// first listener => for global property / Project data
			addAFocusListener(tf_configFile, "configFile");

			// 	second listener => for notification of texteditor

        	tf_configFile.addFocusListener(new java.awt.event.FocusAdapter() {
        	    public void focusLost(java.awt.event.FocusEvent e){

					File f = new File(tf_configFile.getText());
					if(f.exists() && f.isFile() && f.canRead()) {
						panel_editor.setFile(f);
					}

        	    }
        	});



			panel_top.add(tf_configFile);

			b_browse = new JButton("browse...");
			b_browse.addActionListener(this);
			b_browse.setToolTipText("default: webAppRoot"+parent.getFileSeparator()+"WEB-INF"+parent.getFileSeparator()+"dbforms-config.xml\nNon-default values must be configured in web.xml!");
			panel_top.add(b_browse);

			b_generate = new JButton("Generate XML!");
			b_generate.addActionListener(this);
			b_generate.setToolTipText("Generate raw content of config-file by quering the database you defined in tab \"Database properties\".");
			panel_top.add(b_generate);

			panel_top.add(new JLabel("Catalog:"));
			tf_catalog = new JTextField();
			addAFocusListener(tf_catalog, "catalog");
			panel_top.add(tf_catalog);

			panel_top.add(new JLabel("Schema Pattern:"));
			tf_schemaPattern = new JTextField();
			addAFocusListener(tf_schemaPattern, "schemaPattern");
			panel_top.add(tf_schemaPattern);


			panel_top.add(new JLabel("Tablename Pattern:"));
			tf_tableNamePattern = new JTextField();
			addAFocusListener(tf_tableNamePattern, "tableNamePattern");
			panel_top.add(tf_tableNamePattern);


			panel_editor = new EditorPanel();
			add(BorderLayout.CENTER, panel_editor);

		}


		public void actionPerformed(ActionEvent ev) {
			if(ev.getSource() == b_generate) {
				String configFile = projectData.getProperty("configFile");
				if(!"".equals(configFile)) {

					File f = new File(configFile);

					try {

						String result = XMLConfigGenerator.createXMLOutput(projectData);


						FileOutputStream os = new FileOutputStream(f);
						ByteArrayInputStream is = new ByteArrayInputStream(result.toString().getBytes());

						byte[] b = new byte[1024];
						int read;
						while( (read = is.read(b)) != -1) {
						  os.write(b,0,read);
						}

						panel_editor.setFile(f);

					} catch(IOException ioe) {
					  showExceptionDialog(ioe);
					} catch(Exception e) {
					  showExceptionDialog(e);
					}



			    }

			} else if(ev.getSource() == b_browse) {

				String configFile = projectData.getProperty("configFile");

				System.out.println("cconfigFile="+configFile+"!");

				File dlgFile;

				System.out.println("ps2");
				if(!"".equals(configFile)) {
					System.out.println("ps3");
				  dlgFile = new File(configFile);
				  System.out.println("ps4");
				} else {
					System.out.println("ps5");
				  String webAppRoot = projectData.getProperty("webAppRoot");
				  System.out.println("ps6");
				  if(!"".equals(webAppRoot)) {
					  System.out.println("ps7");
				  	dlgFile = new File(webAppRoot);
				  	System.out.println("ps8");
				  } else {
					  System.out.println("ps9");
					dlgFile = null;
					System.out.println("ps10");
				  }
				}


				if(dlgFile != null && dlgFile.exists()) dlgFile = null;

				JFileChooser dlg_fileChooser = new JFileChooser(dlgFile);
				dlg_fileChooser.setVisible(true);

				int returnVal = dlg_fileChooser.showOpenDialog(this);

				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File selectedFile = dlg_fileChooser.getSelectedFile();
					tf_configFile.setText( selectedFile.getAbsolutePath() );
					tf_configFile.grabFocus();
					panel_editor.setFile(selectedFile);
				}


			}


		}
}
