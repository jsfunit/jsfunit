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

package org.jboss.jsfunit.context;

import java.util.Iterator;
import javax.el.ELContext;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * This class is a wrapper for the "real" FacesContext.
 *
 * @author Stan Silvert
 */
public class JSFUnitFacesContext extends FacesContext implements HttpSessionBindingListener
{
   public static final String SESSION_KEY = JSFUnitFacesContext.class.getName() + ".sessionkey";
   
   // This is the wrapped FacesContext instance.
   private FacesContext delegate;
   
   // initialized after the JSF lifecycle is over
   private ExternalContext extContext = null;
   
   public JSFUnitFacesContext(FacesContext delegate, Object request)
   {
      this.delegate = delegate;
      
      HttpServletRequest req = (HttpServletRequest)request;
      req.getSession().setAttribute(SESSION_KEY, this);
      
      setCurrentInstance(this);
   }
   
   public Iterator getMessages(String string)
   {
      return delegate.getMessages(string);
   }
   
   public void addMessage(String string, FacesMessage facesMessage)
   {
      delegate.addMessage(string, facesMessage);
   }
   
   public void setResponseWriter(ResponseWriter responseWriter)
   {
      delegate.setResponseWriter(responseWriter);
   }
   
   public void setResponseStream(ResponseStream responseStream)
   {
      delegate.setResponseStream(responseStream);
   }
   
   public void setViewRoot(UIViewRoot uIViewRoot)
   {
      delegate.setViewRoot(uIViewRoot);
   }
   
   public void responseComplete()
   {
      delegate.responseComplete();
   }
   
   public void renderResponse()
   {
      delegate.renderResponse();
   }
   
   public Application getApplication()
   {
      return delegate.getApplication();
   }
   
   public Iterator getClientIdsWithMessages()
   {
      return delegate.getClientIdsWithMessages();
   }
   
   public ExternalContext getExternalContext()
   {
      if (this.extContext == null)
      {
         return delegate.getExternalContext();
      }
      
      return this.extContext;
   }
   
   public FacesMessage.Severity getMaximumSeverity()
   {
      return delegate.getMaximumSeverity();
   }
   
   public Iterator getMessages()
   {
      return delegate.getMessages();
   }
   
   public RenderKit getRenderKit()
   {
      return delegate.getRenderKit();
   }
   
   public boolean getRenderResponse()
   {
      return delegate.getRenderResponse();
   }
   
   public boolean getResponseComplete()
   {
      return delegate.getResponseComplete();
   }
   
   public ResponseStream getResponseStream()
   {
      return delegate.getResponseStream();
   }
   
   public ResponseWriter getResponseWriter()
   {
      return delegate.getResponseWriter();
   }
   
   public UIViewRoot getViewRoot()
   {
      return delegate.getViewRoot();
   }
   
   /**
    * This is called when the JSF lifecycle is over.  After this is called,
    * most operations will still delegate to the wrapped FacesContext, but
    * the ExternalContext will be replaced with a JSFUnitExternalContext.
    *
    * This method does not call release() on the wrapped FacesContext.  So, all
    * of its state is retained for use by JSFUnit tests.
    */
   public void release()
   {
      this.extContext = new JSFUnitExternalContext(delegate.getExternalContext());
   }
   
   /**
    * This allows the FacesContextBridge to associate the JSFUnitFacesContext
    * with the thread running the tests.
    */
   public void setInstanceToJSFUnitThread()
   {
      setCurrentInstance(this);
   }
   
   public ELContext getELContext()
   {
      ELContext elContext = delegate.getELContext();
      
      // if JSF lifecycle is over we are using the JSFUnitFacesContext
      // instead of the delegate.  So we need to replace it in ELContext
      if (this.extContext != null)
      {
         elContext.putContext(FacesContext.class, this);
      }
      
      return elContext;
   }
   
   /**
    * Clean up the previous FacesContext.
    */
   public void valueUnbound(HttpSessionBindingEvent httpSessionBindingEvent)
   {
      delegate.release();
   }
   
   public void valueBound(HttpSessionBindingEvent httpSessionBindingEvent)
   {
      // do nothing
   }
   
}
