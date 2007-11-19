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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

/**
 * The JSFTimer collects performance data during a JSF request.  It tracks
 * the time for each phase in the JSF Lifecycle and remains accurate even when
 * phases are skipped. 
 * 
 * To use this class you just need to get the instance with the static getTimer() 
 * method.  The instance is held in the session.
 *
 * Statistics are reset at the beginning of each JSF request.  So you can use
 * this class during the JSF lifecycle or afterwards in a JSFUnit test.
 *
 * @author Stan Silvert
 */
public class JSFTimer
{
   
   public static final String SESSION_KEY = JSFTimer.class.getName() + ".SESSION_KEY";
   
   private Map<PhaseId, Long> beforeMap = new HashMap<PhaseId, Long>();
   private Map<PhaseId, Long> afterMap = new HashMap<PhaseId, Long>();
   
   // only allow this class to create an instance
   private JSFTimer()
   {
   }
   
   // ---------------- package private methods --------------------
   void beforePhase(PhaseId phaseId)
   {
      beforeMap.put(phaseId, new Long(System.currentTimeMillis()));
   }
   
   void afterPhase(PhaseId phaseId)
   {
      afterMap.put(phaseId, new Long(System.currentTimeMillis()));
   }
   // -------------------------------------------------------------
   
   /**
    * Clear the timer.  This should only be called by JSFUnit.
    */
   public static void clear()
   {
      try
      {
         JSFTimer timer = getTimer();
         timer.beforeMap.clear();
         timer.afterMap.clear();
      }
      catch (IllegalStateException e)
      {
         // ignore
      }
   }
   
   
   /**
    * Get a reference to the JSFTimer.
    *
    * @return The JSFTimer.
    *
    * @throws IllegalStateException If no JSF requests have been made on this session.
    */
   public static JSFTimer getTimer()
   {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      
      try
      {
         if (facesContext == null) facesContext = FacesContextBridge.getCurrentInstance();
      } 
      catch (NullPointerException e)
      {
         throw new IllegalStateException("No JSF request has been made for this session.");
      }
      
      Map sessionMap = facesContext.getExternalContext().getSessionMap();   
      JSFTimer timer = (JSFTimer)sessionMap.get(SESSION_KEY);
      
      if (timer == null)
      {
         timer = new JSFTimer();
         sessionMap.put(SESSION_KEY, timer);
      }
      
      return timer;
   }
   
   /**
    * Returns the total time spent in the JSF lifecycle.
    *
    * @return The total time in milliseconds.  Returns -1 if no phase has been completed.
    */
   public long getTotalTime()
   {
      long firstTimeStamp = Long.MAX_VALUE;
      for (Iterator<Long> i = beforeMap.values().iterator(); i.hasNext();)
      {
         long time = i.next().longValue();
         if (time < firstTimeStamp) firstTimeStamp = time;
      }
      
      long lastTimeStamp = Long.MIN_VALUE;
      for (Iterator<Long> i = afterMap.values().iterator(); i.hasNext();)
      {
         long time = i.next().longValue();
         if (time > lastTimeStamp) lastTimeStamp = time;
      }
      
      if ((firstTimeStamp == Long.MAX_VALUE) || (lastTimeStamp == Long.MIN_VALUE))
      {
         return -1L;
      }
      
      return lastTimeStamp - firstTimeStamp;
   }
   
   /**
    * Returns the amount of time spent during a JSF phase.
    *
    * @param phaseId The PhaseId.
    *
    * @return The time in milliseconds, or zero if the phase has not run.
    *
    * @throws IllegalArgumentException if the phaseId is ANY_PHASE.
    */
   public long getPhaseTime(PhaseId phaseId)
   {
      if (phaseId == PhaseId.ANY_PHASE) 
      {
         throw new IllegalArgumentException("PhaseId.ANY_PHASE is not valid.");
      }
      
      Long beforeTime = beforeMap.get(phaseId);
      Long afterTime = afterMap.get(phaseId);
      
      if ((beforeTime == null) || (afterTime == null)) return 0L;
      
      return afterTime.longValue() - beforeTime.longValue();
   }
   
}
