package com.markjmind.uni.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.markjmind.uni.hub.Store;

public class JwSound{
	Store sound = new Store();
	SoundPool sp;
	Context context;
	MediaPlayer mp=null;
	public JwSound(Context context,int poolCount){
		this.context = context;
		
		sp = new SoundPool(poolCount, AudioManager.STREAM_ALARM, 0);
	}
	
	public void add(String key, int R_raw_id){
		int value = sp.load(context, R_raw_id, 1);
		sound.add(key, value);
	}
	
	public void play(String key){
		int soundKey = (int)sound.getInt(key);
		sp.play(soundKey, 0.3f,0.3f,1,0,1);
	}
	
	public void play(String key,float volume){
		int soundKey = (int)sound.getInt(key);
		sp.play(soundKey, volume,volume,1,0,1);
	}
	
	public void mediaPlay(int R_raw_id){
		 mp = MediaPlayer.create(context,R_raw_id);
         mp.setLooping(false);
         mp.start();
	}
	

}
