package com.markjmind.libox.test;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.markjmind.libox.R;
import com.markjmind.libox.common.Web;
import com.markjmind.uni.UpdateEvent;
import com.markjmind.uni.UpdateListener;
import com.markjmind.uni.Viewer;
import com.markjmind.uni.ViewerBuilder;
import com.markjmind.uni.annotiation.GetView;
import com.markjmind.uni.annotiation.Layout;
import com.markjmind.uni.hub.Store;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

@Layout(R.layout.test2)
public class Test2 extends Viewer {
        @GetView
        TextView data1,data2,data3,data4,data5,data6,data7,data8;

        String ok = null;

        String result;
        Exception error;

        @Override
        public void onBind(int requestCode, ViewerBuilder build) {
            build.setAsync(true)
                    .setPreLayout(true)
                    .setLoadLayout(R.layout.loading, new UpdateListener() {
                        @Override
                        public void onCreate(int requestCode, View loadView) {

                        }

                        @Override
                        public void onUpdate(int requestCode, View loadView, Object value) {

                        }
                    });
        }

        @Override
        public boolean onLoad(int requestCode, UpdateEvent event) throws IOException, InterruptedException {
            Web web = new Web();
            String url = "http://dm-dev.health-on.co.kr:9100/mobile/IF-HLO-DM-0200";

            Store<String> param = new Store();
            param.add("authKey","0bd4ff25ef8f00d3e651fc97123c6f51b807f00f19b43ab9eb3333445efc71f6");
            param.add("placeCd","00303100");
            param.add("search","20130610");
            param.add("reqDate", "20150914114003");

            result = web.post(url,param);
            Thread.sleep(1000);
            return true;
        }


       @Override
        public void onPost(int requestCode) {
            try {
                JSONObject json = new JSONObject(result);
                data1.setText(json.getString("activeStepCnt"));
                data2.setText(json.getString("goalStepCnt"));
                data3.setText(json.getString("stepCntPercent"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFail(Integer requestCode, Exception e) {
            Toast.makeText(getContext(), "실패", Toast.LENGTH_LONG).show();
        }
}
