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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.model.AtomicIntegerValidator;
import org.jboss.jsfunit.analysis.model.Pojo;

/**
 * A ConfigFilesTestSuite_TestCase.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class ConfigFilesTestSuite_TestCase extends TestCase
{
   private static final String CORRECT = "<validator>" + "<validator-id>javax.faces.DoubleRange</validator-id>"
         + "<validator-class>" + AtomicIntegerValidator.class.getName() + "</validator-class>" + "</validator>";

   private static final String NON_EXISTING_ACTION_LISTENER = "<application><action-listener>com.nonexist.Foo</action-listener></application>";

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ConfigFilesTestSuite#getSuite(java.util.List)}.
    */
   public void testNullPath()
   {
      ConfigFilesTestSuite testSuite = new ConfigFilesTestSuite("ConfigFilesTestSuite_TestCase");
      try
      {
         testSuite.getSuite(null);
         fail("should have failed with RuntimeException");
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
    * Test method for {@link org.jboss.jsfunit.analysis.ConfigFilesTestSuite#getSuite(java.util.List)}.
    */
   public void testEmptyPath()
   {
      List<String> configFiles = new ArrayList<String>();
      ConfigFilesTestSuite testSuite = new ConfigFilesTestSuite("ConfigFilesTestSuite_TestCase");
      try
      {
         Test emptyTest = testSuite.getSuite(configFiles);
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
    * Test method for {@link org.jboss.jsfunit.analysis.ConfigFilesTestSuite#getSuite(java.util.List)}.
    */
   public void testNotExistingPath()
   {
      List<String> configFiles = new ArrayList<String>();
      configFiles.add("not/exsiting/path/file.xml");
      ConfigFilesTestSuite testSuite = new ConfigFilesTestSuite("ConfigFilesTestSuite_TestCase");
      try
      {
         testSuite.getSuite(configFiles);
         fail("should have failed.");
      }
      catch (RuntimeException re)
      {
         assertEquals("Could not locate file 'not/exsiting/path/file.xml'", re.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ConfigFilesTestSuite#getSuite(java.util.List)}.
    */
   public void testEmptyFacesConfiguration()
   {
      List<String> configFiles = new ArrayList<String>();
      configFiles.add((String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0]);
      StreamProvider streamProvider = new StringStreamProvider(TestUtils.getFacesConfig(""));
      ConfigFilesTestSuite testSuite = new ConfigFilesTestSuite("ConfigFilesTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      try
      {
         Test emptyTest = testSuite.getSuite(configFiles);
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
    * Test method for {@link org.jboss.jsfunit.analysis.ConfigFilesTestSuite#getSuite(java.util.List)}.
    */
   public void testMalFormed()
   {
      List<String> configFiles = new ArrayList<String>();
      configFiles.add((String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0]);
      StreamProvider streamProvider = new StringStreamProvider(TestUtils.getFacesConfig("<"));
      ConfigFilesTestSuite testSuite = new ConfigFilesTestSuite("ConfigFilesTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      try
      {
         Test emptyTest = testSuite.getSuite(configFiles);
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

   public void testHappyPaths()
   {
      List<String> configFiles = new ArrayList<String>();
      configFiles.add((String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0]);
      String manageBean = TestUtils.getManagedBean("good", Pojo.class, "none");
      String facesConfig = TestUtils.getFacesConfig(manageBean + CORRECT);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);

      ConfigFilesTestSuite testSuite = new ConfigFilesTestSuite("ConfigFilesTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFiles);
      assertEquals(2, test.countTestCases());
   }

   public void testCanHandleMoreThanOneConfigFile()
   {
      List<String> configFiles = new ArrayList<String>();
      configFiles.add("stubbed resource path");
      configFiles.add("second stubbed resource path");
      StreamProvider streamProvider = new StreamProvider()
      {
         private InputStream stream;

         String managedBean1 = TestUtils.getManagedBean("good1", Pojo.class, "none");

         String managedBean2 = TestUtils.getManagedBean("good2", Pojo.class, "none");

         String facesConfig1 = TestUtils.getFacesConfig(managedBean1 + CORRECT);

         String facesConfig2 = TestUtils.getFacesConfig(managedBean2 + NON_EXISTING_ACTION_LISTENER);

         public InputStream getInputStream(String path)
         {
            stream = stream == null ? new ByteArrayInputStream(facesConfig1.getBytes()) : new ByteArrayInputStream(
                  facesConfig2.getBytes());
            return stream;
         }
      };
      ConfigFilesTestSuite testSuite = new ConfigFilesTestSuite("ConfigFilesTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFiles);
      assertEquals(4, test.countTestCases());

   }
}