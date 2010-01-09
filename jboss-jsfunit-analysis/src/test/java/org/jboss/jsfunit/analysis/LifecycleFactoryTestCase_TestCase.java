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
 * A LifecycleFactoryTestCase_TestCase.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class LifecycleFactoryTestCase_TestCase extends TestCase
{
   /**
    * Test method for {@link org.jboss.jsfunit.analysis.LifecycleFactoryTestCase}.
    */
   public void testHappyPaths()
   {
      LifecycleFactoryTestCase testcase = new LifecycleFactoryTestCase("testHappyPaths",
            "org.jboss.jsfunit.analysis.model.TestLifecycleFactory");
      testcase.runTest();
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.LifecycleFactoryTestCase#testClassLoadable()}.
    */
   public void testTestClassLoadable()
   {
      LifecycleFactoryTestCase testcase = new LifecycleFactoryTestCase("testTestClassLoadable", "com.nonexist.Foo");
      try
      {
         testcase.testClassLoadable();
         fail("should have failed");
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("Could not load class 'com.nonexist.Foo' for element 'Lifecycle Factory'", afe.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.LifecycleFactoryTestCase#testInterface()}.
    */
   public void testTestInterface()
   {
      LifecycleFactoryTestCase testcase = new LifecycleFactoryTestCase("testTestInterface",
            "org.jboss.jsfunit.analysis.model.Pojo");
      try
      {
         testcase.testInterface();
         fail("should have failed");
      }
      catch (AssertionFailedError afe)
      {
         assertEquals(
               "Lifecycle Factory 'org.jboss.jsfunit.analysis.model.Pojo' needs to implement class javax.faces.lifecycle.LifecycleFactory",
               afe.getMessage());
      }
   }

}
