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

import junit.framework.Test;
import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.model.ManagedBeanOneProperty;
import org.w3c.dom.Node;

/**
 * A ManagedBeanTestSuite_TestCase.
 * 
 * @author <a href="alejesse@gamil.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class ManagedBeanTestSuite_TestCase extends TestCase
{
   public void testHappyPathWithProperty()
   {
      String managedProperty = Utilities.getManagedProperty("existing", "value");
      String manageBean = Utilities.getManagedBean("good", ManagedBeanOneProperty.class, "none", managedProperty);
      String facesConfig = Utilities.getFacesConfig(manageBean);
      Node managedBeanNode = Utilities.createManagedBeanNode(facesConfig, "good");
      ManagedBeanTestSuite testSuite = new ManagedBeanTestSuite("ManagedBeanTestSuite_TestCase");
      Test test = testSuite.getSuite((String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "good", managedBeanNode);
      assertEquals(2, test.countTestCases());
   }

   public void testHappyPathWithoutProperty()
   {
      String manageBean = Utilities.getManagedBean("good", ManagedBeanOneProperty.class, "none", "");
      String facesConfig = Utilities.getFacesConfig(manageBean);
      Node managedBeanNode = Utilities.createManagedBeanNode(facesConfig, "good");
      ManagedBeanTestSuite testSuite = new ManagedBeanTestSuite("ManagedBeanTestSuite_TestCase");
      Test test = testSuite.getSuite((String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "good", managedBeanNode);
      assertEquals(1, test.countTestCases());
   }

   public void testHappyPathWithoutPropertyName()
   {
      String managedProperty = Utilities.getManagedProperty(" ", "value");
      String manageBean = Utilities.getManagedBean("good", ManagedBeanOneProperty.class, "none", managedProperty);
      String facesConfig = Utilities.getFacesConfig(manageBean);
      Node managedBeanNode = Utilities.createManagedBeanNode(facesConfig, "good");
      ManagedBeanTestSuite testSuite = new ManagedBeanTestSuite("ManagedBeanTestSuite_TestCase");
      Test test = testSuite.getSuite((String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], "good", managedBeanNode);
      assertEquals(1, test.countTestCases());
   }

   public void testNullStreamProvider()
   {
      ManagedBeanTestSuite testSuite = new ManagedBeanTestSuite("ManagedBeanTestSuite_TestCase");
      Object streamProvider = testSuite.getStreamProvider();
      assertNotNull("TestSuite does not provide default StreamProvider", streamProvider);
      assertTrue("TestSuite does not provide default StreamProvider of correct implementation",
            (streamProvider instanceof StreamProvider));
   }

   public void testStreamProviderAccessors()
   {
      String facesConfig = Utilities.getFacesConfig("");
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      ManagedBeanTestSuite testSuite = new ManagedBeanTestSuite("ManagedBeanTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      StreamProvider streamProviderReturned = testSuite.getStreamProvider();
      assertNotNull("TestSuite does not returned passed StreamProvider", streamProviderReturned);
      assertTrue("TestSuite does not returned passed StreamProvider", (streamProvider == streamProviderReturned));
   }
}