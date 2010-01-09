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

import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Node;

/**
 * A ConverterTestCase.
 * 
 * @author <a href="alejesse@gmail.com">Alexander Jesse</a>
 * @version $Revision: 1.1 $
 */
public class ConverterTestCase extends AbstractInterfaceTestCase
{
   /** the DOM-node from the config file */
   protected Node converterNode = null;

   /** the pat of the config file for error reporting */
   protected String configFilePath = null;

   /**
    * Create a new ConverterTestCase.
    * 
    * @param name TestCase name
    * @param converterNode DOM-node of the converter definition
    * @param configFilePath path to a single config file
    */
   public ConverterTestCase(String name, Node converterNode, String configFilePath)
   {
      super(name, "Converter", null);
      this.converterNode = converterNode;
      this.configFilePath = configFilePath;
   }

   @Override
   protected Class<?> getClassToExtend()
   {
      return javax.faces.convert.Converter.class;
   }

   @Override
   public void runTest()
   {
      this.className = getClassName();
      super.runTest();
      //add additional tests
   }

   /** 
    * Extract the class name of the converter.
    * 
    * @return class name as configured in the config node
    */
   private String getClassName()
   {
      String result = null;

      String xpathConverterClassName = "./converter-class/text()";
      result = ParserUtils.querySingle(this.converterNode, xpathConverterClassName, this.configFilePath);

      return result;
   }
}
