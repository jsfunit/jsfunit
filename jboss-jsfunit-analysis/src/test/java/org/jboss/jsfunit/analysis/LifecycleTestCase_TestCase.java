/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * 
 */
package org.jboss.jsfunit.analysis;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.model.TestPhaseListener;
import org.w3c.dom.Node;

/**
 * A LifecycleTestCase_TestCase.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class LifecycleTestCase_TestCase extends TestCase
{
   private static final String CORRECT = "<lifecycle><phase-listener>" + TestPhaseListener.class.getName()
         + "</phase-listener></lifecycle>";

   private static final String NULL_PHASELISTENER = "<lifecycle><phase-listener></phase-listener></lifecycle>";

   private static final String EMPTY_PHASELISTENER = "<lifecycle><phase-listener>  </phase-listener></lifecycle>";

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ManagedBeanTestCase#runTest()}.
    */
   public void testHappyPaths()
   {
      String facesConfig = TestUtils.getFacesConfig(CORRECT);
      Node lifecycleNode = TestUtils.extractFirstLifecycleNode(facesConfig);
      LifecycleTestCase testcase = new LifecycleTestCase("testHappyPaths", (String) TestUtils.STUBBED_RESOURCEPATH
            .toArray()[0], lifecycleNode);
      testcase.runTest();
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.LifecycleTestCase#testEmptyPhaseListener()}.
    */
   public void testTestNullPhaseListener()
   {
      String facesConfig = TestUtils.getFacesConfig(NULL_PHASELISTENER);
      Node lifecycleNode = TestUtils.extractFirstLifecycleNode(facesConfig);
      LifecycleTestCase testcase = new LifecycleTestCase("testHappyPaths", (String) TestUtils.STUBBED_RESOURCEPATH
            .toArray()[0], lifecycleNode);
      try
      {
         testcase.runTest();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("phase-listener must not be null", afe.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.LifecycleTestCase#testEmptyPhaseListener()}.
    */
   public void testTestEmptyPhaseListener()
   {
      String facesConfig = TestUtils.getFacesConfig(EMPTY_PHASELISTENER);
      Node lifecycleNode = TestUtils.extractFirstLifecycleNode(facesConfig);
      LifecycleTestCase testcase = new LifecycleTestCase("testHappyPaths", (String) TestUtils.STUBBED_RESOURCEPATH
            .toArray()[0], lifecycleNode);
      try
      {
         testcase.runTest();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("phase-listener must not be empty", afe.getMessage());
      }
   }

}
