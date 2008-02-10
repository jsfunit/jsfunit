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

package org.jboss.jsfunit.stub.config;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.convert.Converter;
import javax.faces.render.Renderer;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.jsfunit.stub.base.AbstractJsfTestCase;
import org.jboss.jsfunit.stub.config.ConfigParser;

/**
 * <p>Unit tests for the configuration parser utility class.</p>
 */
public class ConfigParserTestCase extends AbstractJsfTestCase {


    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public ConfigParserTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();
        parser = new ConfigParser();

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(ConfigParserTestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        parser = null;
        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    // ConfigParser instance under test
    ConfigParser parser = null;


    // ------------------------------------------------- Individual Test Methods


    // Test access to the platform configuration resources
    public void testPlatform() throws Exception {

        // Make sure we can acquire a good set of URLs
        URL[] urls = parser.getPlatformURLs();
        assertNotNull(urls);
        assertEquals(1, urls.length);
        
        // commented this out. apparently this was pulling resources from a sun jar in the previous code base
        //assertNotNull(urls[0]);
        // Now can we actually parse them?
        //parser.parse(urls);

    }


    // Test a pristine instance
    public void testPristine() {

        assertNotNull(parser);

    }


    // Test loading a simple configuration resource
    public void testSimple() throws Exception {

        URL url = this.getClass().getResource("/org/faces-config-1.xml");
        assertNotNull(url);
        parser.parse(url);
        Iterator items = null;
        List list = new ArrayList();

        items = application.getComponentTypes();
        list.clear();
        while (items.hasNext()) {
            list.add(items.next());
        }
        assertTrue(list.contains("component-type-1"));
        assertTrue(list.contains("component-type-2"));

        items = application.getConverterIds();
        list.clear();
        while (items.hasNext()) {
            list.add(items.next());
        }
        assertTrue(list.contains("converter-id-1"));
        assertTrue(list.contains("converter-id-2"));

        Converter converter = application.createConverter(Integer.class);
        assertNotNull(converter);
        assertTrue(converter instanceof MyConverter);

        items = application.getValidatorIds();
        list.clear();
        while (items.hasNext()) {
            list.add(items.next());
        }
        assertTrue(list.contains("validator-id-1"));
        assertTrue(list.contains("validator-id-2"));

        Renderer renderer = renderKit.getRenderer("component-family-1", "renderer-type-1");
        assertNotNull(renderer);
        assertTrue(renderer instanceof MyRenderer);
        
        renderer = renderKit.getRenderer("component-family-2", "renderer-type-2");
        assertNotNull(renderer);
        assertTrue(renderer instanceof MyRenderer);
        
    }


    // --------------------------------------------------------- Private Methods


}
