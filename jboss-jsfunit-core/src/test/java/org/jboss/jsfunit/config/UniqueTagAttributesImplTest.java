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

import java.util.ArrayList;

import net.sf.maventaglib.checker.Tag;
import net.sf.maventaglib.checker.TagAttribute;
import net.sf.maventaglib.checker.Tld;
import junit.framework.TestCase;

public class UniqueTagAttributesImplTest extends TestCase {

	public void testNameCollision() {
		
		try {
			
			scrutinize("same", "same");
			
			fail();
			
		}catch(Exception e) {}
		
	}
	
	public void testHappyPath() {
		
		scrutinize("id", "value");
		
	}

	private void scrutinize(String firstName, String secondName) {
		
		TagAttribute id = new TagAttribute();
		id.setAttributeName(firstName);
		
		TagAttribute value = new TagAttribute();
		value.setAttributeName(secondName);
		
		Tag tag = new Tag();
		tag.setAttributes(new TagAttribute[] {id, value});
		
		final Tld tld = new Tld();
		tld.setTags(new Tag[] {tag});
		
		new UniqueTagAttributesImpl(new ArrayList<Tld>() {{
			add(tld);
		}}).test();
	}
	
}
