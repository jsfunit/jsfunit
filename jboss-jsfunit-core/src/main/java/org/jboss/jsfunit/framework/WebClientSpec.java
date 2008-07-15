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

package org.jboss.jsfunit.framework;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebWindow;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * The WebClientSpec allows configuration of the HtmlUnit WebClient and its
 * interaction with JSFUnit.
 *
 * @author Stan Silvert
 */
public class WebClientSpec implements HttpSessionBindingListener
{
   public static final String SESSION_KEY = WebClientSpec.class.getName() + "sessionkey";
   
   // IE6 is the HtmlUnit default
   private static BrowserVersion DEFAULT_BROWSER_VERSION = BrowserVersion.getDefault();
   
   private String initialPage;
   private WebClient webClient;
   private BrowserVersion browserVersion;
   private String proxyHost;
   private int proxyPort;
   private Map<String, String> cookies = new HashMap<String, String>();
   private InitialRequestStrategy requestStrategy = new SimpleInitialRequestStrategy();
   
   private boolean initialRequestDone = false;
   
   /**
    * Create a new WebClientSpec.  
    *
    * Note that the initialPage param should be something that maps into the FacesServlet.
    * In the case where the FacesServlet is extension mapped in web.xml, this param will be something
    * like "/index.jsf" or "/index.faces".  If the FacesServlet is path-mapped then the
    * initialPage param will be something like "/faces/index.jsp".
    * 
    * @param initialPage The page used to start a client session with JSF.  Example: "/index.jsf"
    */
   public WebClientSpec(String initialPage)
   {
      this(initialPage, DEFAULT_BROWSER_VERSION);
   }
   
   /**
    * Create a new WebClientSpec.  
    *
    * Note that the initialPage param should be something that maps into the FacesServlet.
    * In the case where the FacesServlet is extension mapped in web.xml, this param will be something
    * like "/index.jsf" or "/index.faces".  If the FacesServlet is path-mapped then the
    * initialPage param will be something like "/faces/index.jsp".
    * 
    * @param initialPage The page used to start a client session with JSF.  Example: "/index.jsf"
    * @param browserVersion The browser version to simulate.
    */
   public WebClientSpec(String initialPage, BrowserVersion browserVersion)
   {
      this(initialPage, browserVersion, null, 0);
   }
   
   /**
    * Create a new WebClientSpec for use with a proxy server.  
    *
    * Note that the initialPage param should be something that maps into the FacesServlet.
    * In the case where the FacesServlet is extension mapped in web.xml, this param will be something
    * like "/index.jsf" or "/index.faces".  If the FacesServlet is path-mapped then the
    * initialPage param will be something like "/faces/index.jsp".
    * 
    * @param initialPage The page used to start a client session with JSF.  Example: "/index.jsf"
    * @param browserVersion The browser version to simulate.
    * @param proxyHost The proxy server.
    * @param proxyPort The proxy port.
    */
   public WebClientSpec(String initialPage, BrowserVersion browserVersion, String proxyHost, int proxyPort)
   {
      this.initialPage = initialPage;
      this.browserVersion = browserVersion;
      this.proxyHost = proxyHost;
      this.proxyPort = proxyPort;
      WebConversationFactory.makeWebClient(this);
   }
   
   /**
    * Return the initialPage passed into the constructor.
    *
    * @return The initialPage.
    */
   public String getInitialPage()
   {
      return this.initialPage;
   }
   
   /**
    * Package-private method to set the WebClient to be used for the JSFSession.
    */
   void setWebClient(WebClient webClient)
   {
      this.webClient = webClient;
   }
   
   /**
    * Get the WebClient instances used for the JSFSession.
    *
    * @return The WebClient.
    */
   public WebClient getWebClient()
   {
      return this.webClient;
   }
   
   /**
    * Get the BrowserVersion to be used by the WebClient.
    *
    * @return The BrowserVersion.
    */
   public BrowserVersion getBrowserVersion()
   {
      return this.browserVersion;
   }
   
   /**
    * Get the Proxy Host used by the WebClient.
    *
    * @return the Proxy Host
    */
   public String getProxyHost()
   {
      return this.proxyHost;
   }
   
   /**
    * Get the Proxy Port used by the WebClient.
    *
    * @return The Proxy Port
    */
   public int getProxyPort()
   {
      return this.proxyPort;
   }
   
   /**
    * Set the strategy to be used when making the initial request to the server.
    *
    * @param requestStrategy The InitialRequestStrategy implementation.
    */
   public void setInitialRequestStrategy(InitialRequestStrategy requestStrategy)
   {
      this.requestStrategy = requestStrategy;
   }
   
   /**
    * Add a cookie that will be sent with every request.
    *
    * @param name The cookie name.
    * @param value The cookie value.
    */
   public void addCookie(String name, String value)
   {
      this.cookies.put(name, value);
      addCookiesToHeader();
   }
   
   /**
    * Remove a cookie sent with every request.
    *
    * @param name The name of the cookie
    *
    * @return The value of the cookie removed.
    */
   public String removeCookie(String name)
   {
      String value = this.cookies.remove(name);
      addCookiesToHeader();
      return value;
   }
   
   /**
    * Get an unmodifiable Map of all the cookies to be sent with each request.
    *
    * @return The cookie Map.
    */
   public Map<String, String> getCookies()
   {
      return Collections.unmodifiableMap(this.cookies);
   }
   
   /**
    * Perform the initial request to the server.  This is typically only called
    * by the JSFSession.
    *
    * @return The Page created when the initial request is done.
    *
    * @throws IOException if HtmlUnit encountered an error.
    */
   public Page doInitialRequest() throws IOException
   {
      if (this.initialRequestDone) throw new IllegalStateException("Initial request was already made.");
      addCookiesToHeader();
      Page page = this.requestStrategy.doInitialRequest(this);
      this.initialRequestDone = true;
      return page;
   }
   
   protected void addCookiesToHeader()
   {
      StringBuilder builder = new StringBuilder();
      Map<String, String> cookies = getCookies();
      for (Iterator<String> i = cookies.keySet().iterator(); i.hasNext();)
      {
         String key = i.next();
         String value = cookies.get(key);
         builder.append(key).append("=").append(value);
         if (i.hasNext()) builder.append(";");
      }
      
      getWebClient().addRequestHeader("Cookie", builder.toString());
   }

   // -------- implementation of HttpSessionBindingListener -----------
   public void valueUnbound(HttpSessionBindingEvent httpSessionBindingEvent)
   {
	   // This is the recommended cleanup code ("close the browser windows")
	   // as per HtmlUnit issue:
	   // https://sourceforge.net/tracker/?func=detail&atid=448266&aid=2014629&group_id=47038
	   // -----------------------------------------------------------------------------------

	   // first get the top windows and then close them to avoid ConcurrentModificationException
	   final List<TopLevelWindow> topWindows = new ArrayList<TopLevelWindow>();
	   for (final Iterator<WebWindow> iter=webClient.getWebWindows().iterator();iter.hasNext();)
	   {
		   final WebWindow window = iter.next();
		   if (window instanceof TopLevelWindow)
		   {
			   topWindows.add((TopLevelWindow)window);
		   }
	   }
	   for (final Iterator<TopLevelWindow> iter=topWindows.iterator(); iter.hasNext();)
	   {
		   final TopLevelWindow window = iter.next();
		   window.close();
	   }

   }

   public void valueBound(HttpSessionBindingEvent httpSessionBindingEvent)
   {
   }
}
