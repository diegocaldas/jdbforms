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

import org.dbforms.servlets.ConfigServlet;
import org.dbforms.taglib.DbFormTag;

/**
 * Tests of the <code>DbFormsConfig</code> class.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh </a>
 */
public class TestTableQuery extends org.apache.cactus.JspTestCase {
	private DbFormTag tag;

	/**
	 * In addition to creating the tag instance and adding the pageContext to
	 * it, this method creates a BodyContent object and passes it to the tag.
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void setUp() throws Exception {
		super.setUp();
		config.setInitParameter("dbformsConfig", "/WEB-INF/dbforms-config.xml");

		DbFormsConfig dbFormsConfig = null;

		try {
			dbFormsConfig = DbFormsConfigRegistry.instance().lookup();
		} catch (Exception e) {
		}

		if (dbFormsConfig == null) {
			config.setInitParameter("dbformsConfig",
					"/WEB-INF/dbforms-config.xml");

			ConfigServlet configServlet = new ConfigServlet();
			configServlet.init(config);
		}

		this.tag = new DbFormTag();
		this.tag.setPageContext(this.pageContext);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void testTable() throws Exception {
		this.tag.setTableName("BOOK");
		this.tag.setFilter("BOOK_ID=5,AUTHOR_ID=2");
		this.tag.doStartTag();
		assertEquals(1, this.tag.getResultSetVector().size());
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void testQuery() throws Exception {
		this.tag.setTableName("BOOKLISTPERAUTHOR");
		this.tag.setFilter("BOOK_ID=5,AUTHOR_ID=2");
		this.tag.doStartTag();
		assertEquals(1, this.tag.getResultSetVector().size());
	}

	public void testQuery2() throws Exception {
		this.tag.setTableName("BOOK_QUERY_WHERE");
		this.tag.setFilter("BOOK_ID=1");
		this.tag.doStartTag();
		assertEquals(1, this.tag.getResultSetVector().size());
	}

}