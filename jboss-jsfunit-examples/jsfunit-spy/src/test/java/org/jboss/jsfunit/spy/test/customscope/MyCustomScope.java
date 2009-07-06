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

package org.jboss.jsfunit.spy.test.customscope;

import java.util.HashMap;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.PostConstructCustomScopeEvent;
import javax.faces.event.PreDestroyCustomScopeEvent;
import javax.faces.event.ScopeContext;

/**
 * Custom scope for testing Spy handling of JSF 2.0 custom scopes.
 *
 * @author Stan Silvert
 * @since 1.1
 */
public class MyCustomScope extends HashMap
{
   private final String name;
   
   private final ScopeContext scopeContext;

   public MyCustomScope(String name)
   {
      this.name = name;
      scopeContext = new ScopeContext(name, this);
      FacesContext facesContext = FacesContext.getCurrentInstance();
      Application application = facesContext.getApplication();
      application.publishEvent(facesContext, PostConstructCustomScopeEvent.class, scopeContext);
   }

   public void destroy()
   {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      Application application = facesContext.getApplication();
      application.publishEvent(facesContext, PreDestroyCustomScopeEvent.class, scopeContext);
   }
   
   public String getName()
   {
      return this.name;
   }
}
