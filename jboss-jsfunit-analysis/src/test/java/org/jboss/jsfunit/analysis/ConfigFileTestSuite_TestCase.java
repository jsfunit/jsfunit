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

import junit.framework.Test;
import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.model.AtomicIntegerValidator;
import org.jboss.jsfunit.analysis.model.Pojo;
import org.jboss.jsfunit.analysis.model.TestActionListener;
import org.jboss.jsfunit.analysis.model.TestComponent;
import org.jboss.jsfunit.analysis.model.TestConverter;
import org.jboss.jsfunit.analysis.model.TestLifecycleFactory;
import org.jboss.jsfunit.analysis.model.TestPhaseListener;

/**
 * A ConfigFileTestSuite_TestCase.
 * 
 * @author <a href="alejesse@gamil.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class ConfigFileTestSuite_TestCase extends TestCase
{
   private static final String CORRECT = "<validator>" + "<validator-id>javax.faces.DoubleRange</validator-id>"
         + "<validator-class>"
         + AtomicIntegerValidator.class.getName()
         + "</validator-class>"
         + "</validator>"
         + "<application><action-listener>"
         + TestActionListener.class.getName()
         + "</action-listener></application>"
         + "<factory><lifecycle-factory>"
         + TestLifecycleFactory.class.getName()
         + "</lifecycle-factory></factory>"
         + "<lifecycle><phase-listener>"
         + TestPhaseListener.class.getName()
         + "</phase-listener></lifecycle>"
         + "<converter><converter-id>testIdConverter</converter-id>"
         + "<converter-class>"
         + TestConverter.class.getName()
         + "</converter-class>"
         + "</converter>"
         + "<component>"
         + "<component-class>"
         + TestComponent.class.getName()
         + "</component-class>"
         + "</component>"
         + "<render-kit><render-kit-id>myRenderKit</render-kit-id></render-kit>"
         + "<render-kit></render-kit>"
         + "<managed-bean></managed-bean>"
         + "<navigation-rule>"
         + "<description>Test Navigation Rule</description><display-name>MyNavigationRule</display-name>"
         + "<from-view-id>/pages/myFromView.jsp</from-view-id>"
         + "<navigation-case>"
         + "<display-name>MyNavigationCase</display-name>"
         + "<to-view-id>/pages/myToView1.jsp</to-view-id>"
         + "</navigation-case>" + "</navigation-rule>";

   private static final String MINIMAL = "";

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ConfigFileTestSuite#getSuite(java.util.List)}.
    */
   public void testNullPath()
   {
      ConfigFileTestSuite testSuite = new ConfigFileTestSuite("ConfigFileTestSuite_TestCase");
      try
      {
         Test emptyTest = testSuite.getSuite(null, null);
         if (emptyTest.countTestCases() != 0)
         {
            fail("Test should have no test cases...");
         }
      }
      catch (RuntimeException re)
      {
         assertEquals("Invalid input: null", re.getMessage());
      }
      catch (Throwable t)
      {
         fail("should have failed with RuntimeException and not another Throwable");
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ConfigFileTestSuite#getSuite(java.util.List)}.
    */
   public void testEmptyPath()
   {
      ConfigFileTestSuite testSuite = new ConfigFileTestSuite("ConfigFileTestSuite_TestCase");
      try
      {
         Test emptyTest = testSuite.getSuite("", null);
         if (emptyTest.countTestCases() != 0)
         {
            fail("Test should have no test cases...");
         }
      }
      catch (Throwable t)
      {
         fail("should not have failed");
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ConfigFileTestSuite#getSuite(java.util.List)}.
    */
   public void testNotExistingPath()
   {
      String configFile = "not/existing/path/file.xml";
      ConfigFileTestSuite testSuite = new ConfigFileTestSuite("ConfigFileTestSuite_TestCase");
      try
      {
         testSuite.getSuite(configFile, null);
         fail("should have failed.");
      }
      catch (RuntimeException re)
      {
         assertEquals("Could not locate file 'not/existing/path/file.xml'", re.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ConfigFileTestSuite#getSuite(java.util.List)}.
    */
   public void testEmptyFacesConfiguration()
   {
      String configFile = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      StreamProvider streamProvider = new StringStreamProvider(TestUtils.getFacesConfig(""));
      ConfigFileTestSuite testSuite = new ConfigFileTestSuite("ConfigFileTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      try
      {
         Test emptyTest = testSuite.getSuite(configFile, null);
         if (emptyTest.countTestCases() != 0)
         {
            fail("Test should have no test cases...");
         }
      }
      catch (Throwable t)
      {
         fail("should not have failed");
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ConfigFileTestSuite#getSuite(java.util.List)}.
    */
   public void testMalFormed()
   {
      String configFile = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      List<String> configFiles = new ArrayList<String>();
      configFiles.add((String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0]);
      StreamProvider streamProvider = new StringStreamProvider(TestUtils.getFacesConfig("<"));
      ConfigFileTestSuite testSuite = new ConfigFileTestSuite("ConfigFileTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      try
      {
         Test emptyTest = testSuite.getSuite(configFile, configFiles);
         if (emptyTest.countTestCases() != 0)
         {
            fail("Test should have no test cases...");
         }
         fail("malformed xml should fail");
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

   public void testMinimalHappyPath()
   {
      String configFile = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      List<String> configFiles = new ArrayList<String>();
      configFiles.add((String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0]);
      String facesConfig = TestUtils.getFacesConfig(MINIMAL);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);

      ConfigFileTestSuite testSuite = new ConfigFileTestSuite("ConfigFileTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFile, configFiles);
      assertEquals(0, test.countTestCases());
   }

   public void testHappyPaths()
   {
      String configFile = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      List<String> configFiles = new ArrayList<String>();
      configFiles.add((String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0]);
      String manageBean = TestUtils.getManagedBean("good", Pojo.class, "none");
      String facesConfig = TestUtils.getFacesConfig(manageBean + CORRECT);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);

      ConfigFileTestSuite testSuite = new ConfigFileTestSuite("ConfigFileTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFile, configFiles);
      assertEquals(12, test.countTestCases());
   }

   public void testCanHandleMoreThanOneConfigFile()
   {
      String configFile = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      List<String> configFiles = new ArrayList<String>();
      configFiles.add("stubbed resource path");
      configFiles.add("second stubbed resource path");
      String managedBean = TestUtils.getManagedBean("good1", Pojo.class, "none");
      String facesConfig = TestUtils.getFacesConfig(managedBean + CORRECT);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      ConfigFileTestSuite testSuite = new ConfigFileTestSuite("ConfigFileTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFile, configFiles);
      assertEquals(12, test.countTestCases());
   }
}