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
   
   public Principal getUserPrincipal()
   {
      return wrapped.getUserPrincipal();
   }

   public Map<String, Object> getSessionMap()
   {
      return wrapped.getSessionMap();
   }

   public Object getResponse()
   {
      return wrapped.getResponse();
   }

   public Map<String, Object> getRequestCookieMap()
   {
      return wrapped.getRequestCookieMap();
   }

   public String getRequestContextPath()
   {
      return wrapped.getRequestContextPath();
   }

   public Object getRequest()
   {
      return wrapped.getRequest();
   }

   public String getRemoteUser()
   {
      return wrapped.getRemoteUser();
   }

   public Map getInitParameterMap()
   {
      return wrapped.getInitParameterMap();
   }

   public Object getContext()
   {
      return wrapped.getContext();
   }

   public String getAuthType()
   {
      return wrapped.getAuthType();
   }

   public Map<String, Object> getApplicationMap()
   {
      return wrapped.getApplicationMap();
   }

   public Map<String, String> getRequestHeaderMap()
   {
      return wrapped.getRequestHeaderMap();
   }

   public Map<String, String[]> getRequestHeaderValuesMap()
   {
      return wrapped.getRequestHeaderValuesMap();
   }

   public Locale getRequestLocale()
   {
      return wrapped.getRequestLocale();
   }

   public Iterator<Locale> getRequestLocales()
   {
      return wrapped.getRequestLocales();
   }

   public Map<String, Object> getRequestMap()
   {
      return wrapped.getRequestMap();
   }

   public Map<String, String> getRequestParameterMap()
   {
      return wrapped.getRequestParameterMap();
   }

   public Iterator<String> getRequestParameterNames()
   {
      return wrapped.getRequestParameterNames();
   }

   public Map<String, String[]> getRequestParameterValuesMap()
   {
      return wrapped.getRequestParameterValuesMap();
   }

   public String getRequestPathInfo()
   {
      return wrapped.getRequestPathInfo();
   }

   public String getRequestServletPath()
   {
      return wrapped.getRequestServletPath();
   }
   
   /**
    * Return a JSFUnitHttpSession.
    *
    * @param create See superclass description.
    *
    * @return An JSFUnitHttpSession or <code>null</code> if create is false
    *         and there is no current session.
    */
   public Object getSession(boolean create)
   {
      if ((this.session == null) && (wrapped.getSession(create) != null))
      {
         this.session = new JSFUnitHttpSession((HttpSession)wrapped.getSession(create));
      }
      
      return this.session;
   }

   public void redirect(String url) throws IOException
   {
      wrapped.redirect(url);
   }

   public void log(String message)
   {
      wrapped.log(message);
   }

   public boolean isUserInRole(String role)
   {
      return wrapped.isUserInRole(role);
   }

   public Set<String> getResourcePaths(String path)
   {
      return wrapped.getResourcePaths(path);
   }

   public String getInitParameter(String name)
   {
      return wrapped.getInitParameter(name);
   }

   public String encodeResourceURL(String url)
   {
      return wrapped.encodeResourceURL(url);
   }

   public String encodeNamespace(String name)
   {
      return wrapped.encodeNamespace(name);
   }

   public String encodeActionURL(String url)
   {
      return wrapped.encodeActionURL(url);
   }

   public void dispatch(String path) throws IOException
   {
      wrapped.dispatch(path);
   }

   public URL getResource(String path) throws MalformedURLException
   {
      return wrapped.getResource(path);
   }

   public InputStream getResourceAsStream(String path)
   {
      return wrapped.getResourceAsStream(path);
   }

   public void log(String message, Throwable exception)
   {
      wrapped.log(message, exception);
   }
   
}
