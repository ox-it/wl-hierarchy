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

import org.sakaiproject.hierarchy.Hierarchy;
import org.sakaiproject.hierarchy.dao.HierarchyDao;
import org.sakaiproject.hierarchy.model.HierarchyNode;

/**
 * The default implementation of the Hierarchy interface
 * 
 * @author Aaron Zeckoski (aaronz@vt.edu)
 */
public class HierarchyImpl implements Hierarchy {

    private static Log log = LogFactory.getLog(HierarchyImpl.class);

    private HierarchyDao dao;
    public void setDao(HierarchyDao dao) {
        this.dao = dao;
    }


    public void init() {
        log.info("init");
    }


    public HierarchyNode getRootLevelNode(String hierarchyId) {
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

}
