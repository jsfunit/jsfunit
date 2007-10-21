package org.jboss.jsfunit.analysis;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ViewParser {

	private List actionListeners = new LinkedList();
	private List actions = new LinkedList();
	
	public List getActionListeners() {
		return actionListeners;
	}

	public void parse(Node node) {
		
		NamedNodeMap attributes = node.getAttributes();
		NodeList children = node.getChildNodes();
		
		if("actionListener".equals(node.getNodeName())) {
			Node binding = attributes.getNamedItem("binding");
			if(binding != null)
				actionListeners.add(binding.getNodeValue());
		}
		
		for(int i = 0; attributes != null && i < attributes.getLength(); i++) {
			Node action = attributes.getNamedItem("action");
			Node actionListener = attributes.getNamedItem("actionListener");
			if( actionListener != null )
				actionListeners.add(actionListener.getNodeValue());
			if(action != null)
				actions.add(action.getNodeValue());
		}
		
		for(int c = 0 ; c < children.getLength(); c++) 
			parse(node.getChildNodes().item(c));
	}

	public List getActions() {
		return actions;
	}

}