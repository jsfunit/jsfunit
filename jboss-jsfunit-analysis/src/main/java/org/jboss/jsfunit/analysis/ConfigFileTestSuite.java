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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>A TestSuite for a single JSF application config file.
 * To test the faces-config files create a list of config-file path,
 * instantiate this TestSuite and ask for the getSuite().
 * </p>
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $$Revision:  $$
 */
public class ConfigFileTestSuite extends TestSuite
{
   /**
    *  <p>The stream provider to be used, default: DefaultStreamProvider.
    *  This allows for special purposes to change the stream provider for the file-accessing methods.</p> 
    *  
    */
   private StreamProvider streamProvider = null;

   /**
    * Create a new ConfigFileTestSuite.
    * 
    * @param name The name of the test-suite in the JUnit test-hierarchy
    */
   public ConfigFileTestSuite(String name)
   {
      super(name);
   }

   /**
    * Create the TestSuite for a single config file.
    * The TestSuite-name is the name passed to this suite (constructor).
    * 
    * @param configFilePath path to a single config file
    * @param configFilePathList a list of paths to config files
    * @return a JUnit Test to be added to the actual test suite
    */
   public Test getSuite(String configFilePath, List<String> configFilePathList)
   {
      System.out.println("  start building dynamic suite for " + configFilePath);
      long timeStart = System.currentTimeMillis();

      TestSuite suite = new TestSuite();
      suite.setName(getName());
      if (configFilePath == null || "".equals(configFilePath.trim()))
      {
         return suite;
      }
      Document domDocument = getDomDocument(configFilePath);
      List<Node> applicationNodes = getApplicationNodes(domDocument, configFilePath);
      for (Iterator<Node> iterator = applicationNodes.iterator(); iterator.hasNext();)
      {
         Node applicationNode = iterator.next();
         ApplicationTestSuite applicationSuite = new ApplicationTestSuite(getName() + "_application");
         applicationSuite.setStreamProvider(getStreamProvider());
         suite.addTest(applicationSuite.getSuite(configFilePath, applicationNode, configFilePathList));
      }
      List<Node> factoryNodes = getFactoryNodes(domDocument, configFilePath);
      for (Iterator<Node> iterator = factoryNodes.iterator(); iterator.hasNext();)
      {
         Node factoryNode = iterator.next();
         FactoryTestSuite factorySuite = new FactoryTestSuite(getName() + "_factory");
         factorySuite.setStreamProvider(getStreamProvider());
         suite.addTest(factorySuite.getSuite(configFilePath, factoryNode, configFilePathList));
      }
      List<Node> componentNodes = getComponentNodes(domDocument, configFilePath);
      for (Iterator<Node> iterator = componentNodes.iterator(); iterator.hasNext();)
      {
         Node componentNode = iterator.next();
         suite.addTest(new ComponentTestCase(getName() + "_component", componentNode, configFilePath));
      }
      List<Node> converterNodes = getConverterNodes(domDocument, configFilePath);
      for (Iterator<Node> iterator = converterNodes.iterator(); iterator.hasNext();)
      {
         Node converterNode = iterator.next();
         suite.addTest(new ConverterTestCase(getName() + "_converter", converterNode, configFilePath));
      }
      Map<String, Node> beans = getManagedBeans(domDocument, configFilePath);
      for (Iterator<String> iterator = beans.keySet().iterator(); iterator.hasNext();)
      {
         String beanName = iterator.next();
         ManagedBeanTestSuite beanSuite = new ManagedBeanTestSuite(beanName);
         beanSuite.setStreamProvider(getStreamProvider());
         suite.addTest(beanSuite.getSuite(configFilePath, beanName, beans.get(beanName)));
      }
      List<Node> navigationRuleNodes = getNavigationRuleNodes(domDocument, configFilePath);
      for (Iterator<Node> iterator = navigationRuleNodes.iterator(); iterator.hasNext();)
      {
         Node navigationRuleNode = iterator.next();
         NavigationRuleTestSuite navigationRuleSuite = new NavigationRuleTestSuite(getName() + "_navigationRule");
         navigationRuleSuite.setStreamProvider(getStreamProvider());
         suite.addTest(navigationRuleSuite.getSuite(configFilePath, navigationRuleNode));
      }
      Map<String, Node> renderKits = getRenderKits(domDocument, configFilePath);
      for (Iterator<String> iterator = renderKits.keySet().iterator(); iterator.hasNext();)
      {
         String renderKitName = iterator.next();
         RenderKitTestSuite renderKitSuite = new RenderKitTestSuite(renderKitName);
         renderKitSuite.setStreamProvider(getStreamProvider());
         suite.addTest(renderKitSuite.getSuite(configFilePath, renderKits.get(renderKitName)));
      }
      List<Node> lifecycleNodes = getLifecycleNodes(domDocument, configFilePath);
      for (Iterator<Node> iterator = lifecycleNodes.iterator(); iterator.hasNext();)
      {
         Node lifecycleNode = iterator.next();
         LifecycleTestSuite lifecycleSuite = new LifecycleTestSuite(getName() + "_lifecycle");
         lifecycleSuite.setStreamProvider(getStreamProvider());
         suite.addTest(lifecycleSuite.getSuite(configFilePath, lifecycleNode));
      }
      List<Node> validatorNodes = getValidatorNodes(domDocument, configFilePath);
      for (Iterator<Node> iterator = validatorNodes.iterator(); iterator.hasNext();)
      {
         Node validatorNode = iterator.next();
         suite.addTest(new ValidatorTestCase(getName() + "_validator", validatorNode, configFilePath));
      }
      long timeStop = System.currentTimeMillis();
      double timeInSeconds = ((double)(timeStop - timeStart)) / 1000;
      System.out.println("  stop building dynamic suite (duration: " + timeInSeconds + " seconds) adding " + suite.countTestCases() + " testcases.");

      return suite;
   }

   /**
    * Extract the names and the DOM-Nodes for all managed beans defined in the config file.
    * 
    * @param domDocument the preparsed config-file
    * @param configFilePath path to the config file
    * @return a Map with the managed bean-name as a String key and the DOM-Node as the data content
    */
   private Map<String, Node> getManagedBeans(Document domDocument, String configFilePath)
   {
      Map<String, Node> beans = new HashMap<String, Node>();
      String xpathManagedBean = "/faces-config/managed-bean";
      String xpathManagedBeanName = "./managed-bean-name/text()";

      NodeList managedBeans = ParserUtils.query(domDocument, xpathManagedBean, configFilePath);
      for (int i = 0; i < managedBeans.getLength(); i++)
      {
         Node bean = managedBeans.item(i);
         String name = ParserUtils.querySingle(bean, xpathManagedBeanName, configFilePath);
         if (name != null && name.trim().length() > 0)
         {
            beans.put(name.trim(), bean);
         }
      }

      return beans;
   }

   /**
    * Extract the names and the DOM-Nodes for the application config nodes defined in the config file.
    * 
    * @param domDocument the preparsed config-file
    * @param configFilePath path to the config file
    * @return a List of DOM-Nodes with the application elements
    */
   private List<Node> getApplicationNodes(Document domDocument, String configFilePath)
   {
      List<Node> result = new ArrayList<Node>();
      String xpathApplication = "/faces-config/application";

      NodeList applications = ParserUtils.query(domDocument, xpathApplication, configFilePath);
      for (int i = 0; i < applications.getLength(); i++)
      {
         result.add(applications.item(i));
      }

      return result;
   }

   /**
    * Extract the names and the DOM-Nodes for the factory config nodes defined in the config file.
    * 
    * @param domDocument the preparsed config-file
    * @param configFilePath path to the config file
    * @return a List of DOM-Nodes with the factory elements
    */
   private List<Node> getFactoryNodes(Document domDocument, String configFilePath)
   {
      List<Node> result = new ArrayList<Node>();
      String xpathFactory = "/faces-config/factory";

      NodeList factories = ParserUtils.query(domDocument, xpathFactory, configFilePath);
      for (int i = 0; i < factories.getLength(); i++)
      {
         result.add(factories.item(i));
      }

      return result;
   }

   /**
    * Extract the DOM-Nodes for the lifecycle config nodes defined in the config file.
    * 
    * @param domDocument the preparsed config-file
    * @param configFilePath path to the config file
    * @return a List of DOM-Nodes with the lifecycle elements
    */
   private List<Node> getLifecycleNodes(Document domDocument, String configFilePath)
   {
      List<Node> result = new ArrayList<Node>();
      String xpathLifecycle = "/faces-config/lifecycle";

      NodeList lifecycles = ParserUtils.query(domDocument, xpathLifecycle, configFilePath);
      for (int i = 0; i < lifecycles.getLength(); i++)
      {
         result.add(lifecycles.item(i));
      }

      return result;
   }

   /**
    * Extract the DOM-Nodes for the converter config nodes defined in the config file.
    * 
    * @param domDocument the preparsed config-file
    * @param configFilePath path to the config file
    * @return a List of DOM-Nodes with the converter elements
    */
   private List<Node> getConverterNodes(Document domDocument, String configFilePath)
   {
      List<Node> result = new ArrayList<Node>();
      String xpathConverter = "/faces-config/converter";

      NodeList converters = ParserUtils.query(domDocument, xpathConverter, configFilePath);
      for (int i = 0; i < converters.getLength(); i++)
      {
         result.add(converters.item(i));
      }

      return result;
   }

   /**
    * Extract the DOM-Nodes for the navigation rule config nodes defined in the config file.
    * 
    * @param domDocument the preparsed config-file
    * @param configFilePath path to the config file
    * @return a List of DOM-Nodes with the navigation rule elements
    */
   private List<Node> getNavigationRuleNodes(Document domDocument, String configFilePath)
   {
      List<Node> result = new ArrayList<Node>();
      String xpathNavigationRule = "/faces-config/navigation-rule";

      NodeList navigationRules = ParserUtils.query(domDocument, xpathNavigationRule, configFilePath);
      for (int i = 0; i < navigationRules.getLength(); i++)
      {
         result.add(navigationRules.item(i));
      }

      return result;
   }

   /**
    * Extract the DOM-Nodes for the validator config nodes defined in the config file.
    * 
    * @param domDocument the preparsed config-file
    * @param configFilePath path to the config file
    * @return a List of DOM-Nodes with the validator elements
    */
   private List<Node> getValidatorNodes(Document domDocument, String configFilePath)
   {
      List<Node> result = new ArrayList<Node>();
      String xpathValidator = "/faces-config/validator";

      NodeList validators = ParserUtils.query(domDocument, xpathValidator, configFilePath);
      for (int i = 0; i < validators.getLength(); i++)
      {
         result.add(validators.item(i));
      }

      return result;
   }

   /**
    * Extract the DOM-Nodes for the component config nodes defined in the config file.
    * 
    * @param domDocument the preparsed config-file
    * @param configFilePath path to the config file
    * @return a List of DOM-Nodes with the component elements
    */
   private List<Node> getComponentNodes(Document domDocument, String configFilePath)
   {
      List<Node> result = new ArrayList<Node>();
      String xpathComponent = "/faces-config/component";

      NodeList components = ParserUtils.query(domDocument, xpathComponent, configFilePath);
      for (int i = 0; i < components.getLength(); i++)
      {
         result.add(components.item(i));
      }

      return result;
   }

   /**
    * Extract a list of renderkits defined in the config file.
    * 
    * @param domDocument the preparsed config-file
    * @param configFilePath the path to the config file
    * @return a Map with the renderKit id as a String key and the DOM-Node as the data content
    */
   private Map<String, Node> getRenderKits(Document domDocument, String configFilePath)
   {
      Map<String, Node> result = new HashMap<String, Node>();

      String xpathRenderKit = "/faces-config/render-kit";
      String xpathRenderKitId = "./render-kit-id/text()";

      NodeList renderKits = ParserUtils.query(domDocument, xpathRenderKit, configFilePath);
      for (int i = 0; i < renderKits.getLength(); i++)
      {
         Node renderKit = renderKits.item(i);
         String renderKitId = ParserUtils.querySingle(renderKit, xpathRenderKitId, configFilePath);
         if (renderKitId == null || renderKitId.trim().length() == 0)
         {
            renderKitId = "default";
         }
         else
         {
            renderKitId = renderKitId.trim();
         }
         result.put(renderKitId, renderKit);
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
