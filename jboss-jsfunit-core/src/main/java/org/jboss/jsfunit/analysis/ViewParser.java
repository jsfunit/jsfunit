package org.jboss.jsfunit.analysis;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ViewParser {

	private Map<String, List<String>> actionListeners = new HashMap<String, List<String>>();
	private Map<String, List<String>> actions = new HashMap<String, List<String>>();
	
	public Map<String, List<String>> getActionListeners() {
		return actionListeners;
	}
	
	public Map<String, List<String>> getActions() {
		return actions;
	}

	private void addActionListener(String path, String el) {
		List<String> list = actionListeners.get(path);
		if(list == null) 
			actionListeners.put(path, (list = new LinkedList<String>()));
		list.add(el);
	}
	
	private void addAction(String path, String el) {
		List<String> list = actions.get(path);
		if(list == null) 
			actions.put(path, (list = new LinkedList<String>()));
		list.add(el);
	}
	
	public void parse(Node node, String path) {
		
		NamedNodeMap attributes = node.getAttributes();
		NodeList children = node.getChildNodes();
		
		if("actionListener".equals(node.getNodeName())) {
			Node binding = attributes.getNamedItem("binding");
			if(binding != null)
				addActionListener(path, binding.getNodeValue());
		}
		
		for(int i = 0; attributes != null && i < attributes.getLength(); i++) {
			Node action = attributes.getNamedItem("action");
			Node actionListener = attributes.getNamedItem("actionListener");
			if( actionListener != null )
				addActionListener(path, actionListener.getNodeValue());
			if(action != null)
				addAction(path, action.getNodeValue());
		}
		
		for(int c = 0 ; c < children.getLength(); c++) 
			parse(children.item(c), path);
	}

}