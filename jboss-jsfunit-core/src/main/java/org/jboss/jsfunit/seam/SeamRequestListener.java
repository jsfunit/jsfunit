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

import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.jboss.jsfunit.framework.FacesContextBridge;
import org.jboss.jsfunit.framework.RequestListener;
import org.jboss.seam.contexts.ServletLifecycle;
import org.jboss.seam.core.ConversationPropagation;
import org.jboss.seam.core.Manager;
import org.jboss.seam.servlet.ServletRequestSessionMap;
import org.jboss.seam.web.ServletContexts;

/**
 * This listener prepares the Seam environment to be compatible with JSFUnit.
 *
 * After each request, the conversation scope is promoted so that it will be
 * available to JSFUnit tests.  Before each request, conversation scope is
 * demoted to its normal pre-request state so that Seam applications will
 * function normally.
 *
 * @author Stan Silvert
 */
public class SeamRequestListener implements RequestListener
{
   private boolean iPromotedTheConversation = false;
   private boolean iRestoredTheConversation = false;
   
   public void beforeRequest(WebRequestSettings webRequestSettings)
   {
      demoteConversation();
      tearDownConversation();
   }

   public void afterRequest(WebResponse webResponse)
   {
      promoteConversation();
      restoreConversation();
   }
   
   // code copied from org.jboss.seam.servlet.ContextualHttpServletRequest
   private void restoreConversation()
   {
      HttpServletRequest request = httpServletRequest();
      ServletLifecycle.beginRequest(request);
      ServletContexts.instance().setRequest(request);
      ConversationPropagation.instance().restoreConversationId( request.getParameterMap() );
      Manager.instance().restoreConversation();
      ServletLifecycle.resumeConversation(request);
      Manager.instance().handleConversationPropagation( request.getParameterMap() );
      
      this.iRestoredTheConversation = true;
   }
   
   // code copied from org.jboss.seam.servlet.ContextualHttpServletRequest
   private void tearDownConversation()
   {
      if (!this.iRestoredTheConversation) return;
      
      HttpServletRequest request = httpServletRequest();
      Manager.instance().endRequest( new ServletRequestSessionMap(request)  );
      ServletLifecycle.endRequest(request);
      
      this.iRestoredTheConversation = false;
   }
   
   private void demoteConversation()
   {
      if (this.iPromotedTheConversation)
      {
         Manager.instance().setLongRunningConversation(false);
         this.iPromotedTheConversation = false;
      }
   }
   
   private void promoteConversation()
   {
      try
      {
         if (!Manager.instance().isLongRunningConversation())
         {
            Manager.instance().setLongRunningConversation(true);
            this.iPromotedTheConversation = true;
         }
      }
      catch (IllegalStateException e)
      {
         // ignore
      }
   }
   
   private HttpServletRequest httpServletRequest()
   {
      FacesContext facesContext = FacesContextBridge.getCurrentInstance();
      return (HttpServletRequest)facesContext.getExternalContext().getRequest();
   }
   
}
