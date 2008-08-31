package org.richfaces.demo.tree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.richfaces.model.TreeNode;

public class Album implements TreeNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6514596192023597908L;
	private long id;
	private Map songs = new HashMap();
	private String title;
	private Integer year;
	private Artist artist;

	public Album(long id) {
		this.id = id;
	}
	
	public void addSong(Song song) {
		addChild(Long.toString(song.getId()), song);
		song.setParent(this);
	}
	public void addChild(Object identifier, TreeNode child) {
		songs.put(identifier, child);
	}

	public TreeNode getChild(Object id) {
		return (TreeNode) songs.get(id);
	}

	public Iterator getChildren() {
		return songs.entrySet().iterator();
	}

	public Object getData() {
		return this;
	}

	public TreeNode getParent() {
		return artist;
	}

	public boolean isLeaf() {
		return songs.isEmpty();
	}

	public void removeChild(Object id) {
		songs.remove(id);
	}

	public void setData(Object data) {
	}

	public void setParent(TreeNode parent) {
		this.artist = (Artist) parent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public long getId() {
		return id;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}
	
	public String getType() {
		return "album";
	}
	

}
