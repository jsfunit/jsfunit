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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.webapp.UIComponentTag;
import javax.faces.webapp.UIComponentTagBase;

import org.jboss.jsfunit.analysis.TagAttributeTypesImpl;

import net.sf.maventaglib.checker.Tag;
import net.sf.maventaglib.checker.TagAttribute;
import net.sf.maventaglib.checker.Tld;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * @author Dennis Byrne
 */

public class TagAttributeTypesImplTest extends TestCase{

	public void testInvalidTagTypeJsf1_1() {
		
		try {
			
			scrutinize("bad", Integer.class, UIComponentTag.class);
			throw new RuntimeException();
			
		}catch(AssertionFailedError e) {}
		
	}

	public void testHappyPathJsf1_1() {
		
		scrutinize("good", String.class, UIComponentTag.class);
		
	}
	
	public void testNoFalseAlarmForJsf1_2Tags() {
		
		scrutinize("good", Double.class, UIComponentTagBase.class);
		
	}
	
	private void scrutinize(String tagName, Class attributeClass, Class tagClass) {
		
		TagAttribute attribute = new TagAttribute();
		attribute.setAttributeName(tagName);
		attribute.setAttributeType(attributeClass.getName());
		
		Tag tag = new Tag();
		tag.setTagClass(tagClass.getName());
		tag.setAttributes(new TagAttribute[] {attribute});
		
		Tld tld = new Tld();
		tld.setTags(new Tag[] {tag});
		
		Map<Tag, Class> tagClassesByTag = new HashMap<Tag, Class>();
		tagClassesByTag.put(tag, tagClass);
		
		List<Tld> tlds = new LinkedList<Tld>();
		tlds.add(tld);
		
		new TagAttributeTypesImpl(tlds, tagClassesByTag).test();
	}
	
}