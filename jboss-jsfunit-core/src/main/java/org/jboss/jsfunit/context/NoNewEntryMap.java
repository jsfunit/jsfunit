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

package org.jboss.jsfunit.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This Map copies all the entries of the wrapped Map into a new Map
 * and disallows new entries while JSFServerSession.getManagedBeanValue() is 
 * running.  The net effect is that any write-through capabilities 
 * (or other special features) of the wrapped Map are disabled.  Also,
 * put() will not create a new entry and will return null if the key does not exist.  
 * 
 * See http://jira.jboss.org/jira/browse/JSFUNIT-164
 *
 * @author Stan Silvert
 * @since 1.1
 */
public class NoNewEntryMap implements Map
{
   private Map wrapped;
   private boolean isELRunning;
   
   NoNewEntryMap(Map wrapped)
   {
      this.wrapped = new HashMap(wrapped);
   }        

   /**
    * JSFServerSession.getManagedBeanValue() calls this to disable creation
    * of a new managed bean during EL resolution.
    * 
    * @param isELRunning Enable/disable creation of managed beans.
    */
   public void setELRunning(boolean isELRunning)
   {
      this.isELRunning = isELRunning;
   }

   @Override
   public void clear()
   {
      wrapped.clear();
   }

   @Override
   public boolean containsKey(Object key)
   {
      return wrapped.containsKey(key);
   }

   @Override
   public boolean containsValue(Object value)
   {
      return wrapped.containsValue(value);
   }

   @Override
   public Set entrySet()
   {
      return wrapped.entrySet();
   }

   @Override
   public Object get(Object key)
   {
      return wrapped.get(key);
   }

   @Override
   public boolean isEmpty()
   {
      return wrapped.isEmpty();
   }

   @Override
   public Set keySet()
   {
      return wrapped.keySet();
   }

   /**
    * This method does not allow new values to be placed into the Map.  If
    * the Map does not already contain the key then it returns null.  Otherwise,
    * it returns the value already held in the Map.
    * 
    * @param key The Map key.
    * @param value The value that will be thrown away.
    * 
    * @return <code>null</code> if the key does not already exist, otherwise
    *         return the value already held in the Map.
    */
   @Override
   public Object put(Object key, Object value)
   {
      if (this.isELRunning && (wrapped.get(key) == null))
      {
         throw new NewEntryNotAllowedException();
      }
      
      return wrapped.put(key, value);
   }

   @Override
   public void putAll(Map m)
   {
      wrapped.putAll(m);
   }

   @Override
   public Object remove(Object key)
   {
      return wrapped.remove(key);
   }

   @Override
   public int size()
   {
      return wrapped.size();
   }

   @Override
   public Collection values()
   {
      return wrapped.values();
   }
   
   public static class NewEntryNotAllowedException extends RuntimeException
   {
        NewEntryNotAllowedException()
        {
           super("EL not allowed to create new entries from JSFUnit test thread.");
        }
   }

}