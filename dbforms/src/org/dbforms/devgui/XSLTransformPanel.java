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
 *  WebAppPanel.java
 *
 *  Created on 26. April 2001, 15:42
 */
package org.dbforms.devgui;

import org.dbforms.xmldb.FileSplitter;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



/**
 * DOCUMENT ME!
 *
 * @author Joachim Peer
 * @version
 *
 */
public class XSLTransformPanel extends AbstractPropertyPanel implements ActionListener,
                                                                ListSelectionListener {
   private DevGui              parent;
   private EditorPanel         panel_editor;
   private javax.swing.JButton b_browse;
   private JButton             b_openInBrowser;
   private JButton             b_refresh;
   private JButton             b_refreshJSPs;
   private JButton             b_start;
   private JCheckBox           cb_useJsCalendar;
   private javax.swing.JLabel  jLabel1;
   private javax.swing.JLabel  jLabel2;

   // GUI Variables declaration
   private JList list_results;

   // GUI Variables declaration
   private JList                  list_xslStylesheets;
   private javax.swing.JPanel     jPanel2;
   private javax.swing.JTextField tf_stylesheetDir;
   private javax.swing.JTextField tf_xsltEncoding;
   private File[]                 JSPs;

   // other data
   private File[] availableStylesheets;

   /**
    * Creates new form WebAppPanel
    *
    * @param parent Description of the Parameter
    */
   public XSLTransformPanel(DevGui parent) {
      super(parent.getProjectData());

      this.parent = parent;

      initComponents();

      initComponents2();

      refreshAvailableStylesheets();

      refreshJSPs();

      doLayout();
   }

   /**
    * Sets the newProjectData attribute of the XSLTransformPanel object
    *
    * @param projectData The new newProjectData value
    */
   public void setNewProjectData(ProjectData projectData) {
      this.projectData = projectData;

      cb_useJsCalendar.setSelected(TRUESTRING.equalsIgnoreCase(projectData
                                                               .getProperty(USE_JAVASCRIPT_CALENDAR)));

      tf_stylesheetDir.setText(projectData.getProperty(STYLESHEET_DIR));

      tf_xsltEncoding.setText(projectData.getProperty(XSLT_ENCODING));

      refreshAvailableStylesheets();

      refreshJSPs();
   }


   /**
    * Description of the Method
    *
    * @param ev Description of the Parameter
    */
   public void actionPerformed(ActionEvent ev) {
      if (ev.getSource() == b_openInBrowser) {
         int selIndex = list_results.getSelectedIndex();

         if (selIndex != -1) {
            String selFileName = ((File) list_results.getModel()
                                                     .getElementAt(selIndex))
                                 .getName();

            String webAppURLStr = projectData.getProperty("webAppURL")
                                             .trim();

            StringBuffer webAppURL = new StringBuffer(webAppURLStr);

            if (!webAppURLStr.endsWith("/")) {
               webAppURL.append("/");
            }

            webAppURL.append(selFileName);

            try {
               BrowserTool.openURL(webAppURL.toString());
            } catch (Exception ioe) {
               showExceptionDialog(ioe);
            }
         }
      } else if (ev.getSource() == b_refresh) {
         refreshAvailableStylesheets();
      } else if (ev.getSource() == b_start) {
         performXSLTransformation();
      } else if (ev.getSource() == b_refreshJSPs) {
         refreshJSPs();
      } else if (ev.getSource() == b_browse) {
         String stylesheetDir = projectData.getProperty(STYLESHEET_DIR);

         System.out.println("styleSheetDir=" + stylesheetDir + "!");

         File dlgFile;

         System.out.println("ps2");

         if (!"".equals(stylesheetDir)) {
            System.out.println("ps3");

            dlgFile = new File(stylesheetDir);

            System.out.println("ps4");
         } else {
            System.out.println("ps5");

            dlgFile = null;
         }

         //if (dlgFile != null && dlgFile.exists()) {
         //  dlgFile = null;
         //}
         JFileChooser dlg_fileChooser = new JFileChooser(dlgFile);
         dlg_fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

         dlg_fileChooser.setVisible(true);

         int returnVal = dlg_fileChooser.showOpenDialog(this);

         if (returnVal == JFileChooser.APPROVE_OPTION) {
            String dirname = dlg_fileChooser.getSelectedFile()
                                            .getAbsolutePath();
            tf_stylesheetDir.setText(dirname);
            projectData.setProperty(STYLESHEET_DIR, dirname);
            refreshAvailableStylesheets();
            tf_stylesheetDir.grabFocus();
         }
      }
   }


   /**
    * Description of the Method
    *
    * @param e Description of the Parameter
    */
   public void valueChanged(ListSelectionEvent e) {
      if (e.getSource() == list_xslStylesheets) {
         int selIndex = list_xslStylesheets.getSelectedIndex();

         if (selIndex >= 0) {
            File f = (File) list_xslStylesheets.getModel()
                                               .getElementAt(selIndex);

            panel_editor.setFile(f);

            b_start.setEnabled(true);
         } else {
            b_start.setEnabled(false);
         }
      } else if (e.getSource() == list_results) {
         int selIndex = list_results.getSelectedIndex();

         if (selIndex >= 0) {
            File f = (File) list_results.getModel()
                                        .getElementAt(selIndex);

            panel_editor.setFile(f);

            b_openInBrowser.setEnabled(true);
         } else {
            b_openInBrowser.setEnabled(false);
         }
      }
   }


   /**
    * This method is called from within the constructor to initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is always
    * regenerated by the FormEditor.
    */
   private void initComponents() {
      b_browse = new javax.swing.JButton();

      jLabel1 = new javax.swing.JLabel();

      tf_stylesheetDir = new javax.swing.JTextField();
      tf_stylesheetDir.setText(projectData.getProperty(STYLESHEET_DIR));

      jLabel2 = new javax.swing.JLabel();

      tf_xsltEncoding = new javax.swing.JTextField();
      tf_xsltEncoding.setText(projectData.getProperty(XSLT_ENCODING));

      setLayout(new GridBagLayout());

      JPanel panel_top = new JPanel();

      panel_top.setLayout(new GridLayout(1, 2));

      JPanel panel_top_left = new JPanel();

      // xsl stylesheet stuff
      panel_top_left.setLayout(new BorderLayout());

      panel_top_left.add(BorderLayout.NORTH, new JLabel("XSL stylesheets"));

      list_xslStylesheets = new JList();

      list_xslStylesheets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      list_xslStylesheets.addListSelectionListener(this);

      panel_top_left.add(BorderLayout.CENTER,
                         new JScrollPane(list_xslStylesheets));

      JPanel panel_buttons1 = new JPanel();

      panel_buttons1.add(b_start = new JButton("start transformation!"));

      b_start.addActionListener(this);

      b_start.setToolTipText("generate JSPs by applying selected XSL file to the dbforms config XML file.");

      panel_buttons1.add(b_refresh = new JButton("refresh list"));

      b_refresh.addActionListener(this);

      b_refresh.setToolTipText("refresh XSL stylesheet repository in "
                               + parent.getDbFormsHome().getAbsolutePath()
                               + parent.getFileSeparator() + "xsl-stylesheets");

      panel_top_left.add(BorderLayout.SOUTH, panel_buttons1);

      JPanel panel_top_right = new JPanel();

      // xhtml result stuff
      panel_top_right.setLayout(new BorderLayout());

      panel_top_right.add(BorderLayout.NORTH,
                          new JLabel("(Generated) JSP files in WebApp-Root"));

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

      cb_useJsCalendar = new JCheckBox("Use JavaScript Calendar in generated pages for editing date fields");

      jPanel2 = new javax.swing.JPanel();

      jPanel2.setLayout(new java.awt.GridBagLayout());

      java.awt.GridBagConstraints gridBagConstraints2;

      jLabel1.setText("Stylesheet Directory:   ");

      gridBagConstraints2 = new java.awt.GridBagConstraints();

      jPanel2.add(jLabel1, gridBagConstraints2);

      tf_stylesheetDir.setMinimumSize(new java.awt.Dimension(50, 24));

      gridBagConstraints2 = new java.awt.GridBagConstraints();

      gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;

      gridBagConstraints2.weightx = 1.0;

      jPanel2.add(tf_stylesheetDir, gridBagConstraints2);

      b_browse.setText("browse...");

      gridBagConstraints2           = new java.awt.GridBagConstraints();
      gridBagConstraints2.gridwidth = GridBagConstraints.REMAINDER;

      jPanel2.add(b_browse, gridBagConstraints2);

      jLabel2.setText("XSLT Encoding:   ");
      jLabel2.setHorizontalTextPosition(SwingConstants.RIGHT);

      java.awt.GridBagConstraints gridBagConstraints3 = new java.awt.GridBagConstraints();
      gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
      jPanel2.add(jLabel2, gridBagConstraints3);

      gridBagConstraints3         = new java.awt.GridBagConstraints();
      gridBagConstraints3.fill    = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints3.weightx = 1.0;
      tf_xsltEncoding.setMinimumSize(new java.awt.Dimension(50, 24));

      //tf_encoding.setPreferredSize(new java.awt.Dimension(50, 24));
      jPanel2.add(tf_xsltEncoding, gridBagConstraints3);

      cb_useJsCalendar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
               projectData.setProperty(USE_JAVASCRIPT_CALENDAR,
                                       cb_useJsCalendar.isSelected()
                                       ? TRUESTRING
                                       : FALSESTRING);
            }
         });

      GridBagConstraints gridBagConstraints1 = new java.awt.GridBagConstraints();

      gridBagConstraints1.gridx = 0;

      gridBagConstraints1.gridy   = 0;
      gridBagConstraints1.weightx = 1.0;
      gridBagConstraints1.fill    = java.awt.GridBagConstraints.HORIZONTAL;

      gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;

      add(jPanel2, gridBagConstraints1);

      //       add(tf_stylesheetDir, gridBagConstraints1);
      gridBagConstraints1.gridx = 0;

      gridBagConstraints1.gridy = 1;

      gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;

      add(cb_useJsCalendar, gridBagConstraints1);

      gridBagConstraints1.gridy = 2;

      gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;

      gridBagConstraints1.weightx = 0.5;

      gridBagConstraints1.weighty = 0.5;

      add(panel_top, gridBagConstraints1);

      //      add(BorderLayout.NORTH, panel_top);
      panel_editor = new EditorPanel();

      gridBagConstraints1.gridy = 3;

      add(panel_editor, gridBagConstraints1);

      //      add(BorderLayout.CENTER, panel_editor);
      // set logical state of dialog
      list_xslStylesheets.clearSelection();

      b_start.setEnabled(false);

      list_results.clearSelection();

      b_openInBrowser.setEnabled(false);
   }


   /**
    * Description of the Method
    */
   private void initComponents2() {
      b_browse.addActionListener(this);

      // first listener => for global property / Project data
      addAFocusListener(tf_stylesheetDir, STYLESHEET_DIR);

      b_browse.setToolTipText("default: DBFORMS_HOME"
                              + parent.getFileSeparator() + "xsl-stylesheets"
                              + parent.getFileSeparator()
                              + "\nDefault location for Stylesheets");

      // listener => for global property / Project data
      addAFocusListener(tf_xsltEncoding, XSLT_ENCODING);
   }


   /**
    * Description of the Method
    */
   private void performXSLTransformation() {
      System.out.println(projectData.toString());

      boolean useJsCalendar = TRUESTRING.equalsIgnoreCase(projectData
                                                          .getProperty(USE_JAVASCRIPT_CALENDAR));

      String  webAppRoot = projectData.getProperty("webAppRoot");

      System.out.println("webAppRoot=" + webAppRoot + "!");

      if ("".equals(webAppRoot)) {
         JOptionPane.showMessageDialog(this,
                                       "Please provide web application root",
                                       "missing data", JOptionPane.ERROR_MESSAGE);

         return;
      }

      //String sourcePath = webAppRoot + parent.getFileSeparator() + "WEB-INF" + parent.getFileSeparator() + projectData.getProperty("configFile");
      String sourcePath = projectData.getProperty("configFile");

      File   sourceFile = new File(sourcePath);

      System.out.println("sourcePath=" + sourcePath);

      if (!sourceFile.exists() || !sourceFile.canRead()) {
         JOptionPane.showMessageDialog(this,
                                       "Please provide correct XML config file, "
                                       + sourcePath + " is wrong",
                                       "wrong data", JOptionPane.ERROR_MESSAGE);

         return;
      }

      File transformFile = (File) list_xslStylesheets.getModel()
                                                     .getElementAt(list_xslStylesheets
                                                                   .getSelectedIndex());

      if (!transformFile.exists() || !transformFile.canRead()) {
         JOptionPane.showMessageDialog(this,
                                       "Please provide correct XSL stylesheet",
                                       "wrong data", JOptionPane.ERROR_MESSAGE);

         return;
      }

      String destPath = FileNameTool.normalize(parent.getDbFormsHome().getAbsolutePath())
                        + "temp" + parent.getFileSeparator()
                        + "temp_result.xhtml";

      File   destFile = new File(destPath);

      try {
         String xsltEncoding = projectData.getProperty(XSLT_ENCODING);

         // removve trailing spaces if not null
         if (xsltEncoding != null) {
            xsltEncoding = xsltEncoding.trim();
         }

         XSLTransformer.transform(sourceFile, transformFile, destFile,
                                  useJsCalendar, xsltEncoding);

         FileSplitter fs = new FileSplitter(destFile, new File(webAppRoot));

         fs.splitFile();

         this.refreshJSPs();

         JOptionPane.showMessageDialog(this,
                                       "XSL Transformation done.\nCheck new JSPs in your WebApp root",
                                       "XSL result", JOptionPane.PLAIN_MESSAGE);
      } catch (Exception e) {
         JOptionPane.showMessageDialog(this,
                                       "Error during transform:" + e.toString(),
                                       "XSL transform error",
                                       JOptionPane.ERROR_MESSAGE);
      }
   }


   /**
    * Description of the Method
    */
   private void refreshAvailableStylesheets() {
      //String xslDirStr = FileNameTool.normalize(parent.getDbFormsHome().getAbsolutePath()) + "xsl-stylesheets";
      String xslDirStr = FileNameTool.normalize(parent.getProjectData().getProperty(STYLESHEET_DIR));
      File   xslDir = new File(xslDirStr);

      if (xslDir.isDirectory() && xslDir.canRead()) {
         try {
            this.availableStylesheets = FileUtility.getFilesInDirectory(xslDir,
                                                                        null);

            list_xslStylesheets.setListData(this.availableStylesheets);
         } catch (Exception ioe) {
            ioe.printStackTrace();

            showExceptionDialog(ioe);
         }
      }
   }


   /**
    * Description of the Method
    */
   private void refreshJSPs() {
      String jspDirStr = projectData.getProperty("webAppRoot");

      File   jspDir = new File(jspDirStr);

      if (jspDir.isDirectory() && jspDir.canRead()) {
         try {
            String[] postFixList = {
                                      ".jsp"
                                   };

            this.JSPs = FileUtility.getFilesInDirectory(jspDir, postFixList);

            list_results.setListData(this.JSPs);
         } catch (Exception ioe) {
            ioe.printStackTrace();

            showExceptionDialog(ioe);
         }
      }
   }
}
