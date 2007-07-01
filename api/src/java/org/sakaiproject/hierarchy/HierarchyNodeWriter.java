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

import java.util.Set;

import org.sakaiproject.hierarchy.model.HierarchyNode;

/**
 * Allows user to control nodes (create, update, remove)
 * 
 * @author Aaron Zeckoski (aaronz@vt.edu)
 */
public interface HierarchyNodeWriter {

    /**
     * Add a new node to a hierarchy
     * @param hierarchyId a unique id which defines the hierarchy
     * @param parentNodeId the unique id for the parent of this node, can be null if this is the root or a top level node
     * @return the object representing the newly added node
     */
    public HierarchyNode addNode(String hierarchyId, String parentNodeId);

    /**
     * Add parents to a node (creates the association),
     * only adds direct parents (directly connected to this node),
     * others are implicitly defined
     * @param nodeId a unique id for a hierarchy node
     * @param parentNodeIds a set of parent node ids to associate with this node,
     * removes parents not in this array and adds parents which are listed (if not already associated)
     * @return the object representing the updated node
     */
    public HierarchyNode updateParents(String nodeId, Set<String> parentNodeIds);

    /**
     * Add children to a node (creates the association),
     * only adds direct children (directly connected to this node),
     * others are implicitly defined
     * @param nodeId a unique id for a hierarchy node
     * @param childNodeIds a set of child node ids to associate with this node,
     * removes children not in this array and adds children which are listed (if not already associated)
     * @return the object representing the updated node
     */
    public HierarchyNode updateChildren(String nodeId, Set<String> childNodeIds);

    /**
     * Remove a node from the hierarchy if it is possible,
     * nodes can only be removed if they have no children associations,
     * root nodes can never be removed,
     * exception occurs if these rules are violated
     * @param nodeId a unique id for a hierarchy node
     */
    public void removeNode(String nodeId);

    /**
     * Save meta data on a node
     * @param nodeId a unique id for a hierarchy node
     * @param title the title of the node
     * @param description a description for this node
     * @return the object representing the updated node
     */
    public HierarchyNode saveNodeMetaData(String nodeId, String title, String description);

}
