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

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import junit.framework.TestCase;

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
      List<String> definedRenderkits = extractRenderkitsDefined(configFilePathList);
      assertTrue("Default renderkit '" + defaultRenderkitId + "' is not defined in the config files.",
            definedRenderkits.contains(defaultRenderkitId));
   }

   /**
    * Loop over all config file paths and extract a consolidated list of renderkits defined.
    * 
    * @param configFilePathList the list of config file path strings
    * @return a list of renderkits ids
    */
   private List<String> extractRenderkitsDefined(List<String> configFilePathList)
   {
      List<String> result = new ArrayList<String>();
      for (Iterator<String> iterator = configFilePathList.iterator(); iterator.hasNext();)
      {
         String configFilePath = iterator.next();
         result.addAll(extractRenderkitIdsDefined(configFilePath));
      }
      return result;
   }

   /**
    * Extract a list of renderkits defined in the config file.
    * 
    * @param configFilePath the path to the config file
    * @return a list of renderkits ids
    */
   private List<String> extractRenderkitIdsDefined(String configFilePath)
   {
      List<String> result = new ArrayList<String>();

      String xpathRenderkit = "//render-kit";
      String xpathRenderkitId = "./render-kit-id/text()";

      Document domDocument = getDomDocument(configFilePath);
      NodeList renderkits = ParserUtils.query(domDocument, xpathRenderkit, configFilePath);
      for (int i = 0; i < renderkits.getLength(); i++)
      {
         Node renderkit = renderkits.item(i);
         String id = ParserUtils.querySingle(renderkit, xpathRenderkitId, configFilePath);
         if (id != null && id.trim().length() > 0)
         {
            result.add(id);
         }
         else
         {
            result.add("default");
         }
      }
      return result;
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
    * Setup the DOM parser for the file specified.
    * 
    * @param filePath the path to the file to be parsed
    * @return a DOM Document
    */
   private Document getDomDocument(String filePath)
   {
      String xml = ParserUtils.getXml(filePath, getStreamProvider());
      DocumentBuilder builder = ParserUtils.getDocumentBuilder();
      Document document = null;
      try
      {
         document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
      }
      catch (Exception e)
      {
         throw new RuntimeException("Could not parse document '" + filePath + "'\n" + xml, e);
      }
      return document;
   }
}
