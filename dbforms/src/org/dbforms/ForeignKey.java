/*
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

package org.dbforms;

public class ForeignKey {
    
    /** Holds value of property foreignTable. */
    private String foreignTable;
    
    /** Holds value of property name. */
    private String name;
    
    /** Holds value of property visibleFields. */
    private String visibleFields;
    
    /** Holds value of property format. */
    private String format;
    
    /** Holds value of property referencesVector. */
    private java.util.Vector referencesVector;
    
    /** Creates a new instance of ForeignKeyInfo */
    public ForeignKey() {
        referencesVector = new java.util.Vector();
    }
    
    public void addReference (Reference ref) {
         referencesVector.add(ref);
    }
    
    /** Getter for property foreignTable.
     * @return Value of property foreignTable.
     *
     */
    public String getForeignTable() {
        return this.foreignTable;
    }
    
    /** Setter for property foreignTable.
     * @param foreignTable New value of property foreignTable.
     *
     */
    public void setForeignTable(String foreignTable) {
        this.foreignTable = foreignTable;
    }
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public String getName() {
        return this.name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /** Getter for property visibleFields.
     * @return Value of property visibleFields.
     *
     */
    public String getVisibleFields() {
        return this.visibleFields;
    }
    
    /** Setter for property visibleFields.
     * @param visibleFields New value of property visibleFields.
     *
     */
    public void setVisibleFields(String visibleFields) {
        this.visibleFields = visibleFields;
    }
    
    /** Getter for property format.
     * @return Value of property format.
     *
     */
    public String getFormat() {
        return this.format;
    }
    
    /** Setter for property format.
     * @param format New value of property format.
     *
     */
    public void setFormat(String format) {
        this.format = format;
    }
    
    /** Getter for property referencesVector.
     * @return Value of property referencesVector.
     *
     */
    public java.util.Vector getReferencesVector() {
        return this.referencesVector;
    }
}
