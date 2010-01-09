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

import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Node;

/**
 * A TestCase for a single JSF navigation case.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $$Revision:  $$
 */
public class NavigationCaseTestCase extends TestCase
{
   /** path to the config file */
   protected String configFilePath = null;

   /** DOM node of the navigation case to test */
   protected Node navigationCaseNode;

   /**
    * Create a new NavigationCaseTestCase.
    * 
    * @param name The name of the test-case in the JUnit test-hierarchy
    * @param configFilePath path to a single config file
    * @param navigationCaseNode the DOM node containing the navigation case
    */
   public NavigationCaseTestCase(String name, String configFilePath, Node navigationCaseNode)
   {
      super(name);
      this.configFilePath = configFilePath;
      this.navigationCaseNode = navigationCaseNode;
   }

   /**
    * Call the individual test methods.
    */
   public void runTest()
   {
      testToViewId();
      testRedirectNoDuplicates();
      testFromAction();
   }

   /**
    * Assert that a to-view-id is configured and is not of zero length.
    */
   public void testToViewId()
   {
      try
      {
         assertTrue("'to-view-id' must be configured for a 'navigation-case'", isSingleValueConfigured(getToViewId()));
      }
      catch (RuntimeException re)
      {
         assertTrue(re.getMessage(), re.getMessage().startsWith("query ./to-view-id/text() returned"));
         fail("'to-view-id' must not be configured more than once for a 'navigation-case'");
      }
   }

   /**
    * Assert that a from-action is either configured and is not of zero length or not configured.
    */
   public void testFromAction()
   {
      try
      {
         String fromAction = getFromAction();
         if (fromAction != null && fromAction.trim().length() == 0)
         {
            fail("'from-action' must not be configured with empty content");
         }
      }
      catch (RuntimeException re)
      {
         assertTrue(re.getMessage(), re.getMessage().startsWith("query ./from-action/text() returned"));
         fail("'from-action' must not be configured more than once for a 'navigation-case'");
      }
   }

   /**
    * Assert that either no redirect attribute or exactly one is configured.
    */
   public void testRedirectNoDuplicates()
   {
      try
      {
         getRedirect();
      }
      catch (RuntimeException re)
      {
         assertTrue(re.getMessage(), re.getMessage().startsWith("query ./redirect returned"));
         fail("'redirect' must not be configured more than once for a 'navigation-case'");
      }
   }

   /**
    * Is a value configured and not of zero-length?
    * 
    * @param value a configuration value
    * @return true if value is not null and not an empty String
    */
   private boolean isSingleValueConfigured(String value)
   {
      boolean result = false;
      if (value != null && value.trim().length() > 0)
      {
         result = true;
      }
      return result;
   }

   /**
    * Get the value of the from-action-attribute from the navigation cases DOM-node.
    * 
    * @return the configured from-action or null
    */
   private String getFromAction()
   {
      String xpath = "./from-action/text()";
      String fromAction = ParserUtils.querySingle(navigationCaseNode, xpath, configFilePath);
      return fromAction;
   }

   /**
    * Get the value of the from-outcome-attribute from the navigation cases DOM-node.
    * 
    * @return the configured from-outcome or null
    */
   //   private String getFromOutcome()
   //   {
   //      String xpath = "./from-outcome/text()";
   //      String fromOutcome = ParserUtils.querySingle(navigationCaseNode, xpath, configFilePath);
   //      return fromOutcome;
   //   }

   /**
    * Get the value of the from-view-id-attribute from the navigation cases DOM-node.
    * 
    * @return the configured from-view-id or null
    */
   private String getToViewId()
   {
      String xpath = "./to-view-id/text()";
      String toViewId = ParserUtils.querySingle(navigationCaseNode, xpath, configFilePath);
      return toViewId;
   }

   /**
    * Get the value of the redirect-attribute from the navigation cases DOM-node.
    * 
    * @return the configured redirect or null
    */
   private String getRedirect()
   {
      String xpath = "./redirect";
      String redirect = ParserUtils.querySingle(navigationCaseNode, xpath, configFilePath);
      return redirect;
   }

   /**
    * Get a list of the description-attributes from the navigation cases DOM-node.
    * 
    * @return a list of the configured descriptions
    */
   //   private List<String> getDescriptions()
   //   {
   //      List<String> result = new ArrayList<String>();
   //      String xpath = "./description";
   //      NodeList descriptionNodes  = ParserUtils.query(navigationCaseNode, xpath, configFilePath);
   //      for (int i = 0; i < descriptionNodes.getLength(); i++) {
   //         Node description = descriptionNodes.item(i);
   //         result.add(description.getTextContent());
   //      }
   //      return result;
   //   }

   /**
    * Get a list of the display name-attributes from the navigation cases DOM-node.
    * 
    * @return a list of the configured display names
    */
   //   private List<String> getDisplayNames()
   //   {
   //      List<String> result = new ArrayList<String>();
   //      String xpath = "./display-name";
   //      NodeList displayNames  = ParserUtils.query(navigationCaseNode, xpath, configFilePath);
   //      for (int i = 0; i < displayNames.getLength(); i++) {
   //         Node displayName = displayNames.item(i);
   //         result.add(displayName.getTextContent());
   //      }
   //      return result;
   //   }
}
