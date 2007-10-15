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
	private static final String ACTION_LISTENER = "#{bean.foo}";
	
	@Override
	protected void setUp() throws Exception {
		
		viewParser = new ViewParser();
		
	}

	public void testActionListenerTag() throws Exception{
		
		viewParser.parse(getDocument("<root><actionListener binding='" + ACTION_LISTENER + "'/></root>"));
		List actionListeners = viewParser.getActionListeners();
		assertEquals(1, actionListeners.size());
		assertEquals(ACTION_LISTENER, actionListeners.get(0));
		
	}
	
	public void testActionListenerAttribute() throws Exception{
		
		viewParser.parse(getDocument("<root actionListener='" + ACTION_LISTENER + "' />"));
		List actionListeners = viewParser.getActionListeners();
		assertEquals(1, actionListeners.size());
		assertEquals(ACTION_LISTENER, actionListeners.get(0));
		
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