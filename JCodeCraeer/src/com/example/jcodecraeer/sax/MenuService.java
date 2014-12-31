package com.example.jcodecraeer.sax;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;

import com.example.jcodecraeer.entity.JcodeMenu;

public class MenuService {
	private Context context;
	
	public MenuService(Context context){
		this.context = context;
	}
	
	public ArrayList<JcodeMenu> getMenus() {
		ArrayList<JcodeMenu> falls = new ArrayList<JcodeMenu>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();

			MenuHandler handler = new MenuHandler();
			reader.setContentHandler(handler);
			reader.parse(new InputSource(context.getAssets().open(
					"jcodeMenus.xml")));
			
			falls = handler.getMenus();
			
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return falls;
	}
}
