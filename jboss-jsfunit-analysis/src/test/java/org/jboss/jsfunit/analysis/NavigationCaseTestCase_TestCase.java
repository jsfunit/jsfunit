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
 * A NavigationCaseTestCase_TestCase.
 * 
 * @author <a href="alejesse@gamil.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class NavigationCaseTestCase_TestCase extends TestCase
{
   private static final String CONFIG_FILE_PATH = (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0];

   private static final String CORRECT = "<navigation-rule>"
         + "<description>Test Navigation Rule</description><display-name>MyNavigationCase</display-name>"
         + "<from-view-id>/pages/myFromView.jsp</from-view-id>" + "<navigation-case>"
         + "<display-name>MyNavigationCase</display-name>" + "<to-view-id>/pages/myToView1.jsp</to-view-id>"
         + "<from-action>#{testBean.testAction}</from-action>" + "</navigation-case>" + "</navigation-rule>";

   public void testHappyPaths()
   {
      String facesConfig = Utilities.getFacesConfig(CORRECT);
      Node navigationCaseNode = Utilities.extractFirstNavigationCaseNode(facesConfig);
      NavigationCaseTestCase testCase = new NavigationCaseTestCase("NavigationCaseTestCase_TestCase", CONFIG_FILE_PATH,
            navigationCaseNode);
      testCase.runTest();
   }

   public void testNoToViewId()
   {
      String badCaseNoToViewId = "<navigation-rule>"
            + "<description>Test Navigation Rule</description><display-name>MyNavigationCase</display-name>"
            + "<from-view-id>/pages/myFromView.jsp</from-view-id>" + "<navigation-case>"
            + "<display-name>MyNavigationCase</display-name>" + "</navigation-case>" + "</navigation-rule>";
      String facesConfig = Utilities.getFacesConfig(badCaseNoToViewId);
      Node navigationCaseNode = Utilities.extractFirstNavigationCaseNode(facesConfig);
      NavigationCaseTestCase testCase = new NavigationCaseTestCase("NavigationCaseTestCase_TestCase", CONFIG_FILE_PATH,
            navigationCaseNode);
      try
      {
         testCase.testToViewId();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("'to-view-id' must be configured for a 'navigation-case'", afe.getMessage());
      }
   }

   public void testDuplicateToViewId()
   {
      String badCaseDuplicateToViewId = "<navigation-rule>"
            + "<description>Test Navigation Rule</description><display-name>MyNavigationCase</display-name>"
            + "<from-view-id>/pages/myFromView.jsp</from-view-id>" + "<navigation-case>"
            + "<display-name>MyNavigationCase</display-name>" + "<to-view-id>/pages/myToView1.jsp</to-view-id>"
            + "<to-view-id>/pages/myToView2.jsp</to-view-id>" + "</navigation-case>" + "</navigation-rule>";
      String facesConfig = Utilities.getFacesConfig(badCaseDuplicateToViewId);
      Node navigationCaseNode = Utilities.extractFirstNavigationCaseNode(facesConfig);
      NavigationCaseTestCase testCase = new NavigationCaseTestCase("NavigationCaseTestCase_TestCase", CONFIG_FILE_PATH,
            navigationCaseNode);
      try
      {
         testCase.testToViewId();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("'to-view-id' must not be configured more than once for a 'navigation-case'", afe.getMessage());
      }
   }

   public void testDuplicateRedirect()
   {
      String badCaseDuplicateRedirect = "<navigation-rule>"
            + "<description>Test Navigation Rule</description><display-name>MyNavigationCase</display-name>"
            + "<from-view-id>/pages/myFromView.jsp</from-view-id>" + "<navigation-case>"
            + "<display-name>MyNavigationCase</display-name>" + "<to-view-id>/pages/myToView1.jsp</to-view-id>"
            + "<redirect />" + "<redirect />" + "</navigation-case>" + "</navigation-rule>";
      String facesConfig = Utilities.getFacesConfig(badCaseDuplicateRedirect);
      Node navigationCaseNode = Utilities.extractFirstNavigationCaseNode(facesConfig);
      NavigationCaseTestCase testCase = new NavigationCaseTestCase("NavigationCaseTestCase_TestCase", CONFIG_FILE_PATH,
            navigationCaseNode);
      try
      {
         testCase.testRedirectNoDuplicates();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("'redirect' must not be configured more than once for a 'navigation-case'", afe.getMessage());
      }
   }

   public void testDuplicateFromAction()
   {
      String badCaseDuplicateFromAction = "<navigation-rule>"
            + "<description>Test Navigation Rule</description><display-name>MyNavigationCase</display-name>"
            + "<from-view-id>/pages/myFromView.jsp</from-view-id>" + "<navigation-case>"
            + "<display-name>MyNavigationCase</display-name>" + "<to-view-id>/pages/myToView1.jsp</to-view-id>"
            + "<from-action>#{testBean.testAction1}</from-action>"
            + "<from-action>#{testBean.testAction2}</from-action>" + "</navigation-case>" + "</navigation-rule>";
      String facesConfig = Utilities.getFacesConfig(badCaseDuplicateFromAction);
      Node navigationCaseNode = Utilities.extractFirstNavigationCaseNode(facesConfig);
      NavigationCaseTestCase testCase = new NavigationCaseTestCase("NavigationCaseTestCase_TestCase", CONFIG_FILE_PATH,
            navigationCaseNode);
      try
      {
         testCase.testFromAction();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("'from-action' must not be configured more than once for a 'navigation-case'", afe.getMessage());
      }
   }

   public void testEmptyFromAction()
   {
      String badCaseDuplicateFromAction = "<navigation-rule>"
            + "<description>Test Navigation Rule</description><display-name>MyNavigationCase</display-name>"
            + "<from-view-id>/pages/myFromView.jsp</from-view-id>" + "<navigation-case>"
            + "<display-name>MyNavigationCase</display-name>" + "<to-view-id>/pages/myToView1.jsp</to-view-id>"
            + "<from-action> </from-action>" + "</navigation-case>" + "</navigation-rule>";
      String facesConfig = Utilities.getFacesConfig(badCaseDuplicateFromAction);
      Node navigationCaseNode = Utilities.extractFirstNavigationCaseNode(facesConfig);
      NavigationCaseTestCase testCase = new NavigationCaseTestCase("NavigationCaseTestCase_TestCase", CONFIG_FILE_PATH,
            navigationCaseNode);
      try
      {
         testCase.testFromAction();
      }
      catch (AssertionFailedError afe)
      {
         assertEquals("'from-action' must not be configured with empty content", afe.getMessage());
      }
   }
}