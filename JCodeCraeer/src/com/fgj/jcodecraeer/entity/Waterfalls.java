package com.fgj.jcodecraeer.entity;

import java.io.Serializable;
import java.util.ArrayList;


public class Waterfalls implements Serializable,Cloneable{
	private static final long serialVersionUID = 1L;
	private ArrayList<Waterfall> falls;
	private int size;
	private int pagersize;
	private int currentpager;
	
	
	public ArrayList<Waterfall> getFalls() {
		return falls;
	}
	public void setFalls(ArrayList<Waterfall> falls) {
		this.falls = falls;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getPagersize() {
		return pagersize;
	}
	public void setPagersize(int pagersize) {
		this.pagersize = pagersize;
	}
	public int getCurrentpager() {
		return currentpager;
	}
	public void setCurrentpager(int currentpager) {
		this.currentpager = currentpager;
	}
	 
}
