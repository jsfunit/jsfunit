/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.jsfunit.test.richfaces;

import java.io.IOException;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.ajax4jsf.component.UIPoll;
import org.apache.cactus.ServletTestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.jsfunit.richfaces.RichFacesClient;
import org.richfaces.demo.poll.PollBean;

import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;

public class AjaxPollTest extends ServletTestCase {
	// -- Logger
	protected static Log log = LogFactory.getLog(AjaxPollTest.class);

	public static Test suite()
	{
		return new TestSuite( AjaxPollTest.class );
	}

	private static final String _POLL_ID = "poll";
	private static final String _POLLING_INACTIVE_ID = "pollingInactive";
	private static final String _POLLING_ACTIVE_ID = "pollingActive";
	private static final String _BUTTON_ID = "control";
	private static final String _SERVER_DATE_ID = "serverDate";
	
	private static final String _POLLBEAN = "#{pollBean}";
	
	private static final int _EXPECTED_POLLING_INTERVAL = 500; 
	
	
	public void testPoll_OnOffTest() throws InterruptedException, IOException
	{
		JSFSession jsfSession = new JSFSession("/richfaces/poll.jsf");
		JSFClientSession client = jsfSession.getJSFClientSession();
		RichFacesClient ajaxClient = new RichFacesClient(client);
		JSFServerSession server = jsfSession.getJSFServerSession();

		// Get the backing bean to check activity
		PollBean pb = (PollBean)server.getManagedBeanValue(_POLLBEAN);
		assertNotNull("Can't find PollBean",pb);
		// Make sure polling is enabled
		assertTrue("Polling is not enabled in the PollBean",pb.getPollEnabled());
		
		// Get poll control to check status
		UIPoll poll = (UIPoll)server.findComponent(_POLL_ID);
		assertNotNull("Can't find server side poll component",poll);
		int interval = poll.getInterval();
		// Make sure polling is enabled
		assertTrue("Polling is not enabled in the component",poll.isEnabled());
		
		// Make sure the date is updating
		Date startDate = pb.getLasttime();
		Thread.sleep(interval*2);
		Date endDate = pb.getLasttime();
		assertFalse("Date is not being updated in the PollBean",startDate.equals(endDate));
		
		// Get control button and disable polling
		HtmlButtonInput button = (HtmlButtonInput)client.getElement(_BUTTON_ID);
		assertNotNull("Can't find polling control button ["+_BUTTON_ID+"]",button);
		button.click();
		
		// Make sure we're no longer polling
		assertFalse("Polling should be disabled in the PollBean",pb.getPollEnabled());
		assertFalse("Polling should be disabled in the component",poll.isEnabled());
		// Make sure the date is NOT updating
		startDate = pb.getLasttime();
		Thread.sleep(interval*2);
		endDate = pb.getLasttime();
		assertTrue("Date should not be updating in the PollBean",startDate.equals(endDate));
		
		// Turn polling back on
		button.click();
		
		// Make sure we're polling again
		assertTrue("Polling is not enabled in the PollBean",pb.getPollEnabled());
		assertTrue("Polling is not enabled in the component",poll.isEnabled());
		// Make sure the date is updating
		startDate = pb.getLasttime();
		Thread.sleep(interval*2);
		endDate = pb.getLasttime();
		assertFalse("Date is not being updated in the PollBean",startDate.equals(endDate));

	}
	
	public void testPoll_StillRunning() throws InterruptedException, IOException
	{
		JSFSession jsfSession = new JSFSession("/richfaces/inplaceInput.jsf");	// Anything but the Polling page
		JSFClientSession client = jsfSession.getJSFClientSession();
		RichFacesClient ajaxClient = new RichFacesClient(client);
		JSFServerSession server = jsfSession.getJSFServerSession();
		
		// Get the backing bean to check activity
		PollBean pb = (PollBean)server.getManagedBeanValue(_POLLBEAN);
		assertNotNull("Can't find PollBean",pb);
		// Make sure the date is NOT updating
		Date startDate = pb.getLasttime();
		Thread.sleep(_EXPECTED_POLLING_INTERVAL*4);  
		Date endDate = pb.getLasttime();
		assertTrue("Date should not be updating in the PollBean",startDate.equals(endDate));		
	}
}
