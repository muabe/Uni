package com.markjmind.libox.dietary;

import android.widget.TextView;

import com.markjmind.libox.R;
import com.markjmind.libox.common.Web;
import com.markjmind.uni.UpdateEvent;
import com.markjmind.uni.Viewer;
import com.markjmind.uni.ViewerBuilder;
import com.markjmind.uni.annotiation.GetView;
import com.markjmind.uni.annotiation.Layout;
import com.markjmind.uni.hub.Store;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by codemasta on 2015-09-15.
 */
@Layout(R.layout.view_dietary_main)
public class DietaryViewer extends Viewer {

    @GetView
    TextView textViewTodayTotalKcal;

    Store<String> webParam = new Store<String>();
    JSONObject result;

    @Override
    public void onBind(int requestCode, ViewerBuilder build) {
        webParam.add("authKey", "f9e8eb7ecdc9a0783470f08a169e6f27b90e4628053fa74170721cf898e43e4b")
                .add("search", "20130610")
                .add("reqDate", "20150917132514");
        build.setProgressLayout(R.layout.loading, null);
    }

    @Override
    public void onLoad(int requestCode, UpdateEvent event) throws IOException, JSONException {
        Web web = new Web();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String rep = web.addParam(webParam).postDM("IF-HLO-DM-0300");
        result = new JSONObject(rep);
        webParam.clear();

    }

    @Override
    public void onPost(int requestCode) {
        try {
            textViewTodayTotalKcal.setText(result.getString("encourageKcal"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

//for(int i=0;i<=100;i++) {
//        event.update((i+11));
//        Thread.sleep(50);
//        }


