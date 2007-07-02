/******************************************************************************
 * HierarchyUtils.java - created by aaronz on Jul 2, 2007
 * 
 * Copyright (c) 2007 Centre for Academic Research in Educational Technologies
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.sakaiproject.hierarchy.impl.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.sakaiproject.hierarchy.dao.model.HierarchyNodeMetaData;
import org.sakaiproject.hierarchy.dao.model.HierarchyPersistentNode;
import org.sakaiproject.hierarchy.model.HierarchyNode;

/**
 * Utility class for the hierarchy service
 * 
 * @author Aaron Zeckoski (aaronz@vt.edu)
 */
public class HierarchyUtils {

    public static final String SEPERATOR = ":";

    /**
     * Create a {@link HierarchyNode} from the persistent data,
     * exception if the data is not persisted or data is missing
     * @param pNode a {@link HierarchyPersistentNode} which has been persisted
     * @param metaData a {@link HierarchyNodeMetaData} which has been persisted
     * @return a {@link HierarchyNode} which contains data from the 2 inputs
     */
    public static HierarchyNode makeNode(HierarchyPersistentNode pNode, HierarchyNodeMetaData metaData) {
        if (pNode == null || pNode.getId() == null) {
            throw new IllegalArgumentException("pNode cannot be null and id of pNode must be set");
        }

        HierarchyNode hNode = new HierarchyNode();
        hNode.id = pNode.getId().toString();

        hNode.directParentNodeIds = makeNodeIdSet(pNode.getDirectParentIds());
        hNode.parentNodeIds = makeNodeIdSet(pNode.getParentIds());
        hNode.directChildNodeIds = makeNodeIdSet(pNode.getDirectChildIds());
        hNode.childNodeIds = makeNodeIdSet(pNode.getChildIds());

        hNode.hierarchyId = metaData.getHierarchyId();
        hNode.title = metaData.getTitle();
        hNode.description = metaData.getDescription();

        return hNode;
    }

    /**
     * Make a Set of node Ids from an encoded string of nodeIds,
     * will not throw exception or return null
     * @param nodeIds an encoded string of nodeIds
     * @return a {@link Set} with the nodeIds in it, ordered by nodeId
     */
    public static Set<String> makeNodeIdSet(String nodeIds) {
        Set<String> s = new TreeSet<String>();
        if (nodeIds != null) {
            String[] split = nodeIds.split(SEPERATOR);
            if (split.length > 0) {
                for (int i = 0; i < split.length; i++) {
                    if (split[i] != null && !split[i].equals("")) {
                        s.add(split[i]);
                    }
                }
            }
        }
        return s;
    }

    /**
     * Make an encoded string of nodeIds from a Set of nodeIds
     * @param nodeIds a {@link Set} with the nodeIds in it
     * @return an encoded string of nodeIds
     */
    public static String makeNodeIdString(Set<String> nodeIds) {
        if (nodeIds == null || nodeIds.size() <= 0) {
            return null;
        }
        // make sure the order written into the database is natural node order
        List<String> l = new ArrayList<String>(nodeIds);
        Collections.sort(l);
        // encode the string
        StringBuilder coded = new StringBuilder();
        coded.append(HierarchyUtils.SEPERATOR);
        for (String nodeId : l) {
            coded.append(nodeId);
            coded.append(HierarchyUtils.SEPERATOR);            
        }
        return coded.toString();
    }

}
