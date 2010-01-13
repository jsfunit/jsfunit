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

import org.jboss.jsfunit.analysis.model.ManagedBeanOneProperty;
import org.jboss.jsfunit.analysis.model.ManagedBeanOnePropertySerializable;
import org.jboss.jsfunit.analysis.model.Pojo;
import org.w3c.dom.Node;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * A ManagedBeanTestCase_TestCase.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class ManagedBeanTestCase_TestCase extends TestCase
{

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ManagedBeanTestCase#runTest()}.
    */
   public void testHappyPaths()
   {
      String managedProperty = Utilities.getManagedProperty("setter", "value");
      String managedProperty2 = Utilities.getManagedProperty("setter2", "value");
      String managedBean = Utilities.getManagedBean("good", ManagedBeanOneProperty.class, "none", managedProperty
            + managedProperty2);
      String managedBean2 = Utilities.getManagedBean("goodSession", ManagedBeanOnePropertySerializable.class,
            "session", managedProperty + managedProperty2);
      String managedBean3 = Utilities.getManagedBean("goodNoProperties", ManagedBeanOneProperty.class, "request");
      String facesConfig = Utilities.getFacesConfig(managedBean + managedBean2 + managedBean3);

      Node managedBeanNode = Utilities.createManagedBeanNode(facesConfig, "good");
      Node managedBeanSessionNode = Utilities.createManagedBeanNode(facesConfig, "goodSession");
      Node managedBeanNoPropertiesNode = Utilities.createManagedBeanNode(facesConfig, "goodNoProperties");

      ManagedBeanTestCase testCase = new ManagedBeanTestCase("testHappyPaths", (String) Utilities.STUBBED_RESOURCEPATH
            .toArray()[0], "good", managedBeanNode);
      ManagedBeanTestCase testCaseSession = new ManagedBeanTestCase("testHappyPaths_session",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "goodSession", managedBeanSessionNode);
      ManagedBeanTestCase testCaseNoProperties = new ManagedBeanTestCase("testHappyPaths_noProperties",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "goodNoProperties", managedBeanNoPropertiesNode);
      try
      {
         testCase.runTest();
         testCaseSession.runTest();
         testCaseNoProperties.runTest();
      }
      catch (AssertionFailedError e)
      {
         throw new RuntimeException("should not have failed");
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ManagedBeanTestCase#testNameAttribute()}.
    */
   public void testTestNameAttribute()
   {
      //      String manageBean = "<managed-bean><managed-bean-name>good</managed-bean-name><managed-bean-class>org.jboss.jsfunit.analysis.model.Pojo</managed-bean-class><managed-bean-scope>request</managed-bean-scope></managed-bean>";
      String managedBean = "<managed-bean><managed-bean-class>org.jboss.jsfunit.analysis.model.Pojo</managed-bean-class><managed-bean-scope>request</managed-bean-scope></managed-bean>";
      String facesConfig = Utilities.getFacesConfig(managedBean);
      Node managedBeanNode = Utilities.extractFirstManagedBeanNode(facesConfig);
      ManagedBeanTestCase testcase = new ManagedBeanTestCase("testTestNameAttribute",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "good", managedBeanNode);
      try
      {
         testcase.testNameAttribute();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("could not determine name of managed-bean in stubbed resource path", afe.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ManagedBeanTestCase#testClassAttribute()}.
    */
   public void testTestClassAttribute()
   {
      //      String manageBean = "<managed-bean><managed-bean-name>good</managed-bean-name><managed-bean-class>org.jboss.jsfunit.analysis.model.Pojo</managed-bean-class><managed-bean-scope>request</managed-bean-scope></managed-bean>";
      String managedBean = "<managed-bean><managed-bean-name>bad</managed-bean-name><managed-bean-scope>request</managed-bean-scope></managed-bean>";
      String facesConfig = Utilities.getFacesConfig(managedBean);
      Node managedBeanNode = Utilities.createManagedBeanNode(facesConfig, "bad");
      ManagedBeanTestCase testcase = new ManagedBeanTestCase("testTestClassAttribute",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "bad", managedBeanNode);
      try
      {
         testcase.testClassAttribute();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("could not determine class of managed-bean in stubbed resource path", afe.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ManagedBeanTestCase#testScopeAttribute()}.
    */
   public void testTestScopeAttribute()
   {
      //      String manageBean = "<managed-bean><managed-bean-name>good</managed-bean-name><managed-bean-class>org.jboss.jsfunit.analysis.model.Pojo</managed-bean-class><managed-bean-scope>request</managed-bean-scope></managed-bean>";
      String managedBean = "<managed-bean><managed-bean-name>bad</managed-bean-name><managed-bean-class>org.jboss.jsfunit.analysis.model.Pojo</managed-bean-class></managed-bean>";
      String facesConfig = Utilities.getFacesConfig(managedBean);
      Node managedBeanNode = Utilities.createManagedBeanNode(facesConfig, "bad");
      ManagedBeanTestCase testcase = new ManagedBeanTestCase("testTestScopeAttribute",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "bad", managedBeanNode);
      try
      {
         testcase.testScopeAttribute();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("could not determine scope of managed-bean in stubbed resource path", afe.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ManagedBeanTestCase#testClassLoadable()}.
    */
   public void testTestClassLoadable()
   {
      String managedBean = "<managed-bean><managed-bean-name>unloadable</managed-bean-name><managed-bean-class>does.not.exist.ImpossibleToLoad</managed-bean-class><managed-bean-scope>request</managed-bean-scope></managed-bean>";
      String facesConfig = Utilities.getFacesConfig(managedBean);
      Node managedBeanNode = Utilities.createManagedBeanNode(facesConfig, "unloadable");
      ManagedBeanTestCase testcase = new ManagedBeanTestCase("testTestClassLoadable",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "unloadable", managedBeanNode);
      try
      {
         testcase.testClassLoadable();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("Could not load class 'does.not.exist.ImpossibleToLoad' for element 'managed bean'", afe
               .getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ManagedBeanTestCase#testValidScope()}.
    */
   public void testTestValidScope()
   {
      String managedBean = "<managed-bean><managed-bean-name>badScope</managed-bean-name><managed-bean-class>org.jboss.jsfunit.analysis.model.Pojo</managed-bean-class><managed-bean-scope>wrongScope</managed-bean-scope></managed-bean>";
      String facesConfig = Utilities.getFacesConfig(managedBean);
      Node managedBeanNode = Utilities.createManagedBeanNode(facesConfig, "badScope");
      ManagedBeanTestCase testcase = new ManagedBeanTestCase("testTestValidScope",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "badScope", managedBeanNode);
      try
      {
         testcase.testValidScope();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("Managed bean 'badScope' in stubbed resource path has an invalid scope 'wrongScope'", afe
               .getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ManagedBeanTestCase#testSerializableInterface()}.
    */
   public void testTestSerializableInterface()
   {
      String managedBean = "<managed-bean><managed-bean-name>bad</managed-bean-name><managed-bean-class>org.jboss.jsfunit.analysis.model.Pojo</managed-bean-class><managed-bean-scope>session</managed-bean-scope></managed-bean>";
      String facesConfig = Utilities.getFacesConfig(managedBean);
      Node managedBeanNode = Utilities.createManagedBeanNode(facesConfig, "bad");
      ManagedBeanTestCase testcase = new ManagedBeanTestCase("testTestSerializableInterface",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "bad", managedBeanNode);
      try
      {
         testcase.testNameAttribute();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("could not determine name of managed-bean in stubbed resource path", afe.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ManagedBeanTestCase#testDuplicateProperties()}.
    */
   public void testTestDuplicateProperties()
   {
      String duplicateProperty = Utilities.getManagedProperty("duplicate", "value");
      String manageBean = Utilities.getManagedBean("duplicates", Pojo.class, "request", duplicateProperty
            + duplicateProperty);
      String facesConfig = Utilities.getFacesConfig(manageBean);
      Node managedBeanNode = Utilities.createManagedBeanNode(facesConfig, "duplicates");
      ManagedBeanTestCase testcase = new ManagedBeanTestCase("testTestSerializableInterface",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "duplicates", managedBeanNode);
      try
      {
         testcase.testDuplicateProperties();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("managed bean 'duplicates' in stubbed resource path has a duplicate property named duplicate",
               afe.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ManagedBeanTestCase#testDuplicateProperties()}.
    */
   public void testTestDuplicatePropertiesBeanNotFound()
   {
      String manageBean = Utilities.getManagedBean("duplicates", Pojo.class, "request");
      String facesConfig = Utilities.getFacesConfig(manageBean);
      Node managedBeanNode = Utilities.createManagedBeanNode(facesConfig, "duplicates");
      ManagedBeanTestCase testcase = new ManagedBeanTestCase("testTestSerializableInterface",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "duplicate", managedBeanNode);
      testcase.testDuplicateProperties();
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ManagedBeanTestCase#testDuplicateProperties()}.
    */
   public void testTestDuplicatePropertiesNoProperties()
   {
      String manageBean = Utilities.getManagedBean("duplicates", Pojo.class, "request");
      String facesConfig = Utilities.getFacesConfig(manageBean);
      Node managedBeanNode = Utilities.createManagedBeanNode(facesConfig, "duplicates");
      ManagedBeanTestCase testcase = new ManagedBeanTestCase("testTestSerializableInterface",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "duplicates", managedBeanNode);
      testcase.testDuplicateProperties();
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ManagedBeanTestCase#testDuplicateProperties()}.
    */
   public void testTestDuplicatePropertiesNoBeanName()
   {
      String managedBean = "<managed-bean>"
            + "<managed-bean-class>org.jboss.jsfunit.analysis.model.Pojo</managed-bean-class>"
            + "<managed-bean-scope>none</managed-bean-scope>"
            + "<managed-property><property-name>duplicate</property-name><value>value</value></managed-property>"
            + "<managed-property><property-name>duplicate</property-name><value>value</value></managed-property>"
            + "</managed-bean>";
      String facesConfig = Utilities.getFacesConfig(managedBean);
      Node managedBeanNode = Utilities.extractFirstManagedBeanNode(facesConfig);
      ManagedBeanTestCase testcase = new ManagedBeanTestCase("testTestSerializableInterface",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "duplicates", managedBeanNode);
      try
      {
         testcase.testDuplicateProperties();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("managed bean without 'managed-bean-name' configured", afe.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ManagedBeanTestCase#testDuplicateProperties()}.
    */
   public void testTestDuplicatePropertiesNoPropertyName()
   {
      String managedBean = "<managed-bean>" + "<managed-bean-name>duplicates</managed-bean-name>"
            + "<managed-bean-class>org.jboss.jsfunit.analysis.model.Pojo</managed-bean-class>"
            + "<managed-bean-scope>none</managed-bean-scope>"
            + "<managed-property><property-name>duplicate</property-name><value>value</value></managed-property>"
            + "<managed-property><value>value</value></managed-property>"
            + "<managed-property><property-name>duplicate</property-name><value>value</value></managed-property>"
            + "</managed-bean>";
      String facesConfig = Utilities.getFacesConfig(managedBean);
      Node managedBeanNode = Utilities.createManagedBeanNode(facesConfig, "duplicates");
      ManagedBeanTestCase testcase = new ManagedBeanTestCase("testTestSerializableInterface",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "duplicates", managedBeanNode);
      try
      {
         testcase.testDuplicateProperties();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("managed bean 'duplicates' property without 'property-name' configured", afe.getMessage());
      }
   }
}
