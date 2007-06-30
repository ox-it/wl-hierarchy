/******************************************************************************
 * HierarchyPersistentNode.java - created by aaronz on Jun 30, 2007
 * 
 * Copyright (c) 2007 Centre for Academic Research in Educational Technologies
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.sakaiproject.hierarchy.dao.model;

/**
 * This is the persistent object for storing Hierarchy Nodes
 * 
 * @author Aaron Zeckoski (aaronz@vt.edu)
 */
public class HierarchyPersistentNode {

    /**
     * The unique internal id for this hierarchy node
     */
    private Long id;
    /**
     * The name of this hierarchy node (human readable identifier)
     */
    private String name;
    /**
     * the ability to support multiple hierarchies is included in this
     * system, this will be the unique hierarchyId, the default hierarchy
     * id is 0 (used if hierarchy ID is unspecified)
     */
    private String hierarchyId;
    /**
     * this is this parent directly above this node which defines a one-way
     * path to the root node with no branches,
     * the root node will have no direct parentId
     */
    private String directParentIds;
    /**
     * the ids of all parents of this node, 
     * this goes all the way up the hierarchy to the root node, 
     * expect this to be only one parent in most cases, 
     * the path to the root is determined using the directParentId only<br/>
     * Uses a ":" separator between each id, also includes the separator in front
     * of and behind every id.<br/>
     * Examples: ":123:432:43:", ":38:", "" (no parent)
     */
    private String parentIds;
    /**
     * the ids of child nodes that touch this node directly,
     * similar treatment to the way the it works for the {@link #parentIds}
     */
    private String directChildIds;
    /**
     * the ids of all children of this node, 
     * this goes all the way down the hierarchy to the leaf nodes,
     * similar treatment to the way the it works for the {@link #parentIds}
     */
    private String childIds;


    /**
     * Empty constructor
     */
    public HierarchyPersistentNode() {
    }

    /**
     * Minimal constructor
     */
    public HierarchyPersistentNode(String name, String hierarchyId) {
        this.name = name;
        this.hierarchyId = hierarchyId;
    }

    /**
     * Root constructor
     */
    public HierarchyPersistentNode(Long id, String name, String hierarchyId, String directChildIds, String childIds) {
        this.id = id;
        this.name = name;
        this.hierarchyId = hierarchyId;
        this.directChildIds = directChildIds;
        this.childIds = childIds;
    }

    /**
     * Leaf constructor
     */
    public HierarchyPersistentNode(String name, String hierarchyId, String directParentIds, String parentIds) {
        this.name = name;
        this.hierarchyId = hierarchyId;
        this.directParentIds = directParentIds;
        this.parentIds = parentIds;
    }

    /**
     * Full constructor
     */
    public HierarchyPersistentNode(String name, String hierarchyId, String directParentIds, String parentIds, String directChildIds, String childIds) {
        this.name = name;
        this.hierarchyId = hierarchyId;
        this.directParentIds = directParentIds;
        this.parentIds = parentIds;
        this.directChildIds = directChildIds;
        this.childIds = childIds;
    }


    /**
     * Getters and Setters
     */

    public String getChildIds() {
        return childIds;
    }

    public void setChildIds(String childIds) {
        this.childIds = childIds;
    }

    public String getHierarchyId() {
        return hierarchyId;
    }

    public void setHierarchyId(String hierarchyId) {
        this.hierarchyId = hierarchyId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getDirectChildIds() {
        return directChildIds;
    }

    public void setDirectChildIds(String directChildIds) {
        this.directChildIds = directChildIds;
    }

    public String getDirectParentIds() {
        return directParentIds;
    }

    public void setDirectParentIds(String directParentIds) {
        this.directParentIds = directParentIds;
    }

}
