package com.markjmind.fragmenttest;

import android.util.Log;
import android.widget.TextView;

import com.markjmind.uni.UniFragment;
import com.markjmind.uni.mapper.annotiation.GetView;
import com.markjmind.uni.mapper.annotiation.Layout;
import com.markjmind.uni.mapper.annotiation.Param;
import com.markjmind.uni.viewer.UpdateEvent;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */

@Layout(R.layout.menu1_frgt)
public class Menu1Fragment extends UniFragment {
    @Param
    String ok;

    @GetView
    TextView textView, textView2;

    @Override
    public void onPre(int requestCode) {
        textView.setText("시작");
        textView2.setText("하나더");
    }

    @Override
    public void onLoad(int requestCode, UpdateEvent event) throws Exception {


        {
            OkHttpClient client = new OkHttpClient();
            Log.i("dd", "request");
            Request request = new Request.Builder()
                    .url("http://github.com")
                    .build();
            Log.i("dd", "빌드끝");
            Response response = client.newCall(request).execute();
            Log.i("DetachedObservable", response.body().string());
        }

        {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://nate.com")
                    .build();

            Response response = client.newCall(request).execute();
            Log.d("DetachedObservable", response.body().string());
        }

        {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://naver.com")
                    .build();

            Response response = client.newCall(request).execute();
            Log.d("DetachedObservable", response.body().string());
        }

        {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://github.com")
                    .build();

            Response response = client.newCall(request).execute();
            Log.i("DetachedObservable", response.body().string());
        }

        Log.e("DetachedObservable", "Load 끝");

    }

    @Override
    public void onPost(int requestCode) {
        textView.setText("끝");
    }
}
