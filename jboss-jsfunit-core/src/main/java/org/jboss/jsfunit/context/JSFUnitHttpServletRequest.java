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

package org.jboss.jsfunit.context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Stan Silvert
 */
public class JSFUnitHttpServletRequest implements HttpServletRequest
{
   private JSFUnitExternalContext extCtx;
   
   public JSFUnitHttpServletRequest(JSFUnitExternalContext extCtx)
   {
      this.extCtx = extCtx;
   }
   
   public String getQueryString()
   {
      return null;
   }

   public String getProtocol()
   {
      return null;
   }

   public String getPathTranslated()
   {
      return null;
   }

   public String getPathInfo()
   {
      return this.extCtx.getRequestPathInfo();
   }

   public Enumeration getParameterNames()
   {
      return Collections.enumeration(this.extCtx.getRequestParameterMap().keySet());
   }

   public Map getParameterMap()
   {
      return this.extCtx.getRequestParameterMap();
   }

   public String getMethod()
   {
      return null;
   }

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

   public Locale getLocale()
   {
      return this.extCtx.getRequestLocale();
   }

   public Enumeration getAttributeNames()
   {
      return Collections.enumeration(this.extCtx.getRequestMap().keySet());
   }

   public String getAuthType()
   {
      return this.extCtx.getAuthType();
   }

   public String getCharacterEncoding()
   {
      return this.extCtx.getRequestCharacterEncoding();
   }

   public int getContentLength()
   {
      // TODO:
      return 0;
   }

   public String getContentType()
   {
      return this.extCtx.getRequestContentType();
   }

   public String getContextPath()
   {
      return this.extCtx.getRequestContextPath();
   }

   public Cookie[] getCookies()
   {
      return null;
   }

   public Enumeration getHeaderNames()
   {
      return Collections.enumeration(this.extCtx.getRequestHeaderMap().keySet());
   }

   /**
    * Unsupported
    */
   public ServletInputStream getInputStream() throws IOException
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Unsupported
    */
   public BufferedReader getReader() throws IOException
   {
      throw new UnsupportedOperationException();
   }

   public String getRemoteAddr()
   {
      return null;
   }

   public String getRemoteHost()
   {
      return null;
   }

   public String getRemoteUser()
   {
      return null;
   }

   public String getRequestURI()
   {
      return null;
   }

   public StringBuffer getRequestURL()
   {
      return null;
   }

   public String getRequestedSessionId()
   {
      return null;
   }

   public String getScheme()
   {
      return null;
   }

   public String getServerName()
   {
      return null;
   }

   public int getServerPort()
   {
      //TODO
      return 0;
   }

   public String getServletPath()
   {
      return this.extCtx.getRequestServletPath();
   }

   public HttpSession getSession()
   {
      return (HttpSession)this.extCtx.getSession(true);
   }

   public Principal getUserPrincipal()
   {
      return this.extCtx.getUserPrincipal();
   }

   public boolean isRequestedSessionIdFromCookie()
   {
      // always true in JSFUnit
      return true;
   }

   public boolean isRequestedSessionIdFromURL()
   {
      // always false in JSFUnit
      return false;
   }

   public boolean isRequestedSessionIdFromUrl()
   {
      // always false in JSFUnit
      return false;
   }

   public boolean isRequestedSessionIdValid()
   {
      // always true in JSFUnit
      return true;
   }

   public boolean isSecure()
   {
      // TODO
      return false;
   }

   public HttpSession getSession(boolean b)
   {
      return (HttpSession)this.extCtx.getSession(b);
   }

   public void setAttribute(String attribute, Object value)
   {
      this.extCtx.getRequestMap().put(attribute, value);
   }

   public void setCharacterEncoding(String string) throws UnsupportedEncodingException
   {
      //TODO???
   }

   public String[] getParameterValues(String string)
   {
      ArrayList myList = new ArrayList(this.extCtx.getRequestParameterValuesMap().values());
      return (String[])myList.toArray(new String[0]);
   }

   public String getParameter(String name)
   {
      return (String)this.extCtx.getRequestParameterMap().get(name);
   }

   public int getIntHeader(String string)
   {
      //TODO
      return 0;
   }

   public Object getAttribute(String name)
   {
      return this.extCtx.getRequestMap().get(name);
   }

   public long getDateHeader(String string)
   {
      //TODO
      return 0L;
   }

   public String getHeader(String name)
   {
      return (String)this.extCtx.getRequestHeaderMap().get(name);
   }

   public Enumeration getHeaders(String string)
   {
      //TODO
      return null;
   }

   public String getRealPath(String string)
   {
      //TODO
      return null;
   }

   /**
    * Unsupported
    */
   public RequestDispatcher getRequestDispatcher(String string)
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Warning: This method could yield unexpected results.
    */
   public boolean isUserInRole(String name)
   {
      return this.extCtx.isUserInRole(name);
   }

   public void removeAttribute(String name)
   {
      this.extCtx.getRequestMap().remove(name);
   }

   // Servlet 2.4
   public int getLocalPort()
   {
      //TODO
      return 0;
   }
   
   // Servlet 2.4
   public int getRemotePort()
   {
      //TODO
      return 0;
   }
   
   // Servlet 2.4
   public String getLocalName()
   {
      //TODO
      return "";
   }
   
   // Servlet 2.4
   public String getLocalAddr()
   {
      //TODO
      return "";
   }
}
