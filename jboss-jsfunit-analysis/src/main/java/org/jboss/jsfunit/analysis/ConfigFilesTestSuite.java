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
 * 
 */
package org.jboss.jsfunit.analysis;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A TestSuite for the config files of a JSF-application.
 * To test the faces-config files create a list of config-file path, instantiate this TestSuite 
 * and ask for the getSuite().
 *  
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $$Revision:  $$
 */
public class ConfigFilesTestSuite extends TestSuite
{
   /**
    *  <p>The stream provider to be used, default: DefaultStreamProvider.
    *  This allows for special purposes to change the stream provider for the file-accessing methods.</p> 
    *  
    */
   private StreamProvider streamProvider = null;

   /**
    * Create a new ConfigFilesTestSuite.
    * 
    * @param name The name of the test-suite in the JUnit test-hierarchy
    */
   public ConfigFilesTestSuite(String name)
   {
      super(name);
   }

   /**
    * <p>Create the TestSuite for a list of config files. For each config file a sub-TestSuite will be created.</p>
    * <p>The TestSuite-name is composed of the name passed to this suite (constructor)
    * and the class-name.</p>
    * 
    * @param configFilePathList a list of paths to config files
    * @return a JUnit Test to be added to the actual test suite
    */
   public Test getSuite(List<String> configFilePathList)
   {
      System.out.println("start building dynamic suite");
      long timeStart = System.currentTimeMillis();

      if (configFilePathList == null)
      {
         throw new RuntimeException("Invalid input: null");
      }
      TestSuite suite = new TestSuite();
      suite.setName(getName() + "_" + ConfigFilesTestSuite.class.getSimpleName());
      for (Iterator<String> iterator = configFilePathList.iterator(); iterator.hasNext();)
      {
         String singleConfigFilePath = iterator.next();
         ConfigFileTestSuite subSuite = new ConfigFileTestSuite(singleConfigFilePath);
         subSuite.setStreamProvider(getStreamProvider());
         suite.addTest(subSuite.getSuite(singleConfigFilePath, configFilePathList));
      }

      long timeStop = System.currentTimeMillis();
      double timeInSeconds = ((double)(timeStop - timeStart)) / 1000;
      System.out.println("stop building dynamic suite (duration: " + timeInSeconds + " seconds) adding " + suite.countTestCases() + " testcases.");
      return suite;
   }

   /**
    * Accessor for the streamProvider attribute
    * 
    * @return the streamprovider
    */
   public StreamProvider getStreamProvider()
   {
      if (streamProvider == null)
      {
         streamProvider = new DefaultStreamProvider();
      }
      return streamProvider;
   }

   /**
    * <p>Accessor for the streamProvider attribute.
    * Usually used to set a special StreamProvider. By default a DefaultStreamProvider is created.</p>
    * 
    * @param streamProvider the new StreamProvider
    */
   public void setStreamProvider(StreamProvider streamProvider)
   {
      this.streamProvider = streamProvider;
   }
}
