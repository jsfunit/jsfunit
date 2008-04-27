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
package org.jboss.jsfunit.example.auth.basic;

import com.meterware.httpunit.WebConversation;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.facade.JSFClientSession;
import org.jboss.jsfunit.facade.JSFServerSession;
import org.jboss.jsfunit.framework.FacesContextBridge;
import org.jboss.jsfunit.framework.WebConversationFactory;
import org.xml.sax.SAXException;

import com.meterware.httpunit.WebForm;

public class BasicAuthTest extends ServletTestCase
{
        private JSFClientSession client;
        
        public void setUp() throws MalformedURLException, IOException, SAXException
        {
           WebConversation webConv = WebConversationFactory.makeWebConversation();
           webConv.setAuthorization("admin", "adminpw");
           this.client = new JSFClientSession (webConv, "/secured-page.faces");
        }
	    
        /**
         * Test that a secured page can be accessed using basic authentication
         * @throws SAXException 
         * @throws IOException 
         *
         */
        public void testLogin () throws SAXException, IOException
        {
            JSFServerSession server = new JSFServerSession (client);
        
            FacesContext facesContext = FacesContextBridge.getCurrentInstance();
            UIViewRoot root = facesContext.getViewRoot();
            assertEquals ("/secured-page.jsp", root.getViewId());
            
            UIComponent securedGreeting = root.findComponent("secured_page_greeting");
            assertEquals("Welcome to the Secured Application Page", ((ValueHolder)securedGreeting).getValue());
        }
}
