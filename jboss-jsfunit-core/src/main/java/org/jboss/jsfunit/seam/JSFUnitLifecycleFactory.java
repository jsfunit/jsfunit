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

package org.jboss.jsfunit.seam;

import java.util.Iterator;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

/**
 * This LifecycleFactory dispenses the JSFUnitLifecycle if Seam is present.
 * The JSFUnitLifecycle caches Seam conversation scope objects in the
 * HttpSession so that they are available for inspection by JSFUnit tests.
 *
 * @author Stan Silvert
 * @since 1.0
 */
public class JSFUnitLifecycleFactory extends LifecycleFactory
{

   private boolean isSeamPresent = SeamUtil.isSeamPresent();
   
   private LifecycleFactory delegate;
   
   public JSFUnitLifecycleFactory(LifecycleFactory delegate)
   {
      this.delegate = delegate;
   }
   
   @Override
   public void addLifecycle(String lifecycleId, Lifecycle lifecycle) 
   {
      delegate.addLifecycle(lifecycleId, lifecycle);
   }

   @Override
   public Lifecycle getLifecycle(String lifecycleId) 
   {
      Lifecycle realLifecycle = delegate.getLifecycle(lifecycleId);
      
      if (isSeamPresent) return new JSFUnitLifecycle(realLifecycle);
      
      return realLifecycle;
   }

   @Override
   public Iterator<String> getLifecycleIds() 
   {
      return delegate.getLifecycleIds();
   }

}
