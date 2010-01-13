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

import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A LifecycleTestSuite_TestCase.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class LifecycleTestSuite_TestCase extends TestCase
{
   private static final String CORRECT = "<lifecycle>"
         + "<phase-listener>org.jboss.jsfunit.analysis.modell.TestPhaseListener</phase-listener>"
         + "<phase-listener></phase-listener>" + "</lifecycle>";

   private static final String EMPTY = "<lifecycle></lifecycle>";

   /**
    *  <p>The stream provider to be used, default: DefaultStreamProvider.
    *  This allows for special purposes to change the stream provider for the file-accessing methods.</p> 
    *  
    */
   private StreamProvider streamProvider = null;

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.LifecycleTestSuite#getSuite(java.util.List)}.
    */
   public void testNullPath()
   {
      LifecycleTestSuite testSuite = new LifecycleTestSuite("LifecycleTestSuite_TestCase");
      Test test = testSuite.getSuite(null, null);
      assertEquals(1, test.countTestCases());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.LifecycleTestSuite#getSuite(java.util.List)}.
    */
   public void testEmptyPath()
   {
      LifecycleTestSuite testSuite = new LifecycleTestSuite("LifecycleTestSuite_TestCase");
      Test test = testSuite.getSuite("", null);
      assertEquals(1, test.countTestCases());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.LifecycleTestSuite#getSuite(java.util.List)}.
    */
   public void testNotExistingPath()
   {
      String configFile = "not/exsiting/path/file.xml";
      LifecycleTestSuite testSuite = new LifecycleTestSuite("LifecycleTestSuite_TestCase");
      Test test = testSuite.getSuite(configFile, null);
      assertEquals(1, test.countTestCases());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.LifecycleTestSuite#getSuite(java.util.List)}.
    */
   public void testEmptyFacesConfiguration()
   {
      String configFile = (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0];
      StreamProvider streamProvider = new StringStreamProvider(Utilities.getFacesConfig(""));
      LifecycleTestSuite testSuite = new LifecycleTestSuite("LifecycleTestSuite_TestCase");
      setStreamProvider(streamProvider);
      testSuite.setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFile, getLifecycleNode(getDomDocument(configFile), configFile));
      assertEquals(1, test.countTestCases());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.LifecycleTestSuite#getSuite(java.util.List)}.
    */
   public void testMalFormed()
   {
      String configFile = (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0];
      List<String> configFiles = new ArrayList<String>();
      configFiles.add((String) Utilities.STUBBED_RESOURCEPATH.toArray()[0]);
      StreamProvider streamProvider = new StringStreamProvider(Utilities.getFacesConfig("<"));
      LifecycleTestSuite testSuite = new LifecycleTestSuite("LifecycleTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFile, null);
      assertEquals(1, test.countTestCases());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.LifecycleTestSuite#getSuite(java.util.List)}.
    */
   public void testEmptyLifecycleConfiguration()
   {
      String configFile = (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0];
      List<String> configFiles = new ArrayList<String>();
      configFiles.add((String) Utilities.STUBBED_RESOURCEPATH.toArray()[0]);
      String facesConfig = Utilities.getFacesConfig(EMPTY);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);

      LifecycleTestSuite testSuite = new LifecycleTestSuite("LifecycleTestSuite_TestCase");
      setStreamProvider(streamProvider);
      testSuite.setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFile, getLifecycleNode(getDomDocument(configFile), configFile));
      assertEquals(1, test.countTestCases());
   }

   public void testHappyPaths()
   {
      String configFile = (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0];
      List<String> configFiles = new ArrayList<String>();
      configFiles.add((String) Utilities.STUBBED_RESOURCEPATH.toArray()[0]);
      String facesConfig = Utilities.getFacesConfig(CORRECT);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);

      LifecycleTestSuite testSuite = new LifecycleTestSuite("LifecycleTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      setStreamProvider(streamProvider);
      Test test = testSuite.getSuite(configFile, getLifecycleNode(getDomDocument(configFile), configFile));
      assertEquals(2, test.countTestCases());
   }

   public void testNullStreamProvider()
   {
      LifecycleTestSuite testSuite = new LifecycleTestSuite("LifecycleTestSuite_TestCase");
      Object streamProvider = testSuite.getStreamProvider();
      assertNotNull("TestSuite does not provide default StreamProvider", streamProvider);
      assertTrue("TestSuite does not provide default StreamProvider of correct implementation",
            (streamProvider instanceof StreamProvider));
   }

   public void testStreamProviderAccessors()
   {
      String facesConfig = Utilities.getFacesConfig(CORRECT);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      LifecycleTestSuite testSuite = new LifecycleTestSuite("LifecycleTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      StreamProvider streamProviderReturned = testSuite.getStreamProvider();
      assertNotNull("TestSuite does not returned passed StreamProvider", streamProviderReturned);
      assertTrue("TestSuite does not returned passed StreamProvider", (streamProvider == streamProviderReturned));
   }

   /**
    * Extract the names and the DOM-Nodes for the lifecycle config node defined in the config file.
    * 
    * @param domDocument the preparsed config-file
    * @param configFilePath path to the config file
    * @return a DOM-Node
    */
   private Node getLifecycleNode(Document domDocument, String configFilePath)
   {
      Node result = null;
      String xpathLifecycle = "/faces-config/lifecycle";

      NodeList lifecycles = ParserUtils.query(domDocument, xpathLifecycle, configFilePath);
      switch (lifecycles.getLength())
      {
         case 0 : {
            // no application level definitions
            break;
         }
         case 1 : {
            result = lifecycles.item(0);
            break;
         }
         default : {
            throw new RuntimeException("only one lifecycle node may be specified in a config file");
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