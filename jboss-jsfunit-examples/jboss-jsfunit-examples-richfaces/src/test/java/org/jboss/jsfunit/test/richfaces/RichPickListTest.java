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
package org.jboss.jsfunit.test.richfaces;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * Peform JSFUnit tests on RichFaces demo application.
 *
 * @author Stan Silvert
 */
public class RichPickListTest extends ServletTestCase
{
   public static Test suite()
   {
      return new TestSuite( RichPickListTest.class );
   }

   public void testCopyAll() throws IOException
   { 
     /* JSFSession jsfSession = new JSFSession("/richfaces/pickList.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      client.getPageAsText();
      JSFServerSession server = jsfSession.getJSFServerSession();

      //List list = (List)server.getManagedBeanValue("#{pickListBean.result}");
      //assertEquals(0, list.size());
      
      HtmlPage htmlPage = (HtmlPage)client.getContentPage();
      HtmlForm form = (HtmlForm)htmlPage.getElementById("form1");
      for (Iterator i = form.getAllHtmlChildElements().iterator(); i.hasNext();)
      {
         Object element = i.next();
         if (element instanceof ClickableElement)
         {
            System.out.println("id=" + ((HtmlElement)element).getAttribute("id") + " | className=" + element.getClass().getName());
         }
      }
      ClickableElement firstState = (ClickableElement)htmlPage.getElementById("form1:pickList:source::1");
      firstState.dblClick();
      assertTrue(client.getPageAsText().contains("1 Options Choosen"));
      ClickableElement copyAllButton = (ClickableElement)htmlPage.getElementById("form1:pickListcopyAlllink");
      htmlPage.setFocusedElement(copyAllButton);
      copyAllButton.click();
      Integer items = (Integer)server.getManagedBeanValue("#{pickListBean.items}");
      assertEquals(50, items.intValue()); */
   }
   
}
