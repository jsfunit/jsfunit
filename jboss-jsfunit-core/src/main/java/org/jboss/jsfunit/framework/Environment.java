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

import java.util.Arrays;
import java.util.List;

/**
 * Contains methods to determine the test environment.
 *
 * @author Stan Silvert
 */
public class Environment
{
   private static final Class appClass = loadClass("javax.faces.application.Application");
   
   private static final List methods12 = 
           Arrays.asList(new String[] {"getELResolver", 
                                       "getExpressionFactory"});
   
   private static final List methods20 = 
           Arrays.asList(new String[] {"getResourceHandler", 
                                       "setResourceHandler",
                                       "setPageDeclarationLanguage"});
   
   private static final boolean is12Compatible;
   private static final boolean is20Compatible;
   
   static
   {
      if (appClass != null)
      {
         is12Compatible = appHasMethod("getELResolver") &&
                          appHasMethod("getExpressionFactory");
         
         is20Compatible = appHasMethod("getResourceHandler") &&
                          appHasMethod("getProjectStage");
      }
      else
      {
         is12Compatible = false;
         is20Compatible = false;
      }
   }
   
   // don't allow an instance of this static utility class
   private Environment()
   {
   }
   
   private static boolean appHasMethod(String methodName, Class... params)
   {
      try
      {
         return appClass.getMethod(methodName, params) != null;
      }
      catch (Throwable e)
      {
         return false;
      }
   }
   
   /**
    * Determine if the running JSF version is compatible with the JSF 2.0
    * specification.
    * 
    * @return <code>true</code> if running JSF 2.0 or higher, <code>false</code> otherwise.
    */
   public static boolean is20Compatible()
   {
      return is20Compatible;
   }
   
   /**
    * Determine if the running JSF version is compatible with the JSF 1.2
    * specification.
    * 
    * @return <code>true</code> if running JSF 1.2 or higher, <code>false</code> otherwise.
    */
   public static boolean is12Compatible()
   {
      return is12Compatible;
   }

   /**
    * Return 2 for JSF 2.0 and above.  Return 1 otherwise.
    *
    * @return 2 for JSF 2.0 and above.  Return 1 otherwise.
    */
   public static int getJSFMajorVersion()
   {
      if (is20Compatible()) return 2;
      
      return 1;
   }
   
   /**
    * Returns the JSF minor version.  This method will not detect JSF 1.0.
    *
    * @return 1 for JSF 1.1, 2 for JSF 1.2, or 0 for JSF 2.0.
    */
   public static int getJSFMinorVersion()
   {
      if (is20Compatible()) return 0;
      
      if (is12Compatible()) return 2;
      
      return 1;
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
