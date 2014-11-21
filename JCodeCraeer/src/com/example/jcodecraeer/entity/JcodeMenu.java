package com.example.jcodecraeer.entity;

import java.io.Serializable;


public class JcodeMenu implements Serializable,Cloneable{
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name="";
	private String href="";
	private String rel="";
	private JcodeSubMenu[] submenus; 
	
	
	public JcodeMenu(){
		
	}

	public Object clone() throws CloneNotSupportedException {
		JcodeMenu menu = null;
		try {
			menu = (JcodeMenu) super.clone();
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

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public JcodeSubMenu[] getSubmenus() {
		return submenus;
	}

	public void setSubmenus(JcodeSubMenu[] submenus) {
		this.submenus = submenus;
	}

	
}
