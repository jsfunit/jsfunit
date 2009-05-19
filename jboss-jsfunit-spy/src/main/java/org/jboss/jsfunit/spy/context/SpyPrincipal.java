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

package org.jboss.jsfunit.spy.context;

import java.security.Principal;

/**
 * This implementation of Principal just caches the values.
 *
 * @author Stan Silvert
 * @since 1.1
 */
public class SpyPrincipal implements Principal
{
   private String name;
   private String savedToString;
   private int savedHashCode = 0;
   
   public SpyPrincipal(Principal principal)
   {
      if (principal == null) throw new NullPointerException();
      
      this.name = principal.getName();
      this.savedToString = principal.toString();
      this.savedHashCode = principal.hashCode();
   }

   public String getName()
   {
      return this.name;
   }

   @Override
   public String toString()
   {
      return this.savedToString;
   }
   
   @Override
   public int hashCode()
   {
      return this.savedHashCode;
   }

   @Override
   public boolean equals(Object another)
   {
      if (another == this) return true;
      if (!(another instanceof Principal)) return false;
      Principal anotherPrincipal = (Principal)another;
      
      return (anotherPrincipal.hashCode() == this.hashCode()) &&
             (anotherPrincipal.getName().equals(this.getName()));
   }
}
