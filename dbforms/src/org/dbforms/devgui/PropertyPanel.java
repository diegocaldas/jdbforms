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
 * DbPanel.java
 *
 * Created on 26. April 2001, 15:42
 */
package org.dbforms.devgui;

import javax.swing.*;



/**
 * DOCUMENT ME!
 *
 * @author Joachim Peer
 * @version
 */
public abstract class PropertyPanel extends javax.swing.JPanel
   implements PropertyNames {
   /** DOCUMENT ME! */
   protected ProjectData projectData;

   /**
    * Creates a new PropertyPanel object.
    *
    * @param projectData DOCUMENT ME!
    */
   public PropertyPanel(ProjectData projectData) {
      this.projectData = projectData;

      //ToolTipManager.sharedInstance().registerComponent(this);
   }

   /**
    * DOCUMENT ME!
    *
    * @param projectData DOCUMENT ME!
    */
   public abstract void setNewProjectData(ProjectData projectData);


   /**
    * DOCUMENT ME!
    *
    * @param tf DOCUMENT ME!
    * @param p DOCUMENT ME!
    */
   protected void addAFocusListener(final JTextField tf,
                                    final String     p) {
      tf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) {
               projectData.setProperty(p, tf.getText());
            }
         });
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
}
