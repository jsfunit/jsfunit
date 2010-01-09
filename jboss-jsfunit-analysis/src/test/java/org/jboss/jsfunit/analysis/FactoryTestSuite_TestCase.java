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
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import junit.framework.Test;
import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.model.Pojo;
import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A FactoryTestSuite_TestCase.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class FactoryTestSuite_TestCase extends TestCase
{
   private static final String CORRECT = "<factory>"
         + "<application-factory>org.jboss.jsfunit.analysis.modell.TestApplicationFactory</application-factory>"
         + "<faces-context-factory>org.jboss.jsfunit.analysis.modell.TestFacesContextFactory</faces-context-factory>"
         + "<lifecycle-factory>org.jboss.jsfunit.analysis.modell.TestLifecycleFactory</lifecycle-factory>"
         + "<render-kit-factory>org.jboss.jsfunit.analysis.modell.TestRenderKitFactory</render-kit-factory>"
         + "</factory>";

   /**
    *  <p>The stream provider to be used, default: DefaultStreamProvider.
    *  This allows for special purposes to change the stream provider for the file-accessing methods.</p> 
    *  
    */
   private StreamProvider streamProvider = null;

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.FactoryTestSuite#getSuite(java.util.List)}.
    */
   public void testNullPath()
   {
      FactoryTestSuite testSuite = new FactoryTestSuite("FactoryTestSuite_TestCase");
      Test test = testSuite.getSuite(null, null, null);
      assertEquals(0, test.countTestCases());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.FactoryTestSuite#getSuite(java.util.List)}.
    */
   public void testEmptyPath()
   {
      FactoryTestSuite testSuite = new FactoryTestSuite("FactoryTestSuite_TestCase");
      Test test = testSuite.getSuite("", null, null);
      assertEquals(0, test.countTestCases());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.FactoryTestSuite#getSuite(java.util.List)}.
    */
   public void testNotExistingPath()
   {
      String configFilePath = "not/exsiting/path/file.xml";
      FactoryTestSuite testSuite = new FactoryTestSuite("FactoryTestSuite_TestCase");
      Test test = testSuite.getSuite(configFilePath, null, null);
      assertEquals(0, test.countTestCases());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.FactoryTestSuite#getSuite(java.util.List)}.
    */
   public void testEmptyFacesConfiguration()
   {
      String configFilePath = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      StreamProvider streamProvider = new StringStreamProvider(TestUtils.getFacesConfig(""));
      FactoryTestSuite testSuite = new FactoryTestSuite("FactoryTestSuite_TestCase");
      setStreamProvider(streamProvider);
      testSuite.setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFilePath, getFactoryNode(getDomDocument(configFilePath), configFilePath),
            null);
      assertEquals(0, test.countTestCases());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.FactoryTestSuite#getSuite(java.util.List)}.
    */
   public void testEmptyFactory()
   {
      String configFile = "<factory></factory>";
      String configFilePath = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      StreamProvider streamProvider = new StringStreamProvider(TestUtils.getFacesConfig(configFile));
      FactoryTestSuite testSuite = new FactoryTestSuite("FactoryTestSuite_TestCase");
      setStreamProvider(streamProvider);
      testSuite.setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFilePath, getFactoryNode(getDomDocument(configFilePath), configFilePath),
            null);
      assertEquals(0, test.countTestCases());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.FactoryTestSuite#getSuite(java.util.List)}.
    */
   public void testEmptyApplicationFactory()
   {
      String configFile = "<factory>" + "<application-factory></application-factory>" + "</factory>";
      String configFilePath = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      StreamProvider streamProvider = new StringStreamProvider(TestUtils.getFacesConfig(configFile));
      FactoryTestSuite testSuite = new FactoryTestSuite("FactoryTestSuite_TestCase");
      setStreamProvider(streamProvider);
      testSuite.setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFilePath, getFactoryNode(getDomDocument(configFilePath), configFilePath),
            null);
      assertEquals(1, test.countTestCases());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.FactoryTestSuite#getSuite(java.util.List)}.
    */
   public void testEmptyFacesContextFactory()
   {
      String configFile = "<factory>" + "<faces-context-factory></faces-context-factory>" + "</factory>";
      String configFilePath = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      StreamProvider streamProvider = new StringStreamProvider(TestUtils.getFacesConfig(configFile));
      FactoryTestSuite testSuite = new FactoryTestSuite("FactoryTestSuite_TestCase");
      setStreamProvider(streamProvider);
      testSuite.setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFilePath, getFactoryNode(getDomDocument(configFilePath), configFilePath),
            null);
      assertEquals(1, test.countTestCases());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.FactoryTestSuite#getSuite(java.util.List)}.
    */
   public void testEmptyLifecycleFactory()
   {
      String configFile = "<factory>" + "<lifecycle-factory></lifecycle-factory>" + "</factory>";
      String configFilePath = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      StreamProvider streamProvider = new StringStreamProvider(TestUtils.getFacesConfig(configFile));
      FactoryTestSuite testSuite = new FactoryTestSuite("FactoryTestSuite_TestCase");
      setStreamProvider(streamProvider);
      testSuite.setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFilePath, getFactoryNode(getDomDocument(configFilePath), configFilePath),
            null);
      assertEquals(1, test.countTestCases());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.FactoryTestSuite#getSuite(java.util.List)}.
    */
   public void testEmptyRenderKitFactory()
   {
      String configFile = "<factory>" + "<render-kit-factory></render-kit-factory>" + "</factory>";
      String configFilePath = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      StreamProvider streamProvider = new StringStreamProvider(TestUtils.getFacesConfig(configFile));
      FactoryTestSuite testSuite = new FactoryTestSuite("FactoryTestSuite_TestCase");
      setStreamProvider(streamProvider);
      testSuite.setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFilePath, getFactoryNode(getDomDocument(configFilePath), configFilePath),
            null);
      assertEquals(1, test.countTestCases());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.FactoryTestSuite#getSuite(java.util.List)}.
    */
   public void testDuplicateApplicationFactory()
   {
      String configFile = "<factory>"
            + "<application-factory>org.jboss.jsfunit.analysis.modell.TestApplicationFactory</application-factory>"
            + "<application-factory>org.jboss.jsfunit.analysis.modell.TestApplicationFactory</application-factory>"
            + "</factory>";
      String configFilePath = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      StreamProvider streamProvider = new StringStreamProvider(TestUtils.getFacesConfig(configFile));
      FactoryTestSuite testSuite = new FactoryTestSuite("FactoryTestSuite_TestCase");
      setStreamProvider(streamProvider);
      testSuite.setStreamProvider(streamProvider);
      try
      {
         testSuite.getSuite(configFilePath, getFactoryNode(getDomDocument(configFilePath), configFilePath), null);
         fail("should have failed");
      }
      catch (RuntimeException re)
      {
         assertEquals("only one application factory node may be specified in an factory node", re.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.FactoryTestSuite#getSuite(java.util.List)}.
    */
   public void testDuplicateFacesContextFactory()
   {
      String configFile = "<factory>"
            + "<faces-context-factory>org.jboss.jsfunit.analysis.modell.TestFacesContextFactory</faces-context-factory>"
            + "<faces-context-factory>org.jboss.jsfunit.analysis.modell.TestFacesContextFactory</faces-context-factory>"
            + "</factory>";
      String configFilePath = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      StreamProvider streamProvider = new StringStreamProvider(TestUtils.getFacesConfig(configFile));
      FactoryTestSuite testSuite = new FactoryTestSuite("FactoryTestSuite_TestCase");
      setStreamProvider(streamProvider);
      testSuite.setStreamProvider(streamProvider);
      try
      {
         testSuite.getSuite(configFilePath, getFactoryNode(getDomDocument(configFilePath), configFilePath), null);
         fail("should have failed");
      }
      catch (RuntimeException re)
      {
         assertEquals("only one faces context factory node may be specified in an factory node", re.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.FactoryTestSuite#getSuite(java.util.List)}.
    */
   public void testDuplicateLifecycleFactory()
   {
      String configFile = "<factory>"
            + "<lifecycle-factory>org.jboss.jsfunit.analysis.modell.TestLifecycleFactory</lifecycle-factory>"
            + "<lifecycle-factory>org.jboss.jsfunit.analysis.modell.TestLifecycleFactory</lifecycle-factory>"
            + "</factory>";
      String configFilePath = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      StreamProvider streamProvider = new StringStreamProvider(TestUtils.getFacesConfig(configFile));
      FactoryTestSuite testSuite = new FactoryTestSuite("FactoryTestSuite_TestCase");
      setStreamProvider(streamProvider);
      testSuite.setStreamProvider(streamProvider);
      try
      {
         testSuite.getSuite(configFilePath, getFactoryNode(getDomDocument(configFilePath), configFilePath), null);
         fail("should have failed");
      }
      catch (RuntimeException re)
      {
         assertEquals("only one lifecycle factory node may be specified in an factory node", re.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.FactoryTestSuite#getSuite(java.util.List)}.
    */
   public void testDuplicateRenderKitFactory()
   {
      String configFile = "<factory>"
            + "<render-kit-factory>org.jboss.jsfunit.analysis.modell.TestRenderKitFactory</render-kit-factory>"
            + "<render-kit-factory>org.jboss.jsfunit.analysis.modell.TestRenderKitFactory</render-kit-factory>"
            + "</factory>";
      String configFilePath = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      StreamProvider streamProvider = new StringStreamProvider(TestUtils.getFacesConfig(configFile));
      FactoryTestSuite testSuite = new FactoryTestSuite("FactoryTestSuite_TestCase");
      setStreamProvider(streamProvider);
      testSuite.setStreamProvider(streamProvider);
      try
      {
         testSuite.getSuite(configFilePath, getFactoryNode(getDomDocument(configFilePath), configFilePath), null);
         fail("should have failed");
      }
      catch (RuntimeException re)
      {
         assertEquals("only one render kit factory node may be specified in an factory node", re.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.FactoryTestSuite#getSuite(java.util.List)}.
    */
   public void testMalFormed()
   {
      String configFilePath = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      List<String> configFiles = new ArrayList<String>();
      configFiles.add((String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0]);
      StreamProvider streamProvider = new StringStreamProvider(TestUtils.getFacesConfig("<"));
      FactoryTestSuite testSuite = new FactoryTestSuite("FactoryTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFilePath, null, configFiles);
      assertEquals(0, test.countTestCases());
   }

   public void testHappyPaths()
   {
      String configFilePath = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      List<String> configFiles = new ArrayList<String>();
      configFiles.add((String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0]);
      String manageBean = TestUtils.getManagedBean("good", Pojo.class, "none");
      String facesConfig = TestUtils.getFacesConfig(manageBean + CORRECT);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);

      FactoryTestSuite testSuite = new FactoryTestSuite("FactoryTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFilePath, getFactoryNode(getDomDocument(configFilePath), configFilePath),
            configFiles);
      assertEquals(4, test.countTestCases());
   }

   public void testCanHandleMoreThanOneConfigFile()
   {
      String configFilePath = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      List<String> configFiles = new ArrayList<String>();
      configFiles.add("stubbed resource path");
      configFiles.add("second stubbed resource path");
      String managedBean = TestUtils.getManagedBean("good1", Pojo.class, "none");
      String facesConfig = TestUtils.getFacesConfig(managedBean + CORRECT);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      FactoryTestSuite testSuite = new FactoryTestSuite("FactoryTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFilePath, getFactoryNode(getDomDocument(configFilePath), configFilePath),
            configFiles);
      assertEquals(4, test.countTestCases());
   }

   public void testNullStreamProvider()
   {
      FactoryTestSuite testSuite = new FactoryTestSuite("FactoryTestSuite_TestCase");
      Object streamProvider = testSuite.getStreamProvider();
      assertNotNull("TestSuite does not provide default StreamProvider", streamProvider);
      assertTrue("TestSuite does not provide default StreamProvider of correct implementation",
            (streamProvider instanceof StreamProvider));
   }

   public void testStreamProviderAccessors()
   {
      String facesConfig = TestUtils.getFacesConfig(CORRECT);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      FactoryTestSuite testSuite = new FactoryTestSuite("FactoryTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      StreamProvider streamProviderReturned = testSuite.getStreamProvider();
      assertNotNull("TestSuite does not returned passed StreamProvider", streamProviderReturned);
      assertTrue("TestSuite does not returned passed StreamProvider", (streamProvider == streamProviderReturned));
   }

   /**
    * Extract the names and the DOM-Nodes for the factory config node defined in the config file.
    * 
    * @param domDocument the preparsed config-file
    * @param configFilePath path to the config file
    * @return a dom node with the view handler name as a String key and the DOM-Node as the data content
    */
   private Node getFactoryNode(Document domDocument, String configFilePath)
   {
      Node result = null;
      String xpathFactory = "//factory";

      NodeList factories = ParserUtils.query(domDocument, xpathFactory, configFilePath);
      switch (factories.getLength())
      {
         case 0 : {
            // no application level definitions
            break;
         }
         case 1 : {
            result = factories.item(0);
            break;
         }
         default : {
            throw new RuntimeException("only one factory node may be specified in a config file");
         }
      }

      return result;
   }

   /**
    * Setup the DOM parser for the file specified.
    * 
    * @param filePath the path to the file to be parsed
    * @return a DOM Document
    */
   private Document getDomDocument(String filePath)
   {
      String xml = ParserUtils.getXml(filePath, getStreamProvider());
      DocumentBuilder builder = ParserUtils.getDocumentBuilder();
      Document document = null;
      try
      {
         document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
      }
      catch (Exception e)
      {
         throw new RuntimeException("Could not parse document '" + filePath + "'\n" + xml, e);
      }
      return document;
   }

   /**
    * Accessor for the streamProvider attribute
    * 
    * @return the streamprovider
    */
   public StreamProvider getStreamProvider()
   {
      if (streamProvider == null)
      {
         streamProvider = new DefaultStreamProvider();
      }
      return streamProvider;
   }

   /**
    * <p>Accessor for the streamProvider attribute.
    * Usually used to set a special StreamProvider. By default a DefaultStreamProvider is created.</p>
    * 
    * @param streamProvider the new StreamProvider
    */
   public void setStreamProvider(StreamProvider streamProvider)
   {
      this.streamProvider = streamProvider;
   }
}