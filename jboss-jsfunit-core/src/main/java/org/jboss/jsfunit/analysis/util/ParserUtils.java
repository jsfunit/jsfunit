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

package org.jboss.jsfunit.analysis.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.jboss.jsfunit.analysis.JSFUnitEntityResolver;
import org.jboss.jsfunit.analysis.StreamProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Dennis Byrne
 */

public class ParserUtils {

	private static XPathFactory xPathFactory = XPathFactory.newInstance();
	private static DocumentBuilderFactory factory ;
	
	static {
		
		factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		factory.setIgnoringElementContentWhitespace(true);
		
	}
	
	public static DocumentBuilder getDocumentBuilder() {

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new JSFUnitEntityResolver());
			return builder;
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Could not create a " + DocumentBuilder.class.getName());
		}
	}
	
	public static String getXml(String resourcePath, StreamProvider streamProvider) {
		InputStream stream = streamProvider.getInputStream(resourcePath);
		
		if(stream == null)
			throw new RuntimeException("Could not locate file '" + resourcePath + "'" );
		
		return new ResourceUtils().getAsString(stream, resourcePath);
	}
	
	public static Document getDocument(String xml) throws SAXException, IOException {
		DocumentBuilder documentBuilder = getDocumentBuilder();
		return documentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
	}
	
	public static NodeList query(Node node, String xpathQuery, final String filePath) {
		
		if(node == null)
			throw new NullPointerException("document was null " + filePath);
		
		XPath xpath = xPathFactory.newXPath();
		
		try {
			
			return (NodeList) xpath.evaluate(xpathQuery, node, XPathConstants.NODESET);
			
		}catch(Exception e) {
			throw new RuntimeException("Could not run XPath query '" + xpathQuery + "' on document " + filePath);
		}
		
	}
	
	public static String querySingle(Node node, String xpathQuery, final String filePath) {
		
		NodeList list = query(node, xpathQuery, filePath);
		int count = list.getLength();
		
		if(count > 1)
			throw new RuntimeException("query " + xpathQuery + " returned " 
					+ list.getLength() + " results. Should have been one.");
		
		return count == 1 ? list.item(0).getTextContent() : null;
	}
}
