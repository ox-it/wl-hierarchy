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

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.sakaiproject.hierarchy.HierarchyService;
import org.sakaiproject.hierarchy.dao.HierarchyDao;
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



    public void createHierarchy(String hierarchyId) {
        // TODO Auto-generated method stub

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

}
