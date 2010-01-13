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

import org.w3c.dom.Node;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * A ComponentTestCase_TestCase.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class ComponentTestCase_TestCase extends TestCase
{
   private static final String CORRECT = "<component>"
         + "<description>Test Component</description><display-name>MyComponent</display-name>"
         + "<component-type>MyComponentType</component-type>"
         + "<component-class>org.jboss.jsfunit.analysis.model.TestComponent</component-class>" + "</component>";

   private static final String EMPTY = "<component></component>";

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ComponentTestCase}.
    */
   public void testHappyPathEmpty()
   {
      String facesConfig = Utilities.getFacesConfig(EMPTY);
      Node componentNode = Utilities.extractFirstComponentNode(facesConfig);
      ComponentTestCase testcase = new ComponentTestCase("testHappyPathEmpty", componentNode,
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0]);
      try
      {
         testcase.runTest();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("could not determine name of component in stubbed resource path", afe.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ComponentTestCase}.
    */
   public void testHappyPaths()
   {
      String facesConfig = Utilities.getFacesConfig(CORRECT);
      Node componentNode = Utilities.extractFirstComponentNode(facesConfig);
      ComponentTestCase testcase = new ComponentTestCase("testHappyPaths", componentNode,
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0]);
      testcase.runTest();
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ComponentTestCase#testClassLoadable()}.
    */
   public void testTestClassLoadable()
   {
      String inexistingComponentClass = "<component>"
            + "<description>Test Component</description><display-name>MyComponent</display-name>"
            + "<component-type>MyComponentType</component-type>"
            + "<component-class>com.nonexist.Foo</component-class>" + "</component>";
      String facesConfig = Utilities.getFacesConfig(inexistingComponentClass);
      Node componentNode = Utilities.extractFirstComponentNode(facesConfig);
      ComponentTestCase testcase = new ComponentTestCase("testTestClassLoadable", componentNode,
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0]);
      try
      {
         testcase.setClassName("com.nonexist.Foo");
         testcase.testClassLoadable();
         fail("should have failed");
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("Could not load class 'com.nonexist.Foo' for element 'Component'", afe.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ComponentTestCase#testInterface()}.
    */
   public void testTestInterface()
   {
      String facesConfig = Utilities.getFacesConfig(CORRECT);
      Node componentNode = Utilities.extractFirstComponentNode(facesConfig);
      ComponentTestCase testcase = new ComponentTestCase("testTestInterface", componentNode,
            (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0]);
      try
      {
         testcase.setClassName("org.jboss.jsfunit.analysis.model.Pojo");
         testcase.testInterface();
         fail("should have failed");
      }
      catch (AssertionFailedError afe)
      {
         assertEquals(
               "Component 'org.jboss.jsfunit.analysis.model.Pojo' needs to implement class javax.faces.component.UIComponent",
               afe.getMessage());
      }
   }

}
