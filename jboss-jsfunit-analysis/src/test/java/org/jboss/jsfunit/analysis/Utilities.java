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

package org.jboss.jsfunit.analysis;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;

import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Dennis Byrne
 */

public class Utilities {

	public static final Set<String> STUBBED_RESOURCEPATH = new HashSet<String>() {{
		add("stubbed resource path");
	}};
	
	private Utilities() {}
	
	public static final String getFacesConfig(String body) {
		
		return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?><!DOCTYPE faces-config PUBLIC "
			+ "\"-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.1//EN\" "
			+ "\"http://java.sun.com/dtd/web-facesconfig_1_1.dtd\">"
			+ "<faces-config>"
			+ body
			+ "</faces-config>";
	}
	
	
	public static String getManagedProperty(String name, String value) {
		
		return "<managed-property>"
	    	+ "<property-name>" + name + "</property-name>"
	    	+ "<value>" + value + "</value>"
	    	+ "</managed-property>";
	}
	
	public static String getManagedBean(String name, Class clazz, String scope, String managedProperties) {
		
		return "<managed-bean>"
			+ "<managed-bean-name>" + name + "</managed-bean-name>"
			+ "<managed-bean-class>" + clazz.getName() + "</managed-bean-class>"
			+ "<managed-bean-scope>" + scope + "</managed-bean-scope>"
			+ managedProperties
			+ "</managed-bean>";
	}
	
	public static String getManagedBean(String name, Class clazz, String scope) {
		return getManagedBean(name, clazz, scope, "");
	}
	
   public static Node createManagedPropertyNode(String facesConfig, String propertyName)
   {
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      String configFilePath = (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0];
      String xml = ParserUtils.getXml(configFilePath, streamProvider);
      DocumentBuilder builder = ParserUtils.getDocumentBuilder();
      Document document = null;
      try
      {
         document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
      }
      catch (Exception e)
      {
         throw new RuntimeException("Could not parse document '" + configFilePath + "'\n" + xml, e);
      }
      String xpath = "//managed-bean/managed-property";
      NodeList managedProperties = ParserUtils.query(document, xpath, configFilePath);
      for (int j = 0; j < managedProperties.getLength(); j++)
      {
         Node property = managedProperties.item(j);
         String propertyNameTemp = ParserUtils.querySingle(property, "./property-name/text()", configFilePath).trim();
         if (propertyNameTemp != null && propertyNameTemp.trim().length() > 0 && propertyNameTemp.equals(propertyName))
         {
            return property;
         }
      }
      return null;
   }

   public static Node createManagedBeanNode(String facesConfig, String managedBeanName)
   {
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      String configFilePath = (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0];
      String xml = ParserUtils.getXml(configFilePath, streamProvider);
      DocumentBuilder builder = ParserUtils.getDocumentBuilder();
      Document document = null;
      try
      {
         document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
      }
      catch (Exception e)
      {
         throw new RuntimeException("Could not parse document '" + configFilePath + "'\n" + xml, e);
      }
      String xpath = "/faces-config/managed-bean";
      String xpathBeanName = "./managed-bean-name/text()";
      NodeList managedBeans = ParserUtils.query(document, xpath, configFilePath);
      for (int i = 0; i < managedBeans.getLength(); i++)
      {
         Node bean = managedBeans.item(i);
         String name = ParserUtils.querySingle(bean, xpathBeanName, configFilePath).trim();
         if (name != null && name.trim().length() > 0 && managedBeanName.equals(name))
         {
            return bean;
         }
      }
      return null;
   }

   public static Node extractFirstLifecycleNode(String facesConfig)
   {
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      String configFilePath = (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0];
      String xml = ParserUtils.getXml(configFilePath, streamProvider);
      DocumentBuilder builder = ParserUtils.getDocumentBuilder();
      Document document = null;
      try
      {
         document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
      }
      catch (Exception e)
      {
         throw new RuntimeException("Could not parse document '" + configFilePath + "'\n" + xml, e);
      }
      String xpath = "/faces-config/lifecycle";
      NodeList lifecycles = ParserUtils.query(document, xpath, configFilePath);
      if (lifecycles.getLength() > 0)
      {
         return lifecycles.item(0);
      }
      return null;
   }

   public static Node extractFirstManagedBeanNode(String facesConfig)
   {
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      String configFilePath = (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0];
      String xml = ParserUtils.getXml(configFilePath, streamProvider);
      DocumentBuilder builder = ParserUtils.getDocumentBuilder();
      Document document = null;
      try
      {
         document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
      }
      catch (Exception e)
      {
         throw new RuntimeException("Could not parse document '" + configFilePath + "'\n" + xml, e);
      }
      String xpath = "//managed-bean";
      NodeList managedBeans = ParserUtils.query(document, xpath, configFilePath);
      Node bean = null;
      if (managedBeans.getLength() > 0)
      {
         bean = managedBeans.item(0);

      }
      return bean;
   }

   public static Node extractFirstRenderKitNode(String facesConfig)
   {
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      String configFilePath = (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0];
      String xml = ParserUtils.getXml(configFilePath, streamProvider);
      DocumentBuilder builder = ParserUtils.getDocumentBuilder();
      Document document = null;
      try
      {
         document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
      }
      catch (Exception e)
      {
         throw new RuntimeException("Could not parse document '" + configFilePath + "'\n" + xml, e);
      }
      String xpath = "/faces-config/render-kit";
      NodeList renderKits = ParserUtils.query(document, xpath, configFilePath);
      if (renderKits.getLength() > 0)
      {
         return renderKits.item(0);
      }
      return null;
   }

   public static Node extractFirstNavigationRuleNode(String facesConfig)
   {
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      String configFilePath = (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0];
      String xml = ParserUtils.getXml(configFilePath, streamProvider);
      DocumentBuilder builder = ParserUtils.getDocumentBuilder();
      Document document = null;
      try
      {
         document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
      }
      catch (Exception e)
      {
         throw new RuntimeException("Could not parse document '" + configFilePath + "'\n" + xml, e);
      }
      String xpath = "/faces-config/navigation-rule";
      NodeList navigationRules = ParserUtils.query(document, xpath, configFilePath);
      if (navigationRules.getLength() > 0)
      {
         return navigationRules.item(0);
      }
      return null;
   }

   public static Node extractFirstNavigationCaseNode(String facesConfig)
   {
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      String configFilePath = (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0];
      String xml = ParserUtils.getXml(configFilePath, streamProvider);
      DocumentBuilder builder = ParserUtils.getDocumentBuilder();
      Document document = null;
      try
      {
         document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
      }
      catch (Exception e)
      {
         throw new RuntimeException("Could not parse document '" + configFilePath + "'\n" + xml, e);
      }
      String xpath = "/faces-config/navigation-rule/navigation-case";
      NodeList navigationCases = ParserUtils.query(document, xpath, configFilePath);
      if (navigationCases.getLength() > 0)
      {
         return navigationCases.item(0);
      }
      return null;
   }

   public static Node extractFirstRendererNode(String facesConfig)
   {
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      String configFilePath = (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0];
      String xml = ParserUtils.getXml(configFilePath, streamProvider);
      DocumentBuilder builder = ParserUtils.getDocumentBuilder();
      Document document = null;
      try
      {
         document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
      }
      catch (Exception e)
      {
         throw new RuntimeException("Could not parse document '" + configFilePath + "'\n" + xml, e);
      }
      String xpath = "/faces-config/render-kit/renderer";
      NodeList renderers = ParserUtils.query(document, xpath, configFilePath);
      if (renderers.getLength() > 0)
      {
         return renderers.item(0);
      }
      return null;
   }

   public static Node extractFirstConverterNode(String facesConfig)
   {
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      String configFilePath = (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0];
      String xml = ParserUtils.getXml(configFilePath, streamProvider);
      DocumentBuilder builder = ParserUtils.getDocumentBuilder();
      Document document = null;
      try
      {
         document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
      }
      catch (Exception e)
      {
         throw new RuntimeException("Could not parse document '" + configFilePath + "'\n" + xml, e);
      }
      String xpath = "/faces-config/converter";
      NodeList converters = ParserUtils.query(document, xpath, configFilePath);
      if (converters.getLength() > 0)
      {
         return converters.item(0);
      }
      return null;
   }

   public static Node extractFirstValidatorNode(String facesConfig)
   {
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      String configFilePath = (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0];
      String xml = ParserUtils.getXml(configFilePath, streamProvider);
      DocumentBuilder builder = ParserUtils.getDocumentBuilder();
      Document document = null;
      try
      {
         document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
      }
      catch (Exception e)
      {
         throw new RuntimeException("Could not parse document '" + configFilePath + "'\n" + xml, e);
      }
      String xpath = "/faces-config/validator";
      NodeList validators = ParserUtils.query(document, xpath, configFilePath);
      if (validators.getLength() > 0)
      {
         return validators.item(0);
      }
      return null;
   }

   public static Node extractFirstComponentNode(String facesConfig)
   {
      StreamProvider streamProvider = new StringStreamProvider(facesConfig);
      String configFilePath = (String) Utilities.STUBBED_RESOURCEPATH.toArray()[0];
      String xml = ParserUtils.getXml(configFilePath, streamProvider);
      DocumentBuilder builder = ParserUtils.getDocumentBuilder();
      Document document = null;
      try
      {
         document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
      }
      catch (Exception e)
      {
         throw new RuntimeException("Could not parse document '" + configFilePath + "'\n" + xml, e);
      }
      String xpath = "/faces-config/component";
      NodeList validators = ParserUtils.query(document, xpath, configFilePath);
      if (validators.getLength() > 0)
      {
         return validators.item(0);
      }
      return null;
   }
}
