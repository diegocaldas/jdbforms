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
import javax.swing.event.*;

import org.dbforms.xmldb.*;

/**
 *
 * @author  Joachim Peer <j.peer@gmx.net>
 * @version
 */
public class XSLTransformPanel extends PropertyPanel implements ActionListener, ListSelectionListener {


    // GUI Variables declaration

    private JList list_xslStylesheets, list_results;
    private JButton b_start, b_refresh, b_openInBrowser, b_save, b_refreshJSPs;
    private JTextArea ta_editor;
    private EditorPanel panel_editor;

	// other data

	private File[] availableStylesheets;
	private File[] JSPs;
	private DevGui parent;


    /** Creates new form WebAppPanel */
    public XSLTransformPanel(DevGui parent) {
		super(parent.getProjectData());
		this.parent = parent;
        initComponents ();
        refreshAvailableStylesheets();
        refreshJSPs();
        doLayout();

    }


	private void refreshAvailableStylesheets() {

		String xslDirStr = FileNameTool.normalize(parent.getDbFormsHome().getAbsolutePath()) + "xsl-stylesheets";
		File xslDir = new File(xslDirStr);
		if(xslDir.isDirectory() && xslDir.canRead()) {
			try {
				this.availableStylesheets = FileUtility.getFilesInDirectory(xslDir, null);
				list_xslStylesheets.setListData(this.availableStylesheets);
			} catch(IOException ioe) {
				ioe.printStackTrace();
				showExceptionDialog(ioe);
			}
		}

	}

	private void refreshJSPs() {

		String jspDirStr = projectData.getProperty("webAppRoot");
		File jspDir = new File(jspDirStr);

		if(jspDir.isDirectory() && jspDir.canRead()) {
			try {
				String[] postFixList = {".jsp"};
				this.JSPs = FileUtility.getFilesInDirectory(jspDir, postFixList);
				list_results.setListData(this.JSPs);
			} catch(IOException ioe) {
				ioe.printStackTrace();
				showExceptionDialog(ioe);
			}
		}

	}

	public void setNewProjectData(ProjectData projectData) {
		this.projectData = projectData;

        refreshAvailableStylesheets();
        refreshJSPs();
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents

        setLayout(new BorderLayout());

        JPanel panel_top = new JPanel();
        panel_top.setLayout(new GridLayout(1,2));

        JPanel panel_top_left = new JPanel(); // xsl stylesheet stuff
        panel_top_left.setLayout(new BorderLayout());
        panel_top_left.add(BorderLayout.NORTH, new JLabel("XSL stylesheets"));
		list_xslStylesheets = new JList();
		list_xslStylesheets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_xslStylesheets.addListSelectionListener(this);
		panel_top_left.add(BorderLayout.CENTER, new JScrollPane(list_xslStylesheets));

		JPanel panel_buttons1 = new JPanel();
		panel_buttons1.add(b_start = new JButton("start transformation!"));
		b_start.addActionListener(this);
		b_start.setToolTipText("generate JSPs by applying selected XSL file to the dbforms config XML file.");
		panel_buttons1.add(b_refresh = new JButton("refresh list"));
		b_refresh.addActionListener(this);
		b_refresh.setToolTipText("refresh XSL stylesheet repository in "+parent.getDbFormsHome().getAbsolutePath()+parent.getFileSeparator()+"xsl-stylesheets");
		panel_top_left.add(BorderLayout.SOUTH, panel_buttons1);

        JPanel panel_top_right = new JPanel(); // xhtml result stuff
		panel_top_right.setLayout(new BorderLayout());
		panel_top_right.add(BorderLayout.NORTH, new JLabel("(Generated) JSP files in WebApp-Root"));
		list_results = new JList();
		list_results.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_results.addListSelectionListener(this);

		panel_top_right.add(BorderLayout.CENTER, new JScrollPane(list_results));

		JPanel panel_buttons2 = new JPanel();
		b_openInBrowser = new JButton("Open in browser");
		b_openInBrowser.setToolTipText("Test a selected result file. Web server  must be running!");
		b_openInBrowser.addActionListener(this);
		b_refreshJSPs = new JButton("Refresh JSP list");
		b_refreshJSPs.addActionListener(this);
		b_refreshJSPs.setToolTipText("refresh listing of JSP files in your Web application");
		panel_buttons2.add(b_openInBrowser);
		panel_buttons2.add(b_refreshJSPs);
		panel_top_right.add(BorderLayout.SOUTH, panel_buttons2);

		panel_top.add(panel_top_left);
		panel_top.add(panel_top_right);

        add(BorderLayout.NORTH, panel_top);

		panel_editor = new EditorPanel();
		add(BorderLayout.CENTER, panel_editor);


		// set logical state of dialog
		list_xslStylesheets.clearSelection();
		b_start.setEnabled(false);
		list_results.clearSelection();
		b_openInBrowser.setEnabled(false);

    }


	private void performXSLTransformation() {

		System.out.println(projectData.toString());

		String webAppRoot = projectData.getProperty("webAppRoot");
		System.out.println("webAppRoot="+webAppRoot+"!");
		if("".equals(webAppRoot)) {
			JOptionPane.showMessageDialog(this, "Please provide web application root", "missing data", JOptionPane.ERROR_MESSAGE);
			return;
		}

		//String sourcePath = webAppRoot + parent.getFileSeparator() + "WEB-INF" + parent.getFileSeparator() + projectData.getProperty("configFile");
		String sourcePath =  projectData.getProperty("configFile");
		File sourceFile = new File(sourcePath);
		System.out.println("sourcePath="+sourcePath);
		if(!sourceFile.exists() || !sourceFile.canRead()) {
			JOptionPane.showMessageDialog(this, "Please provide correct XML config file, "+sourcePath+" is wrong", "wrong data", JOptionPane.ERROR_MESSAGE);
			return;
		}

		File transformFile = (File) list_xslStylesheets.getModel().getElementAt(list_xslStylesheets.getSelectedIndex());
		if(!transformFile.exists() || !transformFile.canRead()) {
			JOptionPane.showMessageDialog(this, "Please provide correct XSL stylesheet", "wrong data", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String destPath = FileNameTool.normalize(parent.getDbFormsHome().getAbsolutePath()) + "temp" + parent.getFileSeparator() + "temp_result.xhtml";
		File destFile = new File(destPath);


		try {

			XSLTransformer.transform(sourceFile, transformFile, destFile);
			FileSplitter fs = new FileSplitter(destFile, new File(webAppRoot));
			fs.splitFile();
			this.refreshJSPs();

			JOptionPane.showMessageDialog(this, "XSL Transformation done.\nCheck new JSPs in your WebApp root", "XSL result", JOptionPane.PLAIN_MESSAGE );

		}catch(Exception e) {
			JOptionPane.showMessageDialog(this, "Error during transform:"+e.toString(), "XSL transform error", JOptionPane.ERROR_MESSAGE);
		}

	}

    public void actionPerformed(ActionEvent ev) {

		if(ev.getSource() == b_openInBrowser) {

			int selIndex = list_results.getSelectedIndex();
			if(selIndex!=-1) {

				String selFileName = ((File) list_results.getModel().getElementAt(selIndex)).getName();
				String webAppURLStr = projectData.getProperty("webAppURL").trim();
				StringBuffer webAppURL = new StringBuffer(webAppURLStr);

				if(!webAppURLStr.toString().endsWith("/")) {
				  webAppURL.append("/");
				}
				webAppURL.append(selFileName);

				try {
					BrowserTool.openURL(webAppURL.toString());
				} catch(Exception ioe) {
					showExceptionDialog(ioe);
				}

			}

		} else if(ev.getSource() == b_refresh) {
			refreshAvailableStylesheets();
		} else if(ev.getSource() == b_start) {
			performXSLTransformation();
		} else if(ev.getSource()==b_refreshJSPs) {
			refreshJSPs();
		}

	}

	public void valueChanged(ListSelectionEvent e) {

		if(e.getSource()==list_xslStylesheets) {
			int selIndex = list_xslStylesheets.getSelectedIndex();
			if(selIndex>=0) {
				File f= (File) list_xslStylesheets.getModel().getElementAt(selIndex);
				panel_editor.setFile(f);
				b_start.setEnabled(true);
			} else {
				b_start.setEnabled(false);
			}
		} else if(e.getSource()==list_results) {
			int selIndex = list_results.getSelectedIndex();
			if(selIndex>=0) {
				File f= (File) list_results.getModel().getElementAt(selIndex);
				panel_editor.setFile(f);
				b_openInBrowser.setEnabled(true);
			} else {
				b_openInBrowser.setEnabled(false);
			}
		}

	}



}
