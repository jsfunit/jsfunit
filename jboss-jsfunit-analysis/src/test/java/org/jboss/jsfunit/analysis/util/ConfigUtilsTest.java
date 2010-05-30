/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.jsfunit.analysis.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;

import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.StreamProvider;
import org.jboss.jsfunit.analysis.StringStreamProvider;
import org.jboss.jsfunit.analysis.Utilities;
import org.jboss.jsfunit.analysis.util.ConfigUtils.ConfigItemType;
import org.w3c.dom.Document;

/**
 * A ConfigUtilsTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ConfigUtilsTest extends TestCase
{
   private static final String DEFAULT_CONFIGFILE_CORRECT = "<render-kit>"
      + "<renderer><component-family/><renderer-type>testComp</renderer-type>"
      + "<renderer-class>FooComp</renderer-class></renderer>" + "</render-kit>";
   private static final String MY_CONFIGFILE_CORRECT = "<render-kit>"
      + "<render-kit-id>myRenderKit</render-kit-id><render-kit-class>Foo</render-kit-class>"
      + "<renderer><component-family/><renderer-type>testComp</renderer-type>"
      + "<renderer-class>FooComp</renderer-class></renderer>" + "</render-kit>";

   private List<String> dummyPaths = null;
   
   private Document emptyConfigFile = null;
   private Document defaultConfigFile = null;
   private Document myConfigFile = null;

   protected void setUp() throws Exception
   {
      super.setUp();
      dummyPaths = new ArrayList<String>();
      dummyPaths.add((String) (Utilities.STUBBED_RESOURCEPATH.toArray()[0]));
      
      emptyConfigFile = getDomDocument(new ByteArrayInputStream(Utilities.getFacesConfig("").getBytes()));
      defaultConfigFile = getDomDocument(new ByteArrayInputStream(Utilities.getFacesConfig(DEFAULT_CONFIGFILE_CORRECT).getBytes()));
      myConfigFile = getDomDocument(new ByteArrayInputStream(Utilities.getFacesConfig(MY_CONFIGFILE_CORRECT).getBytes()));
   }

   protected void tearDown() throws Exception
   {
      super.tearDown();
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ConfigUtils#isConfigured(org.jboss.jsfunit.analysis.util.ConfigUtils.ConfigItemType, java.lang.String, boolean)}.
    */
   public void testIsConfigured()
   {
      ConfigUtils configUtils = new ConfigUtils();
      configUtils.setConfigFilePaths(dummyPaths);
      StreamProvider streamProvider = new StringStreamProvider(Utilities.getFacesConfig(MY_CONFIGFILE_CORRECT));
      configUtils.setStreamProvider(streamProvider);
      assertTrue(configUtils.isConfigured(ConfigItemType.RENDER_KIT, "myRenderKit", false));
      configUtils.setConfigFilePaths(new ArrayList<String>());
      assertTrue(configUtils.isConfigured(ConfigItemType.RENDER_KIT, "myRenderKit", false));
      assertFalse(configUtils.isConfigured(ConfigItemType.RENDER_KIT, "myClassPathRenderKit", false));
      assertTrue(configUtils.isConfigured(ConfigItemType.RENDER_KIT, "myClassPathRenderKit", true));
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ConfigUtils#isConfiguredConfigItemType(org.jboss.jsfunit.analysis.util.ConfigUtils.ConfigItemType, java.lang.String, org.w3c.dom.Document, java.lang.String)}.
    */
   public void testIsConfiguredConfigItemType()
   {
      assertFalse(new ConfigUtils().isConfiguredConfigItemType(ConfigItemType.RENDER_KIT, "notFound", myConfigFile, "my"));
      assertTrue(new ConfigUtils().isConfiguredConfigItemType(ConfigItemType.RENDER_KIT, "myRenderKit", myConfigFile, "my"));
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ConfigUtils#extractRenderkitIdsDefined(org.w3c.dom.Document, java.lang.String)}.
    */
   public void testExtractRenderkitIdsDefined()
   {
      List<String> renderKitIdsEmpty = new ConfigUtils().extractRenderkitIdsDefined(emptyConfigFile, "empty");
      assertNotNull(renderKitIdsEmpty);
      assertEquals(0, renderKitIdsEmpty.size());
      List<String> renderKitIdsDefault = new ConfigUtils().extractRenderkitIdsDefined(defaultConfigFile, "default");
      assertNotNull(renderKitIdsDefault);
      assertEquals(1, renderKitIdsDefault.size());
      assertEquals("default", renderKitIdsDefault.get(0));
      List<String> renderKitIds = new ConfigUtils().extractRenderkitIdsDefined(myConfigFile, "my");
      assertNotNull(renderKitIds);
      assertEquals(1, renderKitIds.size());
      assertEquals("myRenderKit", renderKitIds.get(0));
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ConfigUtils#getConfigFilePaths()}.
    */
   public void testGetConfigFilePaths()
   {
      ConfigUtils configUtils = new ConfigUtils();
      List<String> configFilePaths = configUtils.getConfigFilePaths();
      assertNotNull(configFilePaths);
      assertTrue(configFilePaths.isEmpty());
      assertEquals(0,configFilePaths.size());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ConfigUtils#setConfigFilePaths(java.util.List)}.
    */
   public void testSetConfigFilePaths()
   {
      ConfigUtils configUtils = new ConfigUtils();
      configUtils.setConfigFilePaths(dummyPaths);

      List<String> configFilePaths = configUtils.getConfigFilePaths();
      assertNotNull(configFilePaths);
      assertSame(dummyPaths, configFilePaths);
      assertFalse(configFilePaths.isEmpty());
      assertEquals(1,configFilePaths.size());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ConfigUtils#getConfigFileDoms()}.
    */
   public void testGetConfigFileDoms()
   {
      ConfigUtils configUtils = new ConfigUtils();
      Map<String, Document> configFileDoms =configUtils.getConfigFileDoms();
      assertNotNull(configFileDoms);
      assertTrue(configFileDoms.isEmpty());
      assertEquals(0,configFileDoms.size());
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.util.ConfigUtils#setConfigFileDoms(java.util.Map)}.
    */
   public void testSetConfigFileDoms()
   {
      Map<String, Document> configFileDoms = new HashMap<String, Document>();
      configFileDoms.put("Dummy", defaultConfigFile);
      
      ConfigUtils configUtils = new ConfigUtils();
      configUtils.setConfigFileDoms(configFileDoms);
      Map<String, Document> configFileDomsReturnded =configUtils.getConfigFileDoms();
      assertNotNull(configFileDomsReturnded);
      assertFalse(configFileDomsReturnded.isEmpty());
      assertEquals(1,configFileDomsReturnded.size());
      assertSame(configFileDoms,configFileDomsReturnded);
   }

   public void testNullStreamProvider()
   {
      ConfigUtils configUtils = new ConfigUtils();
      Object streamProvider = configUtils.getStreamProvider();
      assertNotNull("ConfigUtils does not provide default StreamProvider", streamProvider);
      assertTrue("ConfigUtils does not provide default StreamProvider of correct implementation",
            (streamProvider instanceof StreamProvider));
   }

   public void testStreamProviderAccessors()
   {
      String facesConfig = Utilities.getFacesConfig("");
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      ConfigUtils testCase = new ConfigUtils();
      testCase.setStreamProvider(streamProvider);
      StreamProvider streamProviderReturned = testCase.getStreamProvider();
      assertNotNull("ConfigUtils does not returned passed StreamProvider", streamProviderReturned);
      assertSame("ConfigUtils does not returned passed StreamProvider", streamProvider, streamProviderReturned);
   }
   
   private Document getDomDocument(InputStream file)
   {
      DocumentBuilder builder = ParserUtils.getDocumentBuilder();
      Document document = null;
      try
      {
         document = builder.parse(file);
      }
      catch (Exception e)
      {
         throw new RuntimeException("Could not parse document \n" + file.toString(), e);
      }
      return document;
   }
}
