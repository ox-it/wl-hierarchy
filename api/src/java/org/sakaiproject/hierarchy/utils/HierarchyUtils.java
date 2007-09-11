/**
 * HierarchyUtils.java - hierarchy - 2007 Sep 11, 2007 11:06:45 AM - azeckoski
 */

package org.sakaiproject.hierarchy.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.sakaiproject.hierarchy.model.HierarchyNode;


/**
 * Simple utils to assist with working with the hierarchy
 * 
 * @author Aaron Zeckoski (aaron@caret.cam.ac.uk)
 */
public class HierarchyUtils {

   /**
    * Create a sorted list of nodes based on a set of input nodes,
    * list goes from root (or highest parent) down to the bottom most node
    * @param nodes
    * @return a list of {@link HierarchyNode}
    */
   public static List<HierarchyNode> getSortedNodes(Collection<HierarchyNode> nodes) {
      List<HierarchyNode> sortedNodes = new ArrayList<HierarchyNode>();
      for (HierarchyNode hierarchyNode : nodes) {
         if (sortedNodes.size() < 1) {
            sortedNodes.add(hierarchyNode);
         } else {
            int i;
            for (i = 0; i < sortedNodes.size(); i++) {
               HierarchyNode sortedNode = sortedNodes.get(i);
               if (sortedNode.parentNodeIds.contains(hierarchyNode.id)) {
                  break;
               }
            }
            sortedNodes.add(i, hierarchyNode);
         }
      }
      return sortedNodes;
   }

}

