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

import java.util.HashSet;

import org.jboss.jsfunit.analysis.AbstractTldTestCase;
import org.jboss.jsfunit.analysis.StreamProvider;
import org.jboss.jsfunit.analysis.model.Pojo;
import org.jboss.jsfunit.analysis.model.UIComponentClassicTagBaseChild;

import junit.framework.TestCase;

public class Tld_JSFUNIT_28_TestCase extends TestCase {

	public void testHappyPathUIComponentClassicTagBase() {
		
		String tag = getTag("goodTag", UIComponentClassicTagBaseChild.class);
		String tld = getTld(tag, "A good tag library.");
		StreamProvider streamProvider = new StringStreamProvider(tld);
		
		new AbstractTldTestCase(TestUtils.STUBBED_RESOURCEPATH, streamProvider) {}.testInheritance();
		
	}
	
	public void testNonUIComponentClassicTagBase() {
		
		String tag = getTag("badTag", Pojo.class);
		String tld = getTld(tag, "A bad tag library.");
		StreamProvider streamProvider = new StringStreamProvider(tld);
		
		try {

			new AbstractTldTestCase(TestUtils.STUBBED_RESOURCEPATH, streamProvider) {}.testInheritance();

			fail();
			
		}catch(Exception e) {}
	}
	
	private String getTag(String name, Class clazz) {
		return "<tag>"
		+ "<name>" + name + "</name>"
		+ "<tag-class>" + clazz.getName() + "</tag-class>"
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