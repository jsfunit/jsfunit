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

import javax.servlet.http.HttpServletRequest;
import org.jboss.jsfunit.framework.Environment;
import org.jboss.seam.Component;
import org.jboss.seam.contexts.ServletLifecycle;
import org.jboss.seam.web.Session;

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
   
   private static final boolean isLog4JAvailable;
   
   static
   {
      boolean available;
      try
      {
         Class.forName("org.apache.log4j.Logger");
         available = true;
      }
      catch (ClassNotFoundException cnfe)
      {
         available = false;
      }
      isLog4JAvailable = available;
   }
   
   public static boolean isSeamPresent()
   {
      Class lifecycle = getSeamLifecycle();
      return lifecycle != null;
   }
   
   private static Class getSeamLifecycle()
   {
      return Environment.loadClass("org.jboss.seam.contexts.Lifecycle");
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
    * Invalidate the session the Seam way.  This allows Seam to clean itself
    * up when the session is ending.
    *
    * @param httpServletRequest The current request.
    */
   public static void invalidateSeamSession(HttpServletRequest httpServletRequest)
   {
      try
      {
        ServletLifecycle.beginRequest(httpServletRequest); 
        Session.instance().invalidate();
        ServletLifecycle.endRequest(httpServletRequest);
      }
      catch (Exception e)
      {
         throw new IllegalStateException("Unable to invalidate Seam session.", e);
      }
   }
   
   public static void suppressSeamComponentWarning()
   {
      if (isLog4JAvailable) supressLog4J();
      if (!isLog4JAvailable) supressJUL();
   }
   
   private static void supressLog4J()
   {
      org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Component.class);
      logger.setLevel((org.apache.log4j.Level)org.apache.log4j.Level.ERROR);
   }

   private static void supressJUL()
   {
      java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Component.class.getName());
      logger.setLevel(java.util.logging.Level.SEVERE);
   }
}
