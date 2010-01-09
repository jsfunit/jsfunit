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
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A TestSuite for a single JSF lifecycle config node.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $$Revision:  $$
 */
public class LifecycleTestSuite extends TestSuite implements Test
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
   public LifecycleTestSuite(String name)
   {
      super(name);
   }

   /**
    * Create the TestSuite for a lifecycle config node.
    * The TestSuite-name is the name passed to this suite (constructor).
    * 
    * @param configFilePath path to a single config file
    * @param lifecycleNode the DOM node containing the lifecycle
    * @return a JUnit Test to be added to the actual test suite
    */
   public Test getSuite(String configFilePath, Node lifecycleNode)
   {
      TestSuite suite = new TestSuite();
      suite.setName(getName());
      suite.addTest(new LifecycleTestCase(getName(), configFilePath, lifecycleNode));
      if (configFilePath == null || "".equals(configFilePath.trim()) || lifecycleNode == null)
      {
         return suite;
      }
      List<String> phaseListeners = getPhaseListeners(lifecycleNode, configFilePath);
      for (Iterator<String> iterator = phaseListeners.iterator(); iterator.hasNext();)
      {
         String phaseListener = iterator.next();
         suite.addTest(new PhaseListenerTestCase(phaseListener, phaseListener));
      }

      return suite;
   }

   /**
    * Extract the name for the application lifecycle defined in the config file.
    * 
    * @param lifecycleNode the lifecycle config dom-node
    * @param configFilePath path to the config file
    * @return a String with the actionlistener name
    */
   private List<String> getPhaseListeners(Node lifecycleNode, String configFilePath)
   {
      List<String> result = new ArrayList<String>();
      String xpathPhaseListener = "./phase-listener";
      String xpathPhaseListenerClassName = "./text()";

      NodeList phaseListeners = ParserUtils.query(lifecycleNode, xpathPhaseListener, configFilePath);
      for (int i = 0; i < phaseListeners.getLength(); i++)
      {
         Node phaseListener = phaseListeners.item(i);
         String name = ParserUtils.querySingle(phaseListener, xpathPhaseListenerClassName, configFilePath);
         if (name != null && name.trim().length() > 0)
         {
            result.add(name.trim());
         }
         else
         {
            //ignore
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
