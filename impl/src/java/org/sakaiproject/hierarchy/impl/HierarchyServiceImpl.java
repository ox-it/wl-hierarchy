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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.genericdao.api.search.Order;
import org.sakaiproject.genericdao.api.search.Restriction;
import org.sakaiproject.genericdao.api.search.Search;
import org.sakaiproject.hierarchy.HierarchyService;
import org.sakaiproject.hierarchy.dao.HierarchyDao;
import org.sakaiproject.hierarchy.dao.model.HierarchyNodeMetaData;
import org.sakaiproject.hierarchy.dao.model.HierarchyPersistentNode;
import org.sakaiproject.hierarchy.impl.utils.HierarchyImplUtils;
import org.sakaiproject.hierarchy.model.HierarchyNode;

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

   // private SessionManager sessionManager;
   // public void setSessionManager(SessionManager sessionManager) {
   // this.sessionManager = sessionManager;
   // }

   public void init() {
      log.info("init");
   }

   public HierarchyNode createHierarchy(String hierarchyId) {
      if (hierarchyId.length() < 1 || hierarchyId.length() > 250) {
         throw new IllegalArgumentException("Invalid hierarchyId (" + hierarchyId
               + "): length must be 1 to 250 chars");
      }

      long count = dao.countBySearch(HierarchyNodeMetaData.class, 
            new Search("hierarchyId", hierarchyId) );
      if (count > 0) {
         throw new IllegalArgumentException("Invalid hierarchyId (" + hierarchyId
               + "): this id is already in use, you must use a unique id when creating a new hierarchy");
      }

      HierarchyPersistentNode pNode = new HierarchyPersistentNode(); // no children or parents to
      // start
      HierarchyNodeMetaData metaData = new HierarchyNodeMetaData(pNode, hierarchyId, Boolean.TRUE, null); // getCurrentUserId());
      saveNodeAndMetaData(pNode, metaData);

      return HierarchyImplUtils.makeNode(pNode, metaData);
   }

   public HierarchyNode setHierarchyRootNode(String hierarchyId, String nodeId) {
      HierarchyNodeMetaData metaData = getNodeMeta(nodeId);
      HierarchyNodeMetaData rootMetaData = getRootNodeMetaByHierarchy(hierarchyId);

      Set<HierarchyNodeMetaData> entities = new HashSet<HierarchyNodeMetaData>();

      if (rootMetaData != null) {
         if (metaData.getId().equals(rootMetaData.getId())) {
            // this node is already the root node
            return HierarchyImplUtils.makeNode(metaData);
         } else if (!metaData.getHierarchyId().equals(rootMetaData.getHierarchyId())) {
            throw new IllegalArgumentException("Cannot move a node from one hierarchy ("
                  + metaData.getHierarchyId() + ") to another (" + hierarchyId
                  + ") and replace the root node, this could orphan nodes");
         }
         rootMetaData.setIsRootNode(Boolean.FALSE);
         entities.add(metaData);
      }

      if (metaData.getNode().getParentIds() != null) {
         throw new IllegalArgumentException("Cannot assign a node (" + nodeId
               + ") to the hierarchy rootNode when it has parents");
      }

      metaData.setIsRootNode(Boolean.TRUE);
      entities.add(metaData);

      dao.saveSet(entities);
      return HierarchyImplUtils.makeNode(metaData);
   }

   @SuppressWarnings("unchecked")
   public void destroyHierarchy(String hierarchyId) {
      List<HierarchyNodeMetaData> l = dao.findBySearch(HierarchyNodeMetaData.class, 
            new Search("hierarchyId", hierarchyId) );
      if (l.isEmpty()) {
         throw new IllegalArgumentException("Could not find hierarchy to remove with the following id: "
               + hierarchyId);
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
      HierarchyNodeMetaData metaData = getRootNodeMetaByHierarchy(hierarchyId);
      if (metaData == null) {
         return null;
      }
      return HierarchyImplUtils.makeNode(metaData);
   }

   public HierarchyNode getNodeById(String nodeId) {
      HierarchyNodeMetaData metaData = getNodeMeta(nodeId);
      return HierarchyImplUtils.makeNode(metaData);
   }


   public Map<String, HierarchyNode> getNodesByIds(String[] nodeIds) {
      List<HierarchyNodeMetaData> nodeMetas = getNodeMetas(nodeIds);
      Map<String, HierarchyNode> m = new HashMap<String, HierarchyNode>();
      for (HierarchyNodeMetaData metaData : nodeMetas) {
         HierarchyNode node = HierarchyImplUtils.makeNode(metaData);
         m.put(node.id, node);
      }
      return m;
   }

   public Set<HierarchyNode> getChildNodes(String nodeId, boolean directOnly) {
      Set<HierarchyNode> children = new HashSet<HierarchyNode>();

      HierarchyNodeMetaData parentMetaData = getNodeMeta(nodeId);
      String childIdString = null;
      if (directOnly) {
         childIdString = parentMetaData.getNode().getDirectChildIds();
      } else {
         childIdString = parentMetaData.getNode().getChildIds();
      }

      if (childIdString == null) {
         return children;
      }

      Set<String> childrenIds = HierarchyImplUtils.makeNodeIdSet(childIdString);
      List<HierarchyNodeMetaData> childNodeMetas = getNodeMetas(childrenIds);
      for (HierarchyNodeMetaData metaData : childNodeMetas) {
         children.add(HierarchyImplUtils.makeNode(metaData));
      }
      return children;
   }

   public Set<HierarchyNode> getParentNodes(String nodeId, boolean directOnly) {
      Set<HierarchyNode> parents = new HashSet<HierarchyNode>();

      HierarchyNodeMetaData parentMetaData = getNodeMeta(nodeId);
      String parentIdString = null;
      if (directOnly) {
         parentIdString = parentMetaData.getNode().getDirectParentIds();
      } else {
         parentIdString = parentMetaData.getNode().getParentIds();
      }

      if (parentIdString == null) {
         return parents;
      }

      Set<String> parentsIds = HierarchyImplUtils.makeNodeIdSet(parentIdString);
      List<HierarchyNodeMetaData> parentNodeMetas = getNodeMetas(parentsIds);
      for (HierarchyNodeMetaData metaData : parentNodeMetas) {
         parents.add(HierarchyImplUtils.makeNode(metaData));
      }

      
      return parents;
   }

   public HierarchyNode addNode(String hierarchyId, String parentNodeId) {
      if (parentNodeId == null) {
         throw new RuntimeException("Setting parentNodeId to null is not yet supported");
      }

      // validate the parent node and hierarchy (this needs to be cached for sure)
      HierarchyNodeMetaData parentNodeMeta = getNodeMeta(parentNodeId);
      if (parentNodeMeta == null) {
         throw new IllegalArgumentException("Invalid parent node id, cannot find node with id: "
               + parentNodeId);
      }
      if (!parentNodeMeta.getHierarchyId().equals(hierarchyId)) {
         throw new IllegalArgumentException("Invalid hierarchy id, cannot find node (" + parentNodeId
               + ") in this hierarchy: " + hierarchyId);
      }

      // get the set of all nodes above the new node (these will have to be updated)
      Set<String> parentNodeIds = HierarchyImplUtils.makeNodeIdSet(parentNodeMeta.getNode().getParentIds());
      parentNodeIds.add(parentNodeId);

      // create the new node and assign the new parents from our parent
      HierarchyPersistentNode pNode = new HierarchyPersistentNode(HierarchyImplUtils
            .makeSingleEncodedNodeIdString(parentNodeId), HierarchyImplUtils
            .makeEncodedNodeIdString(parentNodeIds));
      HierarchyNodeMetaData metaData = new HierarchyNodeMetaData(pNode, hierarchyId, Boolean.FALSE, null); // getCurrentUserId());
      // save this new node (perhaps we should be saving all of these in one massive update?) -AZ
      saveNodeAndMetaData(pNode, metaData);
      String newNodeId = pNode.getId().toString();

      // update all the links in the tree for this new node
      List<HierarchyPersistentNode> pNodesList = getNodes(parentNodeIds);
      Set<HierarchyPersistentNode> pNodes = new HashSet<HierarchyPersistentNode>();
      for (HierarchyPersistentNode node : pNodesList) {
         if (node.getId().toString().equals(parentNodeId)) {
            // special case for our parent, update direct children
            node.setDirectChildIds(
                  HierarchyImplUtils.addSingleNodeIdToEncodedString(
                        node.getDirectChildIds(), newNodeId));
         }

         // update the children for each node
         node.setChildIds(
               HierarchyImplUtils.addSingleNodeIdToEncodedString(node.getChildIds(), newNodeId));

         // add to the set of node to be saved
         pNodes.add(node);
      }
      dao.saveSet(pNodes);

      return HierarchyImplUtils.makeNode(pNode, metaData);
   }

   public HierarchyNode removeNode(String nodeId) {
      if (nodeId == null) {
         throw new NullPointerException("nodeId to remove cannot be null");
      }

      // validate the node
      HierarchyNodeMetaData metaData = getNodeMeta(nodeId);
      if (metaData == null) {
         throw new IllegalArgumentException("Invalid node id, cannot find node with id: " + nodeId);
      }
      if (metaData.getIsRootNode().booleanValue()) {
         throw new IllegalArgumentException("Cannot remove the root node (" + nodeId + "), "
               + "you must remove the entire hierarchy (" + metaData.getHierarchyId()
               + ") to remove this root node");
      }

      // get the set of all nodes above the current node (these will have to be updated)
      HierarchyNode currentNode = HierarchyImplUtils.makeNode(metaData);
      if (currentNode.childNodeIds.size() != 0) {
         throw new IllegalArgumentException("Cannot remove a node with children nodes, "
               + "reduce the children on this node from " + currentNode.childNodeIds.size()
               + " to 0 before attempting to remove it");
      }

      if (currentNode.directParentNodeIds.size() > 1) {
         throw new IllegalArgumentException("Cannot remove a node with multiple parents, "
               + "reduce the parents on this node to 1 before attempting to remove it");
      }

      // get the "main" parent node
      String currentParentNodeId = getParentNodeId(currentNode);

      // update all the links in the tree for this removed node
      List<HierarchyPersistentNode> pNodesList = getNodes(currentNode.parentNodeIds);
      Set<HierarchyPersistentNode> pNodes = new HashSet<HierarchyPersistentNode>();
      for (HierarchyPersistentNode pNode : pNodesList) {
         if (pNode.getId().toString().equals(currentParentNodeId)) {
            // special case for our parent, update direct children
            Set<String> nodeChildren = HierarchyImplUtils.makeNodeIdSet(pNode.getDirectChildIds());
            nodeChildren.remove(nodeId);
            pNode.setDirectChildIds(HierarchyImplUtils.makeEncodedNodeIdString(nodeChildren));
         }

         // update the children for each node
         Set<String> nodeChildren = HierarchyImplUtils.makeNodeIdSet(pNode.getChildIds());
         nodeChildren.remove(nodeId);
         pNode.setChildIds(HierarchyImplUtils.makeEncodedNodeIdString(nodeChildren));

         // add to the set of nodes to be saved
         pNodes.add(pNode);
      }
      dao.saveSet(pNodes);

      return HierarchyImplUtils.makeNode(getNodeMeta(currentParentNodeId));
   }

   public HierarchyNode saveNodeMetaData(String nodeId, String title, String description, String permToken) {
      if (nodeId == null) {
         throw new NullPointerException("nodeId to remove cannot be null");
      }

      // validate the node
      HierarchyNodeMetaData metaData = getNodeMeta(nodeId);
      if (metaData == null) {
         throw new IllegalArgumentException("Invalid node id, cannot find node with id: " + nodeId);
      }

      // update the node meta data
      if (title != null) {
         if (title.equals("")) {
            metaData.setTitle(null);
         } else {
            metaData.setTitle(title);
         }
      }
      if (description != null) {
         if (description.equals("")) {
            metaData.setDescription(null);
         } else {
            metaData.setDescription(description);
         }
      }
      if (permToken != null) {
         if (permToken.equals("")) {
            metaData.setPermToken(null);
         } else {
            metaData.setPermToken(permToken);
         }
      }

      // save the node meta data
      dao.save(metaData);

      return HierarchyImplUtils.makeNode(metaData);
   }

   public HierarchyNode addChildRelation(String nodeId, String childNodeId) {
      if (nodeId == null || childNodeId == null) {
         throw new NullPointerException("nodeId (" + nodeId + ") and childNodeId (" + childNodeId
               + ") cannot be null");
      }

      if (nodeId.equals(childNodeId)) {
         throw new IllegalArgumentException("nodeId and childNodeId cannot be the same: " + nodeId);
      }

      HierarchyNodeMetaData metaData = getNodeMeta(nodeId);
      if (metaData == null) {
         throw new IllegalArgumentException("Invalid nodeId: " + nodeId);
      }

      HierarchyNodeMetaData addMetaData = getNodeMeta(childNodeId);
      if (addMetaData == null) {
         throw new IllegalArgumentException("Invalid childNodeId: " + childNodeId);
      }

      HierarchyNode currentNode = HierarchyImplUtils.makeNode(metaData);
      // only add this if it is not already in there
      if (!currentNode.directChildNodeIds.contains(childNodeId)) {
         // first check for a cycle
         if (currentNode.childNodeIds.contains(childNodeId)
               || currentNode.parentNodeIds.contains(childNodeId)) {
            throw new IllegalArgumentException("Cannot add " + childNodeId + " as a child of " + nodeId
                  + " because it is already in the node tree directly above or below this node");
         }

         // now we go ahead and update this node and all the related nodes
         HierarchyNode addNode = HierarchyImplUtils.makeNode(addMetaData);
         Set<HierarchyPersistentNode> pNodes = new HashSet<HierarchyPersistentNode>();

         // update the current node
         metaData.getNode().setDirectChildIds(
               HierarchyImplUtils.addSingleNodeIdToEncodedString(
                     metaData.getNode().getDirectChildIds(), childNodeId));
         metaData.getNode().setChildIds(
               HierarchyImplUtils.addSingleNodeIdToEncodedString(
                     metaData.getNode().getChildIds(), childNodeId));
         pNodes.add(metaData.getNode());

         // update the add node
         addMetaData.getNode().setDirectParentIds(
               HierarchyImplUtils.addSingleNodeIdToEncodedString(
                     addMetaData.getNode().getDirectParentIds(), nodeId));
         addMetaData.getNode().setParentIds(
               HierarchyImplUtils.addSingleNodeIdToEncodedString(
                     addMetaData.getNode().getParentIds(),nodeId));
         pNodes.add(addMetaData.getNode());

         // update the parents of the current node (they have new children)
         List<HierarchyPersistentNode> pNodesList = getNodes(currentNode.parentNodeIds);
         Set<String> nodesToAdd = addNode.childNodeIds;
         nodesToAdd.add(addNode.id);
         for (HierarchyPersistentNode pNode : pNodesList) {
            // update the children for each node
            Set<String> nodeChildren = HierarchyImplUtils.makeNodeIdSet(pNode.getChildIds());
            nodeChildren.addAll(nodesToAdd);
            pNode.setChildIds(HierarchyImplUtils.makeEncodedNodeIdString(nodeChildren));

            // add to the set of nodes to be saved
            pNodes.add(pNode);
         }

         // update the children of the add node (they have new parants)
         pNodesList = getNodes(addNode.childNodeIds);
         nodesToAdd = currentNode.parentNodeIds;
         nodesToAdd.add(currentNode.id);
         for (HierarchyPersistentNode pNode : pNodesList) {
            // update the parents for each node
            Set<String> parents = HierarchyImplUtils.makeNodeIdSet(pNode.getParentIds());
            parents.addAll(nodesToAdd);
            pNode.setParentIds(HierarchyImplUtils.makeEncodedNodeIdString(parents));

            // add to the set of nodes to be saved
            pNodes.add(pNode);
         }

         dao.saveSet(pNodes);
      }

      return HierarchyImplUtils.makeNode(metaData);
   }

   public HierarchyNode removeChildRelation(String nodeId, String childNodeId) {
      if (nodeId == null || childNodeId == null) {
         throw new NullPointerException("nodeId (" + nodeId + ") and childNodeId (" + childNodeId
               + ") cannot be null");
      }

      if (nodeId.equals(childNodeId)) {
         throw new IllegalArgumentException("nodeId and childNodeId cannot be the same: " + nodeId);
      }

      HierarchyNodeMetaData metaData = getNodeMeta(nodeId);
      if (metaData == null) {
         throw new IllegalArgumentException("Invalid nodeId: " + nodeId);
      }

      HierarchyNodeMetaData removeMetaData = getNodeMeta(childNodeId);
      if (removeMetaData == null) {
         throw new IllegalArgumentException("Invalid childNodeId: " + childNodeId);
      }

      HierarchyNode currentNode = HierarchyImplUtils.makeNode(metaData);
      // only do something if this child is a direct child of this node
      if (currentNode.directChildNodeIds.contains(childNodeId)) {
         // first check for orphaning
         HierarchyNode removeNode = HierarchyImplUtils.makeNode(removeMetaData);
         if (removeNode.directParentNodeIds.size() <= 1) {
            throw new IllegalArgumentException("Cannot remove " + childNodeId + " as a child of " + nodeId
                  + " because it would orphan the child node, you need to use the remove method" +
            "if you want to remove a node or add this node as the child of another node first");
         }

         // now we go ahead and update this node and all the related nodes
         Set<HierarchyPersistentNode> pNodes = new HashSet<HierarchyPersistentNode>();
         Set<String> nodes = null;

         // update the current node
         nodes = HierarchyImplUtils.makeNodeIdSet(metaData.getNode().getChildIds());
         nodes.remove(childNodeId);
         metaData.getNode().setChildIds(HierarchyImplUtils.makeEncodedNodeIdString(nodes));
         nodes = HierarchyImplUtils.makeNodeIdSet(metaData.getNode().getDirectChildIds());
         nodes.remove(childNodeId);
         metaData.getNode().setDirectChildIds(HierarchyImplUtils.makeEncodedNodeIdString(nodes));
         pNodes.add(metaData.getNode());

         // update the remove node
         nodes = HierarchyImplUtils.makeNodeIdSet(removeMetaData.getNode().getParentIds());
         nodes.remove(nodeId);
         removeMetaData.getNode().setParentIds(HierarchyImplUtils.makeEncodedNodeIdString(nodes));
         nodes = HierarchyImplUtils.makeNodeIdSet(removeMetaData.getNode().getDirectParentIds());
         nodes.remove(nodeId);
         removeMetaData.getNode().setDirectParentIds(HierarchyImplUtils.makeEncodedNodeIdString(nodes));
         pNodes.add(removeMetaData.getNode());

         // update the parents of the current node (they have less children)
         List<HierarchyPersistentNode> pNodesList = getNodes(currentNode.parentNodeIds);
         Set<String> nodesToRemove = removeNode.childNodeIds;
         nodesToRemove.add(removeNode.id);
         for (HierarchyPersistentNode pNode : pNodesList) {
            // update the children for each node
            Set<String> children = HierarchyImplUtils.makeNodeIdSet(pNode.getChildIds());
            children.removeAll(nodesToRemove);
            // add back in all the children of the currentNode because we may have 
            // taken out part of the tree below where if it connects to the children of removeNode
            children.addAll(currentNode.childNodeIds);
            pNode.setChildIds(HierarchyImplUtils.makeEncodedNodeIdString(children));

            // add to the set of nodes to be saved
            pNodes.add(pNode);
         }

         // update the children of the remove node (they have lost parents)
         pNodesList = getNodes(removeNode.childNodeIds);
         nodesToRemove = currentNode.parentNodeIds;
         nodesToRemove.add(currentNode.id);
         for (HierarchyPersistentNode pNode : pNodesList) {
            // update the parents for each node
            Set<String> parents = HierarchyImplUtils.makeNodeIdSet(pNode.getParentIds());
            parents.removeAll(nodesToRemove);
            // add back in all the parents of the removeNode because we will have 
            // taken out part of the tree above where it reconnects on the way to the root
            parents.addAll(removeNode.parentNodeIds);
            pNode.setParentIds(HierarchyImplUtils.makeEncodedNodeIdString(parents));

            // add to the set of nodes to be saved
            pNodes.add(pNode);
         }

         dao.saveSet(pNodes);

      }

      return HierarchyImplUtils.makeNode(metaData);
   }

   public HierarchyNode addParentRelation(String nodeId, String parentNodeId) {
      // TODO Not implemented yet
      throw new RuntimeException("This method is not implemented yet");
   }

   public HierarchyNode removeParentRelation(String nodeId, String parentNodeId) {
      // TODO Auto-generated method stub
      throw new RuntimeException("This method is not implemented yet");
   }


   @SuppressWarnings("unchecked")
   public Set<String> getNodesWithToken(String hierarchyId, String permToken) {
      if (permToken == null || permToken.equals("")) {
         throw new NullPointerException("permToken cannot be null or empty string");
      }

      List l = dao.findBySearch(HierarchyNodeMetaData.class, 
            new Search("hierarchyId", hierarchyId) );
      if (l.isEmpty()) {
         throw new IllegalArgumentException("Could not find hierarchy with the following id: "
               + hierarchyId);
      }

      List nodeIdsList = dao.findBySearch(HierarchyNodeMetaData.class, 
            new Search(new Restriction[] {
                  new Restriction("hierarchyId", hierarchyId),
                  new Restriction("permToken", permToken)
            }, new Order("node.id")));

      Set<String> nodeIds = new TreeSet<String>();
      for (Iterator iter = nodeIdsList.iterator(); iter.hasNext();) {
         HierarchyNodeMetaData metaData = (HierarchyNodeMetaData) iter.next();
         nodeIds.add(metaData.getNode().getId().toString());
      }

      return nodeIds;
   }

   public Map<String, Set<String>> getNodesWithTokens(String hierarchyId, String[] permTokens) {
      // TODO it would be better if this were more efficient...
      if (permTokens == null) {
         throw new NullPointerException("permTokens cannot be null");
      }

      Map<String, Set<String>> tokenNodes = new HashMap<String, Set<String>>();
      for (int i = 0; i < permTokens.length; i++) {
         Set<String> nodeIds = getNodesWithToken(hierarchyId, permTokens[i]);
         tokenNodes.put(permTokens[i], nodeIds);
      }

      return tokenNodes;
   }


   /**
    * Convenience method to save a node and metadata in one transaction
    * 
    * @param pNode
    * @param metaData
    */
   @SuppressWarnings("unchecked")
   private void saveNodeAndMetaData(HierarchyPersistentNode pNode, HierarchyNodeMetaData metaData) {
      Set<HierarchyPersistentNode> pNodes = new HashSet<HierarchyPersistentNode>();
      pNodes.add(pNode);
      Set<HierarchyNodeMetaData> metaDatas = new HashSet<HierarchyNodeMetaData>();
      metaDatas.add(metaData);
      Set[] entitySets = new Set[] { pNodes, metaDatas };
      dao.saveMixedSet(entitySets);
   }

   /**
    * Fetch node data from storage
    * 
    * @param nodeId
    * @return a {@link HierarchyNodeMetaData} or null if not found
    */
   @SuppressWarnings("unchecked")
   private HierarchyNodeMetaData getNodeMeta(String nodeId) {
      List<HierarchyNodeMetaData> l = dao.findBySearch(HierarchyNodeMetaData.class, 
            new Search("node.id", new Long(nodeId)));
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
    * 
    * @param hierarchyId
    * @return the root {@link HierarchyNodeMetaData} of the hierarchy
    */
   @SuppressWarnings("unchecked")
   private HierarchyNodeMetaData getRootNodeMetaByHierarchy(String hierarchyId) {
      List<HierarchyNodeMetaData> l = dao.findBySearch(HierarchyNodeMetaData.class, 
            new Search(new Restriction[] {
                  new Restriction("hierarchyId", hierarchyId),
                  new Restriction("isRootNode", Boolean.TRUE)
            }) );
      if (l.size() > 1) {
         throw new IllegalStateException("Invalid hierarchy state: more than one root node for hierarchyId: "
               + hierarchyId);
      } else if (l.size() == 1) {
         return l.get(0);
      } else {
         return null;
      }
   }

   /**
    * Get all nodes and meta data based on a set of nodeIds
    * 
    * @param nodeIds
    * @return
    */
   private List<HierarchyNodeMetaData> getNodeMetas(Set<String> nodeIds) {
      return getNodeMetas(nodeIds.toArray(new String[] {}));
   }

   @SuppressWarnings("unchecked")
   private List<HierarchyNodeMetaData> getNodeMetas(String[] nodeIds) {
      List<HierarchyNodeMetaData> l = null;
      if (nodeIds == null || nodeIds.length == 0) {
         l = new ArrayList<HierarchyNodeMetaData>();
      } else {
         Long[] pNodeIds = new Long[nodeIds.length];
         for (int i = 0; i < nodeIds.length; i++) {
            pNodeIds[i] = new Long(nodeIds[i]);
         }
         l = dao.findBySearch(HierarchyNodeMetaData.class, 
               new Search("node.id", pNodeIds) );
      }
      return l;
   }

   /**
    * Get all nodes only based on a set of nodeIds
    * 
    * @param nodeIds
    * @return
    */
   private List<HierarchyPersistentNode> getNodes(Set<String> nodeIds) {
      return getNodes(nodeIds.toArray(new String[] {}));
   }

   @SuppressWarnings("unchecked")
   private List<HierarchyPersistentNode> getNodes(String[] nodeIds) {
      List<HierarchyPersistentNode> l = null;
      if (nodeIds == null || nodeIds.length == 0) {
         l = new ArrayList<HierarchyPersistentNode>();
      } else {
         Long[] pNodeIds = new Long[nodeIds.length];
         for (int i = 0; i < nodeIds.length; i++) {
            pNodeIds[i] = new Long(nodeIds[i]);
         }
         l = dao.findBySearch(HierarchyPersistentNode.class, 
               new Search("id", pNodeIds) );
      }
      return l;
   }

   /**
    * Find the direct parent node id for a node
    * @param node
    * @return the node if or null if none exists
    */
   private String getParentNodeId(HierarchyNode node) {
      String parentNodeId = null;
      if (node.directParentNodeIds != null &&
            node.directParentNodeIds.size() > 0) {
         parentNodeId = node.directParentNodeIds.iterator().next();
      }
      return parentNodeId;
   }

}
