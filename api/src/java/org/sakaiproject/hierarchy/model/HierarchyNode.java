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

import java.util.Set;

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
     * The assigned unique id for the hierarchy this node is in
     */
    public String hierarchyId;
    /**
     * the title of this node
     */
    public String title;
    /**
     * the description for this node 
     */
    public String description;
    /**
     * a set of all direct parents for this node,
     * the ids of parent nodes that touch this node directly
     */
    public Set<String> directParentNodeIds;
    /**
     * a set of all direct children for this node,
     * the ids of child nodes that touch this node directly
     */
    public Set<String> directChildNodeIds;
    /**
     * a set of all parents for this node
     */
    public Set<String> parentNodeIds;
    /**
     * a set of all children for this node
     */
    public Set<String> childNodeIds;



    /**
     * Empty constructor
     */
    public HierarchyNode() {}

}
