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

package org.jboss.jsfunit.example.hellojsf;

import java.io.IOException;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jsfunit.api.BasicAuthentication;
import org.jboss.jsfunit.api.InitialPage;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Version of the FacadeAPITest that uses Arquillian
 * 
 * @author Stan Silvert
 */
@RunWith(Arquillian.class)
public class BasicAuthenticationTest {


    @Deployment
    public static WebArchive createDeployment() {
        return Deployments.createDeployment();

    }

    @Test
    @InitialPage("/secured-page.faces")
    @BasicAuthentication(userName = "admin", password = "password")
    public void testBasicAuth(JSFServerSession server, JSFClientSession client) throws IOException {
        if (server.getCurrentViewID().endsWith("xhtml"))
            Assert.assertEquals("/secured-page.xhtml", server.getCurrentViewID());
        else
            Assert.assertEquals("/secured-page.jsp", server.getCurrentViewID());
        Assert.assertTrue(client.getPageAsText().contains("Welcome to the Basic Secured Application Page"));
    }


    /*
     * @Test
     * 
     * @InitialPage("/secured-page.faces") public void testInvalidLogin() throws IOException { WebClientSpec wcSpec = new
     * WebClientSpec("/secured-page.faces"); wcSpec.getWebClient().setPrintContentOnFailingStatusCode(false);
     * wcSpec.setInitialRequestStrategy(new BasicAuthenticationStrategy("invaliduser", "invalidpassword"));
     * 
     * try { new JSFSession(wcSpec); fail(); } catch (FailingHttpStatusCodeException e) { // Should get 401 Unauthorized
     * Assert.assertEquals(401, e.getStatusCode()); } }
     */

}
