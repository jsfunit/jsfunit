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

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.jboss.jsfunit.framework.WebConversationFactory;

/**
 * When a JSFUnit request is active, this FacesContextFactory will provide 
 * a wrapper for the "real" FacesContext.  If the request is not a JSFUnit
 * request it just returns the true FacesContext.
 *
 * @author Stan Silvert
 */
public class JSFUnitFacesContextFactory extends FacesContextFactory
{
   private FacesContextFactory parent;
   
   public JSFUnitFacesContextFactory(FacesContextFactory parent)
   {
      this.parent = parent;
   }
   
   public FacesContext getFacesContext(Object context, 
                                       Object request, 
                                       Object response, 
                                       Lifecycle lifecycle) throws FacesException
   {
      HttpServletRequest req = (HttpServletRequest)request;
      if (isJSFUnitRequest(req))
      {
         req.getSession().removeAttribute(JSFUnitFacesContext.SESSION_KEY); // must remove before creating any new FacesContext
         FacesContext realFacesContext = parent.getFacesContext(context, request, response, lifecycle);
         return new JSFUnitFacesContext(realFacesContext, request);
      }
      
      return parent.getFacesContext(context, request, response, lifecycle);
   }
   
   private boolean isJSFUnitRequest(HttpServletRequest req)
   {
      Cookie[] cookies = req.getCookies();
      if (cookies == null) return false;
      
      for (int i=0; i < cookies.length; i++)
      {
         if (cookies[i].getName().equals(WebConversationFactory.JSF_UNIT_CONVERSATION_FLAG)) return true;
      }
      
      return false;
   }
   
}
