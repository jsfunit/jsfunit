package org.richfaces.demo.tree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.richfaces.model.TreeNode;

public class Artist implements TreeNode {
	private long id;
	private Map albums = new HashMap();
	private String name;
	private Library library;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6831863694596474846L;

	public Artist(long id) {
		this.id = id;
	}

	public void addAlbum(Album album) {
		addChild(Long.toString(album.getId()), album);
		album.setParent(this);
	}
	
	public void addChild(Object identifier, TreeNode child) {
		albums.put(identifier, child);
	}

	public TreeNode getChild(Object id) {
		return (TreeNode) albums.get(id);
	}

	public Iterator getChildren() {
		return albums.entrySet().iterator();
	}

	public Object getData() {
		return this;
	}

	public TreeNode getParent() {
		return library;
	}

	public boolean isLeaf() {
		return albums.isEmpty();
	}

	public void removeChild(Object id) {
		albums.remove(id);
	}

	public void setData(Object data) {
	}

	public void setParent(TreeNode parent) {
		library = (Library) parent;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Library getLibrary() {
		return library;
	}

	public void setLibrary(Library library) {
		this.library = library;
	}
	public String getType() {
		return "artist";
	}
}
