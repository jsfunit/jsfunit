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

package org.jboss.jsfunit.framework;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.jboss.jsfunit.context.JSFUnitFacesContext;
import org.jboss.jsfunit.seam.SeamUtil;
import org.jboss.seam.contexts.ServletLifecycle;

/**
 * The WebConversationFactory manages the environment needed to set up the WebClientSpec
 * to be used with an HtmlUnit WebClient.
 *
 * @author Stan Silvert
 * @since 1.0
 */
public class WebConversationFactory
{
   public static String JSF_UNIT_CONVERSATION_FLAG = WebConversationFactory.class.getName() + ".testing_flag";
   public static String WAR_URL = WebConversationFactory.class.getName() + ".WARURL";
   
   private static ThreadLocal tlsession = new ThreadLocal()
   {
      protected Object initialValue()
      {
         return null;
      }
   };
   
   private static ThreadLocal warURL = new ThreadLocal()
   {
      protected Object initialValue()
      {
         return null;
      }
   };
   
   /**
    * Associates the HttpSession and WAR URL with ThreadLocals.
    * 
    * @param req The request that kicks off the tests.
    */
   public static void setThreadLocals(HttpServletRequest req)
   {
      tlsession.set(req.getSession());
      String stringWARURL = makeWARURL(req);
      warURL.set(stringWARURL);
   }
   
   /**
    * Get the shared HttpSession from the ThreadLocal variable.
    * 
    * @return The shared HttpSession.
    */
   public static HttpSession getSessionFromThreadLocal()
   {
      return (HttpSession)tlsession.get();
   }
   
   /**
    * Returns the complete WAR URL including everything excluding the query params.
    * 
    * @param req The request that kicks off the tests.
    * @return The WAR URL as a String.
    */
   public static String makeWARURL(HttpServletRequest req)
   {
     return req.getScheme() + "://" + req.getServerName() + ":" + 
            req.getServerPort() + req.getContextPath();
   }
   
   /**
    * Clean up the ThreadLocal variables set in setThreadLocals.
    */
   public static void removeThreadLocals()
   {
      tlsession.remove();
      warURL.remove();
   }
   
   /**
    * Don't allow a new instance of WebConversationFactory
    */
   private WebConversationFactory()
   {
   }
   
   /**
    * Package private method to initialize the wcSpec with a WebClient,
    * clear the sessioin, and add JSFUnit cookies to every request.
    *
    * @param wcSpec The Web Client specification.
    */
   static void makeWebClient(WebClientSpec wcSpec)
   {
      WebClient wc = null;
      String proxyHost = wcSpec.getProxyHost();
      if (proxyHost != null) wc = new WebClient(wcSpec.getBrowserVersion(), 
                                                proxyHost, 
                                                wcSpec.getProxyPort());
      if (proxyHost == null) wc = new WebClient(wcSpec.getBrowserVersion());
      wc.setAjaxController(new NicelyResynchronizingAjaxController());
      wc.setWebConnection(new JSFUnitWebConnection(wc.getWebConnection()));
      wcSpec.setWebClient(wc);
      
      HttpSession session = getSessionFromThreadLocal();
      
      if (session == null)
      {
         throw new IllegalStateException("Can not find HttpSession.  Perhaps JSFUnitFilter has not run?");
      }
      
      clearSession(session);
      session.setAttribute(WebClientSpec.SESSION_KEY, wcSpec);
      
      wcSpec.addCookie("JSESSIONID", session.getId());
      wcSpec.addCookie(JSF_UNIT_CONVERSATION_FLAG, JSF_UNIT_CONVERSATION_FLAG);
   }
   
   /**
    * Clear the HttpSession for use by the WebClientSpec and JSFSession.  We 
    * need to start with a clean (but not new) HttpSession at the beginning of each
    * test.
    * 
    * @param session The shared HttpSession
    */
   protected static void clearSession(HttpSession session)
   {
      for (Enumeration e = session.getAttributeNames(); e.hasMoreElements();)
      {
         String attrName = (String)e.nextElement();
         if (!attrName.equals(JSFUnitFilter.REDIRECTOR_REQUEST_PARAMS_KEY))
         {
            session.removeAttribute(attrName);
         }
      }
      
      JSFUnitFacesContext.cleanUpOldFacesContext();
      
      // start Seam session so @Startup beans in session scope will be initialized
      if (SeamUtil.isSeamInitialized())
      {
         ServletLifecycle.beginSession(session);
      }
   }
   
   /**
    * Get the base URL for this web app.  This will return a String in
    * the form scheme://servername:port/contextpath.
    *
    * @return The base URL for this web app.
    */
   public static String getWARURL()
   {
      return (String)warURL.get();
   }
   
}