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

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.cactus.server.runner.ServletTestRunner;
import org.jboss.jsfunit.context.JSFUnitFacesContext;

/**
 * The JSFUnitServlet is used to invoke JSFUnit tests written with JUnit.  It is
 * responsible for setting up the environment to allow the FacesContext to be
 * passed into a JSFUnit test.
 *
 * If your unit tests extend the Cactus ServletTestCase then you will need to 
 * bundle a cactus.properties with your WAR before calling this servlet.
 *
 * Suggested setup in web.xml is:
 * <code>
 *
 * <servlet>
 *    <servlet-name>JSFUnitServlet</servlet-name>
 *    <servlet-class>org.jboss.jsfunit.framework.JSFUnitServlet</servlet-class>
 *  </servlet>   
 *
 *  <servlet-mapping>
 *     <servlet-name>JSFUnitServlet</servlet-name>
 *     <url-pattern>/jsfunit</url-pattern>
 *  </servlet-mapping>  
 *
 * </code>
 *
 * A typical call to the servlet looks like this:
 * http://localhost:8080/hellojsf/jsfunit?suite=org.foo.MyJSFTest&xsl=cactus-report.xsl
 * where MyJSFTest is a JUnit TestSuite class.
 *
 * This servlet extends the Cactus ServletTestRunner.  See the cactus documentation
 * for full details of configuration and runtime options.  
 * http://jakarta.apache.org/cactus/integration/integration_browser.html
 *
 * @author Stan Silvert
 */
public class JSFUnitServlet extends ServletTestRunner
{
   public void init(ServletConfig config) throws ServletException
   {
      super.init(config);
   }
   
   public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
   {
      try 
      {
        WebConversationFactory.setThreadLocals(req);
        super.doGet(req, resp);
      } 
      finally 
      {
         req.getSession().removeAttribute(JSFUnitFacesContext.SESSION_KEY);
         WebConversationFactory.removeThreadLocals();
      }
   }
   
   public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
   {
      doGet(req, resp);
   }
   
}
