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



/**
 * Tests of the <code>GrantedPrivileges</code> class.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 *
 */
public class TestGrantedPrivileges extends ServletTestCase
{

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void testOpenAccess() throws Exception
    {
        GrantedPrivileges gp = new GrantedPrivileges();
        assertTrue("Should be wideopen", gp.hasUserPrivileg(request, GrantedPrivileges.PRIVILEG_SELECT));
        assertTrue("Should be wideopen", gp.hasUserPrivileg(request, GrantedPrivileges.PRIVILEG_INSERT));
        assertTrue("Should be wideopen", gp.hasUserPrivileg(request, GrantedPrivileges.PRIVILEG_UPDATE));
        assertTrue("Should be wideopen", gp.hasUserPrivileg(request, GrantedPrivileges.PRIVILEG_DELETE));
    }


    /**
     * Note: this only tests unauthenticated users..  To get a positive result, you must log in first,
     * which this does not do!
     */
    public void testOnlySelectAccessRestricted() throws Exception
    {
        GrantedPrivileges gp = new GrantedPrivileges();
        gp.setSelect("bug_admin,bob_owens");
        assertTrue("Should be closed", !gp.hasUserPrivileg(request, GrantedPrivileges.PRIVILEG_SELECT));
        assertTrue("Should be wideopen", gp.hasUserPrivileg(request, GrantedPrivileges.PRIVILEG_INSERT));
        assertTrue("Should be wideopen", gp.hasUserPrivileg(request, GrantedPrivileges.PRIVILEG_UPDATE));
        assertTrue("Should be wideopen", gp.hasUserPrivileg(request, GrantedPrivileges.PRIVILEG_DELETE));
    }


    /**
     * Note: this only tests unauthenticated users..  To get a positive result, you must log in first,
     * which this does not do!
     */
    public void testNoAccessAvailable() throws Exception
    {
        GrantedPrivileges gp = new GrantedPrivileges();
        gp.setSelect("bug_admin,bob_owens");
        gp.setInsert("bug_admin,bob_owens");
        gp.setUpdate("bug_admin,bob_owens");
        gp.setDelete("bug_admin,bob_owens");
        assertTrue("Should be closed", !gp.hasUserPrivileg(request, GrantedPrivileges.PRIVILEG_SELECT));
        assertTrue("Should be closed", !gp.hasUserPrivileg(request, GrantedPrivileges.PRIVILEG_INSERT));
        assertTrue("Should be closed", !gp.hasUserPrivileg(request, GrantedPrivileges.PRIVILEG_UPDATE));
        assertTrue("Should be closed", !gp.hasUserPrivileg(request, GrantedPrivileges.PRIVILEG_DELETE));
    }


}
