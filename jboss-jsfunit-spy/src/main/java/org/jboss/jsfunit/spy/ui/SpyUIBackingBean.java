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

package org.jboss.jsfunit.spy.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.jboss.jsfunit.spy.data.RequestData;
import org.jboss.jsfunit.spy.data.Scope;
import org.jboss.jsfunit.spy.data.Session;
import org.jboss.jsfunit.spy.data.Snapshot;
import org.jboss.jsfunit.spy.data.SpyManager;

/**
 *
 * @author Stan Silvert
 * @since 1.1
 */
public class SpyUIBackingBean {

    private Session selectedSession;
    private RequestData selectedRequestData;

    public Session getSelectedSession()
    {
       return this.selectedSession;
    }
    
    public RequestData getSelectedRequestData()
    {
       return this.selectedRequestData;
    }
    
    /**
     * JSF Action method.
     * 
     * @return 'requestdataview'
     */
    public String selectRequestData()
    {
      this.selectedRequestData = this.selectedSession.getRequests().get(findSequenceNumber());
      return "requestdataview";
    }
    
    /**
     * JSF Action method.
     * 
     * @return 'sessionview'
     */
    public String selectSession()
    {
       this.selectedSession = findSelectedSession();
       return "sessionview";
    }
    
    private int findSequenceNumber()
    {
       return Integer.parseInt(getRequestParam("selectedRequestData"));
    }
    
    private String getRequestParam(String param)
    {
       FacesContext facesContext = FacesContext.getCurrentInstance();
       ExternalContext extCtx = facesContext.getExternalContext();
       return extCtx.getRequestParameterMap().get(param);
    }
    
    private Session findSelectedSession()
    {
       String sessionId = getRequestParam("selectedSession");
       SpyManager spyMgr = SpyManager.getInstance();
       return spyMgr.getSession(sessionId);
    }
    
    public List<PhaseValues> getRequestScopeValues()
    {
       return getScopedValues(Scope.REQUEST);
    }
    
    public List<PhaseValues> getSessionScopeValues()
    {
       return getScopedValues(Scope.SESSION);
    }
       
    public List<PhaseValues> getApplicationScopeValues()
    {
       return getScopedValues(Scope.APPLICATION);
    }
    
    private List<PhaseValues> getScopedValues(Scope scope)
    {
       Map<String, PhaseValues> valuesMap = new LinkedHashMap<String, PhaseValues>();
       
       if (this.selectedRequestData == null)
       {
          return new ArrayList<PhaseValues>(0);
       }
       
       for (Snapshot snapshot : this.selectedRequestData.getSnapshots())
       {
          for (Iterator<String> keys = snapshot.getScopeMap(scope).keySet().iterator(); keys.hasNext();)
          {
             String key = keys.next();
             if (!valuesMap.containsKey(key))
             {
                valuesMap.put(key, new PhaseValues(scope, key, this.selectedRequestData.getSnapshots()));
             }
          }
       }
       
       return new ArrayList(valuesMap.values());
    }
    
    /**
     *  Returns the default time zone for the server.
     * 
     * @return The default time zone for the server.
     */
    public TimeZone getTimeZone()
    {
       return TimeZone.getDefault();
    }
}
