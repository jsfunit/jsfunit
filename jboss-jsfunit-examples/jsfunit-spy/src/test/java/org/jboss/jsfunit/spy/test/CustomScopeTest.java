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
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.jsfunit.spy.data.RequestData;
import org.jboss.jsfunit.spy.data.Snapshot;
import org.jboss.jsfunit.spy.data.SpyManager;
import org.jboss.jsfunit.spy.test.customscope.CustomScopeBean;

/**
 *
 * @author Stan Silvert
 */
public class CustomScopeTest extends ServletTestCase 
{
   private JSFServerSession server;
   private JSFClientSession client;
   private SpyManager spyManager;
   
   public void setUp() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/index.jsf");
      this.server = jsfSession.getJSFServerSession();
      this.client = jsfSession.getJSFClientSession();
      this.spyManager = SpyManager.getInstance();
   }
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( CustomScopeTest.class );
   }

   public void testGetCustomScopes() throws IOException
   {
      // create custom scope 1
      client.click("create1");
      client.click("scope_button");
      
      client.click("submit_button");
      RequestData reqData = spyManager.getCurrentRequest();
      for (Snapshot snapshot: reqData.getSnapshots())
      {
         Map<String, Map<String, String>> customScopes = snapshot.getCustomScopes();
         assertNotNull(customScopes);
         assertEquals(1, customScopes.size());
         assertTrue(customScopes.containsKey(CustomScopeBean.NAME1));
         for (Map<String, String> scope : customScopes.values())
         {
            assertEquals(2, scope.size());
            assertEquals("value1", scope.get("key1"));
            assertEquals("value2", scope.get("key2"));
         }
      }
      
      // destroy custom scope 1
      client.click("destroy1");
      client.click("scope_button");
      
      client.click("submit_button");
      reqData = spyManager.getCurrentRequest();
      for (Snapshot snapshot: reqData.getSnapshots())
      {
         Map<String, Map<String, String>> customScopes = snapshot.getCustomScopes();
         assertEquals(0, customScopes.size());
      }
      
   }
   
}
