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

import java.util.Collection;
import java.util.Map;

import javax.faces.webapp.UIComponentTag;

import net.sf.maventaglib.checker.Tag;
import net.sf.maventaglib.checker.TagAttribute;
import net.sf.maventaglib.checker.Tld;

class TagAttributeTypesImpl {

	private Collection<Tld> tlds;
	private Map<Tag, Class> tagClassesByTag; 
	
	public TagAttributeTypesImpl(Collection<Tld> tlds, Map<Tag, Class> tagClassesByTag) {
		this.tlds = tlds;
		this.tagClassesByTag = tagClassesByTag;
	}
	
	public void test(){
		
		for(Tld tld : tlds) {
			for(Tag tag : tld.getTags()) {
				if( UIComponentTag.class.isAssignableFrom(tagClassesByTag.get(tag))) {
					for(TagAttribute attribute : tag.getAttributes()) {
						if( ! String.class.getName().equals(attribute.getAttributeType()) ) {
							throw new RuntimeException("Tag '" + tag.getName() + "' in TLD " 
									+ "'" + tld.getName() + "' is a " + UIComponentTag.class.getName()
									+ ". Becuase it is a JSF 1.1 tag, each tag attribute must be of " 
									+ "type " + String.class.getName() + "', however attribute '" 
									+ attribute.getAttributeName() + "' is of type " + attribute.getAttributeType()
									+ " See JSF Spec 1.2 section 9.3.1.1 for more information.");
						}
					}
				}
			}
		}
		
	}
	
}