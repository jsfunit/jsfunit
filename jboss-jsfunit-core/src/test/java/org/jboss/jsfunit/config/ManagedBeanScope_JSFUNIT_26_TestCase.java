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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class ManagedBeanScope_JSFUNIT_26_TestCase extends TestCase {

	public void testHappyPaths() {
		
		List<String> scopes = new ArrayList<String>() {{
			add("request");
			add("none");
		}};
		
		for(String scope : scopes) {
			String manageBean = TestUtils.getManagedBean("good", Pojo.class, scope);
			String facesConfig = TestUtils.getFacesConfig(manageBean);
			StreamProvider streamProvider = new StringStreamProvider(facesConfig);
			new AbstractFacesConfigTestCase(TestUtils.STUBBED_RESOURCEPATH, streamProvider) {}.testManagedBeans();
		}
		
		scopes.add("session");
		scopes.add("application");
		
		for(String scope : scopes) {
			String manageBean = TestUtils.getManagedBean("good2", SerializablePojo.class, scope);
			String facesConfig = TestUtils.getFacesConfig(manageBean);
			StreamProvider streamProvider = new StringStreamProvider(facesConfig);
			new AbstractFacesConfigTestCase(TestUtils.STUBBED_RESOURCEPATH, streamProvider) {}.testManagedBeans();
		}
		
	}
	
	public void testNotSerializable() {

		List<String> scopes = new ArrayList<String>() {{
			add("session");
			add("application");
		}};
		
		for(String scope : scopes) 
			testNotSerializable(TestUtils.getManagedBean("bad", Pojo.class, scope));
	}
	
	private void testNotSerializable(String manageBean) {
		
		String facesConfig = TestUtils.getFacesConfig(manageBean);
		StreamProvider streamProvider = new StringStreamProvider(facesConfig);
		
		try {
			
			new AbstractFacesConfigTestCase(TestUtils.STUBBED_RESOURCEPATH, streamProvider) {}.testManagedBeans();
			
			fail("should have failed because " + Pojo.class + " is not " + Serializable.class);
			
		}catch(Exception e) { }
	}
	
}
