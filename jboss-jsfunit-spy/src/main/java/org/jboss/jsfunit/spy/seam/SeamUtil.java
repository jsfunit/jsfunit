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

package org.jboss.jsfunit.spy.seam;

import java.util.HashMap;
import java.util.Map;
import org.jboss.seam.contexts.Context;
import org.jboss.seam.contexts.Contexts;

/**
 * This class contains some useful methods for working with the Seam
 * framework.
 *
 * @author Stan Silvert
 * @since 1.0
 */
public class SeamUtil
{
   
   // don't allow an instance of this class
   private SeamUtil() {}
   
   
   public static Map getConversationMap()
   {
      Map convMap = new HashMap();
      if (!isSeamPresent()) return convMap;
      
      Context convContext = Contexts.getConversationContext();
      if (convContext == null) return convMap;
      
      for (String name : convContext.getNames())
      {
         convMap.put(name, convContext.get(name));
      }
      
      return convMap;
   }
   
   /**
    * Determines if Seam is present in this web application.
    * 
    * @return <code>true</code> if Seam is present, <code>false</code> otherwise.
    */
   public static boolean isSeamPresent()
   {
      Class lifecycle = getSeamLifecycle();
      return lifecycle != null;
   }
   
   private static Class getSeamLifecycle()
   {
      return loadClass("org.jboss.seam.contexts.Lifecycle");
   }
   
   /**
    * Return <code>true</code> if the Seam framework is present and
    * initialized.
    *
    * @return <code>true</code> if Seam is intialized, <code>false</code
    *         otherwise.
    */
   public static boolean isSeamInitialized()
   {
      Class lifecycle = getSeamLifecycle();
      if (lifecycle == null) return false;
      
      try
      {
         Boolean returnVal = (Boolean)lifecycle.getMethod("isApplicationInitialized", null)
                                               .invoke(null, null);
         return returnVal.booleanValue();
      }
      catch (Exception e)
      {
         return false;
      }
   }
   
   /**
    * Load a class using the context class loader.
    *
    * @param clazz The class name to load.
    *
    * @return The Class or <code>null</code> if not found.
    */
   public static Class loadClass(String clazz)
   {
      try
      {
         return Thread.currentThread()
                      .getContextClassLoader()
                      .loadClass(clazz);
      }
      catch (NoClassDefFoundError e)
      {
         return null;
      }
      catch (ClassNotFoundException e)
      {
         return null;
      }
      catch (Exception e)
      {
         throw new IllegalStateException(e);
      }
   }
}
