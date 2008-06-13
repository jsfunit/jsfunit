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

package org.jboss.jsfunit.framework;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.jboss.jsfunit.context.JSFUnitFacesContext;

/**
 * This class pulls the FacesContext from the previous request and associates it
 * with the JSFUnit thread.
 *
 * @author Stan Silvert
 */
public class FacesContextBridge
{
   
   // Don't allow a new instance of FacesContextBridge
   private FacesContextBridge()
   {
   }
   
   /**
    * Get the FacesContext from the previous request.  The FacesContext is a
    * per-request object.  So, this must be called after each faces request to get
    * the latest copy.
    * 
    * @return The FacesContext from the previous request, or <code>null</code> if
    *         no FacesContext has been created.
    */
   public static FacesContext getCurrentInstance()
   {
      HttpSession session = WebConversationFactory.getSessionFromThreadLocal();
      JSFUnitFacesContext facesContext = (JSFUnitFacesContext)session.getAttribute(JSFUnitFacesContext.SESSION_KEY);
      if (facesContext == null) return null;
      facesContext.setInstanceToJSFUnitThread();
      return facesContext;
   }
   
}
