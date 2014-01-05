package com.markjmind.mobile.api.hub;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public class DBhub {
		
	public StoreList getListToCursor(Cursor cursor){
		StoreList dataList = new StoreList();
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
		    do {
		    	Store store = new Store();
		        for (int pos = 0;pos < cursor.getColumnCount();pos++) {
		            if (cursor.getColumnName(pos) != null) {
		            	try{
		            		store.add(cursor.getColumnName(pos), cursor.getString(pos));
		            	}catch(Exception e){
		            		byte[] datas = cursor.getBlob(pos);
		            		store.add(cursor.getColumnName(pos), datas);
		            	}
		            	
		            }
		        }
		        dataList.add(store);
		    } while (cursor.moveToNext());
		}
		cursor.close();
		return dataList;
	}
	
	public String[] getProviderArray(Class className) throws IllegalAccessException, InstantiationException{
		RawContacts d;
		Field[] fields = className.getFields();
		ArrayList list = new ArrayList();
		
		Uri uri = RawContacts.CONTENT_URI;
		List l = uri.getPathSegments();
		for(int i=0;i<l.size();i++){
			Log.i("DBhub","query:"+l.get(i).toString());
		}
		
		for(int i=0;i<fields.length;i++){
			
			int modifier = fields[i].getModifiers();
			if (Modifier.isPublic(modifier)&& Modifier.isFinal(modifier)&& Modifier.isStatic(modifier) ){// && fields[i].getType() == String.class) {
//				Log.i("DBhub",fields[i].getName()+":"+fields[i].get(null).toString());
				if(((String)(fields[i].get(null))).indexOf(".")<0){
					if(!((String)(fields[i].get(null))).equals("_count")){
						list.add((String)(fields[i].get(null)));
						if(fields[i].getType() == Integer.class){
							Log.i("DBhub",""+i+"  "+fields[i].getName()+":"+String.valueOf((Integer)fields[i].get(null)));
						}else if(fields[i].getType() == Long.class){
							Log.i("DBhub",""+i+"  "+fields[i].getName()+":"+String.valueOf((Long)fields[i].get(null)));
						}else if(fields[i].getType() == String.class){
							Log.i("DBhub",""+i+"  "+fields[i].getName()+":"+(String)fields[i].get(null));
						}
							
						
						
					}
				}
			}
		}

		if(list!=null && list.size()>0){
			String[] tempStrs = new String[list.size()];
			for(int i=0;i<list.size();i++){
				tempStrs[i]=(String)list.get(i);
				
			}
			return tempStrs;
		}else{
			return null;
		}
	}
	

	
	public StoreList query(Context context,Uri CONTENT_URI,Class className,String selection,String[] selectionArgs, String sortOrder) throws IllegalAccessException, InstantiationException{
		Cursor cursor = null;
		cursor = context.getContentResolver()
			.query(CONTENT_URI,getProviderArray(className),
					selection,
					selectionArgs, 
					sortOrder);
		StoreList dataList = getListToCursor(cursor);
		return dataList;
	}
	
	public StoreList query(Context context,Uri CONTENT_URI,String[] projection,String selection,String[] selectionArgs, String sortOrder){
		Cursor cursor = null;
		cursor = context.getContentResolver()
			.query(CONTENT_URI,projection,
					selection,
					selectionArgs, 
					sortOrder);
		StoreList dataList = getListToCursor(cursor);
		return dataList;
	}
	
	
	public int update(Context context,Uri uri, ContentValues values, String where, String[] selectionArgs){
		return context.getContentResolver().update(uri, values, where, selectionArgs);
	}
	
	
	/**
	 * 二쇱쓽!! SQLiteDatabase close�섏� �딆쓬
	 * @param sql
	 * @param sqlDb
	 * @return
	 */
	public StoreList storeList(String sql, SQLiteDatabase sqlDb){
		Cursor cursor = sqlDb.rawQuery(sql, null);
		StoreList list = this.getListToCursor(cursor);
		return list;
	}
	public StoreList storeList(String sql,SQLiteOpenHelper soh){
		SQLiteDatabase db = soh.getReadableDatabase();
		StoreList list = storeList(sql,db);
		return list;
	}
	
	public void update(String sql,SQLiteOpenHelper soh){
		soh.getWritableDatabase().execSQL(sql);
		soh.close();
	}
	
}
