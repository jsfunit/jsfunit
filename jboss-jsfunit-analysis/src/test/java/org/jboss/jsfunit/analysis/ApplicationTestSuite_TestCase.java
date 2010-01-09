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
 * A ApplicationTestSuite_TestCase.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class ApplicationTestSuite_TestCase extends TestCase
{
   private static final String CORRECT = "<application>"
         + "<action-listener>org.jboss.jsfunit.analysis.modell.TestActionListener</action-listener>"
         + "<navigation-handler>org.jboss.jsfunit.analysis.modell.TestNavigationHandler</navigation-handler>"
         + "<state-manager>org.jboss.jsfunit.analysis.modell.TestStateManager</state-manager>"
         + "<view-handler>org.jboss.jsfunit.analysis.modell.TestViewHandler</view-handler>"
         + "<default-render-kit-id>testRenderKit</default-render-kit-id>"
         + "<el-resolver id=\"workingElResolver\">org.jboss.jsfunit.analysis.modell.TestElResolver</el-resolver>"
         + "<el-resolver>org.jboss.jsfunit.analysis.modell.TestElResolver</el-resolver>"
         + "<property-resolver id=\"workingPropertyResolver\">org.jboss.jsfunit.analysis.modell.TestPropertyResolver</property-resolver>"
         + "<property-resolver>org.jboss.jsfunit.analysis.modell.TestPropertyResolver</property-resolver>"
         + "<variable-resolver id=\"workingVariableResolver\">org.jboss.jsfunit.analysis.modell.TestVariableResolver</variable-resolver>"
         + "<variable-resolver>org.jboss.jsfunit.analysis.modell.TestVariableResolver</variable-resolver>"
         + "</application>" + " <render-kit><render-kit-id>testRenderKit</render-kit-id></render-kit>";

   /**
    *  <p>The stream provider to be used, default: DefaultStreamProvider.
    *  This allows for special purposes to change the stream provider for the file-accessing methods.</p> 
    *  
    */
   private StreamProvider streamProvider = null;

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ApplicationTestSuite#getSuite(java.util.List)}.
    */
   public void testNullPath()
   {
      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      Test test = testSuite.getSuite(null, null, null);
      assertEquals(0, test.countTestCases());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ApplicationTestSuite#getSuite(java.util.List)}.
    */
   public void testEmptyPath()
   {
      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      Test test = testSuite.getSuite("", null, null);
      assertEquals(0, test.countTestCases());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ApplicationTestSuite#getSuite(java.util.List)}.
    */
   public void testNotExistingPath()
   {
      String configFile = "not/exsiting/path/file.xml";
      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      Test test = testSuite.getSuite(configFile, null, null);
      assertEquals(0, test.countTestCases());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ApplicationTestSuite#getSuite(java.util.List)}.
    */
   public void testEmptyFacesConfiguration()
   {
      String configFile = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      StreamProvider streamProvider = new StringStreamProvider(TestUtils.getFacesConfig(""));
      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      setStreamProvider(streamProvider);
      testSuite.setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFile, getApplicationNode(getDomDocument(configFile), configFile), null);
      assertEquals(0, test.countTestCases());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ApplicationTestSuite#getSuite(java.util.List)}.
    */
   public void testMalFormed()
   {
      String configFile = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      List<String> configFiles = new ArrayList<String>();
      configFiles.add((String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0]);
      StreamProvider streamProvider = new StringStreamProvider(TestUtils.getFacesConfig("<"));
      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFile, null, configFiles);
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

      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFile, getApplicationNode(getDomDocument(configFile), configFile),
            configFiles);
      assertEquals(11, test.countTestCases());
   }

   public void testHappyPaths2()
   {
      String configFile = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      String manageBean = TestUtils.getManagedBean("good", Pojo.class, "none");
      String facesConfig = TestUtils.getFacesConfig(manageBean + CORRECT);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);

      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFile, getApplicationNode(getDomDocument(configFile), configFile), null);
      assertEquals(11, test.countTestCases());
   }

   public void testHappyPathsEmptyApplication()
   {
      String configFile = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      String manageBean = TestUtils.getManagedBean("good", Pojo.class, "none");
      String facesConfig = TestUtils.getFacesConfig(manageBean + "<application></application>");
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);

      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFile, getApplicationNode(getDomDocument(configFile), configFile), null);
      assertEquals(0, test.countTestCases());
   }

   public void testDuplicateActionListener()
   {
      String configFile = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      String manageBean = TestUtils.getManagedBean("good", Pojo.class, "none");
      String application = "<application>"
            + "<action-listener>org.jboss.jsfunit.analysis.modell.TestActionListener</action-listener>"
            + "<action-listener>org.jboss.jsfunit.analysis.modell.TestActionListener</action-listener>"
            + "</application>";
      String facesConfig = TestUtils.getFacesConfig(manageBean + application);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);

      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      setStreamProvider(streamProvider);
      try
      {
         testSuite.getSuite(configFile, getApplicationNode(getDomDocument(configFile), configFile), null);
         fail("should have failed");
      }
      catch (RuntimeException re)
      {
         assertEquals("only one action listener node may be specified in an application node", re.getMessage());
      }
   }

   public void testEmptyActionListener()
   {
      String configFile = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      String manageBean = TestUtils.getManagedBean("good", Pojo.class, "none");
      String application = "<application>" + "<action-listener></action-listener>" + "</application>";
      String facesConfig = TestUtils.getFacesConfig(manageBean + application);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);

      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFile, getApplicationNode(getDomDocument(configFile), configFile), null);
      assertEquals(0, test.countTestCases());
   }

   public void testDuplicateNavigationHandler()
   {
      String configFile = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      String manageBean = TestUtils.getManagedBean("good", Pojo.class, "none");
      String application = "<application>"
            + "<navigation-handler>org.jboss.jsfunit.analysis.modell.TestNavigationHandler</navigation-handler>"
            + "<navigation-handler>org.jboss.jsfunit.analysis.modell.TestNavigationHandler</navigation-handler>"
            + "</application>";
      String facesConfig = TestUtils.getFacesConfig(manageBean + application);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);

      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      setStreamProvider(streamProvider);
      try
      {
         testSuite.getSuite(configFile, getApplicationNode(getDomDocument(configFile), configFile), null);
         fail("should have failed");
      }
      catch (RuntimeException re)
      {
         assertEquals("only one navigation handler node may be specified in an application node", re.getMessage());
      }
   }

   public void testEmptyNavigationHandler()
   {
      String configFile = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      String manageBean = TestUtils.getManagedBean("good", Pojo.class, "none");
      String application = "<application>" + "<navigation-handler></navigation-handler>" + "</application>";
      String facesConfig = TestUtils.getFacesConfig(manageBean + application);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);

      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFile, getApplicationNode(getDomDocument(configFile), configFile), null);
      assertEquals(0, test.countTestCases());
   }

   public void testDuplicateStateManager()
   {
      String configFile = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      String manageBean = TestUtils.getManagedBean("good", Pojo.class, "none");
      String application = "<application>"
            + "<state-manager>org.jboss.jsfunit.analysis.modell.TestStateManager</state-manager>"
            + "<state-manager>org.jboss.jsfunit.analysis.modell.TestStateManager</state-manager>" + "</application>";
      String facesConfig = TestUtils.getFacesConfig(manageBean + application);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);

      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      setStreamProvider(streamProvider);
      try
      {
         testSuite.getSuite(configFile, getApplicationNode(getDomDocument(configFile), configFile), null);
         fail("should have failed");
      }
      catch (RuntimeException re)
      {
         assertEquals("only one state manager node may be specified in an application node", re.getMessage());
      }
   }

   public void testEmptyStateManager()
   {
      String configFile = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      String manageBean = TestUtils.getManagedBean("good", Pojo.class, "none");
      String application = "<application>" + "<state-manager></state-manager>" + "</application>";
      String facesConfig = TestUtils.getFacesConfig(manageBean + application);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);

      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFile, getApplicationNode(getDomDocument(configFile), configFile), null);
      assertEquals(0, test.countTestCases());
   }

   public void testDuplicateViewHandler()
   {
      String configFile = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      String manageBean = TestUtils.getManagedBean("good", Pojo.class, "none");
      String application = "<application>"
            + "<view-handler>org.jboss.jsfunit.analysis.modell.TestViewHandler</view-handler>"
            + "<view-handler>org.jboss.jsfunit.analysis.modell.TestViewHandler</view-handler>" + "</application>";
      String facesConfig = TestUtils.getFacesConfig(manageBean + application);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);

      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      setStreamProvider(streamProvider);
      try
      {
         testSuite.getSuite(configFile, getApplicationNode(getDomDocument(configFile), configFile), null);
         fail("should have failed");
      }
      catch (RuntimeException re)
      {
         assertEquals("only one view handler node may be specified in an application node", re.getMessage());
      }
   }

   public void testEmptyViewHandler()
   {
      String configFile = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];
      String manageBean = TestUtils.getManagedBean("good", Pojo.class, "none");
      String application = "<application>" + "<view-handler></view-handler>" + "</application>";
      String facesConfig = TestUtils.getFacesConfig(manageBean + application);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);

      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFile, getApplicationNode(getDomDocument(configFile), configFile), null);
      assertEquals(0, test.countTestCases());
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
      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFile, getApplicationNode(getDomDocument(configFile), configFile),
            configFiles);
      assertEquals(11, test.countTestCases());
   }

   public void testNullStreamProvider()
   {
      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      Object streamProvider = testSuite.getStreamProvider();
      assertNotNull("TestSuite does not provide default StreamProvider", streamProvider);
      assertTrue("TestSuite does not provide default StreamProvider of correct implementation",
            (streamProvider instanceof StreamProvider));
   }

   public void testStreamProviderAccessors()
   {
      String facesConfig = TestUtils.getFacesConfig(CORRECT);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      ApplicationTestSuite testSuite = new ApplicationTestSuite("ApplicationTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      StreamProvider streamProviderReturned = testSuite.getStreamProvider();
      assertNotNull("TestSuite does not returned passed StreamProvider", streamProviderReturned);
      assertTrue("TestSuite does not returned passed StreamProvider", (streamProvider == streamProviderReturned));
   }

   /**
    * Extract the names and the DOM-Nodes for the application config node defined in the config file.
    * 
    * @param domDocument the preparsed config-file
    * @param configFilePath path to the config file
    * @return a Map with the view handler name as a String key and the DOM-Node as the data content
    */
   private Node getApplicationNode(Document domDocument, String configFilePath)
   {
      Node result = null;
      String xpathApplication = "/faces-config/application";

      NodeList applications = ParserUtils.query(domDocument, xpathApplication, configFilePath);
      switch (applications.getLength())
      {
         case 0 : {
            // no application level definitions
            break;
         }
         case 1 : {
            result = applications.item(0);
            break;
         }
         default : {
            throw new RuntimeException("only one application node may be specified in a config file");
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