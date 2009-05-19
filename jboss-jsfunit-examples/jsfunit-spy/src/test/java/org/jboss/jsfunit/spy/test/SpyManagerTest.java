/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.jsfunit.spy.test;

import java.io.IOException;
import java.util.Collection;
import java.util.TimeZone;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.jsfunit.spy.data.RequestData;
import org.jboss.jsfunit.spy.data.Session;
import org.jboss.jsfunit.spy.data.SpyManager;

/**
 *
 * @author Stan Silvert
 */
public class SpyManagerTest extends ServletTestCase 
{
   JSFServerSession server;
   JSFClientSession client;
   
   @Override
   public void setUp() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/index.jsf");
      this.server = jsfSession.getJSFServerSession();
      this.client = jsfSession.getJSFClientSession();
   }
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( SpyManagerTest.class );
   }

   public void testManagerAccess() throws IOException
   {
      Object spyManagerViaEL = server.getManagedBeanValue("#{spymanager}");
      assertNotNull(spyManagerViaEL);
      Object spyManagerViaContext = SpyManager.getInstance();
      assertEquals(spyManagerViaEL, spyManagerViaContext);
   } 
   
   public void testGetCurrentRequest() throws IOException
   {
      SpyManager spyManager = SpyManager.getInstance();
      RequestData data = spyManager.getCurrentRequest();
      assertNotNull(data);
      HttpServletRequest request = data.getHttpRequest();
      assertNotNull(request);
      assertEquals("/jboss-jsfunit-examples-spy", request.getContextPath());
      assertEquals("/index.jsf", request.toString());
   }
   
   public void testGetSessions() throws IOException
   {
      SpyManager spyManager = SpyManager.getInstance();
      Collection<Session> sessions = spyManager.getSessions();
      assertNotNull(sessions);
      
      // Two tests before this also created sessions
      assertTrue(sessions.size() > 2);
   }
   
   public void testGetMySession() throws IOException
   {
      SpyManager spyManager = SpyManager.getInstance();
      Session session = spyManager.getMySession();
      assertNotNull(session);
      assertEquals(1, session.getRequests().size());
      
      FacesContext facesContext = server.getFacesContext();
      HttpSession httpSession = (HttpSession)facesContext.getExternalContext().getSession(true);
      
      assertEquals(httpSession.getId(), session.getId());
      assertEquals(httpSession.getCreationTime(), session.getCreationTime());
      assertEquals(httpSession.getMaxInactiveInterval(), session.getMaxInactiveInterval());
   }
   
   public void testGetRequestParams() throws IOException
   {
      client.click("submit_button");
      
      SpyManager spyManager = SpyManager.getInstance();
      RequestData request = spyManager.getCurrentRequest();
      assertEquals("value1", request.getHttpRequest().getParameter("form1:param1"));
      assertEquals("value2", request.getHttpRequest().getParameter("form1:param2"));
   }
   
   public void testGetTimeZone() throws IOException
   {
      SpyManager spyManager = SpyManager.getInstance();
      TimeZone timeZone = spyManager.getTimeZone();
      assertNotNull(timeZone);
      assertEquals(TimeZone.getDefault(), timeZone);
   }
}
