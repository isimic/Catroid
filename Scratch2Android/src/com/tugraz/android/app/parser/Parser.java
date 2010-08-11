package com.tugraz.android.app.parser;


import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import com.tugraz.android.app.content.BrickDefine;

import android.util.Log;
import android.util.Xml;


public class Parser {
	private DocumentBuilder builder;
	private Document doc;
	private static int mIdCounter = 1;
	
	final static int CMD_SET_BACKGROUND = 0;
	final static int CMD_SET_SOUND = 100;
	final static int CMD_WAIT = 200;

	public Parser() {
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			Log.e("Parser", e.getMessage());
		} catch (FactoryConfigurationError e) {
			Log.e("Parser", e.getMessage());
		}
	}
	
	public TreeMap<String, ArrayList<HashMap<String, String>>> parse(InputStream stream){
		TreeMap<String, ArrayList<HashMap<String, String>>> spritesMap = new TreeMap<String, ArrayList<HashMap<String,String>>>();
		mIdCounter = 1;
		
		try {
			doc = builder.parse(stream);	
		}
		catch (Exception e) {
			Log.e("Parser", "A parser error occured");
			e.printStackTrace();
		}
		Node stage = doc.getElementsByTagName("stage").item(0);
		NodeList sprites = doc.getElementsByTagName("sprite");
		
		// first read out stage objects
		NodeList bricks = stage.getChildNodes();
		ArrayList<HashMap<String, String>> sublist = new ArrayList<HashMap<String,String>>();
		for (int i=0; i<bricks.getLength(); i++) {
			int brickType = Integer.parseInt(bricks.item(i).getAttributes().getNamedItem("id").getNodeValue());
			String value = "";
			String value1 = "";
			switch (brickType){
				case BrickDefine.SET_BACKGROUND:
				case BrickDefine.PLAY_SOUND:
				case BrickDefine.SET_COSTUME:
					value = bricks.item(i).getFirstChild().getAttributes().getNamedItem("path").getNodeValue();
					break;
				case BrickDefine.WAIT:
					value = bricks.item(i).getFirstChild().getNodeValue();
					break;
				case BrickDefine.GO_TO:
					value = bricks.item(i).getFirstChild().getFirstChild().getNodeValue();
					value1 = bricks.item(i).getLastChild().getFirstChild().getNodeValue();
					break;
			}
			HashMap<String, String> map = getBrickMap(value, value1, brickType);
			sublist.add(map);
			
			mIdCounter++;
			
		}
		
		spritesMap.put("stage", sublist);
		
		//then read out sprites
		for (int j=0; j<sprites.getLength(); j++){
			bricks = sprites.item(j).getChildNodes();
			sublist = new ArrayList<HashMap<String,String>>();
			String name;
			name = sprites.item(j).getAttributes().getNamedItem("name").getNodeValue();
			for (int i=0; i<bricks.getLength(); i++) {
				int brickType = Integer.parseInt(bricks.item(i).getAttributes().getNamedItem("id").getNodeValue());
				String value = "";
				String value1 = "";
				switch (brickType){
					case BrickDefine.SET_BACKGROUND:
					case BrickDefine.PLAY_SOUND:
					case BrickDefine.SET_COSTUME:
						value = bricks.item(i).getFirstChild().getAttributes().getNamedItem("path").getNodeValue();
						break;
					case BrickDefine.WAIT:
						value = bricks.item(i).getFirstChild().getNodeValue();
						break;
					case BrickDefine.GO_TO:
						value = bricks.item(i).getFirstChild().getFirstChild().getNodeValue();
						value1 = bricks.item(i).getLastChild().getFirstChild().getNodeValue();
						break;
				}
				HashMap<String, String> map = getBrickMap(value, value1, brickType);
				sublist.add(map);
				mIdCounter++;
				
			}
			spritesMap.put(name, sublist);
		}
		
		return spritesMap;
	}

	public String toXml(TreeMap<String, ArrayList<HashMap<String,String>>> spritesMap) {
		TreeMap<String, ArrayList<HashMap<String,String>>> tempMap = new TreeMap<String, ArrayList<HashMap<String,String>>>();
		tempMap.putAll(spritesMap);
		
		doc = builder.newDocument(); //TODO eventuell nachher checken ob sich was veraendert hat und nur das aendern
		
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
    	
		try {
	    	serializer.setOutput(writer);
	    	serializer.startDocument("UTF-8", true);
	    	serializer.startTag("", "project");
		}
		catch (Exception e){
	    	Log.e("Parser","An error occured in toXml");
	    	e.printStackTrace();
		}
		
		boolean stageRead = false;
		int mapSizeBeforeRemoving = tempMap.size();;
		
		for (int j=0; j<mapSizeBeforeRemoving; j++) {
			ArrayList<HashMap<String, String>> sprite = tempMap.get(tempMap.firstKey());
		    try {
		    	if (!stageRead) //workaround so that stage always is the first element in xml file
	    		{
	    			sprite = tempMap.get("stage");
	    			serializer.startTag("", "stage");
	    			serializer.attribute("", "name", "stage");
	    		}
		    	else
		    	{
		    		serializer.startTag("", "sprite");
		    	    serializer.attribute("", "name", tempMap.firstKey());
		    	}
		    	for (int i=0; i<sprite.size(); i++) {
		    		HashMap<String,String> brick = sprite.get(i);
		    		
					switch (Integer.parseInt(brick.get(BrickDefine.BRICK_TYPE))){ //TODO nicht bei jedem durchlauf neue elemente erzeugen sonder nur clonen
					case BrickDefine.SET_BACKGROUND:
						serializer.startTag("", "command");
						serializer.attribute("", "id", Integer.toString(BrickDefine.SET_BACKGROUND));
						serializer.startTag("", "image");
						serializer.attribute("", "path", brick.get(BrickDefine.BRICK_VALUE));
						serializer.endTag("", "image");
						serializer.endTag("", "command");
						break;
					case BrickDefine.PLAY_SOUND:
						serializer.startTag("", "command");
						serializer.attribute("", "id", Integer.toString(BrickDefine.PLAY_SOUND));
						serializer.startTag("", "sound");
						serializer.attribute("", "path", brick.get(BrickDefine.BRICK_VALUE));
						serializer.endTag("", "sound");
						serializer.endTag("", "command");
						break;
					case BrickDefine.WAIT:
						serializer.startTag("", "command");
						serializer.attribute("", "id", Integer.toString(BrickDefine.WAIT));
						serializer.text(brick.get(BrickDefine.BRICK_VALUE));
						serializer.endTag("", "command");
						break;
					case BrickDefine.HIDE:
						serializer.startTag("", "command");
						serializer.attribute("", "id", Integer.toString(BrickDefine.HIDE));
						serializer.endTag("", "command");
						break;
					case BrickDefine.SHOW:
						serializer.startTag("", "command");
						serializer.attribute("", "id", Integer.toString(BrickDefine.SHOW));
						serializer.endTag("", "command");
						break;
					case BrickDefine.SET_COSTUME:
						serializer.startTag("", "command");
						serializer.attribute("", "id", Integer.toString(BrickDefine.SET_COSTUME));
						serializer.startTag("", "image");
						serializer.attribute("", "path", brick.get(BrickDefine.BRICK_VALUE));
						serializer.endTag("", "image");
						serializer.endTag("", "command");
						break;
					case BrickDefine.GO_TO:
						serializer.startTag("", "command");
						serializer.attribute("", "id", Integer.toString(BrickDefine.GO_TO));
						serializer.startTag("", "x");
						serializer.text(brick.get(BrickDefine.BRICK_VALUE));
						serializer.endTag("", "x");
						serializer.startTag("", "y");
						serializer.text(brick.get(BrickDefine.BRICK_VALUE_1));
						serializer.endTag("", "y");
						serializer.endTag("", "command");
						break;
					
					}
		    	}
		    	if (!stageRead) {
		    		serializer.endTag("", "stage");
		    		stageRead = true;
		    		tempMap.remove("stage");
		    	}
		    	else {
		    		serializer.endTag("", "sprite");
		    		tempMap.remove(tempMap.firstKey());
		    	}
			}
		    catch (Exception e){
		    	Log.e("Parser","An error occured in toXml");
		    	e.printStackTrace();
		    }
		}

		try{ 
			serializer.endTag("", "project");
			serializer.endDocument();
		}
		catch (Exception e){
			Log.e("Parser","An error occured in toXml");
	    	e.printStackTrace();
		}
	    
	    return writer.toString();
		

	}
	
	private HashMap<String, String> getBrickMap(String value, int type) {
		return getBrickMap("Name", value, "", type);
	}
	
	private HashMap<String, String> getBrickMap(String value, String value1, int type) {
		return getBrickMap("Name", value, value1, type);
	}
	
	private HashMap<String, String> getBrickMap(String name, String value, String value1, int type) {
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put(BrickDefine.BRICK_ID, Integer.toString(mIdCounter));
	    map.put(BrickDefine.BRICK_TYPE, String.valueOf(type));
	    map.put(BrickDefine.BRICK_NAME, name);
	    map.put(BrickDefine.BRICK_VALUE, value);
	    map.put(BrickDefine.BRICK_VALUE_1, value1);
		
		
		return map;
	}
		
}
