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

package org.jboss.jsfunit.context;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;

/**
 * The JSFUnitExternalContext is created at the end of the JSF lifecycle.  It
 * caches as much as possible from the "real" ExternalContext.  
 * 
 * Because the Servlet container is allowed to recycle request and response 
 * objects that the ExternalContext relies upon, a few methods could yield 
 * unexpected results.  These methods are noted in the javadoc.
 *
 * @author Stan Silvert
 */
public class JSFUnitExternalContext extends ExternalContext
{
   private ExternalContext delegate;
   
   private Map cookieMap;
   private String requestContextPath;
   private String remoteUser;
   private Map initParameterMap;
   private ServletContext context;
   private String authType;
   private Map applicationMap;
   private Map requestHeaderMap;
   private Map requestHeaderValuesMap;
   private Locale locale;
   private List locales;
   private Map requestMap;
   private Map requestParameterMap;
   private Map requestParameterValuesMap;
   private String requestPathInfo;
   private String requestServletPath;
   private Object session;
   private Map sessionMap;
   private Principal userPrincipal;
   
   public JSFUnitExternalContext(ExternalContext delegate)
   {
      this.delegate = delegate;
      
      // cache most of the data from the "real" ExternalContext
      this.cookieMap = new HashMap(delegate.getRequestCookieMap());
      this.requestContextPath = delegate.getRequestContextPath();
      this.remoteUser = delegate.getRemoteUser();
      this.initParameterMap = new HashMap(delegate.getInitParameterMap());
      this.context = (ServletContext)delegate.getContext();
      this.authType = delegate.getAuthType();
      this.applicationMap = new HashMap(delegate.getApplicationMap());
      this.requestHeaderMap = new HashMap(delegate.getRequestHeaderMap());
      this.requestHeaderValuesMap = new HashMap(delegate.getRequestHeaderValuesMap());
      this.locale = delegate.getRequestLocale();
      
      this.locales = new ArrayList();
      for (Iterator i = delegate.getRequestLocales(); i.hasNext();) 
      {
         this.locales.add(i.next());
      }
      
      this.requestMap = new HashMap(delegate.getRequestMap());
      this.requestParameterMap = new HashMap(delegate.getRequestParameterMap());
      this.requestParameterValuesMap = new HashMap(delegate.getRequestParameterValuesMap());
      this.requestPathInfo = delegate.getRequestPathInfo();
      this.requestServletPath = delegate.getRequestServletPath();
      this.session = delegate.getSession(true);
      this.sessionMap = new HashMap(delegate.getSessionMap());
      this.userPrincipal = delegate.getUserPrincipal();
   }
   
   public Map getRequestCookieMap()
   {
      return this.cookieMap;
   }

   public String getRequestContextPath()
   {
      return this.requestContextPath;
   }

   public String getRemoteUser()
   {
      return this.remoteUser;
   }

   public String getInitParameter(String string)
   {
      return (String)this.initParameterMap.get(string);
   }
   
   public Map getInitParameterMap()
   {
      return this.initParameterMap;
   }

   public Object getContext()
   {
      return this.context;
   }

   public String getAuthType()
   {
      return this.authType;
   }

   /**
    * Warning: The write-through capabilities of this Map are disabled for
    * JSFUnit tests.  You can still modify the Application attributes 
    * through the ServletContext.  It is not recommended to do this in
    * a JSFUnit test.  Ideally, JSFUnit tests should only examine the state of 
    * the system, not change it.<br/>
    * But if you must, here is how to do it:<br/>
    * <code>
    * HttpSession session = (HttpSession)externalContext.getSession();<br/>
    * ServletContext appContext = session.getServletContext();<br/>
    * appContext.setAttribute("documentsByPath", "bar");
    * </code>
    */
   public Map getApplicationMap()
   {
      return this.applicationMap;
   }

   public Map getRequestHeaderMap()
   {
      return this.requestHeaderMap;
   }

   public Map getRequestHeaderValuesMap()
   {
      return this.requestHeaderValuesMap;
   }

   public Locale getRequestLocale()
   {
      return this.locale;
   }

   public Iterator getRequestLocales()
   {
      return this.locales.iterator();
   }

   public Map getRequestMap()
   {
      return this.requestMap;
   }

   public Map getRequestParameterMap()
   {
      return this.requestParameterMap;
   }

   public Iterator getRequestParameterNames()
   {
      return this.requestParameterMap.keySet().iterator();
   }

   public Map getRequestParameterValuesMap()
   {
      return this.requestParameterValuesMap;
   }

   public String getRequestPathInfo()
   {
      return this.requestPathInfo;
   }

   public String getRequestServletPath()
   {
      return this.requestServletPath;
   }
   
   public Object getSession(boolean b)
   {
      return this.session;
   }

   /**
    * Warning: The write-through capabilities of this Map are disabled for
    * JSFUnit tests.  In other words, modifications to the Map do not actually 
    * affect the HttpSession. You can still modify the Session attributes 
    * using the 'live' HttpSession obtained from the getSession() method.  
    * It is not recommended to do this in a JSFUnit test.  Ideally, JSFUnit
    * tests should only examine the state of the system, not change it.<br/>
    * But if you must, here is how to do it:<br/>
    * <code>
    * HttpSession session = (HttpSession)externalContext.getSession();<br/>
    * session.setAttribute("documentsByPath", "bar");
    * </code>
    */
   public Map getSessionMap()
   {
      return this.sessionMap;
   }
   
   public Principal getUserPrincipal()
   {
      return this.userPrincipal;
   }
   
   public URL getResource(String string) throws MalformedURLException
   {
      return this.context.getResource(string);
   }

   public InputStream getResourceAsStream(String string)
   {
      return this.context.getResourceAsStream(string);
   }
   
   public Set getResourcePaths(String string)
   {
      return this.context.getResourcePaths(string);
   }
   
   public void log(String string, Throwable throwable)
   {
      this.context.log(string, throwable);
   }

   public void log(String string)
   {
      this.context.log(string);
   }
   
   public String encodeNamespace(String string)
   {
      return string;
   }
   
   /**
    * Return the url unchanged.  This is OK in JSFUnit because we know that we 
    * are using cookies for the jsessionid.
    *
    * @param url The url to encode.
    *
    * @return The url unchanged.
    *
    * @throws NullPointerException if the url is null
    */
   public String encodeResourceURL(String url)
   {
      if (url == null) throw new NullPointerException("url can not be null.");
      return url;
   }

   /**
    * Return the url unchanged.  This is OK in JSFUnit because we know that we 
    * are using cookies for the jsessionid.
    *
    * @param url The url to encode.
    *
    * @return The url unchanged.
    *
    * @throws NullPointerException if the url is null
    */
   public String encodeActionURL(String url)
   {
      if (url == null) throw new NullPointerException("url can not be null.");
      return url;
   }
   
// ----- Methods that rely on HttpRequest or HttpResponse: These objects may
// ----- have been recycled/reclaimed by the servlet container.
   /**
    * Warning: This method could yield unexpected results.
    */
   public boolean isUserInRole(String string)
   {
      return delegate.isUserInRole(string);
   }

   /**
    * Warning: This method could yield unexpected results.
    */
   public Object getResponse()
   {
      return delegate.getResponse();
   }
   
   /**
    * Warning: This method could yield unexpected results.
    */
   public Object getRequest()
   {
      return delegate.getRequest();
   }
   
   
// ------------ Unsupported Operations ----------------------------------------------------------
   /**
    * Unsupported method.  Since the JSFUnitExternalContext is not active until 
    * the request is over, it doesn't make sense to do a dispatch by calling 
    * this method in a JSFUnit test.
    *
    * @throws UnsupportedOperationException if this method is called during
    *                                       a JSFUnit test.
    */
   public void dispatch(String string) throws IOException
   {
      throw new UnsupportedOperationException("Dispatch not allowed after request is complete");
   }
   
   /**
    * Unsupported method.  Since the JSFUnitExternalContext is not active until 
    * the request is over, it doesn't make sense to do a dispatch by calling 
    * this method in a JSFUnit test.
    *
    * @throws UnsupportedOperationException if this method is called during
    *                                       a JSFUnit test.
    */
   public void redirect(String string) throws IOException
   {
      throw new UnsupportedOperationException("Redirect not allowed after request is complete");
   }
}
