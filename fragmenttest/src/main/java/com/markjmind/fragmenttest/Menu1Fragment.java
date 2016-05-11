package com.markjmind.fragmenttest;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.markjmind.uni.UniFragment;
import com.markjmind.uni.mapper.annotiation.GetView;
import com.markjmind.uni.mapper.annotiation.Layout;
import com.markjmind.uni.thread.CancelAdapter;
import com.markjmind.uni.thread.LoadEvent;

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
    @GetView
    TextView textView, textView2;

    @Override
    public void onPre() {
        textView.setText("시작");
        textView2.setText("하나더");
    }

    @Override
    public void onLoad(LoadEvent event, CancelAdapter cancelAdapter) throws Exception {
        {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://github.com")
                    .build();
            Response response = client.newCall(request).execute();
            Log.i("DetachedObservable", response.body().string());
        }
        event.update(30);
        {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://nate.com")
                    .build();

            Response response = client.newCall(request).execute();
            Log.d("DetachedObservable", response.body().string());
        }
        event.update(50);
        {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://naver.com")
                    .build();

            Response response = client.newCall(request).execute();
            Log.i("DetachedObservable", response.body().string());
        }
        event.update(70);
        {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://github.com")
                    .build();

            Response response = client.newCall(request).execute();
            Log.i("DetachedObservable", response.body().string());
        }
        event.update(100);
        Log.e("DetachedObservable", "Load 끝");

    }

    @Override
    public void onPost() {
        textView.setText("끝");
        setRefreshBackStack(true);
    }

    @Override
    public void onPostFail(String message, Object arg) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelled(boolean attach) {
        Toast.makeText(getActivity(), "cancel", Toast.LENGTH_SHORT).show();
    }
}
