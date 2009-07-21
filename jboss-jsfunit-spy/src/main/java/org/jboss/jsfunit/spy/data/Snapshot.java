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
import javax.faces.event.ScopeContext;
import org.jboss.jsfunit.spy.seam.SeamUtil;

/**
 * A snapshot records the state of scoped data at a
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
   
   private long spyTime;
   
   private Map<Scope, Map<String, String>> scopes = new HashMap<Scope, Map<String, String>>();
   
   private Map<String, Map<String, String>> customScopes = new HashMap<String, Map<String, String>>();
   
   Snapshot(BeforeOrAfter beforeOrAfter, PhaseEvent event)
   {
      long spyStampStart = System.currentTimeMillis();
      
      this.beforeOrAfter = beforeOrAfter;
      this.phaseId = event.getPhaseId();
      
      FacesContext facesContext = event.getFacesContext();
      ExternalContext extContext = facesContext.getExternalContext();
      scopes.put(Scope.APPLICATION, makeStringMap(extContext.getApplicationMap()));
      scopes.put(Scope.SESSION, makeStringMap(extContext.getSessionMap()));
      scopes.put(Scope.REQUEST, makeStringMap(extContext.getRequestMap()));
      scopes.put(Scope.CONVERSATION, makeStringMap(SeamUtil.getConversationMap()));
      recordCustomScopes();
      
      this.timestamp = System.currentTimeMillis();
      this.spyTime = this.timestamp - spyStampStart;
   }
   
   private void recordCustomScopes()
   {
      Session session = SpyManager.getInstance().getMySession();
      for (ScopeContext ctx : session.getCustomScopes())
      {
         customScopes.put(ctx.getScopeName(), 
                          Collections.unmodifiableMap(makeStringMap(ctx.getScope())));
      }
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
   
   /**
    * The amount of time in milliseconds that it took to take this snapshot.
    * 
    * @return The time to take this snapshot.
    */
   public long getSpyTime()
   {
      return this.spyTime;
   }
   
   public Map<String, String> getScope(Scope scope)
   {
      return Collections.unmodifiableMap(this.scopes.get(scope));
   }
   
   public Map<String, Map<String, String>> getCustomScopes()
   {
      return Collections.unmodifiableMap(this.customScopes);
   }
   
}
