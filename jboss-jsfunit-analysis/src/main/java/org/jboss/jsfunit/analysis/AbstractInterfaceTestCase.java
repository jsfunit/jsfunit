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

import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.util.ClassUtils;

/**
 * A TestCase for a single JSF element that needs to extend a certain class.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $$Revision:  $$
 */
public abstract class AbstractInterfaceTestCase extends TestCase
{
   /** name of the className */
   protected String className = null;

   /** name of the jsfElement */
   protected String jsfElement = null;

   /** class of the classClass */
   protected Class<?> classClass = null;

   /**
    * Create a new AbstractClassExtensionTestCase.
    * 
    * @param name The name of the test-case in the JUnit test-hierarchy
    * @param className the name of the className to test
    */
   public AbstractInterfaceTestCase(String name, String jsfElement, String className)
   {
      super(name);
      this.jsfElement = jsfElement;
      this.className = className;
   }

   /**
    * Call the individual test methods.
    */
   public void runTest()
   {
      testClassLoadable();
      testInterface();
   }

   /**
    * Assert that the configured class can be loaded from the classpath
    */
   public void testClassLoadable()
   {
      getClassExtensionClass();
   }

   /**
    * Assert that the class implements the desired interface as per JSF specification.
    */
   public void testInterface()
   {
      Class<?>[] constraints = new Class<?>[1];
      constraints[0] = getClassToExtend();
      if (!(new ClassUtils().isAssignableFrom(constraints, getClassExtensionClass())))
      {
         fail(jsfElement + " '" + className + "' needs to implement " + getClassToExtend());
      }
   }

   /**
    * Accessor for the class name.
    * 
    * @param className
    */
   protected void setClassName(String className)
   {
      this.className = className;
   }

   /** 
    * <p>Accessor for the class instance. If the class is not yet loaded, then it will be loaded.</p>
    * 
    * @return the loaded class
    */
   private Class<?> getClassExtensionClass()
   {
      if (classClass == null)
      {
         classClass = new ClassUtils().loadClass(className, jsfElement);
      }
      return classClass;
   }

   /**
    * Return the class to be extended, this munst be implemented by the specific TestCase.
    * 
    * @return the class to be extended
    */
   abstract protected Class<?> getClassToExtend();
}
