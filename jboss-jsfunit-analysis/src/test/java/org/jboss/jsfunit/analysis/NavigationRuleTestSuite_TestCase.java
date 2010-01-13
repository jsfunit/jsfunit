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

import org.w3c.dom.Node;

/**
 * A NavigationRuleTestSuite_TestCase.
 * 
 * @author <a href="alejesse@gamil.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class NavigationRuleTestSuite_TestCase extends TestCase
{
   private static final String CORRECT_1 = "<navigation-rule>"
         + "<description>Test Navigation Rule</description><display-name>MyNavigationRule</display-name>"
         + "<from-view-id>/pages/myFromView.jsp</from-view-id>" + "<navigation-case>"
         + "<display-name>MyNavigationCase</display-name>" + "<to-view-id>/pages/myToView1.jsp</to-view-id>"
         + "</navigation-case>" + "<navigation-case>" + "<to-view-id>/pages/myToView2.jsp</to-view-id>"
         + "</navigation-case>" + "</navigation-rule>";

   private static final String CORRECT_2 = "<navigation-rule>" + "<description>Test Navigation Rule</description>"
         + "<from-view-id>/pages/myFromView.jsp</from-view-id>" + "<navigation-case>"
         + "<display-name>MyNavigationCase</display-name>" + "<to-view-id>/pages/myToView1.jsp</to-view-id>"
         + "</navigation-case>" + "<navigation-case>" + "<to-view-id>/pages/myToView2.jsp</to-view-id>"
         + "</navigation-case>" + "</navigation-rule>";

   private static final String EMPTY = "<navigation-rule></navigation-rule>";

   private static final String BAD_CASE_NO_TO_VIEW_ID = "<navigation-rule>"
         + "<description>Test Navigation Rule</description><display-name>MyNavigationRule</display-name>"
         + "<from-view-id>/pages/myFromView.jsp</from-view-id>" + "<navigation-case>"
         + "<display-name>MyNavigationCase</display-name>" + "</navigation-case>" + "<navigation-case>"
         + "</navigation-case>" + "</navigation-rule>";

   public void testHappyPathDisplayName()
   {
      String facesConfig = Utilities.getFacesConfig(CORRECT_1);
      Node navigationRuleNode = Utilities.extractFirstNavigationRuleNode(facesConfig);
      NavigationRuleTestSuite testSuite = new NavigationRuleTestSuite("NavigationRuleTestSuite_TestCase");
      Test test = testSuite.getSuite((String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], navigationRuleNode);
      assertEquals(3, test.countTestCases());
   }

   public void testHappyPathViewId()
   {
      String facesConfig = Utilities.getFacesConfig(CORRECT_2);
      Node navigationRuleNode = Utilities.extractFirstNavigationRuleNode(facesConfig);
      NavigationRuleTestSuite testSuite = new NavigationRuleTestSuite("NavigationRuleTestSuite_TestCase");
      Test test = testSuite.getSuite((String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], navigationRuleNode);
      assertEquals(3, test.countTestCases());
   }

   public void testHappyPathEmpty()
   {
      String facesConfig = Utilities.getFacesConfig(EMPTY);
      Node navigationRuleNode = Utilities.extractFirstNavigationRuleNode(facesConfig);
      NavigationRuleTestSuite testSuite = new NavigationRuleTestSuite("NavigationRuleTestSuite_TestCase");
      Test test = testSuite.getSuite((String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], navigationRuleNode);
      assertEquals(1, test.countTestCases());
   }

   public void testHappyPathNullConfigFilePath()
   {
      String facesConfig = Utilities.getFacesConfig(EMPTY);
      Node navigationRuleNode = Utilities.extractFirstNavigationRuleNode(facesConfig);
      NavigationRuleTestSuite testSuite = new NavigationRuleTestSuite("NavigationRuleTestSuite_TestCase");
      Test test = testSuite.getSuite(null, navigationRuleNode);
      assertEquals(1, test.countTestCases());
   }

   public void testHappyPathEmptyConfigFilePath()
   {
      String facesConfig = Utilities.getFacesConfig(EMPTY);
      Node navigationRuleNode = Utilities.extractFirstNavigationRuleNode(facesConfig);
      NavigationRuleTestSuite testSuite = new NavigationRuleTestSuite("NavigationRuleTestSuite_TestCase");
      Test test = testSuite.getSuite("", navigationRuleNode);
      assertEquals(1, test.countTestCases());
   }

   public void testHappyPathNullNavigationRuleNode()
   {
      NavigationRuleTestSuite testSuite = new NavigationRuleTestSuite("NavigationRuleTestSuite_TestCase");
      Test test = testSuite.getSuite((String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], null);
      assertEquals(1, test.countTestCases());
   }

   public void testNavigationCaseWithoutToViewId()
   {
      String facesConfig = Utilities.getFacesConfig(BAD_CASE_NO_TO_VIEW_ID);
      Node navigationRuleNode = Utilities.extractFirstNavigationRuleNode(facesConfig);
      NavigationRuleTestSuite testSuite = new NavigationRuleTestSuite("NavigationRuleTestSuite_TestCase");
      try
      {
         testSuite.getSuite((String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], navigationRuleNode);
      }
      catch (RuntimeException re)
      {
         assertEquals("navigation case without to-view-id", re.getMessage());
      }
   }

   public void testNullStreamProvider()
   {
      NavigationRuleTestSuite testSuite = new NavigationRuleTestSuite("NavigationRuleTestSuite_TestCase");
      Object streamProvider = testSuite.getStreamProvider();
      assertNotNull("TestSuite does not provide default StreamProvider", streamProvider);
      assertTrue("TestSuite does not provide default StreamProvider of correct implementation",
            (streamProvider instanceof StreamProvider));
   }

   public void testStreamProviderAccessors()
   {
      String facesConfig = Utilities.getFacesConfig("");
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      NavigationRuleTestSuite testSuite = new NavigationRuleTestSuite("NavigationRuleTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      StreamProvider streamProviderReturned = testSuite.getStreamProvider();
      assertNotNull("TestSuite does not returned passed StreamProvider", streamProviderReturned);
      assertTrue("TestSuite does not returned passed StreamProvider", (streamProvider == streamProviderReturned));
   }
}