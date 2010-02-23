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

package org.jboss.jsfunit.staticanalysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.jboss.jsfunit.analysis.ConfigFilesTestSuite;

/**
 * Run the JSFUnit config files static analysis suite against this WAR.
 *
 * @author ssilvert
 */
public class JSFUnitStaticAnalysisTest extends TestSuite
{
   private static final String WEBAPP_PATH;

   static
   {
       Properties props = new Properties();

       try
       {
            props.load(JSFUnitStaticAnalysisTest.class.getResourceAsStream("/test.properties"));
       }
       catch (IOException e)
       {
           throw new IllegalStateException(e);
       }

       WEBAPP_PATH = props.getProperty("webapp.path");
   }

   private static final String CONFIG_PATH = WEBAPP_PATH + "/WEB-INF/faces-config.xml";

   public static Test suite()
   {
      TestSuite suite = new TestSuite();
      suite.setName("MyApplication all tests");

      List<String> configFiles = new ArrayList<String>();
      configFiles.add(CONFIG_PATH);

      suite.addTest(new ConfigFilesTestSuite("JSF config files").getSuite(configFiles));

      return suite;
   }
}