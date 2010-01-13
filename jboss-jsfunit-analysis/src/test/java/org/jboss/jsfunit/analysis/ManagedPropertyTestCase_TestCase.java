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

import org.jboss.jsfunit.analysis.model.ManagedBean;
import org.jboss.jsfunit.analysis.model.ManagedBeanOneProperty;
import org.w3c.dom.Node;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * A ManagedPropertyTestCase_TestCase.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class ManagedPropertyTestCase_TestCase extends TestCase
{

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ManagedPropertyTestCase#runTest()}.
    */
   public void testHappyPaths()
   {
      String managedProperty = "<managed-property>" + " <property-name>existingMap</property-name>" + " <map-entries>"
            + "  <map-entry>" + "       <key>duplicate</key>" + "       <value>3</value>" + "  </map-entry>"
            + "  <map-entry>" + "   <key>duplicate2</key>" + "   <value>4</value>" + "  </map-entry>"
            + " </map-entries>" + "</managed-property>";
      String managedBean = Utilities.getManagedBean("bean", ManagedBeanWithMap.class, "none", managedProperty);
      String managedProperty2 = Utilities.getManagedProperty("existing", "value");
      String managedBean2 = Utilities.getManagedBean("good", ManagedBeanOneProperty.class, "none", managedProperty2);
      String facesConfig = Utilities.getFacesConfig(managedBean + managedBean2);
      Node managedPropertyNode = Utilities.createManagedPropertyNode(facesConfig, "existingMap");
      try
      {
         ManagedPropertyTestCase testCase = new ManagedPropertyTestCase(
               "ManagedPropertyTestCase_testTestMapDuplicateKeys",
               (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "bean", ManagedBean.class.getName(),
               "existingMap", managedPropertyNode);
         testCase.runTest();
      }
      catch (AssertionFailedError afe)
      {
         throw new RuntimeException("should not have failed ... " + afe.getMessage());
      }
      try
      {
         ManagedPropertyTestCase testCase = new ManagedPropertyTestCase(
               "ManagedPropertyTestCase_testTestMapDuplicateKeys",
               (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "good", ManagedBean.class.getName(), "existing",
               managedPropertyNode);
         testCase.runTest();
      }
      catch (AssertionFailedError afe)
      {
         throw new RuntimeException("should not have failed");
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ManagedPropertyTestCase#runTest()}.
    */
   public void testHappyPathMapDuplicateKeys()
   {
      String property = "<managed-property>" + " <property-name>map</property-name>" + " <map-entries>"
            + "  <map-entry>" + "       <key>duplicate</key>" + "       <value>3</value>" + "  </map-entry>"
            + "  <map-entry>" + "   <key>duplicate2</key>" + "   <value>4</value>" + "  </map-entry>"
            + " </map-entries>" + "</managed-property>";
      String managedBean = Utilities.getManagedBean("bean", ManagedBeanWithMap.class, "none", property);
      String facesConfig = Utilities.getFacesConfig(managedBean);
      Node managedPropertyNode = Utilities.createManagedPropertyNode(facesConfig, "map");
      ManagedPropertyTestCase testCase = new ManagedPropertyTestCase(
            "ManagedPropertyTestCase_testTestMapDuplicateKeys", (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0],
            "bean", ManagedBean.class.getName(), "map", managedPropertyNode);
      try
      {
         testCase.testMapDuplicateKeys();
      }
      catch (AssertionFailedError afe)
      {
         throw new RuntimeException("should not have failed");
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ManagedPropertyTestCase#runTest()}.
    */
   public void testHappyPathPropertyAccessor()
   {
      String managedProperty = Utilities.getManagedProperty("existing", "value");
      String manageBean = Utilities.getManagedBean("good", ManagedBeanOneProperty.class, "none", managedProperty);
      String facesConfig = Utilities.getFacesConfig(manageBean);

      Node managedPropertyNode = Utilities.createManagedPropertyNode(facesConfig, "existing");

      ManagedPropertyTestCase testCase = new ManagedPropertyTestCase(
            "ManagedPropertyTestCase_testHappyPathPropertyAccessor",
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "good", ManagedBean.class.getName(), "existing",
            managedPropertyNode);
      testCase.testPropertyAccessors();
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ManagedPropertyTestCase#testPropertyAccessors()}.
    */
   public void testTestPropertyAccessors()
   {
      String managedProperty1 = Utilities.getManagedProperty("notFound", "value");
      String managedProperty2 = Utilities.getManagedProperty("noSetter", "value");
      String managedProperty3 = Utilities.getManagedProperty("noGetter", "value");
      String manageBean = Utilities.getManagedBean("good", ManagedBeanOneProperty.class, "none", managedProperty1
            + managedProperty2 + managedProperty3);
      String facesConfig = Utilities.getFacesConfig(manageBean);
      Node managedPropertyNode1 = Utilities.createManagedPropertyNode(facesConfig, "notFound");
      Node managedPropertyNode2 = Utilities.createManagedPropertyNode(facesConfig, "noSetter");
      Node managedPropertyNode3 = Utilities.createManagedPropertyNode(facesConfig, "noGetter");
      try
      {
         ManagedPropertyTestCase testCase = new ManagedPropertyTestCase(
               "ManagedPropertyTestCase_testTestMapDuplicateKeys",
               (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "bean", ManagedBean.class.getName(), "notFound",
               managedPropertyNode1);
         testCase.testPropertyAccessors();
         fail("should have failed");
      }
      catch (AssertionFailedError afe)
      {
         assertEquals(
               "The managed bean 'bean' has a managed property called 'notFound', but org.jboss.jsfunit.analysis.model.ManagedBean has no method setNotFound(?)",
               afe.getMessage());
      }
      try
      {
         ManagedPropertyTestCase testCase = new ManagedPropertyTestCase(
               "ManagedPropertyTestCase_testTestMapDuplicateKeys",
               (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "bean", ManagedBean.class.getName(), "noSetter",
               managedPropertyNode2);
         testCase.testPropertyAccessors();
         fail("should have failed");
      }
      catch (AssertionFailedError afe)
      {
         assertEquals(
               "The managed bean 'bean' has a managed property called 'noSetter', but org.jboss.jsfunit.analysis.model.ManagedBean has no method setNoSetter(?)",
               afe.getMessage());
      }
      try
      {
         ManagedPropertyTestCase testCase = new ManagedPropertyTestCase(
               "ManagedPropertyTestCase_testTestMapDuplicateKeys",
               (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "bean", ManagedBean.class.getName(), "noGetter",
               managedPropertyNode3);
         testCase.testPropertyAccessors();
         fail("should have failed");
      }
      catch (AssertionFailedError afe)
      {
         assertEquals(
               "The managed bean 'bean' has a managed property called 'noGetter', but org.jboss.jsfunit.analysis.model.ManagedBean has neither a method getNoGetter() nor a method isNoGetter()",
               afe.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ManagedPropertyTestCase#testMapDuplicateKeys()}.
    */
   public void testTestMapDuplicateKeys()
   {
      String property = "<managed-property>" + " <property-name>map</property-name>" + " <map-entries>"
            + "  <map-entry>" + "       <key>duplicate</key>" + "       <value>3</value>" + "  </map-entry>"
            + "  <map-entry>" + "   <key>duplicate</key>" + "   <value>4</value>" + "  </map-entry>"
            + " </map-entries>" + "</managed-property>";
      String managedBean = Utilities.getManagedBean("bean", ManagedBeanWithMap.class, "none", property);
      String facesConfig = Utilities.getFacesConfig(managedBean);
      Node managedPropertyNode = Utilities.createManagedPropertyNode(facesConfig, "map");
      ManagedPropertyTestCase testCase = new ManagedPropertyTestCase(
            "ManagedPropertyTestCase_testTestMapDuplicateKeys", (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0],
            "bean", ManagedBean.class.getName(), "map", managedPropertyNode);
      try
      {
         testCase.testMapDuplicateKeys();
         fail("should have failed");
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("Managed Bean 'bean' has a managed Map property with a duplicate key 'duplicate'", afe
               .getMessage());
      }
   }
   /*
         String managedProperty = Utilities.getManagedProperty("setter", "value");
         String manageBean = Utilities.getManagedBean("bad", ManagedBeanOneProperty.class, "none", managedProperty
               + managedProperty);
         String facesConfig = Utilities.getFacesConfig(manageBean);

         Node managedBeanNode = Utilities.createManagedBeanNode(facesConfig, "bad");

         ManagedBeanTestCase testCase = new ManagedBeanTestCase("ManagedBeanTestCase_JSFUNIT_32_TestCase",
               (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "bad", managedBeanNode);

    */
}
