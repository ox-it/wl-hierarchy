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

import org.easymock.MockControl;
import org.sakaiproject.hierarchy.dao.HierarchyDao;
import org.sakaiproject.hierarchy.dao.model.HierarchyNodeMetaData;
import org.sakaiproject.hierarchy.impl.HierarchyServiceImpl;
import org.sakaiproject.hierarchy.impl.test.data.TestDataPreload;
import org.sakaiproject.hierarchy.model.HierarchyNode;
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
    private TestDataPreload tdp;

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

        // load up the test data preloader from spring
        tdp = (TestDataPreload) applicationContext.getBean("org.sakaiproject.hierarchy.test.data.TestDataPreload");
        if (tdp == null) {
            throw new NullPointerException("TestDatePreload could not be retrieved from spring context");
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

    public void testValidTestData() {
        // ensure the test data is setup the way we think
        assertEquals(new Long(1), tdp.pNode1.getId());
        assertEquals(new Long(6), tdp.pNode6.getId());
        assertEquals(new Long(9), tdp.pNode9.getId());
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#createHierarchy(java.lang.String)}.
     */
    public void testCreateHierarchy() {
        // test creating a valid hierarchy
        HierarchyNode node = hierarchyService.createHierarchy("hierarchyC");
        assertNotNull(node);
        assertEquals("hierarchyC", node.hierarchyId);
        assertNotNull(node.parentNodeIds);
        assertNotNull(node.childNodeIds);
        assertTrue(node.parentNodeIds.isEmpty());
        assertTrue(node.childNodeIds.isEmpty());

        // test creating a hierarchy that already exists
        try {
            hierarchyService.createHierarchy(TestDataPreload.HIERARCHYA);
            fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }

        // test creating a hierarchy with too long an id
        try {
            hierarchyService.createHierarchy("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
            fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }

    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#setHierarchyRootNode(java.lang.String, java.lang.String)}.
     */
    public void testSetHierarchyRootNode() {
        HierarchyNode node;

        // test reassigning existing rootnode is no problem
        node = hierarchyService.setHierarchyRootNode(TestDataPreload.HIERARCHYA, tdp.node1.id);
        assertNotNull(node);
        assertEquals(TestDataPreload.HIERARCHYA, node.hierarchyId);
        assertEquals(tdp.node1.id, node.id);

        // test reassigning a new node to be the parent node
        assertEquals(Boolean.FALSE, tdp.meta11.getIsRootNode());
        assertEquals(Boolean.TRUE, tdp.meta9.getIsRootNode());
        node = hierarchyService.setHierarchyRootNode(TestDataPreload.HIERARCHYB, tdp.node11.id);
        assertNotNull(node);
        assertEquals(TestDataPreload.HIERARCHYB, node.hierarchyId);
        assertEquals(tdp.node11.id, node.id);

        // test assigning a node which has parents causes failure
        try {
            hierarchyService.setHierarchyRootNode(TestDataPreload.HIERARCHYA, tdp.node3.id);
            fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }

        // test assigning a root node from another hierarchy to this root causes failure
        try {
            hierarchyService.setHierarchyRootNode(TestDataPreload.HIERARCHYB, tdp.node1.id);
            fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#destroyHierarchy(java.lang.String)}.
     */
    public void testDestroyHierarchy() {
        hierarchyService.destroyHierarchy(TestDataPreload.HIERARCHYB);
        int count = dao.countByProperties(HierarchyNodeMetaData.class, new String[] {"hierarchyId"}, new Object[] {TestDataPreload.HIERARCHYB});
        assertEquals(0, count);

        // test removing a non-existent hierarchy fails
        try {
            hierarchyService.destroyHierarchy(TestDataPreload.HIERARCHYB);
            fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#getRootLevelNode(java.lang.String)}.
     */
    public void testGetRootLevelNode() {
        HierarchyNode node = hierarchyService.getRootNode(TestDataPreload.HIERARCHYB);
        assertNotNull(node);
        assertEquals(tdp.node9, node);
        assertEquals(TestDataPreload.HIERARCHYB, node.hierarchyId);

        node = hierarchyService.getRootNode(TestDataPreload.HIERARCHYA);
        assertNotNull(node);
        assertEquals(tdp.node1, node);
        assertEquals(TestDataPreload.HIERARCHYA, node.hierarchyId);

        // fetching root from invalid hierarchy fails
        try {
            node = hierarchyService.getRootNode(TestDataPreload.INVALID_HIERARCHY);
            fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }        
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#getNodeById(java.lang.String)}.
     */
    public void testGetNodeById() {
        //fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#getChildNodes(java.lang.String, boolean)}.
     */
    public void testGetChildNodes() {
        //fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#getParentNodes(java.lang.String, boolean)}.
     */
    public void testGetParentNodes() {
        //fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#addNode(java.lang.String, java.lang.String)}.
     */
    public void testAddNode() {
        //fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#updateChildren(java.lang.String, java.util.Set)}.
     */
    public void testUpdateChildren() {
        //fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#updateParents(java.lang.String, java.util.Set)}.
     */
    public void testUpdateParents() {
        //fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#removeNode(java.lang.String)}.
     */
    public void testRemoveNode() {
        //fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link org.sakaiproject.hierarchy.impl.HierarchyServiceImpl#saveNodeMetaData(java.lang.String, java.lang.String, java.lang.String)}.
     */
    public void testSaveNodeMetaData() {
        //fail("Not yet implemented"); // TODO
    }

}
