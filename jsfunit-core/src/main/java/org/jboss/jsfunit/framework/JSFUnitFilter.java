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
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.jboss.jsfunit.context.JSFUnitFacesContext;

/**
 * The JSFUnitFilter is used to set up JSFUnit tests during a client
 * invocation of JUnit.  It must run before the Cactus ServletTestRedirector or
 * Cactus ServletTestRunner.
 * <br/>
 * For details, see http://jakarta.apache.org/cactus/   
 * 
 * Suggested setup in web.xml is:
 * <code>
 * 
 *  <filter>
 *    <filter-name>JSFUnitFilter</filter-name>
 *    <filter-class>org.jboss.jsfunit.framework.JSFUnitFilter</filter-class>
 *  </filter>
 *
 * <filter-mapping>
 *   <filter-name>JSFUnitFilter</filter-name>
 *   <servlet-name>ServletRedirector</servlet-name>
 * </filter-mapping>
 *
 * <filter-mapping>
 *   <filter-name>JSFUnitFilter</filter-name>
 *   <servlet-name>ServletTestRunner</servlet-name>
 * </filter-mapping>	
 *
 * </code>
 * 
 * @author Stan Silvert
 */
public class JSFUnitFilter implements Filter
{

   public void doFilter(ServletRequest req, 
                        ServletResponse res, 
                        FilterChain filterChain) throws IOException, ServletException
   {
      try 
      {
        WebConversationFactory.setThreadLocals((HttpServletRequest)req);
        filterChain.doFilter(req, res);
      } 
      finally 
      {
         ((HttpServletRequest)req).getSession().removeAttribute(JSFUnitFacesContext.SESSION_KEY);
         WebConversationFactory.removeThreadLocals();
      }
   }

   public void init(FilterConfig filterConfig) throws ServletException
   {
   }
   
   public void destroy()
   {
   }
   
}

