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
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

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
   public static void registerUser(String username, String password) throws IOException
   {
      JSFSession jsfSession = new JSFSession("/home.seam");
      JSFClientSession client = jsfSession.getJSFClientSession();
      JSFServerSession server = jsfSession.getJSFServerSession();
     
      // go to Registration screen
      client.click("register");
      
      client.setValue("username", username);
      client.setValue(":name", username + " created by RegisterBot");
      client.setValue("password", password);
      client.setValue("verify", password);
      client.click("register");
      
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
         throws IOException
   {
      client.setValue("username", username);
      client.setValue("password", password);
      client.click("login:login");
   }
   
    public static JSFSession registerAndLogin(String username, String password) 
         throws IOException
    {
       registerUser(username, password);
       
       JSFSession jsfSession = new JSFSession("/home.seam");
       JSFClientSession client = jsfSession.getJSFClientSession();
       login(client, username, password);
       return jsfSession;
    }
   
}
