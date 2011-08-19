/*
 * JBoss, Home of Professional Open Source.
 * Copyright 20010, Red Hat Middleware LLC, and individual contributors
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

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jsfunit.api.FormAuthentication;
import org.jboss.jsfunit.api.InitialPage;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Version of the FacadeAPITest that uses Arquillian
 * 
 * @author Stan Silvert
 */
@RunWith(Arquillian.class)
public class FormAuthenticationTest {

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive webArchive = Deployments.createDeployment();
        webArchive.delete(ArchivePaths.create("WEB-INF/web.xml"));
        webArchive.setWebXML(new File("src/main/webapp/WEB-INF/formauth-web.xml"))
                .addAsWebResource(new File("src/main/webapp", "form-secured-page.xhtml"))
                .addAsWebResource(new File("src/main/webapp", "login.xhtml"))
                .addAsWebResource(new File("src/main/webapp", "error.xhtml"));
        return webArchive;
    }

    @Test
    @InitialPage("/form-secured-page.faces")
    @FormAuthentication(userName = "admin", password = "password", submitComponent = "login_button")
    public void testFormAuthStandard(JSFServerSession server, JSFClientSession client) throws IOException {
        Assert.assertEquals("/form-secured-page.xhtml", server.getCurrentViewID());
        Assert.assertTrue(client.getPageAsText().contains("Welcome to the Form Secured Application Page"));
    }

    @Test
    @InitialPage("/form-secured-page.faces")
    @FormAuthentication(userName = "invaliduser", password = "invalidpassword", submitComponent = "login_button")
    public void testInvalidLogin(JSFServerSession server, JSFClientSession client) throws IOException {
        Assert.assertTrue(client.getPageAsText().contains("Error logging in"));
    }

    @Test
    @InitialPage("/form-secured-page.faces")
    @FormAuthentication(userName = "admin", password = "password", submitComponent = "login_button", userNameComponent = "j_username", passwordComponent = "j_password")
    public void testFormAuthNonStandard(JSFServerSession server, JSFClientSession client) throws IOException {
        Assert.assertEquals("/form-secured-page.xhtml", server.getCurrentViewID());
        Assert.assertTrue(client.getPageAsText().contains("Welcome to the Form Secured Application Page"));
    }

}
