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

package org.dbforms.devgui;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.sql.*;

import java.util.Vector;

import javax.swing.*;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class ConfigFilePanel extends PropertyPanel implements ActionListener {
   private javax.swing.ButtonGroup catalogButtonGroup;
   private javax.swing.ButtonGroup forkeyButtonGroup;
   private javax.swing.ButtonGroup schemaButtonGroup;
   private javax.swing.ButtonGroup tableButtonGroup;

   /* changes 2002-03-04 dikr:
   * - gui part of this panel is totally new, new functionality:
   *    - radiobuttons for selecting all catalogs/schemas/tables or entering a filter value
   *    - names of catalogs and schemas can be loaded into a combobox
   *    - catalog and schemanames can be included in table names written to dbforms
   *       config file. added checkboxes for this feature
   *    - option to let creation of config file run in transaction mode. this is a workaround
   *       for current Ingres II
   *    - let user free select if to examine tables, views, system tables via checkboxes
   * - new options and properties needed changes in several parts of program
   */
   private DevGui                   parent;
   private EditorPanel              panel_editor;
   private javax.swing.JButton      b_browse;
   private javax.swing.JButton      b_generate;
   private javax.swing.JButton      b_loadLists;
   private javax.swing.JCheckBox    cb_autocommit;
   private javax.swing.JCheckBox    cb_examine_systabs;
   private javax.swing.JCheckBox    cb_examine_tables;
   private javax.swing.JCheckBox    cb_examine_views;
   private javax.swing.JCheckBox    cb_include_catalogname;
   private javax.swing.JCheckBox    cb_include_schemaname;
   private javax.swing.JCheckBox    cb_stdTypeNames;
   private javax.swing.JComboBox    catalogList;
   private javax.swing.JComboBox    dateFormat;
   private javax.swing.JComboBox    schemaList;
   private javax.swing.JLabel       jLabel1;
   private javax.swing.JLabel       jLabel2;
   private javax.swing.JLabel       jLabel3;
   private javax.swing.JLabel       jLabel4;
   private javax.swing.JLabel       jLabel5;
   private javax.swing.JLabel       jLabel6;
   private javax.swing.JLabel       jLabel7;
   private javax.swing.JPanel       jPanel1;
   private javax.swing.JPanel       jPanel2;
   private javax.swing.JPanel       jPanel3;
   private javax.swing.JPanel       jPanel4;
   private javax.swing.JPanel       jPanel5;
   private javax.swing.JPanel       jPanel6;
   private javax.swing.JPanel       panel_top;
   private javax.swing.JRadioButton rb_catalog_all;
   private javax.swing.JRadioButton rb_catalog_combobox;
   private javax.swing.JRadioButton rb_forkey_deactivated;
   private javax.swing.JRadioButton rb_forkey_getCrossReferences;
   private javax.swing.JRadioButton rb_forkey_getImportedKeys;
   private javax.swing.JRadioButton rb_schema_all;
   private javax.swing.JRadioButton rb_schema_combobox;
   private javax.swing.JRadioButton rb_table_all;
   private javax.swing.JRadioButton rb_table_textfield;
   private javax.swing.JSeparator   jSeparator1;

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JSeparator jSeparator2;
   private javax.swing.JSeparator jSeparator3;
   private javax.swing.JSeparator jSeparator4;
   private javax.swing.JSeparator jSeparator5;
   private javax.swing.JSeparator jSeparator6;
   private javax.swing.JSeparator jSeparator7;
   private javax.swing.JSeparator jSeparator8;
   private javax.swing.JTextField tf_configFile;
   private javax.swing.JTextField tf_tableNamePattern;
   private String[]               dateFormats = {
                                                   "",
                                                   "yyyy-MM-dd",
                                                   "yyyy_MM_dd",
                                                   "yyyy/MM/dd",
                                                   "yyyy.MM.dd",
                                                   "dd-MMM-yyyy",
                                                   "dd.MM.yyyy",
                                                   "dd/MM/yyyyy",
                                                   "MM-dd-yyyy"
                                                };

   /**
    * Creates new form WebAppPanel
    *
    * @param parent DOCUMENT ME!
    */
   public ConfigFilePanel(DevGui parent) {
      super(parent.getProjectData());
      this.parent = parent;
      initComponents(); // this method is automatically generated by

      // ndetbeans GUI builder
      initComponents2(); // this method is written by a human
      doLayout();
   }

   //GEN-LAST:event_loadCatalogAndSchemaNames

   /**
    * DOCUMENT ME!
    *
    * @param projectData DOCUMENT ME!
    */
   public void setNewProjectData(ProjectData projectData) {
      this.projectData = projectData;

      String webAppRoot = projectData.getProperty("webAppRoot");
      String configFile = projectData.getProperty(CONFIG_FILE);

      tf_configFile.setText("");

      if (!"".equals(configFile)) {
         tf_configFile.setText(configFile);

         File f = new File(configFile);

         if (f.exists() && f.isFile()) {
            panel_editor.setFile(f);
         }
      } else {
         if (!"".equals(webAppRoot)) {
            tf_configFile.setText(FileNameTool.normalize(webAppRoot)
                                  + "WEB-INF" + parent.getFileSeparator());
         }
      }

      initializeCatalogAndSchemaList();

      // set radioButtons for foreign key detection:
      String forKeyDetection = projectData.getProperty(FOREIGNKEY_DETECTION);

      if (DEACTIVATED.equalsIgnoreCase(forKeyDetection)) {
         rb_forkey_deactivated.setSelected(true);
      } else if (USE_GETCROSSREFERENCES.equalsIgnoreCase(forKeyDetection)) {
         rb_forkey_getCrossReferences.setSelected(true);
      } else {
         rb_forkey_getImportedKeys.setSelected(true);
      }

      // set radioButtons for catalog, schema and tablenamepattern selection:
      if (ALL.equalsIgnoreCase(projectData.getProperty(CATALOG_SELECTION))) {
         rb_catalog_all.setSelected(true);
      } else {
         rb_catalog_combobox.setSelected(true);
      }

      if (ALL.equalsIgnoreCase(projectData.getProperty(SCHEMA_SELECTION))) {
         rb_schema_all.setSelected(true);
      } else {
         rb_schema_combobox.setSelected(true);
      }

      if (ALL.equalsIgnoreCase(projectData.getProperty(TABLE_SELECTION))) {
         rb_table_all.setSelected(true);
      } else {
         rb_table_textfield.setSelected(true);
      }

      // set checkboxes for including catalog or schemaname and for
      // autocommit mode:
      cb_include_catalogname.setSelected(TRUESTRING.equalsIgnoreCase(projectData
                                                                     .getProperty(INCLUDE_CATALOGNAME)));
      cb_include_schemaname.setSelected(TRUESTRING.equalsIgnoreCase(projectData
                                                                    .getProperty(INCLUDE_SCHEMANAME)));
      cb_autocommit.setSelected(TRUESTRING.equalsIgnoreCase(projectData
                                                            .getProperty(AUTOCOMMIT_MODE)));

      cb_stdTypeNames.setSelected(TRUESTRING.equalsIgnoreCase(projectData
                                                              .getProperty(WRITE_STD_TYPENAMES)));

      // set checkboxes for examination of tables, views and system tables:
      cb_examine_tables.setSelected(TRUESTRING.equalsIgnoreCase(projectData
                                                                .getProperty(EXAMINE_TABLES)));
      cb_examine_views.setSelected(TRUESTRING.equalsIgnoreCase(projectData
                                                               .getProperty(EXAMINE_VIEWS)));
      cb_examine_systabs.setSelected(TRUESTRING.equalsIgnoreCase(projectData
                                                                 .getProperty(EXAMINE_SYSTABS)));

      // set value for comboboxes for catalog and schema:
      catalogList.setSelectedItem(projectData.getProperty(CATALOG));
      schemaList.setSelectedItem(projectData.getProperty(SCHEMA));

      // set textfield for tablename pattern:
      tf_tableNamePattern.setText(projectData.getProperty(TABLE_NAME_PATTERN));

      // set date format, if found:
      if (projectData.getProperty(DATE_FORMAT) != null) {
         dateFormat.setSelectedItem(projectData.getProperty(DATE_FORMAT));
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param ev DOCUMENT ME!
    */
   public void actionPerformed(ActionEvent ev) {
      if (ev.getSource() == b_generate) {
         String configFile = projectData.getProperty(CONFIG_FILE);

         if (!"".equals(configFile)) {
            File f = new File(configFile);

            try {
               String result = XMLConfigGenerator.createXMLOutput(projectData,
                                                                  true);

               FileOutputStream os = new FileOutputStream(f);
               ByteArrayInputStream is = new ByteArrayInputStream(result.getBytes());

               byte[] b    = new byte[1024];
               int    read;

               while ((read = is.read(b)) != -1) {
                  os.write(b, 0, read);
               }

               panel_editor.setFile(f);
            } catch (IOException ioe) {
               showExceptionDialog(ioe);
            } catch (Exception e) {
               showExceptionDialog(e);
            }
         } else {
            JOptionPane.showMessageDialog(this,
                                          "Please enter name of config file");
         }
      } else if (ev.getSource() == b_browse) {
         String configFile = projectData.getProperty(CONFIG_FILE);

         System.out.println("configFile=" + configFile + "!");

         File dlgFile;

         System.out.println("ps2");

         if (!"".equals(configFile)) {
            System.out.println("ps3");
            dlgFile = new File(configFile);
            System.out.println("ps4");
         } else {
            System.out.println("ps5");

            String webAppRoot = projectData.getProperty("webAppRoot");
            System.out.println("ps6");

            if (!"".equals(webAppRoot)) {
               System.out.println("ps7");
               dlgFile = new File(webAppRoot);
               System.out.println("ps8");
            } else {
               System.out.println("ps9");
               dlgFile = null;
               System.out.println("ps10");
            }
         }

         if ((dlgFile != null) && dlgFile.exists()) {
            dlgFile = null;
         }

         JFileChooser dlg_fileChooser = new JFileChooser(dlgFile);
         dlg_fileChooser.setVisible(true);

         int returnVal = dlg_fileChooser.showOpenDialog(this);

         if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = dlg_fileChooser.getSelectedFile();
            tf_configFile.setText(selectedFile.getAbsolutePath());
            tf_configFile.grabFocus();
            panel_editor.setFile(selectedFile);
         }
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param cb DOCUMENT ME!
    * @param propertyName DOCUMENT ME!
    */
   protected void addCheckBoxItemListener(final JCheckBox cb,
                                          final String    propertyName) {
      cb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
               projectData.setProperty(propertyName,
                                       cb.isSelected() ? TRUESTRING
                                                       : FALSESTRING);
            }
         });
   }


   /**
    * DOCUMENT ME!
    *
    * @param cb DOCUMENT ME!
    * @param p DOCUMENT ME!
    */
   protected void addComboBoxItemListener(final JComboBox cb,
                                          final String    p) {
      cb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
               projectData.setProperty(p, (String) cb.getSelectedItem());
            }
         });
   }


   /**
    * DOCUMENT ME!
    *
    * @param rb DOCUMENT ME!
    * @param propertyName DOCUMENT ME!
    * @param propertyValue DOCUMENT ME!
    */
   protected void addJRadioButtonActionListener(final JRadioButton rb,
                                                final String       propertyName,
                                                final String       propertyValue) {
      rb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
               projectData.setProperty(propertyName, propertyValue);
            }
         });
   }


   /**
    * This method is called from within the constructor to initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is always
    * regenerated by the Form Editor.
    */
   private void initComponents() { //GEN-BEGIN:initComponents

      java.awt.GridBagConstraints gridBagConstraints;

      catalogButtonGroup           = new javax.swing.ButtonGroup();
      schemaButtonGroup            = new javax.swing.ButtonGroup();
      tableButtonGroup             = new javax.swing.ButtonGroup();
      forkeyButtonGroup            = new javax.swing.ButtonGroup();
      panel_top                    = new javax.swing.JPanel();
      tf_tableNamePattern          = new javax.swing.JTextField();
      jPanel2                      = new javax.swing.JPanel();
      jLabel1                      = new javax.swing.JLabel();
      tf_configFile                = new javax.swing.JTextField();
      b_browse                     = new javax.swing.JButton();
      rb_catalog_all               = new javax.swing.JRadioButton();
      rb_schema_all                = new javax.swing.JRadioButton();
      rb_table_all                 = new javax.swing.JRadioButton();
      rb_table_textfield           = new javax.swing.JRadioButton();
      jSeparator1                  = new javax.swing.JSeparator();
      jSeparator2                  = new javax.swing.JSeparator();
      jSeparator3                  = new javax.swing.JSeparator();
      jSeparator4                  = new javax.swing.JSeparator();
      rb_catalog_combobox          = new javax.swing.JRadioButton();
      jPanel1                      = new javax.swing.JPanel();
      b_generate                   = new javax.swing.JButton();
      rb_schema_combobox           = new javax.swing.JRadioButton();
      cb_autocommit                = new javax.swing.JCheckBox();
      jPanel3                      = new javax.swing.JPanel();
      jLabel3                      = new javax.swing.JLabel();
      cb_include_catalogname       = new javax.swing.JCheckBox();
      jLabel4                      = new javax.swing.JLabel();
      cb_include_schemaname        = new javax.swing.JCheckBox();
      jLabel5                      = new javax.swing.JLabel();
      jPanel4                      = new javax.swing.JPanel();
      jLabel6                      = new javax.swing.JLabel();
      cb_examine_tables            = new javax.swing.JCheckBox();
      cb_examine_views             = new javax.swing.JCheckBox();
      cb_examine_systabs           = new javax.swing.JCheckBox();
      jSeparator5                  = new javax.swing.JSeparator();
      catalogList                  = new javax.swing.JComboBox();
      schemaList                   = new javax.swing.JComboBox();
      b_loadLists                  = new javax.swing.JButton();
      cb_stdTypeNames              = new javax.swing.JCheckBox();
      jSeparator6                  = new javax.swing.JSeparator();
      jPanel5                      = new javax.swing.JPanel();
      jLabel2                      = new javax.swing.JLabel();
      dateFormat                   = new javax.swing.JComboBox();
      jSeparator7                  = new javax.swing.JSeparator();
      jPanel6                      = new javax.swing.JPanel();
      jLabel7                      = new javax.swing.JLabel();
      rb_forkey_getImportedKeys    = new javax.swing.JRadioButton();
      rb_forkey_getCrossReferences = new javax.swing.JRadioButton();
      rb_forkey_deactivated        = new javax.swing.JRadioButton();
      jSeparator8                  = new javax.swing.JSeparator();

      setLayout(new java.awt.BorderLayout());

      panel_top.setLayout(new java.awt.GridBagLayout());

      gridBagConstraints           = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx     = 2;
      gridBagConstraints.gridy     = 7;
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.fill      = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.anchor    = java.awt.GridBagConstraints.WEST;
      gridBagConstraints.weightx   = 1.0;
      panel_top.add(tf_tableNamePattern, gridBagConstraints);

      jPanel2.setLayout(new java.awt.GridBagLayout());

      jLabel1.setText("DbForms-Config File:   ");
      jPanel2.add(jLabel1, new java.awt.GridBagConstraints());

      tf_configFile.setMinimumSize(new java.awt.Dimension(50, 24));
      gridBagConstraints         = new java.awt.GridBagConstraints();
      gridBagConstraints.fill    = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.weightx = 1.0;
      jPanel2.add(tf_configFile, gridBagConstraints);

      b_browse.setText("browse...");
      jPanel2.add(b_browse, new java.awt.GridBagConstraints());

      gridBagConstraints           = new java.awt.GridBagConstraints();
      gridBagConstraints.gridwidth = 4;
      gridBagConstraints.fill      = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.anchor    = java.awt.GridBagConstraints.WEST;
      gridBagConstraints.weightx   = 1.0;
      panel_top.add(jPanel2, gridBagConstraints);

      rb_catalog_all.setSelected(true);
      rb_catalog_all.setText("in all catalogs");
      catalogButtonGroup.add(rb_catalog_all);
      gridBagConstraints        = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx  = 0;
      gridBagConstraints.gridy  = 5;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
      panel_top.add(rb_catalog_all, gridBagConstraints);

      rb_schema_all.setSelected(true);
      rb_schema_all.setText("in all schemas");
      schemaButtonGroup.add(rb_schema_all);
      gridBagConstraints        = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx  = 0;
      gridBagConstraints.gridy  = 6;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
      panel_top.add(rb_schema_all, gridBagConstraints);

      rb_table_all.setSelected(true);
      rb_table_all.setText("with arbitrary names");
      tableButtonGroup.add(rb_table_all);
      gridBagConstraints        = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx  = 0;
      gridBagConstraints.gridy  = 7;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
      panel_top.add(rb_table_all, gridBagConstraints);

      rb_table_textfield.setText("with table name pattern:");
      tableButtonGroup.add(rb_table_textfield);
      gridBagConstraints        = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx  = 1;
      gridBagConstraints.gridy  = 7;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
      panel_top.add(rb_table_textfield, gridBagConstraints);

      gridBagConstraints           = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx     = 0;
      gridBagConstraints.gridy     = 1;
      gridBagConstraints.gridwidth = 4;
      gridBagConstraints.fill      = java.awt.GridBagConstraints.HORIZONTAL;
      panel_top.add(jSeparator1, gridBagConstraints);

      gridBagConstraints           = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx     = 0;
      gridBagConstraints.gridy     = 16;
      gridBagConstraints.gridwidth = 4;
      gridBagConstraints.fill      = java.awt.GridBagConstraints.HORIZONTAL;
      panel_top.add(jSeparator2, gridBagConstraints);

      gridBagConstraints           = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx     = 0;
      gridBagConstraints.gridy     = 8;
      gridBagConstraints.gridwidth = 4;
      gridBagConstraints.fill      = java.awt.GridBagConstraints.HORIZONTAL;
      panel_top.add(jSeparator3, gridBagConstraints);

      gridBagConstraints           = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx     = 0;
      gridBagConstraints.gridy     = 10;
      gridBagConstraints.gridwidth = 4;
      gridBagConstraints.fill      = java.awt.GridBagConstraints.HORIZONTAL;
      panel_top.add(jSeparator4, gridBagConstraints);

      rb_catalog_combobox.setText("in catalog with name:");
      catalogButtonGroup.add(rb_catalog_combobox);
      rb_catalog_combobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               rb_catalog_comboboxActionPerformed(evt);
            }
         });

      gridBagConstraints        = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx  = 1;
      gridBagConstraints.gridy  = 5;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
      panel_top.add(rb_catalog_combobox, gridBagConstraints);

      b_generate.setText("Generate XML!");
      jPanel1.add(b_generate);

      gridBagConstraints           = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx     = 0;
      gridBagConstraints.gridy     = 19;
      gridBagConstraints.gridwidth = 4;
      panel_top.add(jPanel1, gridBagConstraints);

      rb_schema_combobox.setText("in schema with name pattern:");
      schemaButtonGroup.add(rb_schema_combobox);
      gridBagConstraints        = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx  = 1;
      gridBagConstraints.gridy  = 6;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
      panel_top.add(rb_schema_combobox, gridBagConstraints);

      cb_autocommit.setSelected(true);
      cb_autocommit.setText("Use autocommit mode while reading metadata (recommended).");
      gridBagConstraints           = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx     = 0;
      gridBagConstraints.gridy     = 9;
      gridBagConstraints.gridwidth = 4;
      gridBagConstraints.anchor    = java.awt.GridBagConstraints.WEST;
      panel_top.add(cb_autocommit, gridBagConstraints);

      jLabel3.setText("Include");
      jPanel3.add(jLabel3);

      cb_include_catalogname.setText("catalog name");
      jPanel3.add(cb_include_catalogname);

      jLabel4.setText(" ");
      jPanel3.add(jLabel4);

      cb_include_schemaname.setText("schema name");
      jPanel3.add(cb_include_schemaname);

      jLabel5.setText("in table name.");
      jPanel3.add(jLabel5);

      gridBagConstraints           = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx     = 0;
      gridBagConstraints.gridy     = 15;
      gridBagConstraints.gridwidth = 4;
      gridBagConstraints.anchor    = java.awt.GridBagConstraints.WEST;
      panel_top.add(jPanel3, gridBagConstraints);

      jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

      jLabel6.setText("Examine");
      jPanel4.add(jLabel6);

      cb_examine_tables.setSelected(true);
      cb_examine_tables.setText("Tables");
      jPanel4.add(cb_examine_tables);

      cb_examine_views.setSelected(true);
      cb_examine_views.setText("Views");
      jPanel4.add(cb_examine_views);

      cb_examine_systabs.setText("System Tables");
      jPanel4.add(cb_examine_systabs);

      gridBagConstraints           = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx     = 0;
      gridBagConstraints.gridy     = 3;
      gridBagConstraints.gridwidth = 4;
      gridBagConstraints.fill      = java.awt.GridBagConstraints.HORIZONTAL;
      panel_top.add(jPanel4, gridBagConstraints);

      gridBagConstraints           = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx     = 0;
      gridBagConstraints.gridy     = 4;
      gridBagConstraints.gridwidth = 4;
      gridBagConstraints.fill      = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.anchor    = java.awt.GridBagConstraints.WEST;
      panel_top.add(jSeparator5, gridBagConstraints);

      catalogList.setEditable(true);
      catalogList.setMinimumSize(new java.awt.Dimension(124, 24));
      catalogList.setPreferredSize(new java.awt.Dimension(128, 24));
      gridBagConstraints         = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx   = 2;
      gridBagConstraints.gridy   = 5;
      gridBagConstraints.fill    = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.anchor  = java.awt.GridBagConstraints.WEST;
      gridBagConstraints.weightx = 1.0;
      panel_top.add(catalogList, gridBagConstraints);

      schemaList.setEditable(true);
      schemaList.setMinimumSize(new java.awt.Dimension(124, 24));
      schemaList.setPreferredSize(new java.awt.Dimension(128, 24));
      gridBagConstraints         = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx   = 2;
      gridBagConstraints.gridy   = 6;
      gridBagConstraints.fill    = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.anchor  = java.awt.GridBagConstraints.WEST;
      gridBagConstraints.weightx = 1.0;
      panel_top.add(schemaList, gridBagConstraints);

      b_loadLists.setText("Load");
      b_loadLists.setToolTipText("Load catalog and schema names from database");
      b_loadLists.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               loadCatalogAndSchemaNames(evt);
            }
         });

      gridBagConstraints            = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx      = 3;
      gridBagConstraints.gridy      = 5;
      gridBagConstraints.gridheight = 2;
      gridBagConstraints.fill       = java.awt.GridBagConstraints.VERTICAL;
      gridBagConstraints.weighty    = 0.5;
      panel_top.add(b_loadLists, gridBagConstraints);

      cb_stdTypeNames.setText("Try to write standard type names for unknown field types into xml config file.");
      gridBagConstraints           = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx     = 0;
      gridBagConstraints.gridy     = 11;
      gridBagConstraints.gridwidth = 4;
      gridBagConstraints.anchor    = java.awt.GridBagConstraints.WEST;
      panel_top.add(cb_stdTypeNames, gridBagConstraints);

      gridBagConstraints           = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx     = 0;
      gridBagConstraints.gridy     = 14;
      gridBagConstraints.gridwidth = 4;
      gridBagConstraints.fill      = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.weightx   = 1.0;
      panel_top.add(jSeparator6, gridBagConstraints);

      jLabel2.setText("Set date format to: ");
      jPanel5.add(jLabel2);

      dateFormat.setEditable(true);
      jPanel5.add(dateFormat);

      gridBagConstraints           = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx     = 0;
      gridBagConstraints.gridy     = 17;
      gridBagConstraints.gridwidth = 4;
      gridBagConstraints.anchor    = java.awt.GridBagConstraints.WEST;
      panel_top.add(jPanel5, gridBagConstraints);

      gridBagConstraints           = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx     = 0;
      gridBagConstraints.gridy     = 18;
      gridBagConstraints.gridwidth = 4;
      gridBagConstraints.fill      = java.awt.GridBagConstraints.HORIZONTAL;
      panel_top.add(jSeparator7, gridBagConstraints);

      jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

      jLabel7.setText("Foreign Key Detection:");
      jPanel6.add(jLabel7);

      rb_forkey_getImportedKeys.setSelected(true);
      rb_forkey_getImportedKeys.setText("getImportedKeys( )");
      forkeyButtonGroup.add(rb_forkey_getImportedKeys);
      jPanel6.add(rb_forkey_getImportedKeys);

      rb_forkey_getCrossReferences.setText("getCrossReferences( )");
      rb_forkey_getCrossReferences.setToolTipText("null");
      forkeyButtonGroup.add(rb_forkey_getCrossReferences);
      jPanel6.add(rb_forkey_getCrossReferences);

      rb_forkey_deactivated.setText("deactivated");
      forkeyButtonGroup.add(rb_forkey_deactivated);
      jPanel6.add(rb_forkey_deactivated);

      gridBagConstraints           = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx     = 0;
      gridBagConstraints.gridy     = 13;
      gridBagConstraints.gridwidth = 4;
      gridBagConstraints.insets    = new java.awt.Insets(0, 1, 0, 1);
      gridBagConstraints.anchor    = java.awt.GridBagConstraints.WEST;
      panel_top.add(jPanel6, gridBagConstraints);

      gridBagConstraints           = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx     = 0;
      gridBagConstraints.gridy     = 12;
      gridBagConstraints.gridwidth = 4;
      gridBagConstraints.fill      = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.weightx   = 1.0;
      panel_top.add(jSeparator8, gridBagConstraints);

      add(panel_top, java.awt.BorderLayout.NORTH);
   }


   private void initComponents2() {
      b_browse.addActionListener(this);

      // first listener => for global property / Project data
      addAFocusListener(tf_configFile, CONFIG_FILE);

      //  second listener => for notification of texteditor
      tf_configFile.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) {
               File f = new File(tf_configFile.getText());

               if (f.exists() && f.isFile() && f.canRead()) {
                  panel_editor.setFile(f);
               }
            }
         });

      b_browse.setToolTipText("default: webAppRoot" + parent.getFileSeparator()
                              + "WEB-INF" + parent.getFileSeparator()
                              + "dbforms-config.xml\nNon-default values must be configured in web.xml!");

      b_generate.addActionListener(this);
      b_generate.setToolTipText("Generate raw content of config-file by quering the database you defined in tab \"Database properties\".");

      panel_editor = new EditorPanel();
      add(BorderLayout.CENTER, panel_editor);

      // we will now register several event listeners that will automatically
      // change property values in case corresponding gui elements are
      // changed:
      // register event listener that sets property in case
      // value of table name pettern textfield is changed:
      addAFocusListener(tf_tableNamePattern, TABLE_NAME_PATTERN);

      // register event listeners for radio buttons...
      // example: if rb_catalog_all is activated, then property
      // CATALOG_SELECTION will be set to value ALL
      addJRadioButtonActionListener(rb_catalog_all, CATALOG_SELECTION, ALL);
      addJRadioButtonActionListener(rb_catalog_combobox, CATALOG_SELECTION,
                                    SELECTION);

      addJRadioButtonActionListener(rb_schema_all, SCHEMA_SELECTION, ALL);
      addJRadioButtonActionListener(rb_schema_combobox, SCHEMA_SELECTION,
                                    SELECTION);

      addJRadioButtonActionListener(rb_table_all, TABLE_SELECTION, ALL);
      addJRadioButtonActionListener(rb_table_textfield, TABLE_SELECTION,
                                    SELECTION);

      addJRadioButtonActionListener(rb_forkey_getImportedKeys,
                                    FOREIGNKEY_DETECTION, USE_GETIMPORTEDKEYS);
      addJRadioButtonActionListener(rb_forkey_getCrossReferences,
                                    FOREIGNKEY_DETECTION, USE_GETCROSSREFERENCES);
      addJRadioButtonActionListener(rb_forkey_deactivated,
                                    FOREIGNKEY_DETECTION, DEACTIVATED);

      // register event listeners for checkboxes...
      // example: cb_include_catalogname is bound to
      // property INCLUDE_CATALOGNAME
      addCheckBoxItemListener(cb_include_catalogname, INCLUDE_CATALOGNAME);
      addCheckBoxItemListener(cb_include_schemaname, INCLUDE_SCHEMANAME);
      addCheckBoxItemListener(cb_autocommit, AUTOCOMMIT_MODE);
      addCheckBoxItemListener(cb_examine_tables, EXAMINE_TABLES);
      addCheckBoxItemListener(cb_examine_views, EXAMINE_VIEWS);
      addCheckBoxItemListener(cb_examine_systabs, EXAMINE_SYSTABS);
      addCheckBoxItemListener(cb_stdTypeNames, WRITE_STD_TYPENAMES);

      initializeCatalogAndSchemaList();

      // register event listeners to automatically set
      // properties for catalog name and schema name:
      addComboBoxItemListener(catalogList, CATALOG);
      addComboBoxItemListener(schemaList, SCHEMA);

      // initialize combo box with date formats:
      for (int i = 0; i < dateFormats.length; i++) {
         dateFormat.addItem(dateFormats[i]);
      }

      // register event Listener for date format combo box:
      addComboBoxItemListener(dateFormat, DATE_FORMAT);
   }


   private void initializeCatalogAndSchemaList() {
      // create initial values for selection lists used in
      // combo boxes for catalog and schema names:
      Vector catalogs = new Vector();
      catalogs.add("--- no catalogs loaded ---");
      catalogList.setModel(new DefaultComboBoxModel(catalogs));

      //                      catalogList.setSelectedIndex(0);
      //                     projectData.setProperty(CATALOG,(String)catalogList.getItemAt(0));
      Vector schemas = new Vector();
      schemas.add("--- no schemas loaded ---");
      schemaList.setModel(new DefaultComboBoxModel(schemas));

      //                     schemaList.setSelectedIndex(0);
      //                     projectData.setProperty(SCHEMA,(String)schemaList.getItemAt(0));
   }


   //GEN-LAST:event_rb_catalog_comboboxActionPerformed

   /*
     try to read catalog and schema names from database and fill corresponding combo boxes
     */
   private void loadCatalogAndSchemaNames(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_loadCatalogAndSchemaNames

      String jdbcDriver = projectData.getProperty("jdbcDriver");
      String jdbcURL  = projectData.getProperty("jdbcURL");
      String username = projectData.getProperty("username");
      String password = projectData.getProperty("password");

      try {
         try {
            Class.forName(jdbcDriver)
                 .newInstance();
         } catch (ClassNotFoundException ex) {
            String message = "Could not find JDBC driver class " + jdbcDriver
                             + "\n"
                             + "Please check database properties and classpath!";

            if (jdbcDriver.equalsIgnoreCase("")) {
               message = "JDBC driver class in database properties not set!\n"
                         + "Please check database properties!";
            }

            JOptionPane.showMessageDialog(this, message);

            return;
         }

         Connection con = DriverManager.getConnection(jdbcURL, username,
                                                      password);

         DatabaseMetaData dbmd = con.getMetaData();

         // try to read list of catalogs, in case of error
         // continue with schemas....
         Vector catalogs = new Vector();

         try {
            ResultSet catalogRs = dbmd.getCatalogs();

            while (catalogRs.next()) {
               catalogs.add(catalogRs.getString(1));
            }
         } catch (SQLException ignored) {
            ;
         }

         if (catalogs.size() == 0) {
            catalogs.add(" --- no catalogs found ---");
         }

         catalogList.setModel(new DefaultComboBoxModel(catalogs));
         catalogList.setSelectedIndex(0);
         projectData.setProperty(CATALOG, (String) catalogList.getItemAt(0));

         // try to read list of schemas
         Vector    schemas  = new Vector();
         ResultSet schemaRs = dbmd.getSchemas();

         while (schemaRs.next()) {
            schemas.add(schemaRs.getString(1));
         }

         if (schemas.size() == 0) {
            schemas.add("--- no schemas found ---");
         }

         schemaList.setModel(new DefaultComboBoxModel(schemas));
         schemaList.setSelectedIndex(0);
         projectData.setProperty(SCHEMA, (String) schemaList.getItemAt(0));
      } catch (Exception ex) {
         showExceptionDialog(ex);
      }
   }


   //GEN-END:initComponents
   private void rb_catalog_comboboxActionPerformed(java.awt.event.ActionEvent evt) {
      //GEN-FIRST:event_rb_catalog_comboboxActionPerformed
      // Add your handling code here:
   }

   // End of variables declaration//GEN-END:variables
}
