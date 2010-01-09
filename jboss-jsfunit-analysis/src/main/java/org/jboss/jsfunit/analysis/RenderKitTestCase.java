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

import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Node;

/**
 * A TestCase for a single JSF renderKit node.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $$Revision:  $$
 */
public class RenderKitTestCase extends AbstractInterfaceTestCase
{
   /** path to the config file */
   protected String configFilePath = null;

   /** DOM node of the renderKit to test */
   protected Node renderKitNode;

   /**
    * Create a new RenderKitTestCase.
    * 
    * @param name The name of the test-case in the JUnit test-hierarchy
    * @param configFilePath path to a single config file
    * @param renderKitNode the DOM node containing the renderKit
    */
   public RenderKitTestCase(String name, String configFilePath, Node renderKitNode)
   {
      super(name, "Render Kit", null);
      this.configFilePath = configFilePath;
      this.renderKitNode = renderKitNode;
   }

   /**
    * Call the individual test methods.
    */
   public void runTest()
   {
      String clazzName = getRenderKitClassName();
      if (clazzName != null)
      {
         setClassName(clazzName);
         testClassLoadable();
         testInterface();
      }
   }

   /**
    * Extract and return the render kit class name, if configured.
    * 
    * @return class name or null (if not configured)
    */
   public String getRenderKitClassName()
   {
      String xpathRenderKitClassName = "./render-kit-class/text()";
      return ParserUtils.querySingle(this.renderKitNode, xpathRenderKitClassName, configFilePath);
   }

   @Override
   protected Class<?> getClassToExtend()
   {
      return javax.faces.render.RenderKit.class;
   }
}
