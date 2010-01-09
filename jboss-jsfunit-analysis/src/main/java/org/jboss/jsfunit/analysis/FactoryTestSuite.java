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

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A TestSuite for a single JSF factory config node.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $$Revision:  $$
 */
public class FactoryTestSuite extends TestSuite implements Test
{
   /**
    *  <p>The stream provider to be used, default: DefaultStreamProvider.
    *  This allows for special purposes to change the stream provider for the file-accessing methods.</p> 
    *  
    */
   private StreamProvider streamProvider = null;

   /**
    * Create a new FactoryTestSuite.
    * 
    * @param name The name of the test-suite in the JUnit test-hierarchy
    */
   public FactoryTestSuite(String name)
   {
      super(name);
   }

   /**
    * Create the TestSuite for a factory config node.
    * The TestSuite-name is the name passed to this suite (constructor).
    * 
    * @param configFilePath path to a single config file
    * @param applicationfactoryNode the DOM node containing the factory
    * @param configFilePathList a list of paths to config files
    * @return a JUnit Test to be added to the actual test suite
    */
   public Test getSuite(String configFilePath, Node factoryNode, List<String> configFilePathList)
   {
      TestSuite suite = new TestSuite();
      suite.setName(getName());
      if (configFilePath == null || "".equals(configFilePath.trim()) || factoryNode == null)
      {
         return suite;
      }
      //      suite.addTest(new ApplicationTestCase(getName(), configFilePath, applicationNode));
      String applicationFactory = getApplicationFactory(factoryNode, configFilePath);
      if (applicationFactory != null)
      {
         suite.addTest(new ApplicationFactoryTestCase(applicationFactory, applicationFactory));
      }
      String facesContextFactory = getFacesContextFactory(factoryNode, configFilePath);
      if (facesContextFactory != null)
      {
         suite.addTest(new FacesContextFactoryTestCase(facesContextFactory, facesContextFactory));
      }
      String lifecycleFactory = getLifecycleFactory(factoryNode, configFilePath);
      if (lifecycleFactory != null)
      {
         suite.addTest(new LifecycleFactoryTestCase(lifecycleFactory, lifecycleFactory));
      }
      String renderKitFactory = getRenderKitFactory(factoryNode, configFilePath);
      if (renderKitFactory != null)
      {
         suite.addTest(new RenderKitFactoryTestCase(renderKitFactory, renderKitFactory));
      }

      return suite;
   }

   /**
    * Extract the name for the application factory defined in the config file.
    * 
    * @param factoryNode the factory config dom-node
    * @param configFilePath path to the config file
    * @return a String with the actionlistener name
    */
   private String getApplicationFactory(Node factoryNode, String configFilePath)
   {
      String result = null;
      String xpathApplicationFactory = "./application-factory";
      String xpathApplicationFactoryClassName = "./text()";

      NodeList applicationFactories = ParserUtils.query(factoryNode, xpathApplicationFactory, configFilePath);
      switch (applicationFactories.getLength())
      {
         case 0 : {
            // no application level definitions
            break;
         }
         case 1 : {
            Node applicationFactory = applicationFactories.item(0);
            String name = ParserUtils.querySingle(applicationFactory, xpathApplicationFactoryClassName, configFilePath);
            if (name != null && name.trim().length() > 0)
            {
               result = name.trim();
            }
            else
            {
               result = "";
            }
            break;
         }
         default : {
            throw new RuntimeException("only one application factory node may be specified in an factory node");
         }
      }

      return result;
   }

   /**
    * Extract the name for the faces context factory defined in the config file.
    * 
    * @param factoryNode the factory config dom-node
    * @param configFilePath path to the config file
    * @return a String with the actionlistener name
    */
   private String getFacesContextFactory(Node factoryNode, String configFilePath)
   {
      String result = null;
      String xpathFacesContextFactory = "./faces-context-factory";
      String xpathFacesContextFactoryClassName = "./text()";

      NodeList facesContextFactories = ParserUtils.query(factoryNode, xpathFacesContextFactory, configFilePath);
      switch (facesContextFactories.getLength())
      {
         case 0 : {
            // no application level definitions
            break;
         }
         case 1 : {
            Node facesContextFactory = facesContextFactories.item(0);
            String name = ParserUtils.querySingle(facesContextFactory, xpathFacesContextFactoryClassName,
                  configFilePath);
            if (name != null && name.trim().length() > 0)
            {
               result = name.trim();
            }
            else
            {
               result = "";
            }
            break;
         }
         default : {
            throw new RuntimeException("only one faces context factory node may be specified in an factory node");
         }
      }

      return result;
   }

   /**
    * Extract the name for the render kit factory defined in the config file.
    * 
    * @param factoryNode the factory config dom-node
    * @param configFilePath path to the config file
    * @return a String with the actionlistener name
    */
   private String getRenderKitFactory(Node factoryNode, String configFilePath)
   {
      String result = null;
      String xpathRenderKitFactory = "./render-kit-factory";
      String xpathRenderKitFactoryClassName = "./text()";

      NodeList renderKitFactories = ParserUtils.query(factoryNode, xpathRenderKitFactory, configFilePath);
      switch (renderKitFactories.getLength())
      {
         case 0 : {
            // no application level definitions
            break;
         }
         case 1 : {
            Node renderKitFactory = renderKitFactories.item(0);
            String name = ParserUtils.querySingle(renderKitFactory, xpathRenderKitFactoryClassName, configFilePath);
            if (name != null && name.trim().length() > 0)
            {
               result = name.trim();
            }
            else
            {
               result = "";
            }
            break;
         }
         default : {
            throw new RuntimeException("only one render kit factory node may be specified in an factory node");
         }
      }

      return result;
   }

   /**
    * Extract the name for the lifecycle factory defined in the config file.
    * 
    * @param factoryNode the factory config dom-node
    * @param configFilePath path to the config file
    * @return a String with the actionlistener name
    */
   private String getLifecycleFactory(Node factoryNode, String configFilePath)
   {
      String result = null;
      String xpathLifecycleFactory = "./lifecycle-factory";
      String xpathLifecycleFactoryClassName = "./text()";

      NodeList lifecycleFactories = ParserUtils.query(factoryNode, xpathLifecycleFactory, configFilePath);
      switch (lifecycleFactories.getLength())
      {
         case 0 : {
            // no application level definitions
            break;
         }
         case 1 : {
            Node lifecycleFactory = lifecycleFactories.item(0);
            String name = ParserUtils.querySingle(lifecycleFactory, xpathLifecycleFactoryClassName, configFilePath);
            if (name != null && name.trim().length() > 0)
            {
               result = name.trim();
            }
            else
            {
               result = "";
            }
            break;
         }
         default : {
            throw new RuntimeException("only one lifecycle factory node may be specified in an factory node");
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
