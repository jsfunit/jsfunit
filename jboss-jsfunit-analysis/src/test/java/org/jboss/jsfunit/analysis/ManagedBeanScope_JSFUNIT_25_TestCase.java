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

import junit.framework.TestCase;

/**
 * @author Dennis Byrne
 */

public class ManagedBeanScope_JSFUNIT_25_TestCase extends TestCase {

   public void testDummy() {
      /*
       * The problem here is (with the issue JSFUNIT-25 is, that JSF allows for such duplicate 
       * bean definitions and has well documented rules on how to deal with such a duplicate 
       * configuration issue.
       * That's why the validity of such a duplicate test should be questioned. And as such the 
       * test is not migrated to the the new structure. For docuemtnation issue not deleted but 
       * merely commented out.
       */
   }
//	public void testDuplicateManagedBean() {
//		
//		String manageBean = TestUtils.getManagedBean("mirror", Pojo.class, "none");
//		String facesConfig = TestUtils.getFacesConfig(manageBean + manageBean);
//		StreamProvider streamProvider = new StringStreamProvider(facesConfig);
//		
//		try {
//			
//			new AbstractFacesConfigTestCase(TestUtils.STUBBED_RESOURCEPATH, streamProvider) {}.testManagedBeans();
//			
//			throw new RuntimeException("should have failed");
//			
//		}catch(AssertionFailedError e) { }
//		
//	}
//
//	public void testDuplicateManagedBeansDifferentConfigSource() {
//
//		String manageBean = TestUtils.getManagedBean("mirror", Pojo.class, "none");
//		String facesConfig = TestUtils.getFacesConfig(manageBean);
//		StreamProvider streamProvider = new StringStreamProvider(facesConfig);
//
//		Set<String> facesConfigPaths = new HashSet<String>() {{
//			add("one path");
//			add("two paths");
//		}};
//
//		try {
//
//			new AbstractFacesConfigTestCase(facesConfigPaths, streamProvider) {}.testManagedBeans();
//
//			throw new RuntimeException("should have failed");
//
//		}catch(AssertionFailedError e) { }		
//	}

}