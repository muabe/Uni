package com.markjmind.mobile.api.android.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.test.IsolatedContext;
/**
 * start : 2013.11.16<br>
 * <br>
 * 
 * @author 오재웅
 * @version 2013.11.16
 */
public class JwEditElementHandler extends DefaultHandler{
	StringBuilder text = new StringBuilder();
	StringBuilder xmlString= new StringBuilder();
	int dep=0;
	String depString =null;
	String lineString =null;
	boolean isStart = false;
	
	ArrayList<JwElement> putEndString = new ArrayList<JwElement>();
	ArrayList<JwTag> tagString = new ArrayList<JwTag>();
	
	public void putEndElementString(String parentName, int dep,String name, String tag){
		putEndString.add(new JwElement(parentName, dep, name, tag));
	}
	
	public void putEndElementString(String parentName, String name, String tag){
		putEndString.add(new JwElement(parentName, -1, name, tag));
	}
	
	public void putEndElementString(String name, String tag){
		putEndString.add(new JwElement(null, 0, name, tag));
	}
	
	public String getXmlString(){
		return xmlString.toString().trim();
	}
	public String toString(){
		return xmlString.toString().trim();
	}
    @Override
    public void startElement(String uri, String localName, String qName,Attributes attributes){
    	if(dep!=0){
    		addLine(lineString);
    		addDep(dep, depString);
    	}
    	xmlString.append("<");
    	xmlString.append(localName);
    	for(int i=0;i<attributes.getLength();i++){
    		String tempLocalName = attributes.getLocalName(i);
    		String value = attributes.getValue(uri, tempLocalName);
    		xmlString.append(" ");
    		xmlString.append(tempLocalName);
    		xmlString.append("=");
    		xmlString.append("\""+value+"\"");
    	}
    	xmlString.append(">");
    	dep++;
    }
    
    @Override
    public void endElement(String uri, String localName, String qName){
    	dep--;
    	
    	JwElement je;
    	for(int i=0;i<putEndString.size();i++){
    		je = putEndString.get(i);
    		if(je.parentName.equals(localName) && (je.dep==-1 || je.dep==dep)){
    			putElementString(je.name, je.tag);
    		}
    		if(je.parentName==null && je.dep==0 && dep==0){
    			putElementString(je.name, je.tag);
    		}
    	}
    	
    	JwTag jt;
    	boolean isModify = false;
    	for(int i=0;i<tagString.size();i++){
    		jt = tagString.get(i);
    		if(jt.parentName.equals(localName) && (jt.dep==-1 || jt.dep==dep)){
    			addLine(lineString);
    	    	addDep(dep+1, depString);
    	    	xmlString.append(jt.tag);
    	    	isModify = true;
    	    	break;
    		}
    	}
    	
    	if(!isModify && text.toString().trim().length()>0){
	    	addLine(lineString);
	    	addDep(dep+1, depString);
	    	xmlString.append(text.toString().trim());
    	}
    		
    		
    	addLine(lineString);
    	addDep(dep, depString);
    	xmlString.append("</");
    	xmlString.append(localName);
    	xmlString.append(">");
    	text.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length)throws SAXException {
    	text.append(ch,start,length);
    }
    
    public void setFormat(String lineString, String depString){
    	this.lineString = lineString;
    	this.depString = depString;
    }
    
    public void addDep(int dep, String depString){
    	if(depString!=null){
	    	for(int i=0;i<dep;i++){
	    		xmlString.append(depString);
	    	}
    	}
    }
    
    public void addLine(String lineString){
    	if(lineString!=null){
	    	xmlString.append(lineString);
    	}
    }
    
    public void addDep(int increase){
    	if(depString!=null){
	    	for(int i=0;i<dep+increase;i++){
	    		xmlString.append(depString);
	    	}
    	}
    }
    
    public void addLine(){
    	if(lineString!=null){
	    	xmlString.append(lineString);
    	}
    }
    public String getLine(){
    	return this.lineString;
    }
    public String getDep(){
    	return this.depString;
    }
    
    private void putElementString(String name, String tag){
    	addLine();
    	addDep(1);
    	String str = "";
	    if(name!=null){	
	    	str+= "<"+name+">";
    	}
	    if(tag!=null){
	    	str+= tag;
	    }
	    if(name!=null){	
	    	str+= "</"+name+">";
    	}
		xmlString.append("<"+name+">"+tag+"</"+name+">");
    }
    
    public void setTag(String parentName, int dep,String tag){
    	tagString.add(new JwTag(parentName, dep, tag));
    }
    public void setTag(String parentName, String tag){
    	tagString.add(new JwTag(parentName, -1, tag));
    }
    
    
    private class JwElement{
    	public String parentName;
    	public int dep;
    	public String name;
    	public String tag;
    	public JwElement(String parentName, int dep,String name, String tag){
    		this.parentName = parentName;
    		this.dep = dep;
    		this.name = name;
    		this.tag = tag;
    	}
    }
    
    private class JwTag{
    	String parentName;
    	int dep;
    	String tag;
    	public JwTag(String parentName, int dep,String tag){
    		this.parentName = parentName;
    		this.dep = dep;
    		this.tag = tag;
    	}
    }
}
