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

import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.httpclient.NameValuePair;
import org.jboss.jsfunit.framework.RequestListener;

/**
 * RequestListener outputs before and after each request made by HtmlUnit.
 * It outputs the messages to standard out.
 *  
 *  To enable, set system property jsfunit.htmlunitsnooper.
 *
 * @author Stan Silvert
 * @since 1.0
 */
public class HtmlUnitSnooper implements RequestListener
{
   public static final String SNOOP_PROPERTY = "jsfunit.htmlunitsnooper";
   
   /**
    * Check to see if HtmlUnit snooping is enabled.
    * 
    * @return <code>true</code> if enabled, <code>false</code> otherwise.
    */
   public static boolean enabled()
   {
      return System.getProperty(SNOOP_PROPERTY) != null;
   }

   public void beforeRequest(WebRequestSettings webRequestSettings) 
   {
      System.out.println("-----------Snooping HtmlUnit Request---------------------------");
      System.out.println("url=" + webRequestSettings.getUrl());
      System.out.println("method=" + webRequestSettings.getHttpMethod());
      System.out.println("encoding=" + webRequestSettings.getEncodingType());

      System.out.println();
      System.out.println("Additional Headers:");
      Map<String, String> addlHeaders = webRequestSettings.getAdditionalHeaders();
      for (Iterator<String> i = addlHeaders.keySet().iterator(); i.hasNext(); )
      {
         String name = i.next();
         System.out.println(name + "=" + addlHeaders.get(name));
      }
      
      System.out.println();
      System.out.println("Request Params:");
      for (Iterator i = webRequestSettings.getRequestParameters().iterator(); i.hasNext();)
      {
         NameValuePair pair = (NameValuePair)i.next();
         System.out.println(pair.getName() + "=" + pair.getValue());
      }
      System.out.println("---------------------------------------------------------------");
   }

   public void afterRequest(WebResponse webResponse) 
   {
      System.out.println("-----------Snooping HtmlUnit Response---------------------------");
      
      if (webResponse == null)
      {
         System.out.println("REQUEST THREW IOException.  Response is null.");
         System.out.println("---------------------------------------------------------------");
      }
      
      System.out.println("Response time=" + webResponse.getLoadTime() + "ms");
      System.out.println("Status code=" + webResponse.getStatusCode());
      System.out.println("Status message=" + webResponse.getStatusMessage());
      System.out.println("Content type=" + webResponse.getContentType());
      System.out.println("Content char set=" + webResponse.getContentCharSet());
      System.out.println();
      System.out.println("Response Headers:");
      for (Iterator i = webResponse.getResponseHeaders().iterator(); i.hasNext();)
      {
         NameValuePair pair = (NameValuePair)i.next();
         System.out.println(pair.getName() + "=" + pair.getValue());
      }
      System.out.println();
      System.out.println("Response body:");
      System.out.println(webResponse.getContentAsString());
      System.out.println("---------------------------------------------------------------");
   }

}
