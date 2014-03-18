package com.markjmind.mobile.api.android.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class JwFile {
	Context context;
	

	public JwFile(Context context){
		this.context=context;
	}
	
	public static int BUFFER_SIZE = 1024;
	
	public static class Dir{
		/**
		 * 스토리지 디렉토리를 가져온다.
		 */
		public static File storage=Environment.getExternalStorageDirectory();
		/**
		 * Data 디렉토리를 가져온다.
		 */
		public static File datd=Environment.getDataDirectory().getAbsoluteFile();
		
		/**
		 * DownloadCacheDirectory 디렉토리를 가져온다.
		 */
		public static File downloadCache=Environment.getDownloadCacheDirectory().getAbsoluteFile();
		
		/**
		 * Root 디렉토리를 가져온다.
		 */
		public static File root = Environment.getRootDirectory().getAbsoluteFile();
		
		/**
		 * Pictures 디렉토리를 가져온다.
		 */
		public static File pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
		
		/**
		 * DCIM 디렉토리를 가져온다.
		 */
		public static File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsoluteFile();
		
		/**
		 * Downloads 디렉토리를 가져온다.
		 */
		public static File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsoluteFile();
		
		/**
		 * movies 디렉토리를 가져온다.
		 */
		public static File movies = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsoluteFile();
		
		/**
		 * music 디렉토리를 가져온다.
		 */
		public static File music = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsoluteFile();
		
		
		/**
		 * notifications 디렉토리를 가져온다.
		 */
		public static File notifications = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS).getAbsoluteFile();
		
		
		/**
		 * podcasts 디렉토리를 가져온다.
		 */
		public static File podcasts = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS).getAbsoluteFile();
		
		
		/**
		 * ringtones 디렉토리를 가져온다.
		 */
		public static File ringtones = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES).getAbsoluteFile();
		
		/**
		 * CacheDir을 가져온다.
		 * @param context
		 * @return
		 */
		public static File getCacheDir(Context context){
			return context.getCacheDir();
		}
		
	}
	
	/**
	 * 파일,디렉토리 리스트를 가져온다.
	 * @param dir
	 * @return File[]
	 */
	public static File[] getList(File dir) {
		if(dir.isDirectory() && dir.exists()){
			return dir.listFiles();
		}else{
			return null;
		}
	}
	
	/**
	 * 파일,디렉토리 리스트를 가져온다.
	 * @param dirPath
	 * @return File[]
	 */
	public static File[] getList(String dirPath){
		File dir = new File(dirPath);
		return getList(dir);
	}
	
	/**
	 * 디렉토리 리스트를 가져온다.
	 * @param dir
	 * @return File[]
	 */
	public static File[] getDirList(File dir){
		if(dir.isDirectory() && dir.exists()){
			return dir.listFiles(new IsDirFilter(true));
		}else{
			return null;
		}
	}
	
	/**
	 * 디렉토리 리스트를 가져온다.
	 * @param dirPath
	 * @return File[]
	 */
	public static File[] getDirList(String dirPath){
		File dir = new File(dirPath);
		return getDirList(dir);
	}
	
	/**
	 * 파일리스트를 가져온다.
	 * @param dir
	 * @return
	 */
	public static File[] getFileList(File dir){
		if(dir.isDirectory() && dir.exists()){
			return dir.listFiles(new IsDirFilter(false));
		}else{
			return null;
		}
	}
	
	/**
	 * 파일 필터 클래스
	 * @author codemasta
	 *
	 */
	private static class IsDirFilter implements FileFilter{
		private boolean isDir;
		public IsDirFilter(boolean isDir){
			this.isDir = isDir;
		}
		@Override
		public boolean accept(File pathname) {
			if(isDir){
				return pathname.isDirectory();
			}else{
				return !pathname.isDirectory();
			}
		}
	}
	
	/**
	 * 파일을 생성한다.
	 * @param file
	 * @return
	 */
	public static boolean mkFile(File file){
		boolean result=true;
		if(!file.getParentFile().exists()){
			result = file.getParentFile().mkdirs();
		}
		if(result){
			try {
				return file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 파일을 생성한다.
	 * @param path
	 * @return
	 */
	public static boolean mkFile(String path){
		File dir = new File(path);
		return mkFile(dir);
	}
	
	/**
	 * Stream에 해당하는 파일을 생성한다.
	 * @param path
	 * @param in
	 * @param append
	 * @throws IOException
	 */
	public static void mkFile(Context context, String path, InputStream in) throws IOException{
		OutputStream out = new FileOutputStream(path,false);
//		FileOutputStream out = context.openFileOutput(path,Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE);
		
		byte[] buffer = new byte[BUFFER_SIZE];
		int readCount=0;
		while((readCount=in.read(buffer))!=-1){
			out.write(buffer, 0, readCount);
			out.flush();
		}
		out.close();
		in.close();
	}
	
	/**
	 * Stream에 해당하는 파일을 복사한다.
	 * @param file
	 * @param in
	 * @param append
	 * @return
	 * @throws IOException
	 */
	public static void mkFile(Context context, File file, InputStream in) throws IOException{
		mkFile(context, file.getPath(), in);
	}
	
	/**
	 * byte에 해당하는 파일을 생성한다.
	 * @param file
	 * @param bytes
	 * @param append
	 * @throws IOException
	 */
	public static void mkFile(Context context, File file, byte[] bytes) throws IOException{
		mkFile(context, file.getPath(), bytes);
	}
	
	/**
	 * byte에 해당하는 파일을 생성한다.
	 * @param path
	 * @param bytes
	 * @param append
	 * @throws IOException
	 */
	public static void mkFile(Context context, String path, byte[] bytes) throws IOException{
		OutputStream out = new FileOutputStream(path,false);
//		FileOutputStream out = context.openFileOutput(path,Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE);
		out.write(bytes);
		out.flush();
		out.close();
	}
	
	/**
	 * Web에 있는 파일을 다운받는다
	 * @param context
	 * @param urlName
	 * @param savefile
	 * @throws IOException
	 */
	public static void mkFileWeb(Context context, String urlName,File savefile) throws IOException{ 
		URL url = new URL(urlName) ;
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("GET");
		urlConnection.setConnectTimeout(1000 * 15);
		urlConnection.setReadTimeout(1000 * 15);
		InputStream in = urlConnection.getInputStream();
		mkFile(context,savefile, in);
	}
	
	/**
	 * Web에 있는 파일을 다운받는다
	 * @param context
	 * @param urlName
	 * @param fileName
	 * @throws IOException
	 */
	public void saveWebFile(Context context,String urlName,String fileName) throws IOException{
		mkFileWeb(context, urlName, new File(fileName));
	}
	
	/**
	 * 파일을 복사한다.
	 * @param taget
	 * @param clone
	 * @throws IOException 
	 */
	public static void copy(Context context, File target, File clone) throws IOException{
		InputStream in = new FileInputStream(target);
		mkFile(context,clone, in);
	}
	
	/**
	 * 파일을 복사한다.
	 * @param targetPath
	 * @param clone
	 * @throws IOException
	 */
	public static void copy(Context context, String targetPath, File clone) throws IOException{
		InputStream in = new FileInputStream(targetPath);
		mkFile(context,clone, in);
	}
	
	/**
	 * 파일을 복사한다.
	 * @param targetPath
	 * @param clonePath
	 * @throws IOException
	 */
	public static void copy(Context context, String targetPath, String clonePath) throws IOException{
		InputStream in = new FileInputStream(targetPath);
		mkFile(context,clonePath, in);
	}
	
	/**
	 * 파일을 복사한다.
	 * @param target
	 * @param clonePath
	 * @throws IOException
	 */
	public static void copy(Context context, File target, String clonePath) throws IOException{
		InputStream in = new FileInputStream(target);
		mkFile(context, clonePath, in);
	}
	
	/**
	 * 디렉토리를 생성한다.
	 * @param dir
	 * @return
	 */
	public static boolean mkDir(File dir){
		return dir.mkdirs();
	}
	
	/**
	 * 디렉토리를 생성한다.
	 * @param path
	 * @return
	 */
	public static boolean mkDir(String path){
		File dir = new File(path);
		return dir.mkdirs();
	}
	
	/**
	 * 파일 및 디렉토리를 삭제한다.
	 * @param file
	 * @return
	 */
	public static boolean del(File file){
		boolean result = true;
		if(file.exists()){
			if(file.isDirectory()){
				File[] files = getList(file);
				for(int i=0;i<files.length;i++){
					result = del(files[i]);
				}
			}
			if(!result){
				file.delete();
				return false;
			}
			return file.delete();
		}else{
			return false;
		}
	}
	
	/**
	 * 파일을 압축한다.
	 * @param files
	 * @param zipFile
	 * @throws IOException
	 */
	public static void zip(String[] filePath, File zipFile) throws IOException {
	    File[] files = new File[filePath.length];
	    for(int i=0;i<files.length;i++){
	    	files[i] = new File(filePath[i]);
	    }
	    zip(files, zipFile);
	}
	
	public static void zip(File[] files, File zipFile) throws IOException {
	    BufferedInputStream origin = null;
	    ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
	    try { 
	        byte data[] = new byte[BUFFER_SIZE];
	        for (int i = 0; i < files.length; i++) {
	            FileInputStream fi = new FileInputStream(files[i]);    
	            origin = new BufferedInputStream(fi, BUFFER_SIZE);
	            try {
//	                ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
	                ZipEntry entry = new ZipEntry(files[i].getName());
	                out.putNextEntry(entry);
	                int count;
	                while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
	                    out.write(data, 0, count);
	                }
	            }
	            finally {
	                origin.close();
	            }
	        }
	    }
	    finally {
	        out.close();
	    }
	}
	
	
	
	public static void zipFolder(File srcFolder, String destZipFile) throws Exception {
	    ZipOutputStream zip = null;
	    FileOutputStream fileWriter = null;
	    fileWriter = new FileOutputStream(destZipFile);
	    zip = new ZipOutputStream(fileWriter);
	    if(srcFolder.isDirectory()){
	    	addFolderToZip(null, srcFolder.getPath(), zip);
	    }else{
	    	addFileToZip(null, srcFolder.getPath(),zip);
	    }
	    zip.flush();
	    zip.close();
	}
	
	public static void zipFolder(String srcFolder, String destZipFile) throws Exception {
		zipFolder(new File(srcFolder), destZipFile);
	}

	private static void addFileToZip(String path, String srcFile, ZipOutputStream zip) throws Exception {
	    File folder = new File(srcFile);
	    if (folder.isDirectory()) {
	        addFolderToZip(path, srcFile, zip);
	    } else {
	        byte[] buf = new byte[1024];
	        int len;
	        FileInputStream in = new FileInputStream(srcFile);
	        if(path==null){
	        	zip.putNextEntry(new ZipEntry(folder.getName()));
	        }else{
	        	zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
	        }
	        
	        while ((len = in.read(buf)) > 0) {
	            zip.write(buf, 0, len);
	        }
	    }
	}

	private static void addFolderToZip(String path, String srcFolder,ZipOutputStream zip) throws Exception {
	    File folder = new File(srcFolder);
	    for (String fileName : folder.list()) {
	        if (path==null) {
	            addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
	        } else {
	            addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
	        }
	    }
	}
	
	public static void zip(File[] files, String zipFile) throws IOException {
	    zip(files, new File(zipFile));
	}
	
	public static void zip(String[] filePath, String zipFile) throws IOException {
	    zip(filePath, new File(zipFile));
	}
	
	

	/**
	 * 압출을 해제한다.
	 * @param zipFile
	 * @param location
	 * @throws IOException
	 */
	public static void unZip(File zipFile, File location) throws IOException {
		if(!location.isDirectory()){
			System.out.println("압축을 해제하는 location이 디렉토리가 아닙니다.");
			return;
		}
        if(!location.exists()) {
        	location.mkdirs();
        }
        
        ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
        try {
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
            	File unzipFile = new File(location,ze.getName());
            	if(!unzipFile.getParentFile().exists()) {
            		unzipFile.getParentFile().mkdirs();
                }
                if (ze.isDirectory()) {
                    if(!unzipFile.isDirectory()) {
                    	unzipFile.mkdirs();
                    }
                }
                else {
                    FileOutputStream fout = new FileOutputStream(unzipFile, false);
                    try {
                        for (int c = zin.read(); c != -1; c = zin.read()) {
                            fout.write(c);
                            fout.flush();
                        }
                        zin.closeEntry();
                    }
                    finally {
                        fout.close();
                    }
                }
            }
        }
        finally {
            zin.close();
        }
	}
	
	public static void unZip(File zipFile, String location) throws IOException {
		unZip(zipFile, new File(location));
	}
	
	public static void unZip(String zipFile, File location) throws IOException {
		unZip(new File(zipFile), location);
	}
	
	public static void unZip(String zipFile, String location) throws IOException {
		unZip(new File(zipFile), new File(location));
	}
	
	
	/**
	 * 파일 및 디렉토리를 삭제한다.
	 * @param file
	 * @return
	 */
	public static boolean del(String fileName){
		File file = new File(fileName);
		return del(file);
	}
	
	/**
	 * 파일의 상위경로를 가져온다.
	 * @param filePath
	 * @return
	 */
	public static String getParentPath(String filePath){
		File file = new File(filePath);
		return file.getParentFile().getPath();
	}
	
	/**
	 * 파일명만 가져온다.
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath){
		File file = new File(filePath);
		return file.getName();
	}
	
	/**
	 * 유니크한 파일명을 가져온다.
	 * @return
	 */
	public static String getTempFileName(){
		String path = ""+new Date().getTime();
		return path;
	}

	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public void saveWebFile(String urlName,String fileName) throws MalformedURLException, ProtocolException, FileNotFoundException,IOException {
			WebConnection wc = new WebConnection();	
			InputStream in = wc.getInputStreamGet(urlName);
	        saveFile(in,fileName);
	}
	
	public void saveFile(InputStream in, String fileName) throws FileNotFoundException,IOException  {
		int readCount = 0;
        byte[] buf = new byte[1024];

        FileOutputStream out = context.openFileOutput(fileName,Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE);
	    
        while((readCount=in.read(buf))!=-1){
        	out.write(buf,0,readCount);
		   
        }   
        in.close();
        out.close();

	}
	
	public Drawable getDrawable(String fileName) throws FileNotFoundException,IOException{
		if(fileName==null || fileName.equals("")){
			return null;
		}
		FileInputStream in = getFileInputStream(fileName);
		Drawable d = Drawable.createFromStream(in, "none");
		in.close();
		return d;
	}
	
	public FileInputStream getFileInputStream(String fileName) throws FileNotFoundException{
		if(fileName==null || fileName.equals("")){
			return null;
		}
		FileInputStream in = context.openFileInput(fileName);
		return in;
	}
	
	public View drawView(String fileName,View iv){		
		try {
			if(fileName!=null && !fileName.equals("")){
				Drawable d = getDrawable(fileName);
				if(d==null){
					return null;
				}
				iv.setBackgroundDrawable(d);
				return iv;
			}else{
				return null;
			}
			
		} catch (Exception e) {
			Log.d("에러 ",e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	public View setImgDrawView(String fileName,ImageView iv){		
		try {
			Drawable d = getDrawable(fileName);
			if(d==null){
				return null;
			}
			iv.setImageDrawable(d);
			return iv;
			
		} catch (Exception e) {
			Log.d("에러 ",e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
			
	
}
