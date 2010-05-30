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

import java.util.List;

import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.util.ConfigUtils;
import org.jboss.jsfunit.analysis.util.ConfigUtils.ConfigItemType;

/**
 * A TestCase for a single JSF default renderkit configuration.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $$Revision:  $$
 */
public class DefaultRenderkitTestCase extends TestCase
{
   /** Id of the defaultRenderkit */
   protected String defaultRenderkitId = null;

   /** List of all passed config Files */
   protected List<String> configFilePathList = null;

   /**
    *  <p>The stream provider to be used, default: DefaultStreamProvider.
    *  This allows for special purposes to change the stream provider for the file-accessing methods.</p> 
    */
   private StreamProvider streamProvider = null;

   /** An instance of the ConfigUtils */
   private ConfigUtils configUtils = null;

   /**
    * Create a new ActionListenerTestCase.
    * 
    * @param name The name of the test-case in the JUnit test-hierarchy
    * @param actionListenerName the name of the action listener to test
    */
   public DefaultRenderkitTestCase(String defaultRenderkitId, List<String> configFilePathList)
   {
      super("defaultRenderkit");
      this.defaultRenderkitId = defaultRenderkitId;
      this.configFilePathList = configFilePathList;
      getConfigUtils().setConfigFilePaths(configFilePathList);
   }

   /**
    * Create a new ActionListenerTestCase.
    * 
    * @param name The name of the test-case in the JUnit test-hierarchy
    * @param actionListenerName the name of the action listener to test
    * @param configUtils an instance of the configUtils
    */
   public DefaultRenderkitTestCase(String defaultRenderkitId, List<String> configFilePathList, ConfigUtils configUtils)
   {
      super("defaultRenderkit");
      this.defaultRenderkitId = defaultRenderkitId;
      this.configFilePathList = configFilePathList;
      setConfigUtils(configUtils);
   }

   /**
    * Call the individual test methods.
    */
   public void runTest()
   {
      testRenderkitDefined();
   }

   /**
    * Assert that the configured default renderkit is defined
    */
   public void testRenderkitDefined()
   {
      configUtils.setStreamProvider(getStreamProvider());
      assertTrue("Default renderkit '" + defaultRenderkitId + "' is not defined in the config files.",
            configUtils.isConfigured(ConfigItemType.RENDER_KIT, defaultRenderkitId, true));
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

   /**
    * Get the configUtils.
    * 
    * @return the configUtils.
    */
   public ConfigUtils getConfigUtils()
   {
      if (configUtils == null) 
      {
         configUtils = new ConfigUtils();
         configUtils.setConfigFilePaths(configFilePathList);
      }
      return configUtils;
   }

   /**
    * Set the configUtils.
    * 
    * @param configUtils The configUtils to set.
    */
   public void setConfigUtils(ConfigUtils configUtils)
   {
      this.configUtils = configUtils;
   }
}
