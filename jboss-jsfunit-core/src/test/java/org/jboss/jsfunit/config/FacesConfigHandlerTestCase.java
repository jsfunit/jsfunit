package org.jboss.jsfunit.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import junit.framework.TestCase;

public class FacesConfigHandlerTestCase extends TestCase {
	
	private final Map<String, Class[]> EMPTY_CLASSES = new HashMap<String, Class[]>();
	private final Map<String, String[]> EMPTY_VALUES = new HashMap<String, String[]>();
	private final String CLASS_NAME = "com.ebay.Foo";
	
	public void testClasses() throws Exception{
		
		Map<String, Class[]> classes = new HashMap<String, Class[]>(){{
			put("foo", new Class[] {});
		}};
		
        FacesConfigHandler handler = new FacesConfigHandler(classes.keySet(), EMPTY_VALUES.keySet());
        InputStream is = new ByteArrayInputStream(("<root><foo>" + CLASS_NAME + "</foo><bar></bar></root>").getBytes());
        getParser().parse(is, handler);
        
        assertEquals(1, handler.getClasses().keySet().size());
        assertEquals(CLASS_NAME, handler.getClasses().get("foo").get(0));
        assertEquals(0, handler.getValues().keySet().size());
	}

	public void testValues() throws Exception{
		
		Map<String, String[]> values = new HashMap<String, String[]>(){{
			put("bar", new String[] {});
		}};	
		FacesConfigHandler handler = new FacesConfigHandler(EMPTY_CLASSES.keySet(), values.keySet());
		InputStream is = new ByteArrayInputStream(("<root><foo></foo><bar>" + CLASS_NAME + "</bar></root>").getBytes());
		getParser().parse(is, handler);

        assertEquals(0, handler.getClasses().keySet().size());
        assertEquals(1, handler.getValues().keySet().size());
        assertEquals(CLASS_NAME, handler.getValues().get("bar").get(0));
	}
	
	public void testClassesAndValues() throws Exception{

		Map<String, Class[]> classes = new HashMap<String, Class[]>(){{
			put("foo", new Class[] {});
		}};
		Map<String, String[]> values = new HashMap<String, String[]>(){{
			put("bar", new String[] {});
		}};			
		FacesConfigHandler handler = new FacesConfigHandler(classes.keySet(), values.keySet());
		InputStream is = new ByteArrayInputStream(("<root><foo>" + CLASS_NAME + "</foo><bar>" + CLASS_NAME + "</bar></root>").getBytes());
		getParser().parse(is, handler);		

        assertEquals(1, handler.getClasses().keySet().size());
        assertEquals(CLASS_NAME, handler.getClasses().get("foo").get(0));
        assertEquals(1, handler.getValues().keySet().size());
        assertEquals(CLASS_NAME, handler.getValues().get("bar").get(0));
	}
	
	public void testNeither() throws Exception{
		
		FacesConfigHandler handler = new FacesConfigHandler(EMPTY_CLASSES.keySet(), EMPTY_VALUES.keySet());
		InputStream is = new ByteArrayInputStream(("<root><foo>" + CLASS_NAME + "</foo><bar>" + CLASS_NAME + "</bar></root>").getBytes());
		getParser().parse(is, handler);
		
		assertEquals(0, handler.getClasses().keySet().size());
		assertEquals(0, handler.getValues().keySet().size());
	}
	
	private SAXParser getParser() throws ParserConfigurationException, SAXException {
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(false);
		return factory.newSAXParser();
		
	}
	
	public void testMultipleResources() throws Exception{
		
		Map<String, Class[]> classes = new HashMap<String, Class[]>(){{
			put("foo", new Class[] {});
		}};
		String xml = "<root><foo>" + CLASS_NAME + "</foo><bar></bar></root>";
		
        FacesConfigHandler handler = new FacesConfigHandler(classes.keySet(), EMPTY_VALUES.keySet());
        getParser().parse(new ByteArrayInputStream(xml.getBytes()), handler);
        getParser().parse(new ByteArrayInputStream(xml.getBytes()), handler);
        
        assertEquals(1, handler.getClasses().keySet().size());
        assertEquals(CLASS_NAME, handler.getClasses().get("foo").get(0));
        assertEquals(CLASS_NAME, handler.getClasses().get("foo").get(1));
        assertEquals(0, handler.getValues().keySet().size());
		
	}

}