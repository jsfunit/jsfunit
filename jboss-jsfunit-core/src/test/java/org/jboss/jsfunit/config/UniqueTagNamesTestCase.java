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

import java.util.HashMap;

import net.sf.maventaglib.checker.Tag;
import net.sf.maventaglib.checker.Tld;
import junit.framework.TestCase;

public class UniqueTagNamesTestCase extends TestCase {

	public void testNameCollision() {
		
		try {
			
			scrutinize("same", "same");
			
			fail();
			
		}catch(Exception e) {}
		
	}
	
	public void testHappyPath() {
	
		scrutinize("firstTag", "secondTag");
		
	}

	private void scrutinize(String firstTagName, String secondTagName) {
		
		Tag first = new Tag();
		first.setName(firstTagName);
		
		Tag second = new Tag();
		second.setName(secondTagName);
		
		final Tld firstTld = new Tld();
		firstTld.setName("firstTld");
		firstTld.setTags(new Tag[] {first, second});

		final Tld secondTld = new Tld();
		secondTld.setName("secondTld");
		secondTld.setTags(new Tag[] {first, second});

		UniqueTagNamesTest test = new UniqueTagNamesTest(new HashMap<String, Tld>(){{
				put("firstPath", firstTld);
				put("secondPath", secondTld);
			}}
		);
		
		test.scrutinize();
	}
	
}
