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
 * Created on Jul 4, 2004
 *
 * Test per bean
 */
package contrib.ivan_codarin;


/**
 * @author ivan.codarin@amm.uniud.it
 *
 * Simple class to create background color.
 */
public class IvanBackgroudColor {
   private String BG_DARK;
   private String BG_LIGHT;
   private String bgColor;
   private int    idNumber;

   /**
    * @param ID_Number
    */
   public IvanBackgroudColor() {
      BG_LIGHT = "#EEEEEE";
      BG_DARK  = "#666666";
   }

   /**
    * @return Returns the bgColor.
    */
   public String getBgColor() {
      if ((this.idNumber % 2) == 1) {
         this.bgColor = BG_DARK;
      } else {
         this.bgColor = BG_LIGHT;
      }

      return bgColor;
   }


   /**
    * @param idNumber
    *            The idNumber to set.
    */
   public void setIdNumber(int idNumber) {
      this.idNumber = idNumber;
   }


   /**
    * @return Returns the idNumber.
    */
   public int getIdNumber() {
      return idNumber;
   }
}
