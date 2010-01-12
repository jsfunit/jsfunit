/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.jsfunit.richclient;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.jboss.jsfunit.jsfsession.ComponentIDNotFoundException;
import org.jboss.jsfunit.jsfsession.DuplicateClientIDException;
import org.jboss.jsfunit.jsfsession.JSFClientSession;

/**
 * This class provides helper methods for RichFaces controls.
 *
 * @author Stan Silvert
 * @since 1.0
 */
public class RichFacesClient
{
   private JSFClientSession jsfClient;

   public RichFacesClient(JSFClientSession jsfClient)
   {
      this.jsfClient = jsfClient;
   }
   
   /**
    * Set a parameter value on a DataFilterSlider.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    * @param value The value to set before the form is submitted.
    *
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
   public void setDataFilterSlider(String componentID, String value)
   {
      jsfClient.setValue(componentID + "slider_val", value);
   }
   
   /**
    * Set a parameter value on a RichCalendar component.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    * @param value The value to set before the form is submitted.
    *
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
   public void setCalendarValue(String componentID, String value)
         throws IOException
   {
      jsfClient.setValue(componentID + "InputDate", value);
   }
   
   
   /**
    * Set the value of a RichInplaceInput component.
    * 
    * @param componentID The JSF component ID or a suffix of the client ID.
    * @param value The value to set
    * @param customSaveID The JSF component ID or a suffix of a custom save button (null if not used)
    * 
    * @throws IOException
    */
   public void setInplaceInput( String componentID, String value, String customSaveID ) throws IOException
   {
      // Find outside span control
      HtmlSpan span = (HtmlSpan)jsfClient.getElement(componentID);
      // Find text control
      HtmlTextInput input = (HtmlTextInput)jsfClient.getElement(componentID+"tempValue");

      // Activate control - click on outside table control
      span.click();

      // Type value into input control
      input.type(value);

      // Type #3 - CUSTOM save button
      if( customSaveID != null ) {
         // Find and Click save button
         HtmlButton saveButton = (HtmlButton)jsfClient.getElement(customSaveID);
         saveButton.click();			
      } else {
         // Check to see if buttons are visible
         HtmlDivision bar = (HtmlDivision)jsfClient.getElement(componentID+"bar");
         String buttonStyle = bar.getStyleAttribute();
         // Type #1 - NO BUTTONS
         if( buttonStyle.contains("display:none") || buttonStyle.contains("display: none") ) 
         {
            // Remove focus from name input control
            input.blur();

            // Type #2 - built-in buttons
         } else {
            // Find and "mousedown" the standard "ok" button
            HtmlImageInput okButton = (HtmlImageInput)jsfClient.getElement(componentID+"ok");
            okButton.fireEvent("mousedown");						
         }
      }
   }
      
   /**
    * Set the value of a RichInplaceInput component.
    * This method is used when a custom save button is not needed.
    * 
    * @param componentID The JSF component ID or a suffix of the client ID.
    * @param value The value to set
    * 
    * @throws IOException
    */
   public void setInplaceInput( String componentID, String value ) throws IOException
   {
      this.setInplaceInput(componentID, value, null);
   }
   
   /**
    * Click this handle icon for a node in a RichTree in order
    * to toggle the open/closed state. In order to use this you
    * will need the key value of the node (from the tree data) 
    * and the id="" attribute value from the rich:treeNode 
    * template declaration.
    * 
    * @param treeNodeKey key value for the node to reference
    * @param treeNodeId id of the treeNode template
    * 
    * @throws IOException 
    * @throws ComponentIDNotFoundException if the component can not be found 
    */
   public void clickTreeNodeHandle( String treeNodeKey, String treeNodeId ) throws IOException
   {
	   final String handleId = ":"+treeNodeKey+"::"+treeNodeId+":handle";
	   HtmlElement icon = (HtmlElement)jsfClient.getElement(handleId);
	   if( icon == null ) throw new ComponentIDNotFoundException(handleId);
	   icon.click();	   
   }
   
   /**
    * Click a value on a DataTableScroller.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    * @param value The number to click on the scroller.
    *
    * @throws IOException if there is a problem submitting the form
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
/*   public void clickDataTableScroller(String componentID, int value) 
         throws IOException
   {
      String strValue = Integer.toString(value);
      DomNode scroller = (DomNode)jsfClient.getElement(componentID + "_table");
      List nodes = scroller.getByXPath(".//tbody/tr/td");
      System.out.println("******************************");
      System.out.println("Found " + nodes.size() + " nodes.");
      System.out.println("strValue=" + strValue);
      for (Iterator i = nodes.iterator(); i.hasNext();)
      {
         Object node = i.next();
         System.out.println("class=" + node.getClass().getName());
         if (node instanceof ClickableElement) 
         {
            ClickableElement clickable = (ClickableElement)node;
            System.out.println("Node value=" + clickable.getTextContent());
            if (strValue.equals(clickable.getTextContent()))
            {
               clickable.click();
               System.out.println("*****************************************");
               return;
            }
         }
      }
      
      throw new ComponentIDNotFoundException(componentID);
      
      // /html/body/table[3]/tbody/tr/td[2]/table/tbody/tr[2]/td/table/tr/td/div/form/div/table/tbody/tr/td[5]
   } */
   
   /**
    * Drag a component with rich:dragSupport to a component with rich:dropSupport.
    *
    * @param dragComponentID The JSF component ID or a suffix of the client ID
    *                        for the rich:dragSupport component.
    * @param dropTargetComponentID The JSF component ID or a suffix of the client ID
    *                              for the target rich:dropSupport component.
    *
    * @throws IOException if there is a problem submitting the form
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
 /*  public void dragAndDrop(String dragComponentID, String dropTargetComponentID)
         throws IOException
   {
      HtmlElement dragElement = (HtmlElement)jsfClient.getElement(dragComponentID);
      //HtmlElement dragParent = (HtmlElement)dragElement.getParentNode();
      
      HtmlElement dropElement = (HtmlElement)jsfClient.getElement("form:phppanel_body");
      
      dragElement.mouseDown();
      dropElement.mouseMove();
      dropElement.mouseUp();
      String dragClientID = client.getClientIDs().findClientID(dragComponentID);
      String dropClientID = client.getClientIDs().findClientID(dropTargetComponentID);
      Map<String, String> params = new HashMap<String, String>(3);
      params.put("dragSourceId", dragClientID);
      params.put("dropTargetId", dropClientID);
      params.put(dragClientID, dragClientID);
      ajaxSubmit(dropClientID, params);
   } */
   
   /**
    * Click a tab on a TabPanel.  Note that this method is a no-op if the tab
    * is in a disabled state or if the TabPanel is in client mode.  In client
    * mode, the tab click does not generate an ajax submit but only changes 
    * the visible tab already present on the client.  JSFUnit does not 
    * currently deal with these kinds of "client only" operations.
    *
    * @param tabPanelComponentID The JSF component ID or a suffix of the client ID
    *                            for the rich:tabPanel component.
    * @param tabPanelComponentID The JSF component ID or a suffix of the client ID
    *                            for the rich:tab component.
    *
    * @throws IOException if there is a problem submitting the form
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
   public void clickTab(String tabComponentID)
         throws IOException
   {
      HtmlElement tab = (HtmlElement)jsfClient.getElement(tabComponentID + "_shifted");
      tab.click();
   }
   
   /**
    * Set a parameter value on a RichComboBox component.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    * @param value The value to set before the form is submitted.
    *
    * @throws IOException if there is a problem submitting the form
    * @throws ComponentIDNotFoundException if the component can not be found 
    * @throws DuplicateClientIDException if more than one client ID matches the 
    *                                    componentID suffix
    */
   public void setComboBox(String componentID, String value) throws IOException
   {
      jsfClient.setValue(componentID + "comboboxValue", value);
   }
   
   /**
    * Set the value of an InputNumberSpinner.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    * @param value The value to set before the form is submitted.
    *
    * @throws IOException if an internal refresh is needed and there is an 
    *                     error sending a request to the server.
    * @throws DuplicateClientIDException if more than one client ID matches the suffix
    * @throws ClassCastException if the current page is not an HtmlPage. 
    */
   public void setInputNumberSpinner(String componentID, String value)
         throws IOException
   {
      DomNode tdTag = (DomNode)jsfClient.getElement(componentID + "Edit");
      HtmlInput input = (HtmlInput)tdTag.getChildNodes().item(0);
      input.setValueAttribute(value);
   }
   
   /**
    * Click the up arrow on an InputNumberSpinner.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    *
    * @throws IOException if an internal refresh is needed and there is an 
    *                     error sending a request to the server.
    * @throws DuplicateClientIDException if more than one client ID matches the suffix
    * @throws ClassCastException if the current page is not an HtmlPage. 
    */
   public void clickInputNumberSpinnerUp(String componentID)
         throws IOException
   {
      clickSpinnerButton(componentID, "SpinnerButtonUp");
   }
   
   /**
    * Click the down arrow on an InputNumberSpinner.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    *
    * @throws IOException if an internal refresh is needed and there is an 
    *                     error sending a request to the server.
    * @throws DuplicateClientIDException if more than one client ID matches the suffix
    * @throws ClassCastException if the current page is not an HtmlPage. 
    */
   public void clickInputNumberSpinnerDown(String componentID)
         throws IOException
   {
      clickSpinnerButton(componentID, "SpinnerButtonDown");
   }
   
   private void clickSpinnerButton(String componentID, String imageName)
          throws IOException
   {
      DomNode table = (DomNode)jsfClient.getElement(componentID + "Buttons");
      List inputs = table.getByXPath("tbody/tr/td/input");
      for (Iterator i = inputs.iterator(); i.hasNext();)
      {
         HtmlInput input = (HtmlInput)i.next();
         if (input.asXml().contains(imageName))
         {
            input.click();
            return;
         }
      }
      throw new RuntimeException("Can't find image for " + imageName);
   }

   /**
    * Set the value of an InputNumberSlider.
    *
    * @param componentID The JSF component ID or a suffix of the client ID.
    * @param value The value to set before the form is submitted.
    *
    * @throws IOException if an internal refresh is needed and there is an 
    *                     error sending a request to the server.
    * @throws DuplicateClientIDException if more than one client ID matches the suffix
    * @throws ClassCastException if the current page is not an HtmlPage. 
    */
   public void setInputNumberSlider(String componentID, String value)
         throws IOException
   {
      HtmlInput input = (HtmlInput)jsfClient.getElement(componentID + "Input");
      input.setValueAttribute(value);
   }

   /**
    * Get the HtmlElement of the tree handle for a node in a RichTree.  Typically,
    * you would then call HtmlElement.click() to toggle expanded/collapsed
    *
    * @param treeID id of the rich:tree
    * @param treeNodeID id of the rich:treeNode
    * @param nodeText text of the node
    *
    * @return The HtmlElement used to toggle expanded/collapsed,
    *         or <code>null</code> if not found.
    *
    * @throws IOException
    * @throws ComponentIDNotFoundException if the component can not be found
    */
   public HtmlElement getTreeHandle(String treeID, String treeNodeID, String nodeText) throws IOException
   {
       DomNode table = (DomNode)jsfClient.getElement(treeID);
       String xpath = "//text()[. = '" + nodeText + "']/ancestor::*" +  // all ancestors of the target text node
                      "/tr[contains(@id, ':" + treeNodeID + ":')]/descendant::*" + // which have tr descendants with the treeNodeID
                      "/a[@class='rich-tree-node-handle'][contains(@id, ':" + treeNodeID + ":')]"; // that has decendants that are tree handles
       
       HtmlElement foundElement = (HtmlElement)table.getFirstByXPath(xpath);
       if (foundElement != null) return foundElement;

       // for ajax, RichFaces will sometimes use xhtml namespace
       xpath = "//text()[. = '" + nodeText + "']/ancestor::*" +
               "/self::node()[namespace-uri()='http://www.w3.org/1999/xhtml'][local-name()='tr'][contains(@id, ':" + treeNodeID + ":')]/descendant::*" +
               "/self::node()[namespace-uri()='http://www.w3.org/1999/xhtml'][local-name()='a'][@class='rich-tree-node-handle'][contains(@id, ':" + treeNodeID + ":')]";
       return (HtmlElement)table.getFirstByXPath(xpath);
   }

   /**
    * Find out if a tree handle is expanded.
    *
    * @param treeID id of the rich:tree
    * @param treeNodeID id of the rich:treeNode
    * @param nodeText text of the node
    *
    * @return <code>true</code> if the tree handle is exapanded, <code>false</code> otherwise.
    *
    * @throws IOException
    * @throws ComponentIDNotFoundException if the component can not be found
    */
   public boolean isTreeHandleExpanded(String treeID, String treeNodeID, String nodeText) throws IOException
   {
       HtmlElement treeHandle = getTreeHandle(treeID, treeNodeID, nodeText);
       if (treeHandle == null) return false;

       String classValue = treeHandle.getAttribute("class");
       if ((classValue == null) || (!classValue.equals("rich-tree-node-handle")))
       {
           throw new IllegalArgumentException("treeHandle element is not a RichFaces tree node handle: " + treeHandle.asXml());
       }

       String handleXpath = treeHandle.getCanonicalXPath();
       String imgXpath = handleXpath + "/img[@class='rich-tree-node-handleicon-expanded'][contains(@style, 'display: none;')]";
       HtmlElement img = (HtmlElement)treeHandle.getFirstByXPath(imgXpath);
       if (img != null) return false;

       // for ajax, RichFaces will sometimes use xhtml namespace
       imgXpath = handleXpath + "/child::*/self::node()[namespace-uri()='http://www.w3.org/1999/xhtml'][local-name()='img']" +
                                             "[@class='rich-tree-node-handleicon-expanded'][contains(@style, 'display: none;')]";

       img = (HtmlElement)treeHandle.getFirstByXPath(imgXpath);
       return (img == null);
   }

   /**
    * Get the HtmlElement of the tree node that contains the given text.
    * Specifically, it finds a text node with the given text and then returns
    * a parent node whose Id contains the treeID and treeNodeID.
    *
    * Typically, you would then call HtmlElement.click() to select the
    * node.
    *
    * @param treeID id of the rich:tree
    * @param treeNodeID id of the rich:treeNode
    * @param nodeText text of the node
    *
    * @return The HtmlElement used to toggle expanded/collapsed
    *
    * @throws IOException
    * @throws ComponentIDNotFoundException if the component can not be found
    */
   public HtmlElement getTreeNodeByText(String treeID, String treeNodeID, String nodeText) throws IOException
   {
       DomNode table = (DomNode)jsfClient.getElement(treeID);
       String xpath = "//text()[. = '" + nodeText + "']/parent::*[contains(@id,'" + treeID + "')][contains(@id,'" + treeNodeID + "')]";
       return (HtmlElement)table.getFirstByXPath(xpath);
   }
}