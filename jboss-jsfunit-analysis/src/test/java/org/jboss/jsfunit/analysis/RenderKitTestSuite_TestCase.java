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
 * A RenderKitTestSuite_TestCase.
 * 
 * @author <a href="alejesse@gamil.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class RenderKitTestSuite_TestCase extends TestCase
{
   private static final String CORRECT = "<render-kit>"
         + "<description>Test Render Kit</description><display-name>MyRenderKit</display-name>"
         + "<render-kit-id>MyRenderKitId</render-kit-id>"
         + "<render-kit-class>org.jboss.jsfunit.analysis.modell.TestRenderKit</render-kit-class>"
         + "<renderer></renderer>" + "<renderer>" + "<display-name>MyRenderer</display-name>" + "</renderer>"
         + "<renderer>" + "<renderer-class>org.jboss.jsfunit.analysis.modell.TestRenderer</renderer-class>"
         + "</renderer>" + "</render-kit>";

   private static final String EMPTY = "<render-kit></render-kit>";

   public void testHappyPath()
   {
      String facesConfig = Utilities.getFacesConfig(CORRECT);
      Node renderKitNode = Utilities.extractFirstRenderKitNode(facesConfig);
      RenderKitTestSuite testSuite = new RenderKitTestSuite("RenderKitTestSuite_TestCase");
      Test test = testSuite.getSuite((String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], renderKitNode);
      assertEquals(4, test.countTestCases());
   }

   public void testHappyPathEmpty()
   {
      String facesConfig = Utilities.getFacesConfig(EMPTY);
      Node renderKitNode = Utilities.extractFirstRenderKitNode(facesConfig);
      RenderKitTestSuite testSuite = new RenderKitTestSuite("RenderKitTestSuite_TestCase");
      Test test = testSuite.getSuite((String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], renderKitNode);
      assertEquals(1, test.countTestCases());
   }

   public void testHappyPathNullConfigFilePath()
   {
      String facesConfig = Utilities.getFacesConfig(EMPTY);
      Node renderKitNode = Utilities.extractFirstRenderKitNode(facesConfig);
      RenderKitTestSuite testSuite = new RenderKitTestSuite("RenderKitTestSuite_TestCase");
      Test test = testSuite.getSuite(null, renderKitNode);
      assertEquals(1, test.countTestCases());
   }

   public void testHappyPathEmptyConfigFilePath()
   {
      String facesConfig = Utilities.getFacesConfig(EMPTY);
      Node renderKitNode = Utilities.extractFirstRenderKitNode(facesConfig);
      RenderKitTestSuite testSuite = new RenderKitTestSuite("RenderKitTestSuite_TestCase");
      Test test = testSuite.getSuite("", renderKitNode);
      assertEquals(1, test.countTestCases());
   }

   public void testHappyPathNullRenderKitNode()
   {
      RenderKitTestSuite testSuite = new RenderKitTestSuite("RenderKitTestSuite_TestCase");
      Test test = testSuite.getSuite((String) Utilities.STUBBED_RESOURCEPATH.toArray()[0], null);
      assertEquals(1, test.countTestCases());
   }

   public void testNullStreamProvider()
   {
      RenderKitTestSuite testSuite = new RenderKitTestSuite("RenderKitTestSuite_TestCase");
      Object streamProvider = testSuite.getStreamProvider();
      assertNotNull("TestSuite does not provide default StreamProvider", streamProvider);
      assertTrue("TestSuite does not provide default StreamProvider of correct implementation",
            (streamProvider instanceof StreamProvider));
   }

   public void testStreamProviderAccessors()
   {
      String facesConfig = Utilities.getFacesConfig(CORRECT);
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      RenderKitTestSuite testSuite = new RenderKitTestSuite("RenderKitTestSuite_TestCase");
      testSuite.setStreamProvider(streamProvider);
      StreamProvider streamProviderReturned = testSuite.getStreamProvider();
      assertNotNull("TestSuite does not returned passed StreamProvider", streamProviderReturned);
      assertTrue("TestSuite does not returned passed StreamProvider", (streamProvider == streamProviderReturned));
   }
}