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
 * 
 */
package org.jboss.jsfunit.analysis;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A TestSuite that contains all development related TestCases.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class AllDevelopmentTests
{
   /**
    * Build a standard JUnit TestSuite.
    * 
    * @return valid JUnit Test (-Suite)
    */
   public static Test suite()
   {
      TestSuite suite = new TestSuite("Development testcases");
      //$JUnit-BEGIN$
      suite.addTestSuite(ActionListenerTestCase_TestCase.class);
      suite.addTestSuite(ApplicationFactoryTestCase_TestCase.class);
      suite.addTestSuite(ApplicationTestSuite_TestCase.class);
      suite.addTestSuite(ComponentTestCase_TestCase.class);
      suite.addTestSuite(ConfigFilesTestSuite_TestCase.class);
      suite.addTestSuite(ConfigFileTestSuite_TestCase.class);
      suite.addTestSuite(ConverterTestCase_TestCase.class);
      suite.addTestSuite(DefaultRenderkitTestCase_TestCase.class);
      suite.addTestSuite(ElResolverTestCase_TestCase.class);
      suite.addTestSuite(FacesContextFactoryTestCase_TestCase.class);
      suite.addTestSuite(FactoryTestSuite_TestCase.class);
      suite.addTestSuite(LifecycleTestCase_TestCase.class);
      suite.addTestSuite(LifecycleTestSuite_TestCase.class);
      suite.addTestSuite(LifecycleFactoryTestCase_TestCase.class);
      suite.addTestSuite(ManagedBeanTestCase_TestCase.class);
      suite.addTestSuite(ManagedBeanTestSuite_TestCase.class);
      suite.addTestSuite(ManagedPropertyTestCase_TestCase.class);
      suite.addTestSuite(NavigationCaseTestCase_TestCase.class);
      suite.addTestSuite(NavigationHandlerTestCase_TestCase.class);
      suite.addTestSuite(NavigationRuleTestCase_TestCase.class);
      suite.addTestSuite(NavigationRuleTestSuite_TestCase.class);
      suite.addTestSuite(PhaseListenerTestCase_TestCase.class);
      suite.addTestSuite(PropertyResolverTestCase_TestCase.class);
      suite.addTestSuite(RendererTestCase_TestCase.class);
      suite.addTestSuite(RenderKitFactoryTestCase_TestCase.class);
      suite.addTestSuite(RenderKitTestCase_TestCase.class);
      suite.addTestSuite(RenderKitTestSuite_TestCase.class);
      suite.addTestSuite(StateManagerTestCase_TestCase.class);
      suite.addTestSuite(ValidatorTestCase_TestCase.class);
      suite.addTestSuite(VariableResolverTestCase_TestCase.class);
      suite.addTestSuite(ViewHandlerTestCase_TestCase.class);
      //$JUnit-END$
      return suite;
   }

}
