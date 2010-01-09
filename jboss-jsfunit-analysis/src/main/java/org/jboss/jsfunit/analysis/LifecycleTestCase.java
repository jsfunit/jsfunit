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

import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A TestCase for a single JSF lifecycle node.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $$Revision:  $$
 */
public class LifecycleTestCase extends TestCase
{
   /** path to the config file */
   protected String configFilePath = null;

   /** DOM node of the lifecycle to test */
   protected Node lifecycleNode;

   /**
    * Create a new LifecycleTestCase.
    * 
    * @param name The name of the test-case in the JUnit test-hierarchy
    * @param configFilePath path to a single config file
    * @param lifecycleNode the DOM node containing the lifecycle
    */
   public LifecycleTestCase(String name, String configFilePath, Node lifecycleNode)
   {
      super(name);
      this.configFilePath = configFilePath;
      this.lifecycleNode = lifecycleNode;
   }

   /**
    * Call the individual test methods.
    */
   public void runTest()
   {
      testEmptyPhaseListener();
   }

   /**
    * Assert that all phase-listener children configure a class name
    */
   public void testEmptyPhaseListener()
   {
      String xpathPhaseListener = "./phase-listener";
      String xpathPhaseListenerClassName = "./text()";

      NodeList phaseListeners = ParserUtils.query(lifecycleNode, xpathPhaseListener, configFilePath);
      for (int i = 0; i < phaseListeners.getLength(); i++)
      {
         Node phaseListener = phaseListeners.item(i);
         String name = ParserUtils.querySingle(phaseListener, xpathPhaseListenerClassName, configFilePath);
         assertNotNull("phase-listener must not be null", name);
         assertTrue("phase-listener must not be empty", name.trim().length() > 0);
      }
   }
}
