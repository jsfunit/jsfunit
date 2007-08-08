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
package org.jboss.jsfunit.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.cactus.integration.ant.util.ResourceUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.codehaus.cargo.module.webapp.WebXml;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class JSFUnitWarTask extends Task{

	private File srcfile;
	private File destfile;
	private List<JSFUnitFilter> jsfFilters = new ArrayList<JSFUnitFilter>();
	private List<FileSet> libs = new ArrayList<FileSet>();
	private List<FileSet> classes = new ArrayList<FileSet>();
	private Boolean autoAddJars = true;
	private List<JSFUnitTestRunnerFilter> testRunnerFilters = new ArrayList<JSFUnitTestRunnerFilter>();

	
	public JSFUnitWarTask()
	{
		super();
	}
	
	/**
	 * Sets the original archive that should have JSFUnit specifics added
	 * @param srcfile the srcfile
	 */
	public void setSrcfile (File srcfile)
	{ 
		this.srcfile = srcfile;
	}

	/**
	 * Sets the destination for the newly created archive with JSFUnit specifics
	 * @param destfile
	 */
	public void setDestfile (File destfile)
	{
		this.destfile = destfile;
	}

	/**
	 * Sets the fileset that should be added to the WEB-INF/lib directory of the 
	 * created archive
	 * @param libFileSet The fileset to add 
	 */
	public void addLib (FileSet libFileSet)
	{
		libs.add(libFileSet);
	}

	/**
	 * Sets the fileset that should be added to the WEB-INF/classes directory of
	 * the created archive
	 * @param classesFileSet
	 */
	public void addClasses (FileSet classesFileSet)
	{
		classes.add(classesFileSet);
	}

	/**
	 * Sets whether or not to automatically add needed jars into the
	 * WEB-INF/lib directory of the crearted archive
	 * @param autoAddJars True to automatically add jars, false otherwise
	 */
	public void setAutoAddJars (Boolean autoAddJars)
	{
		this.autoAddJars = autoAddJars;
	}

	public static class Filter
	{
		protected String name;
		protected String mapping;
		protected String servletClass;
		protected String filterClass;
		protected String servletName;
				
		public Filter(){}
		
		public void setName (String name)
		{
			this.name = name;
		}
		
		public void setMapping (String mapping)
		{
			this.mapping = mapping;
		}
	
		public void setServletClass (String servletClass)
		{
			this.servletClass = servletClass;
		}
		
		public void addFilter(WebXml webXml)
		{
			if (!webXml.hasServlet(this.servletName))
			{
				webXml.addServlet(this.servletName, this.servletClass);
			}
			
			// only add the filter if it doesn't already exist
			if (!webXml.hasFilter(this.name)){
				webXml.addFilter(this.name, this.filterClass);
			}

			webXml.addServletMapping(this.servletName, this.mapping);
			webXml.addFilterMapping(this.name, this.mapping);			
		}
	}
	
	public static class JSFUnitFilter extends Filter
	{
		private final String DEFAULT_NAME = "JSFUnitFilter";
		private final String DEFAULT_MAPPING = "/ServletRedirector";
		private final String DEFAULT_SERVLET_CLASS = "org.apache.cactus.server.ServletTestRedirector";
		private final String DEFAULT_FILTER_CLASS = "org.jboss.jsfunit.framework.JSFUnitFilter";
		
		public JSFUnitFilter()
		{
			name = DEFAULT_NAME;
			mapping = DEFAULT_MAPPING;
			servletClass = DEFAULT_SERVLET_CLASS;
			filterClass = DEFAULT_FILTER_CLASS;
			servletName = "ServletRedirector";
		}
		
	}
	
	public static class JSFUnitTestRunnerFilter extends Filter
	{
		private final String DEFAULT_NAME = "ServletTestFilter";
		private final String DEFAULT_MAPPING = "/ServletTestRunner";
		private final String DEFAULT_SERVLET_CLASS = "org.apache.cactus.server.runner.ServletTestRunner";
		private final String DEFAULT_FILTER_CLASS = "org.jboss.jsfunit.framework.JSFUnitFilter";
		
		public JSFUnitTestRunnerFilter()
		{
			name = DEFAULT_NAME;
			mapping = DEFAULT_MAPPING;
			servletClass = DEFAULT_SERVLET_CLASS;
			filterClass = DEFAULT_FILTER_CLASS;
			servletName="ServletTestRunner";
		}
		
	}
	
	
	/**
	 * Adds a JSFFilter.
	 * Note: this doesn't actually set the once JSFFilter but rather
	 *       adds it to the list of already added filters. 
	 * @param jsfFilter The JSFFilter
	 */
	public void addJSFUnitFilter(JSFUnitFilter jsfFilter)
	{
		jsfFilters.add(jsfFilter);
	}
	
	public void addTestRunner(JSFUnitTestRunnerFilter testRunnerFilter){
		testRunnerFilters.add(testRunnerFilter);
	}

	public void execute()
	{
		if (srcfile == null)
		{
			throw new BuildException ("A srcfile must be specified");
		}
		else if (destfile == null)
		{
			throw new BuildException ("A destfile must be specified");
		}
		else if (destfile == srcfile)
		{
			// Note: it could be changed so that having a srcfile and destfile locations
			//       being the same would result in the srcfile being overwritten in the end.
			//       Not sure how useful this would be.
			throw new BuildException ("The destfile and srcfile must not be the same");
		}
		else
		{
			log("using srcfile :" + srcfile, Project.MSG_DEBUG);
			if (srcfile.isDirectory())
			{
				log("srcfile is an directory", Project.MSG_DEBUG);
				JSFUnitExplodedWar();
			}
			else if (srcfile.isFile())
			{
				log("srcfile is a file", Project.MSG_DEBUG);
				JSFUnitWar();
			}
			else
			{
				throw new BuildException ("Cannot find specifiec srcfile : " + srcfile );
			}
		}

	}


	/**
	 * This method is called when we are dealing with an exploded
	 * War archive.
	 */
	private void JSFUnitExplodedWar ()
	{
		try
		{
			// make a copy of the original war that will be edited
			Utils.copy(srcfile, destfile);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}

		try
		{
			// get the web.xml
			WebXml webxml = getWebXml(destfile);
			// edit the web.xml
			webxml = editWebXml(webxml);
			// write the web.xml back
			writeWebXml(destfile, webxml);
			addLibs(destfile);
			autoAddLibs(destfile);
			addClasses(destfile);			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns a WebXml object from the specified archive that represents the web.xml file
	 * @param archiveRoot The archive to retrieve the web.xml from
	 * @return	The WebXml object
	 * @throws SAXException If an exception occurs when trying to read the archive
	 * @throws IOException If an exception occurs when trying to read the archive
	 * @throws ParserConfigurationException If an exception occurs when trying to read the archive
	 */
	private WebXml getWebXml (File archiveRoot) throws SAXException, IOException, ParserConfigurationException
	{
		// the location within the archive where a web.xml file should exist
		String webXMLLocation = archiveRoot.getPath() + File.separator + "WEB-INF" + File.separator + "web.xml";
		File webXMLFile = new File (webXMLLocation);
		if (!webXMLFile.exists())
		{
			// if there is no web.xml file, then we can't do anything
			throw new BuildException ("No web xml descriptor : " + webXMLLocation);
		} 
		else
		{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document webxmlDoc = docBuilder.parse(webXMLFile);
			WebXml webxml = new WebXml(webxmlDoc);
			return webxml;
		}
	}

	/**
	 * Writes the web.xml file back into the archive
	 * @param archiveRoot the archive to contain the web.xml
	 * @param webxml the web.xml file to be written
	 * @throws IOException if an exception occurs during file writing
	 */
	private void writeWebXml (File archiveRoot, WebXml webxml) throws IOException
	{
		String webXMLLocation = archiveRoot.getPath() + File.separator + "WEB-INF" + File.separator + "web.xml";
		OutputFormat format = new OutputFormat();
		format.setIndenting(true);
		XMLSerializer xmlSerializer = new XMLSerializer(format);
		xmlSerializer.setOutputCharStream(new java.io.FileWriter(webXMLLocation));
		xmlSerializer.serialize(webxml.getDocument());
	}

	/**
	 * This method gets called if we are dealing with an actual archive and
	 * not an exploded archive.
	 * This archive is exploded to a temporary directory so that it can be altered
	 * in the same manner as if we were dealing with an expldoded archive.
	 */
	private void JSFUnitWar ()
	{
		try{
			File tmpDir = File.createTempFile("jsfunitwartask", "");
			// File.createTempFile creates the actual file, so if we want a
			// directory instead of a regular file we need to delete the file
			// and tell it to make a directory instead. 
			tmpDir.delete();
			tmpDir.mkdir();

			// get and explode the zip file to the temp directory
			ZipFile zip = new ZipFile(srcfile);
			File explodedArchive = Utils.explodeArchive(zip, tmpDir);

			// get the web.xml
			WebXml webXml = getWebXml(explodedArchive);
			// edit the web.xml
			editWebXml(webXml);
			// write the back the web.xml
			writeWebXml(explodedArchive, webXml);
			// add any specified jars to WEB-INF/lib
			addLibs(explodedArchive);
			// automatically add any required jars to WEB-INF/lib
			autoAddLibs(explodedArchive);
			// add any classes to WEB-INF/classes
			addClasses(explodedArchive);
			
			// implode the archive to the specified destfile
			Utils.archive(explodedArchive, destfile);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Edit the web.xml by adding the specified JSFUnit filters
	 * @param webXml webXml object to manipulate
	 * @return the edited webXML object
	 */
	private WebXml editWebXml (WebXml webXml)
	{
		// if there is no filter specified, then create the default JSFUnit filter
		if (jsfFilters.isEmpty())
		{
			jsfFilters.add(new JSFUnitFilter());
		}

		// add each filter to the web.xml
		for (java.util.Iterator<JSFUnitFilter> i = jsfFilters.iterator(); i.hasNext();)
		{
			i.next().addFilter(webXml);
		}

		for (java.util.Iterator<JSFUnitTestRunnerFilter> i = testRunnerFilters.iterator(); i.hasNext();)
		{
			i.next().addFilter(webXml);
		}
		
		return webXml;
	}


	/**
	 * Adds a list of files to the archive
	 * @param archiveRoot the archive
	 * @param directoryName the name of the directory to add the files
	 * @param fileSets the files to add
	 * @throws Exception if an exception occurs during the file transfers
	 */
	private void addFilesToArchive (File archiveRoot, String directoryName, List<FileSet> fileSets) throws Exception{
		// if there are no  requested to be added, then do nothing
		if (!fileSets.isEmpty())
		{
			// file object to represent the directory
			File directory = new File (archiveRoot.getPath() + File.separator + directoryName);

			if (!directory.exists())
			{
				//only create the directory if it doesn't already exist
				directory.mkdir();
			}
			else if (!directory.isDirectory())
			{
				throw new Exception ("The " + directoryName + " directory for the archive is not a directory");
			}

			for (java.util.Iterator<FileSet> i = fileSets.iterator(); i.hasNext();)
			{
				FileSet fileSet = i.next();
				DirectoryScanner directoryScanner = fileSet.getDirectoryScanner(getProject());
				String[] files = directoryScanner.getIncludedFiles();
				for (int j=0; j<files.length;j++)
				{
					File file = new File(fileSet.getDir(getProject()) + File.separator + files[j]);
					File dest = new File(directory + File.separator + files[j]);
					if (dest.getParentFile() != null)
					{
						dest.getParentFile().mkdirs();
					}
					Utils.copy(file, dest);
				}
			}
		}
	}

	/**
	 * Add the specified jars to the WEB-INF/lib directory of the archive
	 * @param archiveRoot the archive
	 * @throws Exception if an exception occured during the file transfer
	 */
	private void addLibs (File archiveRoot) throws Exception
	{
		String libsDirectory = "WEB-INF" + File.separator + "lib";
		addFilesToArchive(archiveRoot, libsDirectory, libs);
	}

	/**
	 * Add the specified classes to the WEB-INF/classes directory of the archive
	 * @param archiveRoot the archive
	 * @throws Exception if an exception occured during the file transfer
	 */
	private void addClasses (File archiveRoot) throws Exception
	{
		String classesDirectory = "WEB-INF" + File.separator + "classes";
		addFilesToArchive(archiveRoot, classesDirectory, classes);
	}

	/**
	 * Automatically add the jars that are needed for JSFUnit into the WEB-INF/lib directory 
	 * @param archiveRoot The archive
	 * @throws Exception If an exception occurs during file transfer
	 */
	private void autoAddLibs (File archiveRoot) throws Exception
	{
		log("Automatically adding JSFunit required jars to the new war", Project.MSG_INFO);
		if (autoAddJars)
		{
			// TODO: pick out actual classes used, these ones were picked at random from the jars
			String[][] classNames = new String[][] {
					{"/org/jboss/jsfunit/framework/JSFUnitFilter.class", "JSFUnit"},
					{"/org/aspectj/runtime/CFlow.class", "AspectJ"},
					{"/org/apache/cactus/Request.class", "Cactus"},
					{"/com/meterware/httpunit/HttpUnitUtils.class", "HttpUnit"},
					{"/junit/framework/Assert.class", "JUnit"},
					{"/org/cyberneko/html/HTMLComponent.class", "NekoHTML"},
					{"/org/w3c/tidy/Tidy.class", "JTidy"}
			};


			for (int i=0; i<classNames.length; i++)
			{
				try{
				File jar = ResourceUtils.getResourceLocation(classNames[i][0]);
				if (jar == null)
				{
					log("Could not find the " + classNames[i][1] + " jar in the classpath. Cannot automatically add this jar to the war");
				} 
				else
				{
					addLib (archiveRoot, jar);
				}
				}
				catch (NoClassDefFoundError ncdfe)
				{
					log("Could not find the " + classNames[i][1] + " jar in the classpath. Cannot automatically add this jar to the war");
				}
			}
		}
	}

	/**
	 * Add a specified File into the WEB-INF/lib directory of the archive
	 * @param archiveRoot the archive
	 * @param libJar the file to add
	 * @throws Exception if an exception occured during file transfer
	 */
	private void addLib (File archiveRoot, File libJar) throws Exception
	{
		if (!libJar.exists()){
			throw new Exception ("Can not find " + libJar.getName());
		}
		String libDirectoryName = archiveRoot.getPath() + File.separator + "WEB-INF" + File.separator + "lib";
		File libDir = new File (libDirectoryName);
		if (!libDir.exists())
		{
			libDir.mkdir();
		}
		String libFileName = libDirectoryName + File.separator + libJar.getName();
		File destFile = new File(libFileName);
		Utils.copy(libJar, destFile);
	}

}