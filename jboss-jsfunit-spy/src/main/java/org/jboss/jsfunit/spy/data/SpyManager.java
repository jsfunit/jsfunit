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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PostConstructCustomScopeEvent;
import javax.faces.event.PreDestroyCustomScopeEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.servlet.http.HttpSession;

/**
 * The SpyManager is an application-scoped object that provides access to
 * information about every session and request of the JSF application.
 * 
 * It can be accessed through the getIntance() method or from the expression
 * language via #{spymanager}.
 *
 * @author Stan Silvert
 * @since 1.1
 */
public class SpyManager implements SystemEventListener
{

    public static final String EL_KEY = "spymanager";
    public static final String REQUEST_SEQUENCE_KEY = "jsfunit.spy.requestsequence";
    
    private Map<String, Session> sessions = new LinkedHashMap<String, Session>();
    
    void subscribeToSystemEvents()
    {
      Application application = FacesContext.getCurrentInstance().getApplication();
      application.subscribeToEvent(PostConstructCustomScopeEvent.class, this);
      application.subscribeToEvent(PreDestroyCustomScopeEvent.class, this);
    }
    
    public static SpyManager getInstance()
    {
       FacesContext facesContext = FacesContext.getCurrentInstance();
       SpyManager spyManager = (SpyManager)facesContext.getExternalContext().getApplicationMap().get(EL_KEY);
       if (spyManager == null) throw new IllegalStateException("SpyManager not initialized");
       return spyManager;
    }
    
    RequestData newRequest(FacesContext facesContext)
    {
       RequestData requestData = new RequestData(facesContext);

       Session session = getMySession();
       if (session == null) session = makeNewSession(facesContext);
       
       int sequenceNumber = session.addNewRequestData(requestData, facesContext);
       requestData.setSequenceNumber(sequenceNumber);
       
       Map attributeMap = facesContext.getExternalContext().getRequestMap();
       attributeMap.put(REQUEST_SEQUENCE_KEY, new Integer(sequenceNumber));
       
       return requestData;
    }
    
    void takeSnapshotBefore(PhaseEvent event)
    {
       RequestData  requestData = getCurrentRequest();
       if (requestData == null) 
       {
          requestData = newRequest(event.getFacesContext());
       }  
       
       requestData.takeSnapshotBefore(event);
    }
    
    void takeSnapshotAfter(PhaseEvent event)
    {
       getCurrentRequest().takeSnapshotAfter(event);
    }
    
    /**
     * Get the RequestData from the current JSF request.
     * 
     * @return The RequestData from the current request.  Return
     *         <code>null</code> if no Snapshot has been yet recorded for the
     *         current Request.
     */
    public RequestData getCurrentRequest()
    {
       Session session = getMySession();
       if (session == null) return null;
       
       Integer requestIndex = (Integer)FacesContext.getCurrentInstance()
                                                   .getExternalContext()
                                                   .getRequestMap()
                                                   .get(REQUEST_SEQUENCE_KEY);
       if (requestIndex == null) return null;
       
       return session.getRequests().get(requestIndex.intValue());
    }
    
    private synchronized Session makeNewSession(FacesContext facesContext)
    {
       Session session = new Session (facesContext);
       this.sessions.put(sessionId(facesContext), session);
       return session;
    }
    
    /**
     * Get all of the Sessions recorded by the SpyManager.
     * 
     * @return All the sessions.
     */
    public synchronized List<Session> getSessions()
    {
       return new ArrayList<Session>(sessions.values());
    }
    
    /**
     * Get the Session associated with the current thread.
     * 
     * @return The Session associated with the current thread.
     */
    public synchronized Session getMySession()
    {
       FacesContext facesContext = FacesContext.getCurrentInstance();
       return sessions.get(sessionId(facesContext));
    }
    
    public Session getSession(String sessionId)
    {
       return sessions.get(sessionId);
    }
    
    private static String sessionId(FacesContext facesContext)
    {
       HttpSession session = (HttpSession)facesContext.getExternalContext().getSession(true);
       return session.getId();
    }

   // ---- implementation of SystemEventListener. ------
   public boolean isListenerForSource(Object source)
   {
      return true;
   }

   public void processEvent(SystemEvent event) throws AbortProcessingException
   {
      Session session = getMySession();
      
      if (event instanceof PostConstructCustomScopeEvent) session.customScopeCreated((PostConstructCustomScopeEvent)event); 
      if (event instanceof PreDestroyCustomScopeEvent) session.customScopeDestroyed((PreDestroyCustomScopeEvent)event);
   }
   // ----------------------------------------------------
   
}
