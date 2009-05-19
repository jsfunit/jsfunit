/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This class takes the same approach as the JSFUnitExternalContext in that it
 * caches everything possible from the HttpServletRequest so that it will be
 * available to the JSFUnit test.  For a few methods, it is impossible or 
 * impractical to provide an implementation because the request is finished
 * and the servlet container is free to recycle the original HttpServletRequest.
 * So a few methods throw UnsupportedOperationException.  A few methods also
 * warn that using them can lead to unexpected results.  Those methods that
 * are unreliable have a warning in the javadoc.
 *
 * @author Stan Silvert
 * @since 1.0
 */
public class JSFUnitHttpServletRequest implements HttpServletRequest
{
   private JSFUnitExternalContext extCtx;
   
   private boolean isServlet14OrGreater = false;
   
   private String localName = "";
   private String localAddr = "";
   private int localPort = 0;
   private int remotePort = 0;
   private String queryString;
   private String protocol;
   private String pathTranslated;
   private String method;
   private Map parameterMap;
   private int contentLength;
   private Cookie[] cookies;
   private String remoteAddr;
   private String remoteHost;
   private String requestURI;
   private StringBuffer requestURL;
   private String requestedSessionId;
   private String scheme;
   private String serverName;
   private int serverPort;
   private boolean isRequestedSessionIdFromCookie;
   private boolean isRequestedSessionIdFromURL;
   private boolean isRequestedSessionIdValid;
   private boolean isSecure;
   
   public JSFUnitHttpServletRequest(JSFUnitExternalContext extCtx, HttpServletRequest request)
   {
      this.extCtx = extCtx;
      this.isServlet14OrGreater = isServlet14OrGreater();
      cacheServlet14RequestValues(request);
      this.queryString = request.getQueryString();
      this.protocol = request.getProtocol();
      this.pathTranslated = request.getPathTranslated();
      this.method = request.getMethod();
      this.parameterMap = request.getParameterMap();
      this.contentLength = request.getContentLength();
      this.cookies = request.getCookies();
      this.remoteAddr = request.getRemoteAddr();
      this.remoteHost = request.getRemoteHost();
      this.requestURI = request.getRequestURI();
      this.requestURL = new StringBuffer(request.getRequestURL());
      this.requestedSessionId = request.getRequestedSessionId();
      this.scheme = request.getScheme();
      this.serverName = request.getServerName();
      this.serverPort = request.getServerPort();
      this.isRequestedSessionIdFromCookie = request.isRequestedSessionIdFromCookie();
      this.isRequestedSessionIdFromURL = request.isRequestedSessionIdFromURL();
      this.isRequestedSessionIdValid = request.isRequestedSessionIdValid();
      this.isSecure = request.isSecure();
   }
   
   private boolean isServlet14OrGreater()
   {
      ServletContext servletContext = (ServletContext)this.extCtx.getContext();
      return servletContext.getMinorVersion() > 3;
   }
   
   private void cacheServlet14RequestValues(HttpServletRequest request)
   {
      if (!this.isServlet14OrGreater) return;
      
      this.localName = request.getLocalName();
      this.localAddr = request.getLocalAddr();
      this.localPort = request.getLocalPort();
      this.remotePort = request.getRemotePort();
   }
   
   @Override
   public String getQueryString()
   {
      return this.queryString;
   }

   @Override
   public String getProtocol()
   {
      return this.protocol;
   }

   @Override
   public String getPathTranslated()
   {
      return this.pathTranslated;
   }

   @Override
   public String getPathInfo()
   {
      return this.extCtx.getRequestPathInfo();
   }

   @Override
   public Enumeration getParameterNames()
   {
      return Collections.enumeration(this.extCtx.getRequestParameterMap().keySet());
   }

   @Override
   public Map getParameterMap()
   {
      return this.parameterMap;
   }

   @Override
   public String getMethod()
   {
      return this.method;
   }

   @Override
   public Enumeration getLocales()
   {
      return makeEnumeration(this.extCtx.getRequestLocales());
   }
   
   private Enumeration makeEnumeration(Iterator iterator)
   {
      Set mySet = new HashSet();
      while (iterator.hasNext())
      {
         mySet.add(iterator.next());
      }
      
      return Collections.enumeration(mySet);
   }

   @Override
   public Locale getLocale()
   {
      return this.extCtx.getRequestLocale();
   }

   @Override
   public Enumeration getAttributeNames()
   {
      return Collections.enumeration(this.extCtx.getRequestMap().keySet());
   }

   @Override
   public String getAuthType()
   {
      return this.extCtx.getAuthType();
   }

   @Override
   public String getCharacterEncoding()
   {
      return this.extCtx.getRequestCharacterEncoding();
   }

   @Override
   public int getContentLength()
   {
      return this.contentLength;
   }

   @Override
   public String getContentType()
   {
      return this.extCtx.getRequestContentType();
   }

   @Override
   public String getContextPath()
   {
      return this.extCtx.getRequestContextPath();
   }

   @Override
   public Cookie[] getCookies()
   {
      return this.cookies;
   }

   @Override
   public Enumeration getHeaderNames()
   {
      return Collections.enumeration(this.extCtx.getRequestHeaderMap().keySet());
   }

   /**
    * @throws UnsupportedOperationException
    */
   @Override
   public ServletInputStream getInputStream() throws IOException
   {
      throw new UnsupportedOperationException();
   }

   /**
    * @throws UnsupportedOperationException
    */
   @Override
   public BufferedReader getReader() throws IOException
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public String getRemoteAddr()
   {
      return this.remoteAddr;
   }

   @Override
   public String getRemoteHost()
   {
      return this.remoteHost;
   }

   @Override
   public String getRemoteUser()
   {
      return this.extCtx.getRemoteUser();
   }

   @Override
   public String getRequestURI()
   {
      return this.requestURI;
   }

   @Override
   public StringBuffer getRequestURL()
   {
      return this.requestURL;
   }

   @Override
   public String getRequestedSessionId()
   {
      return this.requestedSessionId;
   }

   @Override
   public String getScheme()
   {
      return this.scheme;
   }

   @Override
   public String getServerName()
   {
      return this.serverName;
   }

   @Override
   public int getServerPort()
   {
      return this.serverPort;
   }

   @Override
   public String getServletPath()
   {
      return this.extCtx.getRequestServletPath();
   }

   @Override
   public HttpSession getSession()
   {
      return (HttpSession)this.extCtx.getSession(true);
   }

   @Override
   public Principal getUserPrincipal()
   {
      return this.extCtx.getUserPrincipal();
   }

   @Override
   public boolean isRequestedSessionIdFromCookie()
   {
      return this.isRequestedSessionIdFromCookie;
   }

   @Override
   public boolean isRequestedSessionIdFromURL()
   {
      return this.isRequestedSessionIdFromURL;
   }

   @Override
   public boolean isRequestedSessionIdFromUrl()
   {
      return isRequestedSessionIdFromURL;
   }

   @Override
   public boolean isRequestedSessionIdValid()
   {
      return this.isRequestedSessionIdValid;
   }

   @Override
   public boolean isSecure()
   {
      return this.isSecure;
   }

   @Override
   public HttpSession getSession(boolean create)
   {
      return (HttpSession)this.extCtx.getSession(create);
   }

   @Override
   public void setAttribute(String attribute, Object value)
   {
      this.extCtx.getRequestMap().put(attribute, value);
   }

   /**
    * This method is a no-op.  Since the response is over by the time this
    * object is active, it is too late to override the name of the character
    * encoding.
    */
   @Override
   public void setCharacterEncoding(String string) throws UnsupportedEncodingException
   {
      //No-op
   }

   @Override
   public String[] getParameterValues(String name)
   {
      return (String[])this.extCtx.getRequestParameterValuesMap().get(name);
   }

   @Override
   public String getParameter(String name)
   {
      return (String)this.extCtx.getRequestParameterMap().get(name);
   }

   @Override
   public int getIntHeader(String name)
   {
      String header = getHeader(name);
      if (header == null) return -1;
      return Integer.parseInt(header);
   }

   @Override
   public Object getAttribute(String name)
   {
      return this.extCtx.getRequestMap().get(name);
   }

   @Override
   public long getDateHeader(String name)
   {
      String header = getHeader(name);
      if (header == null) return -1;
      return Date.parse(header);
   }

   @Override
   public String getHeader(String name)
   {
      return (String)this.extCtx.getRequestHeaderMap().get(name);
   }

   @Override
   public Enumeration getHeaders(String name)
   {
      String[] headers = (String[])this.extCtx.getRequestHeaderValuesMap().get(name);
      if (headers == null) headers = new String[0];
      return Collections.enumeration(Arrays.asList(headers));
   }

   /**
    * @throws UnsupportedOperationException
    */
   @Override
   public String getRealPath(String string)
   {
      throw new UnsupportedOperationException();
   }

   /**
    * @throws UnsupportedOperationException
    */
   @Override
   public RequestDispatcher getRequestDispatcher(String string)
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Warning: This method could yield unexpected results.
    */
   @Override
   public boolean isUserInRole(String name)
   {
      return this.extCtx.isUserInRole(name);
   }

   @Override
   public void removeAttribute(String name)
   {
      this.extCtx.getRequestMap().remove(name);
   }

   /**
    * @throws UnsupportedOperationException if running in a container that
    *         does not support servlet 2.4 or greater.
    *                                   
    */
   @Override
   public int getLocalPort()
   {
      if (!isServlet14OrGreater) throw new UnsupportedOperationException();
      return this.localPort;
   }
   
   /**
    * @throws UnsupportedOperationException if running in a container that
    *         does not support servlet 2.4 or greater.
    *                                   
    */
   @Override
   public int getRemotePort()
   {
      if (!isServlet14OrGreater) throw new UnsupportedOperationException();
      return this.remotePort;
   }
   
   /**
    * @throws UnsupportedOperationException if running in a container that
    *         does not support servlet 2.4 or greater.
    *                                   
    */
   @Override
   public String getLocalName()
   {
      if (!isServlet14OrGreater) throw new UnsupportedOperationException(); 
      return this.localName;
   }
   
   /**
    * @throws UnsupportedOperationException if running in a container that
    *         does not support servlet 2.4 or greater.
    *                                   
    */
   @Override
   public String getLocalAddr()
   {
      if (!isServlet14OrGreater) throw new UnsupportedOperationException();
      return this.localAddr;
   }
}
