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

import java.util.Enumeration;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import org.jboss.jsfunit.framework.JSFUnitFilter;

/**
 * This HttpSession delegates everything to the real HttpSession except for
 * the invalidate method.  For that, it will clear the session of all 
 * attributes except those used by the JSFUnit framework.  It will not
 * actually destroy the session.
 *
 * @author Stan Silvert
 * @since 1.0
 */
public class JSFUnitHttpSession implements HttpSession
{
   private HttpSession wrapped;
   
   JSFUnitHttpSession(HttpSession wrapped)
   {
      if (wrapped == null) throw new NullPointerException("HttpSession can not be null");
      
      this.wrapped = wrapped;
   }
   
   @Override
   public Enumeration getAttributeNames()
   {
      return wrapped.getAttributeNames();
   }

   @Override
   public long getCreationTime()
   {
      return wrapped.getCreationTime();
   }

   @Override
   public String getId()
   {
      return wrapped.getId();
   }

   @Override
   public long getLastAccessedTime()
   {
      return wrapped.getLastAccessedTime();
   }

   @Override
   public int getMaxInactiveInterval()
   {
      return wrapped.getMaxInactiveInterval();
   }

   @Override
   public ServletContext getServletContext()
   {
      return wrapped.getServletContext();
   }

   @Deprecated
   @Override
   public HttpSessionContext getSessionContext()
   {
      return wrapped.getSessionContext();
   }

   @Deprecated
   @Override
   public String[] getValueNames()
   {
      return wrapped.getValueNames();
   }

   /**
    * Instead of invalidating the session, clear all attributes except those
    * used by JSFUnit.
    */
   @Override
   public void invalidate()
   {
      for (Enumeration e = getAttributeNames(); e.hasMoreElements();)
      {
         String attribute = (String)e.nextElement();
         if (!attribute.startsWith("org.jboss.jsfunit"))
         {
            removeAttribute(attribute);
         }
      }
   }

   @Override
   public boolean isNew()
   {
      return wrapped.isNew();
   }

   @Override
   public void setMaxInactiveInterval(int i)
   {
      wrapped.setMaxInactiveInterval(i);
   }

   @Deprecated
   @Override
   public void removeValue(String name)
   {
      removeAttribute(name);
   }

   @Override
   public void removeAttribute(String name)
   {
      if (!name.equals(JSFUnitFilter.REDIRECTOR_REQUEST_PARAMS_KEY))
      {
         wrapped.removeAttribute(name);
      }
   }

   @Override
   public Object getAttribute(String name)
   {
      return wrapped.getAttribute(name);
   }

   @Deprecated
   @Override
   public Object getValue(String name)
   {
      return wrapped.getValue(name);
   }

   @Override
   public void setAttribute(String name, Object value)
   {
      wrapped.setAttribute(name, value);
   }

   @Deprecated
   @Override
   public void putValue(String name, Object value)
   {
      wrapped.putValue(name, value);
   }
   
}