/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.jsfunit.jsfsession.hellojsf;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test the JSFUnit Console.
 *
 * This class extends TestCase instead of ServletTestCase.  The reason is
 * because we don't want this class to show up the console, which
 * only includes classes that extend ServletTestCase.  If this class were
 * included in the console then the "Run All Tests" link would go into an
 * infinite loop.
 * 
 * @author Stan Silvert
 */
public class ConsoleTest extends TestCase
{

   private HtmlPage consolePage;

   @Override
   public void setUp() throws IOException
   {
      WebClient webClient = new WebClient();
      consolePage = webClient.getPage("http://localhost:8080/jboss-jsfunit-examples-hellojsf-jee6/jsfunit/index.jsfunit");
   }
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( ConsoleTest.class  );
   }
   
   public void testRunAllTests() throws IOException
   {
      assertTrue(consolePage.asXml().contains("JSFUnit Console"));

      HtmlElement runAllLink = consolePage.getElementById("runAllTests");
      verifyTestOutput((XmlPage)runAllLink.click());
   }

   public void testRunIndividualTest() throws IOException
   {
      HtmlAnchor testLink = consolePage.getAnchorByText("org.jboss.jsfunit.jsfsession.hellojsf.FacadeAPITest");
      verifyTestOutput((XmlPage)testLink.click());
   }

   private void verifyTestOutput(XmlPage xmlPage)
   {
      String testOutput = xmlPage.asXml();
      assertTrue(testOutput.contains("failures=\"0\""));
      assertTrue(testOutput.contains("errors=\"0\""));
   }
      
}
