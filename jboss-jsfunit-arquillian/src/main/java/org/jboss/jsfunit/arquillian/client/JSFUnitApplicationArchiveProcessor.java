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

package org.jboss.jsfunit.arquillian.client;

import org.jboss.arquillian.spi.TestClass;
import org.jboss.arquillian.spi.client.deployment.ApplicationArchiveProcessor;
import org.jboss.jsfunit.cdi.JSFUnitCDIProducer;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * This adds CDI classes directly to WEB-INF/classes.  They must be there for
 * tests to use them.
 *
 * @author ssilvert
 * @author <a href="mailto:aslak@conduct.no">Aslak Knutsen</a>
 */
// TODO: this can be removed when ARQ-405 is fixed. We don't really need to support both CDI and Arq
public class JSFUnitApplicationArchiveProcessor implements ApplicationArchiveProcessor
{
   /**
    * If this is a CDI WebArchive, add the {@link JSFUnitCDIProducer} to the users deployment so it will be picked up by CDI 
    */
   public void process(Archive<?> archive, TestClass tc)
   {
      if(WebArchive.class.isInstance(archive))
      {
         WebArchive webArchive = WebArchive.class.cast(archive);
         if(webArchive.contains(ArchivePaths.create("WEB-INF/beans.xml")))
         {
            webArchive.addClass(JSFUnitCDIProducer.class);            
         }
      }
   }

}
