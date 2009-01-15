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

package org.jboss.jsfunit.example.ajax4jsf;

import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlDataTable;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

/**
 * Peform JSFUnit tests on three of the Ajax4jsf demo applications.
 *
 * @author Stan Silvert
 */
public class A4JTest extends ServletTestCase
{
   
   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      return new TestSuite( A4JTest.class );
   }

   /**
    * Test the Echo demo page.
    */
   public void testEcho() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/pages/echo.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      // Note: input_text is session scoped
      client.type("input_text", 'f');
      client.type("input_text", 'o');
      client.type("input_text", 'o');
      assertEquals("foo", server.getManagedBeanValue("#{textbean.text}"));
      
      // simulate hitting the "b" key on the field
      client.type("input_text", 'b');
      assertEquals("foob", server.getManagedBeanValue("#{textbean.text}"));
      
      client.setValue("input_text_request_scope", "foo");
      assertEquals("foo", server.getComponentValue("input_text_request_scope"));
      assertEquals("foo", 
                  server.getManagedBeanValue("#{request_scope_textbean.text}"));
   }
   
   private void clickOption(JSFClientSession client, String optionValue) throws IOException
   {
      HtmlPage page = (HtmlPage)client.getContentPage();
      ClickableElement clickable = (ClickableElement)page.getByXPath("//option[@value='" + optionValue + "']").get(0);
      clickable.click(); // click to select
      client.click("list"); // click list to fire event
   }
   /**
    * Test the Selection List demo
    */
   public void testSelectionList() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/list.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      clickOption(client, "Lott, Charlie");
      assertEquals("Mr.", 
               server.getManagedBeanValue("#{userList.currentUser.prefix}"));
      assertEquals("Charlie", 
               server.getManagedBeanValue("#{userList.currentUser.firstName}"));
      assertEquals("Lott", 
               server.getManagedBeanValue("#{userList.currentUser.lastName}"));
      assertEquals("8888 Spartan Rd. Washington D.C., VA 70938-3445", 
               server.getManagedBeanValue("#{userList.currentUser.address}"));
      assertEquals("Talk Radio Host", 
               server.getManagedBeanValue("#{userList.currentUser.jobTitle}"));
      
      clickOption(client, "Story, Leslie");
      assertEquals("Mrs.", 
               server.getManagedBeanValue("#{userList.currentUser.prefix}"));
      assertEquals("Leslie", 
               server.getManagedBeanValue("#{userList.currentUser.firstName}"));
      assertEquals("Story", 
               server.getManagedBeanValue("#{userList.currentUser.lastName}"));
      assertEquals("834 Thomas Road Atlanta, GA 72890-3423", 
               server.getManagedBeanValue("#{userList.currentUser.address}"));
      assertEquals("Ajax Evangelist", 
               server.getManagedBeanValue("#{userList.currentUser.jobTitle}"));
   }
   
   /**
    * Test the Repeat Rerender demo.
    *
    * The interesting thing about this one from a JSFUnit perspective is 
    * the references to indexed components.  Any time you refer to a component
    * that is inside a JSF DataModel, you use the JSF-generated index
    * added to the ID of the component.  Do a "view source" on the demo 
    * page to see how the full ID is generated.  
    *
    * The JSFUnit API will always find the component based on the suffix you 
    * pass in.  In this case, there are many components that end with
    * "command_link_up".  But there is only one that ends with 
    * "0:command_link_up" or "8:command_link_up".
    */
   public void testRepeatRerender() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/pages/a4j-repeat-rerender.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.click("0:command_link_up");
      assertEquals(1, server.getManagedBeanValue("#{bean.requestCounter}"));
      assertEquals(1, server.getManagedBeanValue("#{bean.collection[0]}"));
      
      client.click("1:command_link_down");
      client.click("1:command_link_down"); // do this one twice
      assertEquals(3, server.getManagedBeanValue("#{bean.requestCounter}"));
      assertEquals(-2, server.getManagedBeanValue("#{bean.collection[1]}"));
      
      client.click("2:command_link_up");
      assertEquals(4, server.getManagedBeanValue("#{bean.requestCounter}"));
      assertEquals(1, server.getManagedBeanValue("#{bean.collection[2]}"));
      
      client.click("8:command_link_up");
      assertEquals(5, server.getManagedBeanValue("#{bean.requestCounter}"));
      assertEquals(1, server.getManagedBeanValue("#{bean.collection[8]}"));
   }
   
   public void testReferenceToAFacet() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/pages/a4j-repeat-rerender.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.setValue("facettext", "foo");
      
      client.click("0:command_link_up");
      assertEquals(1, server.getManagedBeanValue("#{bean.requestCounter}"));
      assertEquals(1, server.getManagedBeanValue("#{bean.collection[0]}"));
   }

   // JSFUNIT-186
   public void testGetComponentValueInDatatable() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/pages/a4j-repeat-rerender.jsf");
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      HtmlDataTable table = (HtmlDataTable)server.findComponent("repeat");
      for (int i=0; i < table.getRowCount(); i++)
      {
         int rowIndex = table.getRowIndex();
         
         String id = "repeat:" + i + ":item";
         ValueHolder component = (ValueHolder)server.findComponent(id);
         assertNull(component.getLocalValue());
         assertNull(component.getValue());
         
         assertNotNull(server.getComponentValue(id));
         
         // make sure rowIndex was not corrupted
         assertEquals(rowIndex, table.getRowIndex());
      }
   }
   
   // JSFUNIT-186
   public void testGetComponentValueInNestedDatatable() throws IOException
   {
      //System.out.println("*************************");
      JSFSession jsfSession = new JSFSession("/pages/a4j-repeat-rerender.jsf");
      JSFServerSession server = jsfSession.getJSFServerSession();
      JSFClientSession client = jsfSession.getJSFClientSession();
      client.click("1:command_link_up");
      client.click("1:command_link_up");
      
      HtmlDataTable table = (HtmlDataTable)server.findComponent("repeat");
      HtmlDataTable nestedtable = (HtmlDataTable)server.findComponent("1:nestedtable");
      for (int i=0; i < nestedtable.getRowCount(); i++)
      {
         int rowIndex = table.getRowIndex();
         int nestedRowIndex = nestedtable.getRowIndex();
         
         String id = "1:nestedtable:" + i + ":itemnested";
         ValueHolder component = (ValueHolder)server.findComponent(id);
         assertNull(component.getLocalValue());
         //assertNull(component.getValue());
        // if (i==1) System.out.println("componentValue for " + id + "=" + server.getComponentValue(id));
        // if (i==1) System.out.println("class=" + server.getComponentValue(id).getClass().getName());
         assertNotNull(server.getComponentValue(id));
         
         if (i == 1) assertEquals(new Long(4), server.getComponentValue(id));
         
         // make sure rowIndex was not corrupted
         assertEquals(rowIndex, table.getRowIndex());
         assertEquals(nestedRowIndex, nestedtable.getRowIndex());
      }
      //System.out.println("******************************");
   }
   
   /**
    * Test for http://jira.jboss.com/jira/browse/JSFUNIT-56
    */
   public void testCommandLinkWithLongParam() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/pages/a4j-repeat-rerender_JSFUNIT-56.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      
      client.click("0:fparam_command_link_up");
   }
   
   public void testA4JRedirect() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/index.jsf");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
      client.click("redirect");
      assertEquals("/pages/echo.xhtml", server.getCurrentViewID());
      testEcho(); // make sure I can continue
   }
}
