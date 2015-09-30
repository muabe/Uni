package com.markjmind.libox.common;

import com.markjmind.uni.hub.Store;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by codemasta on 2015-09-14.
 */
public class Web {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;
    private Store<String> param;

    private String uri = "http://dm-dev.health-on.co.kr:9100/mobile/";
    public Web()
    {
        client = new OkHttpClient();
        param = new Store<String>();
    }

    String postJson(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }



    public String post(String url, Store<String> store) throws IOException {
        MultipartBuilder mBuilder =  new MultipartBuilder();
        mBuilder.type(MultipartBuilder.FORM);
        String[] keys = store.getKeys();
        for(int i=0;i<keys.length;i++){
            mBuilder.addFormDataPart(keys[i],store.getString(keys[i]));
        }
        RequestBody body = mBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .cacheControl(new CacheControl.Builder().noCache().build())
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String postDM(String api) throws IOException {
        MultipartBuilder mBuilder =  new MultipartBuilder();
        mBuilder.type(MultipartBuilder.FORM);
        String[] keys = param.getKeys();
        for(int i=0;i<keys.length;i++){
            mBuilder.addFormDataPart(keys[i],param.getString(keys[i]));
        }

        String url = uri+api;
        RequestBody body = mBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .cacheControl(new CacheControl.Builder().noCache().build())
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public Web addParam(String key, String value){
        param.add(key, value);
        return this;
    }

    public Web addParam(Store<String> store){
        param.putAll(store);
        return this;
    }
}
