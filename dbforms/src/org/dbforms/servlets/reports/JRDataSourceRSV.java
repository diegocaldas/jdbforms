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

package org.dbforms.servlets.reports;

/**
 */
import dori.jasper.engine.JRDataSource;
import dori.jasper.engine.JRException;
import dori.jasper.engine.JRField;

import org.dbforms.config.ResultSetVector;
import org.dbforms.util.Util;



/**
 * DOCUMENT ME!
 * 
 * @version $Revision$
 * @author $author$
 */
public class JRDataSourceRSV implements JRDataSource
{
   private ResultSetVector rsv;

   /**
    * Constructor for JRDataSourceRSV.
    * @param rsv DOCUMENT ME!
    */
   public JRDataSourceRSV(ResultSetVector rsv)
   {
      this.rsv = rsv;
      this.rsv.setPointer(-1);
   }

   private Object getFieldValue(String fieldName)
   {
      Object o = null;

      try
      {
         o = rsv.getFieldAsObject(fieldName);
      }
      catch (Exception e)
      {
         ;
      }

      return o;
   }


   /**
    * @see dori.jasper.engine.JRDataSource#getFieldValue(dori.jasper.engine.JRField)
    */
   public Object getFieldValue(JRField field) throws JRException
   {
      Object o;
      o = getFieldValue(field.getName());

      if (o == null)
      {
         o = getFieldValue(field.getName().toUpperCase());
      }

      if (o == null)
      {
         o = getFieldValue(field.getName().toLowerCase());
      }
/*
      // Try class conversation if the classes do not match!
      if ((o != null) && (o.getClass() != field.getValueClass()))
      {
         try
         {
            Object[] constructorArgs      = new Object[] 
            {
               o.toString()
            };
            Class[]  constructorArgsTypes = new Class[] 
            {
               String.class
            };
            o = ReflectionUtil.newInstance(field.getValueClass(), 
                                           constructorArgsTypes, 
                                           constructorArgs);
         }
         catch (Exception e)
         {
            ;
         }
      }
*/
      return o;
   }


   /**
    * @see dori.jasper.engine.JRDataSource#next()
    */
   public boolean next() throws JRException
   {
      if (ResultSetVector.isNull(rsv))
      {
         return false;
      }

      if (rsv.getPointer() == (rsv.size() - 1))
      {
         return false;
      }

      rsv.increasePointer();

      return true;
   }
}