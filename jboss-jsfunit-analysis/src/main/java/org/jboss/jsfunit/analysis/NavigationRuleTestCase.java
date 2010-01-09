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
 * A TestCase for a single JSF navigation rule.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $$Revision:  $$
 */
public class NavigationRuleTestCase extends TestCase
{
   /** path to the config file */
   protected String configFilePath = null;

   /** DOM node of the navigation rule to test */
   protected Node navigationRuleNode;

   /**
    * Create a new NavigationRuleTestCase.
    * 
    * @param name The name of the test-case in the JUnit test-hierarchy
    * @param configFilePath path to a single config file
    * @param navigationRuleNode the DOM node containing the navigation rule
    */
   public NavigationRuleTestCase(String name, String configFilePath, Node navigationRuleNode)
   {
      super(name);
      this.configFilePath = configFilePath;
      this.navigationRuleNode = navigationRuleNode;
   }

   /**
    * Call the individual test methods.
    */
   public void runTest()
   {
      testFromViewId();
   }

   /**
    * ...
    */
   public void testFromViewId()
   {
      try
      {
         String fromViewId = getFromViewId();
         if (fromViewId != null && fromViewId.trim().length() == 0)
         {
            fail("'from-view-id' must not be configured with empty content");
         }
      }
      catch (RuntimeException re)
      {
         assertTrue(re.getMessage(), re.getMessage().startsWith("query ./from-view-id/text() returned"));
         fail("'from-view-id' must not be configured more than once for a 'navigation-rule'");
      }
   }

   /**
    * Get the value of the from-view-id-attribute from the navigation rules DOM-node.
    * 
    * @return the configured from-view-id or null
    */
   private String getFromViewId()
   {
      String xpath = "./from-view-id/text()";
      String fromViewId = ParserUtils.querySingle(navigationRuleNode, xpath, configFilePath);
      return fromViewId;
   }

   /**
    * Get a list of the description-attributes from the navigation rules DOM-node.
    * 
    * @return a list of the configured descriptions
    */
   //   private List<String> getDescriptions()
   //   {
   //      List<String> result = new ArrayList<String>();
   //      String xpath = "./description";
   //      NodeList descriptionNodes  = ParserUtils.query(navigationRuleNode, xpath, configFilePath);
   //      for (int i = 0; i < descriptionNodes.getLength(); i++) {
   //         Node description = descriptionNodes.item(i);
   //         result.add(description.getTextContent());
   //      }
   //      return result;
   //   }

   /**
    * Get a list of the display name-attributes from the navigation rules DOM-node.
    * 
    * @return a list of the configured display names
    */
   //   private List<String> getDisplayNames()
   //   {
   //      List<String> result = new ArrayList<String>();
   //      String xpath = "./display-name";
   //      NodeList displayNames  = ParserUtils.query(navigationRuleNode, xpath, configFilePath);
   //      for (int i = 0; i < displayNames.getLength(); i++) {
   //         Node displayName = displayNames.item(i);
   //         result.add(displayName.getTextContent());
   //      }
   //      return result;
   //   }
}
