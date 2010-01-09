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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A TestSuite for a single JSF application config node.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $$Revision:  $$
 */
public class ApplicationTestSuite extends TestSuite implements Test
{
   /**
    *  <p>The stream provider to be used, default: DefaultStreamProvider.
    *  This allows for special purposes to change the stream provider for the file-accessing methods.</p> 
    *  
    */
   private StreamProvider streamProvider = null;

   /**
    * Create a new ApplicationTestSuite.
    * 
    * @param name The name of the test-suite in the JUnit test-hierarchy
    */
   public ApplicationTestSuite(String name)
   {
      super(name);
   }

   /**
    * Create the TestSuite for a application config node.
    * The TestSuite-name is the name passed to this suite (constructor).
    * 
    * @param configFilePath path to a single config file
    * @param applicationNode the DOM node containing the managed bean
    * @param configFilePathList a list of paths to config files
    * @return a JUnit Test to be added to the actual test suite
    */
   public Test getSuite(String configFilePath, Node applicationNode, List<String> configFilePathList)
   {
      TestSuite suite = new TestSuite();
      suite.setName(getName());
      if (configFilePath == null || "".equals(configFilePath.trim()) || applicationNode == null)
      {
         return suite;
      }
      String actionListener = getActionListener(applicationNode, configFilePath);
      if (actionListener != null)
      {
         suite.addTest(new ActionListenerTestCase(actionListener, actionListener));
      }
      String navigationHandler = getNavigationHandler(applicationNode, configFilePath);
      if (navigationHandler != null)
      {
         suite.addTest(new NavigationHandlerTestCase(navigationHandler, navigationHandler));
      }
      String viewHandler = getViewHandler(applicationNode, configFilePath);
      if (viewHandler != null)
      {
         suite.addTest(new ViewHandlerTestCase(viewHandler, viewHandler));
      }
      String stateManager = getStateManager(applicationNode, configFilePath);
      if (stateManager != null)
      {
         suite.addTest(new StateManagerTestCase(stateManager, stateManager));
      }
      String defaultRenderkit = getDefaultRenderkit(applicationNode, configFilePath);
      if (defaultRenderkit != null)
      {
         List<String> tempList = configFilePathList;
         if (tempList == null)
         {
            tempList = new ArrayList<String>();
            tempList.add(configFilePath);
         }
         suite.addTest(new DefaultRenderkitTestCase(defaultRenderkit, tempList));
      }
      Map<String, String> elResolvers = getElResolvers(applicationNode, configFilePath);
      for (Iterator<String> iterator = elResolvers.keySet().iterator(); iterator.hasNext();)
      {
         String elResolverId = iterator.next();
         String elResolverClassName = elResolvers.get(elResolverId);
         suite.addTest(new ElResolverTestCase(elResolverId, elResolverClassName));
      }
      Map<String, String> propertyResolvers = getPropertyResolvers(applicationNode, configFilePath);
      for (Iterator<String> iterator = propertyResolvers.keySet().iterator(); iterator.hasNext();)
      {
         String propertyResolverId = iterator.next();
         String propertyResolverClassName = propertyResolvers.get(propertyResolverId);
         suite.addTest(new PropertyResolverTestCase(propertyResolverId, propertyResolverClassName));
      }
      Map<String, String> variableResolvers = getVariableResolvers(applicationNode, configFilePath);
      for (Iterator<String> iterator = variableResolvers.keySet().iterator(); iterator.hasNext();)
      {
         String variableResolverId = iterator.next();
         String variableResolverClassName = variableResolvers.get(variableResolverId);
         suite.addTest(new VariableResolverTestCase(variableResolverId, variableResolverClassName));
      }

      return suite;
   }

   /**
    * Extract the name for the action-listener defined in the config file.
    * 
    * @param applicationNode the application config dom-node
    * @param configFilePath path to the config file
    * @return a String with the actionlistener name
    */
   private String getActionListener(Node applicationNode, String configFilePath)
   {
      String result = null;
      String xpathActionListener = "/faces-config/application/action-listener";
      String xpathActionListenerClassName = "./text()";

      NodeList actionListeners = ParserUtils.query(applicationNode, xpathActionListener, configFilePath);
      switch (actionListeners.getLength())
      {
         case 0 : {
            // no application level definitions
            break;
         }
         case 1 : {
            Node actionListener = actionListeners.item(0);
            String name = ParserUtils.querySingle(actionListener, xpathActionListenerClassName, configFilePath);
            if (name != null && name.trim().length() > 0)
            {
               result = name.trim();
            }
            break;
         }
         default : {
            throw new RuntimeException("only one action listener node may be specified in an application node");
         }
      }

      return result;
   }

   /**
    * Extract the name for the navigation handler defined in the config file.
    * 
    * @param applicationNode the application config dom-node
    * @param configFilePath path to the config file
    * @return a String with the navigation handler name
    */
   private String getNavigationHandler(Node applicationNode, String configFilePath)
   {
      String result = null;
      String xpathNavigationHandler = "/faces-config/application/navigation-handler";
      String xpathNavigationHandlerClassName = "./text()";

      NodeList navigationHandlers = ParserUtils.query(applicationNode, xpathNavigationHandler, configFilePath);
      switch (navigationHandlers.getLength())
      {
         case 0 : {
            // no application level definitions
            break;
         }
         case 1 : {
            Node navigationHandler = navigationHandlers.item(0);
            String name = ParserUtils.querySingle(navigationHandler, xpathNavigationHandlerClassName, configFilePath);
            if (name != null && name.trim().length() > 0)
            {
               result = name.trim();
            }
            break;
         }
         default : {
            throw new RuntimeException("only one navigation handler node may be specified in an application node");
         }
      }

      return result;
   }

   /**
    * Extract the name for the view handler defined in the config file.
    * 
    * @param applicationNode the application config dom-node
    * @param configFilePath path to the config file
    * @return a String with the view handler name
    */
   private String getViewHandler(Node applicationNode, String configFilePath)
   {
      String result = null;
      String xpathViewHandler = "/faces-config/application/view-handler";
      String xpathViewHandlerClassName = "./text()";

      NodeList viewHandlers = ParserUtils.query(applicationNode, xpathViewHandler, configFilePath);
      switch (viewHandlers.getLength())
      {
         case 0 : {
            // no application level definitions
            break;
         }
         case 1 : {
            Node viewHandler = viewHandlers.item(0);
            String name = ParserUtils.querySingle(viewHandler, xpathViewHandlerClassName, configFilePath);
            if (name != null && name.trim().length() > 0)
            {
               result = name.trim();
            }
            break;
         }
         default : {
            throw new RuntimeException("only one view handler node may be specified in an application node");
         }
      }

      return result;
   }

   /**
    * Extract the name for the state manager defined in the config file.
    * 
    * @param applicationNode the application config dom-node
    * @param configFilePath path to the config file
    * @return a String with the state manager name
    */
   private String getStateManager(Node applicationNode, String configFilePath)
   {
      String result = null;
      String xpathStateManager = "/faces-config/application/state-manager";
      String xpathStateManagerClassName = "./text()";

      NodeList stateManagers = ParserUtils.query(applicationNode, xpathStateManager, configFilePath);
      switch (stateManagers.getLength())
      {
         case 0 : {
            // no application level definitions
            break;
         }
         case 1 : {
            Node stateManager = stateManagers.item(0);
            String name = ParserUtils.querySingle(stateManager, xpathStateManagerClassName, configFilePath);
            if (name != null && name.trim().length() > 0)
            {
               result = name.trim();
            }
            break;
         }
         default : {
            throw new RuntimeException("only one state manager node may be specified in an application node");
         }
      }

      return result;
   }

   /**
    * Extract the names and the DOM-Nodes for all render kits defined in the config file.
    * 
    * @param applicationNode the application config dom-node
    * @param configFilePath path to the config file
    * @return a Map with the managed bean-name as a String key and the DOM-Node as the data content
    */
   private String getDefaultRenderkit(Node applicationNode, String configFilePath)
   {
      String result = null;
      String xpathDefaultRenderkitId = "./default-render-kit-id/text()";

      String name = ParserUtils.querySingle(applicationNode, xpathDefaultRenderkitId, configFilePath);
      if (name != null && name.trim().length() > 0)
      {
         result = name.trim();
      }

      return result;
   }

   /**
    * Extract the name for the EL-resolvers defined in the config file.
    * 
    * @param applicationNode the application config dom-node
    * @param configFilePath path to the config file
    * @return a Map with el-resolver id's and classnames
    */
   private Map<String, String> getElResolvers(Node applicationNode, String configFilePath)
   {
      Map<String, String> result = new HashMap<String, String>();
      String xpathElResolver = "/faces-config/application/el-resolver";
      String xpathElResolverClassName = "./text()";
      String xpathElResolverIdAttributeName = "id";

      NodeList elResolvers = ParserUtils.query(applicationNode, xpathElResolver, configFilePath);
      for (int i = 0; i < elResolvers.getLength(); i++)
      {
         Node elResolver = elResolvers.item(i);
         NamedNodeMap elAttributes = elResolver.getAttributes();
         Node elResolverIdAttribute = elAttributes.getNamedItem(xpathElResolverIdAttributeName);
         String elResolverId = null;
         if (elResolverIdAttribute != null)
         {
            elResolverId = elResolverIdAttribute.getTextContent();
         }
         String className = ParserUtils.querySingle(elResolver, xpathElResolverClassName, configFilePath);
         if (elResolverId != null)
         {
            result.put(elResolverId, className);
         }
         else
         {
            result.put(className, className);
         }
      }

      return result;
   }

   /**
    * Extract the name for the property-resolvers defined in the config file.
    * 
    * @param applicationNode the application config dom-node
    * @param configFilePath path to the config file
    * @return a Map with property-resolver id's and classnames
    */
   private Map<String, String> getPropertyResolvers(Node applicationNode, String configFilePath)
   {
      Map<String, String> result = new HashMap<String, String>();
      String xpathPropertyResolver = "/faces-config/application/property-resolver";
      String xpathPropertyResolverClassName = "./text()";
      String xpathPropertyResolverIdAttributeName = "id";

      NodeList propertyResolvers = ParserUtils.query(applicationNode, xpathPropertyResolver, configFilePath);
      for (int i = 0; i < propertyResolvers.getLength(); i++)
      {
         Node propertyResolver = propertyResolvers.item(i);
         NamedNodeMap propertyAttributes = propertyResolver.getAttributes();
         Node propertyResolverIdAttribute = propertyAttributes.getNamedItem(xpathPropertyResolverIdAttributeName);
         String propertyResolverId = null;
         if (propertyResolverIdAttribute != null)
         {
            propertyResolverId = propertyResolverIdAttribute.getTextContent();
         }
         String className = ParserUtils.querySingle(propertyResolver, xpathPropertyResolverClassName, configFilePath);
         if (propertyResolverId != null)
         {
            result.put(propertyResolverId, className);
         }
         else
         {
            result.put(className, className);
         }
      }

      return result;
   }

   /**
    * Extract the name for the variable-resolvers defined in the config file.
    * 
    * @param applicationNode the application config dom-node
    * @param configFilePath path to the config file
    * @return a Map with variable-resolver id's and classnames
    */
   private Map<String, String> getVariableResolvers(Node applicationNode, String configFilePath)
   {
      Map<String, String> result = new HashMap<String, String>();
      String xpathVariableResolver = "/faces-config/application/variable-resolver";
      String xpathVariableResolverClassName = "./text()";
      String xpathVariableResolverIdAttributeName = "id";

      NodeList variableResolvers = ParserUtils.query(applicationNode, xpathVariableResolver, configFilePath);
      for (int i = 0; i < variableResolvers.getLength(); i++)
      {
         Node variableResolver = variableResolvers.item(i);
         NamedNodeMap variableAttributes = variableResolver.getAttributes();
         Node variableResolverIdAttribute = variableAttributes.getNamedItem(xpathVariableResolverIdAttributeName);
         String variableResolverId = null;
         if (variableResolverIdAttribute != null)
         {
            variableResolverId = variableResolverIdAttribute.getTextContent();
         }
         String className = ParserUtils.querySingle(variableResolver, xpathVariableResolverClassName, configFilePath);
         if (variableResolverId != null)
         {
            result.put(variableResolverId, className);
         }
         else
         {
            result.put(className, className);
         }
      }

      return result;
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
