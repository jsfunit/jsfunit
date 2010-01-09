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

/**
 * A StateManagerTestCase_TestCase.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class StateManagerTestCase_TestCase extends TestCase
{
   /**
    * Test method for {@link org.jboss.jsfunit.analysis.StateManagerTestCase}.
    */
   public void testHappyPaths()
   {
      StateManagerTestCase testcase = new StateManagerTestCase("testHappyPaths",
            "org.jboss.jsfunit.analysis.model.TestStateManager");
      testcase.runTest();
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.StateManagerTestCase#testClassLoadable()}.
    */
   public void testTestClassLoadable()
   {
      StateManagerTestCase testcase = new StateManagerTestCase("testTestClassLoadable", "com.nonexist.Foo");
      try
      {
         testcase.testClassLoadable();
         fail("should have failed");
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("Could not load class 'com.nonexist.Foo' for element 'State Manager'", afe.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.StateManagerTestCase#testInterface()}.
    */
   public void testTestInterface()
   {
      StateManagerTestCase testcase = new StateManagerTestCase("testTestInterface",
            "org.jboss.jsfunit.analysis.model.Pojo");
      try
      {
         testcase.testInterface();
         fail("should have failed");
      }
      catch (AssertionFailedError afe)
      {
         assertEquals(
               "State Manager 'org.jboss.jsfunit.analysis.model.Pojo' needs to implement class javax.faces.application.StateManager",
               afe.getMessage());
      }
   }

}
