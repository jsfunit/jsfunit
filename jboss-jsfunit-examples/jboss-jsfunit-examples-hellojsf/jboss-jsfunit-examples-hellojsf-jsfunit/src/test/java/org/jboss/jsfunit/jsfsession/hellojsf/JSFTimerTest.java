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

package org.jboss.jsfunit.jsfsession.hellojsf;

import java.io.IOException;
import javax.faces.event.PhaseId;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.framework.Environment;
import org.jboss.jsfunit.framework.JSFTimer;
import org.jboss.jsfunit.framework.WebConversationFactory;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.xml.sax.SAXException;

/**
 * Tests the JSFTimer class.
 *
 * @author Stan Silvert
 */
public class JSFTimerTest extends ServletTestCase
{
   
   public void testSimpleTiming() throws SAXException, IOException
   {
      JSFSession jsfSession = new JSFSession("/index.faces");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFTimer timer = JSFTimer.getTimer();
      assertTrue(timer.getTotalTime() > 0);
   } 
   
   public void testNonFacesRequestGeneratesFacesResponse() throws SAXException, IOException
   {
      if (Environment.is20Compatible()) return; // assertions below can fail under JSF2
      
      JSFSession jsfSession = new JSFSession("/index.faces");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFTimer timer = JSFTimer.getTimer();
      
      assertEquals(0, timer.getPhaseTime(PhaseId.APPLY_REQUEST_VALUES));
      assertEquals(0, timer.getPhaseTime(PhaseId.PROCESS_VALIDATIONS));
      assertEquals(0, timer.getPhaseTime(PhaseId.UPDATE_MODEL_VALUES));
      assertEquals(0, timer.getPhaseTime(PhaseId.INVOKE_APPLICATION));
   }
   
   public void testTotalTime() throws SAXException, IOException
   {
      JSFSession jsfSession = new JSFSession("/index_longValidator.faces");
      JSFClientSession client = jsfSession.getJSFClientSession();
      client.setValue("input_foo_text", "Stan"); 
      client.click("submit_button");
      JSFTimer timer = JSFTimer.getTimer();
      assertTrue(timer.getTotalTime() >= 1500);
   }
   
   public void testClearTimers() throws SAXException, IOException
   {
      JSFSession jsfSession = new JSFSession("/index_longValidator.faces");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFTimer timer = JSFTimer.getTimer();
      
      // ProcessValidations should be skipped for a new session in JSF 1.x
      if (Environment.getJSFMajorVersion() < 2)
      {
         assertEquals(0, timer.getPhaseTime(PhaseId.PROCESS_VALIDATIONS));
      }
      
      client.setValue("input_foo_text", "Stan"); 
      client.click("submit_button");
      timer = JSFTimer.getTimer();
      assertTrue(timer.getPhaseTime(PhaseId.PROCESS_VALIDATIONS) >= 1500);
      
      // Timer must still be cleared when submitting non-faces request
      jsfSession.getWebClient().getPage(WebConversationFactory.getWARURL() + "/indexNoButtons.faces");
      timer = JSFTimer.getTimer();
      assertTrue(timer.getPhaseTime(PhaseId.PROCESS_VALIDATIONS) < 1500);
   }
   
   public void testAnyPhase() throws SAXException, IOException
   {
      JSFSession jsfSession = new JSFSession("/index.faces");
      JSFClientSession client = jsfSession.getJSFClientSession();
      try
      {
         JSFTimer timer = JSFTimer.getTimer();
         timer.getPhaseTime(PhaseId.ANY_PHASE);
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
         // OK
      }
   }
   
   /* debugging code
      System.out.println("Restore View=" + timer.getPhaseTime(PhaseId.RESTORE_VIEW));
      System.out.println("Apply Request Values=" + timer.getPhaseTime(PhaseId.APPLY_REQUEST_VALUES));  
      System.out.println("Process Validations=" + timer.getPhaseTime(PhaseId.PROCESS_VALIDATIONS));
      System.out.println("Update Model Values=" + timer.getPhaseTime(PhaseId.UPDATE_MODEL_VALUES));
      System.out.println("Invoke Application=" + timer.getPhaseTime(PhaseId.INVOKE_APPLICATION));
      System.out.println("Render Response=" + timer.getPhaseTime(PhaseId.RENDER_RESPONSE));
   */
   
}
