/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.jsfunit.analysis.util; 

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jboss.jsfunit.analysis.DefaultStreamProvider;
import org.jboss.jsfunit.analysis.StreamProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Collect methods that deal with JSF configuration.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class ConfigUtils
{
   /**
    * Enumeration to specify the type of a configuration item.
    */
   public enum ConfigItemType {
        RENDER_KIT
     // , MANAGED_BEAN
   }

   /**
    *  <p>The stream provider to be used, default: DefaultStreamProvider.
    *  This allows for special purposes to change the stream provider for the file-accessing methods.</p> 
    */
   private StreamProvider streamProvider = null;

   /** list of the configuration file paths */
   private List<String> configFilePaths = new ArrayList<String>();
   /** map of preparsed configuration files */
   private Map<String, Document> configFileDoms = new HashMap<String, Document>();
 
   /**
    * Verify whether a config item is configured in one of the config files that exist in the setup.
    * 
    * @param elementType the configuration element type 
    * @param elementName name of the configuration element
    * @param includeClassPath if true, then the config-files within the classpath's jar files are also searched
    * @return true if the configuration element is configured within the setup, false otherwise
    */
   public boolean isConfigured(ConfigItemType elementType, String elementName, boolean includeClassPath){
      boolean result = false;
      
      //search cached dom-documents for configuration item
      for (Iterator<String> cachedDomNames = getConfigFileDoms().keySet().iterator(); (cachedDomNames.hasNext() && !result);)
      {
         String configFilePath = cachedDomNames.next();
         Document configFile = getConfigFileDoms().get(configFilePath);
         if (isConfiguredConfigItemType(elementType, elementName, configFile, configFilePath))
         {
            result = true;
         }
      }
      if (!result)
      {
      //search uncached dom-documents from config-file-path-list
      for (Iterator<String> configFilePaths = getConfigFilePaths().iterator(); (configFilePaths.hasNext() && !result);)
      {
         String configFilePath = configFilePaths.next();
         if (!getConfigFileDoms().keySet().contains(configFilePath)) {
            Document configFile = ParserUtils.getDomDocument(configFilePath, getStreamProvider());
            getConfigFileDoms().put(configFilePath, configFile);
            if (isConfiguredConfigItemType(elementType, elementName, configFile, configFilePath))
            {
               result = true;
            }
         }
      }
      }
      
      if (!result && includeClassPath)
      {
         List<InputStream> classPathConfigFiles = ResourceUtils.getClassPathResourcesAsStreams("META-INF/faces-config.xml");
         for (Iterator<InputStream> classPathConfigFilesStreams = classPathConfigFiles.iterator(); (classPathConfigFilesStreams.hasNext() && !result);)
         {
            InputStream classPathConfigFilesStream = classPathConfigFilesStreams.next();
            Document configFile = ParserUtils.getDomDocument(classPathConfigFilesStream, classPathConfigFilesStream.toString());
            if (isConfiguredConfigItemType(elementType, elementName, configFile, classPathConfigFilesStream.toString()))
            {
               result = true;
            }
         }
      }
      
      return result;
   }

   /**
    * Verify whether a config item is configured in one of the config files that exist in the setup.
    * 
    * @param elementType the configuration element type 
    * @param elementName name of the configuration element
    * @param configFile the preparsed config file
    * @param configFilePath the path to the config file
    * @return true if the configuration element is configured within the setup, false otherwise
    */
   public boolean isConfiguredConfigItemType(ConfigItemType elementType, String elementName, Document configFile, String configFilePath)
   {
      boolean result = false;
      
      switch (elementType)
      {
         case RENDER_KIT :
         {
            result = extractRenderkitIdsDefined(configFile, configFilePath).contains(elementName);
            break;
         }

         default :
            break;
      }
      
      return result;
   }

   /**
    * Extract a list of renderkits defined in the config file.
    * 
    * @param configFile the preparsed config file
    * @param configFilePath the path to the config file
    * @return a list of renderkits ids
    */
   public List<String> extractRenderkitIdsDefined(Document configFile, String configFilePath)
   {
      List<String> result = new ArrayList<String>();

      String xpathRenderkit = "//render-kit";
      String xpathRenderkitId = "./render-kit-id/text()";

      NodeList renderkits = ParserUtils.query(configFile, xpathRenderkit, configFilePath);
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
    * Get the configFilePaths.
    * 
    * @return the configFilePaths
    */
   public List<String> getConfigFilePaths()
   {
      return configFilePaths;
   }

   /**
    * Set the configFilePaths.
    * 
    * @param configFilePaths The configFilePaths to set.
    */
   public void setConfigFilePaths(List<String> configFilePaths)
   {
      this.configFilePaths = configFilePaths;
   }

   /**
    * Get the configFileDoms.
    * 
    * @return the configFileDoms.
    */
   public Map<String, Document> getConfigFileDoms()
   {
      return configFileDoms;
   }

   /**
    * Set the configFileDoms.
    * 
    * @param configFileDoms The configFileDoms to set.
    */
   public void setConfigFileDoms(Map<String, Document> configFileDoms)
   {
      this.configFileDoms = configFileDoms;
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
