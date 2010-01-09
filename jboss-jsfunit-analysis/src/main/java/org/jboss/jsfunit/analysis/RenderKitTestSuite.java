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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A TestSuite for a single JSF renderKit config node.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $$Revision:  $$
 */
public class RenderKitTestSuite extends TestSuite implements Test
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
   public RenderKitTestSuite(String name)
   {
      super(name);
   }

   /**
    * Create the TestSuite for a renderKit config node.
    * The TestSuite-name is the name passed to this suite (constructor).
    * 
    * @param configFilePath path to a single config file
    * @param renderKitNode the DOM node containing the renderKit
    * @return a JUnit Test to be added to the actual test suite
    */
   public Test getSuite(String configFilePath, Node renderKitNode)
   {
      TestSuite suite = new TestSuite();
      suite.setName(getName());
      suite.addTest(new RenderKitTestCase(getName(), configFilePath, renderKitNode));
      if (configFilePath == null || "".equals(configFilePath.trim()) || renderKitNode == null)
      {
         return suite;
      }
      Map<String, Node> renderers = getRenderers(configFilePath, renderKitNode);
      for (Iterator<String> iterator = renderers.keySet().iterator(); iterator.hasNext();)
      {
         String rendererName = iterator.next();
         suite.addTest(new RendererTestCase(rendererName, renderers.get(rendererName), configFilePath));
      }

      return suite;
   }

   /**
    * Extract the names and the DOM-Nodes for all renderers defined for the specified render kit.
    * 
    * @param configFilePath path to the config file
    * @param renderKitNode the DOM node containing the render kit
    * @return a Map with the renderer name as a String key and the DOM-Node as the data content
    */
   private Map<String, Node> getRenderers(String configFilePath, Node renderKitNode)
   {
      Map<String, Node> rendererNodes = new HashMap<String, Node>();
      String xpathRenderer = "./renderer";
      String xpathRendererDisplayName = "./display-name/text()";
      String xpathRendererClassName = "./renderer-class/text()";

      NodeList renderers = ParserUtils.query(renderKitNode, xpathRenderer, configFilePath);
      for (int i = 0; i < renderers.getLength(); i++)
      {
         Node renderer = renderers.item(i);
         String rendererName = null;
         String displayName = ParserUtils.querySingle(renderer, xpathRendererDisplayName, configFilePath);
         if (displayName != null && displayName.trim().length() > 0)
         {
            rendererName = displayName.trim();
         }
         else
         {
            String className = ParserUtils.querySingle(renderer, xpathRendererClassName, configFilePath);
            if (className != null)
            {
               rendererName = className.trim();
            }
            else
            {
               rendererName = "unknown";
            }
         }
         rendererNodes.put(rendererName, renderer);
      }

      return rendererNodes;
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
