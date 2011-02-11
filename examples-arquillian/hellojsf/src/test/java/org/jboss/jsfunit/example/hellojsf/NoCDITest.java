/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test without using JSFUnit annotations.
 *
 * To recreate JSFUNIT-269, this test needs to be run alone with -Dtest=NoCDITest
 * 
 * @author Stan Silvert
 */
@RunWith(Arquillian.class)
public class NoCDITest
{

   @Deployment
   public static WebArchive createDeployment() {
      // The following line will work around JSFUNIT-269
      //Class clazz = org.jboss.jsfunit.cdi.InitialPage.class;
      return FacadeAPITest.createDeployment();
   }

   @Test
   public void testGetCurrentViewId() throws IOException
   {
      JSFSession jsfSession = new JSFSession("/index.faces");
      JSFServerSession server = jsfSession.getJSFServerSession();

      // Test navigation to initial viewID
      Assert.assertEquals("/index.xhtml", server.getCurrentViewID());
      Assert.assertEquals(server.getCurrentViewID(), server.getFacesContext().getViewRoot().getViewId());
   }

}
