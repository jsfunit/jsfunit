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

import java.util.ArrayList;
import java.util.List;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * A DefaultRenderkitTestCase_TestCase.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class DefaultRenderkitTestCase_TestCase extends TestCase
{
   private static final String DEFAULT_RENDERKIT_CORRECT = "<render-kit>"
         + "<render-kit-id>testHappyPaths</render-kit-id><render-kit-class>Foo</render-kit-class>"
         + "<renderer><component-family/><renderer-type>testComp</renderer-type>"
         + "<renderer-class>FooComp</renderer-class></renderer>" + "</render-kit>" + "<render-kit>"
         + "<renderer><component-family/><renderer-type>testComp</renderer-type>"
         + "<renderer-class>FooComp</renderer-class></renderer>" + "</render-kit>";

   private static List<String> DUMMY_PATHS = new ArrayList<String>();
   {
      DUMMY_PATHS.add((String) (Utilities.STUBBED_RESOURCEPATH.toArray()[0]));
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.DefaultRenderkitTestCase}.
    */
   public void testHappyPaths()
   {
      StreamProvider streamProvider = new StringStreamProvider(Utilities.getFacesConfig(DEFAULT_RENDERKIT_CORRECT));
      DefaultRenderkitTestCase testCase = new DefaultRenderkitTestCase("testHappyPaths", DUMMY_PATHS);
      testCase.setStreamProvider(streamProvider);
      testCase.runTest();
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.DefaultRenderkitTestCase#testClassLoadable()}.
    */
   public void testTestClassLoadable()
   {
      StreamProvider streamProvider = new StringStreamProvider(Utilities.getFacesConfig(DEFAULT_RENDERKIT_CORRECT));
      DefaultRenderkitTestCase testCase = new DefaultRenderkitTestCase("testNotFound", DUMMY_PATHS);
      testCase.setStreamProvider(streamProvider);
      try
      {
         testCase.runTest();
         fail("should have failed");
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("Default renderkit 'testNotFound' is not defined in the config files.", afe.getMessage());
      }
   }

   public void testNullStreamProvider()
   {
      DefaultRenderkitTestCase testCase = new DefaultRenderkitTestCase("DefaultRenderkitTestCase_TestCase", DUMMY_PATHS);
      Object streamProvider = testCase.getStreamProvider();
      assertNotNull("TestCase does not provide default StreamProvider", streamProvider);
      assertTrue("TestCase does not provide default StreamProvider of correct implementation",
            (streamProvider instanceof StreamProvider));
   }

   public void testStreamProviderAccessors()
   {
      String facesConfig = Utilities.getFacesConfig("");
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      DefaultRenderkitTestCase testCase = new DefaultRenderkitTestCase("DefaultRenderkitTestCase_TestCase", DUMMY_PATHS);
      testCase.setStreamProvider(streamProvider);
      StreamProvider streamProviderReturned = testCase.getStreamProvider();
      assertNotNull("TestCase does not returned passed StreamProvider", streamProviderReturned);
      assertTrue("TestCase does not returned passed StreamProvider", (streamProvider == streamProviderReturned));
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ConfigFileTestSuite#getSuite(java.util.List)}.
    */
   public void testMalFormed()
   {
      StreamProvider streamProvider = new StringStreamProvider(Utilities.getFacesConfig("<"));
      DefaultRenderkitTestCase testCase = new DefaultRenderkitTestCase("ConfigFileTestSuite_TestCase", DUMMY_PATHS);
      testCase.setStreamProvider(streamProvider);
      try
      {
         testCase.runTest();
         fail("should have failed");
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("Default renderkit 'testNotFound' is not defined in the config files.", afe.getMessage());
      }
      catch (RuntimeException re)
      {
         assertTrue(re.getMessage().startsWith("Could not parse document 'stubbed resource path'"));
      }
      catch (Throwable t)
      {
         fail("should fail with Runtimeexception");
      }
   }
}
