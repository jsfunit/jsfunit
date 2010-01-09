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

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.util.ClassUtils;
import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A TestCase for a single JSF managed property.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $$Revision:  $$
 */
public class ManagedPropertyTestCase extends TestCase
{
   /** path to the config file */
   protected String configFilePath = null;

   /** name of the managed bean */
   protected String managedBeanName = null;

   /** class name of the managed bean */
   protected String managedBeanClassName = null;

   /** name of the managed bean property */
   protected String managedPropertyName = null;

   /** class of the managed bean */
   protected Class<?> managedBeanClass = null;

   /** DOM node of the managed property to test */
   protected Node managedPropertyNode;

   /**
    * Create a new ManagedPropertyTestCase.
    * 
    * @param name The name of the test-case in the JUnit test-hierarchy
    * @param configFilePath path to a single config file
    * @param managedBeanName the name of the bean to test
    * @param managedBeanClassName  the class name of the bean to test
    * @param managedPropertyName the name of the property to test
    * @param managedPropertyNode the DOM node containing the managed bean
    */
   public ManagedPropertyTestCase(String name, String configFilePath, String managedBeanName,
         String managedBeanClassName, String managedPropertyName, Node managedPropertyNode)
   {
      super(name);
      this.configFilePath = configFilePath;
      this.managedBeanName = managedBeanName;
      this.managedBeanClassName = managedBeanClassName;
      this.managedPropertyName = managedPropertyName;
      this.managedPropertyNode = managedPropertyNode;
   }

   /**
    * Call the individual test methods.
    */
   public void runTest()
   {
      testPropertyAccessors();
      testMapDuplicateKeys();
   }

   /**
    * Assert that a bean class has accessors for the property
    */
   public void testPropertyAccessors()
   {
      String setter = createAccessorName("set");
      String getter1 = createAccessorName("get");
      String getter2 = createAccessorName("is");

      if (!hasMethod(setter, 1, getManagedBeanClass()))
      {
         fail("The managed bean '" + managedBeanName + "' has a managed property called '" + managedPropertyName
               + "', but " + managedBeanClassName + " has no method " + setter + "(?)");
      }
      if (!(hasMethod(getter1, 0, getManagedBeanClass()) || hasMethod(getter2, 0, getManagedBeanClass())))
      {
         fail("The managed bean '" + managedBeanName + "' has a managed property called '" + managedPropertyName
               + "', but " + managedBeanClassName + " has neither a method " + getter1 + "() nor a method " + getter2
               + "()");
      }
   }

   /**
    * Assert that the property does not define duplicate map-entries
    */
   public void testMapDuplicateKeys()
   {
      //Node managedBean, String facesConfigPath, String name
      String xpathMapEntries = "./map-entries";
      String xpathMapEntryKey = "./map-entry/key";
      NodeList nodeList = ParserUtils.query(managedPropertyNode, xpathMapEntries, configFilePath);

      //      assertTrue(nodeList.getLength() > 0);
      //do this test only if there are map-entries in this property definition
      for (int i = 0; i < nodeList.getLength(); i++)
      {
         NodeList keys = ParserUtils.query(nodeList.item(i), xpathMapEntryKey, configFilePath);
         Set<String> keyNames = new HashSet<String>();
         for (int j = 0; j < keys.getLength(); j++)
         {
            Node firstChild = keys.item(j).getFirstChild();
            assertNotNull(firstChild);
            String textContent = firstChild.getTextContent();
            assertTrue("Managed Bean '" + managedBeanName + "' has a managed Map property with a duplicate key '"
                  + textContent + "'", !(keyNames.contains(textContent)));
            keyNames.add(textContent);
         }
      }
   }

   /**
    * Create the accessor method name with the passed prefix.
    * 
    * @param prefix the accessor name prefix
    * @return the method name according to Java Bean naming conventions
    */
   private String createAccessorName(String prefix)
   {
      StringBuffer result = new StringBuffer(prefix);
      result.append(managedPropertyName.substring(0, 1).toUpperCase());
      result.append(managedPropertyName.substring(1, managedPropertyName.length()));
      return result.toString();
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
         managedBeanClass = new ClassUtils().loadClass(managedBeanClassName, "managed bean");
      }
      return managedBeanClass;
   }

   /**
    * Verify whether the class defines a method with a given name.
    * 
    * @param methodName the method name we are looking for
    * @param clazz the class to be searched
    * @return true if the class or one of the superclasses has such a method
    */
   private boolean hasMethod(String methodName, int numberOfParameters, Class<?> clazz)
   {

      if (clazz == null)
      {
         return false;
      }

      Method[] methods = clazz.getMethods();

      for (int i = 0; i < methods.length; i++)
      {
         if (methodName.equals(methods[i].getName()) & methods[i].getParameterTypes().length == numberOfParameters)
         {
            return true;
         }
      }

      return hasMethod(methodName, numberOfParameters, clazz.getSuperclass());
   }
}
