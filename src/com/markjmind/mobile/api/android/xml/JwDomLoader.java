package com.markjmind.mobile.api.android.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 * start : 2013.11.16<br>
 * <br>
 * 
 * @author 오재웅
 * @version 2013.11.16
 */
public class JwDomLoader {
	public Element root;
	JwDomHandler handler;
	
	public void load(JwDomHandler handler,String xml) throws ParserConfigurationException, SAXException, IOException{
		this.handler = handler;
	   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	   DocumentBuilder builder = factory.newDocumentBuilder();
	   InputStream istream = new ByteArrayInputStream(xml.getBytes("utf-8"));
	   Document doc = builder.parse(istream);
	 
	   root = doc.getDocumentElement();
	   NodeList nodeList = root.getChildNodes();
	   rootTreeRun(nodeList, root);
	}
	
	public void rootTreeRun(NodeList nodeList, Element root){
		   for(int i=0;i<nodeList.getLength();i++){
			   Node node = nodeList.item(i);
			   if(node.getNodeType() == Node.TEXT_NODE)
			   {
				   continue;
			   }
			   String name = node.getNodeName();
			   String text = node.getTextContent();
			   NamedNodeMap attr = node.getAttributes();
			   
			   handler.startElement(node, name, text, attr, root);
			   
			   NodeList childNodeList = node.getChildNodes();
			   rootTreeRun(childNodeList,root);
			   
			   handler.endElement(node, name, text, attr, root);
			   
		   }
	}
}
