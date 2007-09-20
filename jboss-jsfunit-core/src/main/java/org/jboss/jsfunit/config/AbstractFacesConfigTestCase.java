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

package org.jboss.jsfunit.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.w3c.dom.Document;

/**
 * @author Dennis Byrne
 */

public abstract class AbstractFacesConfigTestCase extends TestCase {

	protected final Map<String, Document> documentsByPath = new HashMap<String, Document>();
	
	private StreamProvider streamProvider;
	
	public AbstractFacesConfigTestCase(Set<String> facesConfigPaths) {
		this(facesConfigPaths, new ResourceUtils());
	}
	
	AbstractFacesConfigTestCase(Set<String> facesConfigPaths, StreamProvider streamProvider) {
		
		if(streamProvider == null)
			throw new IllegalArgumentException("stream provider is null");
		
		if(facesConfigPaths == null || facesConfigPaths.isEmpty())
			throw new IllegalArgumentException("facesConfigPaths is null or empty");
		
		this.streamProvider = streamProvider;
		parseResources(facesConfigPaths);
		
	}

	private void parseResources(Set<String> facesConfigPaths) {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		factory.setIgnoringElementContentWhitespace(true);
		
		DocumentBuilder builder = null;
		
		try {
			 builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Could not create a " + DocumentBuilder.class.getName());
		}
		
		for(String facesConfigPath : facesConfigPaths){
			String xml = getXml(facesConfigPath);
			Document document = null;
			try {
				document = builder.parse( new ByteArrayInputStream(xml.getBytes()));
			} catch (Exception e) {
				throw new RuntimeException("Could not parse document '" + facesConfigPath + "'\n" + xml, e);
			}
			documentsByPath.put(facesConfigPath, document);
		}
	}
	
	private String getXml(String resourcePath) {
		InputStream stream = streamProvider.getInputStream(resourcePath);
		
		if(stream == null)
			throw new RuntimeException("Could not locate faces config file '" + resourcePath + "'" );
		
		String xml = new ResourceUtils().getAsString(stream, resourcePath);
		
		// TODO find a better way to prevent DOM from going to the Internet
		int indexOf = xml.indexOf("<faces-config");
		if(indexOf > 0)
			xml = xml.substring(indexOf, xml.length());
		return xml;
	}

	public void testClassDefinitions() {
		
		new ClassDefinitionsTest(documentsByPath).scrutinize(); // delegate method logic to another class
		
	}
	
	public void testManagedBeans() {

		new ManagedBeansTest(documentsByPath).scrutinize(); // delegate method logic to another class
		
	}
	
}