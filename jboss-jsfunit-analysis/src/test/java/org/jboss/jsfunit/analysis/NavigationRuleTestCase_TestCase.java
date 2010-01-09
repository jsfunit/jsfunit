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

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.w3c.dom.Node;

/**
 * A NavigationRuleTestCase_TestCase.
 * 
 * @author <a href="alejesse@gamil.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class NavigationRuleTestCase_TestCase extends TestCase
{
   private static final String CONFIG_FILE_PATH = (String) TestUtils.STUBBED_RESOURCEPATH.toArray()[0];

   private static final String CORRECT = "<navigation-rule>"
         + "<description>Test Navigation Rule</description><display-name>MyNavigationRule</display-name>"
         + "<from-view-id>/pages/myFromView.jsp</from-view-id>" + "</navigation-rule>";

   public void testHappyPaths()
   {
      String facesConfig = TestUtils.getFacesConfig(CORRECT);
      Node navigationRuleNode = TestUtils.extractFirstNavigationRuleNode(facesConfig);
      NavigationRuleTestCase testCase = new NavigationRuleTestCase("NavigationRuleTestCase_TestCase", CONFIG_FILE_PATH,
            navigationRuleNode);
      testCase.runTest();
   }

   public void testDuplicateFromViewId()
   {
      String badCaseDuplicateFromViewId = "<navigation-rule>"
            + "<description>Test Navigation Rule</description><display-name>MyNavigationRule</display-name>"
            + "<from-view-id>/pages/myFromView1.jsp</from-view-id>"
            + "<from-view-id>/pages/myFromView2.jsp</from-view-id>" + "</navigation-rule>";
      String facesConfig = TestUtils.getFacesConfig(badCaseDuplicateFromViewId);
      Node navigationRuleNode = TestUtils.extractFirstNavigationRuleNode(facesConfig);
      NavigationRuleTestCase testCase = new NavigationRuleTestCase("NavigationRuleTestCase_TestCase", CONFIG_FILE_PATH,
            navigationRuleNode);
      try
      {
         testCase.testFromViewId();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("'from-view-id' must not be configured more than once for a 'navigation-rule'", afe.getMessage());
      }
   }

   public void testEmptyFromAction()
   {
      String badCaseEmptyFromViewId = "<navigation-rule>"
            + "<description>Test Navigation Rule</description><display-name>MyNavigationCase</display-name>"
            + "<from-view-id> </from-view-id>" + "</navigation-rule>";
      String facesConfig = TestUtils.getFacesConfig(badCaseEmptyFromViewId);
      Node navigationRuleNode = TestUtils.extractFirstNavigationRuleNode(facesConfig);
      NavigationRuleTestCase testCase = new NavigationRuleTestCase("NavigationRuleTestCase_TestCase", CONFIG_FILE_PATH,
            navigationRuleNode);
      try
      {
         testCase.testFromViewId();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("'from-view-id' must not be configured with empty content", afe.getMessage());
      }
   }
}