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

package org.dbforms.config;

import org.apache.cactus.ServletTestCase;
import org.dbforms.servlets.ConfigServlet;

import java.sql.Connection;

/**
 * Tests of the <code>DbFormsConfig</code> class.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh </a>
 */
public class TestDbFormsConfig extends ServletTestCase {
	private DbFormsConfig dbFormsConfig = null;

	/**
	 * In addition to creating the tag instance and adding the pageContext to
	 * it, this method creates a BodyContent object and passes it to the tag.
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void setUp() throws Exception {
		config.setInitParameter("dbformsConfig", "/WEB-INF/dbforms-config.xml");
		dbFormsConfig = null;
		try {
			dbFormsConfig = DbFormsConfigRegistry.instance().lookup();
		} catch (Exception e) {
		}
		if (dbFormsConfig == null) {
			config.setInitParameter("dbformsConfig",
					"/WEB-INF/dbforms-config.xml");
			ConfigServlet configServlet = new ConfigServlet();
			configServlet.init(config);
			dbFormsConfig = DbFormsConfigRegistry.instance().lookup();
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void testTables() throws Exception {
		Table tbl = dbFormsConfig.getTableByName("AUTHOR");
		assertTrue("Found tblAuthor", tbl.getName().equals("AUTHOR"));
		assertTrue("Found tblBook", dbFormsConfig.getTableByName("BOOK")
				.getName().equals("BOOK"));
		assertTrue("Make sure table names ARE casesensitve", dbFormsConfig
				.getTableByName("book") == null);
		Query qry = (Query) dbFormsConfig.getTableByName("BOOKLISTPERAUTHOR");
		assertTrue("Found BOOKLISTPERAUTHOR", qry.getName().equals(
				"BOOKLISTPERAUTHOR"));
		assertTrue("BOOKLISTPERAUTHOR has set distinct", qry.hasDistinctSet());
	}

	public void testFieldParameter() throws Exception {
		Table tbl = dbFormsConfig.getTableByName("AUTHOR");
		assertTrue("Found tblAuthor", tbl.getName().equals("AUTHOR"));
		Field field = tbl.getFieldByName("AUTHOR_ID");
		assertTrue("Found field AUTHOR_ID", field != null);
		assertTrue("Make sure field names names ARE casesensitve",
				dbFormsConfig.getTableByName("author_id") == null);
		assertTrue("field is key field", field.hasIsKeySet());
		assertTrue("field is autoinc", field.hasAutoIncSet());
		field = tbl.getFieldByName("NAME");
		assertTrue("Found field NAME", field != null);
		assertTrue("field is sortable", field.hasSortableSet());

		tbl = dbFormsConfig.getTableByName("BLOBTEST");
		assertTrue("Found BLOBTEST", tbl.getName().equals("BLOBTEST"));
		field = tbl.getFieldByName("FILE");
		assertTrue("Found field FILE", field != null);
		assertTrue("field is encoded", field.hasEncodedSet());

	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void testAddTable() throws Exception {
		Table newTable = new Table();
		newTable.setName("NEW_TABLE");
		dbFormsConfig.addTable(newTable);

		assertTrue("Found NEW_TABLE", dbFormsConfig.getTableByName("NEW_TABLE")
				.getName().equals("NEW_TABLE"));
	}

	public void testGetConnection() throws Exception {
		Connection con = dbFormsConfig.getConnection();
		assertTrue("default not found", con != null);
		con = dbFormsConfig.getConnection("asoexdb");
		assertTrue("asoexdb", con != null);
	}

}