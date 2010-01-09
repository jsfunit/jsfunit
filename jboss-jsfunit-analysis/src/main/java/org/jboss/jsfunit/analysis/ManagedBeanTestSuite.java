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
 * A TestSuite for a single JSF managed bean.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $$Revision:  $$
 */
public class ManagedBeanTestSuite extends TestSuite implements Test
{
   /**
    *  <p>The stream provider to be used, default: DefaultStreamProvider.
    *  This allows for special purposes to change the stream provider for the file-accessing methods.</p> 
    *  
    */
   private StreamProvider streamProvider = null;

   /**
    * Create a new ManagedBeanTestSuite.
    * 
    * @param name The name of the test-suite in the JUnit test-hierarchy
    */
   public ManagedBeanTestSuite(String name)
   {
      super(name);
   }

   /**
    * Create the TestSuite for a managed bean.
    * The TestSuite-name is the name passed to this suite (constructor).
    * 
    * @param configFilePath path to a single config file
    * @param managedBeanName the name of the bean to build the test suite for
    * @param managedBeanNode the DOM node containing the managed bean
    * @return a JUnit Test to be added to the actual test suite
    */
   public Test getSuite(String configFilePath, String managedBeanName, Node managedBeanNode)
   {
      TestSuite suite = new TestSuite();
      suite.setName(getName());
      suite.addTest(new ManagedBeanTestCase(managedBeanName, configFilePath, managedBeanName, managedBeanNode));
      Map<String, Node> properties = getManagedProperties(configFilePath, managedBeanName, managedBeanNode);
      String managedBeanClassName = getManagedBeanClassName(configFilePath, managedBeanNode);
      for (Iterator<String> iterator = properties.keySet().iterator(); iterator.hasNext();)
      {
         String property = iterator.next();
         suite.addTest(new ManagedPropertyTestCase(property, configFilePath, managedBeanName, managedBeanClassName,
               property, properties.get(property)));
      }
      return suite;
   }

   /**
    * Extract the names and the DOM-Nodes for all managed properties defined for the specified managed bean.
    * 
    * @param configFilePath path to the config file
    * @param managedBeanName the name of the bean to build the test suite for
    * @param managedBeanNode the DOM node containing the managed bean
    * @return a Map with the property name as a String key and the DOM-Node as the data content
    */
   private Map<String, Node> getManagedProperties(String configFilePath, String managedBeanName, Node managedBeanNode)
   {
      Map<String, Node> properties = new HashMap<String, Node>();
      String xpath = "/faces-config/managed-bean";
      String xpathBeanName = "./managed-bean-name/text()";
      String xpathProperty = "./managed-property";
      String xpathPropertyName = "./property-name/text()";

      NodeList managedBeans = ParserUtils.query(managedBeanNode, xpath, configFilePath);
      for (int i = 0; i < managedBeans.getLength(); i++)
      {
         Node bean = managedBeans.item(i);
         String name = ParserUtils.querySingle(bean, xpathBeanName, configFilePath);
         if (name != null && name.trim().length() > 0 && managedBeanName.equals(name))
         {
            NodeList managedProperties = ParserUtils.query(bean, xpathProperty, configFilePath);
            for (int j = 0; j < managedProperties.getLength(); j++)
            {
               Node property = managedProperties.item(j);
               String propertyName = ParserUtils.querySingle(property, xpathPropertyName, configFilePath).trim();
               if (propertyName != null && propertyName.trim().length() > 0)
               {
                  properties.put(propertyName, property);
               }
            }
         }
      }

      return properties;
   }

   /**
    * Extract the managed beans class name.
    * 
    * @param configFilePath path to the config file
    * @param managedBeanNode the DOM node containing the managed bean
    * @return the configured class name for the managed bean
    */
   private String getManagedBeanClassName(String configFilePath, Node managedBeanNode)
   {
      String xpath = "./managed-bean-class/text()";
      return ParserUtils.querySingle(managedBeanNode, xpath, configFilePath).trim();
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
