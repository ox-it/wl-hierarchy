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

import java.util.Set;
import java.util.TreeSet;

import org.sakaiproject.genericdao.api.GenericDao;
import org.sakaiproject.hierarchy.dao.model.HierarchyNodeMetaData;
import org.sakaiproject.hierarchy.dao.model.HierarchyPersistentNode;
import org.sakaiproject.hierarchy.impl.utils.HierarchyUtils;
import org.sakaiproject.hierarchy.model.HierarchyNode;

/**
 * Contains test data for preloading and test constants
 * @author Aaron Zeckoski (aaronz@vt.edu)
 */
public class TestDataPreload {

    public GenericDao dao;
    public void setDao(GenericDao dao) {
        this.dao = dao;
    }

    public void init() {
        preloadTestData(dao);
    }

    /**
     * current user, access level user in LOCATION1_ID
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
    public final static String INVALID_HIERARCHY = "hierarchy-invalid";
    public final static String INVALID_NODE_ID = "invalid-nodeID";

    // testing data objects here
    public HierarchyPersistentNode pNode1 = new HierarchyPersistentNode(null, null, toCode("2,3,4"), toCode("2,3,4,5,6,7,8") );
    public HierarchyPersistentNode pNode2 = new HierarchyPersistentNode(toCode("1"), toCode("1"), null, null );
    public HierarchyPersistentNode pNode3 = new HierarchyPersistentNode(toCode("1"), toCode("1"), toCode("5"), toCode("5") );
    public HierarchyPersistentNode pNode4 = new HierarchyPersistentNode(toCode("1"), toCode("1"), toCode("6,7,8"), toCode("6,7,8") );
    public HierarchyPersistentNode pNode5 = new HierarchyPersistentNode(toCode("3"), toCode("1,3"), null, null );
    public HierarchyPersistentNode pNode6 = new HierarchyPersistentNode(toCode("4"), toCode("1,4"), null, null );
    public HierarchyPersistentNode pNode7 = new HierarchyPersistentNode(toCode("4"), toCode("1,4"), null, null );
    public HierarchyPersistentNode pNode8 = new HierarchyPersistentNode(toCode("4"), toCode("1,4"), null, null );
    public HierarchyPersistentNode pNode9 = new HierarchyPersistentNode(null, null, toCode("10"), toCode("10") );
    public HierarchyPersistentNode pNode10 = new HierarchyPersistentNode(toCode("9,11"), toCode("9,11"), null, null );
    public HierarchyPersistentNode pNode11 = new HierarchyPersistentNode(null, null, toCode("10"), toCode("10") );

    public HierarchyNodeMetaData meta1 = new HierarchyNodeMetaData(pNode1, HIERARCHYA, Boolean.TRUE, USER_ID, "Univ of AZ", null);
    public HierarchyNodeMetaData meta2 = new HierarchyNodeMetaData(pNode2, HIERARCHYA, Boolean.FALSE, USER_ID, "College of Engineering", null);
    public HierarchyNodeMetaData meta3 = new HierarchyNodeMetaData(pNode3, HIERARCHYA, Boolean.FALSE, USER_ID, "College of Arts", null);
    public HierarchyNodeMetaData meta4 = new HierarchyNodeMetaData(pNode4, HIERARCHYA, Boolean.FALSE, USER_ID, "College of Science", null);
    public HierarchyNodeMetaData meta5 = new HierarchyNodeMetaData(pNode5, HIERARCHYA, Boolean.FALSE, USER_ID, "Dept of Art", null);
    public HierarchyNodeMetaData meta6 = new HierarchyNodeMetaData(pNode6, HIERARCHYA, Boolean.FALSE, USER_ID, "Dept of Math", null);
    public HierarchyNodeMetaData meta7 = new HierarchyNodeMetaData(pNode7, HIERARCHYA, Boolean.FALSE, USER_ID, "Dept of Physics", null);
    public HierarchyNodeMetaData meta8 = new HierarchyNodeMetaData(pNode8, HIERARCHYA, Boolean.FALSE, USER_ID, "Dept of Biology", null);
    public HierarchyNodeMetaData meta9 = new HierarchyNodeMetaData(pNode9, HIERARCHYB, Boolean.TRUE, USER_ID, "Univ of BZ", null);
    public HierarchyNodeMetaData meta10 = new HierarchyNodeMetaData(pNode10, HIERARCHYB, Boolean.FALSE, USER_ID, "College of BZ", null);
    public HierarchyNodeMetaData meta11 = new HierarchyNodeMetaData(pNode11, HIERARCHYB, Boolean.FALSE, USER_ID, "Provost of BZ", null);

    public HierarchyNode node1;
    public HierarchyNode node2;
    public HierarchyNode node3;
    public HierarchyNode node4;
    public HierarchyNode node5;
    public HierarchyNode node6;
    public HierarchyNode node7;
    public HierarchyNode node8;
    public HierarchyNode node9;
    public HierarchyNode node10;
    public HierarchyNode node11;


    /**
     * Preload a bunch of test data into the database
     * @param dao a generic dao
     */
    public boolean preloaded = false;
    public void preloadTestData(GenericDao dao) {
        dao.save(pNode1);
        dao.save(pNode2);
        dao.save(pNode3);
        dao.save(pNode4);
        dao.save(pNode5);
        dao.save(pNode6);
        dao.save(pNode7);
        dao.save(pNode8);
        dao.save(pNode9);
        dao.save(pNode10);
        dao.save(pNode11);

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
        dao.save(meta11);

        node1 = HierarchyUtils.makeNode(pNode1, meta1);
        node2 = HierarchyUtils.makeNode(pNode2, meta2);
        node3 = HierarchyUtils.makeNode(pNode3, meta3);
        node4 = HierarchyUtils.makeNode(pNode4, meta4);
        node5 = HierarchyUtils.makeNode(pNode5, meta5);
        node6 = HierarchyUtils.makeNode(pNode6, meta6);
        node7 = HierarchyUtils.makeNode(pNode7, meta7);
        node8 = HierarchyUtils.makeNode(pNode8, meta8);
        node9 = HierarchyUtils.makeNode(pNode9, meta9);
        node10 = HierarchyUtils.makeNode(pNode10, meta10);
        node11 = HierarchyUtils.makeNode(pNode11, meta11);

        preloaded = true;
    }

    /**
     * Encode the comma delimited list of nodes
     * @param commaDelimitedNums
     * @return an encoded string
     */
    private String toCode(String commaDelimitedNums) {
        Set<String> s = new TreeSet<String>();
        if (commaDelimitedNums != null && !commaDelimitedNums.equals("")) {
            String[] split = commaDelimitedNums.split(",");
            for (int i = 0; i < split.length; i++) {
                s.add( split[i] );
            }
        }
        return HierarchyUtils.makeNodeIdString(s);
    }

}
