/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.jsfunit.init;

import java.util.Set;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;

/**
 * This class is a test suite that contains all the JSFUnit tests found
 * in the WAR.  This is the test that is called from the JSFUnit console to
 * run all tests.
 * 
 * @author Stan Silvert
 * @since 1.2
 */
public class AllJSFUnitTests extends ServletTestCase
{

   private static Set<Class<?>> allTests;

   /**
    * This is called by the JSFUnitSCI to set all the test classes.
    *
    * @param tests The Set of all test classes found in the WAR.
    */
   static void setAllTests(Set<Class<?>> tests)
   {
       allTests = tests;
   }

   /**
    * @return the suite of tests being tested
    */
   public static Test suite()
   {
      TestSuite suite = new TestSuite();
      for (Class test : allTests)
      {
        suite.addTestSuite(test);
      }
       
      return suite;
   }
   
}
