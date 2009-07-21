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
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.jboss.jsfunit.spy.data.RequestData;
import org.jboss.jsfunit.spy.data.Scope;
import org.jboss.jsfunit.spy.data.Session;
import org.jboss.jsfunit.spy.data.Snapshot;
import org.jboss.jsfunit.spy.data.SpyManager;
import org.jboss.jsfunit.spy.seam.SeamUtil;

/**
 *
 * @author Stan Silvert
 * @since 1.1
 */
public class SpyUIBackingBean {

    private Session selectedSession;
    private RequestData selectedRequestData;
    private boolean showAllPhases = false;

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
     * @return 'scopesview'
     */
    public String scopesView()
    {
      this.selectedRequestData = this.selectedSession.getRequests().get(findSequenceNumber());
      return "scopesview";
    }
    
    /**
     * JSF Action method.
     * 
     * @return 'scopesview'
     */
    public String showAllPhases()
    {
      this.showAllPhases = true;
      return "scopesview";
    }
    
    /**
     * JSF Action method.
     * 
     * @return 'scopesview'
     */
    public String showFirstAndLastPhase()
    {
      this.showAllPhases = false;
      return "scopesview";
    }
    
    public boolean getShowAllPhasesFlag()
    {
       return this.showAllPhases;
    }
    
    /**
     * JSF Action method.
     * 
     * @return 'httprequestview'
     */
    public String httprequestView()
    {
      this.selectedRequestData = this.selectedSession.getRequests().get(findSequenceNumber());
      return "httprequestview";
    }
    
    /**
     * JSF Action method.
     * 
     * @return 'perfview'
     */
    public String perfView()
    {
      this.selectedRequestData = this.selectedSession.getRequests().get(findSequenceNumber());
      return "perfview";
    }
    
    public String facesMessagesView()
    {
      this.selectedRequestData = this.selectedSession.getRequests().get(findSequenceNumber());
      return "facesmessagesview";
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
    
    public List<Map.Entry<String, Object>> getApplicationScopeSimpleView()
    {
       ExternalContext extCtx = FacesContext.getCurrentInstance().getExternalContext();
       return new ArrayList<Map.Entry<String, Object>>(extCtx.getApplicationMap().entrySet());
    }
    
    public List<KeyValuePair> getServletContextInitParameters()
    {
       ExternalContext extCtx = FacesContext.getCurrentInstance().getExternalContext();
       ServletContext servletContext = (ServletContext)extCtx.getContext();
       ArrayList<KeyValuePair> pairs = new ArrayList<KeyValuePair>();
       for (Enumeration i = servletContext.getInitParameterNames(); i.hasMoreElements(); )
       {
          String key = (String)i.nextElement();
          pairs.add(new KeyValuePair(key, servletContext.getInitParameter(key)));
       }
       
       return pairs;
       //return new ArrayList<Map.Entry<String, Object>>(extCtx.getInitParameterMap().entrySet());
    }
    
    public List<Map.Entry<String, String>> getRegisteredValidators()
    {
       //System.out.println("**********************************");
       
       Application application = FacesContext.getCurrentInstance().getApplication();
       Set<Map.Entry<String, String>> validators = application.getDefaultValidatorInfo().entrySet();
       //System.out.println("validators=" + validators);
       //System.out.println("*********************************");
       return new ArrayList<Map.Entry<String, String>>(validators);
    }
    
    public List<String> getRegisteredComponents()
    {
       List<String> componentTypes = new ArrayList<String>();
       Application application = FacesContext.getCurrentInstance().getApplication();
       for (Iterator<String> i = application.getComponentTypes(); i.hasNext();)
       {
          String type = i.next();
          componentTypes.add(type);
       }
      
       Collections.sort(componentTypes);
       return componentTypes;
    }
    
    public static class KeyValuePair {
       String key, value;
       KeyValuePair(String key, String value) {
          this.key = key;
          this.value = value;
       }
       public String getKey() { return key; }
       public String getValue() {return value; }
    }
    
    public List<PhaseValues> getConversationScopeValues()
    {
       return getScopedValues(Scope.CONVERSATION);
    }
    
    public List<CustomScopeView> getCustomScopeViews()
    {
       List<CustomScopeView> customScopeView = new ArrayList();
       for (String customScopeName : getCustomScopeNames())
       {
          customScopeView.add(new CustomScopeView(customScopeName, getCustomScopedValues(customScopeName)));
       }
       
       return customScopeView;
    }
    
    private Set<String> getCustomScopeNames()
    {
       Set<String> scopeNames = new HashSet<String>();
       for (Snapshot snapshot : this.selectedRequestData.getSnapshots())
       {
         for (String scopeName : snapshot.getCustomScopes().keySet()) 
         {
            scopeNames.add(scopeName);
         }
       }
       
       return scopeNames;
    }
    
    private List<PhaseValues> getCustomScopedValues(String customScopeName)
    {
       if (this.selectedRequestData == null)
       {
          return new ArrayList<PhaseValues>(0);
       }
       
       Map<String, PhaseValues> valuesMap = new LinkedHashMap<String, PhaseValues>();
       for (Snapshot snapshot : this.selectedRequestData.getSnapshots())
       {
          Map<String, String> customScope = snapshot.getCustomScopes().get(customScopeName);
          if (customScope == null) continue; // scope destroyed in middle of lifecycle
          
          for (Iterator<String> keys = customScope.keySet().iterator(); keys.hasNext();)
          {
             String key = keys.next();
             if (!valuesMap.containsKey(key))
             {
                valuesMap.put(key, new PhaseValues(customScopeName, key, this.selectedRequestData.getSnapshots()));
             }
          }
       }
       
       return new ArrayList(valuesMap.values());
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
          for (Iterator<String> keys = snapshot.getScope(scope).keySet().iterator(); keys.hasNext();)
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
    
    public boolean isSeamPresent()
    {
       return SeamUtil.isSeamPresent();
    }
    
    public String getSelectedSessionRowFormat()
    {
       return makeRowFormat(getSelectedSession(), 
                            SpyManager.getInstance().getSessions());
    }
    
    public String getSelectedRequestRowFormat()
    {
       return makeRowFormat(getSelectedRequestData(), 
                            getSelectedSession().getRequests());
    }
    
    private String makeRowFormat(Object selected, List rowData)
    {
       if (selected == null) return "";
       if (rowData == null) return "";
       if (rowData.size() < 2) return "";
       
       StringBuilder formatString = new StringBuilder();
       for (Iterator rows = rowData.iterator(); rows.hasNext();)
       {
          Object row = rows.next();
          if (row == selected) formatString.append("highlight");
          if (row != selected) formatString.append("noop");
          if (rows.hasNext()) formatString.append(",");
       }
       
       return formatString.toString();
    }
    
    public boolean[][] getRenderScopeColumns()
    {
       int totalPhases = 6;
       Snapshot firstSnap = getSelectedRequestData().getFirstSnapshot();
       Snapshot lastSnap = getSelectedRequestData().getLastSnapshot();
       
       boolean[][] renderColumn = new boolean[totalPhases][2];
       for (int i = 0; i < totalPhases; i++) 
       {
          for (int j = 0; j < 2; j++)
          {
             if (this.showAllPhases) 
             {
               renderColumn[i][j] = true;
               continue;
             }
             
             if ((firstSnap.getBeforeOrAfter().ordinal() == j) &&
                 (firstSnap.getPhaseId().getOrdinal() == (i+1)) )
             {
                renderColumn[i][j] = true;
             }
             
             if ((lastSnap.getBeforeOrAfter().ordinal() == j) &&
                 (lastSnap.getPhaseId().getOrdinal() == (i+1)) )
             {
                renderColumn[i][j] = true;
             }
          }
       }
       
       return renderColumn;
    }
}
