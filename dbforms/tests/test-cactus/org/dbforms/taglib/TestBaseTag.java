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

package org.dbforms.taglib;

import org.dbforms.util.AssertUtils;
import javax.servlet.jsp.tagext.BodyTag;
import org.apache.cactus.JspTestCase;
import org.apache.cactus.WebResponse;

/**
 * Tests of the <code>BaseTag</code> class.
 * 
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh </a>
 *  
 */
public class TestBaseTag extends JspTestCase {
	private BaseTag tag;

	/**
	 * In addition to creating the tag instance and adding the pageContext to
	 * it, this method creates a BodyContent object and passes it to the tag.
	 */
	public void setUp() throws Exception {
		super.setUp();

		this.tag = new BaseTag();
		this.tag.setPageContext(this.pageContext);
	}

	//-------------------------------------------------------------------------

	/**
	 * Sets the replacement target and replacement String on the tag, then calls
	 * doAfterBody(). Most of the assertion work is done in endReplacement().
	 */
	public void testBasicBaseTag() throws Exception {
		//none of the other life cycle methods need to be implemented, so they
		//do not need to be called.
		int result = this.tag.doStartTag();
		assertEquals(BodyTag.EVAL_BODY_INCLUDE, result);
	}

	/**
	 * Verifies that the target String has indeed been replaced in the tag's
	 * body.
	 */
	public void endBasicBaseTag(WebResponse theResponse) {
		String content = theResponse.getText();
		AssertUtils.assertContains("base href", content);
		AssertUtils.assertContains("http://localhost:8080/", content);
	}
}
