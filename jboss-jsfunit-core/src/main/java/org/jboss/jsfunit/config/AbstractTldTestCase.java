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

package org.jboss.jsfunit.config;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.faces.webapp.UIComponentClassicTagBase;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;

import junit.framework.TestCase;
import net.sf.maventaglib.checker.Tag;
import net.sf.maventaglib.checker.Tld;
import net.sf.maventaglib.checker.TldParser;

/**
 * @author Dennis Byrne
 */

public abstract class AbstractTldTestCase extends TestCase {

	protected Map<String, Tld> tldsByPath = new HashMap<String, Tld>();
	private StreamProvider streamProvider;
	
	public AbstractTldTestCase(Set<String> tldPaths) {
		this(tldPaths, new DefaultStreamProvider());
	}
	
	AbstractTldTestCase(Set<String> tldPaths, StreamProvider streamProvider) {
		
		if(streamProvider == null)
			throw new IllegalArgumentException("stream provider is null");
		
		if(tldPaths == null || tldPaths.size() == 0)
			throw new IllegalArgumentException("tldPaths was null or empty. At least one TLD needed");
		
		this.streamProvider = streamProvider;
		parseResources(tldPaths);
	}
	
	private void parseResources(Set<String> facesConfigPaths) {
		
		DocumentBuilder builder = ParserUtils.getDocumentBuilder();
		
		for(String facesConfigPath : facesConfigPaths){
			String xml = ParserUtils.getXml(facesConfigPath, streamProvider);
			Tld tld;
			try {
				Document document = builder.parse( new ByteArrayInputStream(xml.getBytes()));
				tld = TldParser.parse(document, facesConfigPath);
			} catch (Exception e) {
				throw new RuntimeException("Could not parse document '" + facesConfigPath + "'\n" + xml, e);
			}
			tldsByPath.put(facesConfigPath, tld);
		}
	}
	
	public void testInheritance() {

		Set<String> tlds = tldsByPath.keySet();
		
		for(String tldPath : tlds) {
			
			Tld tld = tldsByPath.get(tldPath);
			
			for(Tag tag : tld.getTags()) {
				
				String tagClass = tag.getTagClass();
				Class clazz = new ClassUtils().loadClass(tagClass, "tag-class");
				
				if( ! UIComponentClassicTagBase.class.isAssignableFrom(clazz) )
					throw new RuntimeException(tagClass + " configured in " 
							+ tldPath + " needs to be a " + UIComponentClassicTagBase.class.getName());
			}
		}
		
	}
}
