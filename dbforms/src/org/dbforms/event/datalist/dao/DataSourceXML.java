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

package org.dbforms.event.datalist.dao;
import org.apache.log4j.Category;
import java.util.Vector;
import java.sql.SQLException;
import java.net.URI;
import java.net.URLConnection;
import java.io.InputStream;

//	Imported JAVA API for XML Parsing 1.0 classes
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;

//	Imported dom classes
import org.w3c.dom.Document;
import org.w3c.dom.Node;

// apache logging
//import org.apache.log4j.Category;
import org.dbforms.config.DbFormsConfigRegistry;
import org.dbforms.config.Field;
import org.dbforms.config.Table;
import org.dbforms.util.Constants;
import org.dbforms.util.FieldValue;
import org.dbforms.util.Util;



/**
 * Special implementation of DataSource. This class deals with xml data
 * 
 * @author hkk
 */
public class DataSourceXML extends DataSource
{
   private Category logCat = Category.getInstance(this.getClass().getName());
   private static DocumentBuilderFactory dfactory = null;
   private static DocumentBuilder builder         = null;
   private FieldValue[] filterConstraint;
   private FieldValue[] orderConstraint;
   private XMLDataResult data;
   private String[] keys;
   private Object[][] dataObject;

   /**
    * Contructor
    * 
    * @param table to set
    */
   public DataSourceXML(Table table)
   {
      super(table);

      if (dfactory == null)
      {
         try
         {
            dfactory = DocumentBuilderFactory.newInstance();
            dfactory.setValidating(false);
            dfactory.setNamespaceAware(false);
            builder = dfactory.newDocumentBuilder();
         }
         catch (Exception e)
         {
            logCat.error(e);
         }
      }
   }

   /**
    * Set the filterConstraint and orderConstraint used to build the SQL Select
    * condition.
    * 
    * @param filterConstraint FieldValue array used to build a cumulation of
    *        rules for filtering fields.
    * @param orderConstraint  FieldValue array used to build a cumulation of
    *        rules for ordering (sorting) and restricting fields.
    * @param sqlFilter       sql condition to add to where clause
    */
   public void setSelect(FieldValue[] filterConstraint, 
                         FieldValue[] orderConstraint,
                         String sqlFilter)
   {
      this.filterConstraint = filterConstraint;
      this.orderConstraint  = orderConstraint;
      // FIXME find a way to transform sqlFilter in a XPath query
   }


   /**
    * DOCUMENT ME!
    * 
    * @throws SQLException DOCUMENT ME!
    */
   protected final void open() throws SQLException
   {
      try
      {
         String qry = getXPath();
         URI    url = null;

         // Check if we got a full URI
         try
         {
            url = new URI(qry);
         }
         catch (Exception e)
         {
            logCat.error(e);
         }

         // No valid URI given, put query into to qeury part of the 
         // URI object
         if (url == null)
         {
            try
            {
               url = new URI(null, null, null, qry, null);
            }
            catch (Exception e)
            {
               logCat.error(e);
            }
         }

         data = new XMLDataResult(getResultNode(url), url.getQuery());
      }
      catch (Exception e)
      {
         logCat.error(e);
      }

      keys       = new String[size()];
      dataObject = new Object[size()][];
   }


   /**
    * DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    * 
    * @throws SQLException DOCUMENT ME!
    */
   protected final int size() throws SQLException
   {
      int res = 0;

      if (data != null)
      {
         res = data.size();
      }

      return res;
   }

	/**
	 * return true if there are more records to fetch then the given record number
	 *
	 * @param i index of last fetched row.
	 *  
	 * @return true if there are more records to fetch then the given record number
	 * 
	 * @throws SQLException
	 */
	protected boolean hasMore(int i) throws SQLException 
	{
		return (i < size());
	}

   /**
    * DOCUMENT ME!
    * 
    * @param startRow DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    * 
    * @throws SQLException DOCUMENT ME!
    */
   protected final int findStartRow(String startRow) throws SQLException
   {
      for (int i = 0; i < size(); i++)
      {
         if (getKey(i).equals(startRow))
         {
            return i;
         }
      }

      return 0;
   }


   /**
    * DOCUMENT ME!
    * 
    * @param currRow DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    * 
    * @throws SQLException DOCUMENT ME!
    */
   protected final Object[] getRow(int currRow) throws SQLException
   {
      return getValuesAsObject(currRow);
   }


   /**
    * DOCUMENT ME!
    * 
    * @param uri DOCUMENT ME!
    * 
    * @return DOCUMENT ME!
    * 
    * @throws Exception DOCUMENT ME!
    */
   protected Node getResultNode(URI uri) throws Exception
   {
      URLConnection con = uri.toURL().openConnection();
      con.connect();

      InputStream in  = con.getInputStream();
      InputSource src = new InputSource(in);
      Document    doc = builder.parse(src);

      return doc;
   }


   private String getXPath() throws Exception
   {
      StringBuffer buf = new StringBuffer();
      buf.append(Util.replaceRealPath(getTable().getAlias(), 
                                      DbFormsConfigRegistry.instance().lookup()));

      if (filterConstraint != null)
      {
         buf.append("[");

         for (int i = 0; i < filterConstraint.length; i++)
         {
            if (i != 0)
            {
               if (filterConstraint[i].isLogicalOR())
               {
                  buf.append(" or ");
               }
               else
               {
                  buf.append(" and ");
               }
            }

            Field f = filterConstraint[i].getField();
            buf.append(Util.isNull(f.getExpression())
                          ? f.getName() : f.getExpression());

            // Check what type of operator is required
            switch (filterConstraint[i].getOperator())
            {
               case Constants.FILTER_EQUAL:
                  buf.append("=");

                  break;

               case Constants.FILTER_NOT_EQUAL:
                  buf.append("!=");

                  break;

               case Constants.FILTER_GREATER_THEN:
                  buf.append("&gt;");

                  break;

               case Constants.FILTER_SMALLER_THEN:
                  buf.append("&lt;");

                  break;

               case Constants.FILTER_GREATER_THEN_EQUAL:
                  buf.append("&gt;=");

                  break;

               case Constants.FILTER_SMALLER_THEN_EQUAL:
                  buf.append("&lt;=");

                  break;
            }

            buf.append("\"");
            buf.append(filterConstraint[i].getFieldValue());
            buf.append("\"");
         }

         buf.append("]");
      }

      return buf.toString();
   }


   private String getKey(int index) throws SQLException
   {
      if (keys[index] == null)
      {
         Vector   fields    = getTable().getFields();
         String[] objectRow = new String[fields.size()];

         for (int i = 0; i < fields.size(); i++)
         {
            Field f = (Field) fields.elementAt(i);
            objectRow[i] = data.itemValue(index, 
                                          Util.isNull(f.getExpression())
                                             ? f.getName() : f.getExpression());
         }

         keys[index] = getTable().getKeyPositionString(objectRow);
      }

      return keys[index];
   }


   private Object[] getValuesAsObject(int index) throws SQLException
   {
      if ((index < 0) || (index >= dataObject.length))
      {
         return null;
      }

      if (dataObject[index] == null)
      {
         Vector   fields    = getTable().getFields();
         Object[] objectRow = new Object[fields.size()];

         for (int i = 0; i < fields.size(); i++)
         {
            Field f = (Field) fields.elementAt(i);
            objectRow[i] = data.itemValue(index, 
                                          Util.isNull(f.getExpression())
                                             ? f.getName() : f.getExpression(), 
                                          f.getType());
         }

         dataObject[index] = objectRow;
      }

      return dataObject[index];
   }
}