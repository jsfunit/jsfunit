package org.jboss.jsfunit.analysis;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ViewParser {

	private List actionListeners = new LinkedList();
	
	public List getActionListeners() {
		return actionListeners;
	}

	public void parse(Node node) {
		
		NamedNodeMap attributes = node.getAttributes();
		NodeList children = node.getChildNodes();
		
		for(int i = 0; attributes != null && i < attributes.getLength(); i++) {
			Node attribute = attributes.item(i);
			if( "actionListener".equals(attribute.getNodeName()) )
				actionListeners.add(attribute.getNodeValue());
		}
		
		for(int c = 0 ; c < children.getLength(); c++) 
			parse(node.getChildNodes().item(c));
		
	}

}