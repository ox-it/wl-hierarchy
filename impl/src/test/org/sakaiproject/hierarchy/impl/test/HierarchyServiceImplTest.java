/******************************************************************************
 * HierarchyServiceImplTest.java - created by aaronz on Jul 1, 2007
 * 
 * Copyright (c) 2007 Centre for Academic Research in Educational Technologies
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.sakaiproject.hierarchy.impl.test;

import junit.framework.Assert;

import org.easymock.MockControl;
import org.sakaiproject.hierarchy.dao.HierarchyDao;
import org.sakaiproject.hierarchy.impl.HierarchyServiceImpl;
import org.sakaiproject.hierarchy.impl.test.data.TestDataPreload;
import org.sakaiproject.tool.api.SessionManager;
import org.springframework.test.AbstractTransactionalSpringContextTests;

/**
 * Testing the hierarchy service
 * 
 * @author Aaron Zeckoski (aaronz@vt.edu)
 */
public class HierarchyServiceImplTest extends AbstractTransactionalSpringContextTests {

    protected HierarchyServiceImpl hierarchyService;

    private HierarchyDao dao;

    private TestDataPreload tdp = new TestDataPreload();

    private SessionManager sessionManager;
    private MockControl sessionManagerControl;

    protected String[] getConfigLocations() {
        // point to the needed spring config files, must be on the classpath
        // (add component/src/webapp/WEB-INF to the build path in Eclipse),
        // they also need to be referenced in the project.xml file
        return new String[] {"hibernate-test.xml", "spring-hibernate.xml"};
    }

    // run this before each test starts
    protected void onSetUpBeforeTransaction() throws Exception {
        // load the spring created dao class bean from the Spring Application Context
        dao = (HierarchyDao) applicationContext.getBean("org.sakaiproject.hierarchy.dao.HierarchyDao");
        if (dao == null) {
            throw new NullPointerException("Dao could not be retrieved from spring context");
        }

        // load up any other needed spring beans

        // setup the mock objects if needed
        sessionManagerControl = MockControl.createControl(SessionManager.class);
        sessionManager = (SessionManager) sessionManagerControl.getMock();

        //this mock object is simply keeping us from getting a null when getCurrentSessionUserId is called 
        sessionManager.getCurrentSessionUserId(); // expect this to be called
        sessionManagerControl.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
        sessionManagerControl.setReturnValue(TestDataPreload.USER_ID, MockControl.ZERO_OR_MORE);
        sessionManagerControl.replay();

        //create and setup the object to be tested
        hierarchyService = new HierarchyServiceImpl();
        hierarchyService.setDao(dao);
        hierarchyService.setSessionManager(sessionManager);

        // preload the DB for testing
        tdp.preloadTestData(dao);
    }

    // run this before each test starts and as part of the transaction
    protected void onSetUpInTransaction() {
        // preload additional data if desired
    }

    /**
     * ADD unit tests below here, use testMethod as the name of the unit test,
     * Note that if a method is overloaded you should include the arguments in the
     * test name like so: testMethodClassInt (for method(Class, int);
     */

    public void testValidTestDate() {
        // ensure the test data is setup the way we think
        assertEquals(new Long(1), tdp.root1.getId());
        assertEquals(new Long(9), tdp.root9.getId());
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#createHierarchy(java.lang.String)}.
     */
    public void testCreateHierarchy() {
        // test creating a valid hierarchy
        hierarchyService.createHierarchy("hierarchyC");

        // test creating a hierarchy that already exists
        try {
            hierarchyService.createHierarchy(TestDataPreload.HIERARCHYA);
            Assert.fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
            Assert.assertNotNull(e);
        }

        // test creating a hierarchy with too long a name
        try {
            hierarchyService.createHierarchy("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
            Assert.fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
            Assert.assertNotNull(e);
        }

    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#setHierarchyRootNode(java.lang.String, java.lang.String)}.
     */
    public void testSetHierarchyRootNode() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#destroyHierarchy(java.lang.String)}.
     */
    public void testDestroyHierarchy() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#getRootLevelNode(java.lang.String)}.
     */
    public void testGetRootLevelNode() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#getNodeById(java.lang.String)}.
     */
    public void testGetNodeById() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#getChildNodes(java.lang.String, boolean)}.
     */
    public void testGetChildNodes() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#getParentNodes(java.lang.String, boolean)}.
     */
    public void testGetParentNodes() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#addNode(java.lang.String, java.lang.String)}.
     */
    public void testAddNode() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#updateChildren(java.lang.String, java.util.Set)}.
     */
    public void testUpdateChildren() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#updateParents(java.lang.String, java.util.Set)}.
     */
    public void testUpdateParents() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#removeNode(java.lang.String)}.
     */
    public void testRemoveNode() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#saveNodeMetaData(java.lang.String, java.lang.String, java.lang.String)}.
     */
    public void testSaveNodeMetaData() {
        fail("Not yet implemented"); // TODO
    }

}
