package org.richfaces.demo.ddmenu;

public class Menu {
	private String current;
	
	public String getCurrent() {
		return this.current;
	}
	
	public void setCurrent(String current) {
		this.current = current;
	}
	
	public String doNew() {
		this.current="New";
		return null;
	}
	public String doOpen() {
		this.current="Open";
		return null;
	}
	public String doClose() {
		this.current="Close";
		return null;
	}
	public String doSaveText() {
		this.current="Save as Text File";
		return null;
	}
	public String doSavePDF() {
		this.current="Save as PDF File";
		return null;
	}
	public String doExit() {
		this.current="Exit";
		return null;
	}
}
