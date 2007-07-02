/******************************************************************************
 * TestDataPreload.java - created by Sakai App Builder -AZ
 * 
 * Copyright (c) 2006 Sakai Project/Sakai Foundation
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.sakaiproject.hierarchy.impl.test.data;

import org.sakaiproject.genericdao.api.GenericDao;
import org.sakaiproject.hierarchy.dao.model.HierarchyNodeMetaData;
import org.sakaiproject.hierarchy.dao.model.HierarchyPersistentNode;

/**
 * Contains test data for preloading and test constants
 * @author Aaron Zeckoski (aaronz@vt.edu)
 */
public class TestDataPreload {

    /**
     * current user, access level user in LOCATION_ID1
     */
    public final static String USER_ID = "user-11111111";
    public final static String USER_DISPLAY = "Aaron Zeckoski";
    /**
     * access level user in LOCATION1_ID
     */
    public final static String ACCESS_USER_ID = "access-2222222";
    public final static String ACCESS_USER_DISPLAY = "Regular User";
    /**
     * maintain level user in LOCATION1_ID
     */
    public final static String MAINT_USER_ID = "maint-33333333";
    public final static String MAINT_USER_DISPLAY = "Maint User";
    /**
     * super admin user 
     */
    public final static String ADMIN_USER_ID = "admin";
    public final static String ADMIN_USER_DISPLAY = "Administrator";
    /**
     * Invalid user (also can be used to simulate the anonymous user) 
     */
    public final static String INVALID_USER_ID = "invalid-UUUUUU";

    /**
     * current location
     */
    public final static String LOCATION1_ID = "/site/ref-1111111";
    public final static String LOCATION1_TITLE = "Location 1 title";
    public final static String LOCATION2_ID = "/site/ref-22222222";
    public final static String LOCATION2_TITLE = "Location 2 title";
    public final static String INVALID_LOCATION_ID = "invalid-LLLLLLLL";

    // testing constants
    public final static String HIERARCHYA = "hierarchyA";
    public final static String HIERARCHYB = "hierarchyB";

    // testing data objects here
    public HierarchyPersistentNode root1 = new HierarchyPersistentNode(null, null, "2,3,4", "2,3,4,5,6,7");
    public HierarchyPersistentNode node2 = new HierarchyPersistentNode("1", "1", null, null);
    public HierarchyPersistentNode node3 = new HierarchyPersistentNode("1", "1", "5", "5");
    public HierarchyPersistentNode node4 = new HierarchyPersistentNode("1", "1", "6,7,8", "6,7,8");
    public HierarchyPersistentNode node5 = new HierarchyPersistentNode("3", "1,2,3", null, null);
    public HierarchyPersistentNode node6 = new HierarchyPersistentNode("4", "1,2,4", null, null);
    public HierarchyPersistentNode node7 = new HierarchyPersistentNode("4", "1,2,4", null, null);
    public HierarchyPersistentNode node8 = new HierarchyPersistentNode("4", "1,2,4", null, null);
    public HierarchyPersistentNode root9 = new HierarchyPersistentNode(null, null, "10", "10");
    public HierarchyPersistentNode node10 = new HierarchyPersistentNode("9", "9", null, null);

    public HierarchyNodeMetaData meta1 = new HierarchyNodeMetaData(root1, HIERARCHYA, Boolean.TRUE, USER_ID, "Univ of AZ", null);
    public HierarchyNodeMetaData meta2 = new HierarchyNodeMetaData(node2, HIERARCHYA, Boolean.FALSE, USER_ID, "College of Engineering", null);
    public HierarchyNodeMetaData meta3 = new HierarchyNodeMetaData(node3, HIERARCHYA, Boolean.FALSE, USER_ID, "College of Arts", null);
    public HierarchyNodeMetaData meta4 = new HierarchyNodeMetaData(node4, HIERARCHYA, Boolean.FALSE, USER_ID, "College of Science", null);
    public HierarchyNodeMetaData meta5 = new HierarchyNodeMetaData(node5, HIERARCHYA, Boolean.FALSE, USER_ID, "Dept of Art", null);
    public HierarchyNodeMetaData meta6 = new HierarchyNodeMetaData(node6, HIERARCHYA, Boolean.FALSE, USER_ID, "Dept of Math", null);
    public HierarchyNodeMetaData meta7 = new HierarchyNodeMetaData(node7, HIERARCHYA, Boolean.FALSE, USER_ID, "Dept of Physics", null);
    public HierarchyNodeMetaData meta8 = new HierarchyNodeMetaData(node8, HIERARCHYA, Boolean.FALSE, USER_ID, "Dept of Biology", null);
    public HierarchyNodeMetaData meta9 = new HierarchyNodeMetaData(root9, HIERARCHYB, Boolean.TRUE, USER_ID, "Univ of BZ", null);
    public HierarchyNodeMetaData meta10 = new HierarchyNodeMetaData(node10, HIERARCHYB, Boolean.FALSE, USER_ID, "College of BZ", null);

    /**
     * Preload a bunch of test data into the database
     * @param dao a generic dao
     */
    public void preloadTestData(GenericDao dao) {
        dao.save(root1);
        dao.save(node2);
        dao.save(node3);
        dao.save(node4);
        dao.save(node5);
        dao.save(node6);
        dao.save(node7);
        dao.save(node8);
        dao.save(root9);
        dao.save(node10);

        dao.save(meta1);
        dao.save(meta2);
        dao.save(meta3);
        dao.save(meta4);
        dao.save(meta5);
        dao.save(meta6);
        dao.save(meta7);
        dao.save(meta8);
        dao.save(meta9);
        dao.save(meta10);
    }

}
