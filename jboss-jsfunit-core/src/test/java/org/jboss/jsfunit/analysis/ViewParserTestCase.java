package org.jboss.jsfunit.analysis;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import junit.framework.TestCase;

import org.jboss.jsfunit.analysis.util.ParserUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ViewParserTestCase extends TestCase {

	private ViewParser viewParser;
	private static final String EL = "#{bean.foo}";
	
	@Override
	protected void setUp() throws Exception {
		
		viewParser = new ViewParser();
		
	}

	public void testAction() throws Exception{
		
		viewParser.parse(getDocument("<root><actionListener action='" + EL + "'/></root>"));
		List actionListeners = viewParser.getActions();
		assertEquals(1, actionListeners.size());
		assertEquals(EL, actionListeners.get(0));
		
	}
	
	public void testActionListenerTag() throws Exception{
		
		viewParser.parse(getDocument("<root><actionListener binding='" + EL + "'/></root>"));
		List actionListeners = viewParser.getActionListeners();
		assertEquals(1, actionListeners.size());
		assertEquals(EL, actionListeners.get(0));
		
	}
	
	public void testActionListenerAttribute() throws Exception{
		
		viewParser.parse(getDocument("<root actionListener='" + EL + "' />"));
		List actionListeners = viewParser.getActionListeners();
		assertEquals(1, actionListeners.size());
		assertEquals(EL, actionListeners.get(0));
		
	}
	
	public void testNothing() throws Exception {
		
		viewParser.parse(getDocument("<root a='foo'/>"));
		assertEquals(0, viewParser.getActionListeners().size()); 
		assertEquals(0, viewParser.getActions().size());
	}

	private Document getDocument(String xml) throws SAXException, IOException {
		DocumentBuilder documentBuilder = ParserUtils.getDocumentBuilder();
		return documentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
	}

}