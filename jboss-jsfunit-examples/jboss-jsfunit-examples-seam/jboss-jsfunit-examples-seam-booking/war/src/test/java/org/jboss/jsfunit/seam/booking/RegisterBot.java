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

package org.jboss.jsfunit.seam.booking;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import org.jboss.jsfunit.facade.JSFClientSession;
import org.jboss.jsfunit.facade.JSFServerSession;
import org.jboss.jsfunit.seam.SeamClient;
import org.xml.sax.SAXException;

/**
 * The RegisterBot ensures that a user is registered so that a 
 * subsequent loging will always be sucessful.
 *
 * @author Stan Silvert
 */
public class RegisterBot
{

   /**
    * Register a new user for the booking demo.  If the username already exists
    * then this method returns sucessfully.
    *
    * @throws IllegalArgumentException if the username/password is invalid.
    */
   public static void registerUser(String username, String password) throws IOException, SAXException
   {
      SeamClient client = null;
     
      try
      {
         client = new SeamClient("/home.seam");
      }
      catch (MalformedURLException e)
      {
         throw new IllegalStateException("This should never happen", e);
      }
      
      JSFServerSession server = new JSFServerSession(client);
     
      client.clickSLink("register");
      client.setParameter("username", username);
      client.setParameter(":name", username + " created by RegisterBot");
      client.setParameter("password", password);
      client.setParameter("verify", password);
      client.submit("register");
      
      Iterator facesMessages = server.getFacesMessages();
      if (facesMessages.hasNext()) 
      {
         FacesMessage message = (FacesMessage)facesMessages.next();
         String detail = message.getDetail();
         if (detail.toLowerCase().contains("already exists")) return;  // OK
         if (detail.toLowerCase().contains("successfully registered")) return; // OK
         throw new IllegalArgumentException(detail);
      }
   }
   
   public static void login(JSFClientSession client, String username, String password)
         throws IOException, SAXException
   {
      client.setParameter("username", username);
      client.setParameter("password", password);
      client.submit("login:login");
   }
   
    public static SeamClient registerAndLogin(String username, String password) 
         throws IOException, SAXException
    {
       registerUser(username, password);
       SeamClient client = new SeamClient("/home.seam");
       login(client, username, password);
       return client;
    }
   
}
