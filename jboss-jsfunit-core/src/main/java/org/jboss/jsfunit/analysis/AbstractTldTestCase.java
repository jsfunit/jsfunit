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

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.faces.webapp.UIComponentTag;
import javax.faces.webapp.UIComponentTagBase;
import javax.xml.parsers.DocumentBuilder;

import junit.framework.TestCase;
import net.sf.maventaglib.checker.Tag;
import net.sf.maventaglib.checker.TagAttribute;
import net.sf.maventaglib.checker.Tld;
import net.sf.maventaglib.checker.TldParser;

import org.jboss.jsfunit.analysis.util.ClassUtils;
import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Document;

/**
 * @author Dennis Byrne
 */

public abstract class AbstractTldTestCase extends TestCase {

	protected Map<String, Tld> tldsByPath = new HashMap<String, Tld>();
	protected Map<String, Document> documentsByPath = new HashMap<String, Document>();
	private StreamProvider streamProvider;
	// wish mvn-tagib had a Tag.getTld()
	private Map<Tag, Tld> tldsByTag = new HashMap<Tag, Tld>();
	// wish mvn-taglib had a 'Class getTagClass()'
	private Map<Tag, Class> tagClassesByTag = new HashMap<Tag, Class>();
	
	public AbstractTldTestCase(Set<String> tldPaths) {
		this(tldPaths, new DefaultStreamProvider());
	}

	public AbstractTldTestCase(final String tldPath) {
		this(new HashSet<String>() {{add(tldPath);}}, new DefaultStreamProvider());
	}
	
	AbstractTldTestCase(Set<String> tldPaths, StreamProvider streamProvider) {
		
		if(streamProvider == null)
			throw new IllegalArgumentException("stream provider is null");
		
		if(tldPaths == null || tldPaths.size() == 0)
			throw new IllegalArgumentException("tldPaths was null or empty. At least one TLD needed");
		
		this.streamProvider = streamProvider;
		parseResources(tldPaths);
	}
	
	private void parseResources(Set<String> tldPaths) {
		
		DocumentBuilder builder = ParserUtils.getDocumentBuilder();
		
		for(String tldPath : tldPaths){
			String xml = ParserUtils.getXml(tldPath, streamProvider);
			Tld tld;
			Document document;
			try {
				document = builder.parse( new ByteArrayInputStream(xml.getBytes()));
				tld = TldParser.parse(document, tldPath);
			} catch (Exception e) {
				throw new RuntimeException("Could not parse document '" + tldPath + "'\n" + xml, e);
			}
			tldsByPath.put(tldPath, tld);
			documentsByPath.put(tldPath, document);
			trim(tldPath, tld);
		}
	}
	
	private void trim(String tldPath, Tld tld) {
		
		if( tld.getName() == null || "".equals(tld.getName().trim()) )
			throw new RuntimeException("TLD in " + tldPath + " has no name");
		
		tld.setName(tld.getName().trim());
		
		for(Tag tag : tld.getTags()) {
			
			tldsByTag.put(tag, tld);
			
			if(tag.getName() == null || "".equals(tag.getName().trim()))
				throw new RuntimeException("tag in " + tldPath + " has no name");
			tag.setName(tag.getName().trim());
			
			String tagClass = tag.getTagClass();
			Class clazz = new ClassUtils().loadClass(tagClass, "tag-class");
			tagClassesByTag.put(tag, clazz);
			
			for(TagAttribute attribute : tag.getAttributes()) {
				String type = attribute.getAttributeType();
				type = type == null ? type : type.trim();
			}
		}
		
	}
	
	public void testInheritance() {

		Set<Tag> tags = tagClassesByTag.keySet();
		
		for(Tag tag : tags) {
			
			Class clazz = tagClassesByTag.get(tag);
			Class[] constraints = new Class[] {UIComponentTag.class, UIComponentTagBase.class};
			
			if( ! new ClassUtils().isAssignableFrom(constraints, clazz) ) {
				Tld tld = tldsByTag.get(tag);
				fail(clazz + " configured in TLD '" 
						+ tld.getName() + "' needs to be a " 
						+ UIComponentTag.class.getName() + " or a " + UIComponentTagBase.class.getName());			
			}
		}

	}
	
	public void testTagAttributeTypes() {
		
		new TagAttributeTypesImpl(tldsByPath.values(), tagClassesByTag).test();
		
	}
	
	public void testUniqueTagNames() {
		
		new UniqueTagNamesImpl(tldsByPath).test();
		
	}
	
	public void testUniqueTagAttributes() {
	
		new UniqueTagAttributesImpl(tldsByPath.values()).test();
		
	}
	
}