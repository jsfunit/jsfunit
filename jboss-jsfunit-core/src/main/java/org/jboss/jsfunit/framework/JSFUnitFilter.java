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
 * <p>
 * The JSFUnitFilter is used to set up JSFUnit tests during a client
 * invocation of JUnit.  It must run before the Cactus ServletTestRedirector or
 * Cactus ServletTestRunner.
 * </p>
 * <p>
 * For details, see <a href="http://jakarta.apache.org/cactus/">Apache Cactus</a>.
 * </p>
 * <p>
 * Suggested setup in web.xml is to map the filter to the Cactus servlet(s) as
 * follows:
 * <code><pre>
 *  &lt;filter&gt;
 *    &lt;filter-name&gt;JSFUnitFilter&lt;/filter-name&gt;
 *    &lt;filter-class&gt;org.jboss.jsfunit.framework.JSFUnitFilter&lt;/filter-class&gt;
 *  &lt;/filter&gt;
 *
 * &lt;filter-mapping&gt;
 *   &lt;filter-name&gt;JSFUnitFilter&lt;/filter-name&gt;
 *   &lt;servlet-name&gt;ServletRedirector&lt;/servlet-name&gt;
 * &lt;/filter-mapping&gt;
 *
 * &lt;filter-mapping&gt;
 *   &lt;filter-name&gt;JSFUnitFilter&lt;/filter-name&gt;
 *   &lt;servlet-name&gt;ServletTestRunner&lt;/servlet-name&gt;
 * &lt;/filter-mapping&gt;	
 * </pre></code>
 * </p> 
 * <p>
 * If you are using a secure Cacuts redirector, you will also
 * need to declare the filter for that servele:
 * <code><pre>
 * &lt;filter-mapping&gt;
 *   &lt;filter-name&gt;JSFUnitFilter&lt;/filter-name&gt;
 *   &lt;servlet-name&gt;ServletRedirectorSecure&lt;/servlet-name&gt;
 * &lt;/filter-mapping&gt;	
 * </pre></code>
 * </p> 
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

