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

package org.jboss.jsfunit.spy.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

/**
 * A snapshot records the state of scoped data and the component tree at a
 * particular point in time during the JSF lifecycle.  A snapshot is taken
 * before and after each lifecycle phase.
 *
 * @author Stan Silvert
 * @since 1.1
 */
public class Snapshot {

   private BeforeOrAfter beforeOrAfter;
   private PhaseId phaseId;
   
   private long timestamp;
   
   private Map<Scope, Map<String, String>> scopes = new HashMap<Scope, Map<String, String>>();
   
   private Map<String, String> appScope = new HashMap<String, String>();
   private Map<String, String> sessScope = new HashMap<String, String>();
   private Map<String, String> reqScope = new HashMap<String, String>();
   
   Snapshot(BeforeOrAfter beforeOrAfter, PhaseEvent event)
   {
      this.beforeOrAfter = beforeOrAfter;
      this.phaseId = event.getPhaseId();
      
      this.timestamp = System.currentTimeMillis();
      
      FacesContext facesContext = event.getFacesContext();
      ExternalContext extContext = facesContext.getExternalContext();
      scopes.put(Scope.APPLICATION, makeStringMap(extContext.getApplicationMap()));
      scopes.put(Scope.SESSION, makeStringMap(extContext.getSessionMap()));
      scopes.put(Scope.REQUEST, makeStringMap(extContext.getRequestMap()));
   }
   
   private Map<String, String> makeStringMap(Map map)
   {
      Map<String, String> stringMap = new HashMap<String, String>(map.size());
      
      for (Iterator i = map.keySet().iterator(); i.hasNext();)
      {
         Object objKey = i.next();
         String key = objKey.toString();
         while (stringMap.containsKey(key))
         {
            key = key + "_spydupkey";
         }
         //System.out.println(key + "=" + map.get(objKey).toString());
         
         try
         {
            stringMap.put(key.intern(), map.get(objKey).toString().intern());
         }
         catch (Exception e)
         { // Sometimes in Seam, toString() fails with NotLoggedInException
            stringMap.put(key.intern(), "toString() raised Exception: " + e.getLocalizedMessage());
         }
      }
      
      return stringMap;
   }

   public BeforeOrAfter getBeforeOrAfter()
   {
      return this.beforeOrAfter;
   }
   
   public PhaseId getPhaseId()
   {
      return this.phaseId;
   }
   
   public long getTimestamp()
   {
      return this.timestamp;
   }
   
   public Map<String, String> getScopeMap(Scope scope)
   {
      return Collections.unmodifiableMap(this.scopes.get(scope));
   }
   
}
