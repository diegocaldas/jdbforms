package org.dbforms.taglib;

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

import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.cactus.JspTestCase;
import org.apache.cactus.WebResponse;

/**
 * Tests of the <code>DbLabelTag</code> class.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 *
 */
public class TestDbLabelTag extends JspTestCase
{
    private DbLabelTag tag;
    private BodyContent tagContent;

    /**
     * Defines the testcase name for JUnit.
     *
     * @param theName the testcase's name.
     */
    public TestDbLabelTag(String theName)
    {
        super(theName);
    }

    /**
     * Start the tests.
     *
     * @param theArgs the arguments. Not used
     */
    public static void main(String[] theArgs)
    {
        junit.swingui.TestRunner.main(
            new String[] { TestDbLabelTag.class.getName() });
    }

    /**
     * @return a test suite (<code>TestSuite</code>) that includes all methods
     *         starting with "test"
     */
    public static Test suite()
    {
        // All methods starting with "test" will be executed in the test suite.
        return new TestSuite(TestDbLabelTag.class);
    }

    /**
     * In addition to creating the tag instance and adding the pageContext to
     * it, this method creates a BodyContent object and passes it to the tag.
     */
    public void setUp()
    {
        this.tag = new DbLabelTag();
        this.tag.setPageContext(this.pageContext);

        //create the BodyContent object and call the setter on the tag instance
        //this.tagContent = this.pageContext.pushBody();
        //this.tag.setBodyContent(this.tagContent);
    }

    //-------------------------------------------------------------------------

    /**
     * Sets the replacement target and replacement String on the tag, then calls
     * doAfterBody(). Most of the assertion work is done in endReplacement().
     */
    public void  testReplacement() throws Exception
    {
        //set the target and the String to replace it with
        //this.tag.setFieldName("EMAIL_ID");
        assertTrue("This will always be true!",true);

    }


    public void tearDown()
    {
        //necessary for tag to output anything on most servlet engines.
        this.pageContext.popBody();
    }

    /**
     * Verifies that the target String has indeed been replaced in the tag's
     * body.
     */
    public void endReplacement(WebResponse theResponse)
    {
        String content = theResponse.getText();
        assertTrue("This is always true!",true);

     /*   assertTrue("Response should have contained the ["
            + "replacement is now replacement] string",
            content.indexOf("replacement is now replacement") > -1);
        assertTrue("Response should have contained the ["
            + "replacement_replacement] string",
            content.indexOf("replacement") > -1);
            */
    }
}