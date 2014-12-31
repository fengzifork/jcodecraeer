package com.example.jcodecraeer.sax;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.example.jcodecraeer.entity.JcodeMenu;


public class MenuHandler extends DefaultHandler{
	private static final String SELECT_MENU = "menu";
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String HREF = "href";
	private static final String REL = "rel";
	
	private ArrayList<JcodeMenu> menus;
	private JcodeMenu menu;
	
	private String content;

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		content = new String(ch, start, length);
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if(menu!=null){
			if(NAME.equals(localName)){
				menu.setName(content);
			}else if(HREF.equals(localName)){
				menu.setHref(content);
			}else if(REL.equals(localName)){
				menu.setRel(content);
			} 
		}
		if (SELECT_MENU.equals(localName)) {
			menus.add(menu);
			menu = null;
		}
		 
		content = "";
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		menus = new ArrayList<JcodeMenu>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(menus!=null){
			if (SELECT_MENU.equals(localName)) {
				menu = new JcodeMenu();
				menu.setId(Integer.parseInt(attributes.getValue(ID)));
	        }
		}
	}

	public ArrayList<JcodeMenu> getMenus() {
		return menus;
	}

	public void setMenus(ArrayList<JcodeMenu> menus) {
		this.menus = menus;
	}
	
 
}