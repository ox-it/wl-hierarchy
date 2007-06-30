/******************************************************************************
 * HierarchyNode.java - created by aaronz@vt.edu
 * 
 * Copyright (c) 2007 Virginia Polytechnic Institute and State University
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 * Contributors:
 * Aaron Zeckoski (aaronz@vt.edu) - primary
 * 
 *****************************************************************************/

package org.sakaiproject.hierarchy.model;

/**
 * This pea represents a node in a hierarchy 
 * (in academics a department or college would probably be represented by a node)
 * 
 * @author Aaron Zeckoski (aaronz@vt.edu)
 */
public class HierarchyNode {

    /**
     * The unique id for this hierarchy node
     */
    public String id;
    /**
     * The name of this hierarchy node (may be null)<br/>
     * e.g. Department of Computer Science
     */
    public String name;
    /**
     * the primary parent for this node (null if this is the root node)
     */
    public String parentNodeId;
    /**
     * an array containing all parents for this node
     */
    public String[] parentNodeIds;
    /**
     * an array containing all children for this node
     */
    public String[] childNodeIds;


    /**
     * Empty constructor
     */
    public HierarchyNode() {}

    /**
     * Leaf Constructor
     */
    public HierarchyNode(String id, String name, String parentNodeId) {
        this.id = id;
        this.name = name;
        this.parentNodeId = parentNodeId;
    }

    /**
     * Basic constructor
     */
    public HierarchyNode(String id, String name, String parentNodeId, String[] childNodeIds) {
        this.id = id;
        this.name = name;
        this.parentNodeId = parentNodeId;
        this.childNodeIds = childNodeIds;
    }

    /**
     * Full constructor
     */
    public HierarchyNode(String id, String name, String parentNodeId, String[] parentNodeIds, String[] childNodeIds) {
        this.id = id;
        this.name = name;
        this.parentNodeId = parentNodeId;
        this.parentNodeIds = parentNodeIds;
        this.childNodeIds = childNodeIds;
    }

}
