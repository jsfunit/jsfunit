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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;

import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Document;

/**
 * @author Dennis Byrne
 * @since 1.0
 */

public class AbstractViewTestCase extends TestCase {

	protected Map<String, Document> viewsByPath;
	protected Map<String, Document> configByPath;
	protected ViewParser parser;
	
	public AbstractViewTestCase(Set<String> absolutePaths, Set<String> recursivePaths, Set<String> configPaths ){
		this(absolutePaths, recursivePaths, configPaths, new DefaultStreamProvider());
	}

	public AbstractViewTestCase(Set<String> absolutePaths, Set<String> recursivePaths, final String configPath ){
		this(absolutePaths, recursivePaths, new HashSet<String>() {{add(configPath);}});
	}

	public AbstractViewTestCase(Set<String> absolutePaths, final String recursivePath, final String configPath ){
		this(absolutePaths, new HashSet<String>() {{add(recursivePath);}}, new HashSet<String>() {{add(configPath);}});
	}

	public AbstractViewTestCase(final String absolutePath, final String recursivePath, final String configPath ){
		this(new HashSet<String>() {{add(absolutePath);}}, new HashSet<String>() {{add(recursivePath);}}, new HashSet<String>() {{add(configPath);}});
	}	
	
	AbstractViewTestCase(Set<String> absolutePaths, Set<String> recursivePaths, Set<String> configPaths, 
			StreamProvider streamProvider){
	
		if(streamProvider == null)
			throw new IllegalArgumentException("stream provider is null");
		
		if(absolutePaths == null && recursivePaths == null)
			throw new IllegalArgumentException("absolutePaths and recursivePaths are null ... "
					+ AbstractViewTestCase.class.getName() + " needs at least one path to a view");
		if(absolutePaths == null)
			absolutePaths = new HashSet<String>();
		if(recursivePaths == null)
			recursivePaths = new HashSet<String>();
		
		if(configPaths == null)
			throw new IllegalArgumentException("configPaths is null");
		if(configPaths.size() == 0)
			throw new IllegalArgumentException("configPaths empty, must supply at least one");
		
		Set<String> allPaths = new HashSet<String>(absolutePaths);
		for(String path : recursivePaths) {
			explode(new File(path), allPaths, "xhtml");
			explode(new File(path), allPaths, "jsp");
		}
		
		if(allPaths.size() == 0)
			throw new IllegalArgumentException("No view templates found. At least one must be specified");
		
		viewsByPath = new HashMap<String, Document>();
		configByPath = new HashMap<String, Document>();
		parser = new ViewParser();
		
		parseResources(allPaths, streamProvider, viewsByPath);
		parseResources(configPaths, streamProvider, configByPath);
		
		for(String path : viewsByPath.keySet() )
			parser.parse(viewsByPath.get(path), path);
		
	}

	private void parseResources(Set<String> allPaths, StreamProvider streamProvider,
			final Map<String, Document> documentsByPath) {

		DocumentBuilder builder = ParserUtils.getDocumentBuilder();

		for(String path : allPaths) {
			InputStream stream = null;
			try {
				stream = streamProvider.getInputStream(path);
				documentsByPath.put(path, builder.parse(stream));
			} catch (Exception e) {
				throw new RuntimeException("Could not parse file '" + path + "'", e);
			}finally {
				
				if(stream != null)
					try {
						stream.close();
					} catch (IOException e) {
						throw new RuntimeException("Could not close stream for " + path);
					}
			}
			
		}
	}

	public static void explode(File file, final Set<String> files, String ext) {
		
        if (file.isDirectory() ) {
            
        	String[] children = file.list();
            for (int i=0; i<children.length; i++)
                explode(new File(file, children[i]), files, ext);
            
        } else if(file.getName().endsWith(ext)) {
			
        	files.add(file.getAbsolutePath());
        	
        }
        
    }

	public void testActions() {
		
		new ViewConfigReconciler(null, parser.getActions(), configByPath).reconcileActions();
		
	}
	
	public void testActionListeners() {
		
		new ViewConfigReconciler(parser.getActionListeners(), null, configByPath).reconcileActionListeners();
		
	}

}