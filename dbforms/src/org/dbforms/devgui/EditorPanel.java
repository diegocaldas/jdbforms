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

import javax.swing.*;
import javax.swing.event.*;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class EditorPanel extends JPanel implements ActionListener,
                                                   DocumentListener {
   File      file;
   JButton   b_save;
   JLabel    l_fileName;
   JTextArea ta_editor;
   boolean   unsavedChanges = false;

   /**
    * Creates a new EditorPanel object.
    */
   public EditorPanel() {
      initComponents();
      doLayout();
   }

   /**
    * DOCUMENT ME!
    *
    * @param aFile DOCUMENT ME!
    */
   public void setFile(File aFile) {
      if (unsavedChanges) {
         int result = JOptionPane.showConfirmDialog(this,
                                                    this.file.getName()
                                                    + " is not saved. Want to save?",
                                                    "save file?",
                                                    JOptionPane.YES_NO_OPTION);

         if (result == JOptionPane.OK_OPTION) {
            saveFile(this.file);
            this.unsavedChanges = false;
         }
      }

      this.file = aFile;
      this.l_fileName.setText(file.getAbsolutePath());
      this.ta_editor.setText(readFile(file));
      this.ta_editor.setCaretPosition(0);
      this.ta_editor.getDocument()
                    .addDocumentListener(this);
      this.unsavedChanges = false;

      //System.out.println("this.unsavedChanges = "+unsavedChanges);
   }


   /**
    * DOCUMENT ME!
    *
    * @param e DOCUMENT ME!
    */
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == b_save) {
         saveFile(this.file);
         this.unsavedChanges = false;
      }
   }


   /**
    * DOCUMENT ME!
    *
    * @param evt DOCUMENT ME!
    */
   public void changedUpdate(DocumentEvent evt) {
   }


   /**
    * DOCUMENT ME!
    *
    * @param evt DOCUMENT ME!
    */
   public void insertUpdate(DocumentEvent evt) {
      unsavedChanges = true;
   }


   /**
    * DOCUMENT ME!
    *
    * @param evt DOCUMENT ME!
    */
   public void removeUpdate(DocumentEvent evt) {
      unsavedChanges = true;
   }


   /**
    * DOCUMENT ME!
    *
    * @param e DOCUMENT ME!
    */
   protected void showExceptionDialog(Exception e) {
      JOptionPane.showMessageDialog(this,
                                    "An exception occurred:\n\n" + e.toString()
                                    + "\n", "Exception",
                                    JOptionPane.ERROR_MESSAGE);
   }


   private void initComponents() {
      this.setLayout(new BorderLayout());
      l_fileName = new JLabel();

      add(BorderLayout.NORTH, l_fileName);

      ta_editor = new JTextArea();
      add(BorderLayout.CENTER, new JScrollPane(ta_editor));

      JPanel panel_buttons = new JPanel();
      panel_buttons.setLayout(new BorderLayout());
      b_save = new JButton("Save File");
      b_save.addActionListener(this);
      panel_buttons.add(BorderLayout.CENTER, b_save);

      add(BorderLayout.SOUTH, panel_buttons);
   }


   private String readFile(File file) {
      StringBuffer result = new StringBuffer();

      try {
         BufferedReader reader = new BufferedReader(new FileReader(file));

         String         s = null;

         while ((s = reader.readLine()) != null) {
            result.append(s);
            result.append("\n");
         }

         reader.close();
      } catch (IOException ioe) {
         showExceptionDialog(ioe);
      }

      return result.toString();
   }


   private void saveFile(File file) {
      try {
         FileWriter fw = new FileWriter(file);
         fw.write(ta_editor.getText());
         fw.flush();
         fw.close();
         System.out.println("saved.");
      } catch (IOException ioe) {
         showExceptionDialog(ioe);
      }
   }
}
