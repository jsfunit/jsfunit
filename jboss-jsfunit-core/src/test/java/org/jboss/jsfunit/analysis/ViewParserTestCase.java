package org.jboss.jsfunit.analysis;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;

import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ViewParserTestCase extends TestCase {

	private ViewParser viewParser;
	
	@Override
	protected void setUp() throws Exception {
		
		viewParser = new ViewParser();
		
	}

	public void testActionListenerAttribute() throws Exception{
		
		viewParser.parse(getDocument("<root actionListener='#{bean.foo}' />"));
		assertEquals(1, viewParser.getActionListeners().size());
		
	}
	
	public void testNothing() throws Exception {
		
		viewParser.parse(getDocument("<root a='foo'/>"));
		assertEquals(0, viewParser.getActionListeners().size()); 

	}

	private Document getDocument(String xml) throws SAXException, IOException {
		DocumentBuilder documentBuilder = ParserUtils.getDocumentBuilder();
		return documentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
	}

}