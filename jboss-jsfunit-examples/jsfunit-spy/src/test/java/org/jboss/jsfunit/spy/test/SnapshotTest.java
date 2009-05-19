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
import java.util.List;
import javax.faces.event.PhaseId;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.jsfunit.spy.data.BeforeOrAfter;
import org.jboss.jsfunit.spy.data.RequestData;
import org.jboss.jsfunit.spy.data.Snapshot;
import org.jboss.jsfunit.spy.data.SpyManager;

/**
 *
 * @author Stan Silvert
 */
public class SnapshotTest extends ServletTestCase 
{
   JSFServerSession server;
   JSFClientSession client;
   
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
      return new TestSuite( SnapshotTest.class );
   }

   public void testGetValuesFromSnapshot() throws IOException
   {
      client.click("submit_button");
      
      SpyManager spyManager = SpyManager.getInstance();
      RequestData request = spyManager.getCurrentRequest();
      List<Snapshot> snapshots = request.getSnapshots();
      BeforeOrAfter beforeOrAfter = BeforeOrAfter.BEFORE;
      for (Snapshot snapshot : snapshots)
      {
         assertNotNull(snapshot.getPhaseId());
         assertEquals(beforeOrAfter, snapshot.getBeforeOrAfter());
         assertEquals("application scope bean", snapshot.getApplicationScope().get("appscope1"));
         assertEquals("session scope bean", snapshot.getSessionScope().get("sessscope1"));
         
         // Req scope doesn't get applied until after PROCESS_VALIDATIONS
         if (snapshot.getPhaseId().getOrdinal() > PhaseId.PROCESS_VALIDATIONS.getOrdinal())
         {
            assertEquals("request scope bean", snapshot.getRequestScope().get("reqscope1"));
         }
         else
         {
            if (snapshot.getPhaseId() != PhaseId.PROCESS_VALIDATIONS)
            {
               assertNull(snapshot.getRequestScope().get("reqscope1"));
            }
         }
         
         beforeOrAfter = beforeOrAfter.opposite();
      }
   }
   
   public void testChangedValues() throws IOException
   {
      client.setValue("appscope1", "appfoo");
      client.setValue("sessscope1", "sessfoo");
      client.setValue("reqscope1", "reqfoo");
      client.click("submit_button");
      
      SpyManager spyManager = SpyManager.getInstance();
      RequestData request = spyManager.getCurrentRequest();
      List<Snapshot> snapshots = request.getSnapshots();
      
      Snapshot firstSnapshot = snapshots.get(0);
      assertEquals("application scope bean", firstSnapshot.getApplicationScope().get("appscope1"));
      assertEquals("session scope bean", firstSnapshot.getSessionScope().get("sessscope1"));
      assertNull(firstSnapshot.getRequestScope().get("reqscope1"));
      
      Snapshot lastSnapshot = snapshots.get(snapshots.size() - 1);
      assertEquals("appfoo", lastSnapshot.getApplicationScope().get("appscope1"));
      assertEquals("sessfoo", lastSnapshot.getSessionScope().get("sessscope1"));
      assertEquals("reqfoo", lastSnapshot.getRequestScope().get("reqscope1"));
   }
}
