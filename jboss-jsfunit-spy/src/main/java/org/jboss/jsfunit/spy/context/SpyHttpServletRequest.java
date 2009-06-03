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

package org.jboss.jsfunit.spy.context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.context.FacesContext;
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
 * @since 1.1
 */
public class SpyHttpServletRequest implements HttpServletRequest
{
   private boolean isServlet14OrGreater = false;
   
   private String localName = "";
   private String localAddr = "";
   private int localPort = 0;
   private int remotePort = 0;
   private String queryString;
   private String protocol;
   private String pathInfo;
   private String pathTranslated;
   private String method;
   private Map<String, String[]> parameterMap;
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
   private List<Locale> requestLocales;
   private Locale locale;
   private boolean isSecure;
   private String authType;
   private String characterEncoding;
   private String contentType;
   private String contextPath;
   private List headerNames;
   private String remoteUser;
   private String servletPath;
   private Principal userPrincipal = null;
   private Map<String, String[]> headerMap;
   
   public SpyHttpServletRequest(HttpServletRequest request)
   {
      this.isServlet14OrGreater = isServlet14OrGreater();
      cacheServlet14RequestValues(request);
      this.queryString = request.getQueryString();
      this.protocol = request.getProtocol();
      this.pathInfo = request.getPathInfo();
      this.pathTranslated = request.getPathTranslated();
      this.method = request.getMethod();
      this.parameterMap = (Map<String, String[]>)Collections.unmodifiableMap(new HashMap(request.getParameterMap()));
      this.contentLength = request.getContentLength();
      this.cookies = cloneCookies(request);
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
      this.requestLocales = (List<Locale>)Collections.list(request.getLocales());
      this.locale = request.getLocale();
      this.isSecure = request.isSecure();
      this.authType = request.getAuthType();
      this.characterEncoding = request.getCharacterEncoding();
      this.contentType = request.getContentType();
      this.contextPath = request.getContextPath();
      this.headerNames = Collections.list(request.getHeaderNames());
      this.headerMap = makeHeaderMap(request);
      this.remoteUser = request.getRemoteUser();
      this.servletPath = request.getServletPath();
      if (request.getUserPrincipal() != null) 
      {
         this.userPrincipal = new SpyPrincipal(request.getUserPrincipal());
      }
      
   }
   
   private Cookie[] cloneCookies(HttpServletRequest request)
   {
      Cookie[] reqCookies = request.getCookies();
      Cookie[] clonedCookies = new Cookie[reqCookies.length];
      for (int i=0; i < reqCookies.length; i++)
      {
         clonedCookies[i] = (Cookie)reqCookies[i].clone();
      }
      
      return clonedCookies;
   }
   
   private Map<String, String[]> makeHeaderMap(HttpServletRequest request)
   {
      Map<String, String[]> localHeaderMap = new HashMap<String, String[]>();
      for (String name : (List<String>)this.headerNames)
      {
         List headerValues = Collections.list(request.getHeaders(name));
         String[] headerValuesArray = (String[])headerValues.toArray(new String[0]);
         localHeaderMap.put(name, headerValuesArray);
      }
      
      return Collections.unmodifiableMap(localHeaderMap);
   }
   
   private boolean isServlet14OrGreater()
   {
      ServletContext servletContext = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
      return (servletContext.getMinorVersion() > 3) || (servletContext.getMajorVersion() > 1);
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
      return this.pathInfo;
   }

   @Override
   public Enumeration getParameterNames()
   {
      return Collections.enumeration(getParameterMap().keySet());
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
      return Collections.enumeration(this.requestLocales);
   }
   
   /**
    * Return getLocales() as a comma-seperated String.
    * 
    * @return getLocales() as a comma-seperated String.
    */
   public String getAcceptableLocales()
   {
      StringBuilder strLocales = new StringBuilder();
      for (Iterator<Locale> locales = this.requestLocales.iterator(); locales.hasNext();)
      {
         strLocales.append(locales.next().toString());
         if (locales.hasNext()) strLocales.append(", ");
      }
      
      return strLocales.toString();
   }
   
   @Override
   public Locale getLocale()
   {
      return this.locale;
   }

   @Override
   public Enumeration getAttributeNames()
   {
      throw new UnsupportedOperationException("Attributes are available by PhaseId.");
   }

   @Override
   public String getAuthType()
   {
      return this.authType;
   }

   @Override
   public String getCharacterEncoding()
   {
      return this.characterEncoding;
   }

   @Override
   public int getContentLength()
   {
      return this.contentLength;
   }

   @Override
   public String getContentType()
   {
      return this.contentType;
   }

   @Override
   public String getContextPath()
   {
      return this.contextPath;
   }

   @Override
   public Cookie[] getCookies()
   {
      return this.cookies;
   }
   
   @Override
   public Enumeration getHeaderNames()
   {
      return Collections.enumeration(this.headerNames);
   }

   /**
    * @throws UnsupportedOperationException
    */
   @Override
   public ServletInputStream getInputStream() throws IOException
   {
      throw new UnsupportedOperationException("InputStream not available.");
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
      return this.remoteUser;
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
      return this.servletPath;
   }

   @Override
   public HttpSession getSession()
   {
      throw new UnsupportedOperationException("HttpSession object not available here.");
   }

   @Override
   public Principal getUserPrincipal()
   {
      return this.userPrincipal;
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
      throw new UnsupportedOperationException("HttpSession object not available here.");
   }

   @Override
   public void setAttribute(String attribute, Object value)
   {
      throw new UnsupportedOperationException("Request Attributes not available here.");
   }

   /**
    * This method is a no-op.
    */
   @Override
   public void setCharacterEncoding(String string) throws UnsupportedEncodingException
   {
      //No-op
   }

   public List<ParamPair> getParamsAsList()
   {
      return makeParamList(getParameterMap());
   }
   
   public List<ParamPair> getHeadersAsList()
   {
      return makeParamList(this.headerMap);
   }
      
   private List<ParamPair> makeParamList(Map<String, String[]> params)
   {
      List<ParamPair> paramList = new ArrayList<ParamPair>();
      
      for (String param : params.keySet())
      {
         String[] values = params.get(param);
         for (String value : values)
         {
            paramList.add(new ParamPair(param, value));
         }
      }
      
      return paramList;
   }
   
   @Override
   public String[] getParameterValues(String name)
   {
      return (String[])this.parameterMap.get(name);
   }

   @Override
   public String getParameter(String name)
   {
      String[] values = getParameterValues(name);
      if (values == null) return null;
      
      return values[0];
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
      throw new UnsupportedOperationException("Request Attributes not available here.");
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
      String[] header = this.headerMap.get(name);
      if (header == null) return null;
      
      return header[0];
   }

   @Override
   public Enumeration getHeaders(String name)
   {
      String[] headers = (String[])this.headerMap.get(name);
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
      throw new UnsupportedOperationException();
   }

   @Override
   public void removeAttribute(String name)
   {
      throw new UnsupportedOperationException("Request Attributes not available here.");
   }

   /**
    *                                   
    */
   @Override
   public int getLocalPort()
   {
      return this.localPort;
   }
   
   /**
    *                                   
    */
   @Override
   public int getRemotePort()
   {
      return this.remotePort;
   }
   
   /**
    *                                   
    */
   @Override
   public String getLocalName()
   {
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
      return this.localAddr;
   }
   
   @Override
   public String toString()
   {
      String path = getRequestURI();
      if (path == null) return "/";
      return path.substring(getContextPath().length());
   }
}
