/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.jsfunit.seam;

import java.util.HashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.jboss.seam.contexts.Context;
import org.jboss.seam.contexts.Contexts;

/**
 * This class manages Seam conversation scope objects that are cached in the
 * HttpSession.  This makes the objects available to JSFUnit tests through EL
 * expressions that start with "seamconversation."
 *
 * All methods in this class have default scope.  Application and test code
 * should not access this class directly.
 * 
 * @author Stan Silvert
 */
public class ConversationScope 
{
   private static final String SEAM_CONVERSATION_CACHE = JSFUnitLifecycle.class.getName() + ".SEAM_CONVERSATION_CACHE";
   
   // don't allow instance of static class
   private ConversationScope() {}
   
   static Map convCache(FacesContext context)
   {
      Map sessionMap = (Map)context.getExternalContext().getSessionMap();
      Map convMap = (Map)sessionMap.get(SEAM_CONVERSATION_CACHE);
      if (convMap == null)
      {
         convMap = new HashMap();
         sessionMap.put(SEAM_CONVERSATION_CACHE, convMap);
      }
      
      return convMap;
   }
   
   static Object get(FacesContext context, String name)
   {
      return convCache(context).get(name);
   }
   
   static void cache(FacesContext context)
   {
      Map convCache = convCache(context);
      
      Context convContext = Contexts.getConversationContext();
      if (convContext == null) return;
      
      String[] names = convContext.getNames();
      for (int i=0; i < names.length; i++)
      {
         convCache.put(names[i], convContext.get(names[i]));
      }
   }
   
}
