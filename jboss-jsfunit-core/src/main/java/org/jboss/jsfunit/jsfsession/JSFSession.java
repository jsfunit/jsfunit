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
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import org.jboss.jsfunit.framework.WebConversationFactory;
import org.w3c.dom.Element;

/**
 *
 * @author Stan Silvert
 */
public class JSFSession
{
   private JSFServerSession jsfServerSession;
   private JSFClientSession jsfClientSession;
   private WebClient webClient;
   
   public JSFSession(String initialPage) throws IOException
   {
      this.webClient = WebConversationFactory.makeWebClient();
      JSFUnitPageCreator pageCreator = new JSFUnitPageCreator(this.webClient);
      this.jsfServerSession = new JSFServerSession();
      pageCreator.addPageCreationListener(this.jsfServerSession);
      this.jsfClientSession = new JSFClientSession(this.jsfServerSession);
      pageCreator.addPageCreationListener(this.jsfClientSession);
      
      String url = WebConversationFactory.getWARURL() + initialPage;
      webClient.getPage(url);
   }
   
   public WebClient getWebClient()
   {
      return this.webClient;
   }
   
   
   public JSFServerSession getJSFServerSession()
   {
      return this.jsfServerSession;
   }
   
   public JSFClientSession getJSFClientSession()
   {
      return this.jsfClientSession;
   }

}
