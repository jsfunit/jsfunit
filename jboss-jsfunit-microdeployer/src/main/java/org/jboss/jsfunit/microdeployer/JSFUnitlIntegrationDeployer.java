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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.deployers.vfs.spi.deployer.AbstractOptionalVFSRealDeployer;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.jboss.metadata.web.jboss.JBossServletMetaData;
import org.jboss.metadata.web.jboss.JBossServletsMetaData;
import org.jboss.metadata.web.jboss.JBossWebMetaData;
import org.jboss.metadata.web.spec.FilterMappingMetaData;
import org.jboss.metadata.web.spec.FiltersMetaData;
import org.jboss.metadata.web.spec.ServletMappingMetaData;
import org.jboss.metadata.web.spec.ServletMetaData;
import org.jboss.metadata.web.spec.ServletsMetaData;
import org.jboss.metadata.web.spec.Web25MetaData;
import org.jboss.util.StringPropertyReplacer;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;
import org.jboss.virtual.VirtualFileFilter;
import org.jboss.virtual.plugins.vfs.helpers.SuffixMatchFilter;
import org.jboss.xb.binding.JBossXBException;
import org.jboss.xb.binding.Unmarshaller;
import org.jboss.xb.binding.UnmarshallerFactory;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBinding;
import org.jboss.xb.builder.JBossXBBuilder;

/**
 * The JSFUnitIntegrationDeployer automatically adds the needed servlets and
 * servlet filters to a WAR.  It will also add all the JSFUnit classes and 
 * dependencies.
 * 
 * Optionally, it can also add user-defined directories and jars to the 
 * classpath of the WAR.
 *
 * @author Stan Silvert
 */
public class JSFUnitlIntegrationDeployer extends AbstractOptionalVFSRealDeployer<JBossWebMetaData>
{
   private static final VirtualFileFilter JAR_FILTER = new SuffixMatchFilter("jar");
   
   private Collection<String> classpathUrls;
   private Collection<String> warSuffixes;

   /**
    * Unmarshall factory used for parsing shared web.xml.
    */
   private static final UnmarshallerFactory factory = UnmarshallerFactory.newInstance();
   
   /**
    * Create a new deployer.
    */
   public JSFUnitlIntegrationDeployer()
   {
      super(JBossWebMetaData.class);
      // We have to run before the classloading is setup
      setStage(DeploymentStages.POST_PARSE);
   }

   /**
    * Set the collection of suffixes that this deployer will use to choose
    * which wars to "JSFUnify".  For example, if the suffixes were "_jsfunit"
    * and "mywar", then mywar.war and foo_jsfunit.war would match.
    * 
    * @param warSuffixes The case-insensitive suffix (less the ".war").
    */
   public void setWarSuffixes(Collection<String> warSuffixes)
   {
      this.warSuffixes = warSuffixes;
   }
   
   /**
    * A collection of URL strings to add to the classpath of the WAR.  If the
    * URL points to a directory then the directory and any jar found under 
    * that director will also be added.
    * 
    * @param classpathUrls The url strings.
    */
   public void setClasspathURLs(Collection<String> classpathUrls)
   {
      this.classpathUrls = classpathUrls;
   }
   
   private boolean isJSFUnitDeployment(VFSDeploymentUnit unit) {
      if (warSuffixes == null) return false;

      for (String warSuffix: warSuffixes)
      {
         if (unit.getSimpleName().toLowerCase().endsWith(warSuffix + ".war")) return true;
      }
      
      return false;
   }
   
   @Override
   public void deploy(VFSDeploymentUnit unit, JBossWebMetaData metaData) throws DeploymentException
   {
      if (!isJSFUnitDeployment(unit)) return;
      
      try
      {
         mergeWebXml(metaData);
      }
      catch (JBossXBException e)
      {
         throw DeploymentException.rethrowAsDeploymentException("Error adding jsfunit web.xml elements", e);
      }
      
      try
      {
         addClasspaths(unit);
      }
      catch (MalformedURLException e)
      {
         throw DeploymentException.rethrowAsDeploymentException("Malformed URL", e);
      }
      
      log.info("Added JSFUnit to " + unit.getName());
   }
   
   private void addClasspaths(VFSDeploymentUnit unit) throws MalformedURLException
   {
      if (classpathUrls == null) return;
      
      for (String testUrl : classpathUrls)
      {
         testUrl = StringPropertyReplacer.replaceProperties(testUrl);
         URL url = new URL(testUrl);
         
         try
         {
            VirtualFile vFile = VFS.getRoot(url);
            unit.addClassPath(vFile);
            
            // add jar files if url is a directory
            if (!vFile.isLeaf())
            { 
               for (VirtualFile jarFile : vFile.getChildrenRecursively(JAR_FILTER))
               {
                  unit.addClassPath(jarFile);
               }
            }
         }
         catch (IOException e)
         {
            log.warn("Unable to add URL to classpath: " + url.toString());
         }
      }
   }
   
   // merge JSFUnit's web.xml with the WAR's web.xml
   private void mergeWebXml(JBossWebMetaData metaData) throws JBossXBException
   {
      // Parse JSFUnit web.xml
      Unmarshaller unmarshaller = factory.newUnmarshaller();
      URL webXml = this.getClass().getClassLoader().getResource("META-INF/web.xml");
      if (webXml == null)
         throw new IllegalStateException("Unable to find jsfunit web.xml");
      
      SchemaBinding schema = JBossXBBuilder.build(Web25MetaData.class);
      Web25MetaData jsfunitWebMD = (Web25MetaData) unmarshaller.unmarshal(webXml.toString(), schema);
      
      FiltersMetaData filters = metaData.getFilters();
      if (filters == null) filters = new FiltersMetaData();
      filters.addAll(jsfunitWebMD.getFilters());
      metaData.setFilters(filters);
      
      List<FilterMappingMetaData> filterMappings = metaData.getFilterMappings();
      if (filterMappings == null) filterMappings = new ArrayList<FilterMappingMetaData>();
      filterMappings.addAll(jsfunitWebMD.getFilterMappings());
      metaData.setFilterMappings(filterMappings);
      
      JBossServletsMetaData servlets = metaData.getServlets();
      if (servlets == null) servlets = new JBossServletsMetaData();
      ServletsMetaData servletsMD = jsfunitWebMD.getServlets();
      servlets.addAll(makeJBossServletsMetaData(servletsMD));
      metaData.setServlets(servlets);
      
      List<ServletMappingMetaData> servletMappings = metaData.getServletMappings();
      if (servletMappings == null) servletMappings = new ArrayList<ServletMappingMetaData>();
      servletMappings.addAll(jsfunitWebMD.getServletMappings());
      metaData.setServletMappings(servletMappings);
   }
   
   private Collection<JBossServletMetaData> makeJBossServletsMetaData(ServletsMetaData servletsMD)
   {
      Collection<JBossServletMetaData> servletSet = new HashSet<JBossServletMetaData>();
      for (ServletMetaData servletMD : servletsMD)
      {
         JBossServletMetaData jbossMD = new JBossServletMetaData();
         servletSet.add(jbossMD.merge(servletMD));
      }
      
      return servletSet;
   }
   
}