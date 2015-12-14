package com.markjmind.libox.login;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.markjmind.libox.MainViewer;
import com.markjmind.libox.R;
import com.markjmind.libox.common.Web;
import com.markjmind.uni.LoadViewListener;
import com.markjmind.uni.UpdateEvent;
import com.markjmind.uni.Viewer;
import com.markjmind.uni.ViewerBuilder;
import com.markjmind.uni.UniAsyncTask;
import com.markjmind.uni.annotiation.GetView;
import com.markjmind.uni.annotiation.Layout;
import com.markjmind.uni.exception.UniLoadFailException;
import com.markjmind.uni.hub.Store;

import java.io.IOException;

/**
 * Created by codemasta on 2015-09-15.
 */
@Layout(R.layout.view_login)
public class LoginViewer extends Viewer {

    @GetView
    private EditText editId,editPass;
    @GetView
    private Button loginBtn;
    @GetView
    private View findIdPwBtn;



    String result=null;
    Store<String> webParam = new Store<String>();

    @Override
    public void onBind(int requestCode, ViewerBuilder build) {
        build.setLoadLayout(R.layout.loading, new LoadViewListener(){

            @Override
            public void loadCreate(int requestCode, View loadView) {
                TextView text = (TextView)loadView.findViewById(R.id.loading_text);
                if(requestCode==1){
                    text.setText("로그인을 확인합니다.");
                }else{
                    text.setText("로딩중..");
                }
            }

            @Override
            public void loadUpdate(int requestCode, View loadView, Object value) {

            }

            @Override
            public void loadDestroy(int requestCode) {

            }
        }).setAsync(true);
    }

    @Override
    public void onPre(int requestCode) {
        super.onPre(requestCode);
    }

    @Override
    public void onLoad(int requestCode, UpdateEvent event) throws IOException, UniLoadFailException {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(requestCode==1) {
            Web web = new Web();
            result = web.addParam(webParam).postDM("IF-HLO-DM-0001");
            webParam.clear();
        }
    }


    @Override
    public void onPost(int requestCode) {
        if(requestCode==1){
            Toast.makeText(getContext(), "로그인 되었습니다.", Toast.LENGTH_SHORT);
            Viewer.build(MainViewer.class, getActivity()).change(this);
        }else{
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webParam.add("loginId", editId.getText().toString())
                            .add("userPw", editPass.getText().toString())
                            .add("reqDate", "20150916095826")
                            .add("registrationId", "");
                    reBuild(1).setEnableLoadLayout(true).onLoad();
                }
            });
            findIdPwBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    excute(new UniAsyncTask() {
                        @Override
                        public void onLoad(int requestCode, UpdateEvent event, Viewer viewer) throws Exception {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onPost(int requestCode, Viewer viewer) {
                            Toast.makeText(getContext(), "하이", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        result=null;
    }

    @Override
    public void onFail(int requestCode, boolean isException, String message, Exception e) {
        if(isException) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
        }
    }
}
