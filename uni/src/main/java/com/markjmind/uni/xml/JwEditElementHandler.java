package com.markjmind.uni.xml;

import java.util.ArrayList;
import java.util.Hashtable;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
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
	Hashtable<String,String> tempTag = new Hashtable<String, String>();
	Hashtable<String,Integer> nodeIndex = new Hashtable<String, Integer>();
	public static String SEP = "+";
	
	ArrayList<String> nodePath = new ArrayList<String>();
	ArrayList<JwElement> putEndString = new ArrayList<JwElement>();
	ArrayList<JwTag> tagString = new ArrayList<JwTag>();
	
	public void putEndElementString(String nodeName, int dep,String name, String tag){
		putEndString.add(new JwElement(nodeName, dep, name, tag));
	}
	
	public void putEndElementString(String nodeName, String name, String tag){
		putEndString.add(new JwElement(nodeName, -1, name, tag));
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
    	}else{
    		nodePath.clear();
    		nodeIndex.clear();
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
    	nodePath.add(localName);
    	
    	
    	//몇번째 태그인지 검사
    	String nodePathStr = "";
    	int index = 0;
    	for(int i=0;i<nodePath.size();i++){
    		if(i==0){
    			nodePathStr = nodePath.get(i);
    		}else{
    			nodePathStr = nodePathStr+SEP+nodePath.get(i);
    		}
    	}
    	if(nodeIndex.containsKey(nodePathStr) ){
    		index = nodeIndex.get(nodePathStr)+1;
    	}
    	nodeIndex.put(nodePathStr, index);
    }
    
    @Override
    public void endElement(String uri, String localName, String qName){
    	dep--;
    	JwElement je;
    	for(int i=0;i<putEndString.size();i++){
    		je = putEndString.get(i);
    		if(je.nodeName.equals(localName) && (je.dep==-1 || je.dep==dep)){
    			putElementString(je.name, je.tag);
    		}
    		if(je.nodeName==null && je.dep==0 && dep==0){
    			putElementString(je.name, je.tag);
    		}
    	}
    		
    	JwTag jt;
    	boolean isModify = false;
    	String nodePathStr = "";
    	
    	for(int i=0;i<nodePath.size();i++){
    		if(i<nodePath.size()-1){
	    		if(i==0){
	    			nodePathStr = nodePath.get(i);
	    		}else{
	    			nodePathStr = nodePathStr+SEP+nodePath.get(i);
	    		}
    		}
    	}
    	int index =0;
    	if(dep>0){
    		index = nodeIndex.get(nodePathStr);
    	}
    	//사용자 태그값 변경
    	for(int i=0;i<tagString.size();i++){
    		jt = tagString.get(i);
    		if(jt.nodeName.equals(localName)){
    			if(jt.nodePath==null || jt.nodePath.equals(nodePathStr)){
    				if(jt.index==-1 || jt.index==index){
		    			addLine(lineString);
		    	    	addDep(dep+1, depString);
		    	    	xmlString.append(jt.tag);
		    	    	isModify = true;
		    	    	break;
    				}
    			}
    		}
    	}
    	
    	if(!isModify && text.toString().trim().length()>0){
	    	addLine(lineString);
	    	addDep(dep+1, depString);
	    	xmlString.append(text.toString().trim());
    	}
    	if(tempTag.size()>0){
    		String key = nodePathStr+SEP+localName;
    		if(tempTag.containsKey(key)){
    			addLine(lineString);
    	    	addDep(dep+1, depString);
	    		String t = tempTag.get(key);
	    		xmlString.append(t);
    		}
    	}
    		
    	addLine(lineString);
    	addDep(dep, depString);
    	xmlString.append("</");
    	xmlString.append(localName);
    	xmlString.append(">");
    	text.setLength(0);
    	if(nodePath.size()>0){
    		nodePath.remove(nodePath.size()-1);
    	}
    }

    @Override
    public void characters(char[] ch, int start, int length)throws SAXException {
    	if(text.toString().trim().length()>0){
    		String nodePathStr = "";
        	for(int i=0;i<nodePath.size();i++){
    	    	if(i==0){
    	    		nodePathStr = nodePath.get(i);
    	    	}else{
    	    		nodePathStr = nodePathStr+SEP+nodePath.get(i);
    	    	}
        	}
    		tempTag.put(nodePathStr, text.toString().trim());
    		text.setLength(0);
    	}
    	
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
    
    public void setTag(String nodeName, String tag){
    	tagString.add(new JwTag(null, nodeName, tag,-1));
    }
    
    public void setTag(String nodePath, String nodeName, String tag){
    	tagString.add(new JwTag(nodePath, nodeName, tag, -1));
    }
    
    public void setTag(String nodePath, String nodeName, String tag, int index){
    	tagString.add(new JwTag(nodePath, nodeName, tag, index));
    }
    
    
    private class JwElement{
    	public String nodeName;
    	public int dep;
    	public String name;
    	public String tag;
    	public JwElement(String nodeName, int dep,String name, String tag){
    		this.nodeName = nodeName;
    		this.dep = dep;
    		this.name = name;
    		this.tag = tag;
    	}
    }
    
    private class JwTag{
    	String nodePath;
    	String nodeName;
    	String tag;
    	int index;
    	public JwTag(String nodePath, String nodeName, String tag, int index){
    		this.nodePath = nodePath;
    		this.nodeName = nodeName;
    		this.tag = tag;
    		this.index = index;
    	}
    }
}
