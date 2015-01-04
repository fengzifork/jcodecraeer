package com.fgj.jcodecraeer.entity;

import java.io.Serializable;


public class Component implements Serializable,Cloneable{
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name="";
	private String href="";
	 
	
	
	public Component(){
		
	}

	public Object clone() throws CloneNotSupportedException {
		Component menu = null;
		try {
			menu = (Component) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return menu;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	 
	 

	
}
