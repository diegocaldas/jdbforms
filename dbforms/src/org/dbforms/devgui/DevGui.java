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
 * DevGui.java
 *
 * Created on 26. April 2001, 14:50
 */
package org.dbforms.devgui;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.dbforms.util.Util;


/**
 *
 * -- DevGui --
 *
 * This SWING application provides support for automatic generation of JSP views.
 * It does so by retrieving database-metadata as XML file from a given datasource and by applying the
 * XML file to XSL stylesheets.
 *
 * HINTS FOR MAKING THIS APP MORE CONVENIENT ARE VERY WELCOME!
 *
 * Check manual or contact the author for more info.
 *
 * @author Joachim Peer <j.peer@gmx.net>
 * @version
 */
public class DevGui extends javax.swing.JFrame implements ActionListener
{
   private static final String metalLF   = "javax.swing.plaf.metal.MetalLookAndFeel";
   private static final String motifLF   = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
   private static final String windowsLF = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
   
   private static final String ARG_PROPERTY_FILE_NAME = "propertyfilename";
   private static final String ARG_CREATE_CONFIG = "createconfigfile";
   private static final String ARG_OUTPUT_FILE_NAME = "outputfilename";

   // Gui Variables declaration
   private javax.swing.JMenuBar    jMenuBar1;
   private javax.swing.JMenu       jMenu1;
   private javax.swing.JMenuItem   metalMenuItem;
   private javax.swing.JMenuItem   motifMenuItem;
   private javax.swing.JMenuItem   windowsMenuItem;
   private javax.swing.JMenuItem   newMenuItem;
   private javax.swing.JMenuItem   openMenuItem;
   private javax.swing.JMenuItem   saveMenuItem;
   private javax.swing.JMenuItem   saveAsMenuItem;
   private javax.swing.JSeparator  jSeparator1;
   private javax.swing.JMenuItem   exitMenuItem;
   private javax.swing.JTabbedPane jTabbedPane1;
   private javax.swing.JMenuItem   helpMenuItem;
   private javax.swing.JFrame      helpFrame;

   // windows (tabs)
   private PropertyPanel tab_dbPanel;
   private PropertyPanel tab_webAppPanel;
   private PropertyPanel tab_configFilePanel;
   private PropertyPanel tab_xslTranformPanel;

   // other variables
   private ProjectData  projectData;
   private String       dbFormsHomeStr;
   private File         dbFormsHome;
   private String       fileSeparator;
   private final String titleCore = "DbForms Developers' GUI - ";

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public ProjectData getProjectData()
   {
      return projectData;
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public String getFileSeparator()
   {
      return fileSeparator;
   }

   /** Creates new form DevGui */
   public DevGui(ProjectData projectData)
   {
   	  this.projectData = projectData;
   	  boolean projectDataLoaded = false;
      dbFormsHomeStr = System.getProperty("DBFORMS_HOME");

      if (dbFormsHomeStr != null)
      {
         dbFormsHome = new File(dbFormsHomeStr);
         System.out.println("dbFormsHome=" + dbFormsHome.getAbsolutePath());

         if (dbFormsHome.isDirectory() && dbFormsHome.canRead())
         {
            // CREATE DATA MODEL + SOME HELPER INFOS
            if (projectData == null) 
            {
            	this.projectData    = new ProjectData();
            }
            else
            {
            	projectDataLoaded = true;
            }

            this.fileSeparator = System.getProperties().getProperty("file.separator");

            // CREATE GUI
            this.setTitle(titleCore + "[New Project]");
            this.setSize(750, 500);
            initComponents();

            /* changes dikr 2002-03-04: set tab placement to top and
             *  shorten tab titles a bit....
             * (this is done here because initComponents seems to be
             * automatically generated by (netbeans?) Gui builder)
             */
            jTabbedPane1.setTabPlacement(SwingConstants.TOP);
            jTabbedPane1.setTitleAt(0, "(1) Web Application");
            jTabbedPane1.setToolTipTextAt(0,
               "specify web application location and URI");
            jTabbedPane1.setTitleAt(1, "(2) Database");
            jTabbedPane1.setToolTipTextAt(1,
               "set database properties like JDBC driver, username");
            jTabbedPane1.setTitleAt(2, "(3) XML Config");
            jTabbedPane1.setToolTipTextAt(2,
               "automatically generate dbforms config file");
            jTabbedPane1.setTitleAt(3, "(4) XSL Transformation");
            jTabbedPane1.setToolTipTextAt(3,
               "use XSL to generate JSPs from dbforms config file");

            /* create Menuitem for Look and Feel:
              */
            ButtonGroup lfButtonGroup = new ButtonGroup();
            JMenu       jMenu2 = new javax.swing.JMenu();
            jMenu2.setText("Look&Feel");

            metalMenuItem = new javax.swing.JRadioButtonMenuItem(
                  "Java Metal L&F");
            metalMenuItem.setActionCommand("Java Metal L&F");
            metalMenuItem.addActionListener(this);
            lfButtonGroup.add(metalMenuItem);
            jMenu2.add(metalMenuItem);

            motifMenuItem = new javax.swing.JRadioButtonMenuItem("Motif L&F");
            motifMenuItem.setActionCommand("Motiv L&F");
            motifMenuItem.addActionListener(this);
            lfButtonGroup.add(motifMenuItem);
            jMenu2.add(motifMenuItem);

            windowsMenuItem = new javax.swing.JRadioButtonMenuItem(
                  "Windows L&F");
            windowsMenuItem.setActionCommand("Windows L&F");
            windowsMenuItem.addActionListener(this);
            lfButtonGroup.add(windowsMenuItem);
            jMenu2.add(windowsMenuItem);

            // set Default L&F:
            metalMenuItem.setSelected(true);
            setLookAndFeel(metalLF);

            jMenuBar1.add(jMenu2);

            /* create Menuitem for Help:
            */
            JMenu jMenu3 = new javax.swing.JMenu();
            jMenu3.setText("Help");
            helpMenuItem = new javax.swing.JMenuItem();
            helpMenuItem.setActionCommand("Show Helptext");
            helpMenuItem.setText("Show Helptext");
            helpMenuItem.addActionListener(this);
            jMenu3.add(helpMenuItem);
            jMenuBar1.add(jMenu3);

            helpFrame = new HelpFrame();

            //        pack ();
            doLayout();
            
            if (projectDataLoaded) 
            {
              updateGUI();
               this.setTitle(titleCore + "["
                  + projectData.getFile().getAbsolutePath() + "]");
            }
         }
         else
         {
            quickQuit("DBFORMS_HOME is set to \"" + dbFormsHomeStr
               + "\", which is not a valid accessible directory.", 1);
         }
      }
      else
      {
         quickQuit("Pleases set the environment variable DBFORMS_HOME to the root of your unzipped DbForms-Distribution! Check manual for more info.",
            1);
      }
   }

   private void quickQuit(String message, int returnValue)
   {
      System.out.println(message);
      System.out.println(
         "\nPlease drop a line of feedback to j.peer@gmx.net, thanks!\n");
      System.exit(returnValue);
   }


   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the FormEditor.
    */
   private void initComponents()
   { //GEN-BEGIN:initComponents
      jMenuBar1         = new javax.swing.JMenuBar();
      jMenu1            = new javax.swing.JMenu();
      newMenuItem       = new javax.swing.JMenuItem();
      openMenuItem      = new javax.swing.JMenuItem();
      saveMenuItem      = new javax.swing.JMenuItem();
      saveAsMenuItem    = new javax.swing.JMenuItem();
      jSeparator1       = new javax.swing.JSeparator();
      exitMenuItem      = new javax.swing.JMenuItem();
      jTabbedPane1      = new javax.swing.JTabbedPane();

      jMenu1.setActionCommand("Project");
      jMenu1.setText("Project");

      newMenuItem.setActionCommand("New");
      newMenuItem.setText("New");
      newMenuItem.addActionListener(this);
      newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
            InputEvent.CTRL_MASK, false));
      jMenu1.add(newMenuItem);

      openMenuItem.setActionCommand("Open");
      openMenuItem.setText("Open");
      openMenuItem.addActionListener(this);
      openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
            InputEvent.CTRL_MASK, false));
      jMenu1.add(openMenuItem);

      saveMenuItem.setActionCommand("Save");
      saveMenuItem.setText("Save");
      saveMenuItem.addActionListener(this);
      saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
            InputEvent.CTRL_MASK, false));
      jMenu1.add(saveMenuItem);

      saveAsMenuItem.setActionCommand("Save As...");
      saveAsMenuItem.setText("Save As...");
      saveAsMenuItem.addActionListener(this);
      jMenu1.add(saveAsMenuItem);

      jMenu1.add(jSeparator1);

      exitMenuItem.setActionCommand("Exit");
      exitMenuItem.setText("Exit");
      exitMenuItem.addActionListener(this);
      jMenu1.add(exitMenuItem);

      jMenuBar1.add(jMenu1);

      addWindowListener(new java.awt.event.WindowAdapter()
         {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
               exitForm(evt);
            }
         });

      setJMenuBar(jMenuBar1);

      getContentPane().setLayout(new BorderLayout());

      getContentPane().add(java.awt.BorderLayout.CENTER, jTabbedPane1);
      jTabbedPane1.setTabPlacement(SwingConstants.LEFT);

      // install panels
      tab_webAppPanel = new WebAppPanel(this);

      //jTabbedPane1.addTab("(1) Web Application properties", null, tab_webAppPanel, "provide information about your web application");
      jTabbedPane1.add("(1) Web Application properties", tab_webAppPanel);

      tab_dbPanel = new DbPanel(this);

      //jTabbedPane1.addTab("(2) Database properties", null, tab_dbPanel, "provide valid data for creating a database connection");
      jTabbedPane1.add("(2) Database properties", tab_dbPanel);

      tab_configFilePanel = new ConfigFilePanel(this);

      //jTabbedPane1.addTab("(3) XML config", null, tab_configFilePanel, "automatically generate the DbForms Config file" );
      jTabbedPane1.add("(3) XML config", tab_configFilePanel);

      tab_xslTranformPanel = new XSLTransformPanel(this);

      //jTabbedPane1.addTab("(4) XSL transformation", null, tab_xslTranformPanel, "automatically generate JSP views based on table data stored in DbForms Config file");
      jTabbedPane1.add("(4) XSL transformation", tab_xslTranformPanel);

      tab_webAppPanel.doLayout();
      tab_dbPanel.doLayout();
      tab_configFilePanel.doLayout();
      tab_xslTranformPanel.doLayout();

      /*
                      jTabbedPane1.setSelectedIndex(0);
                      jTabbedPane1.setSelectedIndex(1);
                      jTabbedPane1.setSelectedIndex(2);
                      jTabbedPane1.setSelectedIndex(3);
                      jTabbedPane1.setSelectedIndex(0);

      */
   }
    //GEN-END:initComponents


   /**
    * DOCUMENT ME!
    *
    * @param e DOCUMENT ME!
    */
   public void actionPerformed(ActionEvent e)
   {
      if (e.getSource() == newMenuItem)
      {
         newProject();
      }
      else if (e.getSource() == openMenuItem)
      {
         openProject();
      }
      else if (e.getSource() == saveMenuItem)
      {
         saveProject();
      }
      else if (e.getSource() == saveAsMenuItem)
      {
         saveAsProject();
      }
      else if (e.getSource() == exitMenuItem)
      {
         exitProject();
      }
      else if (e.getSource() == helpMenuItem)
      {
         helpFrame.show();
      }
      else if (e.getSource() == metalMenuItem)
      {
         setLookAndFeel(metalLF);
      }
      else if (e.getSource() == motifMenuItem)
      {
         setLookAndFeel(motifLF);
      }
      else if (e.getSource() == windowsMenuItem)
      {
         setLookAndFeel(windowsLF);
      }
   }


   private void setLookAndFeel(String lf)
   {
      try
      {
         UIManager.setLookAndFeel(lf);
         SwingUtilities.updateComponentTreeUI(this);
      }
      catch (Exception ex)
      {
         System.err.println(ex.getMessage());
      }
   }


   /** Exit the Application */
   private void exitForm(java.awt.event.WindowEvent evt)
   { //GEN-FIRST:event_exitForm
      System.exit(0);
   }
    //GEN-LAST:event_exitForm


   //======================= BUSINESS METHODS
   private void updateGUI()
   {
      this.tab_dbPanel.setNewProjectData(projectData);
      this.tab_webAppPanel.setNewProjectData(projectData);
      this.tab_configFilePanel.setNewProjectData(projectData);
      this.tab_xslTranformPanel.setNewProjectData(projectData);
   }


   private void newProject()
   {
      this.projectData = new ProjectData();
      updateGUI();
      this.setTitle(titleCore + "[New Project]");

      System.out.println("new Project initialized");
   }


   private void openProject()
   {
      JFileChooser dlg_fileChooser = new JFileChooser();
      dlg_fileChooser.setDialogTitle("Open Project...");
      dlg_fileChooser.setVisible(true);

      int returnVal = dlg_fileChooser.showOpenDialog(this);

      if (returnVal == JFileChooser.APPROVE_OPTION)
      {
         File f = dlg_fileChooser.getSelectedFile();

         try
         {
            if (f.isFile())
            {
               this.projectData = ProjectData.loadFromDisc(f);
               updateGUI();
               this.setTitle(titleCore + "["
                  + projectData.getFile().getAbsolutePath() + "]");
            }
         }
         catch (IOException ioe)
         {
            ioe.printStackTrace();
            showExceptionDialog(ioe);
         }
      }
   }


   private void saveProject()
   {
      if (projectData.getFile() != null)
      {
         try
         {
            this.projectData.storeToDisc(projectData.getFile());
         }
         catch (IOException ioe)
         {
            ioe.printStackTrace();
            showExceptionDialog(ioe);
         }
      }
      else
      {
         saveAsProject();
      }
   }


   private void saveAsProject()
   {
      JFileChooser dlg_fileChooser = new JFileChooser();
      dlg_fileChooser.setDialogTitle("Save Project As...");
      dlg_fileChooser.setVisible(true);

      int returnVal = dlg_fileChooser.showSaveDialog(this);

      if (returnVal == JFileChooser.APPROVE_OPTION)
      {
         try
         {
            File f = dlg_fileChooser.getSelectedFile();
            this.projectData.storeToDisc(f);
            this.setTitle(titleCore + "["
               + projectData.getFile().getAbsolutePath() + "]");
         }
         catch (IOException ioe)
         {
            ioe.printStackTrace();
            showExceptionDialog(ioe);
         }
      }
   }


   // dkr 2002-03-04:  add gui dialog if user tries to exit while having unsaved data
   private void exitProject()
   {
      if (!projectData.isUnsaved())
      {
         System.exit(0);
      }
      else
      {
         int answer = JOptionPane.showOptionDialog(this,
               "Your project data has not been not saved.", "Ooops",
               JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
               null,
               new String[]
               {
                  "Save and exit",
                  "Exit without saving",
                  "Cancel"
               }, "Save and exit");

         switch (answer)
         {
            case 0:
               saveProject();

            case 1:
               System.exit(0);

            case 2:
               return;
         }
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public File getDbFormsHome()
   {
      return dbFormsHome;
   }


   private void showExceptionDialog(Exception e)
   {
      JOptionPane.showMessageDialog(this,
         "An exception occurred:\n\n" + e.toString() + "\n", "Exception",
         JOptionPane.ERROR_MESSAGE);
   }


   /**
   * @param args the command line arguments
   */
   public static void main(String[] args)
   {
   	
   	  Map argsMap = parseCommandlineParameters(args);
   	  
   	  // create dbforms-config.xml ad exit
   	  if (argsMap.get(ARG_CREATE_CONFIG) != null)
   	  {
   	    createConfigFile((String) argsMap.get(ARG_PROPERTY_FILE_NAME), (String) argsMap.get(ARG_OUTPUT_FILE_NAME));
   	  }
   	  // start gui
   	  else
   	  {
   	  	 ProjectData pd = null;
   	  	 String propsFileName = (String) argsMap.get(ARG_PROPERTY_FILE_NAME);
   	  	 // try to load property file
   	  	 if (! Util.isNull(propsFileName)) {
      	   File projFile = new File(propsFileName);
	       try {
			 pd = ProjectData.loadFromDisc(projFile);
		   } catch (IOException e) {
			 e.printStackTrace();
			 System.exit(1);
		   }
   	  	 }
         new DevGui(pd).show();
      }
   }
   
   protected static void createConfigFile(String propsFileName, String outputFileName) {
   	try
	   {
	      File        projFile       = new File(propsFileName);
	      ProjectData pd             = ProjectData.loadFromDisc(projFile);
	
          if (Util.isNull(outputFileName)) 
	      {
	         outputFileName = pd.getProperty("configFile");
	      }
	
	      if (Util.isNull(outputFileName))
	      {
	         throw new Exception(
	            "Property configFile in propery file not set");
	      }
	
	      String       result = XMLConfigGenerator.createXMLOutput(pd, false);
	      BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));
	      bw.write(result);
	      bw.close();
	   }
	   catch (Exception ex)
	   {
	      ex.printStackTrace();
	   }
   }
   
   protected static Map parseCommandlineParameters(String[] args) 
   {
      if (args == null) {
      	return null;
      }
      
      Map argsMap = new HashMap();
      
      for (int i=0; i<args.length; i++)
      {
      	if (args[i].equalsIgnoreCase("-propertyfile"))
      	{
      		if (argsMap.get(ARG_PROPERTY_FILE_NAME) == null)
      		{
      		  argsMap.put(ARG_PROPERTY_FILE_NAME, args[++i]);
      		}
      		else
      		{
      		  System.err.println("Not allowed to specify \"createconfigfile\" and \"-propertyfile\" at the same time");
      		  System.err.println("Programm stopped");
      		  System.err.println();
      		  usage();
      		  System.exit(1);
      		}
      	}
      	else if (args[i].equalsIgnoreCase("createconfigfile"))
      	{
      		
      		argsMap.put(ARG_CREATE_CONFIG, new Boolean(true));
      		
      		if (argsMap.get(ARG_PROPERTY_FILE_NAME) == null)
      		{
      		  if (i < args.length)
      		  {
      		    argsMap.put(ARG_PROPERTY_FILE_NAME, args[++i]);
      		  }
      		  else
      		  {
      		  	System.err.println("<propertyfilename> must be specified");
      		    System.err.println("Programm stopped");
      		    System.err.println();
      		    usage();
      		    System.exit(1);
      		  }
      		  
      		  if (i < args.length-1)
      		  {
      		  	argsMap.put(ARG_OUTPUT_FILE_NAME, args[++i]);
      		  }
      		}
      		else
      		{
      		  System.err.println("Not allowed to specify \"createconfigfile\" and \"-propertyfile\" at the same time");
      		  System.err.println("Programm stopped");
      		  System.err.println();
      		  usage();
      		  System.exit(1);
      		}
      	}
        else
        {
          System.err.println("Undefined parameter \"" + args[i] + "\"");	
          System.err.println("Programm stopped");
      	  System.err.println();
      	  usage();
      	  System.exit(1);
        }
      }
      
      return argsMap;
   }


   static void usage()
   {
      System.out.println("Usage: ");
      System.out.println("Gui mode: \n"
         + "  java -DDBFORMS_HOME=/path/to/dbfhome org.dbforms.devgui.DevGui ");
      System.out.println("Gui mode with automatic loaded configfile: \n"
         + "  java org.dbforms.devgui.DevGui -propertyfile <propertyfilename>");
      System.out.println("Command line mode: \n"
         + "  java org.dbforms.devgui.DevGui createconfigfile <propertyfilename> [ <outputfilename> ]");
   }
}
