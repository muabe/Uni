package com.markjmind.mobile.api.android.xml;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * start : 2013.11.16<br>
 * <br>
 * 
 * @author 오재웅
 * @version 2013.11.16
 */

public interface JwDomHandler {
	public void startElement(Node node, String name, String text, NamedNodeMap attr, Element root);
	public void endElement(Node node, String name, String text, NamedNodeMap attr, Element root);
}
