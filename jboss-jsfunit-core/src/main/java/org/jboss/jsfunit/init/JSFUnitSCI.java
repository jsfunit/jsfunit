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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

/**
 * This SCI class gathers all the JSFUnit tests in the WAR and makes them
 * available to the JSFUnit console.
 *
 * @author Stan Silvert
 * @since 1.2
 */
@HandlesTypes({org.apache.cactus.ServletTestCase.class})
public class JSFUnitSCI implements ServletContainerInitializer
{
    /**
     * Key used by xhtml markup to access all tests from application scope.
     */
    public static final String APP_SCOPE_KEY = "JSFUnitTests";

    private List<String> jsfunitTests;

    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext ctx) throws ServletException
    {
        ctx.setAttribute(APP_SCOPE_KEY, this);
        TreeSet<String> sortedByClassname = new TreeSet<String>();
        Set<Class<?>> jsfunitClasses = new HashSet<Class<?>>();
        for (Class clazz : classes)
        {
            String className = clazz.getName();
            if (className.startsWith("org.apache.cactus")) continue;
            if (className.startsWith("org.jboss.jsfunit.init")) continue;
            sortedByClassname.add(className);
            jsfunitClasses.add(clazz);
        }
        jsfunitTests = new ArrayList<String>(sortedByClassname);
        
        AllJSFUnitTests.setAllTests(jsfunitClasses);
    }

    /**
     * Get an ordered list of all the JSFUnit tests in the WAR.
     *
     * @return The list of tests.
     */
    public List<String> getTests()
    {
        return this.jsfunitTests;
    }
}
