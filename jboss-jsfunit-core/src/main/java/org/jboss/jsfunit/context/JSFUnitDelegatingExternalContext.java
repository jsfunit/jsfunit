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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpSession;

/**
 * This ExternalContext will delegate everything to the real ExternalContext
 * except for the getSession() method.  For that, it will return a
 * JSFUnitHttpSession that protects JSFUnit's session attributes.
 *
 * @author Stan Silvert
 * @since 1.0
 */
public class JSFUnitDelegatingExternalContext extends ExternalContext
{
   private ExternalContext wrapped;
   private HttpSession session;
   
   JSFUnitDelegatingExternalContext(ExternalContext wrapped)
   {
      if (wrapped == null) throw new NullPointerException("wrapped ExternalContext can not be null");
      
      this.wrapped = wrapped;
   }
   
   @Override
   public Principal getUserPrincipal()
   {
      return wrapped.getUserPrincipal();
   }

   @Override
   public Map<String, Object> getSessionMap()
   {
      return wrapped.getSessionMap();
   }

   @Override
   public Object getResponse()
   {
      return wrapped.getResponse();
   }

   @Override
   public Map<String, Object> getRequestCookieMap()
   {
      return wrapped.getRequestCookieMap();
   }

   @Override
   public String getRequestContextPath()
   {
      return wrapped.getRequestContextPath();
   }

   @Override
   public Object getRequest()
   {
      return wrapped.getRequest();
   }

   @Override
   public String getRemoteUser()
   {
      return wrapped.getRemoteUser();
   }

   @Override
   public Map getInitParameterMap()
   {
      return wrapped.getInitParameterMap();
   }

   @Override
   public Object getContext()
   {
      return wrapped.getContext();
   }

   @Override
   public String getAuthType()
   {
      return wrapped.getAuthType();
   }

   @Override
   public Map<String, Object> getApplicationMap()
   {
      return wrapped.getApplicationMap();
   }

   @Override
   public Map<String, String> getRequestHeaderMap()
   {
      return wrapped.getRequestHeaderMap();
   }

   @Override
   public Map<String, String[]> getRequestHeaderValuesMap()
   {
      return wrapped.getRequestHeaderValuesMap();
   }

   @Override
   public Locale getRequestLocale()
   {
      return wrapped.getRequestLocale();
   }

   @Override
   public Iterator<Locale> getRequestLocales()
   {
      return wrapped.getRequestLocales();
   }

   @Override
   public Map<String, Object> getRequestMap()
   {
      return wrapped.getRequestMap();
   }
   
   @Override
   public Map<String, String> getRequestParameterMap()
   {
      return wrapped.getRequestParameterMap();
   }

   @Override
   public Iterator<String> getRequestParameterNames()
   {
      return wrapped.getRequestParameterNames();
   }

   @Override
   public Map<String, String[]> getRequestParameterValuesMap()
   {
      return wrapped.getRequestParameterValuesMap();
   }

   @Override
   public String getRequestPathInfo()
   {
      return wrapped.getRequestPathInfo();
   }

   @Override
   public String getRequestServletPath()
   {
      return wrapped.getRequestServletPath();
   }
   
   /**
    * Return a JSFUnitHttpSession.
    *
    * @param create See superclass description.
    *
    * @return A JSFUnitHttpSession or <code>null</code> if create is false
    *         and there is no current session.
    */
   @Override
   public Object getSession(boolean create)
   {
      if ((this.session == null) && (wrapped.getSession(create) != null))
      {
         this.session = new JSFUnitHttpSession((HttpSession)wrapped.getSession(create));
      }
      
      return this.session;
   }

   @Override
   public void redirect(String url) throws IOException
   {
      wrapped.redirect(url);
   }

   @Override
   public void log(String message)
   {
      wrapped.log(message);
   }

   @Override
   public boolean isUserInRole(String role)
   {
      return wrapped.isUserInRole(role);
   }

   @Override
   public Set<String> getResourcePaths(String path)
   {
      return wrapped.getResourcePaths(path);
   }

   @Override
   public String getInitParameter(String name)
   {
      return wrapped.getInitParameter(name);
   }

   @Override
   public String encodeResourceURL(String url)
   {
      return wrapped.encodeResourceURL(url);
   }

   @Override
   public String encodeNamespace(String name)
   {
      return wrapped.encodeNamespace(name);
   }

   @Override
   public String encodeActionURL(String url)
   {
      return wrapped.encodeActionURL(url);
   }

   @Override
   public void dispatch(String path) throws IOException
   {
      wrapped.dispatch(path);
   }

   @Override
   public URL getResource(String path) throws MalformedURLException
   {
      return wrapped.getResource(path);
   }

   @Override
   public InputStream getResourceAsStream(String path)
   {
      return wrapped.getResourceAsStream(path);
   }

   @Override
   public void log(String message, Throwable exception)
   {
      wrapped.log(message, exception);
   }

   @Override
   public String getResponseContentType()
   {
      return wrapped.getResponseContentType();
   }

   @Override
   public String getResponseCharacterEncoding()
   {
      return wrapped.getResponseCharacterEncoding();
   }

   @Override
   public String getRequestContentType()
   {
      return wrapped.getRequestContentType();
   }

   @Override
   public String getRequestCharacterEncoding()
   {
      return wrapped.getRequestCharacterEncoding();
   }

   @Override
   public void setResponse(Object response)
   {
      wrapped.setResponse(response);
   }

   @Override
   public void setRequest(Object request)
   {
      wrapped.setRequest(request);
   }

   @Override
   public void setResponseCharacterEncoding(String encoding)
   {
      wrapped.setResponseCharacterEncoding(encoding);
   }

   @Override
   public void setRequestCharacterEncoding(String encoding) throws UnsupportedEncodingException
   {
      wrapped.setRequestCharacterEncoding(encoding);
   }
   
   //--------- JSF 2.0 Methods -------------------------------------
   @Override
   public void addResponseCookie(String name, String value, Map<String, Object> properties)
   {
      wrapped.addResponseCookie(name, value, properties);
   }

   @Override
   public String getContextName()
   {
      return wrapped.getContextName();
   }

   @Override
   public String getMimeType(String arg0)
   {
      return wrapped.getMimeType(arg0);
   }

   @Override
   public String getRealPath(String arg0)
   {
      return wrapped.getRealPath(arg0);
   }

   @Override
   public int getRequestContentLength()
   {
      return wrapped.getRequestContentLength();
   }

   @Override
   public String getRequestScheme()
   {
      return wrapped.getRequestScheme();
   }

   @Override
   public String getRequestServerName()
   {
      return wrapped.getRequestServerName();
   }

   @Override
   public int getRequestServerPort()
   {
      return wrapped.getRequestServerPort();
   }

   @Override
   public OutputStream getResponseOutputStream() throws IOException
   {
      return wrapped.getResponseOutputStream();
   }

   /**
    * Invalidate the session.  This does not actually destroy the session.  It
    * leaves the session intact and removes all elements except those used
    * by the JSFUnit framework.
    */
   @Override
   public void invalidateSession()
   {
      if (this.session != null) this.session.invalidate();
   }

   @Override
   public void setResponseContentType(String arg0)
   {
      wrapped.setResponseContentType(arg0);
   }
   
   @Override
   public void addResponseHeader(String name, String value)
   {
      wrapped.addResponseHeader(name, value);
   }
   
   @Override
   public void setResponseHeader(String name, String value)
   {
      wrapped.setResponseHeader(name, value);
   }
   //--------- End JSF 2.0 Methods ---------------------------------
}
