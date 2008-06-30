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
package org.jboss.jsfunit.test.richfaces;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.jsfunit.richfaces.RichFacesClient;
import org.xml.sax.SAXException;

/**
 * Peform JSFUnit tests on RichFaces demo application.
 *
 * @author Stan Silvert
 */
public class RichCalendarTest extends ServletTestCase
{
   public void testCalendar() throws IOException, SAXException
   {
      JSFSession jsfSession = new JSFSession("/richfaces/calendar.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      RichFacesClient ajaxClient = new RichFacesClient(client);
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.click("pattern4"); // "MMM d, yyyy"
      ajaxClient.setCalendarValue("myCalendar", "Oct 31, 2007");
      
      client.click("german");
      client.click("pattern4"); // "MMM d, yyyy"
      
      Date date = (Date)server.getManagedBeanValue("#{calendarBean.selectedDate}");
      String pattern = (String)server.getManagedBeanValue("#{calendarBean.pattern}");
      Locale locale = (Locale)server.getManagedBeanValue("#{calendarBean.locale}");
      assertEquals("DE", locale.getCountry());
      SimpleDateFormat formatter = new SimpleDateFormat(pattern, locale);
      String dateString = formatter.format(date);
      assertEquals("Okt 31, 2007", dateString);
   }
   
   public static Test suite()
   {
      return new TestSuite( RichCalendarTest.class );
   }
}
