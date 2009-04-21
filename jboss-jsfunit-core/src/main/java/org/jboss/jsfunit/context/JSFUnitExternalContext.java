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
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.Flash;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.jboss.jsfunit.framework.Environment;

/**
 * The JSFUnitExternalContext is created at the end of the JSF lifecycle.  It
 * caches as much as possible from the "real" ExternalContext.  
 * 
 * Because the Servlet container is allowed to recycle request and response 
 * objects that the ExternalContext relies upon, a few methods could yield 
 * unexpected results.  These methods are noted in the javadoc.
 *
 * @author Stan Silvert
 * @since 1.0
 */
public class JSFUnitExternalContext extends ExternalContext
{
   private ExternalContext delegate;
   private JSFUnitHttpServletRequest httpServletRequest;
   
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
   private int requestContentLength;
   
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
      this.applicationMap = new NoNewEntryMap(delegate.getApplicationMap());
      this.requestHeaderMap = new HashMap(delegate.getRequestHeaderMap());
      this.requestHeaderValuesMap = new HashMap(delegate.getRequestHeaderValuesMap());
      this.locale = delegate.getRequestLocale();
      
      this.locales = new ArrayList();
      for (Iterator i = delegate.getRequestLocales(); i.hasNext();) 
      {
         this.locales.add(i.next());
      }
      
      this.requestMap = new NoNewEntryMap(delegate.getRequestMap());
      this.requestParameterMap = new HashMap(delegate.getRequestParameterMap());
      this.requestParameterValuesMap = new HashMap(delegate.getRequestParameterValuesMap());
      this.requestPathInfo = delegate.getRequestPathInfo();
      this.requestServletPath = delegate.getRequestServletPath();
      this.session = new JSFUnitHttpSession((HttpSession)delegate.getSession(true));
      this.sessionMap = new NoNewEntryMap(delegate.getSessionMap());
      this.userPrincipal = delegate.getUserPrincipal();
      
      if (Environment.is20Compatible())
      {
         this.requestContentLength = delegate.getRequestContentLength();
      }
      
      this.httpServletRequest = new JSFUnitHttpServletRequest(this, (HttpServletRequest)delegate.getRequest());
      
   }
   
   @Override
   public Map getRequestCookieMap()
   {
      return this.cookieMap;
   }

   @Override
   public String getRequestContextPath()
   {
      return this.requestContextPath;
   }

   @Override
   public String getRemoteUser()
   {
      return this.remoteUser;
   }

   @Override
   public String getInitParameter(String string)
   {
      return (String)this.initParameterMap.get(string);
   }
   
   @Override
   public Map getInitParameterMap()
   {
      return this.initParameterMap;
   }

   @Override
   public Object getContext()
   {
      return this.context;
   }

   @Override
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
   @Override
   public Map getApplicationMap()
   {
      return this.applicationMap;
   }

   @Override
   public Map getRequestHeaderMap()
   {
      return this.requestHeaderMap;
   }

   @Override
   public Map getRequestHeaderValuesMap()
   {
      return this.requestHeaderValuesMap;
   }

   @Override
   public Locale getRequestLocale()
   {
      return this.locale;
   }

   @Override
   public Iterator getRequestLocales()
   {
      return this.locales.iterator();
   }

   @Override
   public Map getRequestMap()
   {
      return this.requestMap;
   }

   @Override
   public Map getRequestParameterMap()
   {
      return this.requestParameterMap;
   }

   @Override
   public Iterator getRequestParameterNames()
   {
      return this.requestParameterMap.keySet().iterator();
   }

   @Override
   public Map getRequestParameterValuesMap()
   {
      return this.requestParameterValuesMap;
   }

   @Override
   public String getRequestPathInfo()
   {
      return this.requestPathInfo;
   }

   @Override
   public String getRequestServletPath()
   {
      return this.requestServletPath;
   }
   
   @Override
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
   @Override
   public Map getSessionMap()
   {
      return this.sessionMap;
   }
   
   @Override
   public Principal getUserPrincipal()
   {
      return this.userPrincipal;
   }
   
   @Override
   public URL getResource(String string) throws MalformedURLException
   {
      return this.context.getResource(string);
   }

   @Override
   public InputStream getResourceAsStream(String string)
   {
      return this.context.getResourceAsStream(string);
   }
   
   @Override
   public Set getResourcePaths(String string)
   {
      return this.context.getResourcePaths(string);
   }
   
   @Override
   public void log(String string, Throwable throwable)
   {
      this.context.log(string, throwable);
   }

   @Override
   public void log(String string)
   {
      this.context.log(string);
   }
   
   @Override
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
   @Override
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
   @Override
   public String encodeActionURL(String url)
   {
      if (url == null) throw new NullPointerException("url can not be null.");
      return url;
   }
   
   //----------------- JSF 2.0 Methods --------------------------------------------
   /**
    * Warning: Calling this method from a JSFUnit test could yield unexpected results.
    */
   @Override
   public void addResponseCookie(String name, String value, Map<String, Object> properties)
   {
      delegate.addResponseCookie(name, value, properties);
   }

   @Override
   public String getMimeType(String file)
   {
      return context.getMimeType(file);
   }

   @Override
   public String getRealPath(String path)
   {
      return context.getRealPath(path);
   }

   @Override
   public String getRequestCharacterEncoding()
   {
      return httpServletRequest.getCharacterEncoding();
   }

   @Override
   public String getRequestContentType()
   {
      return httpServletRequest.getContentType();
   }

   @Override
   public String getRequestScheme()
   {
      return httpServletRequest.getScheme();
   }

   @Override
   public String getRequestServerName()
   {
      return httpServletRequest.getServerName();
   }

   @Override
   public int getRequestServerPort()
   {
      return httpServletRequest.getServerPort();
   }

   /**
    * Warning: Calling this method from a JSFUnit test could yield unexpected results.
    */
   @Override
   public String getResponseCharacterEncoding()
   {
      return delegate.getResponseCharacterEncoding();
   }

   /**
    * Warning: Calling this method from a JSFUnit test could yield unexpected results.
    */
   @Override
   public String getResponseContentType()
   {
      return delegate.getResponseContentType();
   }

   /**
    * Warning: Calling this method from a JSFUnit test could yield unexpected results.
    */
   @Override
   public OutputStream getResponseOutputStream() throws IOException
   {
      return delegate.getResponseOutputStream();
   }

   @Override
   public void invalidateSession()
   {
      ((JSFUnitHttpSession)session).invalidate();
   }

   /**
    * Warning: Calling this method from a JSFUnit test could yield unexpected results.
    */
   @Override
   public void setRequest(Object request)
   {
      delegate.setRequest(request);
   }

   /**
    * Warning: Calling this method from a JSFUnit test could yield unexpected results.
    */
   @Override
   public void setRequestCharacterEncoding(String encoding) throws UnsupportedEncodingException
   {
      delegate.setRequestCharacterEncoding(encoding);
   }

   /**
    * Warning: Calling this method from a JSFUnit test could yield unexpected results.
    */
   @Override
   public void setResponse(Object response)
   {
      delegate.setResponse(response);
   }

   /**
    * Warning: Calling this method from a JSFUnit test could yield unexpected results.
    */
   @Override
   public void setResponseCharacterEncoding(String encoding)
   {
      delegate.setResponseCharacterEncoding(encoding);
   }

   /**
    * Warning: Calling this method from a JSFUnit test could yield unexpected results.
    */
   @Override
   public void setResponseContentType(String contentType)
   {
      delegate.setResponseContentType(contentType);
   }

   @Override
   public Flash getFlash()
   {
      return delegate.getFlash();
   }

   @Override
   public void addResponseHeader(String name, String value)
   {
      delegate.addResponseHeader(name, value);
   }

   @Override
   public String encodeBookmarkableURL(String baseUrl, Map<String, List<String>> parameters)
   {
      return delegate.encodeBookmarkableURL(baseUrl, parameters);
   }

   @Override
   public String encodePartialActionURL(String url)
   {
      return delegate.encodePartialActionURL(url);
   }

   @Override
   public String encodeRedirectURL(String baseUrl, Map<String, List<String>> parameters)
   {
      return delegate.encodeRedirectURL(baseUrl, parameters);
   }

   @Override
   public String getContextName()
   {
      return delegate.getContextName();
   }

   @Override
   public int getRequestContentLength()
   {
      return delegate.getRequestContentLength();
   }

   @Override
   public int getResponseBufferSize()
   {
      return delegate.getResponseBufferSize();
   }

   @Override
   public Writer getResponseOutputWriter() throws IOException
   {
      return delegate.getResponseOutputWriter();
   }

   @Override
   public boolean isResponseCommitted()
   {
      return delegate.isResponseCommitted();
   }

   @Override
   public void responseFlushBuffer() throws IOException
   {
      delegate.responseFlushBuffer();
   }

   @Override
   public void responseReset()
   {
      delegate.responseReset();
   }

   @Override
   public void responseSendError(int statusCode, String message) throws IOException
   {
      delegate.responseSendError(statusCode, message);
   }

   @Override
   public void setResponseBufferSize(int size)
   {
      delegate.setResponseBufferSize(size);
   }

   @Override
   public void setResponseContentLength(int length)
   {
      delegate.setResponseContentLength(length);
   }

   @Override
   public void setResponseHeader(String name, String value)
   {
      delegate.setResponseHeader(name, value);
   }

   @Override
   public void setResponseStatus(int statusCode)
   {
      delegate.setResponseStatus(statusCode);
   }
   
   
//----------------- End JSF 2.0  Methods --------------------------------------------
   
// ----- Methods that rely on HttpRequest or HttpResponse: These objects may
// ----- have been recycled/reclaimed by the servlet container.
   /**
    * Warning: Calling this method from a JSFUnit test could yield unexpected results.
    */
   @Override
   public boolean isUserInRole(String string)
   {
      return delegate.isUserInRole(string);
   }

   /**
    * Warning: Calling this method from a JSFUnit test could yield unexpected results.
    */
   @Override
   public Object getResponse()
   {
      return delegate.getResponse();
   }
   
   /**
    * Warning: Calling this method from a JSFUnit test could yield unexpected results.
    */
   @Override
   public Object getRequest()
   {
      return this.httpServletRequest;
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
   @Override
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
   @Override
   public void redirect(String string) throws IOException
   {
      throw new UnsupportedOperationException("Redirect not allowed after request is complete");
   }
}
