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
 */

package org.jboss.jsfunit.analysis;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.model.ManagedBeanOneProperty;
import org.w3c.dom.Node;

/**
 * A TestCase related to Jira-issue JSFUNIT_26.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class ManagedBeanTestCase_JSFUNIT_32_TestCase extends TestCase
{

   public void testDuplicateProperty()
   {

      String managedProperty = TestUtils.getManagedProperty("setter", "value");
      String manageBean = TestUtils.getManagedBean("bad", ManagedBeanOneProperty.class, "none", managedProperty
            + managedProperty);
      String facesConfig = TestUtils.getFacesConfig(manageBean);

      Node managedBeanNode = TestUtils.createManagedBeanNode(facesConfig, "bad");

      ManagedBeanTestCase testCase = new ManagedBeanTestCase("ManagedBeanTestCase_JSFUNIT_32_TestCase",
            (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0], "bad", managedBeanNode);
      try
      {
         testCase.testDuplicateProperties();
         throw new RuntimeException("should have failed");
      }
      catch (AssertionFailedError e)
      {
         String msg = "managed bean 'bad' in stubbed resource path has a duplicate property named setter";
         assertEquals(msg, e.getMessage());
      }
   }

   public void testDuplicatePropertyNoBeanName()
   {

      String managedProperty = TestUtils.getManagedProperty("setter", "value");
      String manageBean = "<managed-bean>" + "<managed-bean-class>" + ManagedBeanOneProperty.class.getName()
            + "</managed-bean-class>" + "<managed-bean-scope>none</managed-bean-scope>" + managedProperty
            + "</managed-bean>";
      String facesConfig = TestUtils.getFacesConfig(manageBean);

      Node managedBeanNode = TestUtils.extractFirstManagedBeanNode(facesConfig);

      ManagedBeanTestCase testCase = new ManagedBeanTestCase("ManagedBeanTestCase_JSFUNIT_32_TestCase",
            (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0], "bad", managedBeanNode);
      try
      {
         testCase.testDuplicateProperties();
         throw new RuntimeException("should have failed");
      }
      catch (AssertionFailedError e)
      {
         assertEquals("managed bean without 'managed-bean-name' configured", e.getMessage());
      }
   }

   public void testDuplicatePropertyNoPropertyName()
   {

      String managedProperty = "<managed-property>" + "<value>value</value>" + "</managed-property>";
      String manageBean = "<managed-bean>" + "<managed-bean-name>bad</managed-bean-name>" + "<managed-bean-class>"
            + ManagedBeanOneProperty.class.getName() + "</managed-bean-class>"
            + "<managed-bean-scope>none</managed-bean-scope>" + managedProperty + "</managed-bean>";
      String facesConfig = TestUtils.getFacesConfig(manageBean);

      Node managedBeanNode = TestUtils.extractFirstManagedBeanNode(facesConfig);

      ManagedBeanTestCase testCase = new ManagedBeanTestCase("ManagedBeanTestCase_JSFUNIT_32_TestCase",
            (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0], "bad", managedBeanNode);
      try
      {
         testCase.testDuplicateProperties();
         throw new RuntimeException("should have failed");
      }
      catch (AssertionFailedError e)
      {
         assertEquals("managed bean 'bad' property without 'property-name' configured", e.getMessage());
      }
   }

   public void testHappyPaths()
   {

      String managedProperty = TestUtils.getManagedProperty("setter", "value");
      String managedProperty2 = TestUtils.getManagedProperty("setter2", "value");
      String manageBean = TestUtils.getManagedBean("good", ManagedBeanOneProperty.class, "none", managedProperty
            + managedProperty2);
      String facesConfig = TestUtils.getFacesConfig(manageBean);

      Node managedBeanNode = TestUtils.createManagedBeanNode(facesConfig, "good");

      ManagedBeanTestCase testCase = new ManagedBeanTestCase("ManagedBeanTestCase_JSFUNIT_32_TestCase",
            (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0], "bad", managedBeanNode);
      try
      {
         testCase.testDuplicateProperties();
      }
      catch (AssertionFailedError e)
      {
         throw new RuntimeException("should not have failed");
      }
   }
}