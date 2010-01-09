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
 * A ConverterTestCase_TestCase.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class ConverterTestCase_TestCase extends TestCase
{

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ConverterTestCase}.
    */
   public void testHappyPaths()
   {
      String converter = "<converter><converter-class>org.jboss.jsfunit.analysis.model.TestConverter</converter-class></converter>";
      String facesConfig = TestUtils.getFacesConfig(converter);
      Node converterNode = TestUtils.extractFirstConverterNode(facesConfig);
      ConverterTestCase testcase = new ConverterTestCase("testHappyPaths", converterNode,
            (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0]);
      testcase.runTest();
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ConverterTestCase#testClassLoadable()}.
    */
   public void testTestClassLoadable()
   {
      String converter = "<converter><converter-class>com.nonexist.Foo</converter-class></converter>";
      String facesConfig = TestUtils.getFacesConfig(converter);
      Node converterNode = TestUtils.extractFirstConverterNode(facesConfig);
      ConverterTestCase testcase = new ConverterTestCase("testTestClassLoadable", converterNode,
            (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0]);
      try
      {
         testcase.setClassName("com.nonexist.Foo");
         testcase.testClassLoadable();
         fail("should have failed");
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("Could not load class 'com.nonexist.Foo' for element 'Converter'", afe.getMessage());
      }
   }

   /**
    * Test method for {@link org.jboss.jsfunit.analysis.ConverterTestCase#testInterface()}.
    */
   public void testTestInterface()
   {
      String converter = "<converter><converter-class>org.jboss.jsfunit.analysis.model.Pojo</converter-class></converter>";
      String facesConfig = TestUtils.getFacesConfig(converter);
      Node converterNode = TestUtils.extractFirstConverterNode(facesConfig);
      ConverterTestCase testcase = new ConverterTestCase("testTestInterface", converterNode,
            (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0]);
      try
      {
         testcase.setClassName("org.jboss.jsfunit.analysis.model.Pojo");
         testcase.testInterface();
         fail("should have failed");
      }
      catch (AssertionFailedError afe)
      {
         assertEquals(
               "Converter 'org.jboss.jsfunit.analysis.model.Pojo' needs to implement interface javax.faces.convert.Converter",
               afe.getMessage());
      }
   }

}
