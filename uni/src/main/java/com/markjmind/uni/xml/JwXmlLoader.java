package com.markjmind.uni.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

/**
 * start : 2013.11.16<br>
 * <br>
 * 
 * @author 오재웅
 * @version 2013.11.16
 */
public class JwXmlLoader {
	public String charSet = "UTF-8";
	private DefaultHandler dataHandler;
	
	public JwXmlLoader(DefaultHandler dataHandler){
		setDataHandler(dataHandler);
	}
	
	public void setDataHandler(DefaultHandler dataHandler){
		this.dataHandler = dataHandler;
	}
	
	public DefaultHandler getDataHandler(){
		return dataHandler;
	}
	
	public String readXml(InputStream xmlInputStream) throws ParserConfigurationException, SAXException, IOException{
		SAXParserFactory saxPF = SAXParserFactory.newInstance(); 
	    SAXParser saxP = saxPF.newSAXParser();
	    XMLReader xmlR = saxP.getXMLReader();
	    xmlR.setContentHandler(dataHandler);
	    xmlR.parse(new InputSource(xmlInputStream));
	    return dataHandler.toString();
	}
	
	public String readXml(Context context, int R_raw_id) throws ParserConfigurationException, SAXException, IOException{
		  return readXml(JwXmlResoure.getStream(context, R_raw_id));
	}
	
	public String readXml(String xml) throws ParserConfigurationException, SAXException, IOException{
		   InputStream is = new ByteArrayInputStream(xml.getBytes(charSet));
		   return readXml(is);
	}
	public String readXml(URL url) throws IOException, ParserConfigurationException, SAXException{
		InputStream is = url.openStream();
		return readXml(is);
	}
	
	public static String readXml(InputStream xmlInputStream, DefaultHandler dataHandler) throws ParserConfigurationException, SAXException, IOException{
		SAXParserFactory saxPF = SAXParserFactory.newInstance(); 
	    SAXParser saxP = saxPF.newSAXParser();
	    XMLReader xmlR = saxP.getXMLReader();
	    xmlR.setContentHandler(dataHandler);
	    xmlR.parse(new InputSource(xmlInputStream));
	    return dataHandler.toString();
	}
	
	public static String readXml(Context context, int R_raw_id,DefaultHandler dataHandler) throws ParserConfigurationException, SAXException, IOException{
		  return readXml(JwXmlResoure.getStream(context, R_raw_id),dataHandler);
	}
	
	public static String readXml(String xml, DefaultHandler dataHandler, String charSet) throws ParserConfigurationException, SAXException, IOException{
		   InputStream is = new ByteArrayInputStream(xml.getBytes(charSet));
		   return readXml(is,dataHandler);
	}
	public static String readXml(String xml, DefaultHandler dataHandler) throws ParserConfigurationException, SAXException, IOException{
		   InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		   return readXml(is,dataHandler);
	}
	public static String readXml(URL url, DefaultHandler dataHandler) throws IOException, ParserConfigurationException, SAXException{
		InputStream is = url.openStream();
		return readXml(is,dataHandler);
	}
}