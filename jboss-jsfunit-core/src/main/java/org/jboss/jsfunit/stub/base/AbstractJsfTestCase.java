/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.jsfunit.stub.base;

import java.net.URL;
import java.net.URLClassLoader;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIViewRoot;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKitFactory;

import junit.framework.TestCase;

import org.jboss.jsfunit.stub.ApplicationStub;
import org.jboss.jsfunit.stub.ExternalContextStub;
import org.jboss.jsfunit.stub.MockFacesContext;
import org.jboss.jsfunit.stub.MockFacesContextFactory;
import org.jboss.jsfunit.stub.MockHttpServletRequest;
import org.jboss.jsfunit.stub.MockHttpServletResponse;
import org.jboss.jsfunit.stub.MockHttpSession;
import org.jboss.jsfunit.stub.MockLifecycle;
import org.jboss.jsfunit.stub.MockLifecycleFactory;
import org.jboss.jsfunit.stub.MockRenderKit;
import org.jboss.jsfunit.stub.MockServletConfig;
import org.jboss.jsfunit.stub.MockServletContext;

/**
 * <p>Abstract JUnit test case base class, which sets up the JavaServer Faces
 * mock object environment for a particular simulated request.  The following
 * protected variables are initialized in the <code>setUp()</code> method, and
 * cleaned up in the <code>tearDown()</code> method:</p>
 * <ul>
 * <li><code>application</code> (<code>ApplicationStub</code>)</li>
 * <li><code>config</code> (<code>MockServletConfig</code>)</li>
 * <li><code>externalContext</code> (<code>ExternalContextStub</code>)</li>
 * <li><code>facesContext</code> (<code>MockFacesContext</code>)</li>
 * <li><code>lifecycle</code> (<code>MockLifecycle</code>)</li>
 * <li><code>request</code> (<code>MockHttpServletRequest</code></li>
 * <li><code>response</code> (<code>MockHttpServletResponse</code>)</li>
 * <li><code>servletContext</code> (<code>MockServletContext</code>)</li>
 * <li><code>session</code> (<code>MockHttpSession</code>)</li>
 * </ul>
 *
 * <p>In addition, appropriate factory classes will have been registered with
 * <code>javax.faces.FactoryFinder</code> for <code>Application</code> and
 * <code>RenderKit</code> instances.  The created <code>FacesContext</code>
 * instance will also have been registered in the apppriate thread local
 * variable, to simulate what a servlet container would do.</p>
 *
 * <p><strong>WARNING</strong> - If you choose to subclass this class, be sure
 * your <code>setUp()</code> and <code>tearDown()</code> methods call
 * <code>super.setUp()</code> and <code>super.tearDown()</code> respectively,
 * and that you implement your own <code>suite()</code> method that exposes
 * the test methods for your test case.</p>
 */

public abstract class AbstractJsfTestCase extends TestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new instance of this test case.</p>
     *
     * @param name Name of this test case
     */
    public AbstractJsfTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    /**
     * <p>Set up instance variables required by this test case.</p>
     */
    protected void setUp() throws Exception {

        // Set up a new thread context class loader
        threadContextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[0],
                this.getClass().getClassLoader()));

        // Set up Servlet API Objects
        servletContext = new MockServletContext();
        config = new MockServletConfig(servletContext);
        session = new MockHttpSession();
        session.setServletContext(servletContext);
        request = new MockHttpServletRequest(session);
        request.setServletContext(servletContext);
        response = new MockHttpServletResponse();

        // Set up JSF API Objects
        FactoryFinder.releaseFactories();
        FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
        "org.jboss.jsfunit.stub.ApplicationFactoryStub");
        FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
        "org.jboss.jsfunit.stub.MockFacesContextFactory");
        FactoryFinder.setFactory(FactoryFinder.LIFECYCLE_FACTORY,
        "org.jboss.jsfunit.stub.MockLifecycleFactory");
        FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
        "org.jboss.jsfunit.stub.MockRenderKitFactory");

        externalContext =
            new ExternalContextStub(servletContext, request, response);
        lifecycleFactory = (MockLifecycleFactory)
        FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        lifecycle = (MockLifecycle)
        lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        facesContextFactory = (MockFacesContextFactory)
        FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        facesContext = (MockFacesContext)
        facesContextFactory.getFacesContext(servletContext,
                request,
                response,
                lifecycle);
        externalContext = (ExternalContextStub) facesContext.getExternalContext();
        UIViewRoot root = new UIViewRoot();
        root.setViewId("/viewId");
        root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
        facesContext.setViewRoot(root);
        ApplicationFactory applicationFactory = (ApplicationFactory)
          FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        application = (ApplicationStub) applicationFactory.getApplication();
        facesContext.setApplication(application);
        RenderKitFactory renderKitFactory = (RenderKitFactory)
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        renderKit = new MockRenderKit();
        renderKitFactory.addRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT, renderKit);

    }


    /**
     * <p>Tear down instance variables required by this test case.</p>
     */
    protected void tearDown() throws Exception {

        application = null;
        config = null;
        externalContext = null;
        facesContext.release();
        facesContext = null;
        lifecycle = null;
        lifecycleFactory = null;
        renderKit = null;
        request = null;
        response = null;
        servletContext = null;
        session = null;
        FactoryFinder.releaseFactories();

        Thread.currentThread().setContextClassLoader(threadContextClassLoader);
        threadContextClassLoader = null;

    }


    // ------------------------------------------------------ Instance Variables


    // Mock object instances for our tests
    protected ApplicationStub         application = null;
    protected MockServletConfig       config = null;
    protected ExternalContextStub     externalContext = null;
    protected MockFacesContext        facesContext = null;
    protected MockFacesContextFactory facesContextFactory = null;
    protected MockLifecycle           lifecycle = null;
    protected MockLifecycleFactory    lifecycleFactory = null;
    protected MockRenderKit           renderKit = null;
    protected MockHttpServletRequest  request = null;
    protected MockHttpServletResponse response = null;
    protected MockServletContext      servletContext = null;
    protected MockHttpSession         session = null;

    // Thread context class loader saved and restored after each test
    private ClassLoader threadContextClassLoader = null;

}
