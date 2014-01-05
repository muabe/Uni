package com.markjmind.mobile.api.android.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.markjmind.mobile.api.android.util.JwTextUtils;
import com.markjmind.mobile.api.android.xml.JwXmlLoader;
/**
 * start : 2013.11.17<br>
 * <br>
 * 
 * @author 오재웅
 * @version 2013.11.17
 */
public class ViewerXmlMapper {
	public HashMap<String, ViewerInfo> viewers = new HashMap<String, ViewerInfo>();
	private Application app;
	
	public ViewerXmlMapper(Application app){
		this.app = app;
	}
	
	
	public void load(int R_raw_xml){
		ViewerXmlHandler viewerXmlHandler = new ViewerXmlHandler();
		JwXmlLoader xml = new JwXmlLoader(viewerXmlHandler);
		try {
			xml.readXml(app, R_raw_xml);
			Log.d("JwViewer", viewerXmlHandler.getLog());
		} catch (ParserConfigurationException e) {
			throw new JwMapperException("viewer xml의 parsing을 수행하지 못했습니다.", e);
		} catch (SAXException e) {
			throw new JwMapperException("viewer xml의 parsing을 수행하지 못했습니다.", e);
		} catch (IOException e) {
			throw new JwMapperException("viewer xml의 데이터를 읽지 못했습니다.", e);
		}
	}
	
	public void add(String name, ViewerInfo viewerInfo){
		viewers.put(name, viewerInfo);
	}
	
	public void add(ViewerInfo viewerInfo){
		viewers.put(viewerInfo.id, viewerInfo);
	}
	
	public void add(String id, int R_layout_id, String desc, JwViewer jwViewer, Class viewerClass){
		ViewerInfo info = new ViewerInfo(id, R_layout_id, jwViewer, desc, viewerClass);
		add(info);
	}
	
	/**
	 * 이부분 계속추가
	 * @param id
	 * @param context
	 * @return
	 */
	public JwViewer getViewer(String id, Activity context){
		ViewerInfo info = viewers.get(id);
		return info.getViewer(context);
	}
	public JwViewer getViewer(String id, Dialog context){
		ViewerInfo info = viewers.get(id);
		return info.getViewer(context);
	}
	
	
	public void setInstanceOption(String id, int opt){
		ViewerInfo info = viewers.get(id);
		info.setInstanceOption(opt);
	}
	
	private class ViewerXmlHandler extends DefaultHandler{
    	private StringBuilder text = new StringBuilder();
    	private StringBuffer log = new StringBuffer();
    	
    	public String getLog(){
    		return log.toString();
    	}
    	
        @Override
        public void startElement(String uri, String localName, String qName,Attributes attributes){
        	if("viewer".equals(localName)){
        		String layoutName = attributes.getValue("layout");
        		String id = JwTextUtils.getEmpty(attributes.getValue("id"), layoutName);
        		String[] otpInstance ={"new","save"};
        		int instance = JwTextUtils.getContainsIndex(attributes.getValue("instance"), otpInstance,  1);
        		int layout_id = JwStringID.getLayoutID(layoutName, app);
        		
        		Class vclass=null;
        		if(layoutName==null || layoutName.trim().length()==0){
        			throw new JwMapperException("viewer Node에 layout 속성을 지정하지 않았습니다.");
        		}
				try {
					vclass = Class.forName(attributes.getValue("class"));
				} catch (ClassNotFoundException e) {
					throw new JwMapperException("["+attributes.getValue("class")+"] 클래스가 존재하지 않습니다.", e);
				}
				
				log.append("add Viewer[id:"+id+"  layout:"+layoutName+"  class:"+vclass.getName()+"]\n");
        		ViewerInfo info = new ViewerInfo(id, layout_id, null, "", vclass);
        		if(instance==0){
        			instance = JwViewer.INSTANCE_NEW;
        		}
        		info.setInstanceOption(instance);
        		add(info);
        	}
            text.setLength(0);
        }
        @Override
        public void endElement(String uri, String localName, String qName){
        	text.setLength(0);
        }

        
        @Override
        public void characters(char[] ch, int start, int length)throws SAXException {
        	text.append(ch,start,length);
        }
	}
	
	private class ViewerInfo{
		public String id;
		public int layoutId;
		public JwViewer viewer;
		public String desc;
		public Class viewerClass;
		public int instance;
		
		public ViewerInfo(String id, int layoutId, JwViewer viewer, String desc, Class viewerClass){
			this.id = id;
			this.layoutId = layoutId;
			this.viewer = viewer;
			this.desc = desc;
			this.viewerClass = viewerClass;
			setInstanceOption(JwViewer.INSTANCE_SAVE);
		}
		
		public JwViewer getViewer(Object context){
			if(instance == JwViewer.INSTANCE_NEW){
				viewer=null;
				return  newViewerInstanceObj(context);
			}else{
				if(viewer==null){
					viewer = newViewerInstanceObj(context);
					return  newViewerInstanceObj(context);
				}
			}
			viewInit(viewer, context);
			return viewer;
		}
		
		private JwViewer newViewerInstanceObj(Object context){
			JwViewer bl = getJwViewer();
			viewInit(bl, context);
			bl.setId(id);
			return bl;
		}
		
		/**
		 * 이부분 계속 추가
		 * @param bl
		 * @param context
		 */
		private void viewInit(JwViewer bl, Object context){
			if(context instanceof Activity){
				bl.init((Activity)context,layoutId);
			}else if(context instanceof Dialog){
				bl.init(((Dialog)context),layoutId);
			}else{
				throw new JwMapperException(viewerClass.getName()+" 클래스를 생성할수 없습니다. 해당 Viewer의 context가 Activity인지 Dialog인지 확인 하시기 바랍니다.", null);
			}
			
		}
		
		private JwViewer getJwViewer(){
			try {
				JwViewer bl = (JwViewer)viewerClass.newInstance();
				return bl;
			} catch (InstantiationException e) {
				throw new JwMapperException(viewerClass.getName()+" 클래스를 생성할수 없습니다.", e);
			} catch (IllegalAccessException e) {
				throw new JwMapperException(viewerClass.getName()+"의 접근권한이 없습니다.", e);
			}
		}
		public void setInstanceOption(int opt){
			instance = opt;
		}
	}
}
