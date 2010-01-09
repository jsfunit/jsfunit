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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.model.Pojo;
import org.jboss.jsfunit.analysis.model.SerializablePojo;
import org.w3c.dom.Node;

/**
 * A TestCase related to Jira-issue JSFUNIT_26.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class ManagedBeanTestCase_JSFUNIT_36_TestCase extends TestCase
{
   public void testHappyPaths()
   {

      List<String> scopes = new ArrayList<String>()
      {
         {
            add("request");
            add("none");
         }
      };

      for (String scope : scopes)
      {
         String manageBean = TestUtils.getManagedBean("good", Pojo.class, scope);
         String facesConfig = TestUtils.getFacesConfig(manageBean);
         Node managedBeanNode = TestUtils.createManagedBeanNode(facesConfig, "good");
         ManagedBeanTestCase testCase = new ManagedBeanTestCase("ManagedBeanTestCase_JSFUNIT_26_TestCase",
               (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0], "good", managedBeanNode);
         testCase.testSerializableInterface();
      }

      scopes.add("session");
      scopes.add("application");

      for (String scope : scopes)
      {
         String manageBean = TestUtils.getManagedBean("good2", SerializablePojo.class, scope);
         String facesConfig = TestUtils.getFacesConfig(manageBean);
         Node managedBeanNode = TestUtils.createManagedBeanNode(facesConfig, "good2");
         ManagedBeanTestCase testCase = new ManagedBeanTestCase("ManagedBeanTestCase_JSFUNIT_26_TestCase",
               (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0], "good2", managedBeanNode);
         testCase.testSerializableInterface();
      }

   }

   public void testNotSerializable()
   {

      List<String> scopes = new ArrayList<String>()
      {
         {
            add("session");
            add("application");
         }
      };

      for (String scope : scopes)
      {
         testNotSerializable(TestUtils.getManagedBean("bad", Pojo.class, scope));
      }
   }

   private void testNotSerializable(String manageBean)
   {

      String facesConfig = TestUtils.getFacesConfig(manageBean);
      Node managedBeanNode = TestUtils.createManagedBeanNode(facesConfig, "bad");
      ManagedBeanTestCase testCase = new ManagedBeanTestCase("ManagedBeanTestCase_JSFUNIT_26_TestCase",
            (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0], "bad", managedBeanNode);

      try
      {
         testCase.testSerializableInterface();

         throw new RuntimeException("should have failed because " + Pojo.class + " is not " + Serializable.class);

      }
      catch (AssertionFailedError e)
      {
      }
   }
}