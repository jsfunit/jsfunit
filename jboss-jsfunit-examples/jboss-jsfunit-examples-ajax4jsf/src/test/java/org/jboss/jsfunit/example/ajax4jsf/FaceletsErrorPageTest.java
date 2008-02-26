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

package org.jboss.jsfunit.example.ajax4jsf;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.facade.FaceletsErrorPageException;
import org.jboss.jsfunit.facade.JSFClientSession;
import org.xml.sax.SAXException;

/**
 * If in facelets development mode, facelets will return an HTML error page
 * instead of throwing an error.
 *
 * @author Stan Silvert
 */
public class FaceletsErrorPageTest extends ServletTestCase
{
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( FaceletsErrorPageTest.class );
   }

   /**
    * If in dev mode, facelets brings up an error page if JSF throws an error.
    */
   public void testErrorPageDetection() throws IOException, SAXException
   {
      try 
      {
         JSFClientSession client = new JSFClientSession("/badIndex.jsf");
         fail("Expeted FaceletsErrorPageException");
      } 
      catch (FaceletsErrorPageException e)
      {
         // OK
      }
      
   } 
   
   public void testIsFaceletsErrorPage() throws IOException, SAXException
   {
      JSFClientSession client = new JSFClientSession("/index.jsf");
      assertFalse(FaceletsErrorPageException.isFaceletsErrorPage(client));
   }
   
}
