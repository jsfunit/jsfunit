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

package org.jboss.jsfunit.a4jsupport;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * There are two situations where you need to create an AjaxEvent.  The first is
 * when you want to add your own parameters to the AJAX request.  This is needed
 * if you use the a4j:jsFunction tag and you need to set param1, param2, etc.
 * <br/><br/>
 * The second situation is when you need to supress the automatic refresh of the
 * page.  Because AJAX events don't fully render the page, the client and the server 
 * state can become out of sync.  So if the JSF viewID did not change 
 * (i.e. you didn't go to a new page) change then a second request will be sent 
 * to sync up the client and server state.    
 * <br/><br/>
 * However, the refresh itself has one known side-effect in that 
 * FacesMessages will go away on both client and server side.  So if you need
 * to test a FacesMessage after calling Ajax4jsfClient.fireAjaxEvent(), set 
 * refresh to <code>false</code>, then test for the message, and call 
 * Ajax4jsfClient.doRefresh() manually.
 *
 * @author ssilvert
 */
public class AjaxEvent
{
   private String componentID;
   private boolean refresh = true;
   private Map<String, String> extraReqParams = new HashMap<String, String>();

   /**
    * Create a new AjaxEvent
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    */
   public AjaxEvent(String componentID)
   {
      if (componentID == null)
      {
         throw new NullPointerException("componentID can not be null");
      }
      
      this.componentID = componentID;
   }
   
   /**
    * Get the component ID of the target AJAX component.
    *
    * @return The component ID.
    */
   public String getComponentID()
   {
      return this.componentID;
   }
   
   /**
    * Get the refresh flag.  See the javadoc of this class.
    *
    * @return The refresh flag.
    */
   public boolean getRefresh()
   {
      return refresh;
   }
   
   /**
    * Set the refresh flag.  See the javadoc of this class.
    *
    * @param refresh The refresh flag.
    */
   public void setRefresh(boolean refresh)
   {
      this.refresh = refresh;
   }
   
   /**
    * Add an extra request param to the AJAX request.  Note that the name is
    * not a component ID.  It is a simple name/value pair.
    *
    * @param name The name of the request param
    * @param value The value.
    */
   public void setExtraRequestParam(String name, String value)
   {
      if (name == null) throw new NullPointerException("name can not be null");
      if (value == null) throw new NullPointerException("value can not be null");
      
      this.extraReqParams.put(name, value);
   }
   
   /**
    * Get the extra request params.
    *
    * @return An unmodifiable map of the name/value pairs.
    */
   public Map<String, String> getExtraRequestParams()
   {
      return Collections.unmodifiableMap(this.extraReqParams);
   }
}
