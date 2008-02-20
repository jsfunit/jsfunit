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

package org.jboss.jsfunit.seam;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.jboss.jsfunit.facade.JSFClientSession;
import org.jboss.jsfunit.framework.FacesContextBridge;
import org.jboss.seam.contexts.ServletLifecycle;
import org.jboss.seam.core.ConversationPropagation;
import org.jboss.seam.core.Manager;
import org.jboss.seam.servlet.ServletRequestSessionMap;
import org.jboss.seam.web.ServletContexts;
import org.xml.sax.SAXException;

/**
 * This class provides JSFUnit client support for Seam's client-side
 * JSF components.
 *
 * @author Stan Silvert
 */
public class SeamClient extends JSFClientSession
{
   private boolean iPromotedTheConversation = false;
   private boolean iRestoredTheConversation = false;

   /**
    * Creates a new client interface for testing the JSF application.   
    * This will also clear the HttpSession.
    * 
    * Note that the initialPage param should be something that maps into the FacesServlet.
    * In the case where the FacesServlet is extension mapped in web.xml, this param will be something
    * like "/index.jsf" or "/index.faces".  If the FacesServlet is path-mapped then the
    * initialPage param will be something like "/faces/index.jsp".
    * 
    * @param initialPage The page used to start a client session with JSF.  Example: "/index.jsf"
    *
    * @throws MalformedURLException If the initialPage cannot be used to create a URL for the JSF app
    * @throws IOException If there is an error calling the JSF app
    * @throws SAXException If the response from the JSF app cannot be parsed as HTML
    */
   public SeamClient(String initialPage) throws MalformedURLException, IOException, SAXException
   {
      super(initialPage);
   }
   
   /**
    * Creates a new client interface for testing a JSF application using a
    * customized WebConversation.  To use this constructor, first get a
    * WebConversation from org.jboss.jsfunit.framework.WebConversationFactory.
    * <br/><br/>
    * Example:<br/>
    * <code>
    * WebConversation webConv = WebConversationFactory.makeWebConversation();<br/>
    * webConv.setAuthorization("myuser", "mypassword");<br/>
    * webConv.setHeaderField("Accept-Language", "es-mx,es");<br/> 
    * SeamClient client = new SeamClient(webConv, "/index.jsf");<br/>
    * </code>
    * <br/>
    * Note that the initialPage param should be something that maps into the FacesServlet.
    * In the case where the FacesServlet is extension mapped in web.xml, this param will be something
    * like "/index.jsf" or "/index.faces".  If the FacesServlet is path-mapped then the
    * initialPage param will be something like "/faces/index.jsp".
    * 
    * 
    * @param webConversation A WebConversation object with "custom" attributes.
    * @param initialPage The page used to start a client session with JSF.  Example: "/index.jsf"
    * @throws IllegalArgumentException if the WebConversation did not come from the
    *                                  WebConversationFactory.
    * @throws MalformedURLException If the initialPage cannot be used to create a URL for the JSF app
    * @throws IOException If there is an error calling the JSF app
    * @throws SAXException If the response from the JSF app cannot be parsed as HTML
    */
   public SeamClient(WebConversation webConversation, String initialPage) 
        throws MalformedURLException, IOException, SAXException
   {
      super(webConversation, initialPage);
   }
   
   /**
    * Click on a Seam s:link component.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    *
    * @throws SAXException if the current response page can not be parsed
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
   public void clickSLink(String componentID) throws SAXException, IOException
   {
      String clientID = getClientIDs().findClientID(componentID);
      WebLink link = getWebResponse().getLinkWithID(clientID);
      doWebRequest(link.getRequest());
   }
   
   /**
    * The method submits the WebRequest to the server using the WebConversation
    * of this JSFClientSession instance.  
    * 
    * At the end of this method, a new view from the server will be loaded so 
    * that you can continue to use this JSFClientSession instance to make further 
    * requests.
    * 
    * 
    * @param request The WebRequest
    * @throws IOException If there is an error calling the JSF app
    * @throws SAXException If the response from the JSF app cannot be parsed as 
    *                      HTML
    */
   public void doWebRequest(WebRequest request) throws SAXException, IOException
   {
      demoteConversation();
      tearDownConversation();
      super.doWebRequest(request);
      promoteConversation();
      restoreConversation();
   }

   // code copied from org.jboss.seam.servlet.ContextualHttpServletRequest
   private void restoreConversation()
   {
      HttpServletRequest request = httpServletRequest();
      ServletLifecycle.beginRequest(request);
      ServletContexts.instance().setRequest(request);
      ConversationPropagation.instance().restoreConversationId( request.getParameterMap() );
      Manager.instance().restoreConversation();
      ServletLifecycle.resumeConversation(request);
      Manager.instance().handleConversationPropagation( request.getParameterMap() );
      
      this.iRestoredTheConversation = true;
   }
   
   // code copied from org.jboss.seam.servlet.ContextualHttpServletRequest
   private void tearDownConversation()
   {
      if (!this.iRestoredTheConversation) return;
      
      HttpServletRequest request = httpServletRequest();
      Manager.instance().endRequest( new ServletRequestSessionMap(request)  );
      ServletLifecycle.endRequest(request);
      
      this.iRestoredTheConversation = false;
   }
   
   private void demoteConversation()
   {
      if (this.iPromotedTheConversation)
      {
         Manager.instance().setLongRunningConversation(false);
         this.iPromotedTheConversation = false;
      }
   }
   
   private void promoteConversation()
   {
      try
      {
         if (!Manager.instance().isLongRunningConversation())
         {
            Manager.instance().setLongRunningConversation(true);
            this.iPromotedTheConversation = true;
         }
      }
      catch (IllegalStateException e)
      {
         // ignore
      }
   }
   
   private HttpServletRequest httpServletRequest()
   {
      FacesContext facesContext = FacesContextBridge.getCurrentInstance();
      return (HttpServletRequest)facesContext.getExternalContext().getRequest();
   }
}
