package org.jboss.jsfunit.test.richfaces;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.cactus.ServletTestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;
import org.jboss.jsfunit.richfaces.RichFacesClient;
import org.w3c.dom.Element;

import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class InplaceInputTest extends ServletTestCase {
    // -- Logger
    protected static Log log = LogFactory.getLog(InplaceInputTest.class);

	public static Test suite()
	{
		return new TestSuite( InplaceInputTest.class );
	}

	// Control IDs
	private final static String _SIMPLE_DEFAULT_NAME_ID = "simpleDefaultName";
	private final static String _SIMPLE_DEFAULT_EMAIL_ID = "simpleDefaultEmail";

	private final static String _SIMPLE_WITH_CONTROLS_NAME_ID = "simpleWithControlsName";
	private final static String _SIMPLE_WITH_CONTROLS_EMAIL_ID = "simpleWithControlsEmail";
	
	private final static String _CUSTOM_CONTROLS_ID = "inplaceInput";
	private final static String _CUSTOM_CONTROLS_SAVE_ID = "customControlsSaveButton";
	private final static String _CUSTOM_CONTROLS_CANCEL_ID = "customControlsCancelButton";
		
	private final static String _TEST_NAME_VALUE = "John Smith";
	
	// JSFUnit support objects
	private JSFSession jsfSession;
	private JSFClientSession client;
	private RichFacesClient ajaxClient;
	private JSFServerSession server;
	
	public void setUp() throws IOException
	{
		this.jsfSession = new JSFSession("/richfaces/inplaceInput.jsf");
		this.client = jsfSession.getJSFClientSession();
		this.ajaxClient = new RichFacesClient(client);
		this.server = jsfSession.getJSFServerSession();
	}
	
	/**
	 * This method mirrors the implementation of RichFacesClient.setInplaceInput()
	 * but adds a great many assertions to verify both htmlunit and richfaces
	 * are behaving as they should. ( used by testInplaceInput_Raw() )
	 * 
	 * @param controlId 
	 * @param value
	 * @param customSaveId
	 * 
	 * @throws IOException
	 */
	private void setInplaceInput_CImpl( String controlId, String value, String customSaveId ) throws IOException
	{
		// Find outside control
		Element e = client.getElement(controlId);
		assertNotNull("Control not found: "+controlId,e);
		assertTrue("Control not expected type (HtmlSpan): "+e.getClass(),e instanceof HtmlSpan);
		HtmlSpan span = (HtmlSpan)e;
		
		// Find text control
		e = client.getElement(controlId+"tempValue");
		assertNotNull("Control not found: "+controlId+"tempValue",e);
		assertTrue("Control not expected type (HtmlTextInput): "+e.getClass(),e instanceof HtmlTextInput);
		HtmlTextInput nameInput = (HtmlTextInput)e;
		// Make sure input control is hidden
		String hiddenStyle = nameInput.getStyleAttribute();
		assertTrue("Input control not hidden as expected: ["+hiddenStyle+"]",
				hiddenStyle.contains("display:none") || hiddenStyle.contains("display: none"));
		
		// Activate control - click on outside table control
		span.click();
		
		// Make sure input control is now visible
		String visibleStyle = nameInput.getStyleAttribute();
		assertFalse("Input control not visible as expected: ["+visibleStyle+"]",
				visibleStyle.contains("display:none") || visibleStyle.contains("display: none"));
	
		// Type value into input control
		nameInput.type(value);
		
		// Type #3 - CUSTOM save button
		if( customSaveId != null ) {
			// Find and Click save button
			e = client.getElement(customSaveId);
			assertNotNull("Control not found: "+customSaveId,e);
			assertTrue("Control not expected type (HtmlButton): "+e.getClass(),e instanceof HtmlButton);
			HtmlButton saveButton = (HtmlButton)e;
			saveButton.click();			
		} else {
			// Check to see if buttons are visible
			e = client.getElement(controlId+"bar");
			assertNotNull("Control not found: "+controlId+"bar",e);
			assertTrue("Control not expected type (HtmlDivision): "+e.getClass(),e instanceof HtmlDivision);
			HtmlDivision buttons = (HtmlDivision)e;
			String buttonStyle = buttons.getStyleAttribute();
			// Type #1 - NO BUTTONS
			if( buttonStyle.contains("display:none") || buttonStyle.contains("display: none") ) 
			{
				// Remove focus from name input control
				nameInput.blur();

			// Type #2 - built-in buttons
			} else {
				// Find and "mousedown" the standard "ok" button
				e = client.getElement(controlId+"ok");
				assertNotNull("Control not found: "+controlId+"ok",e);
				assertTrue("Control not expected type (HtmlImageInput): "+e.getClass(),e instanceof HtmlImageInput);
				HtmlImageInput okButton = (HtmlImageInput)e;
				okButton.fireEvent("mousedown");						
			}
		}
		
		// Make sure name input is no longer visible
		hiddenStyle = nameInput.getStyleAttribute();
		assertTrue("Input control not hidden as expected: ["+hiddenStyle+"]",
				hiddenStyle.contains("display:none") || hiddenStyle.contains("display: none"));
		String inputValue = nameInput.getValueAttribute();
		assertEquals("Input control value is not set as expected",_TEST_NAME_VALUE,inputValue);
		
		// Find the hidden input control
		e = client.getElement(controlId+"value");
		assertNotNull("Control not found: "+controlId+"value",e);
		assertTrue("Control not expected type (HtmlHiddenInput): "+e.getClass(),e instanceof HtmlHiddenInput);
		HtmlHiddenInput hiddenInput = (HtmlHiddenInput)e;

		// Make sure the hidden value is set
		String hiddenValue = hiddenInput.getValueAttribute();
		assertEquals("Hidden control value is not set as expected",value,hiddenValue);
		
	}
	
	/**
	 * This helper function will make sure the hidden value of the
	 * inplace input control is set properly.
	 * 
	 * @param componentID 
	 * @param value
	 */
	private void assertInplaceInputValue( String componentID, String value )
	{
		// Find the hidden input control
		Element e = client.getElement(componentID+"value");
		assertNotNull("Control not found: "+componentID+"value",e);
		assertTrue("Control not expected type (HtmlHiddenInput): "+e.getClass(),e instanceof HtmlHiddenInput);
		HtmlHiddenInput hiddenInput = (HtmlHiddenInput)e;

		// Make sure the hidden value is set
		String hiddenValue = hiddenInput.getValueAttribute();
		assertEquals("Hidden control value is not set as expected",value,hiddenValue);		
	}

	/**
	 * This test will make sure the inner workings of the inplace input
	 * control behave as we expect. This is a easy way to determine
	 * if something has changed/broken in htmlunit or richfaces and where
	 * the change might be located.
	 * 
	 * @throws IOException 
	 * 
	 */
	public void testInplaceInput_Raw() throws IOException 
	{
		// Simple Default 
		setInplaceInput_CImpl( _SIMPLE_DEFAULT_NAME_ID, _TEST_NAME_VALUE, null );
		// Simple Default with Controls
		setInplaceInput_CImpl( _SIMPLE_WITH_CONTROLS_NAME_ID, _TEST_NAME_VALUE, null );		
		// Custom Controls
		setInplaceInput_CImpl( _CUSTOM_CONTROLS_ID, _TEST_NAME_VALUE, _CUSTOM_CONTROLS_SAVE_ID );
	}
	
	/**
	 * This test will set the value of an richface inplace input control.
	 * 
	 * This version is for the "Simple Default" version of
	 * the control in the richfaces demo.
	 * 
	 * @throws IOException 
	 * 
	 */
	public void testInplaceInput_SimpleDefault() throws IOException
	{
		ajaxClient.setInplaceInput(_SIMPLE_DEFAULT_NAME_ID, "");
		assertInplaceInputValue(_SIMPLE_DEFAULT_NAME_ID, "");
		ajaxClient.setInplaceInput(_SIMPLE_DEFAULT_NAME_ID, _TEST_NAME_VALUE);
		assertInplaceInputValue(_SIMPLE_DEFAULT_NAME_ID, _TEST_NAME_VALUE);
	}

	/**
	 * This test will set the value of an richface inplace input control.
	 * 
	 * This version is for the "Simple With Controls" version of
	 * the control in the richfaces demo and tests the ok
	 * action only.
	 * 
	 * @throws IOException 
	 * 
	 */
	public void testInplaceInput_SimpleWithControls() throws IOException
	{
		ajaxClient.setInplaceInput(_SIMPLE_WITH_CONTROLS_NAME_ID, "");
		assertInplaceInputValue(_SIMPLE_WITH_CONTROLS_NAME_ID, "");
		ajaxClient.setInplaceInput(_SIMPLE_WITH_CONTROLS_NAME_ID, _TEST_NAME_VALUE);
		assertInplaceInputValue(_SIMPLE_WITH_CONTROLS_NAME_ID, _TEST_NAME_VALUE);
	}

	/**
	 * This test will set the value of an richface inplace input control.
	 * 
	 * This version is for the "Custom Controls" version of
	 * the control in the richfaces demo and tests the save
	 * action only.
	 * 
	 * @throws IOException 
	 * 
	 */
	public void testInplaceInput_CustomControls() throws IOException
	{
		ajaxClient.setInplaceInput(_CUSTOM_CONTROLS_ID, "", _CUSTOM_CONTROLS_SAVE_ID);
		assertInplaceInputValue(_CUSTOM_CONTROLS_ID, "");
		ajaxClient.setInplaceInput(_CUSTOM_CONTROLS_ID, _TEST_NAME_VALUE, _CUSTOM_CONTROLS_SAVE_ID);
		assertInplaceInputValue(_CUSTOM_CONTROLS_ID, _TEST_NAME_VALUE);
	}
	
}
