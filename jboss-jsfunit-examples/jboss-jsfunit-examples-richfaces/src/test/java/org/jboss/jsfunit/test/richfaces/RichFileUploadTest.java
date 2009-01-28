/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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

import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.framework.WebClientSpec;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.mozilla.javascript.Function;
import org.richfaces.demo.fileUpload.FileUploadBean;

/**
 * Peform JSFUnit tests on RichFaces demo application.
 *
 * @author Stan Silvert
 */
public class RichFileUploadTest extends ServletTestCase
{
   public static Test suite()
   {
      return new TestSuite( RichFileUploadTest.class  );
   }

   public void testFielUpload() throws IOException
   { 
   /*   WebClientSpec wcSpec = new WebClientSpec("/richfaces/fileUpload.jsf");
      JSFSession jsfSession = JSFSessionFactory.makeSession(wcSpec);
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
      
     // client.click("autoupload");
      FileUploadBean uploadBean = (FileUploadBean)server.getManagedBeanValue("#{fileUploadBean}");
     // assertTrue(uploadBean.isAutoUpload());
      
      System.out.println("***************");
      String path = "/projects/jbosslabs/jsfunit/jboss-jsfunit-examples/jboss-jsfunit-examples-richfaces/testdata";
      System.out.println("path=" + path + "/jboss.gif");
      
      HtmlFileInput fileInput = (HtmlFileInput)client.getElement("richfileupload:file");
      //fileInput.removeEventHandler("onchange");
      fileInput.setValueAttribute(path + "/jboss.gif");
      //fileInput.setContentType("image/gif");
      //System.out.println("fileInput=" + fileInput.asXml());
      //fileInput.setEventHandler("onchange", eventHandler);
      
      System.out.println("#1");
      
  //    fileInput.setContentType("image/png");
  //    System.out.println("#2");
  //    fileInput.setValueAttribute(path + "/logo_rh_home.png");
  //    System.out.println("#3");
      client.click("richfileupload:upload1");
      assertTrue(client.getPageAsText().contains("2174"));
      System.out.println("#4");
      
      System.out.println("#5");
      assertEquals(1, uploadBean.getFiles().size());
      System.out.println("#6");
      System.out.println("**********************");  */
   }
   
}
