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

import java.util.HashSet;

import junit.framework.TestCase;

public class Tld_JSFUNIT_28_TestCase extends TestCase {

	public void testHappyPath() {
		
		String tag = "<tag>"
			+ "<name>goodTag</name>"
			+ "<tag-class>" + UIComponentClassicTagBaseChild.class.getName() + "</tag-class>"
			+ "<body-content>JSP</body-content>"
			+ "<description />"
			+ "<attribute>"
				+ "<name>id</name>"
				+ "<required>false</required>"
				+ "<rtexprvalue>false</rtexprvalue>"
				+ "<type>java.lang.String</type>"
				+ "<description />"
			+ "</attribute>"
			+ "</tag>";
		String tld = getTld(tag, "A good tag library.");
		StreamProvider streamProvider = new StringStreamProvider(tld);
		
		new AbstractTldTestCase(TestUtils.STUBBED_RESOURCEPATH, streamProvider) {};
		
	}
	
	public void testEmptyLib() {
		
		String tld = getTld("", "An empty tag library.");
		StreamProvider streamProvider = new StringStreamProvider(tld);
		
		new AbstractTldTestCase(TestUtils.STUBBED_RESOURCEPATH, streamProvider) {};
		
	}
	
	public void testMissingResource() {

		try {

			new AbstractTldTestCase(null) {};

			fail();

		} catch (Exception t) {
		}

		try {

			new AbstractTldTestCase(new HashSet<String>()) {};

			fail();

		} catch (Exception t) {
		}

	}
	
	public void testNonExistingResource() {
		
		try {

			new AbstractTldTestCase(new HashSet<String>() {{
				add("not there");
			}}) {};

			fail();

		} catch (Exception t) {
		}		
	}
	
	private String getTld(String body, String display) {
		
		return "<!DOCTYPE taglib PUBLIC \"-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN\" " 
		+ "\"http://java.sun.com/dtd/web-jsptaglibrary_1_2.dtd\">"
		+ "<taglib xmlns=\"http://java.sun.com/JSP/TagLibraryDescriptor\">"
		+ "<tlib-version>1.0</tlib-version>"
		+ "<jsp-version>1.2</jsp-version>"
		+ "<short-name>jsfunit</short-name>"
		+ "<uri>http://labs.jboss.com/jsfunit/</uri>"
		+ "<display-name>" + display + "</display-name>"
		+ "<description />"
		+ body
		+ "</taglib>";
		
	}
}