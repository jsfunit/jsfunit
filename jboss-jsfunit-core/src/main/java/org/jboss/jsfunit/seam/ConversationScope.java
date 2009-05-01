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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.jboss.jsfunit.framework.WebConversationFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.contexts.Context;
import org.jboss.seam.contexts.ServerConversationContext;

/**
 * This class manages Seam conversation scope objects that are cached in the
 * HttpSession.  This makes the objects available to JSFUnit tests through EL
 * expressions that start with "seamconversation."
 *
 * @author Stan Silvert
 * @since 1.0
 */
public class ConversationScope 
{
   // don't allow instance of static class
   private ConversationScope() {}
   
   static Map getConversationCache()
   {
      Map convCache = new HashMap();
      
      String convId = findConversationId();
      if (convId == null) return convCache;
      
      Context conversationContext = new ServerConversationContext(sessionMap(), convId);
      for (String name : conversationContext.getNames() )
      {
         Object value = conversationContext.get(name);
         convCache.put(name, value);
      } 
      
      return convCache;
   }
     
   // figure out the conversationId based on attributes in the session
   private static String findConversationId()
   {
      HttpSession session = WebConversationFactory.getSessionFromThreadLocal();
      for (Enumeration names = session.getAttributeNames(); names.hasMoreElements();)
      {
         String name = (String)names.nextElement();
         if (name.startsWith(ScopeType.CONVERSATION.getPrefix()))
         {
            String conversationKey = name.substring(ScopeType.CONVERSATION.getPrefix().length() + 1);
            return conversationKey.substring(0, conversationKey.indexOf('$'));
         }
      }
      
      return null;
   }
   
   // need map from true HttpSession that I can pass to ServerConversationContext
   private static Map sessionMap()
   {
      HttpSession session = WebConversationFactory.getSessionFromThreadLocal();
      Map sessionMap = new HashMap();
      for (Enumeration names = session.getAttributeNames(); names.hasMoreElements();)
      {
         String name = (String)names.nextElement();
         sessionMap.put(name, session.getAttribute(name));
      }
      
      return sessionMap;
   }
}
