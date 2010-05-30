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

package org.jboss.jsfunit.analysis.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Collect methods that deal with resources.
 * 
 * @author Dennis Byrne
 * @since 1.0
 */

public class ResourceUtils{

   /**
    * Search resources within the classpath that match the given name, create input streams
    * from the found resources and return them as a list.
    * 
    * @param relativeResourceName resource name relative to the root elements of the classpath 
    *                 (usually the jar-files root element, or the folder specified in the classpath)
    * @return a List<InputStream> of input streams matching the resource name
    */
   public static List<InputStream> getClassPathResourcesAsStreams(String relativeResourceName)
   {
      List<InputStream> result = new ArrayList<InputStream>();
      try
      {
         Enumeration<URL> resourceUrls = Thread.currentThread().getContextClassLoader()
                       .getResources(relativeResourceName);
         while (resourceUrls.hasMoreElements())
         {
            result.add(((InputStream)(resourceUrls.nextElement().getContent())));
         }
      }
      catch (IOException e)
      {
         e.printStackTrace();
      } 
      catch (NullPointerException npe)
      {
         npe.printStackTrace();
      }
      return result;
   }
	
	public String getAsString(InputStream stream, String resourceName) {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuffer buffer = new StringBuffer();
		String temp = null;
		
		try {
			
			while ((temp = reader.readLine()) != null)
				buffer.append(temp);
			
		} catch (IOException e) 
		{
			throw new RuntimeException("Could not read file " + resourceName, e);
		}
		
		try {
			reader.close();
		} catch (IOException e) 
		{
			throw new RuntimeException("Could not close stream for " + resourceName);
		}
		
		return buffer.toString();
	}
	
}
