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

package org.jboss.jsfunit.framework;

import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The JSFUnitWebConnection wraps the HtmlUnit WebConnection.  It allows
 * listeners to register for events before and after any HTTP request on 
 * the client side.
 *
 * @author Stan Silvert
 * @since 1.0
 */
public class JSFUnitWebConnection implements WebConnection
{
   private WebConnection wrappedConnection;
   
   private List<RequestListener> listeners = new ArrayList<RequestListener>();
   
   /**
    * Create a new JSFUnitWebConnection
    *
    * @param wrappedConnection The wrappedConnection (normally obtained from 
    *                          the HtmlUnit WebClient
    */
   public JSFUnitWebConnection(WebConnection wrappedConnection)
   {
      this.wrappedConnection = wrappedConnection;
   }

   /**
    * Called by HtmlUnit whenever a request is made to the server.
    *
    * @param webRequestSettings The WebRequestSettings
    *
    * @return The WebResponse
    *
    * @throws IOException
    */
   public WebResponse getResponse(final WebRequestSettings webRequestSettings) throws IOException
   {
      notifyListenersBefore(webRequestSettings);
      WebResponse response = null;
      
      try
      {
         response = this.wrappedConnection.getResponse(webRequestSettings);
      }
      catch (IOException ioe)
      {
         throw ioe;
      }
      finally
      { 
         notifyListenersAfter(response);
      }
      
      return response;
   }
   
   private void notifyListenersBefore(WebRequestSettings webRequestSettings)
   {
      for (Iterator<RequestListener> i = this.listeners.iterator(); i.hasNext();)
      {
         i.next().beforeRequest(webRequestSettings);
      }
   }
   
   private void notifyListenersAfter(WebResponse response)
   {
      for (Iterator<RequestListener> i = this.listeners.iterator(); i.hasNext();)
      {
         i.next().afterRequest(response);
      }
   }
   
   /**
    * Add a RequestListener to be notified whenever an HTTP request is made
    * to the server.
    *
    * @param listener The listener to register
    */
   public void addListener(RequestListener listener)
   {
      this.listeners.add(listener);
   }
   
   /**
    * Remove a RequestListener.
    *
    * @param listener The listener to unregister
    */
   public void removeListener(RequestListener listener)
   {
      this.listeners.remove(listener);
   }
   
}
