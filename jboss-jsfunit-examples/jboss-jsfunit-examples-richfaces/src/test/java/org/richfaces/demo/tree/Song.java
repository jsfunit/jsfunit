package org.richfaces.demo.tree;

import java.util.ArrayList;
import java.util.Iterator;

import org.richfaces.model.TreeNode;

public class Song implements TreeNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7155620465939481885L;
	private long id;
	private String title;
	private String genre;
	private int trackNumber;
	private Album album;

	public Song(long id) {
		this.id = id;
	}
	
	public void addChild(Object identifier, TreeNode child) {
		throw new UnsupportedOperationException("Songs do not have children");
	}

	public TreeNode getChild(Object id) {
		throw new UnsupportedOperationException("Songs do not have children");
	}

	public Iterator getChildren() {
		// TODO: Fix me!
		return new ArrayList().iterator(); // work around limitation for TreeNode
	}

	public Object getData() {
		return this;
	}

	public TreeNode getParent() {
		return album;
	}

	public boolean isLeaf() {
		return true;
	}

	public void removeChild(Object id) {
		throw new UnsupportedOperationException("Songs do not have children");
	}

	public void setData(Object data) {
	}

	public void setParent(TreeNode parent) {
		this.album = (Album) parent;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(int trackNumber) {
		this.trackNumber = trackNumber;
	}

	public long getId() {
		return id;
	}
	public String getType() {
		return "song";
	}
}
