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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.util.ClassUtils;
import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A TestCase for a single JSF managed bean.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $$Revision:  $$
 */
public class ManagedBeanTestCase extends TestCase
{
   /** path to the config file */
   protected String configFilePath = null;

   /** name of the managed bean */
   protected String managedBeanName = null;

   /** class name of the managed bean */
   protected String managedBeanClassName = null;

   /** DOM node of the managed bean to test */
   protected Node managedBeanNode;

   /** class of the managed bean */
   protected Class<?> managedBeanClass = null;

   /** list of valid JSF scopes */
   private static final List<String> SCOPES = new ArrayList<String>()
   {
      {
         add("none");
         add("request");
         add("session");
         add("application");
      }
   };

   /**
    * Create a new ManagedBeanTestCase.
    * 
    * @param name The name of the test-case in the JUnit test-hierarchy
    * @param configFilePath path to a single config file
    * @param managedBeanName the name of the bean to build the test suite for
    * @param managedBeanNode the DOM node containing the managed bean
    */
   public ManagedBeanTestCase(String name, String configFilePath, String managedBeanName, Node managedBeanNode)
   {
      super(name);
      this.configFilePath = configFilePath;
      this.managedBeanName = managedBeanName;
      this.managedBeanNode = managedBeanNode;
   }

   /**
    * Call the individual test methods.
    */
   public void runTest()
   {
      testNameAttribute();
      testClassAttribute();
      testScopeAttribute();
      testClassLoadable();
      testValidScope();
      testSerializableInterface();
   }

   /**
    * Assert that a bean configuration has a name attribute
    */
   public void testNameAttribute()
   {
      String xpath = "./managed-bean-name/text()";
      String name = ParserUtils.querySingle(managedBeanNode, xpath, configFilePath);
      assertNotNull("could not determine name of " + managedBeanNode.getNodeName() + " in " + configFilePath, name);
      assertFalse("could not determine name of " + managedBeanNode.getNodeName() + " in " + configFilePath, ""
            .equals(name.trim()));
   }

   /**
    * Assert that a bean configuration has a class attribute
    */
   public void testClassAttribute()
   {
      String className = getManagedBeanClassName();
      assertNotNull("could not determine class of " + managedBeanNode.getNodeName() + " in " + configFilePath,
            className);
      assertFalse("could not determine class of " + managedBeanNode.getNodeName() + " in " + configFilePath, ""
            .equals(className.trim()));
   }

   /**
    * Assert that a bean configuration has a scope attribute
    */
   public void testScopeAttribute()
   {
      String scope = getScope();
      assertNotNull("could not determine scope of " + managedBeanNode.getNodeName() + " in " + configFilePath, scope);
      assertFalse("could not determine scope of " + managedBeanNode.getNodeName() + " in " + configFilePath, ""
            .equals(scope.trim()));
   }

   /**
    * Assert that the configured class can be loaded from the classpath
    */
   public void testClassLoadable()
   {
      getManagedBeanClass();
   }

   public void testValidScope()
   {
      String scope = getScope();
      assertTrue("Managed bean '" + managedBeanName + "' in " + configFilePath + " has an invalid scope '" + scope
            + "'", SCOPES.contains(scope));
   }

   /**
    * Assert that the classes for beans with scope session or application implement the Serialized interface 
    * as per JSF specification.
    */
   public void testSerializableInterface()
   {
      String scope = getScope();
      if (("session".equals(scope) || "application".equals(scope)))
      {
         assertTrue("Managed bean '" + managedBeanName + "' is in " + scope + " scope, so it needs to implement "
               + Serializable.class, Serializable.class.isAssignableFrom(getManagedBeanClass()));
      }
   }

   /**
    * Assert that no duplicate properties are configured for this managed bean.
    */
   public void testDuplicateProperties()
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
         if (name != null && name.trim().length() > 0)
         {
            if (managedBeanName.equals(name.trim()))
            {
               NodeList managedProperties = ParserUtils.query(bean, xpathProperty, configFilePath);
               for (int j = 0; j < managedProperties.getLength(); j++)
               {
                  Node property = managedProperties.item(j);
                  String propertyName = ParserUtils.querySingle(property, xpathPropertyName, configFilePath);
                  if (propertyName != null && propertyName.trim().length() > 0)
                  {
                     propertyName = propertyName.trim();
                     if (properties.containsKey(propertyName))
                     {
                        fail("managed bean '" + managedBeanName + "' in " + configFilePath
                              + " has a duplicate property named " + propertyName);
                     }
                     else
                     {
                        properties.put(propertyName, property);
                     }
                  }
                  else
                  {
                     fail("managed bean '" + name + "' property without 'property-name' configured");
                  }
               }
            }
         }
         else
         {
            fail("managed bean without 'managed-bean-name' configured");
         }
      }
   }

   /**
    * Extract the managed beans class name.
    * 
    * @return the configured class name for the managed bean
    */
   private String getManagedBeanClassName()
   {
      String xpath = "./managed-bean-class/text()";
      if (managedBeanClassName == null)
      {
         managedBeanClassName = ParserUtils.querySingle(managedBeanNode, xpath, configFilePath);
         if (managedBeanClassName != null)
         {
            managedBeanClassName = managedBeanClassName.trim();
         }
      }
      return managedBeanClassName;
   }

   /**
    * Get the value of the scope-attribute from the managed-beans DOM-node.
    * 
    * @return the configured scope
    */
   private String getScope()
   {
      String xpath = "./managed-bean-scope/text()";
      String scope = ParserUtils.querySingle(managedBeanNode, xpath, configFilePath);
      return scope;
   }

   /** 
    * <p>Accessor for the managed bean class instance. If the class is not yet loaded, then it will be loaded.</p>
    * 
    * @return the loaded class
    */
   private Class<?> getManagedBeanClass()
   {
      if (managedBeanClass == null)
      {
         managedBeanClass = new ClassUtils().loadClass(getManagedBeanClassName(), "managed bean");
      }
      return managedBeanClass;
   }
}
