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

package org.jboss.jsfunit.analysis;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;

import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Document;

/**
 * @author Dennis Byrne
 */

public class AbstractViewTestCase extends TestCase {

	protected Map<String, Document> viewsByPath;
	protected Map<String, Document> configByPath;
	
	public AbstractViewTestCase(Set<String> absolutePaths, Set<String> recursivePaths, Set<String> configPaths ){
		this(absolutePaths, recursivePaths, configPaths, new DefaultStreamProvider());
	}
	
	AbstractViewTestCase(Set<String> absolutePaths, Set<String> recursivePaths, Set<String> configPaths, 
			StreamProvider streamProvider){
	
		if(streamProvider == null)
			throw new IllegalArgumentException("stream provider is null");
		if(absolutePaths == null)
			throw new IllegalArgumentException("absolutePaths is null");
		if(recursivePaths == null)
			throw new IllegalArgumentException("recursivePaths is null");
		if(configPaths == null)
			throw new IllegalArgumentException("configPaths is null");
		if(configPaths.size() == 0)
			throw new IllegalArgumentException("configPaths empty, must supply at least one");
		
		Set<String> allPaths = new HashSet<String>(absolutePaths);
		for(String path : recursivePaths) 
			allPaths.addAll(explode(path));
		
		if(allPaths.size() == 0)
			throw new IllegalArgumentException("No view templates found. At least one must be specified");

		parseResources(allPaths, streamProvider, viewsByPath);
		parseResources(configPaths, streamProvider, configByPath);
	}

	private void parseResources(Set<String> allPaths, StreamProvider streamProvider,
			final Map<String, Document> documentsByPath) {

		DocumentBuilder builder = ParserUtils.getDocumentBuilder();

		for(String path : allPaths) {
			try {
				InputStream stream = streamProvider.getInputStream(path);
				documentsByPath.put(path, builder.parse(stream));
			} catch (Exception e) {
				throw new RuntimeException("Could not parse file '" + path + "'", e);
			}
		}
	}

	private Set<String> explode(String path) {
		
		throw new UnsupportedOperationException();
		
	}
	
	public void testActionListeners() {
		
		throw new UnsupportedOperationException();		
		
	}

}