/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.jsfunit.example.hellojsf;

import java.io.IOException;

import javax.faces.component.UIComponent;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * FasadeAPICDITest
 * 
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
@Ignore
@RunWith(Arquillian.class)
public class FasadeAPICDITest extends FacadeAPITest {
    // @Inject
    private JSFClientSession client;

    // @Inject
    private JSFServerSession server;

    @Deployment
    public static WebArchive createDeployment() {
        return Deployments.createDeployment();
    }


    @Test
    public void testSetParamAndSubmit() throws IOException {
        client.setValue("input_foo_text", "Stan");
        client.click("submit_button");

        UIComponent greeting = server.findComponent("greeting");
        Assert.assertTrue(greeting.isRendered());

        // test CDI bean
        Assert.assertTrue(client.getPageAsText().contains("Hello Stan"));
        Assert.assertEquals("Hello", server.getManagedBeanValue("#{mybean.hello}"));
    }

    @Test
    public void testServerSideComponentValue() throws IOException {

        testSetParamAndSubmit(); // put "Stan" into the input field

        // test the greeting component
        Assert.assertEquals("Hello Stan", server.getComponentValue("greeting"));
    }

    /**
     * This demonstrates how to test managed beans.
     */
    @Test
    public void testManagedBeanValue() throws IOException {

        testSetParamAndSubmit(); // put "Stan" into the input field

        Assert.assertEquals("Stan", server.getManagedBeanValue("#{foo.text}"));
    }

}
