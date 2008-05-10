/**
 * $Id$
 * $URL$
 * CurrentClassLoaderHibernateSessionFactory.java - hierarchy - May 8, 2008 5:19:32 PM - azeckoski
 **************************************************************************
 * Copyright (c) 2008 Aaron Zeckoski
 * Licensed under the Apache License, Version 2.0
 * 
 * A copy of the Apache License has been included in this 
 * distribution and is available at: http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Aaron Zeckoski (azeckoski@gmail.com) (aaronz@vt.edu) (aaron@caret.cam.ac.uk)
 */

package org.sakaiproject.hierarchy.dao;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

/**
 * This is basically allowing us to find our classes which are only in our impl within the
 * current {@link ClassLoader} by setting the current {@link ClassLoader} to the right one
 * 
 * @author Aaron Zeckoski (aaron@caret.cam.ac.uk)
 */
public class CurrentClassLoaderHibernateSessionFactory extends LocalSessionFactoryBean {

   @Override
   protected SessionFactory buildSessionFactory() throws Exception {
      ClassLoader original = Thread.currentThread().getContextClassLoader();
      SessionFactory sf = null;
      try {
         ClassLoader cl = CurrentClassLoaderHibernateSessionFactory.class.getClassLoader();
         Thread.currentThread().setContextClassLoader(cl);
         // now create the session factory so it can find things in the right ClassLoader
         sf = super.buildSessionFactory();
      } finally {
         Thread.currentThread().setContextClassLoader(original);
      }
      return sf;
   }

}
