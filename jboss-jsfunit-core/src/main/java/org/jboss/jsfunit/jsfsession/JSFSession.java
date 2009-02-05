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

package org.jboss.jsfunit.jsfsession;

import com.gargoylesoftware.htmlunit.WebClient;
import java.io.IOException;
import org.jboss.jsfunit.framework.JSFUnitWebConnection;
import org.jboss.jsfunit.framework.WebClientSpec;

/**
 * This class starts and manages the JSF Session on both the client and server side.
 *
 * @author Stan Silvert
 * @since 1.0
 */
public class JSFSession
{
   private JSFServerSession jsfServerSession;
   private JSFClientSession jsfClientSession;
   private WebClient webClient;
   
   /**
    * Creates a new session for testing the JSF application.   
    * This constructor will also clear the HttpSession.
    * 
    * Note that the initialPage param should be something that maps into the FacesServlet.
    * In the case where the FacesServlet is extension mapped in web.xml, this param will be something
    * like "/index.jsf" or "/index.faces".  If the FacesServlet is path-mapped then the
    * initialPage param will be something like "/faces/index.jsp".
    * 
    * @param initialPage The page used to start a client session with JSF.  Example: "/index.jsf"
    *
    * @throws IOException If there is an error calling the JSF app
    */
   public JSFSession(String initialPage) throws IOException
   {
      this(new WebClientSpec(initialPage));
   }
   
   /**
    * Creates a new session for testing the JSF application.   
    * This constructor will also clear the HttpSession.
    * 
    * Note that the initialPage param should be something that maps into the FacesServlet.
    * In the case where the FacesServlet is extension mapped in web.xml, this param will be something
    * like "/index.jsf" or "/index.faces".  If the FacesServlet is path-mapped then the
    * initialPage param will be something like "/faces/index.jsp".
    * 
    * @param webClient An HtmlUnit WebClient containing custom attributes.  Note that this WebClient
    *                  instance should be created with the JSFUnit WebConversationFactory instead of
    *                  the WebClient constructor.
    * @param initialPage The page used to start a client session with JSF.  Example: "/index.jsf"
    *
    * @throws IOException If there is an error calling the JSF app
    */
   public JSFSession(WebClientSpec wcSpec) throws IOException
   {
      this.webClient = wcSpec.getWebClient();
      
      wcSpec.doInitialRequest();
  
      this.jsfServerSession = new JSFServerSession();
      this.jsfClientSession = new JSFClientSession(webClient, jsfServerSession);
      
      JSFUnitWebConnection webConnection = (JSFUnitWebConnection)this.webClient.getWebConnection();
      webConnection.addListener(this.jsfServerSession);
      
      if (HtmlUnitSnooper.enabled()) webConnection.addListener(new HtmlUnitSnooper());
   }
   
   /**
    * Get the WebClient instance used to control client side requests.
    *
    * @return The WebClient instance used to control client side requests.
    */
   public WebClient getWebClient()
   {
      return this.webClient;
   }
   
   /**
    * Get the JSFServerSession instance used to access server-side JSF artifacts.
    *
    * @return The JSFServerSession
    */
   public JSFServerSession getJSFServerSession()
   {
      return this.jsfServerSession;
   }
   
   /**
    * Get the JSFClientSession instance used to access client-side JSF artifacts
    * send requests to the server.
    *
    * @param The JSFClientSession
    */
   public JSFClientSession getJSFClientSession()
   {
      return this.jsfClientSession;
   }

}
