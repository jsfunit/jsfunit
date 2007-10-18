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

package org.jboss.jsfunit.facade;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class contains various methods for searching and manipulating a DOM.
 *
 * @author ssilvert
 */
public class DOMUtil
{

   // don't allow an instance of this class
   private DOMUtil()
   {
   }
   
   /**
    * Search the DOM for the first element that has an id attribute equal
    * to the given value.
    *
    * @param idValue The value to match.
    * @param doc The DOM.
    *
    * @return The matching element or <code>null</code> if not found.
    */
   public static Element findElementWithID(String idValue, Document doc)
   {
      Element docElement = doc.getDocumentElement();
      return findElementWithAttribValue("id", idValue, docElement);
   }
   
   /**
    * Search the Element and its children for the first element that has the 
    * attribute name/value pair.
    *
    * @param attrName The attribute name to match.
    * @param value The attribute value to match.
    * @param element The starting element for the search.
    *
    * @return The matching element or <code>null</code> if not found.
    */
   public static Element findElementWithAttribValue(String attrName, String value, Element element)
   {
      if (value.equals(element.getAttribute(attrName))) return element;
      
      NodeList nodes = element.getChildNodes();
      for (int i=0; i < nodes.getLength(); i++)
      {
         Node node = nodes.item(i);
         if (node.getNodeType() != Node.ELEMENT_NODE) continue;
         Element childElement = (Element)node;
         Element foundElement = findElementWithAttribValue(attrName, value, childElement);
         if (foundElement != null) return foundElement;
      }
      
      return null;
   }
   
   /**
    * Convert a Document to a String and then strip its XML declaration and
    * replace it with an HTML declaration.
    *
    * @param doc The DOM
    *
    * @return The converted String.
    */
   public static String docToHTMLString(Document doc) throws TransformerException
   {
      return xmlStringTohtmlString(docToString(doc));
   }
   
   /**
    * Convert a Document to a String.
    *
    * @param doc The DOM
    *
    * @return The converted String.
    */
   public static String docToString(Document doc) throws TransformerException
   {
      DOMSource source = new DOMSource(doc);
      StringWriter writer = new StringWriter();
      StreamResult result = new StreamResult(writer);
      TransformerFactory transFactory = TransformerFactory.newInstance();
      Transformer transformer = transFactory.newTransformer();
      transformer.transform(source, result);
      return writer.toString();
   }
   
   /**
    * Converts a DOM level 1 document into a DOM level 2 document.
    *
    * @param doc The DOM level 1 document.
    *
    * @return The DOM level 2 document.
    */
   public static Document convertToDomLevel2(Document doc) throws TransformerException
   {
      DOMSource source = new DOMSource(doc);
      DOMResult result = new DOMResult();
      TransformerFactory transFactory = TransformerFactory.newInstance();
      Transformer transformer = transFactory.newTransformer();
      transformer.transform(source, result);
      return (Document)result.getNode();
   }
   
   /**
    * Converts an XML String to an HTML String by stripping off the XML
    * declaration and replacing it with an HTML declaration.
    *
    * @param xmlString The XML String
    *
    * @return The HTML String
    */
   public static String xmlStringTohtmlString(String xmlString)
   {
      int htmlStart = xmlString.indexOf("?>") + 2;
      String html = xmlString.substring(htmlStart);
      return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" >"
             + html;
   }
   
   /**
    * Converts and XML String to a DOM.
    *
    * @param xmlString The XML String.
    *
    * @return The DOM.
    */
   public static Document stringToDoc(String xmlString) 
         throws ParserConfigurationException, SAXException, IOException
   {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      return docBuilder.parse(new InputSource(new StringReader(xmlString)));
   }
}
