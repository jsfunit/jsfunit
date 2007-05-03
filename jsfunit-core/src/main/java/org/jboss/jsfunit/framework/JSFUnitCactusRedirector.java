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

package org.jboss.jsfunit.framework;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.cactus.server.ServletTestRedirector;
import org.jboss.jsfunit.context.JSFUnitFacesContext;

/**
 * The JSFUnitCactusRedirector is used to invoke JSFUnit tests during a client
 * invocation of JUnit.  It is a replacement for the Cactus ServletTestRedirector.
 * For details, see http://jakarta.apache.org/cactus/   
 * 
 * Suggested setup in web.xml is:
 * <code>
 * 
 *  <servlet>
 *    <servlet-name>JSFUnitRedirector</servlet-name>
 *    <servlet-class>org.jboss.jsfunit.framework.JSFUnitCactusRedirector</servlet-class>
 *  </servlet>   
 * 
 *  <servlet-mapping>
 *     <servlet-name>JSFUnitRedirector</servlet-name>
 *     <url-pattern>/JSFUnitRedirector</url-pattern>
 *  </servlet-mapping>  
 * 
 * </code>
 * 
 * @author Stan Silvert
 */
public class JSFUnitCactusRedirector extends ServletTestRedirector
{
   public void init(ServletConfig config) throws ServletException
   {
      super.init(config);
   }
   
   public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException
   {
      try 
      {
        WebConversationFactory.setThreadLocals(req);
        super.doPost(req, resp);
      } 
      finally 
      {
         req.getSession().removeAttribute(JSFUnitFacesContext.SESSION_KEY);
         WebConversationFactory.removeThreadLocals();
      }
   }
   
   public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException
   {
      doPost(req, resp);
   }
   
}
