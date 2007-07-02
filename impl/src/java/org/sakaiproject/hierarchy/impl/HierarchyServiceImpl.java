/******************************************************************************
 * HierarchyImpl.java - created by aaronz on 30 June 2007
 * 
 * Copyright (c) 2007 Centre for Academic Research in Educational Technologies
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 * Contributors:
 * Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 *****************************************************************************/

package org.sakaiproject.hierarchy.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.sakaiproject.hierarchy.HierarchyService;
import org.sakaiproject.hierarchy.dao.HierarchyDao;
import org.sakaiproject.hierarchy.dao.model.HierarchyNodeMetaData;
import org.sakaiproject.hierarchy.dao.model.HierarchyPersistentNode;
import org.sakaiproject.hierarchy.impl.utils.HierarchyUtils;
import org.sakaiproject.hierarchy.model.HierarchyNode;
import org.sakaiproject.tool.api.SessionManager;

/**
 * The default implementation of the Hierarchy interface
 * 
 * @author Aaron Zeckoski (aaronz@vt.edu)
 */
public class HierarchyServiceImpl implements HierarchyService {

    private static Log log = LogFactory.getLog(HierarchyServiceImpl.class);

    private HierarchyDao dao;
    public void setDao(HierarchyDao dao) {
        this.dao = dao;
    }

    private SessionManager sessionManager;
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }



    public void init() {
        log.info("init");
    }



    public HierarchyNode createHierarchy(String hierarchyId) {
        if (hierarchyId.length() < 1 || hierarchyId.length() > 250) {
            throw new IllegalArgumentException("Invalid hierarchyId ("+hierarchyId+"): length must be 1 to 250 chars");
        }

        int count = dao.countByProperties(HierarchyNodeMetaData.class, new String[] {"hierarchyId"}, new Object[] {hierarchyId});
        if (count > 0) {
            throw new IllegalArgumentException("Invalid hierarchyId ("+hierarchyId+"): this id is already in use, you must use a unique id when creating a new hierarchy");
        }

        HierarchyPersistentNode pNode = new HierarchyPersistentNode(); // no children or parents to start
        HierarchyNodeMetaData metaData = new HierarchyNodeMetaData(pNode, hierarchyId, Boolean.TRUE, getCurrentUserId());
        saveNodeAndMetaData(pNode, metaData);

        return HierarchyUtils.makeNode(pNode, metaData);
    }

    public HierarchyNode setHierarchyRootNode(String hierarchyId, String nodeId) {
        // TODO Auto-generated method stub
        return null;
    }

    public void destroyHierarchy(String hierarchyId) {
        // TODO Auto-generated method stub

    }



    public HierarchyNode getRootNode(String hierarchyId) {
        // TODO Auto-generated method stub
        return null;
    }

    public HierarchyNode getNodeById(String nodeId) {
        // TODO Auto-generated method stub
        return null;
    }

    public Set<HierarchyNode> getChildNodes(String nodeId, boolean directOnly) {
        // TODO Auto-generated method stub
        return null;
    }

    public Set<HierarchyNode> getParentNodes(String nodeId, boolean directOnly) {
        // TODO Auto-generated method stub
        return null;
    }



    public HierarchyNode addNode(String hierarchyId, String parentNodeId) {
        // TODO Auto-generated method stub
        return null;
    }

    public HierarchyNode updateChildren(String nodeId, Set<String> childNodeIds) {
        // TODO Auto-generated method stub
        return null;
    }

    public HierarchyNode updateParents(String nodeId, Set<String> parentNodeIds) {
        // TODO Auto-generated method stub
        return null;
    }

    public void removeNode(String nodeId) {
        // TODO Auto-generated method stub

    }

    public HierarchyNode saveNodeMetaData(String nodeId, String title, String description) {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * @return the current userId
     */
    private String getCurrentUserId() {
        String userId = sessionManager.getCurrentSessionUserId();
        if (userId == null || userId.equals("")) { userId = "admin"; } // make sure there is always something
        return userId;
    }

    /**
     * Convenience method to save a node and metadata in one transaction
     * @param pNode
     * @param metaData
     */
    private void saveNodeAndMetaData(HierarchyPersistentNode pNode, HierarchyNodeMetaData metaData) {
        Set<HierarchyPersistentNode> pNodes = new HashSet<HierarchyPersistentNode>();
        pNodes.add(pNode);
        Set<HierarchyNodeMetaData> metaDatas = new HashSet<HierarchyNodeMetaData>();
        metaDatas.add(metaData);
        Set[] entitySets = new Set[] {pNodes, metaDatas};
        dao.saveMixedSet(entitySets);
    }

}
