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
 * AjaxEvent is used when you need to add your own parameters to the AJAX request.  
 * This is needed if you use the a4j:jsFunction tag and you need to set param1, 
 * param2, etc.
 *
 * @author ssilvert
 */
public class AjaxEvent
{
   private String componentID;
   private Map<String, String> extraReqParams = new HashMap<String, String>();

   /**
    * Create a new AjaxEvent with refresh <code>true</code>.
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
    * Add an extra request param to the AJAX request.  Note that the name is
    * not a component ID.  This is a simple name/value pair to be added as a
    * request param during the Ajax request.
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
