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
import java.util.List;
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
        HierarchyNodeMetaData metaData = getNodeMeta(nodeId);
        HierarchyNodeMetaData rootMetaData = getRootNodeMetaByHierarchy(hierarchyId);

        Set<HierarchyNodeMetaData> entities = new HashSet<HierarchyNodeMetaData>();

        if (rootMetaData != null) {
            if (metaData.getId().equals(rootMetaData.getId())) {
                // this node is already the root node
                return HierarchyUtils.makeNode(metaData);
            } else if (!metaData.getHierarchyId().equals(rootMetaData.getHierarchyId())) {
                throw new IllegalArgumentException("Cannot move a node from one hierarchy ("+metaData.getHierarchyId()+
                        ") to another ("+hierarchyId+") and replace the root node, this could orphan nodes");
            }
            rootMetaData.setIsRootNode(Boolean.FALSE);
            entities.add(metaData);
        }

        if (metaData.getNode().getParentIds() != null) {
            throw new IllegalArgumentException("Cannot assign a node ("+nodeId+") to the hierarchy rootNode when it has parents");
        }

        metaData.setIsRootNode(Boolean.TRUE);
        entities.add(metaData);

        dao.saveSet(entities);
        return HierarchyUtils.makeNode(metaData);
    }

    public void destroyHierarchy(String hierarchyId) {
        List l = dao.findByProperties(HierarchyNodeMetaData.class, new String[] {"hierarchyId"}, new Object[] {hierarchyId});
        if (l.isEmpty()) {
            throw new IllegalArgumentException("Could not find hierarchy to remove with the following id: " + hierarchyId);
        }

        Set<HierarchyPersistentNode> nodes = new HashSet<HierarchyPersistentNode>();
        Set<HierarchyNodeMetaData> nodesMetaData = new HashSet<HierarchyNodeMetaData>();
        for (int i = 0; i < l.size(); i++) {
            HierarchyNodeMetaData nmd = (HierarchyNodeMetaData) l.get(i);
            nodesMetaData.add(nmd);
            nodes.add(nmd.getNode());
        }

        Set[] entitySets = new Set[] { nodesMetaData, nodes };
        dao.deleteMixedSet(entitySets);
    }



    public HierarchyNode getRootNode(String hierarchyId) {
        List l = dao.findByProperties(HierarchyNodeMetaData.class, 
                new String[] {"hierarchyId", "isRootNode"}, 
                new Object[] {hierarchyId, Boolean.TRUE}
            );
        if (l.isEmpty() || l.size() != 1) {
            throw new IllegalArgumentException("Could not find hierarchy root node for hierarchy: " + hierarchyId);
        }

        HierarchyNodeMetaData metaData = (HierarchyNodeMetaData) l.get(0);
        return HierarchyUtils.makeNode(metaData);
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

    /**
     * Fetch node data from storage
     * @param nodeId
     * @return a {@link HierarchyNodeMetaData} or null if not found
     */
    @SuppressWarnings("unchecked")
    private HierarchyNodeMetaData getNodeMeta(String nodeId) {
        List<HierarchyNodeMetaData> l = dao.findByProperties(HierarchyNodeMetaData.class, 
                new String[] {"node.id"}, new Object[] {new Long(nodeId)});
        if (l.size() > 1) {
            throw new IllegalStateException("Invalid hierarchy state: more than one node with id: " + nodeId);
        } else if (l.size() == 1) {
            return l.get(0);
        } else {
            return null;
        }
    }

    /**
     * Find the current root node
     * @param hierarchyId
     * @return the root {@link HierarchyNodeMetaData} of the hierarchy
     */
    @SuppressWarnings("unchecked")
    private HierarchyNodeMetaData getRootNodeMetaByHierarchy(String hierarchyId) {
        List<HierarchyNodeMetaData> l = dao.findByProperties(HierarchyNodeMetaData.class, 
                new String[] {"hierarchyId", "isRootNode"}, new Object[] {hierarchyId, Boolean.TRUE});
        if (l.size() > 1) {
            throw new IllegalStateException("Invalid hierarchy state: more than one root node for hierarchyId: " + hierarchyId);
        } else if (l.size() == 1) {
            return l.get(0);
        } else {
            return null;
        }
    }

}
