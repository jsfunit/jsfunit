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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A TestSuite for a single JSF navigation rule.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $$Revision:  $$
 */
public class NavigationRuleTestSuite extends TestSuite implements Test
{
   /**
    *  <p>The stream provider to be used, default: DefaultStreamProvider.
    *  This allows for special purposes to change the stream provider for the file-accessing methods.</p> 
    *  
    */
   private StreamProvider streamProvider = null;

   /**
    * Create a new NavigationRuleTestSuite.
    * 
    * @param name The name of the test-suite in the JUnit test-hierarchy
    */
   public NavigationRuleTestSuite(String name)
   {
      super(name);
   }

   /**
    * Create the TestSuite for a navigation rule.
    * The TestSuite-name is the name passed to this suite (constructor).
    * 
    * @param configFilePath path to a single config file
    * @param navigationRuleName the name of the bean to build the test suite for
    * @param navigationRuleNode the DOM node containing the navigation rule
    * @return a JUnit Test to be added to the actual test suite
    */
   public Test getSuite(String configFilePath, Node navigationRuleNode)
   {
      TestSuite suite = new TestSuite();
      suite.setName(getName());
      String navigationRuleId = getNavigationRuleId(configFilePath, navigationRuleNode);
      suite
            .addTest(new NavigationRuleTestCase("navigationRule_" + navigationRuleId, configFilePath,
                  navigationRuleNode));
      Map<String, Node> navigationCases = getNavigationCases(configFilePath, navigationRuleNode);
      for (Iterator<String> iterator = navigationCases.keySet().iterator(); iterator.hasNext();)
      {
         String navigationcaseId = iterator.next();
         suite.addTest(new NavigationCaseTestCase("navigationCase_" + navigationcaseId, configFilePath, navigationCases
               .get(navigationcaseId)));
      }
      return suite;
   }

   /**
    * Extract the names and the DOM-Nodes for all managed properties defined for the specified navigation rule.
    * 
    * @param configFilePath path to the config file
    * @param navigationRuleName the name of the bean to build the test suite for
    * @param navigationRuleNode the DOM node containing the navigation rule
    * @return a Map with the property name as a String key and the DOM-Node as the data content
    */
   private Map<String, Node> getNavigationCases(String configFilePath, Node navigationRuleNode)
   {
      Map<String, Node> result = new HashMap<String, Node>();
      String xpathNavigationCase = "./navigation-case";
      String xpathToViewId = "./to-view-id/text()";

      if (navigationRuleNode != null)
      {
         NodeList navigationCases = ParserUtils.query(navigationRuleNode, xpathNavigationCase, configFilePath);
         for (int i = 0; i < navigationCases.getLength(); i++)
         {
            Node navigationCase = navigationCases.item(i);
            String displayName = getDisplayName(configFilePath, navigationCase);
            if (displayName != null && displayName.trim().length() > 0)
            {
               result.put(displayName, navigationCase);
            }
            else
            {
               String toViewId = ParserUtils.querySingle(navigationCase, xpathToViewId, configFilePath);
               if (toViewId != null && toViewId.trim().length() > 0)
               {
                  result.put(toViewId.trim(), navigationCase);
               }
               else
               {
                  throw new RuntimeException("navigation case without to-view-id");
               }
            }
         }
      }

      return result;
   }

   /**
    * <p>Extract the navigation rule id configured in the node. If it is configured the display name is used.
    * Else the from view id (which defaults to "*") is used.</p>
    * 
    * @param configFilePath path to the config file
    * @param navigationRuleNode the DOM node containing the navigation rule
    * @return the navigation rule id
    */
   private String getNavigationRuleId(String configFilePath, Node domNode)
   {
      String result = getDisplayName(configFilePath, domNode);
      if (result != null)
      {
         result = result.trim();
      }
      else
      {
         result = getFromViewId(configFilePath, domNode);
         if (result != null)
         {
            result = result.trim();
         }
      }
      return result;
   }

   /**
    * Extract the display name configured in the node.
    * 
    * @param configFilePath path to the config file
    * @param navigationRuleNode the DOM node containing the navigation rule
    * @return the configured display name for the dom node
    */
   private String getDisplayName(String configFilePath, Node domNode)
   {
      String xpath = "./display-name/text()";
      String result = null;
      if (domNode != null)
      {
         result = ParserUtils.querySingle(domNode, xpath, configFilePath);
         if (result != null)
         {
            result = result.trim();
         }
      }
      return result;
   }

   /**
    * Extract the from-view-id configured in the node.
    * 
    * @param configFilePath path to the config file
    * @param navigationRuleNode the DOM node containing the navigation rule
    * @return the configured from-view-id for the dom node
    */
   private String getFromViewId(String configFilePath, Node domNode)
   {
      String xpath = "./from-view-id/text()";
      String result = null;
      if (domNode != null)
      {
         result = ParserUtils.querySingle(domNode, xpath, configFilePath);
         if (result != null)
         {
            result = result.trim();
         }
         else
         {
            result = "*";
         }
      }
      return result;
   }

   /**
    * Accessor for the streamProvider attribute
    * 
    * @return the streamprovider
    */
   public StreamProvider getStreamProvider()
   {
      if (streamProvider == null)
      {
         streamProvider = new DefaultStreamProvider();
      }
      return streamProvider;
   }

   /**
    * <p>Accessor for the streamProvider attribute.
    * Usually used to set a special StreamProvider. By default a DefaultStreamProvider is created.</p>
    * 
    * @param streamProvider the new StreamProvider
    */
   public void setStreamProvider(StreamProvider streamProvider)
   {
      this.streamProvider = streamProvider;
   }
}
