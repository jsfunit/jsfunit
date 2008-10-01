/*
* JBoss, Home of Professional Open Source
* Copyright 2008, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
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
package org.jboss.jsfunit.microdeployer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The sole purpose of this servlet is to serve up cactus-report.xsl.  Therefore,
 * when someone is using the Cactus ServletTestRunner and includes xsl=cactus-report.xsl
 * then cactus-report.xsl will be retrieved using this servlet instead of getting
 * it directly.  This keeps the developer from needing to bundle cactus-report.xsl
 * with the WAR deployment.
 * 
 * @author Stan Silvert
 */
public class XSLFacadeServlet extends HttpServlet
{

   protected String xsl;
   
   @Override
   public void init(ServletConfig config) throws ServletException {
      InputStream in = getClass().getClassLoader().getResourceAsStream("cactus-report.xsl");
      DataInputStream dataStream = new DataInputStream(in);
      
      try
      {
         byte[] data = new byte[in.available()];
         dataStream.readFully(data);
         this.xsl = new String(data);
      }
      catch (IOException e)
      {
         throw new ServletException(e);
      }
      
   }

   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
   {
      response.getWriter().print(xsl);
   }
   
}
