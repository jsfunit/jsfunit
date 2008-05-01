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

package org.jboss.jsfunit.jsfsession;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.PageCreator;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Stan Silvert
 */
public class JSFUnitPageCreator implements PageCreator
{
   private PageCreator delegate;
   
   private List<PageCreationListener> listeners = new ArrayList<PageCreationListener>(2);
   
   public JSFUnitPageCreator(WebClient webClient)
   {
      if (webClient == null) throw new NullPointerException();
      this.delegate = webClient.getPageCreator();
      webClient.setPageCreator(this);
   }
   
   public Page createPage(WebResponse webResponse, WebWindow webWindow) throws IOException
   {
      Page page = this.delegate.createPage(webResponse, webWindow);
      notifyListeners(page);
      return page;
   }
   
   private void notifyListeners(Page page)
   {
      for (Iterator<PageCreationListener> i = listeners.iterator(); i.hasNext();)
      {
         PageCreationListener listener = i.next();
         listener.pageCreated(page);
      }
   }
   
   public void addPageCreationListener(PageCreationListener listener)
   {
      if (listener == null) throw new NullPointerException();
      this.listeners.add(listener);
   }
   
   public void removePageCreationListener(PageCreationListener listener)
   {
      this.listeners.remove(listener);
   }
}
