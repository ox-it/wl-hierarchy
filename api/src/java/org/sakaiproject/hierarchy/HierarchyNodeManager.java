/******************************************************************************
 * HierarchyNodeManager.java - created by aaronz on Jul 1, 2007
 * 
 * Copyright (c) 2007 Centre for Academic Research in Educational Technologies
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.sakaiproject.hierarchy;

import org.sakaiproject.hierarchy.model.HierarchyNode;

/**
 * This defines all node managing methods
 * 
 * @author Aaron Zeckoski (aaronz@vt.edu)
 */
public interface HierarchyNodeManager {

    /**
     * Add a new node to a hierarchy
     * @param hierarchyId a unique id which defines the hierarchy
     * @param parentNodeId the unique id for the parent of this node, can be null if this is the root or a top level node
     * @return the object representing the newly added node
     */
    public HierarchyNode addNode(String hierarchyId, String parentNodeId);

    /**
     * @param nodeId a unique id for a hierarchy node
     * @param parentNodeIds 
     * @return
     */
    public HierarchyNode addParents(String nodeId, String[] parentNodeIds);

    public HierarchyNode addChildren(String nodeId, String[] childNodeIds);

    public HierarchyNode removeParents(String nodeId, String[] parentNodeIds);

    public HierarchyNode removeChildren(String nodeId, String[] childNodeIds);

    public void removeNode(String nodeId);

}
